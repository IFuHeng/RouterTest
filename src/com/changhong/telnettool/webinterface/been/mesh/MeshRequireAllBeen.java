package com.changhong.telnettool.webinterface.been.mesh;

import java.util.List;

public class MeshRequireAllBeen {

    /**
     * 1为WEB,2为APP，3为其他
     */
    private Integer src_type = 1;

    /**
     * 1:enable 2:disabled
     */
    private Integer ssid_2_enable;
    private String ssid_2;
    private String encryption_2;
    private String key_2;
    private String ssid_5;
    /**
     * 1:enable 2 :disabled
     */
    private Integer ssid_5_enable;
    /**
     * none/wpa2-psk/wpa2_mixed_psk
     */
    private String encryption_5;
    private String key_5;

    /**
     * 0:5G 1:2.4G
     */
    private Integer radio_id;
    /**
     * 0,1,2,3 vap,默认为0
     */
    private Integer wlan_id;
    /**
     * 0，1
     */
    private Integer enable;
    private String mesh_id;
    /**
     * 0：不加密;4：wpa2加密
     */
    private Integer mesh_encrypt_type;
    /**
     * 1：自动加密  2：wpa加密
     */
    private Integer auth_mode;
    /**
     * 默认为：0
     */
    private Integer wpa2_cipher_suite;
    /**
     * 0：字符串 ；1：十六进制
     */
    private Integer pre_key_format;
    private String pre_key;

    /**
     * 1:添加设备到组网网络
     */
    private Integer op;
    private List<ListInfo> dev;

    private List<CustomerInfo> dev_list;

    public void setSrc_type(Integer src_type) {
        this.src_type = src_type;
    }

    public void setSsid_2_enable(Integer ssid_2_enable) {
        this.ssid_2_enable = ssid_2_enable;
    }

    public void setSsid_2(String ssid_2) {
        this.ssid_2 = ssid_2;
    }

    public void setEncryption_2(String encryption_2) {
        this.encryption_2 = encryption_2;
    }

    public void setKey_2(String key_2) {
        this.key_2 = key_2;
    }

    public void setSsid_5(String ssid_5) {
        this.ssid_5 = ssid_5;
    }

    public void setSsid_5_enable(Integer ssid_5_enable) {
        this.ssid_5_enable = ssid_5_enable;
    }

    public void setEncryption_5(String encryption_5) {
        this.encryption_5 = encryption_5;
    }

    public void setKey_5(String key_5) {
        this.key_5 = key_5;
    }

    public void setRadio_id(Integer radio_id) {
        this.radio_id = radio_id;
    }

    public void setWlan_id(Integer wlan_id) {
        this.wlan_id = wlan_id;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public void setMesh_id(String mesh_id) {
        this.mesh_id = mesh_id;
    }

    public void setMesh_encrypt_type(Integer mesh_encrypt_type) {
        this.mesh_encrypt_type = mesh_encrypt_type;
    }

    public void setAuth_mode(Integer auth_mode) {
        this.auth_mode = auth_mode;
    }

    public void setWpa2_cipher_suite(Integer wpa2_cipher_suite) {
        this.wpa2_cipher_suite = wpa2_cipher_suite;
    }

    public void setPre_key_format(Integer pre_key_format) {
        this.pre_key_format = pre_key_format;
    }

    public void setPre_key(String pre_key) {
        this.pre_key = pre_key;
    }

    public void setOp(Integer op) {
        this.op = op;
    }

    public void setDev(List<ListInfo> dev) {
        this.dev = dev;
    }

    public void setDev_list(List<CustomerInfo> dev_list) {
        this.dev_list = dev_list;
    }

    @Override
    public String toString() {
        return "MeshRequireAllBeen{" +
                "src_type=" + src_type +
                ", ssid_2_enable=" + ssid_2_enable +
                ", ssid_2='" + ssid_2 + '\'' +
                ", encryption_2='" + encryption_2 + '\'' +
                ", key_2='" + key_2 + '\'' +
                ", ssid_5='" + ssid_5 + '\'' +
                ", ssid_5_enable=" + ssid_5_enable +
                ", encryption_5='" + encryption_5 + '\'' +
                ", key_5='" + key_5 + '\'' +
                ", radio_id=" + radio_id +
                ", wlan_id=" + wlan_id +
                ", enable=" + enable +
                ", mesh_id='" + mesh_id + '\'' +
                ", mesh_encrypt_type=" + mesh_encrypt_type +
                ", auth_mode=" + auth_mode +
                ", wpa2_cipher_suite=" + wpa2_cipher_suite +
                ", pre_key_format=" + pre_key_format +
                ", pre_key='" + pre_key + '\'' +
                ", op=" + op +
                ", dev=" + dev +
                ", dev_list=" + dev_list +
                '}';
    }
}
