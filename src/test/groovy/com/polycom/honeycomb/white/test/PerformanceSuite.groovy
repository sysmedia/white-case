package com.polycom.honeycomb.white.test


import com.polycom.honeycomb.white.test.Performance.PerformanceIdle
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite.class)
@Suite.SuiteClasses([
//        PerformanceRepeatBTOnOff,
//        PerformanceRepeatFaceDetectionOnOff,
//        PerformanceRepeatWifiConnectForget,
//        PerformanceRepeatWifiOnOff,
//        //PerformanceRepeatPowerOnOff,
//        PerformanceRepeatRestartOnWin,
//        PerformanceRepeatRestartOnMac,
//        PerformanceSFBWinShortCall,
//        PerformanceSFBWinLongCall,
//        PerformanceRPDMacLongCall
//        PerformanceRPDMacShortCall
        PerformanceIdle
])

class PerformanceSuite {
}
