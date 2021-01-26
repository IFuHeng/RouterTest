package com.changhong.telnettool.function.cpu;

import com.changhong.telnettool.database.SQLiteJDBC;
import com.changhong.telnettool.net.TelnetClientHelper;
import com.changhong.telnettool.tool.Tool;
import javafx.util.Pair;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/***
 * 运行cpu和内存检测，并监控进程情况
 * @author fuheng
 *
 */
public class CpuMemProcessFunction extends Observable implements Runnable {

    /********序号*******/
    private static final String CMD_TOP = "top -b -n 1";
    private static final String CMD_CPUINFO = "cat /proc/cpuinfo";
    private static final String CMD_UPTIME2 = "cat /proc/uptime";

    /************* value *********************/
    private SimpleDateFormat mSdf;
    private Thread mThread;
    private boolean isRunning;

    private TopInfo mTmpTopInfo;
    private MissionDataBeen mData;
    private EventOfFunction state = EventOfFunction.STOPED;

    private static CpuMemProcessFunction sInstance;

    public static CpuMemProcessFunction getInstance() {
//        System.out.println("getInstance run in --->  " + Thread.currentThread().getName());
        if (sInstance == null)
            synchronized (CMD_TOP) {
                if (sInstance == null)
                    sInstance = new CpuMemProcessFunction();
            }
        return sInstance;
    }

    private CpuMemProcessFunction() {
    }

    public synchronized void setData(MissionDataBeen data) {
        this.mData = data;
    }

    public MissionDataBeen getData() {
        return mData;
    }

    /**
     * 获取当前运行情况数据对象
     */
    public TopInfo getTopInfo() {
        return mTmpTopInfo;
    }

    public EventOfFunction getState() {
        return state;
    }

