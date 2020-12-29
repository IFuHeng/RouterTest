package com.changhong.telnettool.function.test_exam;

import com.changhong.telnettool.dialog.AlertDialog;
import com.changhong.telnettool.event.IPv4TextLimitListener;
import com.changhong.telnettool.event.MyWindowAdapter;
import com.changhong.telnettool.event.PositiveNumberTextLimitListener;
import com.changhong.telnettool.event.TextInputLengthLimitListener;
import com.changhong.telnettool.tool.Tool;
import com.changhong.telnettool.webinterface.BWR510LocalConnectionHelper;
import com.changhong.telnettool.webinterface.been.sys.SettingResponseAllBeen;
import com.changhong.telnettool.webinterface.been.wan.WanResponseAllBeen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * 2.2.11.局域网设置功能测试
 */
public class Test2p2p11 extends JFrame implements ActionListener, Runnable {

    private static final String ACTION_CONNECT = "连接";
    private static final String TITLE_FRAME = "2.2.11.局域网设置功能测试";

    private JButton mBtnConnect;
    private TextField mViewIP;
    private JPasswordField mTvRouterPwd;
    private JScrollPane mScrollpaneRouterInfo;
    private JTextArea mViewRouterInfo;

    private TextField mTvGateway;

    public Test2p2p11() {
        super(TITLE_FRAME);
        createUi();
    }

    private void createUi() {
        this.setLayout(new BorderLayout());
        {
            JPanel panel = new JPanel();
            panel.setBorder(BorderFactory.createTitledBorder("路由器网关和密码设置"));
            GridBagLayout layout = new GridBagLayout();
            GridBagConstraints gbc = new GridBagConstraints();
            panel.setLayout(layout);
            {// ip port play in north
                mViewIP = new TextField(Tool.getGuessGateway());
                mViewIP.setColumns(25);
                mViewIP.addTextListener(new TextInputLengthLimitListener(15));

                mBtnConnect = new JButton(ACTION_CONNECT);
                mBtnConnect.setActionCommand(ACTION_CONNECT);
                mBtnConnect.addActionListener(this);
                panel.add(new Label("IP:"));
                {
                    gbc.fill = GridBagConstraints.HORIZONTAL;
                    layout.setConstraints(mViewIP, gbc);
                    panel.add(mViewIP);
                }
                {
                    gbc.fill = GridBagConstraints.VERTICAL;
                    gbc.gridwidth = GridBagConstraints.REMAINDER;
                    gbc.gridheight = 2;
                    gbc.weighty = 2;
                    layout.setConstraints(mBtnConnect, gbc);
                    panel.add(mBtnConnect);
                }
            }
            {
                {
                    gbc.fill = GridBagConstraints.NONE;
                    gbc.gridwidth = 1;
                    gbc.gridy = 1;
                    gbc.gridx = 0;
                    JLabel tab = new JLabel("密码：");
                    layout.setConstraints(tab, gbc);
                    panel.add(tab);
                }
                {
                    mTvRouterPwd = new JPasswordField();
                    gbc.gridy = 1;
                    gbc.gridx = 1;
                    gbc.fill = GridBagConstraints.HORIZONTAL;
                    layout.setConstraints(mTvRouterPwd, gbc);
                    mTvRouterPwd.setColumns(20);
                    panel.add(mTvRouterPwd);
                }
            }
            this.add(panel, BorderLayout.NORTH);
        }
        {

            JPanel panel = new JPanel();
            panel.setBorder(BorderFactory.createTitledBorder("LAN设置"));
            GridBagLayout layout = new GridBagLayout();
            GridBagConstraints gbc = new GridBagConstraints();
            panel.setLayout(layout);
            {
                panel.add(new JLabel("网关："));
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.gridx = 1;
                mTvGateway = new TextField();
                mTvGateway.setColumns(20);
                mTvGateway.addTextListener(new IPv4TextLimitListener());
            }


            this.add(panel, BorderLayout.NORTH);
        }
        {//列表
            mViewRouterInfo = new JTextArea();
            mViewRouterInfo.setRows(10);
            mScrollpaneRouterInfo = new JScrollPane(mViewRouterInfo);
            mScrollpaneRouterInfo.setBorder(BorderFactory.createTitledBorder("路由器信息"));
            mScrollpaneRouterInfo.setVisible(false);
            this.add(mScrollpaneRouterInfo, BorderLayout.CENTER);
        }

        this.addWindowListener(new MyWindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                Tool.log(e);
                super.windowClosing(e);
            }

        });
        this.setResizable(true);
        this.setVisible(true);
        this.pack();
        this.setLocationRelativeTo(null);
    }


    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals(ACTION_CONNECT)) {
            if (!Tool.isIpv4(mViewIP.getText())) {
                mViewIP.requestFocus();
                new AlertDialog(this, "中断", "请输入正确的网关地址……").setVisible(true);
                return;
            }
            if (mTvRouterPwd.getPassword().length == 0) {
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

            mViewIP.setEnabled(false);
            mTvRouterPwd.setEnabled(false);
            mBtnConnect.setEnabled(false);

            loadDeviceInfo();
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
            new AlertDialog(this, "失败", "登录失败：" + e.getMessage()).setVisible(true);
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
            e.printStackTrace();
            new AlertDialog(this, "失败", "获取设备类型失败：" + e.getMessage()).setVisible(true);
        }
        return false;
    }

    protected void loadDeviceInfo() {
        try {
            SettingResponseAllBeen been = BWR510LocalConnectionHelper.getInstance().getBase_RouterInfo(mViewIP.getText());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("设备类型：").append(been.getEquipment()).append('\n')
                    .append("软件版本：").append(been.getSoft_ver()).append('\n')
                    .append("SN：").append(been.getSN()).append('\n')
                    .append("上电时间：").append(Tool.turnTimeString(been.getUptime())).append('\n')
                    .append("联网时间：").append(Tool.turnTimeString(been.getNetwork_time())).append('\n')
                    .append("WAN MAC地址：").append(been.getWan_mac()).append('\n')
                    .append("WAN类型：").append(been.getWan_type()).append('\n')
                    .append("WAN IP：").append(been.getWan_ip()).append('\n')
                    .append("WAN网关：").append(been.getWan_gw()).append('\n')
                    .append("WAN掩码：").append(been.getWan_netmask()).append('\n')
                    .append("LAN MAC：").append(been.getLan_mac()).append('\n')
                    .append("LAN IP：").append(been.getLan_ip()).append('\n')
                    .append("LAN掩码：").append(been.getLan_netmask()).append('\n')
                    .append("DNS：").append(been.getDNS()).append('\n');
            mViewRouterInfo.setText(stringBuilder.toString());
            if (!mScrollpaneRouterInfo.isVisible()) {
                mScrollpaneRouterInfo.setVisible(true);
                this.pack();
            }
        } catch (Exception e) {
            new AlertDialog(this, "失败", "获取路由信息失败：" + e.getMessage()).setVisible(true);
            e.printStackTrace();
        }
    }

    protected void loadLanInfo() {
        try {
            WanResponseAllBeen been = BWR510LocalConnectionHelper.getInstance().getLanInfo(mViewIP.getText());
            if (!mScrollpaneRouterInfo.isVisible()) {
                mScrollpaneRouterInfo.setVisible(true);
                this.pack();
            }
        } catch (Exception e) {
            new AlertDialog(this, "失败", "获取路由信息失败：" + e.getMessage()).setVisible(true);
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

    }
}

