package com.polycom.honeycomb.white.test.Performance

import com.polycom.honeycomb.LyncClientWin
import com.polycom.honeycomb.RpdWin
import com.polycom.honeycomb.SystemTestSpec
import com.polycom.honeycomb.White
import com.polycom.honeycomb.log.LogUtils
import com.polycom.honeycomb.mediastatistics.CallType
import groovy.time.TimeCategory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Shared

class PerformanceLongevity extends SystemTestSpec{
    private static final Logger logger = LoggerFactory.getLogger(PerformanceLongevity.class)

    String dialNumber = "76969665@172.21.113.57"
    int duration = 7 * 24 * 3600 // one week

    Random random = new Random()

    @Shared
    White white

    @Shared
    RpdWin rpdWin

    @Shared
    LyncClientWin sfbWin

    def setupSpec() {
        white = testContext.bookSut(White.class, "Longevity")
        sfbWin = testContext.bookSut(LyncClientWin.class, "Longevity")
        white.getPc().rest().rpdQuit()
        sleep(3000)
        white.getPc().rest().rpdStart()
        white.enableTalkerTracking()
        sleep(20000)
        rpdWin = testContext.bookSut(RpdWin.class, "Longevity")
    }

    def cleanupSpec() {
        rpdWin.hangUp()
        sfbWin.hangUp()
        white.getPc().rest().rpdQuit()
        testContext.releaseSut(white)
        testContext.releaseSut(rpdWin)
        testContext.releaseSut(sfbWin)
    }

    def setup() {
    }

    def cleanup() {
    }

    def "Longevity start"() {
        def timeStart = new Date()
        def timeStop = new Date()
        def round = 1
        def rpdCall = 0
        def sfbCall = 0
        def cpuInfo = []
        def memoInfo = []
        when:
        while(TimeCategory.minus(timeStop, timeStart).seconds < duration) {
            logger.info(String.format("Round %d start at %s", round++, timeStop.toString()))
            white.getPc().rest().rpdQuit()
            def dice = random.nextInt(10)
            def runMinutes = random.nextInt(60) + 60
            if (dice == 0) {
                logger.info(String.format("This round: Sfb call, duration: %d minutes", runMinutes))
                sfbCall++
                try {
                    sfbWin.hangUp()
                    sleep(5 * 1000)
                    sfbWin.startMeeting()
                    sleep(60 * 1000)
                    sfbWin.startLocalVideo()
                    sleep(180 * 1000)
                    1.upto(runMinutes / 5) {
                        assert sfbWin.isAdWorking("White") && sfbWin.isVdWorking("White")
                        logger.info(LogUtils.imageHtml(white.screenshot()))
                        if (!sfbWin.isInCall()) {
                            logger.info("Call is disconnected, redial")
                            sfbWin.startMeeting()
                            sleep(60 * 1000)
                            sfbWin.startLocalVideo()
                        }
                        sleep(300 * 1000)
                    }
                    cpuInfo << white.getCpuUsage()
                    memoInfo << white.getMemUsage()
                    sfbWin.hangUp()
                } catch (Exception e) {
                    logger.error(e.getMessage())
                }
            } else if (dice == 1) {
                logger.info(String.format("This round: Rpd call, duration: %d minutes", runMinutes))
                rpdCall++
                try {
                    sleep(10 * 1000)
                    white.getPc().rest().rpdStart()
                    sleep(60 * 1000)
                    rpdWin.placeCall(dialNumber, CallType.SIP, 1920)
                    1.upto(runMinutes / 5) {
                        sleep(300 * 1000)
                        logger.info(LogUtils.imageHtml(white.screenshot()))
                        if (!(rpdWin.getCallStatus() =~ 'connected')) {
                            logger.info("Call is disconnected, redial")
                            rpdWin.placeCall(dialNumber, CallType.SIP, 1920)
                        }
                    }
                    cpuInfo << white.getCpuUsage()
                    memoInfo << white.getMemUsage()
                    rpdWin.hangUp()
                } catch (Exception e) {
                    logger.error(e.getMessage())
                }
            } else {
                logger.info(String.format("This round: Idle, duration: %d minutes", runMinutes))
                sleep(1000 * 60 * runMinutes)
            }

            logger.info(cpuInfo.toString())
            logger.info(memoInfo.toString())

            logger.info("Check if White is core dump")
            assert !white.isCoreDumpExisted()
            timeStop = new Date()
        }
        logger.info(String.format("Test end, Sfb Call: %d, Rpd Call: %d", sfbCall, rpdCall))

        then:
        noExceptionThrown()
    }
}
