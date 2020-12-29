package com.changhong.telnettool.been;

import java.io.*;
import java.util.Arrays;

/**
 * 无线设备的驱动信息
 *
 * @author aluca
 */
public class WlanDriveInfo {
    /**
     * 驱动
     */
    String drive;

    /**
     * 供应商
     */
    String supplier;

    /**
     * 版本
     */
    String softwareVer;

    String[] supportedTypes;

    String date;

    boolean isSupportFIPS104_2Mode;//支持 FIPS 140-2 模式
    boolean isSupport802p11wMngProguard;//支持 802.11w 管理帧保护
    boolean isSupportBearerNetworks;//支持的承载网络

    public WlanDriveInfo() {

    }

    public WlanDriveInfo(InputStream cmdResponse) {

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(cmdResponse, "GBK"));
            for (String temp = bufferedReader.readLine(); temp != null; temp = bufferedReader.readLine()) {
                temp = temp.trim();
                if (temp.indexOf(':') == -1)
                    continue;

                String value = temp.substring(temp.indexOf(':') + 1).trim();

                if (temp.startsWith("驱动程序")) {
                    drive = value;
                } else if (temp.startsWith("供应商")) {
                    supplier = value;
                } else if (temp.startsWith("日期")) {
                    date = value;
                } else if (temp.startsWith("版本")) {
                    softwareVer = value;
                } else if (temp.startsWith("支持的无线电类型")) {
                    supportedTypes = value.split(" ");
                } else if (temp.contains("FIPS 140-2")) {
                    isSupportFIPS104_2Mode = "是".equals(value);
                } else if (temp.contains("802.11w")) {
                    isSupport802p11wMngProguard = "是".equals(value);
                } else if (temp.contains("支持的承载网络")) {
                    isSupportBearerNetworks = "是".equals(value);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String toString() {
        return "WlanDriveInfo{" +
                "drive='" + drive + '\'' +
                ", supplier='" + supplier + '\'' +
                ", softwareVer='" + softwareVer + '\'' +
                ", supportedTypes=" + Arrays.toString(supportedTypes) +
                ", date='" + date + '\'' +
                ", isSupportFIPS104_2Mode=" + isSupportFIPS104_2Mode +
                ", isSupport802p11wMngProguard=" + isSupport802p11wMngProguard +
                ", isSupportBearerNetworks=" + isSupportBearerNetworks +
                '}';
    }

    public String getDrive() {
        return drive;
    }

    public String getSupplier() {
        return supplier;
    }

    public String getSoftwareVer() {
        return softwareVer;
    }

    public String[] getSupportedTypes() {
        return supportedTypes;
    }

    public String getDate() {
        return date;
    }

    public boolean isSupportFIPS104_2Mode() {
        return isSupportFIPS104_2Mode;
    }

    public boolean isSupport802p11wMngProguard() {
        return isSupport802p11wMngProguard;
    }

    public boolean isSupportBearerNetworks() {
        return isSupportBearerNetworks;
    }

    public static final synchronized WlanDriveInfo load() {
        try {
            InputStream is = Runtime.getRuntime().exec("netsh wlan show drivers").getInputStream();
            return new WlanDriveInfo(is);
        } catch (IOException e) {
            e.printStackTrace();
            return new WlanDriveInfo();
        }
    }
}
