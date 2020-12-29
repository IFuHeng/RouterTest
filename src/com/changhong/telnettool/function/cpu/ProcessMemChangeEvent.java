package com.changhong.telnettool.function.cpu;

import com.sun.org.glassfish.gmbal.Description;

import java.util.Date;

@Description("进程内存变化事件")
public class ProcessMemChangeEvent {
    @Description("命令")
    private String command;
    private int PID;
    private int PPID;
    @Description("内存(kb)")
    int VSZ;
    @Description("内存(%)")
    float pMem;
    @Description("上电时间")
    private int uptime;
    @Description("系统时间")
    private Date sysTime;

    public ProcessMemChangeEvent() {
        this.sysTime = new Date();
    }

    public ProcessMemChangeEvent(ProcessInfo process, long sysTime, int uptime) {
        this.PID = process.PID;
        this.PPID = process.PPID;
        this.VSZ = process.VSZ;
        this.pMem = process.pMem;
        this.command = process.command;
        this.sysTime = new Date(sysTime);
        this.uptime = uptime;
    }

//    @Override
//    public String toString() {
//        return "ProcessMemChangeEvent{" +
//                "command='" + command + '\'' +
//                ", PID=" + PID +
//                ", PPID=" + PPID +
//                ", sysTime=" + sysTime +
//                ", uptime=" + uptime +
//                ", VSZ=" + VSZ +
//                ", pMem=" + pMem +
//                '}';
//    }
}
