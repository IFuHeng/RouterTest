package com.changhong.telnettool.task;

import com.changhong.telnettool.function.cpu.Callback;
import com.changhong.telnettool.net.TelnetClientHelper;
import com.sun.istack.internal.NotNull;

import java.awt.*;

public abstract class BaseTelnetReceiver<T> implements Runnable {
    private String IP;
    private int port = 23;
    private String user = "root";
    private String password;
    protected TelnetClientHelper telnetClientHelper;
    private Callback<T> callback;
    protected boolean isRunning;

    /**
     * 连接并登录
     *
     * @return
     */
    protected boolean connectAndLogin() {
        try {
            telnetClientHelper = new TelnetClientHelper("192.168.2.1", 23);
            telnetClientHelper.login(user, password);
        } catch (Exception e) {
            e.printStackTrace();
            Toolkit.getDefaultToolkit().beep();
            disconnect();
            return false;
        }

        if (callback != null)
            callback.onConnected();

        return true;
    }

    protected void disconnect() {
        if (telnetClientHelper != null) {
            telnetClientHelper.disconnect();
            if (callback != null)
                callback.onDisconnected();
        }
    }

    public BaseTelnetReceiver(@NotNull String IP, int port, @NotNull String user, String password, Callback<T> callback) {
        this.IP = IP;
        this.port = port;
        this.user = user;
        this.password = password;
        this.callback = callback;
    }

    protected void callback(T t) {
        if (callback != null)
            callback.callback(t);
    }

    protected void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        isRunning = false;
    }
}
