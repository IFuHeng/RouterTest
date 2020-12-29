package com.changhong.telnettool.been;

import java.util.ArrayList;
import java.util.List;

public class SSIDInfo {
    String SSID;
    String networkType;
    String authentication;
    String password;
    List<BSSIDPoint> arrBSSID;

    public SSIDInfo() {
    }

    @Override
    public String toString() {
        return "SSIDInfo{" +
                "SSID='" + SSID + '\'' +
                ", networkType='" + networkType + '\'' +
                ", authentication='" + authentication + '\'' +
                ", password='" + password + '\'' +
                ", arrBSSID=" + arrBSSID +
                '}';
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<BSSIDPoint> getArrBSSID() {
        return arrBSSID;
    }

    public void setArrBSSID(List<BSSIDPoint> arrBSSID) {
        this.arrBSSID = arrBSSID;
    }

    public void appendBssid(BSSIDPoint point) {
        if (arrBSSID == null)
            arrBSSID = new ArrayList<>();
        arrBSSID.add(point);
    }

    BSSIDPoint lastBssid() {
        if (arrBSSID == null || arrBSSID.isEmpty())
            return null;
        return arrBSSID.get(arrBSSID.size() - 1);
    }

}
