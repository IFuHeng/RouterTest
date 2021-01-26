package com.changhong.telnettool.function.cpu;

import java.util.HashSet;

public class MissionDataBeen {
    static final int MIN_INTERVAL = 1000;
    String ip;
    String telnet_user;
    String telnet_password;
    int telnet_port = 23;
    int interval = MIN_INTERVAL;
    int memThreshold = 1;//内存阈值
    int cpuThreshold = 1;//cpu阈值
    String dbOutPath;//数据库路径

    HashSet<String> mSetMonitorCmd;

    public MissionDataBeen() {
    }

    public MissionDataBeen(String ip, String telnet_user, String telnet_password,
                           int telnet_port, int interval, int memThreshold,
                           int cpuThreshold, String dbOutPath, HashSet<String> mSetMonitorCmd) {
        this.ip = ip;
        this.telnet_user = telnet_user;
        this.telnet_password = telnet_password;
        this.telnet_port = telnet_port;
        this.interval = interval;
        this.memThreshold = memThreshold;
        this.cpuThreshold = cpuThreshold;
        this.dbOutPath = dbOutPath;
        this.mSetMonitorCmd = mSetMonitorCmd;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTelnet_user() {
        return telnet_user;
    }

    public void setTelnet_user(String telnet_user) {
        this.telnet_user = telnet_user;
    }

    public String getTelnet_password() {
        return telnet_password;
    }

    public void setTelnet_password(String telnet_password) {
        this.telnet_password = telnet_password;
    }

    public int getTelnet_port() {
        return telnet_port;
    }

    public void setTelnet_port(int telnet_port) {
        this.telnet_port = telnet_port;
    }

    public int getInterval() {
        if (interval < MIN_INTERVAL)
            return MIN_INTERVAL;
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getMemThreshold() {
        if (memThreshold < 1)
            return 1;
        return memThreshold;
    }

    public void setMemThreshold(int memThreshold) {
        this.memThreshold = memThreshold;
    }

    public int getCpuThreshold() {
        if (cpuThreshold < 1)
            return 1;
        return cpuThreshold;
    }

    public void setCpuThreshold(int cpuThreshold) {
        this.cpuThreshold = cpuThreshold;
    }

    public String getDbOutPath() {
        return dbOutPath;
    }

    public void setDbOutPath(String dbOutPath) {
        this.dbOutPath = dbOutPath;
    }

    public HashSet<String> getSetMonitorCmd() {
        return mSetMonitorCmd;
    }

    public void setSetMonitorCmd(HashSet<String> setMonitorCmd) {
        this.mSetMonitorCmd = setMonitorCmd;
    }
}
