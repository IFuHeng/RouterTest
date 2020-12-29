package com.changhong.telnettool.webinterface.been.plc;

public enum EnumAutoMode {
    open("open", 0), wpa_psk("wpa-psk", 2),
    wpa2_psk("wpa2-psk", 3), wpa_wp2_psk("wpa/wp2-psk", 4);

    String name;
    int code;

    EnumAutoMode(String name, int code) {
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

    public static EnumAutoMode getModeByCode(int code) {
        for (EnumAutoMode value : values()) {
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
