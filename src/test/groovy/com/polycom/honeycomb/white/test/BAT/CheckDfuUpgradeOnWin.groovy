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
class CheckDfuUpgradeOnWin extends SystemTestSpec {
    private static final Logger logger = LoggerFactory.getLogger(CheckDfuUpgradeOnWin.class)

    private static White white_win_1 = null
    private static White white_win_2 = null

    def setupSpec() {
        boolean setupCompleted = false
        try {
            white_win_1 = testContext.bookSut(White.class, "BAT", "Win", "1")
            white_win_2 = testContext.bookSut(White.class, "SATWin", "2")
            setupCompleted = true
        } catch (Exception ignored) {
            logger.error("failed to setup test environment, case is quited!!!")
        }

        assert setupCompleted
    }

    def cleanupSpec() {
        try { if (white_win_2 != null) testContext.releaseSut(white_win_2) } catch (Exception e) {
            logger.error(String.format("failed to release White(%s)!", white_win_2.getIp()), e)
        }
        try { if (white_win_1 != null) testContext.releaseSut(white_win_1) } catch (Exception e) {
            logger.error(String.format("failed to release White(%s)!", white_win_1.getIp()), e)
        }
    }

    def "Check white DFU Upgrade to latest build"() {
        when:
        logger.info("===================DFU Upgrading===================")
        String latestVersionOnServer = white_win_1.startDfuUpgradeToLatest()
        white_win_2.startDfuUpgradeToLatest()
        logger.info("White is upgrading to latest version: " + latestVersionOnServer)
        // wait 8 mins at most
        for (int i = 1; i <= 16; i++) {
            sleep(30000)
            String status1 = white_win_1.getDfuUpgradeResult()
            String status2 = white_win_2.getDfuUpgradeResult()
            logger.info(String.format("%s upgrade status is %s after %ds", white_win_1.getIp(), status1, 30*i))
            if (status1 == "success" && status2 == "success") {
                break
            }
        }
        logger.info("Wait 2 min for white reboot")
        sleep(120000)
        logger.info("===================DFU Upgrade Done===================")

        then:
        noExceptionThrown()
        //latestVersionOnServer == white_win_1.getVersion()
    }

    def "Check white to be detected by PC"() {
        expect:
        logger.info("===================Check white to be detected after upgrade===================")
        assert white_win_1.isDetectedByPC()
    }

    def "Check white to be detected by DFU"() {
        expect:
        logger.info("===================DFU Detection===================")
        assert white_win_1.isDetectedByDfu()
    }
}
