package com.changhong.telnettool.webinterface.been;

/**
 * 0 -- 未连接；1 -- 连接中；2 -- 连接成功；3 -- 用户名或密码错误 ； 4 -- 未知错误。
 */
public enum EnumPPPoeState {
    DISCONNECT(0, "未连接"),
    CONNECTING(1, "连接中"),
    CONNECTED(2, "连接成功"),
    ACCOUNT_OR_PASSWORD_ERROR(3, "用户名或密码错误"),
    UNKNOWN_ERROR(4, "未知错误");

    private int value;

    private String name;

    EnumPPPoeState(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}