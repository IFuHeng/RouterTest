package com.changhong.telnettool.webinterface.been.plc;


import com.changhong.telnettool.webinterface.been.BaseResponseBeen;

public class LanV4Info extends BaseResponseBeen {
    private Integer addr_sel;//	required	维护地址方式 1: dhcp  2: static
    private String ip_addr;//	required	IP地址，默认维护IP地址，需校验IP有效
    private String mask;//required	掩码，默认维护地址子网掩码
    private String domain_name;//	required	域名，不超过64个字符
    private Integer dhcps_enable;//	required	DHCP服务器使能1: enable    0: disable
    private String dhcp_start;//	required	起始地址    取值范围0-255，不得大于end
    private String dhcp_end;//required	结束地址    取值范围0-255，不得小于start
    private String sub_mask;//	required	子网掩码
    private Integer lease;//	required	租期，单位：秒

    public Integer getAddr_sel() {
        return addr_sel;
    }

    public void setAddr_sel(Integer addr_sel) {
        this.addr_sel = addr_sel;
    }

    public String getIp_addr() {
        return ip_addr;
    }

    public void setIp_addr(String ip_addr) {
        this.ip_addr = ip_addr;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getDomain_name() {
        return domain_name;
    }

    public void setDomain_name(String domain_name) {
        this.domain_name = domain_name;
    }

    public Integer getDhcps_enable() {
        return dhcps_enable;
    }

    public void setDhcps_enable(Integer dhcps_enable) {
        this.dhcps_enable = dhcps_enable;
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

    public String getSub_mask() {
        return sub_mask;
    }

    public void setSub_mask(String sub_mask) {
        this.sub_mask = sub_mask;
    }

    public Integer getLease() {
        return lease;
    }

    public void setLease(Integer lease) {
        this.lease = lease;
    }
}
