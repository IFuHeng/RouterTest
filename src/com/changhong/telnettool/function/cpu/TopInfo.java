package com.changhong.telnettool.function.cpu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * 解析 linux下top命令内容
 */
public class TopInfo {


    /*
        Mem: 78256K used, 28064K free, 0K shrd, 3104K buff, 13952K cached
        CPU:  0.0% usr  0.0% sys  0.0% nic 90.9% idle  0.0% io  0.0% irq  9.0% sirq
        Load average: 1.36 1.34 1.32 1/60 3619
          PID  PPID USER     STAT   VSZ %MEM CPU %CPU COMMAND
        10106     1 root     S    20176 18.9   0  0.9 AC /etc/capwap
         9088     1 root     S    10864 10.2   0  0.2 chnm
         2777     1 root     S     9536  8.9   0  0.0 chdeviceservice /web
         9068     1 root     S     7920  7.4   0  0.0 WTP /etc/capwap
        11757     1 root     S     7648  7.1   0  0.0 ch_cloud_deamon_auth https
         9056     1 root     S     1440  1.3   0  0.0 timelycheck
         1378     1 root     S     1232  1.1   0  0.0 crond
        31780  9086 root     S     1232  1.1   0  0.0 -sh
         1383     1 root     S     1232  1.1   0  0.0 -/bin/sh
         9090     1 root     S     1216  1.1   0  0.0 /bin/sh /bin/daemon.sh
            1     0 root     S     1216  1.1   0  0.0 init
         9086     1 root     S     1216  1.1   0  0.0 telnetd
         3607  3606 root     S     1216  1.1   0  0.0 ping 223.5.5.5 -w 3
         3619 31780 root     R     1216  1.1   0  0.0 top
         3606  9088 root     S     1216  1.1   0  0.0 sh -c ping 223.5.5.5 -w 3 | grep "packets received"
         3608  3606 root     S     1216  1.1   0  0.0 grep packets received
         3617  9090 root     S     1200  1.1   0  0.0 sleep 60
         8811     1 root     S     1184  1.1   0  0.0 wscd -start -c /var/wsc-wlan0-wlan1.conf -w wlan0 -w2 wlan1 -fi /var/wscd-wlan0.fifo -fi2 /var/wscd-wlan1.fifo -daemon
         9039     1 root     S      896  0.8   0  0.0 lld2d br0
        11622     1 root     S      864  0.8   0  0.0 ntp_inet -x ntp.ntsc.ac.cn
         9186     1 root     S      832  0.7   0  0.0 dnrd --cache=off -s 192.168.3.1
         8793     1 root     S      832  0.7   0  0.0 udhcpd /var/udhcpd.conf
        11631     1 root     S      816  0.7   0  0.0 /bin/igmpproxy eth1.100 br0 -D
         9173     1 root     S      816  0.7   0  0.0 udhcpc -i eth1.100 -p /etc/udhcpc/udhcpc-eth1.100.pid -s /usr/share/udhcpc/eth1.sh -h UNDEFINED_74D0F9 -a 5
         8814     1 root     S      816  0.7   0  0.0 iwcontrol wlan0 wlan1 wlan1-va1
         8804     1 root     S      800  0.7   0  0.0 pathsel -i wlan-msh -P -d
        14858     1 root     S      784  0.7   0  0.0 ther_control
         1319     1 root     S <    784  0.7   0  0.0 watchdog 1000
         9037     1 root     S      784  0.7   0  0.0 reload -k /var/wlsch.conf
         8800     1 root     S      784  0.7   0  0.0 iapp br0 wlan0 wlan1 wlan1-va1
         9052     1 root     S      256  0.2   0  0.0 fwd
            3     2 root     SW<      0  0.0   0  0.0 [ksoftirqd/0]
           88     2 root     SW       0  0.0   0  0.0 [spi0]
            4     2 root     SW       0  0.0   0  0.0 [kworker/0:0]
            6     2 root     SW       0  0.0   0  0.0 [kworker/u2:0]
          716     2 root     SW       0  0.0   0  0.0 [mtdblock0]
          721     2 root     SW       0  0.0   0  0.0 [mtdblock1]
          726     2 root     SW       0  0.0   0  0.0 [mtdblock2]
         1035     2 root     SW       0  0.0   0  0.0 [kworker/u2:2]
          110     2 root     SW       0  0.0   0  0.0 [kswapd0]
           85     2 root     SW<      0  0.0   0  0.0 [kblockd]
            2     0 root     SW       0  0.0   0  0.0 [kthreadd]
          105     2 root     SW       0  0.0   0  0.0 [kworker/0:1]
          813     2 root     SW<      0  0.0   0  0.0 [deferwq]
            5     2 root     SW<      0  0.0   0  0.0 [kworker/0:0H]
         1315     2 root     SW<      0  0.0   0  0.0 [kworker/0:1H]
            7     2 root     SW<      0  0.0   0  0.0 [khelper]
           79     2 root     SW<      0  0.0   0  0.0 [writeback]
           82     2 root     SW<      0  0.0   0  0.0 [bioset]
           83     2 root     SW<      0  0.0   0  0.0 [crypto]
    */

