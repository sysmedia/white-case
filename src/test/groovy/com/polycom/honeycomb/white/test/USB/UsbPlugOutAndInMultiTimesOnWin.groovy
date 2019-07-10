package com.polycom.honeycomb.white.test.USB

import com.polycom.honeycomb.LyncClientWin
import com.polycom.honeycomb.SystemTestSpec
import com.polycom.honeycomb.White
import com.polycom.honeycomb.log.LogUtils
import com.polycom.honeycomb.test.logCollecting.CollectSutLogIfFailed
import com.polycom.honeycomb.test.performance.Performance
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Shared

/**
 * Created by qxu on 7/25/2018.
 */
@Performance(runTimes = 20)
class UsbPlugOutAndInMultiTimesOnWin extends SystemTestSpec{
    private static final Logger logger = LoggerFactory.getLogger(UsbPlugOutAndInMultiTimesOnWin.class)
    @Shared
    LyncClientWin lync

    private static White white =null

    def setupSpec() {
        boolean setupCompleted = false
        try {
            lync = testContext.bookSut(LyncClientWin.class, "SATWin", "1")
            white = testContext.bookSut(White.class, "SATWin", "1")
            setupCompleted = true
        } catch (Exception ignored) {
            logger.error("failed to setup test environment, case is quited!!!")
        }

        assert setupCompleted
    }

    def cleanupSpec() {
        try { if (white != null) {
            testContext.releaseSut(white)
        }
        } catch (Exception e) {
            logger.error(String.format("failed to release White(%s)!", white.getIp()), e)
        }
        try { if (lync != null) testContext.releaseSut(lync)
        } catch (Exception e) {
            logger.error(String.format("failed to release lync(%s)!", lync.getIp()), e)
        }
    }

    def "Check white to be detected by Skype after re-plugging to PC"() {
        when:"Check white to be detected after plugging to PC"
        logger.info("===================check white to be detected after plug to PC===================")
        white.plugToPC()
        sleep(5000)
        lync.startMeeting()
        sleep(5000)
        lync.startLocalVideo()
        sleep(35000)
        logger.info(LogUtils.imageHtml(white.screenshot()))
        logger.info("<<<<<<<<<<<<<<< plugged white to pc >>>>>>>>>>>>>>>>>")
        then:
        assert lync.isVdWorking("Cube") //lync.isAdWorking("Cube") &&
        sleep(2000)
        lync.hangUp()
        sleep(2000)

        when: "Check white to be removed after unplugging from PC"
        white.unplugFromPC()
        sleep(3000)
        logger.info("<<<<<<<<<<<<<<< unplugged white from pc >>>>>>>>>>>>>>>>>")
        then:
        assert !(lync.isAdWorking("Cube") && lync.isVdWorking("Cube"))

        when: "Check white to be detected after re-plugging to PC"
        logger.info("===================check white to be detected after plug to PC===================")
        white.plugToPC()
        sleep(3000)
        logger.info("<<<<<<<<<<<<<<< plugged white to pc >>>>>>>>>>>>>>>>>")
        then:
        assert lync.isVdWorking("Cube") //lync.isAdWorking("Cube") &&
    }

}
