package com.polycom.honeycomb.white.test.Video

import com.polycom.honeycomb.SystemTestSpec
import com.polycom.honeycomb.White
import spock.lang.Shared

class CloseWebCamController extends SystemTestSpec{
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

    def "Close WebCamController"() {
        when:
        whiteWin.stopWebCamController()
        whiteWin2.stopWebCamController()
        whiteMac.stopWebCamController()

        then:
        noExceptionThrown()
    }
}
