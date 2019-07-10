package com.polycom.honeycomb.white.test.Performance

import com.polycom.honeycomb.RpdMac
import com.polycom.honeycomb.SystemTestSpec
import com.polycom.honeycomb.White
import com.polycom.honeycomb.log.LogUtils
import com.polycom.honeycomb.mediastatistics.CallType
import com.polycom.honeycomb.test.performance.PerfDataProbe
import com.polycom.honeycomb.test.performance.Performance
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Shared

//@CollectSutLogIfFailed
@Performance(runTimes = 100)
class PerformanceRPDMacShortCall extends SystemTestSpec{
    private static final Logger logger = LoggerFactory.getLogger(PerformanceRPDMacShortCall.class)

    String DialNumber = "76969665@172.21.113.57"

    @Shared
    PerfDataProbe probe

    @Shared
    White white

    @Shared
    RpdMac rpdmac

    def setupSpec() {
        white = testContext.bookSut(White.class, "BAT", "Mac", "1")
        probe = testContext.addPerfDataProbe(white, 30*1000)
        white.enableTalkerTracking()
        white.getPc().rest().rpdQuit()
        sleep(3000)
        white.getPc().rest().rpdStart()
        sleep(20000)
        rpdmac = testContext.bookSut(RpdMac.class, "RPDMac")
    }

    def cleanupSpec() {
        testContext.releaseSut(white)
        testContext.releaseSut(rpdmac)
        white.getPc().rest().rpdQuit()
    }

    def setup() {
        rpdmac.hangUp()
    }

    def cleanup() {
    }

    def "RPD Mac Short Call"() {
        when:
        rpdmac.placeCall(DialNumber, CallType.SIP, 1920)
        sleep(10*1000)

        then:
        assert rpdmac.getCallStatus() =~ 'RPDConnected'

        when:
        logger.info("Call last 5 minutes")
        sleep(60*1000*5)
        logger.info(LogUtils.imageHtml(white.screenshot()))
        rpdmac.hangUp()
        sleep(10*1000)

        then:
        assert rpdmac.getCallStatus() == 'RPDDisconnected'
        noExceptionThrown()

        when:
        logger.info("Check if White is core dump")
        boolean result = !white.isCoreDumpExisted()

        then:
        assert result
    }
}
