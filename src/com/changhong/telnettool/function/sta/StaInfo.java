package com.changhong.telnettool.function.sta;

import com.changhong.telnettool.tool.Tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class StaInfo {
    private long MAC;
    int ip;
    String name;
    boolean is5g;
    private int state;
    private long tx_bytes;
    private long rx_bytes;
    private int tx_pkts;
    private int rx_pkts;
    private int rssi;
    int lease;
    private String band_width;

    int speedTx;
    int speedRx;

    public long getMAC() {
        return MAC;
    }

    public int getIp() {
        return ip;
    }

    public String getName() {
        return name;
    }

    public boolean is5g() {
        return is5g;
    }

    public long getTx_bytes() {
        return tx_bytes;
    }

    public long getRx_bytes() {
        return rx_bytes;
    }

    public int getTx_pkts() {
        return tx_pkts;
    }

    public int getRx_pkts() {
        return rx_pkts;
    }

    public int getRssi() {
        return rssi;
    }

    public int getLease() {
        return lease;
    }

    public int getState() {
        return state;
    }

    public String getBand_width() {
        return band_width;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StaInfo bean = (StaInfo) o;

        return MAC == bean.MAC;
    }

    @Override
    public int hashCode() {
        int result = (int) MAC;
        result = (int) (31 * result + (MAC >>> 32 & 0xffffffff));
        return result;
    }

    @Override
    public String toString() {
        return "StaInfo{" +
                "MAC='" + Tool.turnMacString(MAC) + '\'' +
                ", ip='" + Tool.turn2IpV4(ip) + '\'' +
                ", name='" + name + '\'' +
                ", is5g=" + is5g +
                ", state=" + state +
                ", tx_bytes=" + tx_bytes +
                ", rx_bytes=" + rx_bytes +
                ", tx_pkts=" + tx_pkts +
                ", rx_pkts=" + rx_pkts +
                ", rssi=" + rssi +
                '}';
    }

    public static final ArrayList<StaInfo> load(String info) {
        ArrayList<StaInfo> result = new ArrayList<>();
        BufferedReader br = new BufferedReader(new StringReader(info));
        try {
            String temp;
            StaInfo cur = null;
            while ((temp = br.readLine()) != null) {
                if (temp.indexOf("stat_info...") != -1) {
                    cur = new StaInfo();
                    result.add(cur);
                    continue;
                } else if (temp.contains("state")) {
                    cur.state = Integer.parseInt(temp.substring(temp.indexOf(':') + 1).trim());
                } else if (temp.contains("hwaddr")) {
                    cur.MAC = analysisMacString2Long(temp.substring(temp.indexOf(':') + 1).trim());
                } else if (temp.contains("tx_bytes")) {
                    if (cur.tx_bytes != 0) {
                        continue;
                    }
                    cur.tx_bytes = Long.parseLong(temp.substring(temp.indexOf(':') + 1).trim());
                } else if (temp.contains("rx_bytes")) {
                    if (cur.rx_bytes != 0) {
                        continue;
                    }
                    cur.rx_bytes = Long.parseLong(temp.substring(temp.indexOf(':') + 1).trim());
                } else if (temp.contains("rx_pkts")) {
                    cur.rx_pkts = Integer.parseInt(temp.substring(temp.indexOf(':') + 1).trim());
                } else if (temp.contains("tx_pkts")) {
                    cur.tx_pkts = Integer.parseInt(temp.substring(temp.indexOf(':') + 1).trim());
                } else if (temp.contains("rssi")) {
                    String tmp = temp.substring(temp.indexOf(':') + 1).trim();
                    int tmpIndex = tmp.indexOf('(');
                    if (tmpIndex != -1)
                        tmp = tmp.substring(0, tmpIndex).trim();
                    cur.rssi = Integer.parseInt(tmp);
                    cur.rssi -= 100;
                } else if (temp.contains("rx_bw:")) {
                    cur.band_width = temp.substring(temp.indexOf(':') + 1).trim();
                } else if (temp.contains("tx_bw:")) {
                    String t = temp.substring(temp.indexOf(':') + 1).trim();
                    if (!cur.band_width.equalsIgnoreCase(t))
                        cur.band_width += '/' + t;
                } else if (temp.contains("tx_bw_bak:")) {
                    String t = temp.substring(temp.indexOf(':') + 1).trim();
                    if (!cur.band_width.contains(t))
                        cur.band_width += '/' + t;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    /**
     * 将mac字符串型转为长整型
     *
     * @param mac
     * @return
     */
    private static final long analysisMacString2Long(String mac) {
        if (mac == null)
            return 0;
        if (mac.indexOf(':') != -1)
            mac = mac.replace(":", "");
        try {
            return Long.parseLong(mac, 16);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
