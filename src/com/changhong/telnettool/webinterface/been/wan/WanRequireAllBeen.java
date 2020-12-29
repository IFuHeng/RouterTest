package com.changhong.telnettool.webinterface.been.wan;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class WanRequireAllBeen {


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


    private String mask;
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

    /**
     * 需校准mac地址是否有效 传输的数据是不带冒号的mac地址
     * 6.2.5 需要排除列表中已有mac
     */
    private String mac;
    /**
     * 不得超过32字符
     */
    private String name;

    /**
     * 需校验ip地址是否合法，以及判断是否设置过该IP，先获取列表再对比判断。
     */
    private String ip;
    /**
     * 1:保存并执行；0：只保存
     */
    private Integer action_flag;
    /**
     * 不得超过32字符
     */
    private String description;

    /**
     * 0:disabled;1:enabled
     */
    private Integer enabled;

    private List<Level2Been> list;

    /**
     * Mac地址数组 ITEM是
     * {
     * “mac”:”001122334457”
     * }
     */
    private List<Level2Been> filter_macs;

    /**
     * 上行带宽限制，单位Kbps，0表示不限速。不能超过WAN口最大速率，百兆为102400（100Mbps），千兆为1024000（1000Mbps）
     */
    private Integer max_up_bandwidth;
    /**
     * 下行带宽限制，单位Kbps，0:表示不限速。不能超过WAN口最大速率，百兆为102400（100Mbps），千兆为1024000（1000Mbps）
     */
    private Integer max_down_bandwidth;
    /**
     * 1:表示添加， 0：表示删除
     */
    private Integer mode;

    /**
     * 允许上网的开始时间，小时；     为0-23取整数值。
     */
    private Integer start_time_h;
    /**
     * 允许上网的开始时间，分钟；     为0-59取整数值
     */
    private Integer start_time_m;
    /**
     * 允许上网的开始时间，星期；
     * 0-7取整数，0和7表示周日；如果同时存几个的话，输入格式为：x,x,x
     * 比如同时有周一周三周日，输入为：
     * 1,3,7；只“*”表示周一至周日均执行
     */
    private String start_time_w;
    /**
     * 结束上网的开始时间，小时；
     * 为0-23取整数值。
     */
    private Integer end_time_h;
    /**
     * 结束上网的开始时间，分钟；
     * 为0-59取整数值
     */
    private Integer end_time_m;
    /**
     * string	required	结束上网的开始时间，星期；
     * 0-7取整数，0和7表示周日；如果同时存几个的话，输入格式为：x,x,x
     * 比如同时有周一周三周日，输入为：
     * 1,3,7；只“*”表示周一至周日均执行
     */
    private String end_time_w;

    public WanRequireAllBeen() {

    }

    public WanRequireAllBeen(WanResponseAllBeen responseAllBeen) {
        setSrc_type(responseAllBeen.getSrc_type());
        setType(responseAllBeen.getType());
        setIpaddr(responseAllBeen.getIpaddr());
        setNetmask(responseAllBeen.getNetmask());
        setGw(responseAllBeen.getGw());
        setPppoe_username(responseAllBeen.getPppoe_username());
        setPppoe_password(responseAllBeen.getPppoe_password());
        setService_name(responseAllBeen.getService_name());
        setDail_type(responseAllBeen.getDail_type());
        setMtu(responseAllBeen.getMtu());
        setClone_mac(responseAllBeen.getClone_mac());
        setFlag_dns(responseAllBeen.getFlag_dns());
        setDns1(responseAllBeen.getDns1());
        setDns2(responseAllBeen.getDns2());
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

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public void setDail_type(Integer dail_type) {
        this.dail_type = dail_type;
    }

    public void setSave_action(Integer save_action) {
        this.save_action = save_action;
    }

    public void setMtu(Integer mtu) {
        this.mtu = mtu;
    }

    public void setClone_mac(String clone_mac) {
        this.clone_mac = clone_mac;
    }

    public void setFlag_dns(Integer flag_dns) {
        this.flag_dns = flag_dns;
    }

    public void setPppoe_state(Integer pppoe_state) {
        this.pppoe_state = pppoe_state;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public void setGw_addr(String gw_addr) {
        this.gw_addr = gw_addr;
    }

    public void setDomain_name(String domain_name) {
        this.domain_name = domain_name;
    }

    public void setDhcpd_enabled(Integer dhcpd_enabled) {
        this.dhcpd_enabled = dhcpd_enabled;
    }

    public void setDhcp_start(String dhcp_start) {
        this.dhcp_start = dhcp_start;
    }

    public void setDhcp_end(String dhcp_end) {
        this.dhcp_end = dhcp_end;
    }

    public void setLease(Integer lease) {
        this.lease = lease;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setAction_flag(Integer action_flag) {
        this.action_flag = action_flag;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public void setList(List<Level2Been> list) {
        this.list = list;
    }

    public void setFilter_macs(List<Level2Been> filter_macs) {
        this.filter_macs = filter_macs;
    }

    public void setMax_up_bandwidth(Integer max_up_bandwidth) {
        this.max_up_bandwidth = max_up_bandwidth;
    }

    public void setMax_down_bandwidth(Integer max_down_bandwidth) {
        this.max_down_bandwidth = max_down_bandwidth;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public void setStart_time_h(Integer start_time_h) {
        this.start_time_h = start_time_h;
    }

    public void setStart_time_m(Integer start_time_m) {
        this.start_time_m = start_time_m;
    }

    public void setStart_time_w(String start_time_w) {
        this.start_time_w = start_time_w;
    }

    public void setEnd_time_h(Integer end_time_h) {
        this.end_time_h = end_time_h;
    }

    public void setEnd_time_m(Integer end_time_m) {
        this.end_time_m = end_time_m;
    }

    public void setEnd_time_w(String end_time_w) {
        this.end_time_w = end_time_w;
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

    public String getService_name() {
        return service_name;
    }

    public Integer getDail_type() {
        return dail_type;
    }

    public Integer getSave_action() {
        return save_action;
    }

    public Integer getMtu() {
        return mtu;
    }

    public String getClone_mac() {
        return clone_mac;
    }

    public Integer getFlag_dns() {
        return flag_dns;
    }

    public Integer getPppoe_state() {
        return pppoe_state;
    }

    public String getMask() {
        return mask;
    }

    public String getGw_addr() {
        return gw_addr;
    }

    public String getDomain_name() {
        return domain_name;
    }

    public Integer getDhcpd_enabled() {
        return dhcpd_enabled;
    }

    public String getDhcp_start() {
        return dhcp_start;
    }

    public String getDhcp_end() {
        return dhcp_end;
    }

    public Integer getLease() {
        return lease;
    }

    public String getMac() {
        return mac;
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public Integer getAction_flag() {
        return action_flag;
    }

    public String getDescription() {
        return description;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public List<Level2Been> getList() {
        return list;
    }

    public List<Level2Been> getFilter_macs() {
        return filter_macs;
    }

    public Integer getMax_up_bandwidth() {
        return max_up_bandwidth;
    }

    public Integer getMax_down_bandwidth() {
        return max_down_bandwidth;
    }

    public Integer getMode() {
        return mode;
    }

    public Integer getStart_time_h() {
        return start_time_h;
    }

    public Integer getStart_time_m() {
        return start_time_m;
    }

    public String getStart_time_w() {
        return start_time_w;
    }

    public Integer getEnd_time_h() {
        return end_time_h;
    }

    public Integer getEnd_time_m() {
        return end_time_m;
    }

    public String getEnd_time_w() {
        return end_time_w;
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
                ", service_name='" + service_name + '\'' +
                ", dail_type=" + dail_type +
                ", save_action=" + save_action +
                ", mtu=" + mtu +
                ", clone_mac='" + clone_mac + '\'' +
                ", flag_dns=" + flag_dns +
                ", pppoe_state=" + pppoe_state +
                ", mask='" + mask + '\'' +
                ", gw_addr='" + gw_addr + '\'' +
                ", domain_name='" + domain_name + '\'' +
                ", dhcpd_enabled=" + dhcpd_enabled +
                ", dhcp_start='" + dhcp_start + '\'' +
                ", dhcp_end='" + dhcp_end + '\'' +
                ", lease=" + lease +
                ", mac='" + mac + '\'' +
                ", name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", action_flag=" + action_flag +
                ", description='" + description + '\'' +
                ", enabled=" + enabled +
                ", list=" + list +
                ", filter_macs=" + filter_macs +
                ", max_up_bandwidth=" + max_up_bandwidth +
                ", max_down_bandwidth=" + max_down_bandwidth +
                ", mode=" + mode +
                ", start_time_h=" + start_time_h +
                ", start_time_m=" + start_time_m +
                ", start_time_w='" + start_time_w + '\'' +
                ", end_time_h=" + end_time_h +
                ", end_time_m=" + end_time_m +
                ", end_time_w='" + end_time_w + '\'' +
                '}';
    }
}
