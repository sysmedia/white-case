package com.polycom.honeycomb.white.test.Video.USB30

import com.polycom.honeycomb.SystemTestSpec
import com.polycom.honeycomb.White
import com.polycom.honeycomb.log.LogUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Shared
import spock.lang.Title
import spock.lang.Unroll

@Title ("USB30_MAC_YUY2_352x288")
class USB30_MAC_MJPEG_352x288 extends SystemTestSpec{
    static final Logger LOGGER = LoggerFactory.getLogger(USB30_MAC_MJPEG_352x288.class)
    static int indexMac = -1

    @Shared
    White whiteMac

    def setupSpec() {
        whiteMac = testContext.bookSut(White.class, "SATMac", "2")
    }

    def cleanupSpec() {
        if (whiteMac != null) testContext.releaseSut(whiteMac)
    }

    def setup() {
        whiteMac.stopVideo()
    }

    def cleanup() {
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
        indexMac = whiteMac.getCameraIndex()

        then:
        assert indexMac > -1

        when:
        whiteMac.select()

        then:
        noExceptionThrown()
    }

    @Unroll
    def "Format -- MJPEG_Resolution -- 352x288_Rate -- #rate" (Float rate) {
        when:
        LOGGER.info(String.format("Start White by rate %.1f", rate))
        whiteMac.startVideo("MJPG", "352x288", (int) rate)
        sleep(5000)
        //LOGGER.info(LogUtils.imageHtml(whiteMac.captureImage()))
        List<Integer> actualRatesMac = new ArrayList<>()
        String resolutionMac = ""
        for (int i = 0; i < 3; i++) {
            sleep(2000)
            def statisticsMac = whiteMac.getStatistics()
            actualRatesMac.add(statisticsMac.get("frameRate") as Integer)
            resolutionMac = statisticsMac.get("resolution")
        }

        LOGGER.info("Stop White")
        whiteMac.stopVideo()
        sleep(2000)

        def expectedRes = "352x288"
        if (resolutionMac != expectedRes || !compareRate(actualRatesMac, rate))
            LOGGER.info(LogUtils.imageHtml(whiteMac.captureImage()))

        then:
        noExceptionThrown()
        resolutionMac == "352x288"
        compareRate(actualRatesMac, rate)

        where:
        rate << whiteMac.getSupportedFrameRates( "MJPG", "352x288")
    }
}
