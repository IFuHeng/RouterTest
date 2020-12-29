package com.changhong.telnettool.function.test_exam;

import com.changhong.telnettool.tool.Tool;
import com.changhong.telnettool.webinterface.Observer;
import com.changhong.telnettool.webinterface.been.wan.WanResponseAllBeen;
import javafx.util.Pair;

/**
 * 2.2.2. DHCP  自动获取 IP  上网 功能
 */
public class Test2p2p2 extends WanSettingBase implements Runnable {
    public Test2p2p2(String password, Observer<Pair<Boolean, String>> callback) {
        super(password, callback);
    }

    @Override
    public void run() {
        //获得类型
        if (!getDeviceType())
            return;

        //登录
        if (!login())
            return;

        if (!saveBackSetting()) {
            callbackError(new Exception("备份原始设置，中断。"));
            return;
        }

        // 检测当前设备基本信息和WAN口是否为dhcp
        String wanType = showDeviceBaseInfo();

        if (wanType == null) {
            callbackError(new Exception("未获取到wan口类型，中断"));
            return;
        }

        if (!wanType.equalsIgnoreCase("dhcp")) {
            if (!setWanDhcp() || waitReceiveIp()) {
                callbackError(new Exception("设置dhcp失败或超时，中断"));
                return;
            }
        }

        //检测外网
        testInternet("http://www.baidu.com", "http://www.sina.cn", "http://www.qq.com");

        String ip = Tool.turn2IpV4((int) Math.round(Math.random() * Integer.MAX_VALUE) & 0xffffffff);
        boolean is = setWanStatic(ip,
                Tool.turn2IpV4((int) Math.round(Math.random() * Integer.MAX_VALUE) & 0xffffffff),
                Tool.turn2IpV4((int) Math.round(Math.random() * Integer.MAX_VALUE) & 0xffffffff),
                Tool.turn2IpV4((int) Math.round(Math.random() * Integer.MAX_VALUE) & 0xffffffff),
                Tool.turn2IpV4((int) Math.round(Math.random() * Integer.MAX_VALUE) & 0xffffffff));
        if (!is || !waitForGotIp(ip, 20)) {
            callbackError(new Exception("设置static模式，ip为" + ip + "失败或超时，中断"));
            return;
        }

        //检测外网
        testInternet("http://www.baidu.com", "http://www.sina.cn", "http://www.qq.com");

        restoreWanSettings();

        //完成
        callbackComplete();
    }

}
