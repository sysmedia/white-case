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
@Title('NetworkCheckApList')
class NetworkCheckApList extends SystemTestSpec{
    private static final Logger logger = LoggerFactory.getLogger(NetworkCheckApList.class)
    private static White white_network = null

    @Shared
    ssid_DUT = 'Evan_wifi_test_PSK'

    @Shared
    password = 'Polycom123'

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


    def "Check AP list after enable wifi under disconnected status"() {

        given:
        white_network.wifiEnable()
        sleep(10000)

        logger.info('\n##################check AP list whether under disconnected status')
        when:
        def wifiStatus = white_network.getWifiStatus()
        if (wifiStatus['wifi_status'] == 'connected') {
            white_network.forgetNetwork(wifiStatus['wifi_connection_info']['mWifiSsid'])
            sleep(2000)
        }

        then:
        white_network.getWifiStatus()['wifi_status'] == 'disconnected'
        CheckAvailableSsid()



        logger.info('\n##############check AP list after scan wifi')
        when:
        white_network.wifiScan()
        sleep(2000)

        then:
        CheckAvailableSsid()

    }

    def "Check AP list after enable wifi under connected status"() {

        given:
        white_network.wifiEnable()
        sleep(10000)


        logger.info('\n##################check AP list whether under disconnected status')
        when:'connect AP'
        boolean ConnectionStatus = true

        if ( white_network.getWifiStatus()['wifi_status'] == 'disconnected' ) { white_network.connectWifiWithoutSecurity(); sleep(5000)}

        if ( white_network.getWifiStatus()['wifi_status'] == 'disconnected' ) {white_network.connectWifiByPSK(ssid_DUT,password); sleep(5000)}

 //       if ( white_network.getWifiStatus()['wifi_status'] == 'disconnected') { white_network.connectWifiByEAP();sleep(5000)}

        if ( white_network.getWifiStatus()['wifi_status'] == 'disconnected' ) { white_network.connectWifiByWEP(ssid_DUT,ssid_DUT); sleep(5000)}

        if ( white_network.getWifiStatus()['wifi_status'] == 'disconnected' ) {ConnectionStatus = false}

        then:
        assert ConnectionStatus

        and:
        CheckAvailableSsid()



        logger.info('\n##############check AP list after scan wifi')
        when:
        white_network.wifiScan()
        sleep(2000)

        then:
        CheckAvailableSsid()

    }



    def "Check AP list when disable wifi"() {


        logger.info('check AP list after wifi disable')
        when:
        white_network.wifiDisable()
        sleep(5000)

        then:
        assert white_network.getWifiStatus()['wifi_enable'] == false
        assert !white_network.getWifiAvailableSsid()['ssid_info']


        logger.info('check AP list after wifi scan')
        when:
        white_network.wifiScan()
        sleep(2000)

        then:
        assert white_network.getWifiStatus()['wifi_enable'] == false
        assert !white_network.getWifiAvailableSsid()['ssid_info']
    }



    void CheckAvailableSsid() {
        logger.info("\n ##############check wifi available ssid############### \n ")
        assert white_network.getWifiStatus()['wifi_enable'] == true
        white_network.getWifiAvailableSsid()['ssid_info'].each{

            assert it['bssid']
            assert it['ssid']
            assert it['encryptionType']
            assert it['signalStrength'] > 0
        }
    }


}
