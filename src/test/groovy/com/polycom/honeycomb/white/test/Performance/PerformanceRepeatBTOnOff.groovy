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
class PerformanceRepeatBTOnOff extends SystemTestSpec {
    private static final Logger logger = LoggerFactory.getLogger(PerformanceRepeatBTOnOff.class)
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

    def "Turn off and on Bluetooth for White"() {
        when:
        logger.info("===================Turn off Bluetooth for White===================")
        white_performance_win.setBlueToothOnOff(false, false, false)
        sleep(2000)
        def status_off = white_performance_win.getBlueToothInfo()

        then:
        noExceptionThrown()
        assert status_off.enable == "false"
        assert status_off.ble_enable == "false"
        assert status_off.autopair_enable == "false"

        when:
        logger.info("===================Turn on Bluetooth for White===================")
        white_performance_win.setBlueToothOnOff(true, true, true)
        sleep(2000)
        def status_on = white_performance_win.getBlueToothInfo()

        then:
        noExceptionThrown()
        assert status_on.enable == "true"
        assert status_on.ble_enable == "true"
        assert status_on.autopair_enable == "true"
    }

}