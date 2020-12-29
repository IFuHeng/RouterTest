package com.changhong.telnettool.function.cpu;

import com.sun.org.glassfish.gmbal.Description;

import java.util.regex.Pattern;

public class ProcessInfo {
    int PID;
    int PPID;
    @Description("用户")
    String USER;
    /*
        D      //无法中断的休眠状态（通常 IO 的进程）；
        R      //正在运行可中在队列中可过行的；
        S      //处于休眠状态；
        T      //停止或被追踪；
        W      //进入内存交换 （从内核2.6开始无效）；
        X      //死掉的进程 （基本很少见）；
        Z      //僵尸进程；
        <      //优先级高的进程
        N      //优先级较低的进程
        L      //有些页被锁进内存；
        s      //进程的领导者（在它之下有子进程）；
        l      //多线程，克隆线程（使用 CLONE_THREAD, 类似 NPTL pthreads）；
        +      //位于后台的进程组；
     */
    @Description("状态")
    String STAT;
    @Description("内存")
    int VSZ;
    @Description("内存(%)")
    float pMem;
    @Description("CPU序号")
    int cpuIndex;
    @Description("CPU(%)")
    float pCpu;
    @Description("命令")
    String command;

    //{"名称", "状态", "PID", "PPID", "CPU(%)", "内存(%)", "内存", "用户"};
    public ProcessInfo(String processStr) {
        int index = 0;//填写某项数据的序号

        int start = -1;
        for (int i = 0; i < processStr.length(); ++i) {
            char c = processStr.charAt(i);
            boolean isWhitespace = Character.isWhitespace(c);
            if (start == -1) {
                if (!isWhitespace)
                    start = i;
                continue;
            } else {
                if (isWhitespace) {
                    String temp = processStr.substring(start, i);
                    switch (index) {
                        case 0:
                            PID = parseInt(temp);
                            ++index;
                            break;
                        case 1:
                            PPID = parseInt(temp);
                            ++index;
                            break;
                        case 2:
                            USER = temp;
                            ++index;
                            break;
                        case 3:
                            if (STAT == null) {
                                STAT = temp;
                                break;
                            } else if (Pattern.compile("[^0-9]").matcher(temp).matches()) {
                                STAT += temp;
                                break;
                            } else {
                                ++index;
                            }
                        case 4:
                            VSZ = parseInt(temp);
                            ++index;
                            break;
                        case 5:
                            pMem = parseFloat(temp);
                            ++index;
                            break;
                        case 6:
                            cpuIndex = parseInt(temp);
                            ++index;
                            break;
                        case 7:
                            pCpu = parseFloat(temp);
                            command = processStr.substring(i).trim();
                            return;
                    }
                    start = -1;
                }
            }
        }
    }

    private int parseInt(String str) {
        int result = 0;
        if (str != null && str.trim().length() > 0)
            try {
                result = Integer.parseInt(str);
            } catch (NumberFormatException e) {
                System.err.println(e.getMessage());
                if (Pattern.matches("[0-9.]*", str))
                    result = (int) parseFloat(str);
            }

        return result;
    }

    private float parseFloat(String str) {
        float result = 0;
        if (str != null && str.trim().length() > 0)
            try {
                result = Float.parseFloat(str);
            } catch (NumberFormatException e) {
                System.err.println(e.getMessage());
            }

        return result;
    }


    public int getPID() {
        return PID;
    }

    public int getPPID() {
        return PPID;
    }

    public String getUSER() {
        return USER;
    }

    public String getSTAT() {
        return STAT;
    }

    public int getVSZ() {
        return VSZ;
    }

    public float getpMem() {
        return pMem;
    }

    public int getCpuIndex() {
        return cpuIndex;
    }

    public float getpCpu() {
        return pCpu;
    }

    public String getCommand() {
        return command;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (obj instanceof ProcessInfo) {
            ProcessInfo o = (ProcessInfo) obj;
            boolean result = PID == o.PID && PPID == o.PPID;
//            if (result) {
//                System.out.println(getClass().getSimpleName() + " :  equals(){" + o.command + " vs " + command);
//            }
            return result;
        }


        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int h = 0;
        h = 31 * h + PID;
        h = 31 * h + PPID;
//        System.out.println(getClass().getSimpleName() + " :  hashCode() = " + h);
        return h;
    }

    @Override
    public String toString() {
        return "ProcessInfo{" +
                "PID=" + PID +
                ", PPID=" + PPID +
                ", USER='" + USER + '\'' +
                ", STAT='" + STAT + '\'' +
                ", VSZ=" + VSZ +
                ", pMem=" + pMem +
                ", cpuIndex=" + cpuIndex +
                ", pCpu=" + pCpu +
                ", command='" + command + '\'' +
                '}';
    }
}