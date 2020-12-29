package com.changhong.telnettool.webinterface;

public interface HttpRequestMethod {
    /**
     * 4.1.1 开始自动检测wan口类型
     * 注意事项：
     * 针对router_info_show中的must_check_wan为1，
     * 1、如果检测网口没有插网线不能点击该接口，也不下一步跳过，提示插入网线。
     * 2、插入网线后让用户点击该接口，如果返回static表示没有识别到上级服务器，
     * 不能通过给与提示并让他们再次检测，知道检测到dhcp或者pppoe上网方式后，才能出现下一步可用户点击。
     */
    String METHOD_AUTO_CHECK_WAN_TYPE = "auto_check_wan_type";

    /**
     * 4.2.1 设置向导设置WAN口的参数
     * 注意事项：该接口会因为不同的版本功能显示会有所差异，
     * 主要根据router_info_show中的must_check_wan来判断显示和流程。
     * 该流程分为必须自动检测WAN口网络状态 和 不必自动检测网络状态两种。
     * 如果must_check_wan为1，显示页面不能手动选择上网模式，必须通过自动检测出上网模式才能进行下一步。
     * 如果must_check_wan为0，显示页面可以手动选着上网模式，可以不用自动检测上模式而手动选着上模式，
     * 并进行下一步操作，也可以自动检测上网模式，再下一步。
     */
    String METHOD_WIZARD_SETTING = "wizard_setting";

    /**
     * 4.2.2 获取向导中 上网设置的参数
     */
    String METHOD_WIZARD_GET_NETWORK = "wizard_get_network";

    /**
     * 4.2.3	设置向导参数
     */
    String METHOD_WIZARD_SET_WIRELESS = "wizard_set_wireless";

    /**
     * 4.2.4 获取设置向导中的无线信息
     */
    String METHOD_WIZARD_GET_WIRELESS = "wizard_get_wireless";


    /**
     * 4.2.5 设置“设置向导”完成标记，设置向导设置成功后调用
     */
    String METHOD_WIZARD_SET_GUID = "wizard_set_guid";
    /**
     * 4.2.6 获取“设置向导”是否设置标记，如果返回值为1，进入登录页面；为0，进入设置向导。
     */
    String METHOD_WIZARD_GET_GUID = "wizard_get_guid";

    /**
     * 5.1	获取状态信息
     */
    String METHOD_STATUS_SHOW = "status_show";

    /**
     * 6.1.1	设置wan口参数
     */
    String METHOD_WAN_SETTING = "wan_setting";
    /**
     * 6.1.2	获取 wan口参数
     */
    String METHOD_WAN_SHOW = "wan_show";
    /**
     * 6.1.3	获取pppoe连接状态
     */
    String METHOD_CHECK_PPPOE_STATE = "check_pppoe_state";
    /**
     * 6.2.1	设置lan口参数
     */
    String METHOD_LAN_SETTING = "lan_setting";

    /**
     * 6.2.2	获取 lan口参数
     */
    String METHOD_LAN_SHOW = "lan_show";

    /**
     * 6.2.3	设置STA信息
     */
    String METHOD_STA_INFO_SETTING = "sta_info_setting";
    /**
     * 6.2.4	获取STA信息
     */
    String METHOD_STA_INFO_SHOW = "sta_info_show";
    /**
     * 6.2.5    增加静态DHCP
     */
    String METHOD_STATIC_DHCP_ADD = "static_dhcp_add";
    /**
     * 6.2.6    获取静态DHCP信息
     */
    String METHOD_STATIC_DHCP_SHOW = "static_dhcp_show";
    /**
     * 6.2.7   设置静态DHCP
     */
    String METHOD_STATIC_DHCP_SETTING = "static_dhcp_setting";
    /**
     * 6.2.8	增加静态DHCP
     */
    String METHOD_STATIC_DHCP_DEL = "static_dhcp_del";

    /**
     * 6.3.1	设置mac地址过滤
     */
    String METHOD_MAC_FILTER_SETTING = "mac_filter_setting";
    /**
     * 6.3.2	设置mac地址限速
     */
    String METHOD_MAC_RATE_LIMIT_SETTING = "mac_rate_limit_setting";
    /**
     * 6.3.3	显示mac地址限速 的数据
     */
    String METHOD_MAC_RATE_LIMIT_SHOW = "mac_rate_limit_show";
    /**
     * 6.3.4	设置mac地址限速
     */
    String METHOD_INTERNET_TIME_LIMIT_SETTING = "internet_time_limit_setting";
    /**
     * 6.3.5	显示限速的mac及其详细信息
     */
    String METHOD_INTERNET_TIME_LIMIT_SHOW = "internet_time_limit_show";

