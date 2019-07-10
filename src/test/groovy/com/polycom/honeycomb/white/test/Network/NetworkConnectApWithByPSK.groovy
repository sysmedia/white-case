package com.polycom.honeycomb.white.test.Network

import com.polycom.honeycomb.SystemTestSpec
import com.polycom.honeycomb.White
import com.polycom.honeycomb.test.logCollecting.CollectSutLogIfFailed
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.*

/*
    Create by Evan Li on 10/16/2018
 */


@CollectSutLogIfFailed
@Stepwise
@Title('CheckNetworkConnectWithApByPSK')
class NetworkConnectApWithByPSK extends SystemTestSpec{

    private static final Logger logger = LoggerFactory.getLogger(NetworkConnectApWithByPSK.class)
    private static White white_network = null


    @Shared ssid_DUT = 'Evan_wifi_test_PSK'
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
    def 'check connection with PSK under #ConnectionStatus status'(ConnectionStatus) {

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
        white_network.connectWifiByPSK(ssid_DUT,password)
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

    @Unroll
    def 'check connection with wrong ssid: #SsidName or password: #Passwd'(ConnectionStatus,SsidName,Passwd ){

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



        logger.info('connect the ssid with password')



        when:
        def wifistatusBefore = white_network.getWifiStatus()
        white_network.connectWifiByPSK(SsidName,Passwd)
        sleep(10000)
        def wifistatusAfter = white_network.getWifiStatus()

        then:
        assert wifistatusAfter['wifi_status'] == wifistatusBefore['wifi_status']
        assert wifistatusAfter['main_ip_address'] == wifistatusBefore['main_ip_address']
        assert wifistatusAfter['wifi_status'] == wifistatusBefore['wifi_status']

        if (wifistatusAfter['wifi_status'] == 'connected' ) {
            assert wifistatusAfter['wifi_connection_info']['mWifiSsid'] == wifistatusBefore['wifi_connection_info']['mWifiSsid']
        }else if (wifistatusAfter['wifi_status'] == 'disconnected') {
            assert wifistatusAfter['wifi_connection_info'] == wifistatusBefore['wifi_connection_info']
        }


        where:
        ConnectionStatus |  SsidName                     |    Passwd
        'connected'      |'Evan_wifi_test_PSK'           |'polyocm123'
        'connected'      |'Evan_wifi_'                   |'Polycom123'
        'connected'      |'qwe123!@#$%^&*()'             |'123qwe!@#$%^&*()'
        'connected'      |'qwertyuio'                    |'kjhgfds'
        'connected'      |'1234567890'                   |'1234567890'
        'connected'      |'~!@#$%^&*()_+{}:"<>?[];,./\\|'|'~!@#$%^&*()_+{}:"<>?[];,./\\|'
        'connected'      |'qwerty1234567890uiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnm' | '123qweasdzxc4567890rtyuiopdfhgjvkblmnqwertyuiopasdfghjklzxcvbnm'
        'disconnected'   |'Evan_wifi_test_PSK'           |   'polyocm123'
        'disconnected'   |'Evan_wifi_'                   |   'Polycom123'
        'disconnected'   |'qwe123!@#$%^&*()'             |   '123qwe!@#$%^&*()'
        'disconnected'   |'qwertyuio'                    |   'kjhgfds'
        'disconnected'   |'1234567890'                   |   '1234567890'
        'disconnected'   |'~!@#$%^&*()_+{}:"<>?[];,./\\|'|   '~!@#$%^&*()_+{}:"<>?[];,./\\|'
        'disconnected'   |'qwerty1234567890uiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnm' | '123qweasdzxc4567890rtyuiopdfhgjvkblmnqwertyuiopasdfghjklzxcvbnm'


    }

}