    public void startPlay() {
        //启动线程
        if (mThread != null && !mThread.isInterrupted()) {
            isRunning = false;
            mThread.interrupt();
            while (mThread.isAlive()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        //启动线程
        mThread = new Thread(this);
        mThread.start();
    }

    public void stopPlay() {
        isRunning = false;
        if (mThread != null && !mThread.isInterrupted()) {
            mThread.interrupt();
            mThread = null;
        }
    }

    @Override
    public void run() {
        if (mData == null) {
            callback(EventOfFunction.ERROR, "Data is null");
            return;
        }
        isRunning = true;
        final long startTime = System.currentTimeMillis();//程序启动计时时间

        SQLiteJDBC databaseOffline = null;
        SQLiteJDBC databaseExceed = null;
        SQLiteJDBC databaseMemChange = null;

        state = EventOfFunction.STARTED;
        callback(state, null);

        long lastRunTime = mData.getInterval();//计算上次运行top的时间，运行后，被赋值为0，等待值超过 refreshInterval
        //输出开头信息
        Tool.log("repared");
        // 初始化telnet连接，并登录
        TelnetClientHelper telnetManager = null;
        try {
            telnetManager = new TelnetClientHelper(mData.getIp(), mData.getTelnet_port());
            Tool.log("login: " + telnetManager.login(mData.getTelnet_user(), mData.getTelnet_password()));
        } catch (Exception e) {
            e.printStackTrace();
            callback(EventOfFunction.ERROR, e.getLocalizedMessage());
            // 初始化telnet失败，输出结束信息，并关闭输出流，返回
            isRunning = false;
        }

        final long uptime;//获取上电时间（毫秒）

        if (isRunning) {
//        long uptime = getUpTime(telnetManager);//获取上电时间（分）
//        String cpuinfo = telnetManager.sendCommand(CMD_CPUINFO);
//        Tool.log("cpu = " + cpuinfo);
            uptime = getUpTime2(telnetManager);
            callback(EventOfFunction.REFRESH_TIME, turnLong2Time(uptime));
            databaseOffline = new SQLiteJDBC(mData.dbOutPath, ProcessOnOfflineEvent.class);
            databaseExceed = new SQLiteJDBC(mData.dbOutPath, ExceedThresholdEvent.class);
            databaseMemChange = new SQLiteJDBC(mData.dbOutPath, ProcessMemChangeEvent.class);
        } else
            uptime = 0;
        while (isRunning) {
            long roundStartTime = System.currentTimeMillis();//每次循环启动时间
            long rUptime = roundStartTime - startTime + uptime;//上电时间
            //上电时间
            String strUptime = turnLong2Time(rUptime);
            callback(EventOfFunction.REFRESH_TIME, strUptime);

            if (isRunning && lastRunTime >= mData.getInterval()) {
                System.out.println(getClass().getSimpleName() + " , ----------------round  " + strUptime + "(上电时间)  " + getCurrentTime() + "(系统时间) ---------------------");
                lastRunTime = 0;
                //读取top信息
                String temp;
                try {
                    temp = telnetManager.sendCommand(CMD_TOP);
                } catch (Exception e) {
                    e.printStackTrace();
                    callback(EventOfFunction.ERROR, "读取失败\n    失败时间（" + DateFormat.getTimeInstance().format(new Date()) + "）：" + e.getMessage());
                    break;
                }
                //新的top信息
                TopInfo info = new TopInfo(temp);
                if (info.getArrProcess() != null) {
                    for (int i = 0; i < info.getArrProcess().size(); i++) {
                        // 去除当前top命令
                        ProcessInfo process = info.getArrProcess().get(i);
                        if (CMD_TOP.equals(process.getCommand()))
                            info.getArrProcess().remove(i--);
                    }
                }

                //对比和存储
                compareProgress(databaseOffline, databaseExceed, databaseMemChange,
                        info, mData.memThreshold, mData.cpuThreshold, (int) rUptime);

                //刷新列表
                mTmpTopInfo = info;

                //刷新cpu和内存占用百分比图
                callback(EventOfFunction.REFRESH_DATA, info);
                Tool.log("cpu info= " + info.getCpuUseInfo());
            }

            if (isRunning)
                try {
                    long cost = System.currentTimeMillis() - roundStartTime;
                    if (cost < 1000)
                        Thread.sleep(1000 - cost);

                    lastRunTime += System.currentTimeMillis() - roundStartTime;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }

        state = EventOfFunction.STOPED;
        telnetManager.disconnect();
        callback(state, null);
    }

    private <T> void callback(EventOfFunction event, T t) {
        setChanged();
        notifyObservers(new Pair(event, t));
        clearChanged();
    }

    private void compareProgress(SQLiteJDBC databaseOffline, SQLiteJDBC databaseExceed, SQLiteJDBC databaseMemChange,
                                 TopInfo info, int memThreshold, int cpuThreshold, int rUptime) {
        if (mData.mSetMonitorCmd == null || mData.mSetMonitorCmd.isEmpty())
            return;

        long time = System.currentTimeMillis();//getCurrentTime();
        //监控某条进程内存变化
        HashMap<String, ProcessInfo> mapNewProcesses = new HashMap<>();

        //存储上线和超过阈值
        for (ProcessInfo arrProcess : info.getArrProcess()) {
            if (!mData.mSetMonitorCmd.contains(arrProcess.command))// 未监控的命令
                continue;

            if (arrProcess.getpMem() > memThreshold) {
                ExceedThresholdEvent event = new ExceedThresholdEvent(arrProcess, time, rUptime, false);
                databaseExceed.insert(event);
                callback(EventOfFunction.REFRESH_CHANGE, event);
            }
            if (arrProcess.getpCpu() > cpuThreshold) {
                ExceedThresholdEvent event = new ExceedThresholdEvent(arrProcess, time, rUptime, true);
                databaseExceed.insert(event);
                callback(EventOfFunction.REFRESH_CHANGE, event);
            }

            mapNewProcesses.put(arrProcess.getCommand(), arrProcess);
        }

        if (mTmpTopInfo == null) {
            return;
        }

        //监控某项进程内存变化
        if (!mapNewProcesses.isEmpty())
            for (ProcessInfo process : mTmpTopInfo.getArrProcess()) {
                String cmd = process.getCommand();
                if (mData.mSetMonitorCmd.contains(cmd) && mapNewProcesses.containsKey(cmd)) {
                    if (mapNewProcesses.get(cmd).getVSZ() != process.getVSZ()) {
                        ProcessMemChangeEvent event = new ProcessMemChangeEvent(mapNewProcesses.get(cmd), time, rUptime);
                        databaseMemChange.insert(event);
                        callback(EventOfFunction.REFRESH_CHANGE, event);
                    }
                    break;
                }
            }

        //记录新增进程
        ArrayList<ProcessInfo> arrOnOffline = new ArrayList<>();
        arrOnOffline.addAll(info.getArrProcess());
        boolean diffSet = arrOnOffline.removeAll(mTmpTopInfo.getArrProcess());
        if (diffSet) {
            for (int i = 0; i < arrOnOffline.size(); i++) {//排除掉当前Top命令
                if (mData.mSetMonitorCmd.contains(arrOnOffline.get(i).getCommand()))//当上线进程处于被监控列表时，才写入数据库
                {
                    ProcessOnOfflineEvent event = new ProcessOnOfflineEvent(arrOnOffline.get(i), time, rUptime, true);
                    databaseOffline.insert(event);
                    callback(EventOfFunction.REFRESH_CHANGE, event);
                }
            }
        }

        // 记录下线进程
        arrOnOffline.clear();
        arrOnOffline.addAll(mTmpTopInfo.getArrProcess());
        diffSet = arrOnOffline.removeAll(info.getArrProcess());
        if (diffSet) {
            for (int i = 0; i < arrOnOffline.size(); i++) {
                if (mData.mSetMonitorCmd.contains(arrOnOffline.get(i).getCommand()))//当下线进程处于被监控列表时，才写入数据库
                {
                    ProcessOnOfflineEvent event = new ProcessOnOfflineEvent(arrOnOffline.get(i), time, rUptime, false);
                    databaseOffline.insert(event);
                    callback(EventOfFunction.REFRESH_CHANGE, event);
                }
            }
        }
    }

    private String getCurrentTime() {
        if (mSdf == null)
            mSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return mSdf.format(new Date(System.currentTimeMillis()));
    }

    private long getUpTime2(TelnetClientHelper telnetManager) {
        long result = 0;
        try {
            String temp = telnetManager.sendCommand(CMD_UPTIME2).trim();
            int index;
            if ((index = temp.indexOf(' ')) != 0)
                temp = temp.substring(0, index);
            return Math.round(Float.parseFloat(temp) * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String turnLong2Time(long time) {
        int ms = (int) (time % 1000);
        int s = (int) (time / 1000);
        int m = s / 60;
        int h = m / 60;
        int day = h / 24;
        h %= 24;
        m %= 60;
        s %= 60;

        StringBuilder sb = new StringBuilder();
        if (day > 0)
            sb.append(day).append("天 ");
        if (h > 0)
            sb.append(h).append(':');

        sb.append(String.format("%02d:%02d", m, s));
        return sb.toString();
    }

    public static void main(String[] args) {
//        getInstance().addObserver(new Observer() {
//            @Override
//            public void update(Observable o, Object arg) {
//                System.out.println("callback    =======>    " + arg);
//            }
//        });
        getInstance().setData(
                new MissionDataBeen("192.168.2.1", "root", "admin2020@ch",
                        23, 10000, 1, 1,
                        "resource.db", null));
        getInstance().startPlay();

        CpuToolMainUI cpuToolMainUI = new CpuToolMainUI();

    }
}