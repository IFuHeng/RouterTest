package com.changhong.telnettool.function.test_exam;

import com.changhong.telnettool.dialog.AlertDialog;
import com.changhong.telnettool.event.MyWindowAdapter;
import com.changhong.telnettool.event.TextInputLengthLimitListener;
import com.changhong.telnettool.task.DosCmdHelper;
import com.changhong.telnettool.tool.Tool;
import com.changhong.telnettool.webinterface.BWR510LocalConnectionHelper;
import com.changhong.telnettool.webinterface.been.StaInfo;
import com.changhong.telnettool.webinterface.been.wan.WanRequireAllBeen;
import com.changhong.telnettool.webinterface.been.wan.WanResponseAllBeen;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 2.2.2~2.2.4. wan口测试
 */
public class TestWan extends JFrame implements Runnable, ActionListener, ItemListener {

    private static final String[] TITLE_PROCESS = {"名称", "MAC", "IP", "连接\n方式", "在线时间", "上行", "下行"};
    private static final String ACTION_PAUSE = "停止";
    private static final String ACTION_PLAY = "启动";
    private static final String ACTION_CONNECT = "连接";
    private static final String ACTION_TEST = "测试";
    private static final String ACTION_SET = "设置";
    private static final String TITLE_FRAME = "2.2.2~2.2.4 wan口测试";

    private List<StaInfo> mArrStaInfo = new ArrayList<>();
    private JButton mBtnConnect;
    private TextField mViewIP;
    private JTable table;
    private JPasswordField mTvRouterPwd;
    ExecutorService mThreadPool;
    private JTextField mViewPing;
    private JButton mBtnTest;
    private JTextArea mViewPingResult;
    private JTextField mViewPppoeAccount;
    private JPasswordField mViewPppoePassword;
    private JButton mBtnSet;

    private JTextField mViewStaticIp;
    private JTextField mViewStaticMask;
    private JTextField mViewStaticGateway;
    private JTextField mViewStaticDNS1;
    private JTextField mViewStaticDNS2;
    private CardLayout cardlayout;
    private JPanel cardPanel;

    private CheckboxGroup mCheckboxGroupWanType;
    private Checkbox mCbPppoe;
    private Checkbox mCbStatic;
    private Checkbox mCbDhcp;
    private JPanel mPanelWan;

    private WanResponseAllBeen mWanInfo;

    public TestWan() {
        super(TITLE_FRAME);
        mThreadPool = Executors.newFixedThreadPool(3);
        createUi();
    }

