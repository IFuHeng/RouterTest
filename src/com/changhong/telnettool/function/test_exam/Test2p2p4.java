package com.changhong.telnettool.function.test_exam;

import com.changhong.telnettool.tool.Tool;
import com.changhong.telnettool.webinterface.BWR510LocalConnectionHelper;
import com.changhong.telnettool.webinterface.Observer;
import com.changhong.telnettool.webinterface.been.EnumPPPoeState;
import com.changhong.telnettool.webinterface.been.wan.WanResponseAllBeen;
import javafx.util.Pair;

/**
 * 2.2.4. PPPOE 上网 上网 功能
 */
public class Test2p2p4 extends WanSettingBase implements Runnable {
    private String account;
    private String pppoePwd;

    public Test2p2p4() {
    }

    public Test2p2p4(String password, String account, String pppoePwd, Observer<Pair<Boolean, String>> callback) {
        super(password, callback);
        this.account = account;
        this.pppoePwd = pppoePwd;
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

        // step 1 设置路由器通过 PPPOE 拨号方式上网，并输入错误的 PPPOE 账号和密码
        callbackNext(true, "step 1:设置路由器通过 PPPOE 拨号方式上网，并输入错误的 PPPOE 账号和密码");
        if (!setWanPPPoe(Tool.turn2IpV4((int) Math.round(Math.random() * Integer.MAX_VALUE) & 0xffffffff),
                Tool.turn2IpV4((int) Math.round(Math.random() * Integer.MAX_VALUE) & 0xffffffff))) {
            callbackError(new Exception("设置pppoe模式失败，中断"));
            return;
        } else {
            waitReceivePPPoeConnecting(20);
        }

        try {// step 2: 查看获取到的IP
            callbackNext(false, "step 2: 查看获取到的IP……");
            String ip = BWR510LocalConnectionHelper.getInstance().getWan(mGateWay).getIpaddr();
            callbackNext(true, "ip = " + ip);
        } catch (Exception e) {
            e.printStackTrace();
            callbackNext(true, "失败");
        }


        // step 3 设置为 DHCP自动获取IP 方式获取 IP 后，访问任意 WEB
        if (setWanDhcp() && waitReceiveIp(20)) {
            testInternet("http://www.baidu.com", "http://www.sina.cn", "http://www.qq.com");
        }

        //step 4 设置为PPPOE模式
        callbackNext(true, "step 4:设置为PPPOE模式");
        if (!setWanPPPoe(account, pppoePwd) || !waitReceivePPPoeConnecting(20)) {
            callbackError(new Exception("设置pppoe失败，中断"));
            return;
        }

        testInternet("http://www.baidu.com", "http://www.sina.cn", "http://www.qq.com");

        try {// step 5: 查看获取到的IP
            callbackNext(false, "step 5: 查看获取到的IP……");
            String ip = BWR510LocalConnectionHelper.getInstance().getWan(mGateWay).getIpaddr();
            callbackNext(true, "ip = " + ip);
        } catch (Exception e) {
            e.printStackTrace();
            callbackNext(true, "失败");
        }

        //检测外网
        testInternet("http://www.baidu.com", "http://www.sina.cn", "http://www.qq.com");

        //测试完，还原
        restoreWanSettings();

        callbackComplete();
    }
}
