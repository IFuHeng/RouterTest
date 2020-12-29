package com.changhong.telnettool.webinterface.been.mesh;

import com.changhong.telnettool.webinterface.been.StaInfo;

import java.util.List;

public class ListInfo  {

    private String mac;
    private String ip;
    /**
     * 1表示快连接口下挂的临时设备；0表示正式组网设备；
     */
    private Integer qlink;
    /**
     * 该设备下连接设备信息
     */
    private List<StaInfo> sta_info;

    public ListInfo() {
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getQlink() {
        return qlink;
    }

    public void setQlink(Integer qlink) {
        this.qlink = qlink;
    }

    public List<StaInfo> getSta_info() {
        return sta_info;
    }

    public void setSta_info(List<StaInfo> sta_info) {
        this.sta_info = sta_info;
    }

    @Override
    public String toString() {
        return "ListInfo{" +
                "mac='" + mac + '\'' +
                ", ip='" + ip + '\'' +
                ", qlink='" + qlink + '\'' +
                ", sta_info=" + sta_info +
                '}';
    }
}
