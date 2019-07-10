package com.polycom.honeycomb.white.test.BAT

import com.polycom.honeycomb.LyncClientMac
import com.polycom.honeycomb.LyncClientWin
import com.polycom.honeycomb.SystemTestSpec
import com.polycom.honeycomb.White
import com.polycom.honeycomb.log.LogUtils
import com.polycom.honeycomb.test.logCollecting.CollectSutLogIfFailed
import jodd.util.StringUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Shared

@CollectSutLogIfFailed
class CheckOnSfbWinP2P extends SystemTestSpec {
    private static final Logger logger = LoggerFactory.getLogger(CheckOnSfbWinP2P.class)

    @Shared
    LyncClientWin lyncCaller

    @Shared
    LyncClientMac lyncCallee

    @Shared
    White white

    def setupSpec() {
        boolean setupCompleted = false
        try {
            lyncCaller = testContext.bookSut(LyncClientWin.class, "BAT", "Win", "1")
            lyncCallee = testContext.bookSut(LyncClientMac.class, "BAT", "Mac", "1")
            white = testContext.bookSut(White.class, "BAT", "Win", "1")

            if (lyncCallee.isInCall()) {
                logger.info(String.format("Lync %s(%s) hangup the before call", lyncCallee.getAccount(), lyncCallee.getIp()))
                lyncCallee.hangUp()
                sleep(5000)
            }
            if (lyncCaller.isInCall()) {
                logger.info(String.format("Lync %s(%s) hangup the before call", lyncCaller.getAccount(), lyncCaller.getIp()))
                lyncCaller.hangUp()
                sleep(5000)
            }

            setupCompleted = true
        } catch (Exception e) {
            logger.error("failed to setup test environment, case is quited!!!", e)
        }

        assert setupCompleted
    }

    def cleanupSpec() {
        try {
            if (lyncCaller != null) {testContext.releaseSut(lyncCaller)}
        } catch (Exception e) {
            logger.error(String.format("failed to release Lync(%s)!", lyncCaller.getIp()), e)
        }
        try {
            if (lyncCallee != null) testContext.releaseSut(lyncCallee)
        } catch (Exception e) {
            logger.error(String.format("failed to release Lync(%s)!", lyncCallee.getIp()), e)
        }
        try {
            if (white != null) testContext.releaseSut(white)
        } catch (Exception e) {
            logger.error(String.format("failed to release White(%s)!", white.getIp()), e)
        }
    }

    def "Lync P2P Call"() {
        when:
        logger.info(String.format("Lync %s(%s) place call to Lync %s(%s)", lyncCaller.getAccount(), lyncCaller.getIp(), lyncCallee.getAccount(), lyncCallee.getIp()))
        lyncCaller.placeCall("leo3@sfb2015.com")
        sleep(6000)
        logger.info(String.format("Lync %s(%s) answer the call", lyncCallee.getAccount(), lyncCallee.getIp()))
        lyncCallee.answerCall()
        sleep(10000)
        logger.info(LogUtils.imageHtml(white.screenshot()))
        String url = lyncCallee.getPc().rest().screenShot()
        if (!StringUtil.isEmpty(url) && !url.startsWith("http")) {
            url = "http://" + url
        }
        logger.info(LogUtils.imageHtml(url))

        then:
        assert lyncCaller.isInCall()
        assert lyncCallee.isInCall()
    }

    def "Hang up"() {
        when:
        //if (lyncCallee.isInCall()) {
            //logger.info(String.format("Lync %s(%s) hangup the call", lyncCallee.getAccount(), lyncCallee.getIp()))
            lyncCallee.hangUp()
            sleep(6000)
        //}
        then:
        assert !lyncCaller.isInCall()
        assert !lyncCallee.isInCall()
    }
}
