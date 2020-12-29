package com.changhong.telnettool.net.tftpserver;

public class BadPacketFormatException extends Exception {

    public BadPacketFormatException() {
        super();
    }

    public BadPacketFormatException(String msg) {
        super(msg);
    }
}