    /**
     * CPU:  0.0% usr  0.0% sys  0.0% nic 90.9% idle  0.0% io  0.0% irq  9.0% sirq
     * usr：表示用户空间程序的cpu使用率（没有通过nice调度）
     * sys：表示系统空间的cpu使用率，主要是内核程序。
     * nic：表示用户空间且通过nice调度过的程序的cpu使用率。
     * idle：空闲cpu
     * io：cpu运行时在等待io的时间
     * irq：cpu处理硬中断的数量
     * sirq：cpu处理软中断的数量
     */
    private HashMap<String, String> cpuUseInfo;

    private MemUseInfo memUseInfo;

    private ArrayList<ProcessInfo> arrProcess = new ArrayList<>();

    /**
     * @param infoString 解析范例: CPU:  0.0% usr  0.0% sys  0.0% nic 90.9% idle  0.0% io  0.0% irq  9.0% sirq
     */
    private void loadCpuUseInfo(String infoString) {
        cpuUseInfo = new HashMap<>();
        String info = infoString;
        if (infoString.indexOf(':') != -1)
            info = infoString.substring(infoString.indexOf(':') + 1).trim();

        String[] tempArr = Utils.split(info);
        for (int i = 0; i < tempArr.length; i += 2) {
            cpuUseInfo.put(tempArr[i + 1], tempArr[i]);
        }
    }

    float getCpuUsed() {
        if (cpuUseInfo != null && cpuUseInfo.containsKey("idle")) {
            String value = cpuUseInfo.get("idle");
            float result = Float.parseFloat(Pattern.compile("[^0-9.]").matcher(value).replaceAll(""));
            result = 100 - result;
            result /= 100;
            return result;
        }
        return 0;
    }

    public TopInfo(String topStr) {
        BufferedReader bufferedReader = new BufferedReader(new StringReader(topStr));
        try {
            String line;
            int row = -1;
            while ((line = bufferedReader.readLine()) != null) {
                ++row;
                if (row == 0)//line.contains("Mem:"))
                    memUseInfo = new MemUseInfo(line);
                else if (row == 1) //(line.contains("CPU:"))
                    loadCpuUseInfo(line);
                else if (row > 3) {
                    ProcessInfo info = new ProcessInfo(line);
                    arrProcess.add(info);
                }
            }
        } catch (IOException e) {

        }
    }

    public HashMap<String, String> getCpuUseInfo() {
        return cpuUseInfo;
    }

    public MemUseInfo getMemUseInfo() {
        return memUseInfo;
    }

    public ArrayList<ProcessInfo> getArrProcess() {
        return arrProcess;
    }

    @Override
    public String toString() {
        return "TopInfo{" +
                "cpuUseInfo=" + cpuUseInfo +
                ", memUseInfo=" + memUseInfo +
                ", arrProcess=" + arrProcess +
                '}';
    }
}
