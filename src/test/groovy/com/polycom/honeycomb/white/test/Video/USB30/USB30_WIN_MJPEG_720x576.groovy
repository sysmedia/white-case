package com.polycom.honeycomb.white.test.Video.USB30


import com.polycom.honeycomb.SystemTestSpec
import com.polycom.honeycomb.White
import com.polycom.honeycomb.log.LogUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Shared
import spock.lang.Title
import spock.lang.Unroll


@Title ("USB30_WIN_MJPEG_720x576")
class USB30_WIN_MJPEG_720x576 extends SystemTestSpec{
    static final Logger LOGGER = LoggerFactory.getLogger(USB30_WIN_MJPEG_720x576.class)
    static int indexWin = -1
//    static int indexMac = -1

    @Shared
    White whiteWin
//    @Shared
//    White whiteMac

    def setupSpec() {
        whiteWin = testContext.bookSut(White.class, "SATWin", "2")
//        whiteMac = testContext.bookSut(White.class, "SATMac", "2")
    }

    def cleanupSpec() {
        if (whiteWin != null) testContext.releaseSut(whiteWin)
//        testContext.releaseSut(whiteMac)
    }

    def setup() {
        whiteWin.stopVideo()
//        whiteMac.stopVideo()
    }

    def cleanup() {
        whiteWin.stopVideo()
//        whiteMac.stopVideo()
    }

    def compareRate(List <Integer> rateSample, Float expectRate) {
        LOGGER.info("Expected Rate: " + expectRate)
        for (int sample:rateSample) {
            LOGGER.info("Sample Rate: "+ sample)
            if (sample >= expectRate * 0.8 && sample <= expectRate * 1.2) {
                return true
            }
        }
        return false
    }

    def "Select White"() {
        when:
        indexWin = whiteWin.getCameraIndex()
//        indexMac = whiteMac.getCameraIndex()

        then:
        assert indexWin > -1
//        assert indexMac > -1

        when:
        whiteWin.select()
//        whiteMac.select()

        then:
        noExceptionThrown()
    }

    @Unroll
    def "Format -- MJPEG_Resolution -- 720x576_Rate -- #rate" (Float rate) {
        when:
        LOGGER.info(String.format("Start White by rate %.1f", rate))
        whiteWin.startVideo("MJPG", "720x576", (int) rate)
//        whiteMac.startVideo("MJPG", "720x576", (int) rate)
        sleep(5000)
        //LOGGER.info(LogUtils.imageHtml(whiteWin.captureImage()))
//        //LOGGER.info(LogUtils.imageHtml(whiteMac.captureImage()))
        List<Integer> actualRatesWin = new ArrayList<>()
        String resolutionWin = ""
        List<Integer> actualRatesMac = new ArrayList<>()
        String resolutionMac = ""
        for (int i = 0; i < 3; i++) {
            sleep(2000)
            def statisticsWin = whiteWin.getStatistics()
            actualRatesWin.add(statisticsWin.get("frameRate") as Integer)
            resolutionWin = statisticsWin.get("resolution")
//            def statisticsMac = whiteMac.getStatistics()
//            actualRatesMac.add(statisticsMac.get("frameRate") as Integer)
//            resolutionMac = statisticsMac.get("resolution")
        }
        LOGGER.info("Stop White")
        whiteWin.stopVideo()
//        whiteMac.stopVideo()
        sleep(2000)

        then:
        noExceptionThrown()
        resolutionWin == "720x576"
//        resolutionMac == "720x576"
        compareRate(actualRatesWin, rate)
//        compareRate(actualRatesMac, rate)

        where:
        rate << whiteWin.getSupportedFrameRates( "MJPG", "720x576")
    }
}
