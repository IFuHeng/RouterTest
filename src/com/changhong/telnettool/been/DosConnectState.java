package com.changhong.telnettool.been;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DosConnectState {
    EnumDosConnectType type;
    String mac;
    String ip;
    String mask;
    String gateway;
    boolean dhpcEnable;

    public static List<DosConnectState> read(String str) {
        BufferedReader br = new BufferedReader(new StringReader(str));
        List<DosConnectState> result = read(br);
        return result;
    }

    public static List<DosConnectState> read(InputStream is) {
        List<DosConnectState> result = null;
        try {
            is = Runtime.getRuntime().exec("ipconfig /all").getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "GBK"));
            result = read(br);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<DosConnectState> read(BufferedReader br) {
        List<DosConnectState> result = null;
        try {
            String temp;
            DosConnectState state = null;
            while ((temp = br.readLine()) != null) {
                if (temp.trim().length() == 0)
                    continue;
                if (temp.charAt(0) == ' ') {
                    if (state == null)
                        continue;
                    if (temp.contains("IPv4")) {
                        String tmp = temp.substring(temp.indexOf(':') + 1).trim();
                        int index = tmp.indexOf('(');
                        if (index != -1)
                            tmp = tmp.substring(0, tmp.indexOf('('));
                        state.ip = tmp;
                    } else if (temp.contains("子网掩码"))
                        state.mask = temp.substring(temp.indexOf(':') + 1).trim();
                    else if (temp.contains("默认网关"))
                        state.gateway = temp.substring(temp.indexOf(':') + 1).trim();
                    else if (temp.contains("物理地址"))
                        state.mac = temp.substring(temp.indexOf(':') + 1).trim();
                    else if (temp.contains("DHCP 已启用"))
                        state.dhpcEnable = "是".equals(temp.substring(temp.indexOf(':') + 1).trim());
                } else {
                    state = null;
                    for (EnumDosConnectType value : EnumDosConnectType.values()) {
                        if (temp.lastIndexOf(value.getTag()) == temp.length() - 1 - value.getTag().length()) {
                            state = new DosConnectState();
                            state.type = value;
                            if (result == null)
                                result = new ArrayList();
                            result.add(state);
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String toString() {
        return "DosConnectState{" +
                "type=" + type +
                ", mac='" + mac + '\'' +
                ", ip='" + ip + '\'' +
                ", mask='" + mask + '\'' +
                ", gateway='" + gateway + '\'' +
                ", dhpcEnable=" + dhpcEnable +
                '}';
    }
}
