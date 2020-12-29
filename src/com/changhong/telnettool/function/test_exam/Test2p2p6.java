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
 * 2.2.6. 无线加密功能测试
 */
public class Test2p2p6 extends Test2p2p5 {

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


        try {//step3 ： 分别连接 2.4G和 5G的ssid , 用错误密码
            DosCmdHelper.createWlanProfile(ssid2$4, ssid2$4, pwd2$4 + "1");
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
        try {//step3-2 ： 分别连接 2.4G和 5G的ssid, 用错误密码
            String profileName = ssid5.equals(ssid2$4) ? ssid5 + "_2" : ssid5;
            DosCmdHelper.createWlanProfile(profileName, ssid5, pwd5 + "1");
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

        try {//step4 ： 分别连接 2.4G和 5G的ssid
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
        try {//step4-2 ： 分别连接 2.4G和 5G的ssid
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

        callbackComplete();
    }

}
