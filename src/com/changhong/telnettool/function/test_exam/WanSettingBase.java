package com.changhong.telnettool.function.test_exam;

import com.changhong.telnettool.tool.Tool;
import com.changhong.telnettool.webinterface.BWR510LocalConnectionHelper;
import com.changhong.telnettool.webinterface.Observer;
import com.changhong.telnettool.webinterface.been.EnumPPPoeState;
import com.changhong.telnettool.webinterface.been.sys.SettingResponseAllBeen;
import com.changhong.telnettool.webinterface.been.wan.WanRequireAllBeen;
import com.changhong.telnettool.webinterface.been.wan.WanResponseAllBeen;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 2.2.2. DHCP  自动获取 IP  上网 功能
 */
public abstract class WanSettingBase {
    protected String password;
    private Observer<Pair<Boolean, String>> callback;
    protected final String mGateWay;
    private WanResponseAllBeen mWanSettingBack;

    public WanSettingBase() {
        mGateWay = Tool.getGuessGateway();
    }

    public WanSettingBase(String password, Observer<Pair<Boolean, String>> callback) {
        this();
        this.password = password;
        this.callback = callback;
    }

    protected boolean login() {
        try {
            callbackNext(false, "登录中……");
            BWR510LocalConnectionHelper.getInstance().login(mGateWay, password);
            callbackNext(true, "成功");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            if (callback != null) callback.onError(e);
        }
        return false;
    }

