package com.changhong.telnettool.webinterface.been.mesh;

import com.changhong.telnettool.webinterface.been.BaseResponseBeen;

import java.util.List;

public class MeshResponseAllBeen extends BaseResponseBeen {

    private List<ListInfo> mesh_clients;

    /**
     * 0:5g,1:2.4g
     */
    private String radio;

    private List<NeighborInfo> neighbor;

    private String ssid_2;
    /**
     * none/wpa2-psk/wpa2_mixed_psk
     */
    private String encryption_2;
    private String key_2;

    private String ssid_5;
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
     * 1:已开启， 0：未开启
     */
    private Integer quick_status;

    private List<CustomerInfo> dev_list;

    public List<ListInfo> getMesh_clients() {
        return mesh_clients;
    }

    public String getRadio() {
        return radio;
    }

    public List<NeighborInfo> getNeighbor() {
        return neighbor;
    }

    public String getSsid_2() {
        return ssid_2;
    }

    public String getEncryption_2() {
        return encryption_2;
    }

    public String getKey_2() {
        return key_2;
    }

    public String getSsid_5() {
        return ssid_5;
    }

    public String getEncryption_5() {
        return encryption_5;
    }

    public String getKey_5() {
        return key_5;
    }

    public Integer getRadio_id() {
        return radio_id;
    }

    public Integer getWlan_id() {
        return wlan_id;
    }

    public Integer getEnable() {
        return enable;
    }

    public String getMesh_id() {
        return mesh_id;
    }

    public Integer getMesh_encrypt_type() {
        return mesh_encrypt_type;
    }

    public Integer getAuth_mode() {
        return auth_mode;
    }

    public Integer getWpa2_cipher_suite() {
        return wpa2_cipher_suite;
    }

    public Integer getPre_key_format() {
        return pre_key_format;
    }

    public String getPre_key() {
        return pre_key;
    }

    public Integer getQuick_status() {
        return quick_status;
    }


    public List<CustomerInfo> getDev_list() {
        return dev_list;
    }

    public void setMesh_clients(List<ListInfo> mesh_clients) {
        this.mesh_clients = mesh_clients;
    }

    public void setRadio(String radio) {
        this.radio = radio;
    }

    public void setNeighbor(List<NeighborInfo> neighbor) {
        this.neighbor = neighbor;
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

    public void setQuick_status(Integer quick_status) {
        this.quick_status = quick_status;
    }

    public void setDev_list(List<CustomerInfo> dev_list) {
        this.dev_list = dev_list;
    }

    @Override
    public String toString() {
        return "MeshResponseAllBeen{" +
                "mesh_clients=" + mesh_clients +
                ", radio='" + radio + '\'' +
                ", neighbor=" + neighbor +
                ", ssid_2='" + ssid_2 + '\'' +
                ", encryption_2='" + encryption_2 + '\'' +
                ", key_2='" + key_2 + '\'' +
                ", ssid_5='" + ssid_5 + '\'' +
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
                ", quick_status=" + quick_status +
                ", dev_list=" + dev_list +
                '}';
    }
}