    /**
     * 7.1.1	设置无线基本参数
     */
    String METHOD_WLAN_QUICK_SETTING = "wlan_quick_setting";
    /**
     * 7.1.2	获取wlan基本配置
     */
    String METHOD_WLAN_QUICK_SHOW = "wlan_quick_show";
    /**
     * 7.2.1	设置无线高级参数
     */
    String METHOD_WLAN_ADVANCED_SETTING = "wlan_advanced_setting";
    /**
     * 7.2.2	获取wlan高级配置
     */
    String METHOD_WLAN_ADVANCED_SHOW = "wlan_advanced_show";
    /**
     * 7.3.1	设置无线访问控制
     */
    String METHOD_ACCESS_SETTING = "wlan_access_setting";
    /**
     * 7.3.2	获取访问控制信息
     */
    String METHOD_ACCESS_SHOW = "wlan_access_show";
    /**
     * 7.3.3	增加无线访问控制设备
     */
    String METHOD_ACCESS_ADD = "wlan_access_add";
    /**
     * 7.3.4	删除无线访问控制设备
     */
    String METHOD_ACCESS_DEL = "wlan_access_del";
    /**
     * 7.4.1    设置访客网络参数
     */
    String METHOD_GUEST_NETCORK_SET = "guest_network_set";
    /**
     * 7.4.2	获取访客网络信息
     */
    String METHOD_GUEST_NETCORK_SHOW = "guest_network_show";
    /**
     * 7.5.1    获取周围无线接入点参数
     */
    String METHOD_SCAN_WIRELESS_NETWORK = "scan_wireless_network";
    /**
     * 7.5.2	设置上级无线基本参数
     */
    String METHOD_REPEATER_SETTING = "repeater_setting";

    /**
     * 7.6.1	对STA进行分组设置，包括添加、修改和删除操作
     */
    String METHOD_STA_GROUP_SET = "sta_group_set";
    /**
     * 7.6.2	获取STA分组信息
     */
    String METHOD_STA_GROUP_SHOW = "sta_group_show";

    /**
     * 8.1.1 设置ddns参数
     */
    String METHOD_DDNS_SETTING = "ddns_setting";
    /**
     * 8.1.2	获取ddns参数
     */
    String METHOD_DDNS_SHOW = "ddns_show";
    /**
     * 9.1.1	 获取路由信息
     */
    String METHOD_ROUTER_INFO_SHOW = "router_info_show";
    /**
     * 9.2.1	切换语言成功后调用
     */
    String METHOD_LANGUAGE_SET = "language_set";
    /**
     * 9.2.2	获取网页显示语言
     */
    String METHOD_LANGUAGE_GET = "language_get";
    /**
     * 9.3.1	获取当前设备类型
     */
    String METHOD_DEVICE_TYPE_GET = "device_type_get";
    /**
     * 9.4.1	描述：设置用户登录密码
     */
    String METHOD_USER_PSW_SETTING = "user_password_setting";
    /**
     * 9.4.2	描述：设置用户登录密码
     */
    String METHOD_USER_PSW_SHOW = "user_password_show";
    /**
     * 9.5.1	描述：设置网络时间
     */
    String METHOD_NTP_SETTING = "ntp_setting";
    /**
     * 9.5.2	获取ntp状态
     */
    String METHOD_NTP_SHOW = "ntp_show";
    /**
     * 9.6.1	描述：设置重启
     */
    String METHOD_REBOOT = "reboot";
    /**
     * 9.7.1    描述：恢复出厂
     */
    String METHOD_RESET = "reset";
    /**
     * 9.8.1	描述：升级文件提交到WEB server，并调用upload接口，WEB Server需要提供升级文件地址及长度，upload接口读取文件并处理升级流程。
     */
    String METHOD_UPDATE = "update";

    /**
     * 10.1.1	描述：获取组网设备列表
     */
    String METHOD_NETWORK_SHOW = "network_show";
    /**
     * 10.2.1	描述：获取组网邻居列表
     */
    String METHOD_NEIGHBOR_SHOW = "neighbor_show";
    /**
     * 10.3.1	描述：设置组网无线参数
     */
    String METHOD_NETWORK_WLAN_SETTING = "network_wlan_setting";
    /**
     * @deprecated 10.3.2    描述：获取组网wlan配置
     */
    String METHOD_NETWORK_WLAN_SHOW = "network_wlan_show";
    /**
     * @deprecated 10.3.3    描述：设置组网mesh配置
     */
    String METHOD_MESH_SETTING = "mesh_setting";

    /**
     * @deprecated 10.3.4   描述：获取组网mesh配置
     */
    String METHOD_MESH_SHOW = "mesh_show";
    /**
     * 10.3.5	描述：设置组网无线快连接口
     */
    String METHOD_MESH_QUICK_LINK = "mesh_quick_link";
    /**
     * 10.3.6	描述：设置组网无线快连接口
     */
    String METHOD_MESH_QUICK_SHOW = "mesh_quick_show";
    /**
     * 10.4.1   描述：添加设备到组网网络接口
     */
    String METHOD_NETWORK_ADD = "network_add";
    /**
     * 10.5.1	 设置设备基本信息，主要用于为设备重新命名，添加位置信息
     */
    String METHOD_DEVICE_CUSTOMER_INFO_MODIFY = "device_customer_info_modify";
    /**
     * 10.5.2	 获取用户自定义个性化数据
     */
    String METHOD_DEVICE_CUSTOMER_INFO_SHOW = "device_customer_info_show";
}
