package com.polycom.honeycomb.white.test.BAT

import com.polycom.honeycomb.SystemTestSpec
import com.polycom.honeycomb.White
import com.polycom.honeycomb.test.logCollecting.CollectSutLogIfFailed
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by thzhang on 1/14/2019.
 */
@CollectSutLogIfFailed
class CheckDetectionAfterPlugUnplugOnWin extends SystemTestSpec {
    private static final Logger logger = LoggerFactory.getLogger(CheckDetectionAfterPlugUnplugOnWin.class)

    private static White white_win_1 = null

    def setupSpec() {
        boolean setupCompleted = false
        try {
            white_win_1 = testContext.bookSut(White.class, "BAT", "Win", "1")
            setupCompleted = true
        } catch (Exception ignored) {
            logger.error("failed to setup test environment, case is quited!!!")
        }

        assert setupCompleted
    }

    def cleanupSpec() {
        try { if (white_win_1 != null) testContext.releaseSut(white_win_1) } catch (Exception e) {
            logger.error(String.format("failed to release White(%s)!", white_win_1.getIp()), e)
        }
    }

    def "Check white to be detected after plugging to PC"() {
        when:
        logger.info("===================check white to be detected after plug to PC===================")
        for (int i=0; i<3 && !white_win_1.isDetectedByPC(); i++) {
            white_win_1.plugToPC()
            sleep(6000)
        }

        then:
        assert white_win_1.isDetectedByPC()
    }

    def "Check white to be removed after unplugging from PC"() {
        when:
        logger.info("===================check white to be removed after unplug from PC===================")
        white_win_1.unplugFromPC()
        sleep(2000)

        then:
        assert !white_win_1.isDetectedByPC()
    }

    def "Check white to be detected after plugging to PC again"() {
        when:
        logger.info("===================check white to be detected after plug to PC===================")
        for (int i=0; i<3 && !white_win_1.isDetectedByPC(); i++) {
            white_win_1.plugToPC()
            sleep(6000)
        }

        then:
        assert white_win_1.isDetectedByPC()
    }
}
