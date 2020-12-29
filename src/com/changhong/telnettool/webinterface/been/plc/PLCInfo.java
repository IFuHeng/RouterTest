package com.changhong.telnettool.webinterface.been.plc;

import com.changhong.telnettool.tool.Tool;
import com.changhong.telnettool.webinterface.been.*;
import com.changhong.telnettool.webinterface.been.wan.WanResponseAllBeen;

import java.util.ArrayList;
import java.util.List;

public class PLCInfo extends BaseResponseBeen {

    private Integer enable;//Integer  required  PLC 网络使能, 0：禁用，1：启用
    private String dev_mac;//String  required  结点 MAC 地址
    private String domain;// String  required  域名
    private String passwd;//  String  required  密码（允许密码为空，即无须密码）
    private Integer role;//  Integer  required  0:子路由，1:母路由，2:自适应
    private Integer link_num;// Integer  required  下挂设备数目
    private Integer uptime;// 设备上电后时间，单位为秒
    private String wan_ip;//  IP 地址: AA:BB:CC:DD
    private Integer sta_num;// Integer  required  下挂终端总数（接入设备总量）
    private Integer network_time;// Integer  required  上网时长，单位为秒
    private List<PlcNode> plc_node;
    private List<Dev_Info> dev_info;//设备列表

    private String dev_model;// 设备型号
    private String manufacturer;// 生产厂家
    private String dev_type;// 设备类型
    private String dev_name;//设备名称
    private String dev_location;//设备位置，例如客厅，卧室，默认为客厅
    private String dev_identity;// 设备标识(SN)
    private String hw_ver;// 硬件版本
    private String sw_ver;// 软件版本
    private String dev_desc;// 设备描述

    private Integer port;//  Integer  required  端口 1: GE 2: RGMII
    private Integer conn_mode;// Integer  required  连接模式 1: route（路由） 2: bridge（桥接）
    private Integer ip_ver;// Integer  required  IP 协议版本 1: IPv4 2: IPv6 3: IPv4/IPv6（双栈）
    private Integer addr_type;//  Integer  required  地址类型1: Static 2: DHCP 3: PPPoE
    /*required  when    addr_type is static    and ip_ver is IPV4 start */
    private String ip_addr;// String   静态 IP
    private String netmask;//  String  掩码
    private String gw;//  String 默认网关
    private String dns1;// String  dns 服务器 1
    private String dns2;//String dns 服务器 2
    private String dns3;// String  dns 服务器 3
    /*required  when    addr_type is static    and ip_ver is IPV4 end */

    /*required  when    addr_type  is    PPPOE start */
    private String username;//            用户名
    private Integer auth_type;//  认证类型 0: Auto 1: PAP 2: CHAP
    private Integer dialing_mode;// Integer  拨号方式1: 按需拨号2: 自动拨号3: 手动拨号
    /*required  when    addr_type  is    PPPOE end */

    /*required  when    ip_ver is ipv6 start*/
    private Integer obtain_addr;//           地址获取方式 1: Static 2: DHCPv6 3: SLAAC 4: Auto
    private Integer obtain_gw;//            网关获取方式 0: SLAAC 1: Static
    private Integer obtain_dns;//    dns 服务器获取方式 0: SLAAC 1: Static 2: DHCPv6
    private Integer obtain_prefix;//    前缀获取方式 0: Static 1: PD
    /*required  when    ip_ver is ipv6 end*/

    /*required  when    obtain_addr  is    static start */
    private String ipv6_addr;//  String     IPv6 地址
    private String ipv6_gw;// String     IPv6 网关
    private String ipv6_dns1;//    ipv6 dns 服务器1
    private String ipv6_dns2;// String  ipv6 dns 服务器2
    private String ipv6_dns3;// String  ipv6 dns 服务器3
    private String ipv6_prefix;// ipv6 前缀

    /**
     * 报文统计
     */
    private List<PktStat> pkt_stat;
    /**
     * 2.4Gwifi使能状态，0: disable    1: enable
     */
    private Integer conn_status_2;
    /**
     * Integer	required	2.4G wifi实际工作信道
     */
    private Integer channel_2;
    /**
     * 5Gwifi使能状态，0: disable    1: enable
     */
    private Integer conn_status_5;
    /**
     * Integer	required	5G wifi实际工作信道
     */
    private Integer channel_5;


