package com.changhong.telnettool.webinterface.been.plc;

public enum EnumBandWidth {
    _20MHz("20MHz", 0), _40MHz("40MHz", 1), _20_40MHz("20/40MHz", 2);

    String name;
    int code;

    EnumBandWidth(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public static EnumBandWidth findBandWidthByPlcCode(int code) {
        for (EnumBandWidth value : values()) {
            if (value.code == code)
                return value;
        }
        return null;
    }
}
