package com.polycom.honeycomb.white.test.Performance

import com.polycom.honeycomb.SystemTestSpec
import com.polycom.honeycomb.White
import com.polycom.honeycomb.test.logCollecting.CollectSutLogIfFailed
import com.polycom.honeycomb.test.performance.Performance
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by Genie Yan on 8/24/2018.
 */
@CollectSutLogIfFailed
@Performance(runTimes = 20)
class PerformanceRepeatRestartOnMac extends SystemTestSpec {
    private static final Logger logger = LoggerFactory.getLogger(PerformanceRepeatRestartOnMac.class)

    private static White white_performance_mac = null

    def setupSpec() {
        boolean setupCompleted = false
        try {
            white_performance_mac = testContext.bookSut(White.class, "Performance", "Mac")
            setupCompleted = true
        } catch (Exception ignored) {
            logger.error("failed to setup test environment, case is quited!!!")
        }

        assert setupCompleted
    }

    def cleanupSpec() {
        try {
            if (white_performance_mac != null) testContext.releaseSut(white_performance_mac)
        } catch (Exception e) {
            logger.error(String.format("failed to release White(%s)!", white_performance_mac.getIp()), e)
        }
    }

    def "Restart White on Mac"() {
        when:
        logger.info("===================Restart White on Mac===================")
        white_performance_mac.reboot()
        sleep(2000)

        then:
        assert !white_performance_mac.isDetectedByPC()

        when:
        logger.info("===================Wait White on Mac startup===================")
        sleep(50000) // Wait White startup
        logger.info("<<<<<<<<<<<<<<< White Restarted>>>>>>>>>>>>>>>>>")

        then:
        assert white_performance_mac.isDetectedByPC()
    }

}
