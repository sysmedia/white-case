package com.polycom.honeycomb.white.test.Network

import com.polycom.honeycomb.White
import com.polycom.honeycomb.test.logCollecting.CollectSutLogIfFailed
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.polycom.honeycomb.SystemTestSpec
import spock.lang.*


/*
    Create by Evan Li on 10/16/2018
 */


@CollectSutLogIfFailed
@Stepwise
@Title('NetworkConnectApWithoutPassword ')
class NetworkConnectApWithoutPassword extends SystemTestSpec {

    private static final Logger logger = LoggerFactory.getLogger(NetworkConnectApWithoutPassword.class)
    private static White white_network = null


    @Shared ssid_DUT = 'Evan_wifi_test_NoPassword'
    @Shared ssid_AUT = 'TP-LINK_White'

    @Shared password = 'Polycom123'

    def setupSpec() {

        boolean setupCompleted = false

        try {
            white_network = testContext.bookSut(White.class, "Network")
            setupCompleted = true
            logger.info('Get the white successfully! ')
        } catch (Exception ignored) {
            logger.error("failed to setup test environment, case is quited!!!")
        }

        assert setupCompleted
        white_network.forgetNetwork(ssid_DUT)

    }

    def cleanupSpec() {
        try {
            if (white_network != null) {
                testContext.releaseSut(white_network)
                logger.info('Release White successfully!')
            }
        } catch (Exception e) {
            logger.error(String.format("failed to release White(%s)!", white_network.getIp()), e)
        }
    }


    @Unroll
    def 'check connection without password under #ConnectionStatus status'(ConnectionStatus) {

        logger.info('enable the wifi')
        given:
        white_network.wifiEnable()
        sleep(5000)

        when:
        if (ConnectionStatus == 'disconnected') {
            while (white_network.getWifiStatus()['wifi_status'] == 'connected') {
                white_network.forgetNetwork(white_network.getWifiStatus()['wifi_connection_info']['mWifiSsid'])
                sleep(5000)
            }
            assert white_network.getWifiStatus()['wifi_status'] == 'disconnected'
        } else {
            if (white_network.getWifiStatus()['wifi_status'] == 'disconnected') {
                white_network.connectWifiByPSK(ssid_AUT,password)
                sleep(5000)
            }
            assert white_network.getWifiStatus()['wifi_status'] == 'connected'
        }


        then:
        assert white_network.getWifiStatus()['wifi_enable'] == true
        assert white_network.getWifiAvailableSsid()



        logger.info('check whether can scan the specific ssid')
        when:
        boolean APexist = false
        white_network.getWifiAvailableSsid()['ssid_info'].each {
            if (it['ssid'] ==  ssid_DUT) {
                APexist = true
                return
            }
        }

        then:
        assert APexist


        logger.info('connect the ssid with password')
        when:
        white_network.connectWifiWithoutSecurity(ssid_DUT)
        sleep(10000)
        def wifistatus = white_network.getWifiStatus()

        then:
        assert wifistatus['wifi_status'] == 'connected'
        assert wifistatus['main_ip_address'] != '0.0.0.0'
        assert wifistatus['wifi_connection_info']['mWifiSsid'] == ssid_DUT


        where:
        ConnectionStatus | _
        'connected'      | _
        'disconnected'   | _
    }


}
