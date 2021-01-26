package com.changhong.telnettool.function.other;

import com.changhong.telnettool.event.MyWindowAdapter;
import com.changhong.telnettool.event.PositiveNumberTextLimitListener;
import com.changhong.telnettool.event.TextInputLengthLimitListener;
import com.changhong.telnettool.net.TelnetClientHelper;
import com.changhong.telnettool.tool.DataManager;
import com.changhong.telnettool.tool.Tool;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 自动配置一个静态IP地址：192.168.58.XX 网段，子网掩码 3个255， 成功后，telnet 到 58.1，  执行 flash reset 后，检测是否执行成功，获取  DEVICE_MODE 是否为0，0表示成功，然后发 reboot命令
 */
public class ResetExam extends JFrame implements ActionListener, Runnable {

    private static final String TITLE_FRAME = "小工具测试。静态IP reset";
    private static final String CMD_FLASH_RESET = "flash reset";
    private static final String CMD_GET_MODE = "flash get DEVICE_MODE";

    private static final String ACTION_PLAY = "重置Flash";
    private static final String ACTION_PAUSE = "停止";

    private static final int DEFAULT_INTERVAL = 100;

    //    private final JPanel mPanelTableProcess;
    private final JButton mBtnPlayOrPause;
    private final TextField mViewPort;
    private final TextField mViewIP;
    private final TextField mViewAccount;
    private final JPasswordField mViewPassword;
    private final JTextArea mTvResponse;

    /************* value *********************/
    private Thread mThread;

