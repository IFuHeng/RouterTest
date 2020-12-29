package com.changhong.telnettool.webinterface.been;

import java.util.List;

public class DeviceItem {
    private Boolean isUpConnected;
    private String deviceName;
    private String Ip;
    private String mac;
    private DeviceType type;
    private int staNum;
    private String location;
    private boolean isChild;
    private String upNodeName;
    private WanType wan_type;
    private String iconUrl;
    private Integer qlink;
    private String version;
    private String hard_version;
    private String uuid;
    private boolean isFromShared;
    private String ssid;
    private List<StaInfo> list_sta;

    public DeviceItem() {
    }


    public WanType getWan_type() {
        return wan_type;
    }

    public void setWan_type(WanType wan_type) {
        this.wan_type = wan_type;
    }

    public String getUpNodeName() {
        return upNodeName;
    }

    public void setUpNodeName(String upNodeName) {
        this.upNodeName = upNodeName;
    }


    public Boolean getUpConnected() {
        return isUpConnected;
    }

    public void setUpConnected(Boolean upConnected) {
        isUpConnected = upConnected;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceNameOrMac() {
        if (deviceName != null && deviceName.trim().length() > 0)
            return deviceName;
        else if (type != null)
            return type.getName();
        else
            return mac;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getIp() {
        if ("none_link".equals(Ip))
            return "0.0.0.0";
        return Ip;
    }

    public void setIp(String ip) {
        Ip = ip;
    }

    public boolean isLinkOn() {
        return Ip != null && !"none_link".equals(Ip);
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public DeviceType getType() {
        return type;
    }

    public void setType(DeviceType type) {
        this.type = type;
    }

    public int getStaNum() {
        return staNum;
    }

    public void setStaNum(int staNum) {
        this.staNum = staNum;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isChild() {
        return isChild;
    }

    public void setChild(boolean child) {
        isChild = child;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Integer getQlink() {
        return qlink;
    }

    public void setQlink(Integer qlink) {
        this.qlink = qlink;
    }

    @Override
    public String toString() {
        return "DeviceItem{" +
                "isUpConnected=" + isUpConnected +
                ", deviceName='" + deviceName + '\'' +
                ", Ip='" + Ip + '\'' +
                ", mac='" + mac + '\'' +
                ", type=" + type +
                ", staNum=" + staNum +
                ", location='" + location + '\'' +
                ", isChild=" + isChild +
                ", upNodeName='" + upNodeName + '\'' +
                ", wan_type=" + wan_type +
                ", iconUrl='" + iconUrl + '\'' +
                ", qlink=" + qlink +
                ", version=" + version +
                ", hard_version=" + hard_version +
                ", uuid=" + uuid +
                ", isFromShared=" + isFromShared +
                '}';
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public String getHard_version() {
        return hard_version;
    }

    public void setHard_version(String hard_version) {
        this.hard_version = hard_version;
    }

    public Boolean isBinded() {
        return uuid != null && uuid.length() > 0;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isFromShared() {
        return isFromShared;
    }

    public void setFromShared(boolean fromShared) {
        isFromShared = fromShared;
    }

    public List<StaInfo> getList_sta() {
        return list_sta;
    }

    public void setStaList(List<StaInfo> staInfos) {
        list_sta = staInfos;
    }
}
