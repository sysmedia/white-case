package com.polycom.honeycomb.white.test.Performance

import com.polycom.honeycomb.SystemTestSpec
import com.polycom.honeycomb.White
import com.polycom.honeycomb.test.logCollecting.CollectSutLogIfFailed
import com.polycom.honeycomb.test.performance.Performance
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by Genie Yan on 9/3/2018.
 */
@CollectSutLogIfFailed
@Performance(runTimes = 20)
class PerformanceRepeatWifiConnectForget extends SystemTestSpec {
    private static final Logger logger = LoggerFactory.getLogger(PerformanceRepeatWifiConnectForget.class)
    private static White white_performance_win = null
    String ssid = "D-Link_DIR-816_5G"
    String password = "Polycom123"

    def setupSpec() {
        boolean setupCompleted = false
        try {
            white_performance_win = testContext.bookSut(White.class, "Performance", "Win")
            setupCompleted = true
        } catch (Exception ignored) {
            logger.error("failed to setup test environment, case is quited!!!")
        }

        assert setupCompleted

        logger.info("===================Enable Wifi for White===================")
        white_performance_win.wifiEnable()

        assert white_performance_win.getWifiStatus().wifi_enable

    }

    def cleanupSpec() {
        try {
            if (white_performance_win != null) testContext.releaseSut(white_performance_win)
        } catch (Exception e) {
            logger.error(String.format("failed to release White(%s)!", white_performance_win.getIp()), e)
        }
    }

    def "Connect and Forget Wifi for White"() {

        when:
        logger.info("===================Connect Wifi for White===================")
        white_performance_win.connectWifiByPSK(ssid, password)
        sleep(5000)
        def result_connect = white_performance_win.getWifiStatus()

        then:
        noExceptionThrown()
        assert result_connect.wifi_enable
        assert result_connect.wifi_status == "connected"
        logger.info("Connected SSID is:" + result_connect.wifi_connection_info.mWifiSsid)
        logger.info("White IP is:" + result_connect.wifi_connection_info.mIpAddress)
        assert result_connect.wifi_connection_info.mWifiSsid == ssid
        assert result_connect.wifi_connection_info.mIpAddress && result_connect.wifi_connection_info.mIpAddress != ""

        when:
        logger.info("===================Forget Wifi for White===================")
        white_performance_win.forgetNetwork(ssid)
        sleep(2000)
        def result_forget = white_performance_win.getWifiStatus()

        then:
        noExceptionThrown()
        assert result_forget.wifi_enable
        assert result_forget.wifi_status == "disconnected"
    }

}