    public ResetExam() {
        super(TITLE_FRAME);

        Pair<String, Integer> version = DataManager.getVersionInfo();
        if (version != null)
            setTitle(TITLE_FRAME + " v" + version.getKey());

        setSize(800, 600);

        setLayout(new BorderLayout());
        {
            JPanel panel2 = new JPanel();
            panel2.setLayout(new BorderLayout());
            {// ip port play in north
                mViewIP = new TextField(Tool.getGuessGateway());
                mViewIP.setColumns(15);
                mViewIP.addTextListener(new TextInputLengthLimitListener(15));
                mViewPort = new TextField("23");
                mViewPort.setColumns(5);
                mViewPort.addTextListener(new TextInputLengthLimitListener(5));
                mViewPort.addTextListener(new PositiveNumberTextLimitListener());

                mBtnPlayOrPause = new JButton(ACTION_PLAY);
                mBtnPlayOrPause.setActionCommand(ACTION_PLAY);
                mBtnPlayOrPause.addActionListener(this);
                JPanel panel = new JPanel();
                panel.setBorder(BorderFactory.createTitledBorder("服务地址和端口设置"));
                panel.add(new Label("IP:"));
                panel.add(mViewIP);
                panel.add(new Label("  port:"));
                panel.add(mViewPort);

                panel.add(mBtnPlayOrPause);
                panel.setMinimumSize(new Dimension(400, 300));
                panel2.add(panel, "North");
            }
            {// ip port play in north
                mViewAccount = new TextField("root");
                mViewAccount.setColumns(15);
                mViewAccount.addTextListener(new TextInputLengthLimitListener(25));
                mViewPassword = new JPasswordField("admin2020@ch");
                mViewPassword.setColumns(10);

                JPanel panel = new JPanel();
                panel.setBorder(BorderFactory.createTitledBorder("用户名和密码"));
                panel.add(new Label("User:"));
                panel.add(mViewAccount);
                panel.add(new Label("  password:"));
                panel.add(mViewPassword);

                panel2.add(panel, "Center");
            }

            this.add(panel2, "North");
        }
        {
            mTvResponse = new JTextArea();
            mTvResponse.setRows(8);
            JScrollPane jScrollPane = new JScrollPane(mTvResponse);
            this.add(jScrollPane, BorderLayout.CENTER);
        }

        addWindowListener(new MyWindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stopPlay();
                dispose();
                Tool.log(e);
                if (mThread != null && !mThread.isAlive()) {
                    mThread.interrupt();
                }

                super.windowClosing(e);
            }
        });
        setResizable(true);
        setVisible(true);
        pack();
        setLocationRelativeTo(null);
    }

    private void startPlay() {

        mBtnPlayOrPause.setText(ACTION_PAUSE);
        mBtnPlayOrPause.setActionCommand(ACTION_PAUSE);

        mViewIP.setEnabled(false);
        mViewPort.setEnabled(false);
        if (mThread != null && !mThread.isAlive()) {
            mThread.interrupt();
        }
        mThread = new Thread(this);
        mThread.start();
    }

    private void stopPlay() {
        mBtnPlayOrPause.setText(ACTION_PLAY);
        mBtnPlayOrPause.setActionCommand(ACTION_PLAY);

        mViewIP.setEnabled(true);
        mViewPort.setEnabled(true);
        if (mThread != null && !mThread.isAlive()) {
            mThread.interrupt();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Tool.log(e.getActionCommand());

        // play or pause
        if (e.getActionCommand().equals(ACTION_PLAY)) {
            if (!Tool.isIpv4(mViewIP.getText())) {
                mViewIP.requestFocus();
                return;
            } else if (mViewPort.getText().isEmpty()) {
                mViewPort.requestFocus();
                return;
            }
            startPlay();
        } else if (e.getActionCommand().equals(ACTION_PAUSE)) {
            stopPlay();
        }
    }

    @Override
    public void run() {
        String IP = mViewIP.getText();
        String USER = mViewAccount.getText();
        System.out.println(USER);
        String PASSWORD = new String(mViewPassword.getPassword());
        System.out.println(PASSWORD);
        int port = Tool.turnString2Int(mViewPort.getText(), 23);
        int interval = DEFAULT_INTERVAL;//每次请求间隔 <=0.1s

        //如果未设置输出文件路径，将不初始化输出字符流

        mTvResponse.setText(null);
        //输出开头信息
        Toolkit.getDefaultToolkit().beep();
        Tool.log("repared");
        // 初始化telnet连接，并登录
        TelnetClientHelper telnetManager;
        try {
            telnetManager = new TelnetClientHelper(IP, port);
            String info = telnetManager.login(USER, PASSWORD);
            Tool.log("login: " + info);
            mTvResponse.append(info);
            mTvResponse.append("\n");

            int originalDeviceMode = getDeviceMode(telnetManager);
            mTvResponse.append("原始DEVICE_MODE = " + originalDeviceMode);
            mTvResponse.append("\n");
        } catch (Exception e) {
            e.printStackTrace();
            // 初始化telnet失败，输出结束信息，并关闭输出流，返回
            stopPlay();
            mTvResponse.append("登录Telnet失败： " + e.getMessage());
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        try {
            String resetFlashResult = telnetManager.sendCommand(CMD_FLASH_RESET);
            mTvResponse.append("启动flash reset： \n" + resetFlashResult);
            mTvResponse.append("\n");
            Tool.log("flash reset = " + resetFlashResult);
        } catch (Exception e) {
            e.printStackTrace();
            stopPlay();
            mTvResponse.append("启动flash reset失败： " + e.getMessage());
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        mTvResponse.append("flash reset完成 \n\n\n");
        // 启动按钮命令是 ACTION_PAUSE 并且 要运行的命令集合非空，执行循环
        while (mBtnPlayOrPause.getActionCommand().equals(ACTION_PAUSE)) {
            long costTime = System.currentTimeMillis();//每次循环消耗时间
            Tool.log("----------------round---------------------");
            int temp = -1;
            try {
                temp = getDeviceMode(telnetManager);
            } catch (Exception e) {
                e.printStackTrace();
                mTvResponse.append("登录Telnet失败： " + e.getMessage());
                break;
            }
            Tool.log("Flash DEVICE_MODE = " + temp);
            mTvResponse.append("Flash DEVICE_MODE=" + temp);
            mTvResponse.append("\n");

            if (temp == 0) {
                mTvResponse.append("Flash reset完成\n");
                mTvResponse.append("启动reboot");
                try {
                    Tool.log(telnetManager.sendCommand("reboot"));
                } catch (Exception e) {
                    e.printStackTrace();
                    mTvResponse.append("reboot 失败： " + e.getMessage());
                }
                break;
            }
            costTime = System.currentTimeMillis() - costTime;
            if (costTime < interval)
                try {
                    Thread.sleep(interval - costTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
        stopPlay();
        //输出结束信息并关闭输出流

        telnetManager.disconnect();
        Toolkit.getDefaultToolkit().beep();
    }

    private int getDeviceMode(TelnetClientHelper telnetManager) throws Exception {
        String temp = telnetManager.sendCommand(CMD_GET_MODE);
        temp = temp.substring(temp.indexOf('=') + 1);
        return Integer.parseInt(temp);
    }

}