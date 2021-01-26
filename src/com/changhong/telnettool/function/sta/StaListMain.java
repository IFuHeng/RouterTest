package com.changhong.telnettool.function.sta;

import com.changhong.telnettool.dialog.AlertDialog;
import com.changhong.telnettool.event.MyWindowAdapter;
import com.changhong.telnettool.event.PositiveNumberTextLimitListener;
import com.changhong.telnettool.event.TextInputLengthLimitListener;
import com.changhong.telnettool.net.TelnetClientHelper;
import com.changhong.telnettool.tool.DataManager;
import com.changhong.telnettool.tool.Tool;
import javafx.util.Pair;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StaListMain extends JFrame implements ActionListener, Runnable {

    private static final String TITLE_FRAME = "下挂设备查看器";

    private static final String CMD_GET_STA_LIST_2p4G = "cat /proc/wlan0/sta_info";
    private static final String CMD_GET_STA_LIST_5G = "cat /proc/wlan1/sta_info";
    private static final String CMD_GET_STA_NAME = "hexdump /var/lib/misc/udhcpd.leases";
    private static final String CMD_GET_STA_IP = "cat proc/net/arp";

    private static final String[] TITLE_STA = {"名称", "MAC", "IP", "频段", "强度", "state", "上行", "下行", "带宽"};

    private static final String ACTION_PLAY = "START";
    private static final String ACTION_PAUSE = "PAUSE";
    private static final String ACTION_EXPORT = "EXPORT";

    private static final int DEFAULT_INTERVAL = 1000;

    private final Button mBtnPlayOrPause;
    private final TextField mViewPort;
    private final TextField mViewIP;
    private final TextField mViewAccount;
    private final JPasswordField mViewPassword;
    private final JTable table;
    private final JLabel mViewTxSpeed;
    private final JLabel mViewRxSpeed;
    private final JLabel mViewTxSpeed2$4G;
    private final JLabel mViewTxSpeed5G;
    private final JLabel mViewRxSpeed2$4G;
    private final JLabel mViewRxSpeed5G;

    private Button mBtnExport;
    private TextField mViewOutPutField;

    /************* value *********************/
    private final List<StaInfo> mArrSta = new ArrayList<>();
    ExecutorService mThreadPool;

    public StaListMain() {
        super(TITLE_FRAME);
        mThreadPool = Executors.newFixedThreadPool(3);

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
                mViewIP.setColumns(25);
                mViewIP.addTextListener(new TextInputLengthLimitListener(15));
                mViewPort = new TextField("23");
                mViewPort.setColumns(5);
                mViewPort.addTextListener(new TextInputLengthLimitListener(5));
                mViewPort.addTextListener(new PositiveNumberTextLimitListener());

                mBtnPlayOrPause = new Button(ACTION_PLAY);
                mBtnPlayOrPause.setActionCommand(ACTION_PLAY);
                mBtnPlayOrPause.addActionListener(this);
                JPanel panel = new JPanel();
                panel.setBorder(BorderFactory.createTitledBorder("服务地址和端口设置"));
                panel.add(new Label("IP:"));
                panel.add(mViewIP);
                panel.add(new Label("  port:"));
                panel.add(mViewPort);

                panel.add(mBtnPlayOrPause);
                panel.setMinimumSize(new Dimension(600, 300));
                panel2.add(panel, BorderLayout.NORTH);
            }
            {// ACCOUNT AND PASSWORD IN CENTER
                mViewAccount = new TextField("root");
                mViewAccount.setColumns(25);
                mViewAccount.addTextListener(new TextInputLengthLimitListener(25));
                mViewPassword = new JPasswordField("admin2020@ch");
                mViewPassword.setColumns(15);

                JPanel panel = new JPanel();
                panel.setBorder(BorderFactory.createTitledBorder("用户名和密码"));
                panel.add(new Label("User:"));
                panel.add(mViewAccount);
                panel.add(new Label("  password:"));
                panel.add(mViewPassword);

                panel2.add(panel, BorderLayout.CENTER);
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
            table = new JTable(new MyTableMode(TITLE_STA, mArrSta));
            RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
            table.setRowSorter(sorter);
            table.setRowHeight(table.getFont().getSize());
            JScrollPane scrollpane = new JScrollPane(table);
            scrollpane.setBorder(BorderFactory.createTitledBorder("下挂设备列表"));
            this.add(scrollpane, "Center");

            for (int i = 0; i < TITLE_STA.length; ++i) {
                if (i == 0 || i == 4 || i == 5)
                    continue;
                TableColumn column = table.getColumnModel().getColumn(i);
                DefaultTableCellRenderer render = new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    }
                };
                if (i == 1 || i == 2 || i == 3 || i == 8) {
                    render.setHorizontalAlignment(SwingConstants.CENTER);
                } else
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
                if (mThreadPool != null && !mThreadPool.isShutdown()) {
                    mThreadPool.shutdown();
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
        mViewOutPutField = new TextField();
        mViewOutPutField.setColumns(60);
        panel.add(mViewOutPutField);
        mBtnExport = new Button(ACTION_EXPORT);
        mBtnExport.setActionCommand(ACTION_EXPORT);
        mBtnExport.addActionListener(this);
        panel.add(mBtnExport);
        return panel;
    }

    private void startPlay() {

        mBtnPlayOrPause.setLabel(ACTION_PAUSE);
        mBtnPlayOrPause.setActionCommand(ACTION_PAUSE);

        mViewIP.setEnabled(false);
        mViewPort.setEnabled(false);
        mViewOutPutField.setEnabled(false);

        mThreadPool.submit(this);
    }

    private void stopPlay() {
        mBtnPlayOrPause.setLabel(ACTION_PLAY);
        mBtnPlayOrPause.setActionCommand(ACTION_PLAY);

        mViewIP.setEnabled(true);
        mViewPort.setEnabled(true);
        mViewOutPutField.setEnabled(true);
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
                        mViewOutPutField.setText(null);
                    else
                        mViewOutPutField.setText(directory + file);
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
                return;
            } else if (mViewPort.getText().isEmpty()) {
                mViewPort.requestFocus();
                return;
            }
            startPlay();
        } else if (e.getActionCommand().equals(ACTION_PAUSE)) {
            stopPlay();
        }//export
        else if (e.getActionCommand().equals(ACTION_EXPORT)) {
            showFileDialog2ChooseFile();
        }
    }

    @Override
    public void run() {

        final String IP = mViewIP.getText();
        final String USER = mViewAccount.getText();
        final String PASSWORD = new String(mViewPassword.getPassword());
        final int port = Tool.turnString2Int(mViewPort.getText(), 23);
        final int interval = DEFAULT_INTERVAL;//每次请求间隔 <=1s
        final String outPath = mViewOutPutField.getText();

        long lastLoadTime = System.currentTimeMillis();

        final long[] speedAll = new long[6];// [上一次的上行速度, 上一次的下行速度, 上一次的2.4G上行速度, 上一次的2.4G下行速度, 上一次的5G上行速度, 上一次的5G下行速度]
        final long[] tmpBytesAll = new long[6];// [上一次的上行速度, 上一次的下行速度, 上一次的2.4G上行速度, 上一次的2.4G下行速度, 上一次的5G上行速度, 上一次的5G下行速度]
        final long[] bytesAll = new long[6];// [上行总量, 下行总量, 2.4G上行总量, 2.4G下行总量, 5G上行总量, 5G下行总量 ]

        FileWriter fileWriter = null;//输出字符流
        //如果未设置输出文件路径，将不初始化输出字符流
        if (outPath != null && outPath.length() > 0) {
            try {
                fileWriter = new FileWriter(outPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //输出开头信息
        Toolkit.getDefaultToolkit().beep();
        if (fileWriter != null) {
            try {
                fileWriter.write("----------------Start----------------\n");
                fileWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Tool.log("repared");
        // 初始化telnet连接，并登录
        TelnetClientHelper telnetManager;
        try {
            telnetManager = new TelnetClientHelper(IP, port);
            Tool.log("login: " + telnetManager.login(USER, PASSWORD));
        } catch (Exception e) {
            e.printStackTrace();
            // 初始化telnet失败，输出结束信息，并关闭输出流，返回
            stopPlay();
            if (fileWriter != null)
                try {
                    fileWriter.write(e.getMessage());
                    fileWriter.write('\n');
                    fileWriter.write("----------------End----------------\n");
                    fileWriter.write('\n');
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            Toolkit.getDefaultToolkit().beep();
            new AlertDialog(this, "警告", e.getMessage()).setVisible(true);
            return;
        }
        try {
            // 启动按钮命令是 ACTION_PAUSE 并且 要运行的命令集合非空，执行循环
            while (mBtnPlayOrPause.getActionCommand().equals(ACTION_PAUSE)) {
                long costTime = System.currentTimeMillis();//每次循环消耗时间
//            Tool.log("----------------round---------------------");

                String temp = telnetManager.sendCommand(CMD_GET_STA_NAME);
                ArrayList<StaInfo1> arrStaInfo1 = null;
                ArrayList<StaInfo> arrStaInfo = null;

                {//获取 下挂设备列表（name/ip/mac/lease）
                    byte[] data = Tool.analysisHexDump(temp);
                    arrStaInfo1 = StaInfo1.read(data);
                }


                {//获取当前表单  mac/上下行量、状态
                    temp = telnetManager.sendCommand(CMD_GET_STA_LIST_2p4G);
                    arrStaInfo = StaInfo.load(temp);
                    temp = telnetManager.sendCommand(CMD_GET_STA_LIST_5G);
                    ArrayList<StaInfo> arrStaInfo_2 = StaInfo.load(temp);
                    for (StaInfo staInfo : arrStaInfo_2) {
                        staInfo.is5g = true;
                    }
                    if (arrStaInfo_2 != null)
                        arrStaInfo.addAll(arrStaInfo_2);
                }

                //填充IP和name和租期
                for (StaInfo1 info1 : arrStaInfo1) {
                    for (StaInfo info : arrStaInfo) {
                        if (info1.mac == info.getMAC()) {
                            info.ip = info1.ip;
                            info.name = info1.name;
                            break;
                        }
                    }
                }

                // 速度计算,比较
                if (!mArrSta.isEmpty()) {
                    final long interval2 = System.currentTimeMillis() - lastLoadTime;
                    lastLoadTime = System.currentTimeMillis();

                    Arrays.fill(tmpBytesAll, 0);

                    for (StaInfo b : arrStaInfo) {
                        tmpBytesAll[0] += b.getTx_bytes();
                        tmpBytesAll[1] += b.getRx_bytes();
                        if (b.is5g()) {
                            tmpBytesAll[4] += b.getTx_bytes();
                            tmpBytesAll[5] += b.getRx_bytes();
                        } else {
                            tmpBytesAll[2] += b.getTx_bytes();
                            tmpBytesAll[3] += b.getRx_bytes();
                        }
                        for (StaInfo a : mArrSta) {
                            if (a.equals(b)) {
                                b.speedTx = (int) ((b.getTx_bytes() - a.getTx_bytes()) * 1000 / interval2);
                                b.speedRx = (int) ((b.getRx_bytes() - a.getRx_bytes()) * 1000 / interval2);
                                break;
                            }
                        }
                    }

                    for (int i = 0; i < tmpBytesAll.length; i++) {
                        speedAll[i] = (tmpBytesAll[i] - bytesAll[i]) * 1000 / interval2;
                    }
                    System.arraycopy(tmpBytesAll, 0, bytesAll, 0, tmpBytesAll.length);

                    // 刷新Ui
                    mViewTxSpeed.setText(Tool.getSpeedStringBit(speedAll[1]));
                    mViewRxSpeed.setText(Tool.getSpeedStringBit(speedAll[0]));
                    mViewTxSpeed2$4G.setText(Tool.getSpeedStringBit(speedAll[3]));
                    mViewRxSpeed2$4G.setText(Tool.getSpeedStringBit(speedAll[2]));
                    mViewTxSpeed5G.setText(Tool.getSpeedStringBit(speedAll[5]));
                    mViewRxSpeed5G.setText(Tool.getSpeedStringBit(speedAll[4]));
                } else {
                    Arrays.fill(speedAll, 0);
                    for (StaInfo b : arrStaInfo) {
                        bytesAll[0] += b.getTx_bytes();
                        bytesAll[1] += b.getRx_bytes();
                        if (b.is5g()) {
                            bytesAll[4] += b.getTx_bytes();
                            bytesAll[5] += b.getRx_bytes();
                        } else {
                            bytesAll[2] += b.getTx_bytes();
                            bytesAll[3] += b.getRx_bytes();
                        }
                    }
                }

                mArrSta.clear();
                mArrSta.addAll(arrStaInfo);

                ((AbstractTableModel) table.getModel()).fireTableDataChanged();

                if (fileWriter != null)
                    try {
                        fileWriter.write(Tool.getTime());
                        fileWriter.write(':');
                        fileWriter.write(' ');
                        fileWriter.write(temp);
                        fileWriter.write('\n');
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                costTime = System.currentTimeMillis() - costTime;
                if (costTime < interval)
                    try {
                        Thread.sleep(interval - costTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        stopPlay();
        //输出结束信息并关闭输出流
        if (fileWriter != null)
            try {
                fileWriter.write("----------------End----------------\n");
                fileWriter.write('\n');
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        telnetManager.disconnect();
        Toolkit.getDefaultToolkit().beep();
    }

    public static void main(String[] args) {
        float level = Tool.getScreenSizeLevel();
        if (level != 1)
            Tool.setGlobalFontRelative(level);

        new StaListMain();

//        test();
    }

//    public static final void test() {
//        TelnetClientHelper telnetManager = null;
//        long interval = 1000;
//        try {
//            telnetManager = new TelnetClientHelper("192.168.2.1", 23);
//            Tool.log("login: " + telnetManager.login("root", "admin2020@ch"));
//        } catch (Exception e) {
//            e.printStackTrace();
//            // 初始化telnet失败，输出结束信息，并关闭输出流，返回
//            Toolkit.getDefaultToolkit().beep();
//            return;
//        }
//
//        // 启动按钮命令是 ACTION_PAUSE 并且 要运行的命令集合非空，执行循环
////        while (System.currentTimeMillis() < Long.MAX_VALUE) {
////            long costTime = System.currentTimeMillis();//每次循环消耗时间
////            Tool.log("----------------round---------------------");
//
////        String temp = telnetManager.sendCommand(CMD_GET_STA_NAME);
////        System.out.println(temp.getBytes().length);
////        System.out.println(Arrays.toString(temp.getBytes()));
//
////        byte[] arr = telnetManager.sendCommandForBytes(CMD_GET_STA_NAME);
////        System.out.println(Arrays.toString(CMD_GET_STA_NAME.getBytes()));
////        System.out.println(arr.length);
////        System.out.println(Arrays.toString(arr));
////        ArrayList<StaInfo1> arrStaInfo1 = StaInfo1.read(arr);
////        for (StaInfo1 info1 : arrStaInfo1) {
////            System.out.println(info1);
////        }
////        Tool.showByteArrayData(arr);
//
////            costTime = System.currentTimeMillis() - costTime;
////            if (costTime < interval)
////                try {
////                    Thread.sleep(interval - costTime);
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
////        }
//
//        String temp = telnetManager.sendCommand(CMD_GET_STA_NAME);
////        System.out.println(temp);
//        byte[] data = Tool.analysisHexDump(temp);
////        System.out.println(Integer.toHexString(data.length));
////        Tool.showByteArrayData(data);
//        ArrayList<StaInfo1> arrStaInfo1 = StaInfo1.read(data);
//
//
//        temp = telnetManager.sendCommand(CMD_GET_STA_LIST_2p4G);
//        ArrayList<StaInfo> arrStaInfo = StaInfo.load(temp);
//        temp = telnetManager.sendCommand(CMD_GET_STA_LIST_5G);
//        ArrayList<StaInfo> arrStaInfo_2 = StaInfo.load(temp);
//        for (StaInfo staInfo : arrStaInfo_2) {
//            staInfo.is5g = true;
//        }
//        if (arrStaInfo_2 != null)
//            arrStaInfo.addAll(arrStaInfo_2);
//
//        for (StaInfo1 info1 : arrStaInfo1) {
//            System.out.println(info1);
//            for (StaInfo info : arrStaInfo) {
//                if (info1.mac == info.getMAC()) {
//                    System.out.println("====> mac = " + Tool.turnMacString(info.getMAC()));
//                    info.ip = info1.ip;
//                    info.name = info1.name;
//                    break;
//                }
//            }
//        }
//
//        System.out.println();
//
//        for (StaInfo info : arrStaInfo) {
//            System.out.println(info);
//        }
//
//        telnetManager.disconnect();
//        Toolkit.getDefaultToolkit().beep();
//    }

}
