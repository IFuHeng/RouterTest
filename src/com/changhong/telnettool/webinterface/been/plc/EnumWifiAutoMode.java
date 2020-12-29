package com.changhong.telnettool.webinterface.been.plc;

public enum EnumWifiAutoMode {
    NONE(null, 0), TKIP(null, 2),
    AES(null, 3), TKIP_AES("TKIP&AES", 4);
    String name;
    int code;

    EnumWifiAutoMode(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        if (name == null)
            return super.name();
        return name;
    }

    public int getCode() {
        return code;
    }

    public static EnumWifiAutoMode getModeByCode(int code) {
        for (EnumWifiAutoMode value : values()) {
            if (value.getCode() == code)
                return value;
        }
        return null;
    }

    public static int getIndexByCode(int code) {
        for (int i = 0; i < values().length; i++) {
            if (values()[i].getCode() == code) {
                return i;
            }
        }
        return -1;
    }
}
