package com.changhong.telnettool.been;

import java.util.Arrays;

public class BSSIDPoint {

    String mac;
    int per_signal_strength;
    String wirelessType;//无线电类型
    int channel;
    float[] baseSpeed;
    float[] otherSpeed;
    String speedUnit;

    public String getMac() {
        return mac;
    }

    public int getPer_signal_strength() {
        return per_signal_strength;
    }

    public String getWirelessType() {
        return wirelessType;
    }

    public int getChannel() {
        return channel;
    }

    public float[] getBaseSpeed() {
        return baseSpeed;
    }

    public float[] getOtherSpeed() {
        return otherSpeed;
    }

    public String getSpeedUnit() {
        return speedUnit;
    }

    /**
     * SSID 1 : ceshizu-baoxiao
     * Network type            : 结构
     * 身份验证                : WPA2 - 个人
     * 加密                    : CCMP
     * BSSID 1               : fc:d7:33:d4:cf:08
     * 信号               : 83%
     * 无线电类型         : 802.11n
     * 频道               : 1
     * 基本速率(Mbps)     : 1 2 5.5 11
     * 其他速率(Mbps)     : 6 9 12 18 24 36 48 54
     */

    @Override
    public String toString() {
        return "BSSIDPoint{" +
                "mac='" + mac + '\'' +
                ", per_signal_strength=" + per_signal_strength + "%" +
                ", wirelessType='" + wirelessType + '\'' +
                ", channel=" + channel +
                ", baseSpeed(" + speedUnit + ")=" + Arrays.toString(baseSpeed) +
                ", otherSpeed(" + speedUnit + ")=" + Arrays.toString(otherSpeed) +
                '}';
    }
}
