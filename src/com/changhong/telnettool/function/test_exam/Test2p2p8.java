package com.changhong.telnettool.function.test_exam;

import com.changhong.telnettool.dialog.AlertDialog;
import com.changhong.telnettool.event.MyWindowAdapter;
import com.changhong.telnettool.event.TextInputLengthLimitListener;
import com.changhong.telnettool.tool.Tool;
import com.changhong.telnettool.webinterface.BWR510LocalConnectionHelper;
import com.changhong.telnettool.webinterface.been.StaInfo;
import com.changhong.telnettool.webinterface.been.mesh.ListInfo;
import com.changhong.telnettool.webinterface.been.sys.SettingResponseAllBeen;

import javax.security.sasl.AuthenticationException;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 2.2.8. 连接设备 已连接设备 显示 功能测试
 */
public class Test2p2p8 extends JFrame implements Runnable, ActionListener {

    private static final String[] TITLE_PROCESS = {"名称", "MAC", "IP", "连接\n方式", "在线时间", "上行", "下行"};
    private static final String ACTION_PAUSE = "停止";
    private static final String ACTION_PLAY = "启动";
    private static final String TITLE_FRAME = "2.2.8. 连接设备 已连接设备 显示 功能测试";

    private List<StaInfo> mArrStaInfo = new ArrayList<>();
    private JButton mBtnPlayOrPause;
    private TextField mViewIP;
    private JTable table;
    private JPasswordField mTvRouterPwd;
    ExecutorService mThreadPool;
    private JScrollPane mScrollpaneRouterInfo;
    private JTextArea mViewRouterInfo;

    public Test2p2p8() {
        super(TITLE_FRAME);
        mThreadPool = Executors.newFixedThreadPool(3);
        createUi();
    }

    @Override
    public void run() {
        loadDeviceInfo();
        while (mBtnPlayOrPause.getActionCommand().equals(ACTION_PAUSE)) {
            try {
                ArrayList<StaInfo> staInfos = new ArrayList<>();
                List<StaInfo> tempArr = BWR510LocalConnectionHelper.getInstance().getStaInfo(mViewIP.getText());
                mArrStaInfo.clear();
                mArrStaInfo.addAll(tempArr);

                List<ListInfo> meshInfos = BWR510LocalConnectionHelper.getInstance().getMeshNetwork(mViewIP.getText());
                for (ListInfo listInfo : meshInfos) {
                    mArrStaInfo.addAll(listInfo.getSta_info());
                }

                ((AbstractTableModel) table.getModel()).fireTableDataChanged();

                Thread.sleep(5000);
            } catch (Exception e) {
                if (e instanceof AuthenticationException)
                    if (!login()) {
                        break;
                    }
                e.printStackTrace();
            }
        }

        stopPlay();
    }

