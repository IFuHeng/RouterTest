package com.changhong.telnettool.function.test_exam;

import com.changhong.telnettool.been.SSIDInfo;
import com.changhong.telnettool.been.WlanBssidList;
import com.changhong.telnettool.task.DosCmdHelper;
import com.changhong.telnettool.webinterface.BWR510LocalConnectionHelper;
import com.changhong.telnettool.webinterface.Observer;
import com.changhong.telnettool.webinterface.been.wifi.WifiRequireAllBeen;
import com.changhong.telnettool.webinterface.been.wifi.WifiResponseAllBeen;
import javafx.util.Pair;

/**
 * 2.2.5. 无线 SSID  修改 功能 测试
 */
public class Test2p2p5 extends WanSettingBase implements Runnable {
    protected String ssid2$4, pwd2$4, ssid5, pwd5;

    public Test2p2p5() {
    }

    public Test2p2p5(String password, String ssid2$4, String pwd2$4, String ssid5, String pwd5, Observer<Pair<Boolean, String>> callback) {
        super(password, callback);
        this.ssid2$4 = ssid2$4;
        this.pwd2$4 = pwd2$4;
        this.ssid5 = ssid5;
        this.pwd5 = pwd5;
    }

    @Override
    public void run() {

        //获得类型
        if (!getDeviceType())
            return;

        //登录
        if (!login())
            return;

        Pair<WifiResponseAllBeen, WifiResponseAllBeen> wifiSettings = null;
        try {
            callbackNext(false, "读取wifi设置中……");
            wifiSettings = BWR510LocalConnectionHelper.getInstance().getWifiSetting(mGateWay);
            callbackNext(true, "成功\n" +
                    "    2.4G SSID = " + wifiSettings.getKey().getSsid() + "\n    5G SSID = " + wifiSettings.getValue().getSsid() + "\n    双频优选: " + (wifiSettings.getKey().getPrefer_5g() == 1 ? '开' : '关'));
        } catch (Exception e) {
            e.printStackTrace();
            callbackError(new Exception("未获取到wifi设置，中断"));
            return;
        }

        try {//step 2：通过路由器管理 web 页面分别设置 2.4G与 5G 的 SSID
            callbackNext(false, "step 2：通过路由器管理 web 页面分别设置 2.4G 与 5G 的 SSID……");
            WifiRequireAllBeen wifiSetting2$4G = new WifiRequireAllBeen(wifiSettings.getKey());
            wifiSetting2$4G.setSsid(ssid2$4);
            wifiSetting2$4G.setPrefer_5g(0);
            wifiSetting2$4G.setKey(pwd2$4);
            wifiSetting2$4G.setEncryption(pwd2$4 == null || pwd2$4.length() == 0 ? "none" : "wpa2_mixed_psk");
            wifiSetting2$4G.setFlag(1);
            wifiSetting2$4G.setSave_action(0);
            WifiRequireAllBeen wifiSetting5G = new WifiRequireAllBeen(wifiSettings.getValue());
            wifiSetting5G.setSsid(ssid5);
            wifiSetting5G.setPrefer_5g(0);
            wifiSetting5G.setKey(pwd5);
            wifiSetting5G.setFlag(2);
            wifiSetting5G.setSave_action(1);
            wifiSetting5G.setEncryption(pwd2$4 == null || pwd2$4.length() == 0 ? "none" : "wpa2_mixed_psk");
            BWR510LocalConnectionHelper.getInstance().setWifiSetting(mGateWay, wifiSetting2$4G, wifiSetting5G);
            callbackNext(true, "OK");
        } catch (Exception e) {
            e.printStackTrace();
            callbackError(new Exception("设置wifi失败，中断"));
            return;
        }

        callbackNext(true, "等待10秒让路由器完成操作……");
        waitForSecounds(10);


        try {//step3 ： 分别连接 2.4G和 5G的ssid
            DosCmdHelper.createWlanProfile(ssid2$4, ssid2$4, pwd2$4);
            callbackNext(true, "等待3秒让wifi完成连接……");
            waitForSecounds(3);

            callbackNext(false, "检查当前ssid……");
            String tempSSID = DosCmdHelper.getCurrentWlanSSID();

            if (tempSSID == null)
                callbackError(new RuntimeException("未获取到ssid，失败"));
            else
                callbackNext(true, "连接到" + ssid2$4 + (tempSSID.equals(ssid2$4) ? "成功" : "失败"));

            DosCmdHelper.readDosRuntime("netsh wlan delete profile " + ssid2$4);
        } catch (Exception e) {
            callbackError(e);
        }
        try {//step3-2 ： 分别连接 2.4G和 5G的ssid
            String profileName = ssid5.equals(ssid2$4) ? ssid5 + "_2" : ssid5;
            DosCmdHelper.createWlanProfile(profileName, ssid5, pwd5);
            callbackNext(true, "等待3秒让wifi完成连接……");
            waitForSecounds(3);

            callbackNext(false, "检查当前ssid……");
            String tempSSID = DosCmdHelper.getCurrentWlanSSID();

            if (tempSSID == null)
                callbackError(new RuntimeException("未获取到ssid，失败"));
            else
                callbackNext(true, "连接到" + profileName + (tempSSID.equals(ssid5) ? "成功" : "失败"));

            DosCmdHelper.readDosRuntime("netsh wlan delete profile " + profileName);
        } catch (Exception e) {
            callbackError(e);
        }


        {// step 4: PC 分别通过 2.4G 与 5G 无线连接路由器 广播出的新的 SSID
            callbackNext(false, "判断周围SSID中是否有目标SSID……");
            WlanBssidList wlanBssids = WlanBssidList.load();
            boolean contain2$4G = false;
            boolean contain5G = false;
            for (SSIDInfo ssidInfo : wlanBssids.getArrSSID()) {
                if (ssidInfo.getSSID().equals(ssid2$4))
                    contain2$4G = true;
                if (ssidInfo.getSSID().equals(ssid5))
                    contain5G = true;
                if (contain2$4G && contain5G)
                    break;
            }
            if (contain2$4G && contain5G)
                callbackNext(true, ssid2$4 + "和" + ssid5 + " 都成功被发现");
            else if (contain2$4G)
                callbackNext(true, "仅发现 " + ssid2$4 + " (2.4G)");
            else if (contain5G)
                callbackNext(true, "仅发现 " + ssid5 + " (5G)");
            else
                callbackNext(true, "失败");
        }

        callbackComplete();
    }

    protected void waitForSecounds(int secounds) {
        for (int i = 0; i < secounds; i++) {
            callbackNext(true, (secounds - i) + "秒");
            sleep(1000);
        }
        callbackNext(true, "等待结束");
    }
}