    @Override
    public void run() {
        while (mBtnConnect.getActionCommand().equals(ACTION_PAUSE)) {
            try {
                List<StaInfo> tempArr = BWR510LocalConnectionHelper.getInstance().getStaInfo(mViewIP.getText());
                mArrStaInfo.clear();
                mArrStaInfo.addAll(tempArr);
                ((AbstractTableModel) table.getModel()).fireTableDataChanged();

                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        stopPlay();
    }

    private void createUi() {
        JPanel mainPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
        mainPanel.setLayout(boxLayout);
        {
            //路由器网关和密码设置
            {
                JPanel panel = new JPanel();
                panel.setBorder(BorderFactory.createTitledBorder("路由器网关和密码设置"));
                GridBagLayout layout = new GridBagLayout();
                GridBagConstraints bgc = new GridBagConstraints();
                panel.setLayout(layout);
                // ip port play in north
                {
                    mViewIP = new TextField(Tool.getGuessGateway());
                    mViewIP.setColumns(25);
                    mViewIP.addTextListener(new TextInputLengthLimitListener(15));

                    mBtnConnect = new JButton(ACTION_CONNECT);
                    mBtnConnect.setActionCommand(ACTION_CONNECT);
                    mBtnConnect.addActionListener(this);
                    panel.add(new JLabel("IP:"));
                    {
                        bgc.fill = GridBagConstraints.HORIZONTAL;
                        layout.setConstraints(mViewIP, bgc);
                        panel.add(mViewIP);
                    }
                    {
                        bgc.fill = GridBagConstraints.VERTICAL;
                        bgc.gridwidth = GridBagConstraints.REMAINDER;
                        bgc.gridheight = 2;
                        bgc.weighty = 2;
                        layout.setConstraints(mBtnConnect, bgc);
                        panel.add(mBtnConnect);
                    }
                }
                {
                    {
                        bgc.fill = GridBagConstraints.NONE;
                        bgc.gridwidth = 1;
                        bgc.gridy = 1;
                        bgc.gridx = 0;
                        JLabel tab = new JLabel("密码：");
                        layout.setConstraints(tab, bgc);
                        panel.add(tab);
                    }
                    {
                        mTvRouterPwd = new JPasswordField();
                        bgc.gridy = 1;
                        bgc.gridx = 1;
                        layout.setConstraints(mTvRouterPwd, bgc);
                        mTvRouterPwd.setColumns(20);
                        panel.add(mTvRouterPwd);
                    }
                }
                mainPanel.add(panel);
            }
            {//WAN SET
                mPanelWan = new JPanel(new BorderLayout());
                mPanelWan.setBorder(BorderFactory.createTitledBorder("WAN口设置"));
                // 模式选择
                {
                    JPanel panelWanType = new JPanel();
                    mCheckboxGroupWanType = new CheckboxGroup();

                    mCbPppoe = new Checkbox("PPPoe", mCheckboxGroupWanType, false);
                    mCbPppoe.addItemListener(this);
                    mCbStatic = new Checkbox("STATIC", mCheckboxGroupWanType, false);
                    mCbStatic.addItemListener(this);
                    mCbDhcp = new Checkbox("DHCP", mCheckboxGroupWanType, false);
                    mCbDhcp.addItemListener(this);
                    panelWanType.add(mCbPppoe);
                    panelWanType.add(mCbStatic);
                    panelWanType.add(mCbDhcp);
                    mBtnSet = new JButton(ACTION_SET);
                    mBtnSet.setActionCommand(ACTION_SET);
                    mBtnSet.addActionListener(this);
                    panelWanType.add(mBtnSet);
                    mPanelWan.add(panelWanType, BorderLayout.NORTH);
                }
                //pppoe 和 static 的设置
                {
                    cardPanel = new JPanel();
                    cardlayout = new CardLayout();
                    cardPanel.setLayout(cardlayout);//设置cardPanel布局类型
                    // pppoe 设置窗口

                    {
                        JPanel panelPPPoe = new JPanel();
                        panelPPPoe.setBorder(BorderFactory.createTitledBorder("PPPOE设置"));
                        GridBagLayout layout = new GridBagLayout();
                        GridBagConstraints bgc = new GridBagConstraints();
                        panelPPPoe.setLayout(layout);

                        panelPPPoe.add(new JLabel("PPPOE账号: "));
                        {
                            mViewPppoeAccount = new JTextField();
                            mViewPppoeAccount.setColumns(20);
                            layout.setConstraints(mViewPppoeAccount, bgc);
                            panelPPPoe.add(mViewPppoeAccount);
                        }
                        {
                            bgc.gridx = 0;
                            bgc.gridy = 1;
                            JLabel label = new JLabel("PPPOE密码: ");
                            layout.setConstraints(label, bgc);
                            panelPPPoe.add(label);
                        }
                        {//设置pppoe密码
                            bgc.gridx = 1;
                            mViewPppoePassword = new JPasswordField();
                            mViewPppoePassword.setColumns(20);
                            layout.setConstraints(mViewPppoePassword, bgc);
                            panelPPPoe.add(mViewPppoePassword);
                        }
                        cardPanel.add(panelPPPoe);//在cardPanel面板中连续插入三个界面
                    }
                    // static 设置窗口
                    {
                        JPanel panelStatic = new JPanel();
                        panelStatic.setBorder(BorderFactory.createTitledBorder("STATIC设置"));
                        GridBagLayout layout = new GridBagLayout();
                        GridBagConstraints bgc = new GridBagConstraints();
                        panelStatic.setLayout(layout);

                        panelStatic.add(new JLabel("IP: "));
                        {
                            mViewStaticIp = new JTextField();
                            mViewStaticIp.setColumns(20);
                            layout.setConstraints(mViewStaticIp, bgc);
                            panelStatic.add(mViewStaticIp);
                        }
                        {
                            bgc.gridx = 0;
                            bgc.gridy = 1;
                            JLabel label = new JLabel("掩码: ");
                            layout.setConstraints(label, bgc);
                            panelStatic.add(label);
                        }
                        {
                            bgc.gridx = 1;
                            mViewStaticMask = new JTextField();
                            mViewStaticMask.setColumns(20);
                            layout.setConstraints(mViewStaticMask, bgc);
                            panelStatic.add(mViewStaticMask);
                        }
                        {
                            bgc.gridx = 0;
                            bgc.gridy = 2;
                            JLabel label = new JLabel("网关: ");
                            layout.setConstraints(label, bgc);
                            panelStatic.add(label);
                        }
                        {
                            bgc.gridx = 1;
                            mViewStaticGateway = new JTextField();
                            mViewStaticGateway.setColumns(20);
                            layout.setConstraints(mViewStaticGateway, bgc);
                            panelStatic.add(mViewStaticGateway);
                        }
                        {
                            bgc.gridx = 0;
                            bgc.gridy = 3;
                            JLabel label = new JLabel("DNS1: ");
                            layout.setConstraints(label, bgc);
                            panelStatic.add(label);
                        }
                        {
                            bgc.gridx = 1;
                            mViewStaticDNS1 = new JTextField();
                            mViewStaticDNS1.setColumns(20);
                            layout.setConstraints(mViewStaticDNS1, bgc);
                            panelStatic.add(mViewStaticDNS1);
                        }
                        {
                            bgc.gridx = 0;
                            bgc.gridy = 4;
                            JLabel label = new JLabel("DNS2: ");
                            layout.setConstraints(label, bgc);
                            panelStatic.add(label);
                        }
                        {
                            bgc.gridx = 1;
                            mViewStaticDNS2 = new JTextField();
                            mViewStaticDNS2.setColumns(20);
                            layout.setConstraints(mViewStaticDNS2, bgc);
                            panelStatic.add(mViewStaticDNS2);
                        }
                        cardPanel.add(panelStatic);//在cardPanel面板中连续插入三个界面
                    }
                    cardPanel.setVisible(false);
                    mPanelWan.add(cardPanel, BorderLayout.CENTER);
                }
                mPanelWan.setVisible(false);
                mainPanel.add(mPanelWan);
            }
            {//列表
                JPanel panel = new JPanel();
                panel.setBorder(BorderFactory.createTitledBorder("外网访问测试"));
                panel.add(new JLabel("PING: "));
                mViewPing = new JTextField();
                mViewPing.setText("baidu.com");
                mViewPing.setColumns(30);
                panel.add(mViewPing);
                mBtnTest = new JButton(ACTION_TEST);
                mBtnTest.setActionCommand(ACTION_TEST);
                mBtnTest.addActionListener(this);
                panel.add(mBtnTest);
                mainPanel.add(panel);

            }
            {
                mViewPingResult = new JTextArea();
                mViewPingResult.setBorder(BorderFactory.createTitledBorder("PING 结果"));
                mViewPingResult.setEditable(false);
                mViewPingResult.setRows(10);
                mainPanel.add(mViewPingResult);
            }
        }
        this.add(mainPanel);
        this.addWindowListener(new MyWindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stopPlay();
                dispose();
                Tool.log(e);
                if (mThreadPool != null && !mThreadPool.isShutdown()) {
                    mThreadPool.shutdown();
                }
                super.windowClosing(e);
            }

        });
        this.setResizable(true);
        this.setVisible(true);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    private void startPlay() {

        mBtnConnect.setLabel(ACTION_PAUSE);
        mBtnConnect.setActionCommand(ACTION_PAUSE);

        mViewIP.setEnabled(false);
        mTvRouterPwd.setEnabled(false);

        mThreadPool.submit(this);
    }

    private void stopPlay() {
        mBtnConnect.setLabel(ACTION_PLAY);
        mBtnConnect.setActionCommand(ACTION_PLAY);

        mViewIP.setEnabled(true);
        mTvRouterPwd.setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals(ACTION_CONNECT)) {
            if (!Tool.isIpv4(mViewIP.getText())) {
                mViewIP.requestFocus();
//                System.err.println("请输入正确的网关地址……");
                new AlertDialog(this, "中断", "请输入正确的网关地址……").setVisible(true);
                return;
            }
            if (mTvRouterPwd.getPassword().length == 0) {
//                System.err.println("路由器密码不可为空……");
                new AlertDialog(this, "中断", "路由器密码不可为空……").setVisible(true);
                mTvRouterPwd.requestFocus();
                return;
            }

            //获得类型
            if (!getDeviceType())
                return;

            //登录
            if (!login())
                return;


            mBtnConnect.setEnabled(false);
            mViewIP.setEnabled(false);
            mTvRouterPwd.setEnabled(false);

            loadWan();
        } else if (ae.getActionCommand().equals(ACTION_TEST)) {
            String string = DosCmdHelper.readDosRuntime("ping -n 1 " + mViewPing.getText());
            mViewPingResult.setText(string);
        } else if (ae.getActionCommand().equals(ACTION_SET)) {
            String label = mCheckboxGroupWanType.getSelectedCheckbox().getLabel();
            WanRequireAllBeen require = new WanRequireAllBeen(mWanInfo);
            if (label.equalsIgnoreCase("dhcp")) {
                require.setType("dhcp");
            } else if (label.equalsIgnoreCase("static")) {
                require.setType("static");
                require.setIpaddr(mViewStaticIp.getText());
                require.setGw(mViewStaticGateway.getText());
                require.setNetmask(mViewStaticMask.getText());
                require.setDns1(mViewStaticDNS1.getText());
                require.setDns2(mViewStaticDNS2.getText());
            } else if (label.equalsIgnoreCase("pppoe")) {
                require.setType("pppoe");
                require.setPppoe_username(mViewPppoeAccount.getText());
                require.setPppoe_password(new String(mViewPppoePassword.getPassword()));
            }
            try {
                BWR510LocalConnectionHelper.getInstance().setWan(mViewIP.getText(), require);
            } catch (Exception e) {
                e.printStackTrace();
                new AlertDialog(this, "失败", "设置失败：" + e.getMessage()).setVisible(true);
            }
        }
    }

