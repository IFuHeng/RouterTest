package com.changhong.telnettool.webinterface.been.mesh;

public class NeighborInfo {

    private String mac;
    /**
     * 0:5g,1:2.4g
     */
    private Integer radio;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Integer getRadio() {
        return radio;
    }

    public void setRadio(Integer radio) {
        this.radio = radio;
    }

    @Override
    public String toString() {
        return "NeighborInfo{" +
                "mac='" + mac + '\'' +
                ", radio=" + radio +
                '}';
    }
}
