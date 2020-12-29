package com.changhong.telnettool.been;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class LocalPerimeterWirelessList {

    List<SSIDInfo> arrSSID;

    public LocalPerimeterWirelessList() {

    }

    public LocalPerimeterWirelessList(String data) {
        init(new BufferedReader(new StringReader(data)));
    }

    public LocalPerimeterWirelessList(InputStream is) {
        try {
            init(new BufferedReader(new InputStreamReader(is, "GBK")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private void init(BufferedReader bufferedReader) {
        try {
            boolean need = false;
            for (String temp = bufferedReader.readLine(); temp != null; temp = bufferedReader.readLine()) {
                temp = temp.trim();
                if (!need) {
                    if (temp.startsWith("=") && temp.endsWith("=")) {
                        temp = Pattern.compile("[=]").matcher(temp).replaceAll("");
                        if (temp != null && temp.contains("显示网络模式 MODEBSSID")) {
                            System.err.println("===============start =============");
                            need = true;
                        }
                    }
                    continue;
                } else {
                    if (temp.startsWith("=") && temp.endsWith("=")) {
                        temp = Pattern.compile("[=]").matcher(temp).replaceAll("");
                        if (temp != null && !temp.isEmpty()) {
                            System.err.println("===============end =============");
                            break;
                        }
                        continue;
                    }
                }

                if (temp.indexOf(':') == -1)
                    continue;
                System.err.println(temp);
                String value = temp.substring(temp.indexOf(':') + 1).trim();

                if (temp.startsWith("SSID")) {
                    SSIDInfo info = new SSIDInfo();
                    info.setSSID(value);
                    appendSSIDInfo(info);
                } else if (temp.startsWith("Network type")) {
                    lastSSIDInfo().setNetworkType(value);
                } else if (temp.startsWith("身份验证")) {
                    lastSSIDInfo().setAuthentication(value);
                } else if (temp.startsWith("加密")) {
                    lastSSIDInfo().setPassword(value);
                } else if (temp.startsWith("BSSID")) {
                    BSSIDPoint bp = new BSSIDPoint();
                    bp.mac = value;
                    lastSSIDInfo().appendBssid(bp);
                } else if (temp.startsWith("信号")) {
                    lastSSIDInfo().lastBssid().per_signal_strength = Integer.parseInt(Pattern.compile("[^0-9]").matcher(value).replaceAll(""));
                } else if (temp.startsWith("无线电类型")) {
                    lastSSIDInfo().lastBssid().wirelessType = value;
                } else if (temp.startsWith("频道")) {
                    lastSSIDInfo().lastBssid().channel = Integer.parseInt(value);
                } else if (temp.startsWith("基本速率") && value != null) {
                    String[] list = value.split(" ");
                    lastSSIDInfo().lastBssid().baseSpeed = new float[list.length];
                    for (int i = 0; i < list.length; i++) {
                        lastSSIDInfo().lastBssid().baseSpeed[i] = Float.parseFloat(list[i]);
                    }
                    lastSSIDInfo().lastBssid().speedUnit = temp.substring(temp.indexOf('(') + 1, temp.indexOf(')'));
                } else if (temp.startsWith("其他速率") && value != null) {
                    String[] list = value.split(" ");
                    lastSSIDInfo().lastBssid().otherSpeed = new float[list.length];
                    for (int i = 0; i < list.length; i++) {
                        lastSSIDInfo().lastBssid().otherSpeed[i] = Float.parseFloat(list[i]);
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<SSIDInfo> getArrSSID() {
        return arrSSID;
    }

    public void setArrSSID(List<SSIDInfo> arrSSID) {
        this.arrSSID = arrSSID;
    }

    private void appendSSIDInfo(SSIDInfo info) {
        if (arrSSID == null)
            arrSSID = new ArrayList<>();
        arrSSID.add(info);
    }

    private SSIDInfo lastSSIDInfo() {
        return arrSSID.get(arrSSID.size() - 1);
    }

    @Override
    public String toString() {
        return "LocalPerimeterWirelessList{" +
                "arrSSID=" + arrSSID +
                '}';
    }

    public static final synchronized LocalPerimeterWirelessList load() {
        try {
            InputStream is = Runtime.getRuntime().exec("netsh wlan show all").getInputStream();
            return new LocalPerimeterWirelessList(is);
        } catch (IOException e) {
            e.printStackTrace();
            return new LocalPerimeterWirelessList();
        }
    }

}
