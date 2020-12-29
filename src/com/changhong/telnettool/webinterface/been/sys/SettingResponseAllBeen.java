package com.changhong.telnettool.webinterface.been.sys;

import com.changhong.telnettool.webinterface.been.BaseResponseBeen;

public class SettingResponseAllBeen extends BaseResponseBeen {

    /**
     * 上电的秒数
     */
    private Integer uptime;
    /**
     * Wan口mac地址
     */
    private String wan_mac;
    /**
     * Wan口IP，0.0.0.0：还未获取到IP，none_link：表示WAN口没有插入网线。
     */
    private String wan_ip;
    /**
     * Wan口默认网关，未获取到为0.0.0.0
     */
    private String wan_gw;
    /**
     * Wan口子网掩码，未获取到为0.0.0.0
     */
    private String wan_netmask;
    /**
     * Lan口mac地址
     */
    private String lan_mac;
    /**
     * lan口（br0）IP，未获取到为0.0.0.0
     */
    private String lan_ip;
    /**
     * lan口子网掩码，未获取到为0.0.0.0
     */
    private String lan_netmask;
    /**
     * Dns服务器IP，未获取到为0.0.0.0
     */
    private String DNS;
    /**
     * 序列号（不超过32位）
     */
    private String SN;
    /**
     * 上网时间秒数
     */
    private Integer network_time;
    /**
     * 软件版本（不超过32位）
     */
    private String soft_ver;
    /**
     * 产品名称（不超过32位）
     */
    private String equipment;
    /**
     * static，dhcp，pppoe
     */
    private String wan_type;
    /**
     * 1:表示必须自动检测wan口； 0:不必须自动检测wan口
     */
    private Integer must_check_wan;

    /**
     * 1 为英语。0 为汉语。
     */
    private Integer language_flag;


    /**
     * 设备型号：0、百兆；1、千兆；2、组网
     * 如果又是千兆又是组网就返回3（1 | 2=3）。
     */
    private Integer platform_type;
    /**
     * 设备类型，“R2s”：千兆路由器，“BWR-510”：分布式路由器等
     */
    private String dev_type;


    /**
     * 不超过32字符
     */
    private String user_password;
    /**
     * 不超过32字符
     */
    private String user_name;

    /**
     * 1:已同步；0:未同步
     */
    private Integer status;
    private String time_zone;
    /**
     * 从 1970 年 1 月 1 日至今的秒数
     */
    private Integer time;
    /**
     * 2019-4-25 17:24:18 3     年月日 时分秒 星期（0-6）
     */
    private String str_time;


    public Integer getUptime() {
        return uptime;
    }

    public String getWan_mac() {
        return wan_mac;
    }

    public String getWan_ip() {
        return wan_ip;
    }

    public String getWan_gw() {
        return wan_gw;
    }

    public String getWan_netmask() {
        return wan_netmask;
    }

    public String getLan_mac() {
        return lan_mac;
    }

    public String getLan_ip() {
        return lan_ip;
    }

    public String getLan_netmask() {
        return lan_netmask;
    }

    public String getDNS() {
        return DNS;
    }

    public String getSN() {
        return SN;
    }

    public Integer getNetwork_time() {
        return network_time;
    }

    public String getSoft_ver() {
        return soft_ver;
    }

    public String getEquipment() {
        return equipment;
    }

    public String getWan_type() {
        return wan_type;
    }

    public Integer getMust_check_wan() {
        return must_check_wan;
    }

    public Integer getLanguage_flag() {
        return language_flag;
    }

    public Integer getPlatform_type() {
        return platform_type;
    }

    public String getDev_type() {
        return dev_type;
    }

    public String getUser_password() {
        return user_password;
    }

    public String getUser_name() {
        return user_name;
    }

    public Integer getStatus() {
        return status;
    }

    public String getTime_zone() {
        return time_zone;
    }

    public Integer getTime() {
        return time;
    }

    public String getStr_time() {
        return str_time;
    }

    public void setUptime(Integer uptime) {
        this.uptime = uptime;
    }

    public void setWan_mac(String wan_mac) {
        this.wan_mac = wan_mac;
    }

