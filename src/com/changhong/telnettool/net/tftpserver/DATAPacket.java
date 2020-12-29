package com.changhong.telnettool.net.tftpserver;

import com.changhong.telnettool.tool.Tool;

import java.util.Arrays;

public class DATAPacket extends TFtpPacket {
    private int block;
    private byte[] data;

    public DATAPacket() {
        setOperateCode(EnumTftpActCode.DATA);
    }

    /**
     * @return
     */
    public int getBlock() {
        return block;
    }

    /**
     * @return
     */
    public byte[] getData() {
        return data;
    }

    /**
     * @param block
     */
    public void setBlock(int block) {
        this.block = block;
    }

    /**
     * @param data
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    public String toString() {
        return "Data packet:[block=" + block + "][data size=" + (data == null ? 0 : data.length) + "] : " + (data == null ? "null" : Tool.turnByteArrayData2HexString(data));
    }
}
