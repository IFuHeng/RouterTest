package com.changhong.telnettool.function.cpu;

import com.sun.org.glassfish.gmbal.Description;
import com.sun.org.glassfish.gmbal.DescriptorFields;

import java.util.Date;

@Description("进程上下线事件")
public class ProcessOnOfflineEvent {
    @Description("命令")
    private String command;
    private int PID;
    private int PPID;
    @Description("用户")
    private String USER;
    @Description("事件")
    @DescriptorFields({"离线", "上线"})
    private boolean isOnLine;
    @Description("上电时间")
    private int uptime;
    @Description("系统时间")
    private Date sysTime;

    public ProcessOnOfflineEvent() {
    }

    public ProcessOnOfflineEvent(ProcessInfo process, long time, int upTime, boolean isOnLine) {
        this.PID = process.PID;
        this.PPID = process.PPID;
        this.USER = process.USER;
        this.command = process.command;
        this.sysTime = new Date(time);
        this.uptime = upTime;
        this.isOnLine = isOnLine;
    }

    @Override
    public String toString() {
        return "ProcessOnOfflineEvent{" +
                "command='" + command + '\'' +
                ", PID=" + PID +
                ", PPID=" + PPID +
                ", USER='" + USER + '\'' +
                ", isOnLine=" + isOnLine +
                ", uptime=" + uptime +
                ", sysTime=" + sysTime +
                '}';
    }
}
