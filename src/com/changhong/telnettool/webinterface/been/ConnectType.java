package com.changhong.telnettool.webinterface.been;
public enum ConnectType {
    //    bit0: 有线;bit1:2.4G; bit2:5G；bit3:访客网络
//    如1表示有线； 2表示2.4G；4表示5G；8表示访客网络

    NO_CONNECT(0, "未连接"),
    LINE(1, "有线"),
    WIFI24(2, "2.4G"),
    WIFI5(4, "5G"),
    WIFI2or5(6, "2.4G/5G"),
    GUEST(8, "访客网络"),
    PLC(16, "电力猫");


    private int value;
    private String name;

    ConnectType(int value, String string) {
        this.value = value;
        this.name = string;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