    private void loadWan() {
        //获得类型
        try {
            String gateway = mViewIP.getText();
            WanResponseAllBeen wanInfo = BWR510LocalConnectionHelper.getInstance().getWan(gateway);
            mWanInfo = wanInfo;
            mViewPppoeAccount.setText(wanInfo.getPppoe_username());
            mViewPppoePassword.setText(wanInfo.getPppoe_password());

            mViewStaticIp.setText(wanInfo.getIpaddr());
            mViewStaticGateway.setText(wanInfo.getGw());
            mViewStaticMask.setText(wanInfo.getNetmask());
            mViewStaticDNS1.setText(wanInfo.getDns1());
            mViewStaticDNS2.setText(wanInfo.getDns2());

            mPanelWan.setVisible(true);

            if (wanInfo.getType().equalsIgnoreCase("pppoe")) {
                mCheckboxGroupWanType.setSelectedCheckbox(mCbPppoe);
                itemStateChanged(new ItemEvent(mCbPppoe, 1, mCbPppoe.getLabel(), 1));
            } else if (wanInfo.getType().equalsIgnoreCase("static")) {
                mCheckboxGroupWanType.setSelectedCheckbox(mCbStatic);
                itemStateChanged(new ItemEvent(mCbStatic, 1, mCbStatic.getLabel(), 1));
            } else if (wanInfo.getType().equalsIgnoreCase("dhcp")) {
                mCheckboxGroupWanType.setSelectedCheckbox(mCbDhcp);
                itemStateChanged(new ItemEvent(mCbDhcp, 1, mCbDhcp.getLabel(), 1));
            }

            this.pack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected boolean login() {
        try {
            String gateway = mViewIP.getText();
            String password = new String(mTvRouterPwd.getPassword());
            BWR510LocalConnectionHelper.getInstance().login(gateway, password);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            new AlertDialog(this, "失败", "登录失败:" + e.getMessage()).setVisible(true);
        }
        return false;
    }

    protected boolean getDeviceType() {
        //获得类型
        try {
            String gateway = mViewIP.getText();
            String deviceType = BWR510LocalConnectionHelper.getInstance().getBase_DeviceType(gateway);
            Tool.log("device type = " + deviceType);
            return true;
        } catch (Exception e) {
            new AlertDialog(this, "失败", "未获取到设备类型 : " + e.getMessage()).setVisible(true);
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getItem() == "PPPoe") {
            if (!cardPanel.isVisible()) {
                cardPanel.setVisible(true);
                this.pack();
            }
            cardlayout.first(cardPanel);
        } else if (e.getItem() == "STATIC") {
            if (!cardPanel.isVisible()) {
                cardPanel.setVisible(true);
                this.pack();
            }
            cardlayout.last(cardPanel);
        } else if (e.getItem() == "DHCP") {
            if (cardPanel.isVisible()) {
                cardPanel.setVisible(false);
                this.pack();
            }
        }
    }
}

