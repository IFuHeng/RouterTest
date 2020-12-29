package com.changhong.telnettool.net.tftpserver;

public enum EnumTftpActCode {
    UNKNOWN("unknown", 0),
    RRQ("read request", 1),
    WRQ("write request", 2),
    DATA("data", 3),
    ACK("Acknowledgment", 4),
    ERROR("error", 5);

    final int code;
    String name;


    EnumTftpActCode(String name, int code) {
        this.code = code;
        this.name = name;
    }

    public static EnumTftpActCode getTypeByCode(int code) {
        for (EnumTftpActCode value : EnumTftpActCode.values()) {
            if (value.code == code)
                return value;
        }
        return UNKNOWN;
    }
}
