package com.changhong.telnettool.net.tftpserver;

public abstract class TFtpPacket {
    private EnumTftpActCode operateCode = EnumTftpActCode.UNKNOWN;

    public EnumTftpActCode getOperateCode() {
        return operateCode;
    }

    /**
     * @param i
     */
    public void setOperateCode(int i) {
        operateCode = EnumTftpActCode.getTypeByCode(i);
    }

    /**
     * @param i
     */
    public void setOperateCode(EnumTftpActCode i) {
        operateCode = i;
    }

}
