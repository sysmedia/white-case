package com.polycom.honeycomb.white.test.BAT

import com.polycom.honeycomb.RpdMac
import com.polycom.honeycomb.RpdWin
import com.polycom.honeycomb.SystemTestSpec
import com.polycom.honeycomb.White
import com.polycom.honeycomb.log.LogUtils
import com.polycom.honeycomb.mediastatistics.CallType
import com.polycom.honeycomb.mediastatistics.ChannelType
import com.polycom.honeycomb.mediastatistics.MediaChannelStatistics
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.polycom.honeycomb.test.logCollecting.CollectSutLogIfFailed
import spock.lang.Shared

import static com.polycom.honeycomb.mediastatistics.MediaType.VIDEO

@CollectSutLogIfFailed
class CheckStatisticsOnRPDWinAndMacP2P extends SystemTestSpec {
    private static final Logger logger = LoggerFactory.getLogger(CheckStatisticsOnRPDWinAndMacP2P.class)

    @Shared
    RpdWin rpdWin

    @Shared
    RpdMac rpdMac

    @Shared
    White whiteWin

    @Shared
    White whiteMac

    def setupSpec() {
        boolean setupCompleted = false
        try {
            whiteWin = testContext.bookSut(White.class, "BAT", "Win", "1")
            whiteMac = testContext.bookSut(White.class, "BAT", "Mac", "1")

            logger.info("Start RPD Win")
            whiteWin.getPc().rest().rpdStart()
            logger.info("Start RPD Mac")
            whiteMac.getPc().rest().rpdStart()
            sleep(30000)

            rpdWin = testContext.bookSut(RpdWin.class, "RPDWin")
            rpdMac = testContext.bookSut(RpdMac.class, "RPDMac")
            setupCompleted = true
        } catch (Exception ignored) {
            logger.error("failed to setup test environment, case is quited!!!")
        }

        assert setupCompleted
    }

    def cleanupSpec() {
        try {
            if (rpdWin != null) {testContext.releaseSut(rpdWin)}
        } catch (Exception e) {
            logger.error(String.format("failed to release RPDWin(%s)!", rpdWin.getIp()), e)
        }
        try {
            if (rpdMac != null) testContext.releaseSut(rpdMac)
        } catch (Exception e) {
            logger.error(String.format("failed to release RPDMac(%s)!", rpdMac.getIp()), e)
        }

//        logger.info("Close RPD Win")
//        whiteWin.getPc().rest().rpdQuit()
//        logger.info("Close RPD Mac")
//        whiteMac.getPc().rest().rpdQuit()

        try {
            if (whiteWin != null) testContext.releaseSut(whiteWin)
        } catch (Exception e) {
            logger.error(String.format("failed to release White(%s)!", whiteWin.getIp()), e)
        }
        try {
            if (whiteMac != null) testContext.releaseSut(whiteMac)
        } catch (Exception e) {
            logger.error(String.format("failed to release White(%s)!", whiteMac.getIp()), e)
        }
    }

//    def "Init RPDWin and RPDMac"() {
//        when:
//        logger.info("===================RPDWin Init===================")
//        rpdWin.hangUp()
//        sleep(2000)
//        rpdWin.signOut()
//        sleep(2000)
//        rpdWin.unregisterH323()
//        sleep(2000)
//        rpdWin.unregisterSIP()
//        sleep(2000)
//        rpdWin.enableAutoAnswer()
//        logger.info("===================RPDWin Init Done===================")
//
//        logger.info("===================RPDMac Init===================")
//        rpdMac.hangUp()
//        sleep(2000)
//        rpdMac.signOut()
//        sleep(2000)
//        rpdMac.unregisterH323()
//        sleep(2000)
//        rpdMac.unregisterSIP()
//        sleep(2000)
//        rpdMac.enableAutoAnswer()
//        logger.info("===================RPDMac Init Done===================")
//
//        then:
//        noExceptionThrown()
//    }

    def "RPDWin call to RPDMac"() {
        when:
        logger.info("===================RPDWin Place Call To RPDMac===================")
        rpdWin.placeCall(rpdMac.getIp(), CallType.SIP, 1920)
        logger.info("===================RPDWin Place Call To RPDMac Done===================")
        sleep(10000)

        then:
        noExceptionThrown()
    }

    def "Check the Video Statistics"() {
        when:
        logger.info("Capture from Win:")
        logger.info(LogUtils.imageHtml(whiteWin.screenshot()))
//        logger.info("Capture from Mac:")
//        logger.info(LogUtils.imageHtml(whiteMac.screenshot()))
        boolean isWinVideoTxExist = false
        boolean isWinVideoRxExist = false
        boolean isMacVideoTxExist = false
        boolean isMacVideoRxExist = false
        for (MediaChannelStatistics channel : rpdWin.mediaStatistics.channels) {
            if (channel.mediaType == VIDEO) {
                if (channel.channelType == ChannelType.TX && channel.rateUsed > 0 && channel.frameRate > 0) {
                    isWinVideoTxExist = true
                } else if (channel.channelType == ChannelType.RX && channel.rateUsed > 0 && channel.frameRate > 0) {
                    isWinVideoRxExist = true
                }
            }
        }

        for (MediaChannelStatistics channel : rpdMac.mediaStatistics.channels) {
            if (channel.mediaType == VIDEO) {
                if (channel.channelType == ChannelType.TX && channel.rateUsed > 0 && channel.frameRate > 0) {
                    isMacVideoTxExist = true
                } else if (channel.channelType == ChannelType.RX && channel.rateUsed > 0 && channel.frameRate > 0) {
                    isMacVideoRxExist = true
                }
            }
        }

        then:
        noExceptionThrown()
        isWinVideoTxExist && isWinVideoRxExist && isMacVideoTxExist && isMacVideoRxExist
    }

    def "RPDWin Hang up"() {
        when:
        logger.info("===================RPDWin Hang up===================")
        rpdWin.hangUp()
        logger.info("===================RPDWin Hang up Done===================")

        then:
        noExceptionThrown()
    }
}
