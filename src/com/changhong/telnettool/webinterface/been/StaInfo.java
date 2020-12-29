package com.changhong.telnettool.webinterface.been;

public class StaInfo {
    public Integer speedTx;
    public Integer speedRx;
    private String name;
    private String ip;
    private String mac;
    /**
     * bit0: 有线;bit1:2.4G; bit2:5G；bit3:访客网络 如1表示有线； 2表示2.4G；4表示5G；8表示访客网络 , 16PLC
     */
    private int connect_type;
    /**
     * 在线时长 单位秒.(有线没有时长)
     */
    private int link_time;

    private long upload;
    private long download;

    private Integer rssi;

    /**
     * 自定义名称，存储在云端
     */
    private String custumName;

    private String superiorNode;

    public StaInfo() {
    }

    public String getName() {
        if (this.custumName != null && this.custumName.length() > 0)
            return this.custumName;
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCustumName(String custumName) {
        this.custumName = custumName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Integer getConnect_type() {
        return connect_type;
    }

    public ConnectType getConnectType() {

        for (ConnectType value : ConnectType.values()) {
            if (connect_type == value.getValue())
                return value;
        }
        return ConnectType.NO_CONNECT;
    }

    /**
     * @param connect_type bit0: 有线;bit1:2.4G; bit2:5G；bit3:访客网络 如1表示有线； 2表示2.4G；4表示5G；8表示访客网络
     */
    public void setConnect_type(Integer connect_type) {
        this.connect_type = connect_type;
    }

    public Integer getLink_time() {
        return link_time;
    }

    public void setLink_time(Integer link_time) {
        this.link_time = link_time;
    }

    public Long getUpload() {
        return upload;
    }

    public void setUpload(Integer upload) {
        this.upload = upload;
    }

    public Long getDownload() {
        return download;
    }

    public void setDownload(Integer download) {
        this.download = download;
    }

    public void setRssi(Integer rssi) {
        this.rssi = rssi;
    }

    public Integer getRssi() {
        return rssi;
    }

    public void setConnect_type(int connect_type) {
        this.connect_type = connect_type;
    }

    public void setLink_time(int link_time) {
        this.link_time = link_time;
    }

    public void setUpload(long upload) {
        this.upload = upload;
    }

    public void setDownload(long download) {
        this.download = download;
    }

    public String getCustumName() {
        return custumName;
    }

    public String getSuperiorNode() {
        return superiorNode;
    }

    public void setSuperiorNode(String superiorNode) {
        this.superiorNode = superiorNode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || mac == null || !(obj instanceof StaInfo))
            return super.equals(obj);

        return mac.equals(((StaInfo) obj).mac);
    }

    @Override
    public String toString() {
        return "StaInfo{" +
                "speedTx=" + speedTx +
                ", speedRx=" + speedRx +
                ", name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", mac='" + mac + '\'' +
                ", connect_type=" + connect_type +
                ", link_time=" + link_time +
                ", upload=" + upload +
                ", download=" + download +
                ", rssi=" + rssi +
                ", custumName='" + custumName + '\'' +
                '}';
    }
}
