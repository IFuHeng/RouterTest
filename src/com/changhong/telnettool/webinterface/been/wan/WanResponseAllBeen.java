package com.changhong.telnettool.webinterface.been.wan;

import com.changhong.telnettool.webinterface.been.BaseResponseBeen;
import com.changhong.telnettool.webinterface.been.StaInfo;
import com.changhong.telnettool.webinterface.been.wifi.WlanLevel2Been;

import java.util.List;

public class WanResponseAllBeen extends BaseResponseBeen {

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
     * 最大输入长度校验不超过32字符
     */
    private String service_name;
    /**
     * 0:自动；1：按需；2：手动
     */
    private Integer dail_type;
    private String mask;

    /**
     * 1：保存并执行；0：只保存数据不执行。（可以先保存后面统一执行）
     */
    private Integer save_action;
    private Integer mtu;
    private String clone_mac;
    /**
     * 0:自动获取DNS；1:手动输入DNS（主要针对DHCP和PPPoE才有该配置）
     */
    private Integer flag_dns;


    /**
     * 0 -- 未连接；1 -- 连接中；2 -- 连接成功；
     * 3 -- 用户名或密码错误 ； 4 -- 未知错误。
     */
    private Integer pppoe_state;

    /**
     * when dhcpd_enabled is 0	网关（在设置关闭dhcp的时候显示）
     */
    private String gw_addr;
    /**
     * 不超过64个字符
     */
    private String domain_name;
    /**
     * 1:client；0:关闭;2:server
     */
    private Integer dhcpd_enabled;
    /**
     * when dhcp_mode is server	取值范围0-255，不得大于end
     */
    private String dhcp_start;
    /**
     * 取值范围0-255，不得小于start
     */
    private String dhcp_end;
    /**
     * 单位:秒
     */
    private Integer lease;

    private List<StaInfo> sta_info;

    private List<Level2Been> list;
    /**
     * 6.3.3	mac_rate_limit_show 限制的详细信息
     * 6.3.5	internet_time_limit_show 显示限速的mac及其详细信息
     */
    private List<Level2Been> info_list;

    public Integer getSrc_type() {
        return src_type;
    }

    public void setSrc_type(Integer src_type) {
        this.src_type = src_type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }

    public String getNetmask() {
        return netmask;
    }

    public void setNetmask(String netmask) {
        this.netmask = netmask;
    }

    public String getGw() {
        return gw;
    }

    public void setGw(String gw) {
        this.gw = gw;
    }

    public String getDns1() {
        return dns1;
    }

    public void setDns1(String dns1) {
        this.dns1 = dns1;
    }

    public String getDns2() {
        return dns2;
    }

    public void setDns2(String dns2) {
        this.dns2 = dns2;
    }

    public String getPppoe_username() {
        return pppoe_username;
    }

    public void setPppoe_username(String pppoe_username) {
        this.pppoe_username = pppoe_username;
    }

    public String getPppoe_password() {
        return pppoe_password;
    }

    public void setPppoe_password(String pppoe_password) {
        this.pppoe_password = pppoe_password;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public Integer getDail_type() {
        return dail_type;
    }

    public void setDail_type(Integer dail_type) {
        this.dail_type = dail_type;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getGw_addr() {
        return gw_addr;
    }

    public void setGw_addr(String gw_addr) {
        this.gw_addr = gw_addr;
    }

    public String getDomain_name() {
        return domain_name;
    }

    public void setDomain_name(String domain_name) {
        this.domain_name = domain_name;
    }

    public Integer getDhcpd_enabled() {
        return dhcpd_enabled;
    }

    public void setDhcpd_enabled(Integer dhcpd_enabled) {
        this.dhcpd_enabled = dhcpd_enabled;
    }

    public String getDhcp_start() {
        return dhcp_start;
    }

    public void setDhcp_start(String dhcp_start) {
        this.dhcp_start = dhcp_start;
    }

    public String getDhcp_end() {
        return dhcp_end;
    }

    public void setDhcp_end(String dhcp_end) {
        this.dhcp_end = dhcp_end;
    }

    public Integer getLease() {
        return lease;
    }

    public void setLease(Integer lease) {
        this.lease = lease;
    }

    public List<StaInfo> getSta_info() {
        return sta_info;
    }

    public List<Level2Been> getList() {
        return list;
    }

    public void setList(List<Level2Been> list) {
        this.list = list;
    }

    public List<Level2Been> getInfo_list() {
        return info_list;
    }

    public void setInfo_list(List<Level2Been> info_list) {
        this.info_list = info_list;
    }

    public Integer getSave_action() {
        return save_action;
    }

    public void setSave_action(Integer save_action) {
        this.save_action = save_action;
    }

    public Integer getMtu() {
        return mtu;
    }

    public void setMtu(Integer mtu) {
        this.mtu = mtu;
    }

    public String getClone_mac() {
        return clone_mac;
    }

    public void setClone_mac(String clone_mac) {
        this.clone_mac = clone_mac;
    }

    public Integer getFlag_dns() {
        return flag_dns;
    }

    public void setFlag_dns(Integer flag_dns) {
        this.flag_dns = flag_dns;
    }

    public Integer getPppoe_state() {
        return pppoe_state;
    }

    public void setPppoe_state(Integer pppoe_state) {
        this.pppoe_state = pppoe_state;
    }

    public void setSta_info(List<StaInfo> sta_info) {
        this.sta_info = sta_info;
    }

    @Override
    public String toString() {
        return "ResponseAllBeen{" +
                "src_type=" + src_type +
                ", type='" + type + '\'' +
                ", ipaddr='" + ipaddr + '\'' +
                ", netmask='" + netmask + '\'' +
                ", gw='" + gw + '\'' +
                ", dns1='" + dns1 + '\'' +
                ", dns2='" + dns2 + '\'' +
                ", pppoe_username='" + pppoe_username + '\'' +
                ", pppoe_password='" + pppoe_password + '\'' +
                ", service_name='" + service_name + '\'' +
                ", dail_type=" + dail_type +
                ", mask='" + mask + '\'' +
                ", save_action=" + save_action +
                ", mtu=" + mtu +
                ", clone_mac='" + clone_mac + '\'' +
                ", flag_dns=" + flag_dns +
                ", pppoe_state=" + pppoe_state +
                ", gw_addr='" + gw_addr + '\'' +
                ", domain_name='" + domain_name + '\'' +
                ", dhcpd_enabled=" + dhcpd_enabled +
                ", dhcp_start='" + dhcp_start + '\'' +
                ", dhcp_end='" + dhcp_end + '\'' +
                ", lease=" + lease +
                ", sta_info=" + sta_info +
                ", list=" + list +
                ", info_list=" + info_list +
                ", err_code=" + err_code +
                ", message='" + message + '\'' +
                ", waite_time=" + waite_time +
                '}';
    }
}
