package com.changhong.telnettool.been;

import java.io.*;
import java.util.Objects;
import java.util.regex.Pattern;

public class WlanInfo {
    String GUID;//                   : 3faa2b9a-bac9-41e1-887e-d5d960f8b997
    String mac;// 物理地址               : 08:d2:3e:a0:7f:21
    String state;//状态
    String SSID;//                   : crispin_mi_5G
    String BSSID;//                  : a0:b0:c0:d0:00:0b
    String curType;//网络类型               : 结构
    String wlanType;// 无线电类型             : 802.11ac
    String authentication;//身份验证               : WPA2 - 个人
    String password;//密码                   : CCMP
    boolean aotuConnect;//连接模式               : 自动连接
    int channel;//信道                   : 64
    float RX;//接收速率(Mbps)         : 390
    float TX;//传输速率 (Mbps)        : 325
    String rxUnit;
    String txUnit;
    int per_signal_strength;//信号                   : 68%

    public WlanInfo() {
    }

    public WlanInfo(InputStream is) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "GBK"));
            for (String temp = bufferedReader.readLine(); temp != null; temp = bufferedReader.readLine()) {
                temp = temp.trim();
                if (temp.indexOf(':') == -1)
                    continue;
                String value = temp.substring(temp.indexOf(':') + 1).trim();
                if (temp.startsWith("GUID")) {
                    GUID = value;
                } else if (temp.startsWith("物理地址")) {
                    mac = value.toUpperCase();
                } else if (temp.startsWith("状态")) {
                    state = value;
                } else if (temp.startsWith("SSID")) {
                    SSID = value;
                } else if (temp.startsWith("BSSID")) {
                    BSSID = value.toUpperCase();
                } else if (temp.contains("网络类型")) {
                    curType = value;
                } else if (temp.contains("无线电类型")) {
                    wlanType = value;
                } else if (temp.contains("身份验证")) {
                    authentication = value;
                } else if (temp.contains("密码")) {
                    password = value;
                } else if (temp.contains("连接模式")) {
                    aotuConnect = "是".equals(value);
                } else if (temp.contains("信道")) {
                    channel = Integer.parseInt(value);
                } else if (temp.contains("接收速率")) {
                    RX = Float.parseFloat(value);
                    rxUnit = temp.substring(temp.indexOf('(') + 1, temp.indexOf(')'));
                } else if (temp.contains("传输速率")) {
                    TX = Float.parseFloat(value);
                    txUnit = temp.substring(temp.indexOf('(') + 1, temp.indexOf(')'));
                } else if (temp.contains("信号")) {
                    per_signal_strength = Integer.parseInt(Pattern.compile("[^0-9]").matcher(value).replaceAll(""));
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getGUID() {
        return GUID;
    }

    public String getMac() {
        return mac;
    }

    public String getState() {
        return state;
    }

    public String getSSID() {
        return SSID;
    }

    public String getBSSID() {
        return BSSID;
    }

    public String getCurType() {
        return curType;
    }

    public String getWlanType() {
        return wlanType;
    }

    public String getAuthentication() {
        return authentication;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAotuConnect() {
        return aotuConnect;
    }

    public int getChannel() {
        return channel;
    }

    public float getRX() {
        return RX;
    }

    public float getTX() {
        return TX;
    }

    public String getRxUnit() {
        return rxUnit;
    }

    public String getTxUnit() {
        return txUnit;
    }

    public int getPer_signal_strength() {
        return per_signal_strength;
    }

    @Override
    public String toString() {
        return "WlanInfo{" +
                "GUID='" + GUID + '\'' +
                ", mac='" + mac + '\'' +
                ", state='" + state + '\'' +
                ", SSID='" + SSID + '\'' +
                ", BSSID='" + BSSID + '\'' +
                ", curType='" + curType + '\'' +
                ", wlanType='" + wlanType + '\'' +
                ", authentication='" + authentication + '\'' +
                ", password='" + password + '\'' +
                ", aotuConnect=" + aotuConnect +
                ", channel=" + channel +
                ", RX=" + RX + rxUnit +
                ", TX=" + TX + txUnit +
                ", per_signal_strength=" + per_signal_strength + "%" +
                '}';
    }

    public static final synchronized WlanInfo load() {
        try {
            InputStream is = Runtime.getRuntime().exec("netsh wlan show interfaces").getInputStream();
            return new WlanInfo(is);
        } catch (IOException e) {
            e.printStackTrace();
            return new WlanInfo();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof WlanInfo))
            return super.equals(obj);

        WlanInfo other = (WlanInfo) obj;
        return other.per_signal_strength == this.per_signal_strength
                && other.aotuConnect == this.aotuConnect
                && other.channel == this.channel
                && other.RX == this.RX
                && other.TX == this.TX
                && Objects.equals(other.authentication, this.authentication)
                && Objects.equals(other.BSSID, this.BSSID)
                && Objects.equals(other.SSID, this.SSID)
                && Objects.equals(other.curType, this.curType)
                && Objects.equals(other.GUID, this.GUID)
                && Objects.equals(other.mac, this.mac)
                && Objects.equals(other.rxUnit, this.rxUnit)
                && Objects.equals(other.txUnit, this.txUnit)
                && Objects.equals(other.state, this.state)
                ;
    }

}
