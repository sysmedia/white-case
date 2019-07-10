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
@Title ("VideoCameraSettingsZPTE")
class VideoCameraSettingsZPTE extends SystemTestSpec {
    static final Logger LOGGER = LoggerFactory.getLogger(VideoCameraSettingsZPTE.class)
    static int index = -1

    @Shared
    White white

    def setupSpec() {
        white = testContext.bookSut(White.class, "SATWin", "1")
    }

    def cleanupSpec() {
        testContext.releaseSut(white)
    }

    def setup() {
        white.stopVideo()
    }

    def cleanup() {
        white.stopVideo()
    }

    def "Select White"() {
        when:
        index = white.getCameraIndex()
        then:
        assert index > -1
    }

    @Unroll
    def "Test camera setting for Zoom"() {
        when:
        LOGGER.info(String.format("Start White by NJPG 360P"))
        white.startVideo("MJPG", "640x360", 30)
        sleep(5000)
        LOGGER.info(LogUtils.imageHtml(white.captureImage()))
        def statistics = white.getStatistics()
        String resolution = statistics.get("resolution")
        def zoomSettings = white.getCameraSetting(3)
        LOGGER.info(String.format("Start White by Zoom Max value about " + zoomSettings.Max))
        white.setCameraSetting(3, zoomSettings.Max)
        sleep(5000)
        def currentZoomSettingsMax = white.getCameraSetting(3)
        LOGGER.info(LogUtils.imageHtml(white.captureImage()))

        then:
        resolution == "640x360"
        currentZoomSettingsMax.Value == zoomSettings.Max

        when:
        def randomValue = (int) (Math.random() * 200+zoomSettings.Min)
        LOGGER.info(String.format("Start White by Zoom random value about " + randomValue))
        white.setCameraSetting(3, randomValue)
        sleep(5000)
        def currentZoomSettingsRandom = white.getCameraSetting(3)
        LOGGER.info(LogUtils.imageHtml(white.captureImage()))

        then:
        currentZoomSettingsRandom.Value == randomValue

        when:
        LOGGER.info(String.format("Start White by Zoom Min value about " + zoomSettings.Min))
        white.setCameraSetting(3, zoomSettings.Min)
        sleep(5000)
        def currentZoomSettingsMin = white.getCameraSetting(3)
        LOGGER.info(LogUtils.imageHtml(white.captureImage()))

        then:
        currentZoomSettingsMin.Value == zoomSettings.Min
        LOGGER.info("Stop White")
        white.stopVideo()
        sleep(5000)
    }

    @Unroll
    def "Test camera setting for Pan"() {
        when:
        LOGGER.info(String.format("Start White by NJPG 360P"))
        white.startVideo("MJPG", "640x360", 30)
        sleep(5000)
        LOGGER.info(LogUtils.imageHtml(white.captureImage()))
        def statistics = white.getStatistics()
        String resolution = statistics.get("resolution")
        def panSettings = white.getCameraSetting(0)
        LOGGER.info(String.format("Start White by Pan min value about " + panSettings.Min))
        white.setCameraSetting(0, panSettings.Min)
        sleep(5000)
        def currentPanSettingsMin = white.getCameraSetting(0)
        LOGGER.info(LogUtils.imageHtml(white.captureImage()))

        then:
        resolution == "640x360"
        currentPanSettingsMin.Value == panSettings.Min

        when:
        def randomValue = (int) (Math.random() * 200+panSettings.Min)
        LOGGER.info(String.format("Start White by Pan random value about " + randomValue))
        white.setCameraSetting(0, randomValue)
        sleep(5000)
        def currentPanSettingsRandom = white.getCameraSetting(0)
        LOGGER.info(LogUtils.imageHtml(white.captureImage()))

        then:
        currentPanSettingsRandom.Value == randomValue

        when:
        LOGGER.info(String.format("Start White by Pan Max value about " + panSettings.Max))
        white.setCameraSetting(0, panSettings.Max)
        sleep(5000)
        def currentPanSettingsMax = white.getCameraSetting(0)
        LOGGER.info(LogUtils.imageHtml(white.captureImage()))

        then:
        currentPanSettingsMax.Value == panSettings.Max
        LOGGER.info("Stop White")
        white.stopVideo()
        sleep(5000)
    }

