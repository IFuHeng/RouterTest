package com.changhong.telnettool.net.tftpserver;

import com.changhong.telnettool.net.tftpserver.TFtpPacket;

public class RWPacket extends TFtpPacket {

    private String fileName;
    private String mode;

    public RWPacket() {

    }

    /**
     * @return
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @return
     */
    public String getMode() {
        return mode;
    }

    /**
     * @param string
     */
    public void setFileName(String string) {
        fileName = string;
    }


    /**
     * @param string
     */
    public void setMode(String string) {
        mode = string;
    }

    public String toString() {
        return (getOperateCode() == EnumTftpActCode.RRQ ? "read packet:" : "write packet:") + "[fileName:=" +
                fileName + "][mode=" + mode + "]";
    }

}
