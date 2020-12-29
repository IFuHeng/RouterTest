package com.changhong.telnettool.webinterface.been.wifi;

import java.util.Objects;

public class WlanLevel2Been {
    private String name;
    private String mac;

    /**
     * 不超过32字符
     */
    private String ssid;
    /**
     * Mac地址
     */
    private String bssid;
    /**
     * 信道
     */
    private Integer channel;
    /**
     * 3: a;7:n; 11:an; 63: ac; 71: n+ac;75: a+n+ac, 0:b;1:g;2:bg;7:n;9:gn;10:bgn
     */
    private Integer type;
    /**
     * none/wpa2-psk/wpa2_mixed_psk
     */
    private String encrypt;
    /**
     * 信号强度
     */
    private Integer signal;

    public WlanLevel2Been() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(String encrypt) {
        this.encrypt = encrypt;
    }

    public Integer getSignal() {
        return signal;
    }

    public void setSignal(Integer signal) {
        this.signal = signal;
    }

    @Override
    public String toString() {
        return "Level2Been{" +
                "name='" + name + '\'' +
                ", mac='" + mac + '\'' +
                ", ssid='" + ssid + '\'' +
                ", bssid='" + bssid + '\'' +
                ", channel=" + channel +
                ", type=" + type +
                ", encrypt='" + encrypt + '\'' +
                ", signal=" + signal +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WlanLevel2Been)) return false;
        WlanLevel2Been that = (WlanLevel2Been) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(mac, that.mac) &&
                Objects.equals(ssid, that.ssid) &&
                Objects.equals(bssid, that.bssid) &&
                Objects.equals(channel, that.channel) &&
                Objects.equals(type, that.type) &&
                Objects.equals(encrypt, that.encrypt) &&
                Objects.equals(signal, that.signal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, mac, ssid, bssid, channel, type, encrypt, signal);
    }
}
