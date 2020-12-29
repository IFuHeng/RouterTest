package com.changhong.telnettool.webinterface.been.plc;

/**
 * wifi 发射功率
 */
public enum EnumPowerTrans {
    _20per("20%", 0), _40per("40%", 1), _60per("60%", 2), _80per("80%", 3), _100per("100%", 4);

    String name;
    int code;

    EnumPowerTrans(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public static EnumPowerTrans findBandWidthByPlcCode(int code) {
        for (EnumPowerTrans value : EnumPowerTrans.values()) {
            if (value.code == code)
                return value;
        }
        return null;
    }
}
