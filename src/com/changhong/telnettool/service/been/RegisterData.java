package com.changhong.telnettool.service.been;

import com.sun.org.glassfish.gmbal.Description;

/**
 * 注册信息
 */
public class RegisterData {
    String MAC;
    String IP;
    @Description("客户端版本")
    String clientVer;
    @Description("上次登录时间")
    long time;

    public RegisterData(String MAC, String ip, String clientVer) {
        this.MAC = MAC;
        this.clientVer = clientVer;
        this.IP = ip;
        this.time = System.currentTimeMillis();
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return this.time;
    }
}