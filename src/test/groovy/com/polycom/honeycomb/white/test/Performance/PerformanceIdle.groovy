package com.polycom.honeycomb.white.test.Performance

import com.polycom.honeycomb.RpdWin
import com.polycom.honeycomb.SystemTestSpec
import com.polycom.honeycomb.White
import com.polycom.honeycomb.log.LogUtils
import com.polycom.honeycomb.mediastatistics.CallType
import com.polycom.honeycomb.test.performance.PerfDataProbe
import com.polycom.honeycomb.test.performance.Performance
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Shared

//@CollectSutLog
@Performance
class PerformanceIdle extends SystemTestSpec{
    private static final Logger logger = LoggerFactory.getLogger(PerformanceIdle.class)

    String DialNumber = "76969665@172.21.113.57"

    @Shared
    PerfDataProbe probe

    @Shared
    White white

    @Shared
    RpdWin rpdWin

    def setupSpec() {
        white = testContext.bookSut(White.class, "BAT", "Win", "2")
        probe = testContext.addPerfDataProbe(white, 30*1000)
        white.enableTalkerTracking()
        white.getPc().rest().rpdQuit()
        sleep(3000)
        white.getPc().rest().rpdStart()
        sleep(20000)
        rpdWin = testContext.bookSut(RpdWin.class, "RPDWin")
    }

    def cleanupSpec() {
        testContext.releaseSut(white)
        testContext.releaseSut(rpdWin)
        white.getPc().rest().rpdQuit()
    }

    def "Idle with preview"() {
        when:
        logger.info("Start RPD to launch White preview")
        white.getPc().rest().rpdStart()
        logger.info("Idle with preview last 6 hours")
        sleep(60*1000*360)
//        sleep(10*1000)
        logger.info(LogUtils.imageHtml(white.screenshot()))

        and:
        logger.info("Check if White is core dump")
        boolean result = !white.isCoreDumpExisted()

        then:
        assert result
    }

    def "Idle without preview"() {
        when:
        logger.info("Close RPD to exit White preview")
        white.getPc().rest().rpdQuit()
        logger.info("Idle without preview last 6 hours")
        sleep(60*1000*360)
//        sleep(10*1000)
        logger.info(LogUtils.imageHtml(white.screenshot()))

        and:
        logger.info("Check if White is core dump")
        boolean result = white.adb().collectLogAndCoreDump()

        then:
        assert result
    }

    def "Place RPD call"() {
        when:
        logger.info("Start RPD to place call")
        white.getPc().rest().rpdStart()
        sleep(20000)
        rpdWin.placeCall(DialNumber, CallType.SIP, 1920)
        sleep(10*1000)
        logger.info(LogUtils.imageHtml(white.screenshot()))

        then:
        assert rpdWin.getCallStatus() =~ 'connected'

        when:
        rpdWin.hangUp()
        sleep(2000)

        then:
        assert rpdWin.getCallStatus() =~ 'no call session'
    }
}