    private void createUi() {
        this.setLayout(new BorderLayout());
        {
            JPanel panel = new JPanel();
            panel.setBorder(BorderFactory.createTitledBorder("路由器网关和密码设置"));
            GridBagLayout layout = new GridBagLayout();
            GridBagConstraints bgc = new GridBagConstraints();
            panel.setLayout(layout);
            {// ip port play in north
                mViewIP = new TextField(Tool.getGuessGateway());
                mViewIP.setColumns(25);
                mViewIP.addTextListener(new TextInputLengthLimitListener(15));

                mBtnPlayOrPause = new JButton(ACTION_PLAY);
                mBtnPlayOrPause.setActionCommand(ACTION_PLAY);
                mBtnPlayOrPause.addActionListener(this);
                panel.add(new Label("IP:"));
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
                    layout.setConstraints(mBtnPlayOrPause, bgc);
                    panel.add(mBtnPlayOrPause);
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
                    bgc.fill = GridBagConstraints.HORIZONTAL;
                    layout.setConstraints(mTvRouterPwd, bgc);
                    mTvRouterPwd.setColumns(20);
                    panel.add(mTvRouterPwd);
                }
            }
            this.add(panel, BorderLayout.NORTH);
        }
        {//列表
            table = new JTable(new MyTableMode());
            RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
            table.setRowSorter(sorter);
            table.setRowHeight(table.getFont().getSize());
            JScrollPane scrollpane = new JScrollPane(table);
            scrollpane.setBorder(BorderFactory.createTitledBorder("在线列表"));
            this.add(scrollpane, BorderLayout.CENTER);

            {
                TableColumn column = table.getColumnModel().getColumn(3);
                DefaultTableCellRenderer render = new DefaultTableCellRenderer();
                render.setHorizontalAlignment(SwingConstants.CENTER);
                column.setCellRenderer(render);
            }
            for (int i = 4; i < 7; i++) {
                TableColumn column = table.getColumnModel().getColumn(i);
                DefaultTableCellRenderer render = new DefaultTableCellRenderer();
                render.setHorizontalAlignment(SwingConstants.RIGHT);
                column.setCellRenderer(render);
            }
        }
        {//列表
            mViewRouterInfo = new JTextArea();
            mViewRouterInfo.setRows(10);
            mScrollpaneRouterInfo = new JScrollPane(mViewRouterInfo);
            mScrollpaneRouterInfo.setBorder(BorderFactory.createTitledBorder("路由器信息"));
            mScrollpaneRouterInfo.setVisible(false);
            this.add(mScrollpaneRouterInfo, BorderLayout.SOUTH);
        }

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

        mBtnPlayOrPause.setText(ACTION_PAUSE);
        mBtnPlayOrPause.setActionCommand(ACTION_PAUSE);

        mViewIP.setEnabled(false);
        mTvRouterPwd.setEnabled(false);

        mThreadPool.submit(this);
    }

    private void stopPlay() {
        mBtnPlayOrPause.setLabel(ACTION_PLAY);
        mBtnPlayOrPause.setActionCommand(ACTION_PLAY);

        mViewIP.setEnabled(true);
        mTvRouterPwd.setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals(ACTION_PLAY)) {
            if (!Tool.isIpv4(mViewIP.getText())) {
                mViewIP.requestFocus();
                System.err.println("请输入正确的网关地址……");
                return;
            }
            if (mTvRouterPwd.getPassword().length == 0) {
                System.err.println("路由器密码不可为空……");
                mTvRouterPwd.requestFocus();
                return;
            }

            //获得类型
            if (!getDeviceType())
                return;

            //登录
            if (!login())
                return;

            startPlay();
        } else if (ae.getActionCommand().equals(ACTION_PAUSE)) {
            stopPlay();
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
            mScrollpaneRouterInfo.setVisible(true);
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
            this.pack();
        } catch (Exception e) {
            new AlertDialog(this, "失败", "获取路由信息失败：" + e.getMessage()).setVisible(true);
            e.printStackTrace();
        }
    }

    class MyTableMode extends AbstractTableModel {

        public int getColumnCount() {
            return TITLE_PROCESS.length;
        }

        public int getRowCount() {
            return mArrStaInfo == null ? 0 : mArrStaInfo.size();
        }

        @Override
        public String getColumnName(int column) {
            return TITLE_PROCESS[column];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 5:
                case 6:
//                    return Integer.class;
                default:
                    return String.class;
            }
        }

        public Object getValueAt(int row, int col) {
            StaInfo staInfo = mArrStaInfo.get(row);
            switch (col) {
                case 0:
                    return staInfo.getName();
                case 1:
                    return staInfo.getMac();
                case 2:
                    return staInfo.getIp();
                case 3:
                    return staInfo.getConnectType().getName();
                case 4:
                    return Tool.turnTimeString(staInfo.getLink_time());
                case 5:
                    return Tool.getCountString(staInfo.getUpload());
                case 6:
                    return Tool.getCountString(staInfo.getDownload());
                default:
                    return null;
            }
        }
    }
}

