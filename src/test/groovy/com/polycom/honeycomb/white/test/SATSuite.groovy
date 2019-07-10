package com.polycom.honeycomb.white.test


import com.polycom.honeycomb.white.test.Video.VideoResolutionYUV21024X576
import com.polycom.honeycomb.white.test.Video.VideoResolutionYUV21280X720
import com.polycom.honeycomb.white.test.Video.VideoResolutionYUV21600X900

import com.polycom.honeycomb.white.test.Video.VideoResolutionYUV21920X1080

import com.polycom.honeycomb.white.test.Video.VideoResolutionYUV2352X288

import com.polycom.honeycomb.white.test.Video.VideoResolutionYUV2480X270
import com.polycom.honeycomb.white.test.Video.VideoResolutionYUV2640X360
import com.polycom.honeycomb.white.test.Video.VideoResolutionYUV2640X480
import com.polycom.honeycomb.white.test.Video.VideoResolutionYUV2720X480
import com.polycom.honeycomb.white.test.Video.VideoResolutionYUV2720X576

import com.polycom.honeycomb.white.test.Video.VideoResolutionYUV2800X600

import com.polycom.honeycomb.white.test.Video.VideoResolutionYUV2960X540
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite.class)
@Suite.SuiteClasses([
// Video
// Preparation,
// Video for MJPEG
// VideoResolutionMJPEG160X120,VideoResolutionMJPEG176X144, VideoResolutionMJPEG320X180, VideoResolutionMJPEG320X240, VideoResolutionMJPEG352X288, VideoResolutionMJPEG424X240, VideoResolutionMJPEG480X270,
//VideoResolutionMJPEG640X360, VideoResolutionMJPEG640X480, VideoResolutionMJPEG720X480, VideoResolutionMJPEG720X576, VideoResolutionMJPEG800X448, VideoResolutionMJPEG800X600, VideoResolutionMJPEG848X480,
//VideoResolutionMJPEG960X540, VideoResolutionMJPEG1024X576, VideoResolutionMJPEG1280X720, VideoResolutionMJPEG1600X900, VideoResolutionMJPEG1920X1080, VideoResolutionMJPEG2560X1440, VideoResolutionMJPEG3840X2160,
// Video for NV12
// VideoResolutionNV12160X120, VideoResolutionNV12320X180, VideoResolutionNV12320X240, VideoResolutionNV12352X288, VideoResolutionNV12480X270, VideoResolutionNV12640X360,
//VideoResolutionNV12640X480, VideoResolutionNV12800X448, VideoResolutionNV12800X600, NV12_848_480, VideoResolutionNV12960X540, VideoResolutionNV121024X576,
//VideoResolutionNV121280X720, VideoResolutionNV121600X900, VideoResolutionNV121920X1080,
//VideoResolutionNV122560X1440
// Video for YUV2
        VideoResolutionYUV2352X288,
        VideoResolutionYUV2480X270, VideoResolutionYUV2640X360,
        VideoResolutionYUV2640X480, VideoResolutionYUV2720X480, VideoResolutionYUV2720X576,
        VideoResolutionYUV2800X600,  VideoResolutionYUV2960X540, VideoResolutionYUV21024X576,
        VideoResolutionYUV21280X720, VideoResolutionYUV21600X900, VideoResolutionYUV21920X1080

//Video Camera settings for PTZE
// VideoCameraSettingsZPTE
// CloseWebCamController

// Network
// NetworkCheckApList, NetworkConnectApWithByPSK, NetworkConnectApWithoutPassword


]
)

class SATSuite {
}


