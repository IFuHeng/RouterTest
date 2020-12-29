package com.changhong.telnettool.webinterface.been.sys;

public class ServiceRequireAllBeen {


    /**
     * 1为WEB,2为APP，3为其他
     */
    private Integer src_type = 1;

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

    public void setSrc_type(Integer src_type) {
        this.src_type = src_type;
    }

    public void setEnabled(Integer enable) {
        this.enabled = enable;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setDomain_name(String domain_name) {
        this.domain_name = domain_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    @Override
    public String toString() {
        return "ServiceRequireAllBeen{" +
                "src_type=" + src_type +
                ", enable=" + enabled +
                ", type=" + type +
                ", domain_name='" + domain_name + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_password='" + user_password + '\'' +
                '}';
    }
}