    /*required  when    obtain_addr  is    static end */
    public void writeWanInfo2Obj(PLCInfo obj) {
        obj.setUsername(username);
        obj.setPort(port);
        obj.setConn_mode(conn_mode);
        obj.setIp_ver(ip_ver);
        obj.setAddr_type(addr_type);
        obj.setIp_addr(ip_addr);
        obj.setNetmask(netmask);
        obj.setGw(gw);
        obj.setDns1(dns1);
        obj.setDns2(dns2);
        obj.setDns3(dns3);
        obj.setUsername(username);
        obj.setPasswd(passwd);
        obj.setAuth_type(auth_type);
        obj.setDialing_mode(dialing_mode);
        obj.setObtain_addr(obtain_addr);
        obj.setObtain_dns(obtain_dns);
        obj.setObtain_gw(obtain_gw);
        obj.setObtain_prefix(obtain_prefix);
        obj.setIpv6_addr(ipv6_addr);
        obj.setIpv6_gw(ipv6_gw);
        obj.setIpv6_dns1(ipv6_dns1);
        obj.setIpv6_dns2(ipv6_dns2);
        obj.setIpv6_dns3(ipv6_dns3);
        obj.setIpv6_prefix(ipv6_prefix);
    }


    private Integer mtu;//  范围：128~1540

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public String getDev_mac() {
        return dev_mac;
    }

