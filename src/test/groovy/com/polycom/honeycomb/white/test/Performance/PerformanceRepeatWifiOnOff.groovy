package com.polycom.honeycomb.white.test.Performance

import com.polycom.honeycomb.SystemTestSpec
import com.polycom.honeycomb.White
import com.polycom.honeycomb.test.logCollecting.CollectSutLogIfFailed
import com.polycom.honeycomb.test.performance.Performance
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by Genie Yan on 9/3/2018.
 */
@CollectSutLogIfFailed
@Performance(runTimes = 20)
class PerformanceRepeatWifiOnOff extends SystemTestSpec {
    private static final Logger logger = LoggerFactory.getLogger(PerformanceRepeatWifiOnOff.class)
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

    def "Turn off and on Wifi for White"() {
        when:
        logger.info("===================Turn off Wifi for White===================")
        white_performance_win.wifiDisable()
        sleep(1000)

        then:
        noExceptionThrown()
        assert !white_performance_win.getWifiStatus().wifi_enable

        when:
        logger.info("===================Turn on Wifi for White===================")
        white_performance_win.wifiEnable()
        sleep(2000)

        then:
        noExceptionThrown()
        assert white_performance_win.getWifiStatus().wifi_enable
    }

}