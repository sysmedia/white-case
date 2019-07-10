package com.polycom.honeycomb.white.test.Performance

import com.polycom.honeycomb.LyncClientWin
import com.polycom.honeycomb.SystemTestSpec
import com.polycom.honeycomb.White
import com.polycom.honeycomb.log.LogUtils
import com.polycom.honeycomb.test.performance.PerfDataProbe
import com.polycom.honeycomb.test.performance.Performance
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Shared

//@CollectSutLog
@Performance
class PerformanceSFBWinLongCall extends SystemTestSpec{
    private static final Logger logger = LoggerFactory.getLogger(PerformanceSFBWinLongCall.class)

    @Shared
    PerfDataProbe probe

    @Shared
    White white

    @Shared
    LyncClientWin lyncCaller

    @Shared
    LyncClientWin lyncCallee

    def setupSpec() {
        white = testContext.bookSut(White.class, "BAT", "Win", "2")
        lyncCaller = testContext.bookSut(LyncClientWin.class, "BAT", "Caller")
        lyncCallee = testContext.bookSut(LyncClientWin.class, "BAT", "Callee")
        probe = testContext.addPerfDataProbe(white, 30*1000)
        white.enableTalkerTracking()
//        white.enableAdb()
    }

    def cleanupSpec() {
        testContext.releaseSut(white)
    }

    def setup() {
        if (lyncCaller.isInCall()) {
            lyncCaller.hangUp()
        }
        if (lyncCallee.isInCall()) {
            lyncCallee.hangUp()
        }
    }

    def cleanup() {
    }

    def "Sfb Win Long Call"() {
        when:
        lyncCaller.placeCall(lyncCallee.getAccount())
        sleep(2000)
        lyncCallee.answerCall()
        sleep(10000)

        then:
        assert lyncCaller.isInCall()
        assert lyncCallee.isInCall()
        assert lyncCaller.isAdWorking("White") && lyncCaller.isVdWorking("White")

        when:
        logger.info("Call last 12 hours")
        1.upto(144) {
            sleep(60*1000*5)
            if (!lyncCaller.isInCall()) {
                logger.info("Call is disconnected, redial")
                lyncCaller.placeCall(lyncCallee.getAccount())
                sleep(2000)
                lyncCallee.answerCall()
                sleep(10000)
            }
            logger.info(LogUtils.imageHtml(white.screenshot()))
            assert lyncCaller.isAdWorking("Polycom Studio") && lyncCaller.isVdWorking("Polycom Studio")
        }
        lyncCaller.hangUp()
        sleep(10*1000)

        then:
        assert !lyncCaller.isInCall()
        assert !lyncCallee.isInCall()
        noExceptionThrown()

        when:
        logger.info("Check if White is core dump")
        boolean result = !white.isCoreDumpExisted()

        then:
        assert result
    }
}