    @Unroll
    def "Test camera setting for Tilt"() {
        when:
        LOGGER.info(String.format("Start White by NJPG 360P"))
        white.startVideo("MJPG", "640x360", 30)
        sleep(5000)
        LOGGER.info(LogUtils.imageHtml(white.captureImage()))
        def statistics = white.getStatistics()
        String resolution = statistics.get("resolution")
        def tiltSettings = white.getCameraSetting(1)
        LOGGER.info(String.format("Start White by tilt min value about " + tiltSettings.Min))
        white.setCameraSetting(1, tiltSettings.Min)
        sleep(5000)
        def currentTiltSettingsMin = white.getCameraSetting(1)
        LOGGER.info(LogUtils.imageHtml(white.captureImage()))

        then:
        resolution == "640x360"
        currentTiltSettingsMin.Value == tiltSettings.Min

        when:
        def randomValue = (int) (Math.random() * 200+tiltSettings.Min)
        LOGGER.info(String.format("Start White by Tilt random value about " + randomValue))
        white.setCameraSetting(1, randomValue)
        sleep(5000)
        def currentTiltSettingsRandom = white.getCameraSetting(1)
        LOGGER.info(LogUtils.imageHtml(white.captureImage()))

        then:
        currentTiltSettingsRandom.Value == randomValue

        when:
        LOGGER.info(String.format("Start White by Tilt Max value about " + tiltSettings.Max))
        white.setCameraSetting(1, tiltSettings.Max)
        sleep(5000)
        def currentTiltSettingsMax = white.getCameraSetting(1)
        LOGGER.info(LogUtils.imageHtml(white.captureImage()))

        then:
        currentTiltSettingsMax.Value == tiltSettings.Max
        LOGGER.info("Stop White")
        white.stopVideo()
        sleep(5000)
    }

    @Unroll
    def "Test camera setting for Exposure"() {
        when:
        LOGGER.info(String.format("Start White by NJPG 360P"))
        white.startVideo("MJPG", "640x360", 30)
        sleep(5000)
        LOGGER.info(LogUtils.imageHtml(white.captureImage()))
        def statistics = white.getStatistics()
        String resolution = statistics.get("resolution")
        def exposureSettings = white.getCameraSetting(4)
        LOGGER.info(String.format("Start White by Exposure min value about " + exposureSettings.Min))
        white.setCameraSetting(4, exposureSettings.Min)
        sleep(5000)
        def currentExposureSettingsMin = white.getCameraSetting(4)
        LOGGER.info(LogUtils.imageHtml(white.captureImage()))

        then:
        resolution == "640x360"
        currentExposureSettingsMin.Value == exposureSettings.Min

        when:
        def randomValue = (int) (Math.random() * (exposureSettings.Max - exposureSettings.Min))+exposureSettings.Min
        LOGGER.info(String.format("Start White by Exposure random value about " + randomValue))
        white.setCameraSetting(4, randomValue)
        sleep(5000)
        def currentExposureSettingsRandom = white.getCameraSetting(4)
        LOGGER.info(LogUtils.imageHtml(white.captureImage()))

        then:
        currentExposureSettingsRandom.Value == randomValue

        when:
        LOGGER.info(String.format("Start White by Exposure Max value about " + exposureSettings.Max))
        white.setCameraSetting(4, exposureSettings.Max)
        sleep(5000)
        def currentExposureSettingsMax = white.getCameraSetting(4)
        LOGGER.info(LogUtils.imageHtml(white.captureImage()))

        then:
        currentExposureSettingsMax.Value == exposureSettings.Max
        LOGGER.info("Stop White")
        white.stopVideo()
        sleep(5000)
    }
}
