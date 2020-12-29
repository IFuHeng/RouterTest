package com.changhong.telnettool.net.tftpserver;

public class ACKPacket extends TFtpPacket {
    private int block;

    public ACKPacket() {
        setOperateCode(EnumTftpActCode.ACK);
    }

    /**
     * @return
     */
    public int getBlock() {
        return block;
    }

    /**
     * @param i
     */
    public void setBlock(int i) {
        block = i;
    }

    public String toString() {
        return "Ack packet:[block="+block+"]";
    }
}