    public void setDev_mac(String dev_mac) {
        this.dev_mac = dev_mac;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getLink_num() {
        return link_num;
    }

    public void setLink_num(Integer link_num) {
        this.link_num = link_num;
    }

    public List<PlcNode> getPlc_node() {
        return plc_node;
    }

    public void setPlc_node(List<PlcNode> plc_node) {
        this.plc_node = plc_node;
    }

    public Integer getUptime() {
        return uptime;
    }

    public void setUptime(Integer uptime) {
        this.uptime = uptime;
    }

    public String getWan_ip() {
        return wan_ip;
    }

    public void setWan_ip(String wan_ip) {
        this.wan_ip = wan_ip;
    }

    public Integer getSta_num() {
        return sta_num;
    }

    public void setSta_num(Integer sta_num) {
        this.sta_num = sta_num;
    }

    public Integer getNetwork_time() {
        return network_time;
    }

    public void setNetwork_time(Integer network_time) {
        this.network_time = network_time;
    }

    public String getDev_model() {
        return dev_model;
    }

    public void setDev_model(String dev_model) {
        this.dev_model = dev_model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getDev_type() {
        return dev_type;
    }

    public void setDev_type(String dev_type) {
        this.dev_type = dev_type;
    }

    public String getDev_identity() {
        return dev_identity;
    }

    public void setDev_identity(String dev_identity) {
        this.dev_identity = dev_identity;
    }

    public String getHw_ver() {
        return hw_ver;
    }

    public void setHw_ver(String hw_ver) {
        this.hw_ver = hw_ver;
    }

    public String getSw_ver() {
        return sw_ver;
    }

    public void setSw_ver(String sw_ver) {
        this.sw_ver = sw_ver;
    }

    public String getDev_desc() {
        return dev_desc;
    }

    public void setDev_desc(String dev_desc) {
        this.dev_desc = dev_desc;
    }

    public List<Dev_Info> getDev_info() {
        return dev_info;
    }

    public void setDev_info(List<Dev_Info> dev_info) {
        this.dev_info = dev_info;
    }

    public List<PktStat> getPkt_stat() {
        return pkt_stat;
    }

    public void setPkt_stat(List<PktStat> pkt_stat) {
        this.pkt_stat = pkt_stat;
    }

    public Integer getConn_status_2() {
        return conn_status_2;
    }

    public Integer getChannel_2() {
        return channel_2;
    }

    public Integer getConn_status_5() {
        return conn_status_5;
    }

    public Integer getChannel_5() {
        return channel_5;
    }


    public void setConn_status_2(Integer conn_status_2) {
        this.conn_status_2 = conn_status_2;
    }

    public void setChannel_2(Integer channel_2) {
        this.channel_2 = channel_2;
    }

    public void setConn_status_5(Integer conn_status_5) {
        this.conn_status_5 = conn_status_5;
    }

    public void setChannel_5(Integer channel_5) {
        this.channel_5 = channel_5;
    }

    public String getDev_name() {
        return dev_name;
    }

    public void setDev_name(String dev_name) {
        this.dev_name = dev_name;
    }

    public String getDev_location() {
        return dev_location;
    }

    public void setDev_location(String dev_location) {
        this.dev_location = dev_location;
    }

    /**
     * @return 生成一个新的deviceItem对象
     */
    public DeviceItem turn2DeviceItem() {
        DeviceItem result = new DeviceItem();
        result.setMac(getDev_mac());
        result.setIp(getWan_ip());
        result.setUpConnected(true);
        result.setChild(getRole() == 0);
        if (dev_info != null) {
            result.setStaNum(getLinkNum(getDev_mac()));
            result.setStaList(getLinkStaList(getDev_mac()));
        } else
            result.setStaNum(getSta_num());
        result.setDeviceName(dev_name==null ? dev_model : dev_name);
        result.setType(DeviceType.PLC);
        result.setLocation(dev_location);
        result.setWan_type(WanType.getDeviceTypeFromTypeCode(addr_type));
//        result.setLocation(dev_desc);
        return result;
    }

    /**
     * 获取mac设备下挂的终端数量
     *
     * @param mac
     * @return
     */
    public int getLinkNum(String mac) {
        int num = 0;
        if (dev_info != null)
            for (Dev_Info devInfo : dev_info) {
                if (Tool.compareMac(devInfo.getPlc_node(), mac))
                    ++num;
            }
        return num;
    }

    /**
     * 获取mac设备下挂的终端
     *
     * @param mac
     * @return
     */
    public List<StaInfo> getLinkStaList(String mac) {
        ArrayList<StaInfo> staInfos = new ArrayList<>();
        if (dev_info != null)
            for (Dev_Info devInfo : dev_info) {
                if (Tool.compareMac(devInfo.getPlc_node(), mac)) {
                    staInfos.add(devInfo.turn2StaInfo());
                }
            }
        if (staInfos.isEmpty())
            return null;
        return staInfos;
    }

    @Override
    public String toString() {
        return "PLCInfo{" +
                "enable=" + enable +
                ", dev_mac='" + dev_mac + '\'' +
                ", domain='" + domain + '\'' +
                ", passwd='" + passwd + '\'' +
                ", role=" + role +
                ", link_num=" + link_num +
                ", uptime=" + uptime +
                ", wan_ip='" + wan_ip + '\'' +
                ", sta_num=" + sta_num +
                ", network_time=" + network_time +
                ", plc_node=" + plc_node +
                ", dev_info=" + dev_info +
                ", dev_model='" + dev_model + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", dev_type='" + dev_type + '\'' +
                ", dev_identity='" + dev_identity + '\'' +
                ", hw_ver='" + hw_ver + '\'' +
                ", sw_ver='" + sw_ver + '\'' +
                ", dev_desc='" + dev_desc + '\'' +
                ", port=" + port +
                ", conn_mode=" + conn_mode +
                ", ip_ver=" + ip_ver +
                ", addr_type=" + addr_type +
                ", ip_addr=" + ip_addr +
                ", netmask='" + netmask + '\'' +
                ", gw='" + gw + '\'' +
                ", dns1='" + dns1 + '\'' +
                ", dns2='" + dns2 + '\'' +
                ", dns3='" + dns3 + '\'' +
                ", username='" + username + '\'' +
                ", auth_type=" + auth_type +
                ", dialing_mode=" + dialing_mode +
                ", obtain_addr=" + obtain_addr +
                ", obtain_gw=" + obtain_gw +
                ", obtain_dns=" + obtain_dns +
                ", obtain_prefix=" + obtain_prefix +
                ", ipv6_addr='" + ipv6_addr + '\'' +
                ", ipv6_gw='" + ipv6_gw + '\'' +
                ", ipv6_dns1='" + ipv6_dns1 + '\'' +
                ", ipv6_dns2='" + ipv6_dns2 + '\'' +
                ", ipv6_dns3='" + ipv6_dns3 + '\'' +
                ", ipv6_prefix='" + ipv6_prefix + '\'' +
                ", mtu=" + mtu +
                ", err_code=" + err_code +
                ", message='" + message + '\'' +
                ", waite_time=" + waite_time +
                '}';
    }

    public void set2ResponseBeen(WanResponseAllBeen result) {
        if (ip_ver == 2) {//ipv6
            result.setType(WanType.getDeviceTypeFromTypeCode(addr_type).getName());
            result.setIpaddr(ip_addr);
            result.setNetmask(netmask);
            result.setDns1(dns1);
            result.setDns2(dns2);
            result.setGw(gw);
            result.setPppoe_username(username);
            result.setPppoe_password(passwd);
        } else {//IPV4
            result.setType(WanType.getDeviceTypeFromTypeCode(addr_type).getName());
            result.setIpaddr(ip_addr);
            result.setNetmask(netmask);
            result.setDns1(dns1);
            result.setDns2(dns2);
            result.setGw(gw);
            result.setPppoe_username(username);
            result.setPppoe_password(passwd);
        }
    }

    public static class PlcNode {
        private String mac;//  String  required  结点 MAC 地址
        private Integer capability;//  Integer  required  结点能力，0-从机，1-主机。
        private Integer link_speed;//  Integer  required  结点连接速率(Mbps)，若是主机本身则显示为本机。

        public String getMac() {
            return mac;
        }

        public Integer getCapability() {
            return capability;
        }

        public Integer getLink_speed() {
            return link_speed;
        }

        public DeviceItem turn2DeviceItem() {
            DeviceItem result = new DeviceItem();
            result.setDeviceName("子节点电力猫");
            result.setMac(mac);
            result.setUpConnected(true);
            result.setChild(true);
            return result;
        }

        @Override
        public String toString() {
            return "PlcNode{" +
                    "mac='" + mac + '\'' +
                    ", capability=" + capability +
                    ", link_speed=" + link_speed +
                    '}';
        }
    }

    public static class Dev_Info {
        private String dev_name;// String  required  设备名
        private Integer dev_type;// Interger  optional  设备类型，0-未知，1-PC, 2-手机，3-PAD
        private String mac;// String  required  设备 mac 地址
        private Integer status;// Integer  required  在线状态，0-offline 1-online
        private String ip_addr;// String  required  ip 地址
        private Integer access_port;// Integer  required  接入端口（LAN口，2.4G wifi或5G）0 – 未知，1 – 有线， 2 – 2.4G，3 – 5G , 4 - PLC
        private Integer online_time;// 设备介入时间，单位s
        private Integer power_level;// 设备信号强度，单位db
        private String plc_node;//  String  required  接入 plc 节点
        private Integer rx_speed;// Integer  required  下行速率，单位kbps
        private Integer tx_speed;// Integer  required  上行速率，单位kbps
        private Integer rx_speed_limited;// Integer  required  下行限速，单位kbps，0 表示不限速
        private Integer tx_speed_limited;// Integer  required  上行限速，单位kbps，0 表示不限速

        public DeviceItem turn2DeviceItem() {
            DeviceItem result = new DeviceItem();
            result.setDeviceName(dev_name);
            result.setIp(ip_addr);
            result.setMac(mac);
            result.setUpConnected(true);
            result.setUpNodeName(plc_node);
            result.setChild(true);
            if (access_port == 0)
                result.setType(DeviceType.PLC);
            return result;
        }

        public StaInfo turn2StaInfo() {
            StaInfo result = new StaInfo();
            result.setName(dev_name);
            result.setIp(ip_addr);
            result.setMac(mac);
            //0 - GE            //8 - RGMII            //10 - PLC            //11 - WLAN
            switch (access_port) {
                case 1:
                    result.setConnect_type(1);
                    break;
                case 2:
                    result.setConnect_type(2);
                    break;
                case 3:
                    result.setConnect_type(4);
                    break;
                case 4:
                    result.setConnect_type(16);
                    break;
                default:
                    result.setConnect_type(0);
                    break;
            }
            result.setLink_time(online_time);
            result.setDownload(rx_speed);
            result.setUpload(tx_speed);
            result.setRssi(power_level);
            return result;
        }

        public String getDev_name() {
            return dev_name;
        }

        public Integer getDev_type() {
            return dev_type;
        }

        /**
         * @return 是否无线设备类型
         */
        public boolean isWirelessType() {
            return access_port != null && (access_port == 2 || access_port == 3);
        }

        public String getMac() {
            return mac;
        }

        public Integer getStatus() {
            return status;
        }

        public String getIp_addr() {
            return ip_addr;
        }

        public Integer getAccess_port() {
            return access_port;
        }

        public String getPlc_node() {
            return plc_node;
        }

        public Integer getRx_speed() {
            return rx_speed;
        }

        public Integer getTx_speed() {
            return tx_speed;
        }

        public Integer getRx_speed_limited() {
            return rx_speed_limited;
        }

        public Integer getTx_speed_limited() {
            return tx_speed_limited;
        }

        public Integer getPower_level() {
            return power_level;
        }

        public Integer getOnline_time() {
            return online_time;
        }

        @Override
        public String toString() {
            return "Dev_Info{" +
                    "dev_name='" + dev_name + '\'' +
                    ", dev_type=" + dev_type +
                    ", mac='" + mac + '\'' +
                    ", status=" + status +
                    ", ip_addr='" + ip_addr + '\'' +
                    ", access_port=" + access_port +
                    ", online_time=" + online_time +
                    ", plc_node='" + plc_node + '\'' +
                    ", rx_speed=" + rx_speed +
                    ", tx_speed=" + tx_speed +
                    ", rx_speed_limited=" + rx_speed_limited +
                    ", tx_speed_limited=" + tx_speed_limited +
                    '}';
        }
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getConn_mode() {
        return conn_mode;
    }

    public void setConn_mode(Integer conn_mode) {
        this.conn_mode = conn_mode;
    }

    public Integer getIp_ver() {
        return ip_ver;
    }

    public void setIp_ver(Integer ip_ver) {
        this.ip_ver = ip_ver;
    }

    public Integer getAddr_type() {
        return addr_type;
    }

    public void setAddr_type(Integer addr_type) {
        this.addr_type = addr_type;
    }

    public String getIp_addr() {
        return ip_addr;
    }

    public void setIp_addr(String ip_addr) {
        this.ip_addr = ip_addr;
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

    public String getDns3() {
        return dns3;
    }

    public void setDns3(String dns3) {
        this.dns3 = dns3;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAuth_type() {
        return auth_type;
    }

    public void setAuth_type(Integer auth_type) {
        this.auth_type = auth_type;
    }

    public Integer getDialing_mode() {
        return dialing_mode;
    }

    public void setDialing_mode(Integer dialing_mode) {
        this.dialing_mode = dialing_mode;
    }

    public Integer getObtain_addr() {
        return obtain_addr;
    }

    public void setObtain_addr(Integer obtain_addr) {
        this.obtain_addr = obtain_addr;
    }

    public Integer getObtain_gw() {
        return obtain_gw;
    }

    public void setObtain_gw(Integer obtain_gw) {
        this.obtain_gw = obtain_gw;
    }

    public Integer getObtain_dns() {
        return obtain_dns;
    }

    public void setObtain_dns(Integer obtain_dns) {
        this.obtain_dns = obtain_dns;
    }

    public Integer getObtain_prefix() {
        return obtain_prefix;
    }

    public void setObtain_prefix(Integer obtain_prefix) {
        this.obtain_prefix = obtain_prefix;
    }

    public String getIpv6_addr() {
        return ipv6_addr;
    }

    public void setIpv6_addr(String ipv6_addr) {
        this.ipv6_addr = ipv6_addr;
    }

    public String getIpv6_gw() {
        return ipv6_gw;
    }

    public void setIpv6_gw(String ipv6_gw) {
        this.ipv6_gw = ipv6_gw;
    }

    public String getIpv6_dns1() {
        return ipv6_dns1;
    }

    public void setIpv6_dns1(String ipv6_dns1) {
        this.ipv6_dns1 = ipv6_dns1;
    }

    public String getIpv6_dns2() {
        return ipv6_dns2;
    }

    public void setIpv6_dns2(String ipv6_dns2) {
        this.ipv6_dns2 = ipv6_dns2;
    }

    public String getIpv6_dns3() {
        return ipv6_dns3;
    }

    public void setIpv6_dns3(String ipv6_dns3) {
        this.ipv6_dns3 = ipv6_dns3;
    }

    public String getIpv6_prefix() {
        return ipv6_prefix;
    }

    public void setIpv6_prefix(String ipv6_prefix) {
        this.ipv6_prefix = ipv6_prefix;
    }

    public Integer getMtu() {
        return mtu;
    }

    public void setMtu(Integer mtu) {
        this.mtu = mtu;
    }

    public static class PktStat {
        /**
         * ssid 索引
         */
        private Integer ssid_idx;
        /**
         * ssid 名称
         */
        private String ssid_name;
        /**
         * Integer	required	已连接设备数量
         */
        private Integer dev_num;
        /**
         * Integer	required	接收报文统计（按字节）
         */
        private Integer rx_bytes;
        /**
         * Integer	required	接收报文统计（按数据帧）
         */
        private Integer rx_frame;
        /**
         * Integer	required	发送报文统计（按字节）
         */
        private Integer tx_bytes;
        /**
         * Integer	required	发送报文统计（按数据帧）
         */
        private Integer tx_frame;

        public Integer getSsid_idx() {
            return ssid_idx;
        }

        public String getSsid_name() {
            return ssid_name;
        }

        public Integer getDev_num() {
            return dev_num;
        }

        public Integer getRx_bytes() {
            return rx_bytes;
        }

        public Integer getRx_frame() {
            return rx_frame;
        }

        public Integer getTx_bytes() {
            return tx_bytes;
        }

        public Integer getTx_frame() {
            return tx_frame;
        }

        @Override
        public String toString() {
            return "PktStat{" +
                    "ssid_idx=" + ssid_idx +
                    ", ssid_name='" + ssid_name + '\'' +
                    ", dev_num=" + dev_num +
                    ", rx_bytes=" + rx_bytes +
                    ", rx_frame=" + rx_frame +
                    ", tx_bytes=" + tx_bytes +
                    ", tx_frame=" + tx_frame +
                    '}';
        }
    }
}
