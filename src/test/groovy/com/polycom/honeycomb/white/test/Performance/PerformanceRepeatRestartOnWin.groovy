package com.polycom.honeycomb.white.test.Performance

import com.polycom.honeycomb.SystemTestSpec
import com.polycom.honeycomb.White
import com.polycom.honeycomb.test.logCollecting.CollectSutLogIfFailed
import com.polycom.honeycomb.test.performance.Performance
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by Genie Yan on 8/22/2018.
 */
@CollectSutLogIfFailed
@Performance(runTimes = 20)
class PerformanceRepeatRestartOnWin extends SystemTestSpec {
    private static final Logger logger = LoggerFactory.getLogger(PerformanceRepeatRestartOnWin.class)

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

    def "Restart White on Win"() {
        when:
        logger.info("===================Restart White on Win===================")
        white_performance_win.reboot()
        sleep(2000)

        then:
        assert !white_performance_win.isDetectedByPC()

        when:
        logger.info("===================Wait White on Win startup===================")
        sleep(100000) // Wait White startup
        logger.info("<<<<<<<<<<<<<<< White Restarted>>>>>>>>>>>>>>>>>")

        then:
        assert white_performance_win.isDetectedByPC()
    }

}
