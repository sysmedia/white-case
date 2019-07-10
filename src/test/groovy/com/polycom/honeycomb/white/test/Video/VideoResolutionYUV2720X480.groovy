package com.polycom.honeycomb.white.test.Video


import com.polycom.honeycomb.SystemTestSpec
import com.polycom.honeycomb.White
import com.polycom.honeycomb.log.LogUtils
import io.restassured.path.json.JsonPath
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Shared
import spock.lang.Title
import spock.lang.Unroll
/**
 * Debug by Andy.Lin on 3/12/2019.
 */
@Title("VideoResolutionYUV2720x480 case")
class VideoResolutionYUV2720X480 extends SystemTestSpec{
    static final Logger LOGGER = LoggerFactory.getLogger(VideoResolutionYUV2720X480.class)
    static int indexWin = -1
    static int indexMac = -1

    @Shared
    White whiteWin
    @Shared
    White whiteMac

    def setupSpec() {
        whiteWin = testContext.bookSut(White.class, "SATWin", "1")
        whiteMac = testContext.bookSut(White.class, "SATMac", "1")
    }

    def cleanupSpec() {
        testContext.releaseSut(whiteWin)
        testContext.releaseSut(whiteMac)
    }

    def setup() {
        whiteWin.stopVideo()
        whiteMac.stopVideo()
    }

    def cleanup() {
        whiteWin.stopVideo()
        whiteMac.stopVideo()
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
        indexMac = whiteMac.getCameraIndex()

        then:
        assert indexWin > -1
        assert indexMac > -1

        when:
        whiteWin.select()
        whiteMac.select()

        then:
        noExceptionThrown()
    }

    @Unroll
    def "Format -- YUV2_Resolution -- 720x480_Rate -- #rate" (Float rate) {
        when:
        LOGGER.info(String.format("Start White by rate %.1f", rate))
        whiteWin.startVideo("YUY2", "720x480", (int) rate)
        whiteMac.startVideo("YUY2", "720x480", (int) rate)
        sleep(5000)
        LOGGER.info(LogUtils.imageHtml(whiteWin.captureImage()))
        LOGGER.info(LogUtils.imageHtml(whiteMac.captureImage()))
        List<Integer> actualRatesWin = new ArrayList<>()
        String resolutionWin = ""
        List<Integer> actualRatesMac = new ArrayList<>()
        String resolutionMac = ""
        for (int i = 0; i < 3; i++) {
            sleep(2000)
            def statisticsWin = whiteWin.getStatistics()
            actualRatesWin.add(statisticsWin.get("frameRate") as Integer)
            resolutionWin = statisticsWin.get("resolution")
            def statisticsMac = whiteMac.getStatistics()
            actualRatesMac.add(statisticsMac.get("frameRate") as Integer)
            resolutionMac = statisticsMac.get("resolution")
        }
        LOGGER.info("Stop White")
        whiteWin.stopVideo()
        whiteMac.stopVideo()
        sleep(2000)

        then:
        noExceptionThrown()
        resolutionWin == "720x480"
        resolutionMac == "720x480"
        compareRate(actualRatesWin, rate)
        compareRate(actualRatesMac, rate)

        where:
        rate << whiteWin.getSupportedFrameRates( "YUY2", "720x480")
    }
}

