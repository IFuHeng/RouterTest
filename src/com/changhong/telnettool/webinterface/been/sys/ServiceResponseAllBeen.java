package com.changhong.telnettool.webinterface.been.sys;

import com.changhong.telnettool.webinterface.been.BaseResponseBeen;

public class ServiceResponseAllBeen extends BaseResponseBeen {
    /**
     * 0:disabled;1:enabled
     * when 7.3.1	wlan_access_setting:0:disabled;1:allow list;2:deny list
     */
    private Integer enabled;
    /**
     * 0:DynDNS;1:TZO
     */
    private Integer type;
    /**
     * 不超过64个字符
     */
    private String domain_name;
    /**
     * 不超过32字符
     */
    private String user_name;
    /**
     * 不超过32字符
     */
    private String user_password;

    public Integer getEnabled() {
        return enabled;
    }

    public Integer getType() {
        return type;
    }

    public String getDomain_name() {
        return domain_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_password() {
        return user_password;
    }

    @Override
    public String toString() {
        return "ServiceResponseAllBeen{" +
                "enable=" + enabled +
                ", type=" + type +
                ", domain_name='" + domain_name + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_password='" + user_password + '\'' +
                ", err_code=" + err_code +
                ", message='" + message + '\'' +
                ", waite_time=" + waite_time +
                '}';
    }
}
