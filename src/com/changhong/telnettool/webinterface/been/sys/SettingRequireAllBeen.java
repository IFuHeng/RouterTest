package com.changhong.telnettool.webinterface.been.sys;

public class SettingRequireAllBeen {


    /**
     * 1为WEB,2为APP，3为其他
     */
    private Integer src_type = 1;
    /**
     * 1 为英语。0 为汉语。
     */
    private Integer language_flag;

    /**
     * 不超过32字符
     */
    private String user_password;
    /**
     * 不超过32字符
     */
    private String new_user_password;
    /**
     * 设置向导中必须	1：为设置向导设置密码；非1或该字段不存在：为普通设置密码
     */
    private Integer wizard_flag;

    private String time_zone;

    /**
     * 	1.回复默认值，2.恢复出厂值
     */
    private Integer type;

    /**
     * 上传文件的完整路径加名称，如“/tmp/fw.bin”。
     */
    private String fw_name;
    /**
     * 文件的大小
     */
    private Integer fw_len;

    public void setSrc_type(Integer src_type) {
        this.src_type = src_type;
    }

    public void setLanguage_flag(Integer language_flag) {
        this.language_flag = language_flag;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public void setNew_user_password(String new_user_password) {
        this.new_user_password = new_user_password;
    }

    public void setWizard_flag(Integer wizard_flag) {
        this.wizard_flag = wizard_flag;
    }

    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setFw_name(String fw_name) {
        this.fw_name = fw_name;
    }

    public void setFw_len(Integer fw_len) {
        this.fw_len = fw_len;
    }

    public Integer getSrc_type() {
        return src_type;
    }

    public Integer getLanguage_flag() {
        return language_flag;
    }

    public String getUser_password() {
        return user_password;
    }

    public String getNew_user_password() {
        return new_user_password;
    }

    public Integer getWizard_flag() {
        return wizard_flag;
    }

    public String getTime_zone() {
        return time_zone;
    }

    public Integer getType() {
        return type;
    }

    public String getFw_name() {
        return fw_name;
    }

    public Integer getFw_len() {
        return fw_len;
    }

    @Override
    public String toString() {
        return "SettingRequireAllBeen{" +
                "src_type=" + src_type +
                ", language_flag=" + language_flag +
                ", user_password='" + user_password + '\'' +
                ", new_user_password='" + new_user_password + '\'' +
                ", wizard_flag=" + wizard_flag +
                ", time_zone='" + time_zone + '\'' +
                ", type=" + type +
                ", fw_name='" + fw_name + '\'' +
                ", fw_len=" + fw_len +
                '}';
    }
}
