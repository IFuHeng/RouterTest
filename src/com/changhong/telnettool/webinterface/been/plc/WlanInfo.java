package com.changhong.telnettool.webinterface.been.plc;


import com.changhong.telnettool.webinterface.been.BaseResponseBeen;

public class WlanInfo extends BaseResponseBeen {
    private Integer enable;//		0: disable    1: enable
    private Integer bus_switch_2;//		0:disable 1:enable 2.4G总开关
    private Integer bus_switch_5;//		0:disable 1:enable 5G总开关
    private WifiBaseInfo wifiBase2G;
    private WifiBaseInfo wifiBase5G;
    private WifiAdvanceInfo wifiAdvanceInfo2G;
    private WifiAdvanceInfo wifiAdvanceInfo5G;

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public Integer getBus_switch_2() {
        return bus_switch_2;
    }

    public void setBus_switch_2(Integer bus_switch_2) {
        this.bus_switch_2 = bus_switch_2;
    }

    public Integer getBus_switch_5() {
        return bus_switch_5;
    }

    public void setBus_switch_5(Integer bus_switch_5) {
        this.bus_switch_5 = bus_switch_5;
    }


    public WifiBaseInfo getWifiBase5G() {
        return wifiBase5G;
    }

    public void setWifiBase5G(WifiBaseInfo wifiBase5G) {
        this.wifiBase5G = wifiBase5G;
    }

    public WifiBaseInfo getWifiBase2G() {
        return wifiBase2G;
    }

    public void setWifiBase2G(WifiBaseInfo wifiBase2G) {
        this.wifiBase2G = wifiBase2G;
    }

    public WifiAdvanceInfo getWifiAdvanceInfo2G() {
        return wifiAdvanceInfo2G;
    }

    public void setWifiAdvanceInfo2G(WifiAdvanceInfo wifiAdvanceInfo2G) {
        this.wifiAdvanceInfo2G = wifiAdvanceInfo2G;
    }

    public WifiAdvanceInfo getWifiAdvanceInfo5G() {
        return wifiAdvanceInfo5G;
    }

    public void setWifiAdvanceInfo5G(WifiAdvanceInfo wifiAdvanceInfo5G) {
        this.wifiAdvanceInfo5G = wifiAdvanceInfo5G;
    }
}
