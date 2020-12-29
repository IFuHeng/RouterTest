package com.changhong.telnettool.dialog;

import com.changhong.telnettool.net.TelnetClientHelper;
import com.changhong.telnettool.tool.DataManager;
import com.changhong.telnettool.tool.Tool;
import com.changhong.telnettool.been.CommandBeen;
import com.sun.istack.internal.NotNull;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SpeedAndTemperatureDialog extends Dialog implements Runnable {

    private static final String PATH = "SpeedAndTemperature.dat";
    private final List<CommandBeen> mDataCommand;
    // 初始化telnet连接，并登录
    public TelnetClientHelper telnetManager;
    private Frame frame;


    public SpeedAndTemperatureDialog(@NotNull Frame frame, @NotNull TelnetClientHelper telnetManager) {
        super(frame);
        this.frame = frame;
        this.telnetManager = telnetManager;

        mDataCommand = load();
    }

    @Override
    public void run() {
        Toolkit.getDefaultToolkit().beep();
        System.out.println("----------------Start (" + Tool.getTime() + ")----------------\n");
//
//        //将即将运行的命令装入集合
//        List<CommandBeen> commandBeans = new ArrayList<>();
//        for (CommandBeen commandBeen : mDataCommand) {
//            if (commandBeen.isCheck)
//                commandBeans.add(commandBeen);
//        }
//
//        int step = 0;
//        // 启动按钮命令是 ACTION_PAUSE 并且 要运行的命令集合非空，执行循环
//        while (mBtnPlayOrPause.getActionCommand().equals(ACTION_PAUSE) && !commandBeans.isEmpty()) {
//            long costTime = System.currentTimeMillis();//每次循环消耗时间
//            Tool.log("----------------round---------------------");
//
//            CommandBeen commandBeen = commandBeans.get(step % commandBeans.size());
//            //当前命令所在位置
//            Component component = mViewListCommand.getComponent((mDataCommand.indexOf(commandBeen) + 1) * column + 2);
//            Color originalForegroundColor = component.getForeground();// 原始文字颜色
//            // 显示命令，背景设置蓝色
//            component.setForeground(Color.blue);
//
//            String cmd = commandBeen.command;
//            if (cmd.contains("\\n"))
//                cmd = cmd.replace("\\n", ";");
//            String temp = telnetManager.sendCommand(cmd);
//            try {
//                testWlanSpeed(temp);
//                testWlanTemperature(temp);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            mViewResponse.append(temp);
//            if (fileWriter != null)
//                try {
//                    fileWriter.write(Tool.getTime());
//                    fileWriter.write(':');
//                    fileWriter.write(' ');
//                    fileWriter.write(temp);
//                    fileWriter.write('\n');
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            if (!commandBeen.isUnlimited)
//                commandBeans.remove(commandBeen);
//            else
//                ++step;
//
//            costTime = System.currentTimeMillis() - costTime;
//            if (costTime < interval)
//                try {
//                    Thread.sleep(interval - costTime);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            //命令行所在view还原
//            component.setForeground(originalForegroundColor);
//        }
//
//        stopPlay();
//        //输出结束信息并关闭输出流
//        mViewResponse.append("----------------End----------------\n");
//        if (fileWriter != null)
//            try {
//                fileWriter.write("----------------End----------------\n");
//                fileWriter.write('\n');
//                fileWriter.flush();
//                fileWriter.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        telnetManager.disconnect();
//        Toolkit.getDefaultToolkit().beep();
    }

    public List<CommandBeen> load() {
        ArrayList<CommandBeen> result = new ArrayList<>();
        URL url = DataManager.class.getClassLoader().getResource(PATH);
        if (url == null)
            return null;
        BufferedReader fileReader = null;
        try {
            fileReader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = fileReader.readLine()) != null) {
                result.add(new CommandBeen(line));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
