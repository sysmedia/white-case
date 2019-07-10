package com.polycom.honeycomb.white.test.Video


import com.polycom.honeycomb.SystemTestSpec
import com.polycom.honeycomb.White
import com.polycom.honeycomb.log.LogUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Shared
import spock.lang.Title
import spock.lang.Unroll

@Title ("VideoResolutionNV121024X576 Case")
class VideoResolutionNV121024X576 extends SystemTestSpec{
    static final Logger LOGGER = LoggerFactory.getLogger(VideoResolutionNV121024X576.class)
    static int index = -1
    //static final String whiteName = "Polycom White Video"

    @Shared
    White white

    def setupSpec () {
        white = testContext.bookSut(White.class, "SATMac", "1")
    }

    def cleanupSpec () {
        testContext.releaseSut(white)
    }

    def setup () {
        white.stopVideo()
    }

    def cleanup () {
        white.stopVideo()
    }

    def compareRate (List<Integer> rateSample, Float expectRate) {
        LOGGER.info("Expect Rate: " + expectRate)
        for (int sample : rateSample) {
            LOGGER.info("Sample Rate: " + sample)
            if (sample >= expectRate*0.8 && sample <= expectRate*1.2) {
                return true
            }
        }
        return false
    }

    def "Select White" () {
        when:
        index = white.getCameraIndex()
        then:
        assert index > -1

        when:
        white.select()
        then:
        noExceptionThrown()
    }

    @Unroll
    def "Format -- NV12_Resolution -- 1024x576_Rate -- #rate" (Float rate) {
        when:
        LOGGER.info(String.format("Start White by rate %.1f",rate))
        white.startVideo("NV12", "1024x576", (int) rate)
        sleep(5000)
        LOGGER.info(LogUtils.imageHtml(white.captureImage()))
        List<Integer> actualRates = new ArrayList<>()
        String resolution = ""
        for (int i = 0; i < 3; i++) {
            sleep(2000)
            def statistics = white.getStatistics()
            actualRates.add(statistics.get("frameRate") as Integer)
            resolution = statistics.get("resolution")
        }
        LOGGER.info("Stop White")
        white.stopVideo()
        sleep(2000)
        then:
        noExceptionThrown()
        resolution == "1024x576"
        compareRate(actualRates, rate)
        where:
        rate << white.getSupportedFrameRates("NV12", "1024x576")

    }

}
