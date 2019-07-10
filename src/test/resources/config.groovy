import com.polycom.honeycomb.LyncClientMac
import com.polycom.honeycomb.LyncClientWin
import com.polycom.honeycomb.RpdMac
import com.polycom.honeycomb.RpdWin
import com.polycom.honeycomb.White
import com.polycom.honeycomb.test.context.GroovyTestContext

import static com.polycom.honeycomb.test.context.TestContextLoader.*

config(GroovyTestContext.class) {
    reportFolderRootPath = "build/testreports"

    suts = [sut(White.class, "BAT", "Win", "1").setIp("172.21.119.156").setPcIp("172.21.119.156"),
            sut(White.class, "BAT", "Mac", "1").setIp("172.21.118.155").setPcIp("172.21.118.155"),
            sut(LyncClientWin.class, "BAT", "Win", "1").setPcIp("172.21.119.156").setIp("172.21.119.156"),
            sut(LyncClientMac.class, "BAT", "Mac", "1").setPcIp("172.21.118.155").setIp("172.21.118.155"),
            sut(RpdWin.class, "RPDWin").setIp("172.21.119.156").setPcIp("172.21.119.156"),
            sut(RpdMac.class, "RPDMac").setIp("172.21.118.155").setPcIp("172.21.118.155"),
            sut(White.class, "SATWin", "1").setIp("172.21.119.156").setPcIp("172.21.119.156"),
            sut(White.class, "SATMac", "1").setIp("172.21.118.155").setPcIp("172.21.118.155"),
            sut(LyncClientWin.class, "SATWin", "1").setPcIp("172.21.119.156").setIp("172.21.119.156"),
            sut(LyncClientMac.class, "SATMac", "1").setPcIp("172.21.118.155").setIp("172.21.118.155"),
            sut(White.class, "SATWin", "2").setIp("172.21.118.208").setPcIp("172.21.118.208"),
            sut(White.class, "SATMac", "2").setIp("172.21.118.155").setPcIp("172.21.118.155"),
    ]
}