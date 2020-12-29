package com.changhong.telnettool.function.cpu;

import com.sun.org.glassfish.gmbal.Description;
import com.sun.org.glassfish.gmbal.DescriptorFields;

import java.util.Date;

@Description("超阈值事件")
public class ExceedThresholdEvent {
    @Description("命令")
    String command;
    int PID;
    int PPID;
    @Description("用户")
    String USER;
    @Description("内存(kb)")
    int VSZ;
    @Description("内存(%)")
    float pMem;
    @Description("cpu序列")
    int cpuIndex;
    @Description("cpu占用(%)")
    float pCpu;
    /**
     * false：内存超阈值
     */
    @Description("事件")
    @DescriptorFields({"内存超阈值", "CPU超阈值"})
    boolean isCpuExceed;
    @Description("上电时间")
    private int uptime;
    @Description("系统时间")
    private Date sysTime;

    public ExceedThresholdEvent() {
    }

    public ExceedThresholdEvent(ProcessInfo process, long sysTime, int upTime, boolean isCpuExceed) {
        this.PID = process.PID;
        this.PPID = process.PPID;
        this.USER = process.USER;
        this.VSZ = process.VSZ;
        this.pMem = process.pMem;
        this.cpuIndex = process.cpuIndex;
        this.pCpu = process.pCpu;
        this.command = process.command;
        this.sysTime = new Date(sysTime);
        this.uptime = upTime;
        this.isCpuExceed = isCpuExceed;
    }

    @Override
    public String toString() {
        return "ExceedThresholdEvent{" +
                "command='" + command + '\'' +
                ", PID=" + PID +
                ", PPID=" + PPID +
                ", USER='" + USER + '\'' +
                ", VSZ=" + VSZ +
                ", pMem=" + pMem +
                ", cpuIndex=" + cpuIndex +
                ", pCpu=" + pCpu +
                ", isCpuExceed=" + isCpuExceed +
                ", uptime=" + uptime +
                ", sysTime=" + sysTime +
                '}';
    }
}