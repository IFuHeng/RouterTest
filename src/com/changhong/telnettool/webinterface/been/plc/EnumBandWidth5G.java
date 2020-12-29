package com.changhong.telnettool.webinterface.been.plc;

public enum EnumBandWidth5G {
    _20MHz("20MHz", 0), _40MHz("40MHz", 1), _20_40MHz("20/40MHz", 2), _80MHz("80MHz", 3), _20_40_80MHz("20/40/80MHz", 4);

    String name;
    int code;

    EnumBandWidth5G(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public static EnumBandWidth5G findBandWidthByPlcCode(int code) {
        for (EnumBandWidth5G value : values()) {
            if (value.code == code)
                return value;
        }
        return null;
    }
}