    protected boolean getDeviceType() {
        //获得类型
        try {
            callbackNext(false, "获取设备类型中……");
            String deviceType = BWR510LocalConnectionHelper.getInstance().getBase_DeviceType(mGateWay);
            callbackNext(true, deviceType);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null) callback.onError(e);
        }
        return false;
    }

    protected void testInternet(String... urls) {
        if (urls == null || urls.length == 0)
            return;

        for (String url : urls) {
            try {
                callbackNext(false, "检测是否能访问" + url + "……");
                URLConnection connection = new URL(url).openConnection();
                connection.setConnectTimeout(5000);
                connection.connect();
//                System.out.println("connection.getContent = " + connection.getContent());
                callbackNext(true, url + "访问成功");
            } catch (IOException e) {
//                e.printStackTrace();
                if (callback != null) callback.onError(e);
                callbackNext(true, "无法访问" + url);
            }
        }
    }

    private void restoreWanSettings(String gateway, WanResponseAllBeen wanSettingBack) {
        // 还原wan口原始设置
        if (wanSettingBack != null) {
            WanRequireAllBeen been = new WanRequireAllBeen(wanSettingBack);

            try {
                callbackNext(false, "还原WAN口设置中……");
                BWR510LocalConnectionHelper.getInstance().setWan(gateway, been);
                callbackNext(true, "还原完成");
            } catch (Exception e) {
                e.printStackTrace();
                if (callback != null) callback.onError(e);
                callbackNext(true, "还原失败，请用户手动还原。");
            }
        }
    }

    boolean waitForGotIp(String aimIp, int retryTimes) {
        callbackNext(false, "校验路由器wan口是否ip设置为" + aimIp + "……");
        for (int i = 0; i < retryTimes; i++) {
            try {
                WanResponseAllBeen obj = BWR510LocalConnectionHelper.getInstance().getWan(mGateWay);
                if (obj.getIpaddr().equalsIgnoreCase(aimIp)) {
                    callbackNext(true, "当前路由ip为：" + obj.getIpaddr() + " ， 成功");
                    return true;
                } else Thread.sleep(1000);

                if (i == retryTimes - 1)
                    callbackNext(true, "当前路由ip为：" + obj.getIpaddr() + " ,  失败");
            } catch (Exception e) {
                if (callback != null) callback.onError(e);
            }
        }
        return false;
    }

    boolean waitReceiveIp(int... retryTimes) {
        callbackNext(false, "等待获取到IP地址中……");
        int retryTime = 10;
        if (retryTimes != null && retryTimes.length > 0 && retryTimes[0] > 0) {
            retryTime = retryTimes[0];
        }
        while (retryTime-- > 0) {
            try {
                SettingResponseAllBeen obj = BWR510LocalConnectionHelper.getInstance().getBase_RouterInfo(mGateWay);
                if (obj.getWan_gw().indexOf('0') != 0) {
                    callbackNext(true, "成功");
                    return true;
                } else sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        callbackNext(true, "获取IP地址超时");
        return false;
    }

    boolean waitReceivePPPoeConnecting(int retryTimes) {
        callbackNext(false, "等待PPPoe连接……");
        for (int i = 0; i < retryTimes; i++) {
            try {
                EnumPPPoeState type = BWR510LocalConnectionHelper.getInstance().checkPPPoeState(mGateWay);
                callbackNext(true, type.getName());
                if (type == EnumPPPoeState.CONNECTED) {
                    callbackNext(true, "成功");
                    return true;
                }

                callbackNext(false, type.getName() + "……");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        callbackNext(true, "超时");
        return false;
    }

    /**
     * 显示路由器基本信息
     */
    String showDeviceBaseInfo() {
        try {
            callbackNext(false, "获取设备基本信息中……");
            SettingResponseAllBeen obj = BWR510LocalConnectionHelper.getInstance().getBase_RouterInfo(mGateWay);
            callbackNext(true, "获取基本信息成功：" + obj.toRouterRuntimeInfoString());
            return obj.getWan_type();
        } catch (Exception e) {
            e.printStackTrace();
            callbackNext(true, "获取基本信息失败");
            return null;
        }
    }

    boolean setWanPPPoe(String account, String password) {
        WanRequireAllBeen been = new WanRequireAllBeen(mWanSettingBack);
        been.setPppoe_username(account);
        been.setPppoe_password(password);
        been.setType("pppoe");

        try {
            callbackNext(false, "设置为pppoe模式……");
            BWR510LocalConnectionHelper.getInstance().setWan(mGateWay, been);
            callbackNext(true, "成功");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            callbackNext(true, "失败");
            return false;
        }
    }

    boolean setWanStatic(String ip, String gateway, String mask, String dns1, String dns2) {
        WanRequireAllBeen been = new WanRequireAllBeen(mWanSettingBack);
        been.setIpaddr(ip);
        been.setNetmask(mask);
        been.setGw(gateway);
        been.setDns1(dns1);
        been.setDns2(dns2);
        been.setType("static");

        try {
            callbackNext(false, "设置为static模式……");
            BWR510LocalConnectionHelper.getInstance().setWan(mGateWay, been);
            callbackNext(true, "成功");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            callbackNext(true, "失败");
            return false;
        }
    }

    boolean setWanDhcp() {
        WanRequireAllBeen been = new WanRequireAllBeen(mWanSettingBack);
        been.setType("dhcp");

        try {
            callbackNext(false, "设置为dhcp模式……");
            BWR510LocalConnectionHelper.getInstance().setWan(mGateWay, been);
            callbackNext(true, "成功");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            callbackNext(true, "失败");
            return false;
        }
    }

    /**
     * 备份原始设置
     *
     * @return 备份成功or失败
     */
    boolean saveBackSetting() {
        callbackNext(false, "当前WAN口模式非dhcp，备份当前WAN口设置并切换到dhcp模式中……");
        try {
            mWanSettingBack = BWR510LocalConnectionHelper.getInstance().getWan(mGateWay);
            callbackNext(true, "备份成功");
            return true;
        } catch (Exception e) {
            callbackNext(false, "备份失败");
            e.printStackTrace();
            return false;
        }
    }

    void restoreWanSettings() {
        // 还原wan口原始设置
        if (mWanSettingBack != null) {
            WanRequireAllBeen been = new WanRequireAllBeen(mWanSettingBack);

            try {
                callbackNext(false, "还原WAN口设置中……");
                BWR510LocalConnectionHelper.getInstance().setWan(mGateWay, been);
                callbackNext(true, "还原完成");
            } catch (Exception e) {
                e.printStackTrace();
                if (callback != null) callback.onError(e);
                callbackNext(true, "还原失败，请用户手动还原。");
            }
        }
    }

    void sleep(long millis) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void callbackNext(boolean isResult, String msg) {
        if (callback != null) callback.onNext(new Pair<>(isResult, msg));
    }

    void callbackError(Throwable e) {
        if (callback != null) callback.onError(e);
    }

    void callbackComplete() {
        if (callback != null) callback.onComplete();
    }

}
