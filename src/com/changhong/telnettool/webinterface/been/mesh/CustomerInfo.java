package com.changhong.telnettool.webinterface.been.mesh;

public class CustomerInfo {
    private String mac;//设备mac地址（无冒号）
    private String dev_name;
    private String dev_location;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        if (mac != null && mac.contains(":"))
            this.mac = mac.replace(":", "");
        else
            this.mac = mac;
    }

    public String getDev_name() {
        return dev_name;
    }

    public void setDev_name(String dev_name) {
        if (dev_name != null && dev_name.length() > 32)
            this.dev_name = dev_name.substring(0, 32);
        else
            this.dev_name = dev_name;
    }

    public String getDev_location() {
        return dev_location;
    }

    public void setDev_location(String dev_location) {
        if (dev_location != null && dev_location.length() > 32)
            this.dev_location = dev_location.substring(0, 32);
        else
            this.dev_location = dev_location;
    }

    @Override
    public String toString() {
        return "CustomerInfo{" +
                "mac='" + mac + '\'' +
                ", dev_name='" + dev_name + '\'' +
                ", dev_location='" + dev_location + '\'' +
                '}';
    }
}
