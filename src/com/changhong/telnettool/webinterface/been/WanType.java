package com.changhong.telnettool.webinterface.been;

public enum WanType {
    STATIC("static", 1), DHCP("dhcp", 2), PPPOE("pppoe", 3), UNKONWN("", 0);
    private String name;

    private int typeCode;

    public int getTypeCode() {
        return typeCode;
    }

    WanType(String n, int type) {
        name = n;
        typeCode = type;
    }

    public String getName() {
        return name;
    }

    public static WanType getDeviceTypeFromName(String type) {
        for (WanType value : values()) {
            if (value.getName().equalsIgnoreCase(type))
                return value;
        }
        return UNKONWN;
    }

    public static WanType getDeviceTypeFromTypeCode(int type) {
        for (WanType value : values()) {
            if (value.typeCode == type)
                return value;
        }
        return UNKONWN;
    }
}
