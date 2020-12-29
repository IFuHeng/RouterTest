package com.changhong.telnettool.function.sta;

import com.changhong.telnettool.tool.Tool;

import java.util.ArrayList;

public class StaInfo1 {
    long mac;
    int ip;
    String name;
    int lease;

    public StaInfo1() {

    }

    public StaInfo1(byte[] data, int offset) {
        this.mac = readMac(data, offset);
        offset += 16;
        this.ip = readInt(data, offset);
        offset += 4;
        this.lease = readInt(data, offset);
        offset += 4;
        this.name = readName(data, offset);
    }

    @Override
    public String toString() {
        return "StaInfo1{" +
                "mac='" + Tool.turnMacString(mac) + '\'' +
                ", ip='" + Tool.turn2IpV4(ip) + '\'' +
                ", lease=" + lease +
                ", name='" + name + '\'' +
                '}';
    }

    private final long readMac(byte[] data, int offset) {
        long result = 0;
        for (int i = 0; i < 6; i++) {
            result <<= 8;
            result |= data[offset + i] & 0xff;
        }
        return result;
    }


    private final int readInt(byte[] data, int offset) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result <<= 8;
            result += data[offset + i] & 0xff;
        }
        return result;
    }

    private final String readName(byte[] data, int offset) {
        return new String(data, offset, Math.min(64, data.length - offset)).trim();
    }

    public static final ArrayList<StaInfo1> read(byte[] data) {
        int offset = 0;
        ArrayList<StaInfo1> result = new ArrayList<>();
        while (data.length > offset) {
            result.add(new StaInfo1(data, offset));
            offset += 88;
//            System.out.println("还剩下:" + (data.length - offset));
        }
        return result;
    }
}