    public void setWan_ip(String wan_ip) {
        this.wan_ip = wan_ip;
    }

    public void setWan_gw(String wan_gw) {
        this.wan_gw = wan_gw;
    }

    public void setWan_netmask(String wan_netmask) {
        this.wan_netmask = wan_netmask;
    }

    public void setLan_mac(String lan_mac) {
        this.lan_mac = lan_mac;
    }

    public void setLan_ip(String lan_ip) {
        this.lan_ip = lan_ip;
    }

    public void setLan_netmask(String lan_netmask) {
        this.lan_netmask = lan_netmask;
    }

    public void setDNS(String DNS) {
        this.DNS = DNS;
    }

    public void setSN(String SN) {
        this.SN = SN;
    }

    public void setNetwork_time(Integer network_time) {
        this.network_time = network_time;
    }

    public void setSoft_ver(String soft_ver) {
        this.soft_ver = soft_ver;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public void setWan_type(String wan_type) {
        this.wan_type = wan_type;
    }

    public void setMust_check_wan(Integer must_check_wan) {
        this.must_check_wan = must_check_wan;
    }

    public void setLanguage_flag(Integer language_flag) {
        this.language_flag = language_flag;
    }

    public void setPlatform_type(Integer platform_type) {
        this.platform_type = platform_type;
    }

    public void setDev_type(String dev_type) {
        this.dev_type = dev_type;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public void setStr_time(String str_time) {
        this.str_time = str_time;
    }

    public boolean isLinkOn() {
        return wan_ip != null && !"none_link".equals(wan_ip);
    }

    @Override
    public String toString() {
        return "SettingResponseAllBeen{" +
                "uptime=" + uptime +
                ", wan_mac='" + wan_mac + '\'' +
                ", wan_ip='" + wan_ip + '\'' +
                ", wan_gw='" + wan_gw + '\'' +
                ", wan_netmask='" + wan_netmask + '\'' +
                ", lan_mac='" + lan_mac + '\'' +
                ", lan_ip='" + lan_ip + '\'' +
                ", lan_netmask='" + lan_netmask + '\'' +
                ", DNS='" + DNS + '\'' +
                ", SN='" + SN + '\'' +
                ", network_time='" + network_time + '\'' +
                ", soft_ver='" + soft_ver + '\'' +
                ", equipment='" + equipment + '\'' +
                ", wan_type='" + wan_type + '\'' +
                ", must_check_wan=" + must_check_wan +
                ", language_flag=" + language_flag +
                ", platform_type=" + platform_type +
                ", dev_type='" + dev_type + '\'' +
                ", user_password='" + user_password + '\'' +
                ", user_name='" + user_name + '\'' +
                ", status=" + status +
                ", time_zone='" + time_zone + '\'' +
                ", time=" + time +
                ", str_time='" + str_time + '\'' +
                '}';
    }

    public String toRouterRuntimeInfoString() {
        return "设备运行状态信息:" + '\n' +
                "    " + "产品名称: " + equipment + '\n' +
                "    " + "SN: " + SN + '\n' +
                "    " + "软件版本：" + soft_ver + '\n' +
                "    " + "上电时间: " + uptime + '秒' + '\n' +
                "    " + "上网时间: " + network_time + '秒' + '\n' +
                "    " + "WAN口上网类型: " + wan_type + '\n' +
                "    " + "是否必须检测WAN口: " + (must_check_wan == null ? ' ' : must_check_wan == 1 ? "必须" : "不必") + '\n' +
                "    " + "WAN口MAC地址: " + wan_mac + '\n' +
                "    " + "WAN口IP地址: " + wan_ip + '\n' +
                "    " + "WAN口掩码: " + wan_netmask + '\n' +
                "    " + "WAN口网关地址：" + wan_gw + '\n' +
                "    " + "LAN口MAC地址: " + lan_mac + '\n' +
                "    " + "LAN口IP地址: " + lan_ip + '\n' +
                "    " + "LAN口掩码: " + lan_netmask + '\n' +
                "    " + "DNS: " + DNS + '\n'
                ;
    }
}
