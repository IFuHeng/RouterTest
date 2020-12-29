package com.changhong.telnettool.webinterface.been.guide;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

public class RequireAllBeen {


    /**
     * 1为WEB,2为APP，3为其他
     */
    private Integer src_type = 1;

    /**
     * static，dhcp，pppoe
     */
    private String type;
    /**
     * when type is static	需校验输入值是否为有效IP
     */
    private String ipaddr;
    /**
     * 校验netmask
     */
    private String netmask;
    /**
     * 需校验输入值是否为有效IP
     */
    private String gw;
    /**
     * 需校验输入值是否为有效IP
     */
    private String dns1;
    /**
     * 需校验输入值是否为有效IP
     */
    private String dns2;
    /**
     * required when type is pppoe	校验长度不超过128位
     */
    private String pppoe_username;
    /**
     * 校验长度不超过128位
     */
    private String pppoe_password;
    /**
     * 1：保存并执行；0：只保存数据不执行。（可以先保存后面统一执行）
     */
    private Integer save_action;


    /**
     * 不超过32个字符
     */
    private String ssid;
    private String ssid_2G;
    private String ssid_5G;
    /**
     * 不超过64个字符
     */
    private String key;
    /**
     * string	required	none/wpa2-psk/wpa2_mixed_psk
     */
    private String encryption;
    /**
     * 0:不同步到登录密码；1：同步
     */
    private Integer key_sync;

    /**
     * 1：5g优先；0：2.4g优先
     */
    @JSONField(name = "5G_priority")
    public Integer _5G_priority;


    /**
     * Integer	Required	1 为设置完成。0 为设置失败或取消掉了。2为只设置完登陆密码，而后的设置向导并非完成。
     */
    private Integer guid_flag;

    public RequireAllBeen() {
    }


    @Override
    public String toString() {
        return "RequireAllBeen{" +
                "src_type=" + src_type +
                ", type='" + type + '\'' +
                ", ipaddr='" + ipaddr + '\'' +
                ", netmask='" + netmask + '\'' +
                ", gw='" + gw + '\'' +
                ", dns1='" + dns1 + '\'' +
                ", dns2='" + dns2 + '\'' +
                ", pppoe_username='" + pppoe_username + '\'' +
                ", pppoe_password='" + pppoe_password + '\'' +
                ", save_action=" + save_action +
                ", ssid='" + ssid + '\'' +
                ", key='" + key + '\'' +
                ", encryption='" + encryption + '\'' +
                ", key_sync=" + key_sync +
                ", _5G_priority=" + _5G_priority +
                ", guid_flag=" + guid_flag +
                '}';
    }

    public String toJsonString() {
        return JSONObject.toJSONString(this);
    }

    public void setSrc_type(Integer src_type) {
        this.src_type = src_type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }

    public void setNetmask(String netmask) {
        this.netmask = netmask;
    }

    public void setGw(String gw) {
        this.gw = gw;
    }

    public void setDns1(String dns1) {
        this.dns1 = dns1;
    }

    public void setDns2(String dns2) {
        this.dns2 = dns2;
    }

    public void setPppoe_username(String pppoe_username) {
        this.pppoe_username = pppoe_username;
    }

    public void setPppoe_password(String pppoe_password) {
        this.pppoe_password = pppoe_password;
    }

    public void setSave_action(Integer save_action) {
        this.save_action = save_action;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }

    public void setKey_sync(Integer key_sync) {
        this.key_sync = key_sync;
    }

    public void set_5G_priority(Integer _5G_priority) {
        this._5G_priority = _5G_priority;
    }

    public void setGuid_flag(Integer guid_flag) {
        this.guid_flag = guid_flag;
    }

    public Integer getSrc_type() {
        return src_type;
    }

    public String getType() {
        return type;
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public String getNetmask() {
        return netmask;
    }

    public String getGw() {
        return gw;
    }

    public String getDns1() {
        return dns1;
    }

    public String getDns2() {
        return dns2;
    }

    public String getPppoe_username() {
        return pppoe_username;
    }

    public String getPppoe_password() {
        return pppoe_password;
    }

    public Integer getSave_action() {
        return save_action;
    }

    public String getSsid() {
        return ssid;
    }

    public String getKey() {
        return key;
    }

    public String getEncryption() {
        return encryption;
    }

    public Integer getKey_sync() {
        return key_sync;
    }

    public Integer get_5G_priority() {
        return _5G_priority;
    }

    public Integer getGuid_flag() {
        return guid_flag;
    }

    public String getSsid_2G() {
        return ssid_2G;
    }

    public void setSsid_2G(String ssid_2G) {
        this.ssid_2G = ssid_2G;
    }

    public String getSsid_5G() {
        return ssid_5G;
    }

    public void setSsid_5G(String ssid_5G) {
        this.ssid_5G = ssid_5G;
    }
}
