package com.polycom.honeycomb.white.test.Performance

import com.polycom.honeycomb.SystemTestSpec
import com.polycom.honeycomb.White
import com.polycom.honeycomb.pdu.PduClient
import com.polycom.honeycomb.test.logCollecting.CollectSutLogIfFailed
import com.polycom.honeycomb.test.performance.Performance
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by Genie Yan on 8/29/2018.
 */
@CollectSutLogIfFailed
@Performance(runTimes = 20)
class PerformanceRepeatPowerOnOff extends SystemTestSpec {
    private static final Logger logger = LoggerFactory.getLogger(PerformanceRepeatPowerOnOff.class)
    private static White white_performance_win = null
    int outlet = 6 // PDU outlet number that SUT connected to

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

    def "Power Off and On White"() {
        when:
        logger.info("===================Power Off White===================")
        PduClient pdu = new PduClient("10.220.61.94") // If PDU changed default username and password, please use PduClient("10.220.61.94", "username", "password")
        pdu.powerOff(outlet)  // The parameter is the PDU outlet number that SUT connected to
        sleep(5000)

        then:
        noExceptionThrown()
        assert !pdu.isOn(outlet)
        assert !white_performance_win.isDetectedByPC()

        when:
        logger.info("===================Power On White===================")
        pdu.powerOn(outlet)
        sleep(5000)

        then:
        noExceptionThrown()
        assert pdu.isOn(outlet) // It means that PDU have applied power to this outlet, does not mean that SUT have started successfully
        logger.info("===================Wait White startup===================")
        sleep(100000)
        logger.info("<<<<<<<<<<<<<<< White Started>>>>>>>>>>>>>>>>>")
        assert white_performance_win.isDetectedByPC()
    }

}
