package com.polycom.honeycomb.white.test.Performance

import com.polycom.honeycomb.SystemTestSpec
import com.polycom.honeycomb.White
import com.polycom.honeycomb.test.logCollecting.CollectSutLogIfFailed
import com.polycom.honeycomb.test.performance.Performance
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by Genie Yan on 9/4/2018.
 */
@CollectSutLogIfFailed
@Performance(runTimes = 20)
class PerformanceRepeatFaceDetectionOnOff extends SystemTestSpec {
    private static final Logger logger = LoggerFactory.getLogger(PerformanceRepeatFaceDetectionOnOff.class)
    private static White white_performance_win = null

    def setupSpec() {
        boolean setupCompleted = false
        try {
            white_performance_win = testContext.bookSut(White.class, "Performance", "Win")
            setupCompleted = true
        } catch (Exception ignored) {
            logger.error("failed to setup test environment, case is quited!!!")
        }

        assert setupCompleted
    }

    def cleanupSpec() {
        try {
            if (white_performance_win != null) testContext.releaseSut(white_performance_win)
        } catch (Exception e) {
            logger.error(String.format("failed to release White(%s)!", white_performance_win.getIp()), e)
        }
    }

    def "Turn on and off face detection for White"() {
        when:
        logger.info("===================Turn on face detection for White===================")
        white_performance_win.enableTalkerTracking()
        sleep(2000)

        then:
        noExceptionThrown()
        assert white_performance_win.getCameraInfo().tracking_info.auto_tracking == "true"

        when:
        logger.info("===================Turn off face detection for White===================")
        white_performance_win.disableTalkerTracking()
        sleep(2000)

        then:
        noExceptionThrown()
        assert white_performance_win.getCameraInfo().tracking_info.auto_tracking == "false"
    }

}