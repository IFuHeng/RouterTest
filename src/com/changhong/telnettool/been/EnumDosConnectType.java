package com.changhong.telnettool.been;

public enum EnumDosConnectType {
    ETH("以太网"), WLAN("WLAN");
    String tag;

    EnumDosConnectType(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
