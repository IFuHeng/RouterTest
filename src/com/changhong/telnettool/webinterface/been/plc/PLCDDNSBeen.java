package com.changhong.telnettool.webinterface.been.plc;


import com.changhong.telnettool.webinterface.been.BaseResponseBeen;

public class PLCDDNSBeen extends BaseResponseBeen  {
    private Integer enable;//1:使能；0:关闭
    private String domain_name;//域名
    private String user_name;//用户名
    private String user_password;//用户密码
    private Integer type;//0:DynDNS; 1:TZO

    public PLCDDNSBeen() {
    }

    public String getDomain_name() {
        return domain_name;
    }

    public void setDomain_name(String domain_name) {
        this.domain_name = domain_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

}

