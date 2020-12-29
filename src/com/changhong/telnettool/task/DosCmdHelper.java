package com.changhong.telnettool.task;

import com.changhong.telnettool.been.*;
import com.changhong.telnettool.tool.Tool;

import java.io.*;
import java.util.List;

public class DosCmdHelper {

    public String getWlanDeviceInfo() {
        return null;
    }

    public String getWlanList() {
        return null;
    }

    public static final void main(String[] args) {
//        System.out.println(isLinux());
//        System.out.println(isWindows());
//        System.out.println(System.getProperty("file.encoding"));
//        System.out.println(WlanDriveInfo.load().toString());
//        System.out.println(WlanInfo.load().toString());
//        System.out.println(LocalPerimeterWirelessList.load().toString());
//        System.out.println(WlanBssidList.load().toString());


        System.out.println(getLocalConnectState());

//        System.out.println(readDosRuntime("diskpart"));
//        System.err.println(createWlanProfile("panpan", "panpan", "501028panpan"));
    }

    public static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    /**
     * 创建dos环境下的wlan 的profile并引入连接
     */
    public static boolean createWlanProfile(String name, String ssid, String key) {
        String content = Tool.loadLocalFile("res/defaultDosWlanProfile.xml");
        content = content.replace("[NAME]", name);
        String HEX = Tool.turnString2HexString(ssid);
        content = content.replace("[HEX]", HEX).replace("[SSID]", ssid);

        if (key == null || key.length() == 0) {
            content = content.replace("[AUTHEN]", "open").replace("[ENCRY]", "none");
            int start = content.indexOf("<sharedKey>");
            int end = content.indexOf("</sharedKey>") + 12;
            String temp = content.substring(start, end);
            content = content.replace(temp, "");
        } else {
            content = content.replace("[AUTHEN]", "WPA2PSK").replace("[ENCRY]", "AES");
            content = content.replace("[KEYS]", key);
        }

        File file = new File(name + ".xml");
        Tool.writeFile(file, content, "GBK");
//        System.out.println(file.getAbsolutePath());
        String cmd = "netsh wlan add profile filename=\"" + file.getAbsolutePath() + "\"";
//        System.out.println(cmd);

        String value = readDosRuntime(cmd);
        if (!value.contains("已") || !value.contains("添加"))
            throw new RuntimeException(value);

        System.out.println(value);

        value = readDosRuntime("netsh wlan connect name=" + name + " ssid=" + ssid);
        System.out.println(value);

        file.delete();
        if (!value.contains("已") || !value.contains("完成"))
            throw new RuntimeException(value);

        return true;
    }

    public static String getCurrentWlanSSID() {
        WlanInfo obj = WlanInfo.load();
        return obj.getSSID();
    }

    public static String readDosRuntime(String cmd) {
        try {
            InputStream is = Runtime.getRuntime().exec(cmd).getInputStream();
            String string = new String(Tool.readFromIputStream(is), "GBK");
            is.close();
            return string;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Reader getDosRuntimeReader(String cmd) {
        try {
            InputStream is = Runtime.getRuntime().exec(cmd).getInputStream();
            return new InputStreamReader(is, "GBK");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<DosConnectState> getLocalConnectState() {
        InputStream is = null;
        try {
            is = Runtime.getRuntime().exec("ipconfig").getInputStream();
            List<DosConnectState> result = DosConnectState.read(is);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


}
