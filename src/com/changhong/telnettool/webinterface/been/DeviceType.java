package com.changhong.telnettool.webinterface.been;

public enum DeviceType {
    BWR("BWR-510", "15", "长虹分布式路由器"),
    BWR2("BWR-5102", "15", "长虹分布式路由器-YOUCAST"),
    BWR3("BWR-5103", "15", "长虹分布式路由器-南美"),
    R2s("R2s", "14", "长虹千兆路由器"),
    PLC("PN200", "4", "长虹电力猫"),
    NAS("BWR-NAS", "18", "NAS");

    private String name;

    public String getTypeOnCloud() {
        return typeOnCloud;
    }

    private String typeOnCloud;

    private int iconResId;

    DeviceType(String n, String typeOnCloud, String name) {
        name = n;
        this.typeOnCloud = typeOnCloud;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getIconResId() {
        return iconResId;
    }

    public static DeviceType getDeviceTypeFromName(String deviceType) {
        for (DeviceType value : values()) {
            if (value.getName().equalsIgnoreCase(deviceType))
                return value;
        }
        return null;
    }

    public static DeviceType getDeviceTypeByCloudCode(String cloudCode) {
        for (DeviceType value : values()) {
            if (value.getTypeOnCloud().equalsIgnoreCase(cloudCode))
                return value;
        }
        return null;
    }

}
