package com.polycom.honeycomb.white.test

import com.polycom.honeycomb.white.test.BAT.*
import com.polycom.honeycomb.white.test.Interop.*
import com.polycom.honeycomb.white.test.USB.*
import com.polycom.honeycomb.white.test.Video.*
import com.polycom.honeycomb.white.test.Video.USB30.*
import com.polycom.honeycomb.white.test.Video.USB20.*
import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * Created by thzhang on 3/1/2019.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses([
//        CheckDfuUpgradeOnWin,
//        CheckDfuUpgradeOnMac,
//        CheckDetectionAfterPlugUnplugOnWin,
//        CheckDetectionAfterPlugUnplugOnMac,
//        CheckStatisticsOnRPDWinAndMacP2P,

        Preparation,
        USB30_WIN_MJPEG_352x288,
        USB30_WIN_MJPEG_480x270,
        USB30_WIN_MJPEG_640x360,
        USB30_WIN_MJPEG_640x480,
        USB30_WIN_MJPEG_720x480,
        USB30_WIN_MJPEG_720x576,
        USB30_WIN_MJPEG_800x600,
        USB30_WIN_MJPEG_960x540,
        USB30_WIN_MJPEG_1024x576,
        USB30_WIN_MJPEG_1280x720,
        USB30_WIN_MJPEG_1600x900,
        USB30_WIN_MJPEG_1920x1080,
        USB30_WIN_MJPEG_2560x1440,
        USB30_WIN_MJPEG_3840x2160,

        USB30_WIN_YUY2_352x288,
        USB30_WIN_YUY2_480x270,
        USB30_WIN_YUY2_640x360,
        USB30_WIN_YUY2_640x480,
        USB30_WIN_YUY2_720x480,
        USB30_WIN_YUY2_720x576,
        USB30_WIN_YUY2_800x600,
        USB30_WIN_YUY2_960x540,
        USB30_WIN_YUY2_1024x576,
        USB30_WIN_YUY2_1280x720,
        USB30_WIN_YUY2_1600x900,
        USB30_WIN_YUY2_1920x1080,
        USB30_WIN_YUY2_2560x1440,

        USB30_MAC_MJPEG_352x288,
        USB30_MAC_MJPEG_480x270,
        USB30_MAC_MJPEG_640x360,
        USB30_MAC_MJPEG_640x480,
        USB30_MAC_MJPEG_720x480,
        USB30_MAC_MJPEG_720x576,
        USB30_MAC_MJPEG_800x600,
        USB30_MAC_MJPEG_960x540,
        USB30_MAC_MJPEG_1024x576,
        USB30_MAC_MJPEG_1280x720,
        USB30_MAC_MJPEG_1600x900,
        USB30_MAC_MJPEG_1920x1080,
        USB30_MAC_MJPEG_2560x1440,
        USB30_MAC_MJPEG_3840x2160,

        USB30_MAC_NV12_352x288,
        USB30_MAC_NV12_480x270,
        USB30_MAC_NV12_640x360,
        USB30_MAC_NV12_640x480,
        USB30_MAC_NV12_720x480,
        USB30_MAC_NV12_720x576,
        USB30_MAC_NV12_800x600,
        USB30_MAC_NV12_960x540,
        USB30_MAC_NV12_1024x576,
        USB30_MAC_NV12_1280x720,
        USB30_MAC_NV12_1600x900,
        USB30_MAC_NV12_1920x1080,
        USB30_MAC_NV12_2560x1440,

        USB30_MAC_YUY2_352x288,
        USB30_MAC_YUY2_480x270,
        USB30_MAC_YUY2_640x360,
        USB30_MAC_YUY2_640x480,
        USB30_MAC_YUY2_720x480,
        USB30_MAC_YUY2_720x576,
        USB30_MAC_YUY2_800x600,
        USB30_MAC_YUY2_960x540,
        USB30_MAC_YUY2_1024x576,
        USB30_MAC_YUY2_1280x720,
        USB30_MAC_YUY2_1600x900,
        USB30_MAC_YUY2_1920x1080,
        USB30_MAC_YUY2_2560x1440,

        USB20_WIN_MJPEG_352x288,
        USB20_WIN_MJPEG_480x270,
        USB20_WIN_MJPEG_640x360,
        USB20_WIN_MJPEG_640x480,
        USB20_WIN_MJPEG_720x480,
        USB20_WIN_MJPEG_720x576,
        USB20_WIN_MJPEG_800x600,
        USB20_WIN_MJPEG_960x540,
        USB20_WIN_MJPEG_1024x576,
        USB20_WIN_MJPEG_1280x720,
        USB20_WIN_MJPEG_1600x900,
        USB20_WIN_MJPEG_1920x1080,

        USB20_WIN_YUY2_352x288,
        USB20_WIN_YUY2_480x270,
        USB20_WIN_YUY2_640x360,
        USB20_WIN_YUY2_640x480,
        USB20_WIN_YUY2_720x480,
        USB20_WIN_YUY2_720x576,
        USB20_WIN_YUY2_800x600,
        USB20_WIN_YUY2_960x540,
        USB20_WIN_YUY2_1024x576,
        USB20_WIN_YUY2_1280x720,

//        VideoResolutionMJPEG352X288,
//        VideoResolutionMJPEG480X270,
//        VideoResolutionMJPEG640X360,
//        VideoResolutionMJPEG640X480,
//        VideoResolutionMJPEG720X480,
//        VideoResolutionMJPEG720X576,
//        VideoResolutionMJPEG800X600,
//        VideoResolutionMJPEG960X540,
//        VideoResolutionMJPEG1024X576,
//        VideoResolutionMJPEG1280X720,
//        VideoResolutionMJPEG1600X900,
//        VideoResolutionMJPEG1920X1080,
//
//        VideoResolutionYUV2352X288,
//        VideoResolutionYUV2480X270,
//        VideoResolutionYUV2640X360,
//        VideoResolutionYUV2640X480,
//        VideoResolutionYUV2720X480,
//        VideoResolutionYUV2720X576,
//        VideoResolutionYUV2800X600,
//        VideoResolutionYUV2960X540,
//        VideoResolutionYUV21024X576,
//        VideoResolutionYUV21280X720,
//
//        VideoResolutionNV12352X288,
//        VideoResolutionNV12480X270,
//        VideoResolutionNV12640X360,
//        VideoResolutionNV12640X480,
//        VideoResolutionNV12800X600,
//        VideoResolutionNV12960X540,
//        VideoResolutionNV121024X576,

        CloseWebCamController,
//        SfbMuteUnmuteDuringP2PCall,
//        SfbHoldResumeDuringP2PCall,
        UsbPlugOutAndInMultiTimesOnWin
//        UsbPlugOutAndInMultiTimesOnMac

//        CheckOnSfbWinP2P
])

class BATSuite {
}
