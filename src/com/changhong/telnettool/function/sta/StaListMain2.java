package com.changhong.telnettool.function.sta;

import com.changhong.telnettool.database.SQLiteJDBC;
import com.changhong.telnettool.dialog.AlertDialog;
import com.changhong.telnettool.dialog.ShowDbDialog;
import com.changhong.telnettool.event.MyWindowAdapter;
import com.changhong.telnettool.event.TextInputLengthLimitListener;
import com.changhong.telnettool.tool.DataManager;
import com.changhong.telnettool.tool.Preferences;
import com.changhong.telnettool.tool.Tool;
import com.changhong.telnettool.webinterface.BWR510LocalConnectionHelper;
import com.changhong.telnettool.webinterface.been.ConnectType;
import com.changhong.telnettool.webinterface.been.StaInfo;
import com.changhong.telnettool.webinterface.been.mesh.ListInfo;
import com.changhong.telnettool.webinterface.been.sys.SettingResponseAllBeen;
import javafx.util.Pair;

import javax.security.sasl.AuthenticationException;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class StaListMain2 extends JFrame implements ActionListener, Runnable {

    private static final String DEFAULT_DB_NAME = "staEvent.db";

    private static final String TITLE_FRAME = "下挂设备查看器";
    private static final String ACTION_PLAY = "START";
    private static final String ACTION_PAUSE = "PAUSE";
    private static final String ACTION_EXPORT = "导出";
    private static final String ACTION_SHOW = "显示";

    private static final int DEFAULT_INTERVAL = 3000;
    private static final String KEY_WEB_PWD = "sta_list_web_password";
    private static final String KEY_OUT_DB_NAME = "sta_list_db_name";

    private final TextField mViewIP;
    private final JTable table;
    private final JLabel mViewTxSpeed;
    private final JLabel mViewRxSpeed;
    private final JLabel mViewTxSpeed2$4G;
    private final JLabel mViewTxSpeed5G;
    private final JLabel mViewRxSpeed2$4G;
    private final JLabel mViewRxSpeed5G;
    private final JButton mBtnPlayOrPause;
    private final JPasswordField mTvRouterPwd;

    private JButton mBtnExport;
    private JButton mBtnShow;
    private JTextField mViewOutPutField;
    private ShowDbDialog mShowDbDialog;
    /************* value *********************/
    private Thread mThread;
    private List<StaInfo> mArrStaInfo = new ArrayList<>();
    private SimpleDateFormat mSdf;
    private boolean isRunning;

    public StaListMain2() {
        super(TITLE_FRAME);

        Pair<String, Integer> version = DataManager.getVersionInfo();
        if (version != null)
            setTitle(TITLE_FRAME + " v" + version.getKey());

        setSize(800, 600);

        setLayout(new BorderLayout());
        {
            JPanel panel2 = new JPanel();
            panel2.setLayout(new BorderLayout());
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

                    mBtnPlayOrPause = new JButton(ACTION_PLAY);
                    mBtnPlayOrPause.setActionCommand(ACTION_PLAY);
                    mBtnPlayOrPause.addActionListener(this);
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
                        mTvRouterPwd = new JPasswordField(Preferences.getInstance().readString(KEY_WEB_PWD));
                        bgc.gridy = 1;
                        bgc.gridx = 1;
                        layout.setConstraints(mTvRouterPwd, bgc);
                        mTvRouterPwd.setColumns(20);
                        panel.add(mTvRouterPwd);
                    }
                }
                panel2.add(panel, BorderLayout.NORTH);
            }

            {// tx and rx show
                mViewTxSpeed = new JLabel(Tool.getSpeedStringBit(0));
                Font font = mViewTxSpeed.getFont();
                font = new Font(font.getName(), font.getStyle(), font.getSize() * 2);//new Font(Font.SANS_SERIF, Font.BOLD, 35);
                {
                    mViewTxSpeed.setForeground(new Color(0xFF008B8B));
                    mViewTxSpeed.setFont(font);
                }
                mViewRxSpeed = new JLabel(Tool.getSpeedStringBit(0));
                {
                    mViewRxSpeed.setForeground(new Color(0xFF00EE76));
                    mViewRxSpeed.setFont(font);
                }

                mViewTxSpeed2$4G = new JLabel(Tool.getSpeedStringBit(0));
                {
                    TitledBorder border = BorderFactory.createTitledBorder("2.4G");
                    border.setTitleFont(new Font(Font.DIALOG, Font.PLAIN, border.getTitleFont().getSize()));
                    mViewTxSpeed2$4G.setBorder(border);
                    mViewTxSpeed2$4G.setForeground(new Color(0xFF008B8B));
                }
                mViewTxSpeed5G = new JLabel(Tool.getSpeedStringBit(0));
                {
                    TitledBorder border = BorderFactory.createTitledBorder("5G");
                    border.setTitleFont(new Font(Font.DIALOG, Font.PLAIN, border.getTitleFont().getSize()));
                    mViewTxSpeed5G.setBorder(border);
                    mViewTxSpeed5G.setForeground(new Color(0xFF008B8B));
                }

                mViewRxSpeed2$4G = new JLabel(Tool.getSpeedStringBit(0));
                {
                    TitledBorder border = BorderFactory.createTitledBorder("2.4G");
                    border.setTitleFont(new Font(Font.DIALOG, Font.PLAIN, border.getTitleFont().getSize()));
                    mViewRxSpeed2$4G.setBorder(border);
                    mViewRxSpeed2$4G.setForeground(new Color(0xFF00EE76));
                }
                mViewRxSpeed5G = new JLabel(Tool.getSpeedStringBit(0));
                {
                    TitledBorder border = BorderFactory.createTitledBorder("5G");
                    border.setTitleFont(new Font(Font.DIALOG, Font.PLAIN, border.getTitleFont().getSize()));
                    mViewRxSpeed5G.setBorder(border);
                    mViewRxSpeed5G.setForeground(new Color(0xFF00EE76));
                }

                JPanel panel = new JPanel();
                {
                    panel.setBorder(BorderFactory.createTitledBorder("上下行速率"));
                    JPanel panelTx = new JPanel(new BorderLayout());
                    {
                        panelTx.setBorder(BorderFactory.createTitledBorder("上行速率"));
                        panelTx.add(mViewTxSpeed, BorderLayout.NORTH);
                        panelTx.add(mViewTxSpeed2$4G, BorderLayout.WEST);
                        panelTx.add(mViewTxSpeed5G, BorderLayout.EAST);
                    }
                    JPanel panelRx = new JPanel(new BorderLayout());
                    {
                        panelRx.setBorder(BorderFactory.createTitledBorder("下行速率"));
                        panelRx.add(mViewRxSpeed, BorderLayout.NORTH);
                        panelRx.add(mViewRxSpeed2$4G, BorderLayout.WEST);
                        panelRx.add(mViewRxSpeed5G, BorderLayout.EAST);
                    }
                    panel.add(panelTx);
                    panel.add(panelRx);
                }

                panel2.add(panel, BorderLayout.SOUTH);
            }

            this.add(panel2, BorderLayout.NORTH);
        }
        {
            table = new JTable(new MyTableMode2(mArrStaInfo));
            RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
            table.setRowSorter(sorter);
            table.setRowHeight(table.getFont().getSize());
            JScrollPane scrollpane = new JScrollPane(table);
            scrollpane.setBorder(BorderFactory.createTitledBorder("下挂设备列表"));
            this.add(scrollpane, "Center");

            {
                TableColumn column = table.getColumnModel().getColumn(0);
                int fontSize = table.getFont().getSize();
                column.setMaxWidth(fontSize * 6);
                column.setMinWidth(fontSize * 2);
                DefaultTableCellRenderer render = new DefaultTableCellRenderer();
                render.setHorizontalAlignment(SwingConstants.CENTER);
                render.setForeground(Color.white);
                render.setBackground(Color.darkGray);
                column.setCellRenderer(render);
            }
            for (int i = 2; i <= MyTableMode2.TITLE_STA.length; ++i) {
                TableColumn column = table.getColumnModel().getColumn(i);
                DefaultTableCellRenderer render = new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    }
                };
                if (i == 5 || i == 2 || i == 3 || i == 4) {
                    render.setHorizontalAlignment(SwingConstants.CENTER);
                } else if (i == 7 || i == 6)
                    render.setHorizontalAlignment(SwingConstants.RIGHT);
                column.setCellRenderer(render);
            }
        }

        this.add(initOutputBar(), BorderLayout.SOUTH);

        addWindowListener(new MyWindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stopPlay();
                dispose();
                Tool.log(e);
                if (mThread != null && !mThread.isInterrupted()) {
                    isRunning = false;
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

    private Component initOutputBar() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("输出设置："));
        panel.add(new Label("Output path:"));
        mViewOutPutField = new JTextField(Preferences.getInstance().readString(KEY_OUT_DB_NAME, DEFAULT_DB_NAME));
        mViewOutPutField.setColumns(60);
        panel.add(mViewOutPutField);
        mBtnExport = new JButton(ACTION_EXPORT);
        mBtnExport.setActionCommand(ACTION_EXPORT);
        mBtnExport.addActionListener(this);
        panel.add(mBtnExport);
        mBtnShow = new JButton(ACTION_SHOW);
        mBtnShow.setActionCommand(ACTION_SHOW);
        mBtnShow.addActionListener(this);
        panel.add(mBtnShow);
        return panel;
    }

    private void startPlay() {

        mBtnPlayOrPause.setText(ACTION_PAUSE);
        mBtnPlayOrPause.setActionCommand(ACTION_PAUSE);

        mViewIP.setEnabled(false);
        mTvRouterPwd.setEnabled(false);

        mViewOutPutField.setEnabled(false);
        String outPath = mViewOutPutField.getText();
        if (outPath.length() == 0) {
            mViewOutPutField.setText(DEFAULT_DB_NAME);
        } else if (!outPath.endsWith(".db")) {
            mViewOutPutField.setText(outPath + ".db");
        }
        mBtnExport.setEnabled(false);

        Preferences.getInstance().saveString(KEY_OUT_DB_NAME, mViewOutPutField.getText())
                .saveString(KEY_WEB_PWD, new String(mTvRouterPwd.getPassword())).commit();

        isRunning = true;
        mThread = new Thread(this);
        mThread.start();
    }

    private void stopPlay() {
        mBtnPlayOrPause.setLabel(ACTION_PLAY);
        mBtnPlayOrPause.setActionCommand(ACTION_PLAY);

        mViewIP.setEnabled(true);
        mTvRouterPwd.setEnabled(true);

        isRunning = false;
        if (mThread != null && mThread.isInterrupted()) {
            mThread.interrupt();
            while (mThread.isAlive()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showFileDialog2ChooseFile() {
        FileDialog fileDialog = new FileDialog(this, "Set out put path……", FileDialog.SAVE);
        fileDialog.addComponentListener(new ComponentListener() {

            @Override
            public void componentShown(ComponentEvent e) {
                Tool.log("componentShown   ====>   " + e.toString());
            }

            @Override
            public void componentResized(ComponentEvent e) {
                Tool.log("componentResized   ====>   " + e.toString());
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                Tool.log("componentMoved   ====>   " + e.toString());
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                Tool.log("componentHidden   ====>   " + e.getComponent().toString());
                if (e.getComponent() instanceof FileDialog) {
                    String file = ((FileDialog) e.getComponent()).getFile();
                    String directory = ((FileDialog) e.getComponent()).getDirectory();
                    if (file == null && directory == null)
                        mViewOutPutField.setText(DEFAULT_DB_NAME);
                    else {
                        if (!file.endsWith(".db"))
                            file = file + ".db";
                        mViewOutPutField.setText(directory + file);
                    }
                }
            }
        });
        fileDialog.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Tool.log(e.getActionCommand());

        if (e.getActionCommand().equals(ACTION_PLAY)) {
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
        } else if (e.getActionCommand().equals(ACTION_PAUSE)) {
            stopPlay();
        }//export
        else if (e.getActionCommand().equals(ACTION_EXPORT)) {
            showFileDialog2ChooseFile();
        } else if (e.getActionCommand().equals(ACTION_SHOW)) {
            String outPath = mViewOutPutField.getText();
            if (outPath == null || outPath.trim().length() == 0)
                outPath = DEFAULT_DB_NAME;
            File file = new File(outPath);
            if (!file.exists()) {
                new AlertDialog(this, "提示", "文件不存在……").setVisible(true);
                return;
            } else {
                if (mShowDbDialog == null)
                    mShowDbDialog = new ShowDbDialog(this, "查看数据库", outPath.substring(0, outPath.length() - 3), StaOnOffEvent.class);
                mShowDbDialog.setVisible(true);
            }
        }
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

    protected ArrayList<StaInfo> getAllSta(String gateway) throws Exception {
        ArrayList<StaInfo> staInfos = new ArrayList<>();
        List<StaInfo> tempArr = BWR510LocalConnectionHelper.getInstance().getStaInfo(gateway);
        for (StaInfo staInfo : tempArr) {
            staInfo.setSuperiorNode("主机");
        }
        staInfos.addAll(tempArr);
        List<ListInfo> meshInfos = BWR510LocalConnectionHelper.getInstance().getMeshNetwork(gateway);
        for (ListInfo listInfo : meshInfos) {
            staInfos.addAll(listInfo.getSta_info());
            for (StaInfo staInfo : listInfo.getSta_info()) {
                staInfo.setSuperiorNode(listInfo.getMac());
            }
        }
        return staInfos;
    }

    protected int getRouterInfo(String gateway) throws Exception {
        SettingResponseAllBeen response = BWR510LocalConnectionHelper.getInstance().getBase_RouterInfo(gateway);
        return response.getTime() != null ? response.getTime() : 0;
    }

    private String getCurrentTime() {
        if (mSdf == null)
            mSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return mSdf.format(new Date(System.currentTimeMillis()));
    }

    private void compareProgress(SQLiteJDBC databaseOffline, ArrayList<StaInfo> staList) {
//        String time = getCurrentTime();
        long time = System.currentTimeMillis();
        if (mArrStaInfo == null || mArrStaInfo.isEmpty()) {
            return;
        }

        //上线判断
        ArrayList<StaInfo> arrOnline = new ArrayList<>();
        arrOnline.addAll(staList);
        boolean diffSet = arrOnline.removeAll(mArrStaInfo);
        if (diffSet) {
            for (StaInfo staInfo : arrOnline) {
                if (staInfo.getMac() == null || staInfo.getMac().isEmpty()) {
                    System.err.println("====~" + staInfo.toString());
                    continue;
                }
                databaseOffline.insert(new StaOnOffEvent(staInfo.getName(), staInfo.getMac(), staInfo.getIp(), staInfo.getSuperiorNode(), staInfo.getLink_time(), time, true));
            }
        }

        //下线判断
        ArrayList<StaInfo> arrOffline = new ArrayList<>();
        arrOffline.addAll(mArrStaInfo);
        diffSet = arrOffline.removeAll(staList);
        if (diffSet) {
            for (StaInfo staInfo : arrOffline) {
                if (staInfo.getMac() == null || staInfo.getMac().isEmpty()) {
                    System.err.println("====~" + staInfo.toString());
                    continue;
                }
                databaseOffline.insert(new StaOnOffEvent(staInfo.getName(), staInfo.getMac(), staInfo.getIp(), staInfo.getSuperiorNode(), staInfo.getLink_time(), time, false));
            }
        }

        //前后同时存在，但是可能下线判断
        ArrayList<StaInfo> oldArr = new ArrayList<>();
        oldArr.addAll(mArrStaInfo);
        oldArr.retainAll(staList);
        ArrayList<StaInfo> newArr = new ArrayList<>();
        newArr.addAll(staList);
        newArr.retainAll(mArrStaInfo);

    }

    @Override
    public void run() {
        long costTime;
        long lastLoadTime = System.currentTimeMillis();
        final long[] speedAll = new long[6];// [上一次的上行速度, 上一次的下行速度, 上一次的2.4G上行速度, 上一次的2.4G下行速度, 上一次的5G上行速度, 上一次的5G下行速度]
        final long[] tmpBytesAll = new long[6];// [上一次的上行速度, 上一次的下行速度, 上一次的2.4G上行速度, 上一次的2.4G下行速度, 上一次的5G上行速度, 上一次的5G下行速度]
        final long[] bytesAll = new long[6];// [上行总量, 下行总量, 2.4G上行总量, 2.4G下行总量, 5G上行总量, 5G下行总量 ]
        final String gateway = mViewIP.getText();

        String outPath = mViewOutPutField.getText();
        if (outPath.endsWith(".db"))
            outPath = outPath.replace(".db", "");
        File file = new File(outPath);
        if (file.getAbsolutePath().indexOf('/') != -1) {
            file = file.getParentFile();
            if (!file.exists() || file.isFile()) {
                file.mkdirs();
            }
        }
        SQLiteJDBC databaseOffline = new SQLiteJDBC(outPath, StaOnOffEvent.class);

        while (isRunning) {
            try {
                costTime = System.currentTimeMillis();
                ArrayList<StaInfo> staInfos = getAllSta(gateway);

                // 速度计算,比较
                if (!staInfos.isEmpty()) {
                    final long interval2 = System.currentTimeMillis() - lastLoadTime;
                    lastLoadTime = System.currentTimeMillis();

                    Arrays.fill(tmpBytesAll, 0);

                    for (StaInfo b : staInfos) {
                        tmpBytesAll[0] += b.getDownload();
                        tmpBytesAll[1] += b.getUpload();
                        if (b.getConnectType() == ConnectType.WIFI24) {
                            tmpBytesAll[2] += b.getDownload();
                            tmpBytesAll[3] += b.getUpload();
                        } else if (b.getConnectType() == ConnectType.WIFI5) {
                            tmpBytesAll[4] += b.getDownload();
                            tmpBytesAll[5] += b.getUpload();
                        }
                        for (StaInfo a : mArrStaInfo) {
                            if (a.equals(b)) {
                                b.speedTx = (int) ((b.getDownload() - a.getDownload()) * 1000 / interval2);
                                b.speedRx = (int) ((b.getUpload() - a.getUpload()) * 1000 / interval2);
                                break;
                            }
                        }
                    }

                    for (int i = 0; i < tmpBytesAll.length; i++) {
                        speedAll[i] = (tmpBytesAll[i] - bytesAll[i]) * 1000 / interval2;
                    }
                    System.arraycopy(tmpBytesAll, 0, bytesAll, 0, tmpBytesAll.length);

                    // 刷新Ui
                    mViewTxSpeed.setText(Tool.getSpeedString(speedAll[1]));
                    mViewRxSpeed.setText(Tool.getSpeedString(speedAll[0]));
                    mViewTxSpeed2$4G.setText(Tool.getSpeedString(speedAll[3]));
                    mViewRxSpeed2$4G.setText(Tool.getSpeedString(speedAll[2]));
                    mViewTxSpeed5G.setText(Tool.getSpeedString(speedAll[5]));
                    mViewRxSpeed5G.setText(Tool.getSpeedString(speedAll[4]));
                } else {
                    Arrays.fill(speedAll, 0);
                    for (StaInfo b : staInfos) {
                        bytesAll[0] += b.getDownload();
                        bytesAll[1] += b.getUpload();
                        if (b.getConnectType() == ConnectType.WIFI24) {
                            bytesAll[2] += b.getDownload();
                            bytesAll[3] += b.getUpload();
                        } else if (b.getConnectType() == ConnectType.WIFI5) {
                            bytesAll[4] += b.getDownload();
                            bytesAll[5] += b.getUpload();
                        }
                    }
                }

                compareProgress(databaseOffline, staInfos);

                mArrStaInfo.clear();
                mArrStaInfo.addAll(staInfos);
                ((AbstractTableModel) table.getModel()).fireTableDataChanged();

                Thread.sleep(DEFAULT_INTERVAL + costTime - System.currentTimeMillis());
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

    public static void main(String[] args) {
        new StaListMain2();
    }
}
