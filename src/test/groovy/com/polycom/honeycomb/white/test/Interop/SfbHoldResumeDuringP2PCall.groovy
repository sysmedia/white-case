package com.polycom.honeycomb.white.test.Interop

import com.polycom.honeycomb.LyncClientMac
import com.polycom.honeycomb.LyncClientWin
import com.polycom.honeycomb.SystemTestSpec
import com.polycom.honeycomb.White
import com.polycom.honeycomb.log.LogUtils
import com.polycom.honeycomb.test.logCollecting.CollectSutLogIfFailed
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Shared

@CollectSutLogIfFailed
class SfbHoldResumeDuringP2PCall extends SystemTestSpec{
    private static final Logger logger = LoggerFactory.getLogger(SfbHoldResumeDuringP2PCall.class)

    @Shared
    LyncClientWin lyncCaller

    @Shared
    LyncClientMac lyncCallee

    @Shared
    White white

    def setupSpec() {
        boolean setupCompleted = false
        try {
            lyncCaller = testContext.bookSut(LyncClientWin.class, "SATWin", "1")
            lyncCallee = testContext.bookSut(LyncClientMac.class, "SATMac", "1")
            white = testContext.bookSut(White.class, "SATWin", "1")

            if (lyncCaller.isInCall()) {
                logger.info(String.format("Lync %s(%s) hangup the before call", lyncCaller.getAccount(), lyncCaller.getIp()))
                lyncCaller.hangUp()
                sleep(5000)
            }
            if (lyncCallee.isInCall()) {
                logger.info(String.format("Lync %s(%s) hangup the before call", lyncCallee.getAccount(), lyncCallee.getIp()))
                lyncCallee.hangUp()
                sleep(5000)
            }
            setupCompleted = true
        } catch (Exception ignored) {
            logger.error("failed to setup test environment, case is quited!!!")
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
        def callee = lyncCallee.getAccount().toLowerCase().split(" ")[0] + "@sfb2015.com"
        logger.info(String.format("Lync %s(%s) place call to Lync %s(%s)", lyncCaller.getAccount(), lyncCaller.getIp(), callee, lyncCallee.getIp()))
        lyncCaller.placeCall(callee)
        sleep(2000)
        logger.info(String.format("Lync %s(%s) answer the call", callee, lyncCallee.getIp()))
        lyncCallee.answerCall()
        sleep(30000)
        logger.info(LogUtils.imageHtml(white.screenshot()))

        then:
        assert lyncCaller.isInCall()
        assert lyncCallee.isInCall()
        assert lyncCaller.isAdWorking("Cube") && lyncCaller.isVdWorking("Cube")
    }

    def "hold call"(){
        when:
        lyncCaller.holdCall()
        sleep(5000)
        logger.info(LogUtils.imageHtml(white.screenshot()))

        then:
        assert lyncCaller.isCallOnHold()
    }

    def "resume call"(){
        when:
        lyncCaller.resumeCall()
        sleep(5000)
        logger.info(LogUtils.imageHtml(white.screenshot()))

        then:
        assert !lyncCaller.isCallOnHold()
    }

    def "Hang up"() {
        when:
        if (lyncCaller.isInCall()) {
            logger.info(String.format("Lync %s(%s) hangup the call", lyncCaller.getAccount(), lyncCaller.getIp()))
            lyncCaller.hangUp()
            sleep(10000)
        }
        then:
        assert !lyncCaller.isInCall()
        assert !lyncCallee.isInCall()
    }
}
