package com.changhong.telnettool.function.test_exam;

import com.changhong.telnettool.tool.Tool;
import com.changhong.telnettool.webinterface.Observer;
import javafx.util.Pair;

/**
 * 2.2.3. 静态 IP  上网 功能
 */
public class Test2p2p3 extends WanSettingBase implements Runnable {
    private String ip;
    private String gateway;
    private String mask;
    private String dns1;
    private String dns2;

    public Test2p2p3() {
    }

    public Test2p2p3(String password, String ip, String gateway, String mask, String dns1, String dns2, Observer<Pair<Boolean, String>> callback) {
        super(password, callback);
        this.ip = ip;
        this.gateway = gateway;
        this.mask = mask;
        this.dns1 = dns1;
        this.dns2 = dns2;
    }

    @Override
    public void run() {
        String tempIp = null;

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

        // 检测当前设备基本信息和WAN口是否为dhcp
        // step 1
        callbackNext(true, "step 1:设置为随机IP的static模式");
        String ip = Tool.turn2IpV4((int) Math.round(Math.random() * Integer.MAX_VALUE) & 0xffffffff);
        boolean is = setWanStatic(ip,
                Tool.turn2IpV4((int) Math.round(Math.random() * Integer.MAX_VALUE) & 0xffffffff),
                Tool.turn2IpV4((int) Math.round(Math.random() * Integer.MAX_VALUE) & 0xffffffff),
                Tool.turn2IpV4((int) Math.round(Math.random() * Integer.MAX_VALUE) & 0xffffffff),
                Tool.turn2IpV4((int) Math.round(Math.random() * Integer.MAX_VALUE) & 0xffffffff));

        // step 2
        callbackNext(true, "step 2:检查IP 与设置的相同");
        if (!is || !waitForGotIp(ip, 20)) {
            callbackError(new Exception("设置static模式，ip为" + ip + "失败或超时，中断"));
            return;
        }

        //step 3 设置为DHCP模式
        callbackNext(true, "step 3:设置为DHCP模式");
        setWanDhcp();
        waitReceiveIp(20);
        testInternet("http://www.sina.cn");

        // step4 设置为目标static模式
        setWanStatic(ip, gateway, mask, dns1, dns2);
        waitForGotIp(ip, 20);

        //检测外网
        testInternet("http://www.baidu.com", "http://www.sina.cn", "http://www.qq.com");

        //测试完，还原
        restoreWanSettings();

        callbackComplete();
    }
}
