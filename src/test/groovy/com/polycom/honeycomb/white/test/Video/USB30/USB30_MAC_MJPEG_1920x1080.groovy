package com.polycom.honeycomb.white.test.Video.USB30

import com.polycom.honeycomb.SystemTestSpec
import com.polycom.honeycomb.White
import com.polycom.honeycomb.log.LogUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Shared
import spock.lang.Title
import spock.lang.Unroll

@Title ("USB30_MAC_MJPEG_1920x1080")
class USB30_MAC_MJPEG_1920x1080 extends SystemTestSpec{
    static final Logger LOGGER = LoggerFactory.getLogger(USB30_MAC_MJPEG_1920x1080.class)
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
    def "Format -- MJPEG_Resolution -- 1920x1080_Rate -- #rate" (Float rate) {
        when:
        LOGGER.info(String.format("Start White by rate %.1f", rate))
        whiteMac.startVideo("MJPG", "1920x1080", (int) rate)
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

        then:
        noExceptionThrown()
        resolutionMac == "1920x1080"
        compareRate(actualRatesMac, rate)

        where:
        rate << whiteMac.getSupportedFrameRates( "MJPG", "1920x1080")
    }
}
