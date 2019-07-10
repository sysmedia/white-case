package com.polycom.honeycomb.white.test.USB

import com.polycom.honeycomb.LyncClientMac
import com.polycom.honeycomb.SystemTestSpec
import com.polycom.honeycomb.White
import com.polycom.honeycomb.log.LogUtils
import com.polycom.honeycomb.test.logCollecting.CollectSutLogIfFailed
import com.polycom.honeycomb.test.performance.Performance
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Shared

/**
 * Created by qxu on 8/14/2018.
 */
@CollectSutLogIfFailed
@Performance(runTimes = 20)
class UsbPlugOutAndInMultiTimesOnMac extends SystemTestSpec{
    private static final Logger logger = LoggerFactory.getLogger(UsbPlugOutAndInMultiTimesOnMac.class)
    @Shared
    LyncClientMac lync

    private static White white =null
    def setupSpec() {
        boolean setupCompleted = false
        try {
            lync = testContext.bookSut(LyncClientMac.class, "SATMac", "1")
            white = testContext.bookSut(White.class, "SATMac", "1")
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

    def "check white can detect after re-plug"(){
        when:
        logger.info("========== trying to plug in White ==========")
        white.plugToPC()
        sleep(3000)
        lync.startMeeting()
        sleep(8000)
        logger.info(LogUtils.imageHtml(white.screenshot()))
        logger.info("<<<<<<<<<< plugged White to PC >>>>>>>>>>")

        then:
        assert lync.isVdWorking("Cube") //lync.isAdWorking("Cube") &&
        sleep(2000)
        lync.hangUp()
        sleep(2000)

        when:
        logger.info("========== trying to un-plug White ==========")
        white.unplugFromPC()
        sleep(3000)
        logger.info("<<<<<<<<<< unplugged White from PC >>>>>>>>>>")

        then:
        assert !(lync.isAdWorking("Cube") && lync.isVdWorking("Cube"))

        when:
        logger.info("========== trying to plug in White again ==========")
        white.plugToPC()
        sleep(3000)
        logger.info("<<<<<<<<<< plugged White to PC >>>>>>>>>>")

        then:
        assert lync.isVdWorking("Cube") //lync.isAdWorking("Cube") &&
    }

}
