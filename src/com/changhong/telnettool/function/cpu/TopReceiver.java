package com.changhong.telnettool.function.cpu;

import com.changhong.telnettool.task.BaseTelnetReceiver;
import com.changhong.telnettool.tool.Tool;

public class TopReceiver extends BaseTelnetReceiver<TopInfo> {

    private static final String CMD_TOP = "top -b -n 1";

    private static final int INTERVAL = 2000;//每次请求间隔 <=1s

    public TopReceiver(String IP, int port, String user, String password, Callback<TopInfo> callback) {
        super(IP, port, user, password, callback);
    }

    @Override
    public void run() {
        if (!super.connectAndLogin())
            return;

        isRunning = true;
        while (isRunning) {
            Tool.log("----------------round---------------------");
            long costTime = System.currentTimeMillis();//每次循环消耗时间

            String top = telnetClientHelper.sendCommand(CMD_TOP);
            callback(new TopInfo(top));

            costTime = System.currentTimeMillis() - costTime;
            if (costTime < INTERVAL)
                try {
                    Thread.sleep(INTERVAL - costTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }

        super.disconnect();
    }
}
