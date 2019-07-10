package com.polycom.honeycomb.white.test.Video

import com.polycom.honeycomb.SystemTestSpec
import com.polycom.honeycomb.White
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Shared

class Preparation extends SystemTestSpec{
    static final Logger LOGGER = LoggerFactory.getLogger(Preparation.class)

    @Shared
    White whiteWin
    @Shared
    White whiteWin2
    @Shared
    White whiteMac

    def setupSpec() {
        whiteWin = testContext.bookSut(White.class, "SATWin", "1")
        whiteWin2 = testContext.bookSut(White.class, "SATWin", "2")
        whiteMac = testContext.bookSut(White.class, "SATMac", "1")
    }

    def cleanupSpec() {
        testContext.releaseSut(whiteMac)
        testContext.releaseSut(whiteWin2)
        testContext.releaseSut(whiteWin)
    }

    def "Open WebCamController And enable talker tracking"() {
        when:
        LOGGER.info("Close WebCamController")
        whiteWin.stopWebCamController()
        whiteWin2.stopWebCamController()
        whiteMac.stopWebCamController()
        LOGGER.info("Start WebCamController")
        whiteWin.startWebCamController()
        whiteWin2.startWebCamController()
        whiteMac.startWebCamController()
        LOGGER.info("Enable talker tracking")
        whiteWin.enableTalkerTracking()
        whiteWin2.enableTalkerTracking()
        whiteMac.enableTalkerTracking()

        then:
        noExceptionThrown()
    }
}
