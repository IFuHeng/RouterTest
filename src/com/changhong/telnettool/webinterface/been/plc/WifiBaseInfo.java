package com.changhong.telnettool.webinterface.been.plc;


import com.changhong.telnettool.webinterface.been.BaseResponseBeen;

public class WifiBaseInfo extends BaseResponseBeen {
    private Integer ssid_enable;//		SSID使能 1:使能 0:禁用
    private Integer ssid_hide;//		SSID广播 1:隐藏广播 0:显示广播
    private Integer wmm_enable;//			多媒体开关 1:使能 0:关闭
    private String name;//		SSID 0~27位数字或字母或-
    private Integer auth_mode;//			认证模式  :open 2:wpa-psk 3:wpa2-psk 4:wpa/wp2-psk
    private Integer encryption;//		加密方式 0:NONE 2:TKIP 3:AES 4:TKIP&AES
    private String key;//	 when auth_mode is 2/3/4	8~63个ascii字符
    private Integer update_time;//		WPA密钥更新时间
    private Integer wps;//		0: close  1: push button 2: ap-pin  3: client-pin
    private String local_pin;//	 when wps is 2	8~63个ascii字符
    private String client_pin;//	 when wps is 3	8~63个ascii字符
    private Integer ssid_idx;

    public Integer getSsid_enable() {
        return ssid_enable;
    }

    public void setSsid_enable(Integer ssid_enable) {
        this.ssid_enable = ssid_enable;
    }

    public Integer getSsid_hide() {
        return ssid_hide;
    }

    public void setSsid_hide(Integer ssid_hide) {
        this.ssid_hide = ssid_hide;
    }

    public Integer getWmm_enable() {
        return wmm_enable;
    }

    public void setWmm_enable(Integer wmm_enable) {
        this.wmm_enable = wmm_enable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAuth_mode() {
        return auth_mode;
    }

    public void setAuth_mode(Integer auth_mode) {
        this.auth_mode = auth_mode;
    }

    public Integer getEncryption() {
        return encryption;
    }

    public void setEncryption(Integer encryption) {
        this.encryption = encryption;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Integer update_time) {
        this.update_time = update_time;
    }

    public Integer getWps() {
        return wps;
    }

    public void setWps(Integer wps) {
        this.wps = wps;
    }

    public String getLocal_pin() {
        return local_pin;
    }

    public void setLocal_pin(String local_pin) {
        this.local_pin = local_pin;
    }

    public String getClient_pin() {
        return client_pin;
    }

    public void setClient_pin(String client_pin) {
        this.client_pin = client_pin;
    }

    public void setSsid_idx(Integer ssid_idx) {
        this.ssid_idx = ssid_idx;
    }
}
