package com.changhong.telnettool.function.cpu;

import com.changhong.telnettool.component.PercentCycleCanvas;
import com.changhong.telnettool.database.SQLiteJDBC;
import com.changhong.telnettool.dialog.AlertDialog;
import com.changhong.telnettool.dialog.ShowDbDialog;
import com.changhong.telnettool.dialog.WebInterfaceDialog;
import com.changhong.telnettool.event.MyWindowAdapter;
import com.changhong.telnettool.event.PositiveNumberTextLimitListener;
import com.changhong.telnettool.event.TextInputLengthLimitListener;
import com.changhong.telnettool.net.TelnetClientHelper;
import com.changhong.telnettool.tool.DataManager;
import com.changhong.telnettool.tool.IOUtils;
import com.changhong.telnettool.tool.Preferences;
import com.changhong.telnettool.tool.Tool;
import com.changhong.telnettool.webinterface.Observer;
import javafx.util.Pair;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/***
 * 你可以先把框架写好，比如： 有一个大功能点是 循环读取信息并写入日志，这个功能支持 命令手动添加 你给一个接口，这个接口允许使用者 通过你的界面添加 删除
 * 修改 命令，并可以配置每条命令的执行间隔时间（以秒为单位），你通过telnet发送，再把 回显内容打印到界面并间隔10或者30秒左右 写入日志 比如
 * 可以建立分组命令，每个组的循环时间是相同的，我有2个组，第一组有3条命令，间隔10秒执行一次，把结果按照 时间：结果1，结果2，结果3
 * 回显到界面并可以手动选择导出日志，第二组有5条，间隔时间30秒执行一次，把结果按照 时间：结果1，结果2，结果3
 * ，4，5，回显到界面并可以手动选择导出日志 要注意处理的是，有些回显内容 只有一个数字 或者 一行字符串，有些内容可能是一堆字符串
 *
 * @author fuheng
 *
 */
public class CpuToolMain extends JFrame implements ActionListener, Runnable {

    /********序号*******/
    private static final long serialVersionUID = 7745711326889285938L;
    private static final String TITLE_FRAME = "任务管理器";

    private static final String CMD_TOP = "top -b -n 1";
    private static final String CMD_CPUINFO = "cat /proc/cpuinfo";
    private static final String CMD_UPTIME2 = "cat /proc/uptime";
    private static final String[] TITLE_PROCESS = {"名称", "状态", "PID", "PPID", "CPU(%)", "内存(%)", "内存", "用户"};

    private static final String ACTION_PLAY = "START";
    private static final String ACTION_PAUSE = "PAUSE";
    private static final String ACTION_EXPORT = "导出";
    private static final String ACTION_SHOW = "查看记录";
    private static final String ACTION_SETTING = "设置";

    private static final String ACTION_WEB_INTERFACE = "TOOL_WEB_INTERFACE";
    private static final int DEFAULT_INTERVAL = 10000;
    private static final String DEFAULT_DB_NAME = "resource.db";

    private static final String KEY_MONITOR_CMD = "cpu_mem_monitor_cmd";
    private static final String KEY_PORT = "cpu_mem_port";
    private static final String KEY_TELNET_ACCOUNT = "cpu_mem_telnet_account";
    private static final String KEY_TELNET_PASSWORD = "cpu_mem_telnet_password";
    private static final String KEY_THRESHOLD_MEM = "cpu_mem_threshold_mem";
    private static final String KEY_THRESHOLD_CPU = "cpu_mem_threshold_cpu";
    private static final String KEY_DB_NAME = "cpu_mem_output_db";
    private static final String KEY_INTERVAL = "cpu_mem_interval";

    //    private final JPanel mPanelTableProcess;
    private Button mBtnPlayOrPause;
    private TextField mViewPort;
    private TextField mViewIP;
    private TextField mViewInterval;
    private PercentCycleCanvas mProgressViewMem;
    private JPasswordField mViewPassword;
    private PercentCycleCanvas mProgressViewCpu;
    private TextField mViewAccount;
    private JTable table;
    private TextField mTvMenThreshold;
    private TextField mTvCpuThreshold;
    private TextField mViewOutPutField;
    private JButton mBtnExport;
    private JLabel mViewUpTime;

    /************* value *********************/
    private HashSet<String> mSetMonitorCmd = new HashSet<>();
    private TopInfo mTmpTopInfo;
    private SimpleDateFormat mSdf;
    private ShowDbDialog mShowDbDialog;
    private Thread mThread;
    private boolean isRunning;
    /**
     * 打开进程内存监控选择对话框的按钮
     */
    private JButton mBtnShowProcessChoseDialog;

    public CpuToolMain() {
        super(TITLE_FRAME);

        Pair<String, Integer> version = DataManager.getVersionInfo();
        if (version != null)
            setTitle(TITLE_FRAME + " v" + version.getKey());

        setSize(800, 600);

        setLayout(new BorderLayout());
        this.add(initOption(), BorderLayout.NORTH);
        this.add(initTableComponent(), BorderLayout.CENTER);
        this.add(initOutputBar(), BorderLayout.SOUTH);

        {
            String[] arr = Preferences.getInstance().readStringArray(KEY_MONITOR_CMD);
            if (arr != null)
                for (String s : arr) {
                    mSetMonitorCmd.add(s);
                }
        }
        addWindowListener(new MyWindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stopPlay();
                dispose();
                Tool.log(e);
                if (mThread != null && !mThread.isInterrupted()) {
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

    private Component initOption() {
        JPanel panel2 = new JPanel();
        BoxLayout boxLayout = new BoxLayout(panel2, BoxLayout.Y_AXIS);
        panel2.setLayout(boxLayout);
//        panel2.setLayout(new BorderLayout());
        {// ip port play in north
            mViewIP = new TextField(Tool.getGuessGateway());
            mViewIP.setColumns(25);
            mViewIP.addTextListener(new TextInputLengthLimitListener(15));
            mViewPort = new TextField(String.valueOf(Preferences.getInstance().readShort(KEY_PORT, (short) 23)));
            mViewPort.setColumns(5);
            mViewPort.addTextListener(new TextInputLengthLimitListener(5));
            mViewPort.addTextListener(new PositiveNumberTextLimitListener());

            mViewInterval = new TextField(String.valueOf(Preferences.getInstance().readInt(KEY_INTERVAL, DEFAULT_INTERVAL / 1000)));
            mViewInterval.setColumns(4);
            mViewInterval.addTextListener(new TextInputLengthLimitListener(4));
            mViewInterval.addTextListener(new PositiveNumberTextLimitListener());

            mBtnPlayOrPause = new Button(ACTION_PLAY);
            mBtnPlayOrPause.setActionCommand(ACTION_PLAY);
            mBtnPlayOrPause.addActionListener(this);
            JPanel panel = new JPanel();
            panel.setBorder(BorderFactory.createTitledBorder("服务地址和端口设置"));
            panel.add(new Label("IP:"));
            panel.add(mViewIP);
            panel.add(new Label("  port:"));
            panel.add(mViewPort);

            panel.add(new JLabel("  刷新间隔:"));
            panel.add(mViewInterval);
            panel.add(new JLabel("秒"));
            panel.add(mBtnPlayOrPause);

            panel2.add(panel);
        }
        {// ip port play in north
            mViewAccount = new TextField(Preferences.getInstance().readString(KEY_TELNET_ACCOUNT, "root"));
            mViewAccount.setColumns(25);
            mViewAccount.addTextListener(new TextInputLengthLimitListener(25));
            mViewPassword = new JPasswordField(Preferences.getInstance().readString(KEY_TELNET_PASSWORD, "admin2020@ch"));
            mViewPassword.setColumns(15);

            JPanel panel = new JPanel();
            panel.setBorder(BorderFactory.createTitledBorder("用户名和密码"));
            panel.add(new Label("User:"));
            panel.add(mViewAccount);
            panel.add(new Label("  password:"));
            panel.add(mViewPassword);

            panel2.add(panel);
        }

        // cup and memory show
        panel2.add(initCpuMemShow());

        return panel2;
    }

    /**
     * @return 初始化表格组件
     */
    private final Component initTableComponent() {
        table = new JTable(new MyTableMode());
        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
        table.setRowSorter(sorter);
        table.setRowHeight(table.getFont().getSize());
        JScrollPane scrollpane = new JScrollPane(table);
        scrollpane.setBorder(BorderFactory.createTitledBorder("线程表"));

        {
            int fontSize = table.getFont().getSize();
            TableColumn column = table.getColumnModel().getColumn(0);
            column.setMaxWidth(fontSize * 6);
            column.setMinWidth(fontSize * 2);
            DefaultTableCellRenderer render = new DefaultTableCellRenderer();
            render.setHorizontalAlignment(SwingConstants.CENTER);
            render.setForeground(Color.white);
            render.setBackground(Color.darkGray);
            column.setCellRenderer(render);
        }
        {
            TableColumn column = table.getColumnModel().getColumn(1);
            int size = table.getFont().getSize();
            column.setMinWidth(size * 16);
//            System.out.println(size);
        }
        {
            TableColumn column = table.getColumnModel().getColumn(2);
            DefaultTableCellRenderer render = new DefaultTableCellRenderer();
            render.setHorizontalAlignment(SwingConstants.CENTER);
            column.setCellRenderer(render);
        }

        {
            TableColumn column = table.getColumnModel().getColumn(8);
            DefaultTableCellRenderer render = new DefaultTableCellRenderer();
            render.setHorizontalAlignment(SwingConstants.CENTER);
            column.setCellRenderer(render);
        }
        return scrollpane;
    }

    /**
     * @return 初始化cpu和内存显示组件
     */
    private final Component initCpuMemShow() {
        JPanel panel = new JPanel();

        {//上电时间
            JPanel jPanel = new JPanel(new GridLayout(2, 1));
            {
                mViewUpTime = new JLabel("", JLabel.CENTER);
                mViewUpTime.setBorder(BorderFactory.createTitledBorder("上电时间"));
                mViewUpTime.setVisible(false);
                jPanel.add(mViewUpTime);
            }
            {
                Image image = IOUtils.loadLocalImage("res/ic_setting.png");
                ImageIcon icon = new ImageIcon(image.getScaledInstance(15, 15, Image.SCALE_SMOOTH));
                mBtnShowProcessChoseDialog = new JButton("内存监控设置", icon);
                mBtnShowProcessChoseDialog.setActionCommand(ACTION_SETTING);
                mBtnShowProcessChoseDialog.addActionListener(this);
                mBtnShowProcessChoseDialog.setVisible(false);
                jPanel.add(mBtnShowProcessChoseDialog);
            }
            panel.add(jPanel);
        }
        panel.setBorder(BorderFactory.createTitledBorder("资源情况"));
        mProgressViewCpu = new PercentCycleCanvas("cpu使用率");
        mProgressViewMem = new PercentCycleCanvas("内存使用率");
        panel.add(mProgressViewCpu);
        panel.add(mProgressViewMem);
        float level = Tool.getScreenSizeLevel();
        if (level >= 2.5f) {
            mProgressViewCpu.setPreferredSize(new Dimension(400, 400));
            mProgressViewMem.setPreferredSize(new Dimension(400, 400));
        } else if (level >= 2) {
            mProgressViewCpu.setPreferredSize(new Dimension(240, 240));
            mProgressViewMem.setPreferredSize(new Dimension(240, 240));
        } else if (level >= 1.5f) {
            mProgressViewCpu.setPreferredSize(new Dimension(200, 200));
            mProgressViewMem.setPreferredSize(new Dimension(200, 200));
        }

        {//阈值设置
            JPanel panel1 = new JPanel(new GridLayout(4, 1));
            panel1.setBorder(BorderFactory.createTitledBorder("阈值(%)"));
            mTvMenThreshold = new TextField(String.valueOf(Preferences.getInstance().readByte(KEY_THRESHOLD_MEM, (byte) 30)));
            mTvMenThreshold.addTextListener(new TextInputLengthLimitListener(2));
            mTvMenThreshold.addTextListener(new PositiveNumberTextLimitListener());
            mTvCpuThreshold = new TextField(String.valueOf(Preferences.getInstance().readByte(KEY_THRESHOLD_CPU, (byte) 10)));
            mTvCpuThreshold.addTextListener(new TextInputLengthLimitListener(2));
            mTvCpuThreshold.addTextListener(new PositiveNumberTextLimitListener());
            panel1.add(new JLabel("CPU阈值："));
            panel1.add(mTvCpuThreshold);
            panel1.add(new JLabel("内存阈值："));
            panel1.add(mTvMenThreshold);

            panel.add(panel1);
        }

        return panel;
    }

    /**
     * @return 初始化输出栏
     */
    private Component initOutputBar() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("输出设置："));
        panel.add(new JLabel("输出路径:"));
        mViewOutPutField = new TextField(Preferences.getInstance().readString(KEY_DB_NAME, DEFAULT_DB_NAME));
        mViewOutPutField.setColumns(60);
        panel.add(mViewOutPutField);
        mBtnExport = new JButton(ACTION_EXPORT);
        mBtnExport.setActionCommand(ACTION_EXPORT);
        mBtnExport.addActionListener(this);
        panel.add(mBtnExport);

        JButton jButton = new JButton(ACTION_SHOW);
        jButton.setActionCommand(ACTION_SHOW);
        jButton.addActionListener(this);
        panel.add(jButton);

        return panel;
    }

    private void startPlay() {

        mBtnPlayOrPause.setLabel(ACTION_PAUSE);
        mBtnPlayOrPause.setActionCommand(ACTION_PAUSE);

        mViewIP.setEnabled(false);
        mViewPort.setEnabled(false);
        mViewAccount.setEnabled(false);
        mViewPassword.setEnabled(false);
        mViewInterval.setEnabled(false);
        String interval = mViewInterval.getText();
        if (interval.isEmpty() || interval.trim().isEmpty() || interval.indexOf('0') == 0)
            mViewInterval.setText(String.valueOf(DEFAULT_INTERVAL / 1000));

        mViewOutPutField.setEnabled(false);
        String outPath = mViewOutPutField.getText();
        if (outPath.length() == 0) {
            mViewOutPutField.setText(DEFAULT_DB_NAME);
        } else if (!outPath.endsWith(".db")) {
            mViewOutPutField.setText(outPath + ".db");
        }
        mBtnExport.setEnabled(false);

        mTvMenThreshold.setEnabled(false);
        if (mTvMenThreshold.getText().trim().length() < 2) {
            mTvMenThreshold.setText("30");
        }

        mTvCpuThreshold.setEnabled(false);
        if (mTvCpuThreshold.getText().trim().length() < 2) {
            mTvCpuThreshold.setText("10");
        }

        //如果更改过输出数据库文件，则清除旧的数据显示窗口。
        if (!mViewOutPutField.getText().equals(Preferences.getInstance().readString(KEY_DB_NAME) != null)) {
            mShowDbDialog = null;
        }

        //保存配置
        Preferences.getInstance().saveShort(KEY_PORT, Short.parseShort(mViewPort.getText()))
                .saveInt(KEY_INTERVAL, Integer.parseInt(interval))
                .saveString(KEY_TELNET_ACCOUNT, mViewAccount.getText())
                .saveString(KEY_TELNET_PASSWORD, new String(mViewPassword.getPassword()))
                .saveString(KEY_DB_NAME, mViewOutPutField.getText())
                .saveByte(KEY_THRESHOLD_CPU, Byte.valueOf(mTvCpuThreshold.getText()))
                .saveByte(KEY_THRESHOLD_MEM, Byte.valueOf(mTvMenThreshold.getText()))
                .commit();

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
        isRunning = true;
        mThread.start();
    }

    private void stopPlay() {
        mBtnPlayOrPause.setLabel(ACTION_PLAY);
        mBtnPlayOrPause.setActionCommand(ACTION_PLAY);

        mViewIP.setEnabled(true);
        mViewPort.setEnabled(true);
        mViewInterval.setEnabled(true);

        mViewAccount.setEnabled(true);
        mViewPassword.setEnabled(true);

        mViewOutPutField.setEnabled(true);
        mBtnExport.setEnabled(true);

        mTvMenThreshold.setEnabled(true);
        mTvCpuThreshold.setEnabled(true);

        mViewUpTime.setVisible(false);
        mBtnShowProcessChoseDialog.setVisible(false);

        isRunning = false;
        if (mThread != null && !mThread.isInterrupted()) {
            mThread.interrupt();
            mThread = null;
        }
    }

    private void showFileDialog2ChooseFile() {
        FileDialog fileDialog = new FileDialog(this, "Set out put path……", FileDialog.SAVE);
        fileDialog.setFile("*.db;");
        fileDialog.addComponentListener(new ComponentListener() {

            @Override
            public void componentShown(ComponentEvent e) {
                Tool.log("cmponentShown   ====>   " + e.toString());
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


    private void showSettingDialog() {
        if (mTmpTopInfo == null || mTmpTopInfo.getArrProcess() == null || mTmpTopInfo.getArrProcess().isEmpty())
            return;

        ArrayList<ProcessInfo> arr = new ArrayList<>();
        for (ProcessInfo process : mTmpTopInfo.getArrProcess()) {//不添加当前top命令
            if (CMD_TOP.equals(process.command))
                continue;
            arr.add(process);
        }
        String[] choices = new String[arr.size()];//选项
        boolean[] arrChecked = new boolean[arr.size()];//是否被选中
        for (int i = 0; i < arr.size(); i++) {
            ProcessInfo item = arr.get(i);
            choices[i] = item.getCommand();// + "    " + item.getPID();
            arrChecked[i] = mSetMonitorCmd.contains(item.getCommand());
        }
        SelectDialog selectDialog = new SelectDialog(this, "请选择需要监控内存的进程", choices, arrChecked, new Observer<Set<String>>() {
            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onComplete() {
            }

            @Override
            public void onNext(Set<String> result) {
                mSetMonitorCmd.clear();
                Iterator<String> iterator = result.iterator();
                while (iterator.hasNext()) {
                    mSetMonitorCmd.add(iterator.next());
                }
                String[] arr = new String[mSetMonitorCmd.size()];
                mSetMonitorCmd.toArray(arr);
                Preferences.getInstance().saveStringArray(KEY_MONITOR_CMD, arr).commit();
//                System.out.println("result = " + result + "\nmSetMonitorCmd = " + mSetMonitorCmd);
            }
        });
        selectDialog.setVisible(true);
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
//            mTopReceiver.stop();
            stopPlay();
        }//export
        else if (e.getActionCommand().equals(ACTION_EXPORT)) {
            showFileDialog2ChooseFile();
        } else if (e.getActionCommand().equals(ACTION_SETTING)) {
            showSettingDialog();
        }

        // {@link #initToolBar()}
        else if (e.getActionCommand().equals(ACTION_WEB_INTERFACE)) {
            WebInterfaceDialog webInterfaceDialog = new WebInterfaceDialog(this);
            webInterfaceDialog.setVisible(true);
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
                    mShowDbDialog = new ShowDbDialog(this, "查看数据库", outPath.substring(0, outPath.length() - 3),
                            ProcessOnOfflineEvent.class, ExceedThresholdEvent.class, ProcessMemChangeEvent.class);
                mShowDbDialog.setVisible(true);
            }
        }
    }


    @Override
    public void run() {
        String IP = mViewIP.getText();
        String USER = mViewAccount.getText();
//        System.out.println(USER);
        String PASSWORD = new String(mViewPassword.getPassword());
//        System.out.println(PASSWORD);
        int port = Tool.turnString2Int(mViewPort.getText(), 23);


        final int refreshInterval;//刷新间隔
        {
            int interval = Tool.turnString2Int(mViewInterval.getText(), 0);//每次请求间隔 <=1s
            if (interval == 0)
                interval = DEFAULT_INTERVAL;
            else interval *= 1000;
            refreshInterval = interval;
        }
        long lastRunTime = refreshInterval;//计算上次运行top的时间，运行后，被赋值为0，等待值超过 refreshInterval

        int memThreshold = Integer.parseInt(mTvMenThreshold.getText());
        int cpuThreshold = Integer.parseInt(mTvCpuThreshold.getText());

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
        SQLiteJDBC databaseOffline = new SQLiteJDBC(outPath, ProcessOnOfflineEvent.class);
        SQLiteJDBC databaseExceed = new SQLiteJDBC(outPath, ExceedThresholdEvent.class);
        SQLiteJDBC databaseMemChange = new SQLiteJDBC(outPath, ProcessMemChangeEvent.class);

        //输出开头信息
        Toolkit.getDefaultToolkit().beep();
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
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        final long startTime = System.currentTimeMillis();//程序启动计时时间
//        long uptime = getUpTime(telnetManager);//获取上电时间（分）
        final long uptime = getUpTime2(telnetManager);//获取上电时间（毫秒）
        mViewUpTime.setVisible(true);
        mViewUpTime.setText(turnLong2Time(uptime));
//        String cpuinfo = telnetManager.sendCommand(CMD_CPUINFO);
//        Tool.log("cpu = " + cpuinfo);

        while (isRunning) {
            long costTime = System.currentTimeMillis();//每次循环消耗时间
            long rUptime = costTime - startTime + uptime;
            {
                String strUptime = turnLong2Time(rUptime);
                mViewUpTime.setText(strUptime);
            }
            if (isRunning && lastRunTime >= refreshInterval) {
                System.out.println(getClass().getSimpleName() + " , ----------------round  " + mViewUpTime.getText() + "(上电时间)  " + DateFormat.getTimeInstance().format(new Date()) + "(系统时间) ---------------------");
                lastRunTime = 0;
                String temp = telnetManager.sendCommand(CMD_TOP);
                TopInfo info = new TopInfo(temp);
                mProgressViewMem.setPercent(info.getMemUseInfo().getUsedPercent());
                mProgressViewCpu.setPercent(info.getCpuUsed());
                Tool.log("cpu info= " + info.getCpuUseInfo());
//            resetAllRow(info.getArrProcess());

                if (!mBtnShowProcessChoseDialog.isVisible() && info.getArrProcess() != null && !info.getArrProcess().isEmpty())
                    mBtnShowProcessChoseDialog.setVisible(true);

                //对比和存储
                compareProgress(databaseOffline, databaseExceed, databaseMemChange,
                        info, memThreshold, cpuThreshold, (int) rUptime);

                mTmpTopInfo = info;
                ((AbstractTableModel) table.getModel()).fireTableDataChanged();
            }

            if (isRunning)
                try {
                    long cost = System.currentTimeMillis() - costTime;
                    Thread.sleep(cost > 1000 ? 1000 : cost);
                    lastRunTime += System.currentTimeMillis() - costTime;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
        stopPlay();
        telnetManager.disconnect();
        Toolkit.getDefaultToolkit().beep();
    }

    private void compareProgress(SQLiteJDBC databaseOffline, SQLiteJDBC databaseExceed, SQLiteJDBC databaseMemChange, TopInfo info, int memThreshold, int cpuThreshold, int rUptime) {
        long time = System.currentTimeMillis();//getCurrentTime();
        //监控某条进程内存变化
        HashMap<String, ProcessInfo> mapNewProcesses = new HashMap<>();

        //存储上线和超过阈值
        for (ProcessInfo arrProcess : info.getArrProcess()) {
            if (CMD_TOP.equals(arrProcess.command))//排除掉当前Top命令
                continue;
            if (arrProcess.getpMem() > memThreshold) {
                databaseExceed.insert(new ExceedThresholdEvent(arrProcess, time, rUptime, false));
            }
            if (arrProcess.getpCpu() > cpuThreshold) {
                databaseExceed.insert(new ExceedThresholdEvent(arrProcess, time, rUptime, true));
            }

            if (mSetMonitorCmd.contains(arrProcess.getCommand())) {
                mapNewProcesses.put(arrProcess.getCommand(), arrProcess);
            }
        }

        if (mTmpTopInfo == null) {
            return;
        }

        //监控某项进程内存变化
        if (!mapNewProcesses.isEmpty())
            for (ProcessInfo process : mTmpTopInfo.getArrProcess()) {
                String cmd = process.getCommand();
                if (mSetMonitorCmd.contains(cmd) && mapNewProcesses.containsKey(cmd)) {
                    if (mapNewProcesses.get(cmd).getVSZ() != process.getVSZ()) {
                        databaseMemChange.insert(new ProcessMemChangeEvent(mapNewProcesses.get(cmd), time, rUptime));
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
                if (CMD_TOP.equals(arrOnOffline.get(i).command))
                    arrOnOffline.remove(i--);
                else if (mSetMonitorCmd.contains(arrOnOffline.get(i).getCommand()))//当上线进程处于被监控列表时，才写入数据库
                    databaseOffline.insert(new ProcessOnOfflineEvent(arrOnOffline.get(i), time, rUptime, true));
            }
        }

        // 记录下线进程
        arrOnOffline.clear();
        arrOnOffline.addAll(mTmpTopInfo.getArrProcess());
        diffSet = arrOnOffline.removeAll(info.getArrProcess());
        if (diffSet) {
            for (int i = 0; i < arrOnOffline.size(); i++) {//排除掉当前Top命令
                if (CMD_TOP.equals(arrOnOffline.get(i).command))
                    arrOnOffline.remove(i--);
                else if (mSetMonitorCmd.contains(arrOnOffline.get(i).getCommand()))//当下线进程处于被监控列表时，才写入数据库
                    databaseOffline.insert(new ProcessOnOfflineEvent(arrOnOffline.get(i), time, rUptime, false));
            }
        }
    }


    private class MyTableMode extends AbstractTableModel {

        public int getColumnCount() {
            return TITLE_PROCESS.length + 1;
        }

        public int getRowCount() {
            return mTmpTopInfo == null ? 0 : mTmpTopInfo.getArrProcess().size();
        }

        @Override
        public String getColumnName(int column) {
            if (column == 0)
                return "序号";
            return TITLE_PROCESS[column - 1];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0)
                return Integer.class;
            switch (columnIndex - 1) {
                case 0:
                case 1:
                case 7:
                    return String.class;
                case 2:
                case 3:
                case 6:
                    return Integer.class;
                case 4:
                case 5:
                    return Float.class;
                default:
                    return Object.class;
            }
        }

        public Object getValueAt(int row, int col) {
            if (col == 0)
                return row + 1;
            ProcessInfo process = mTmpTopInfo.getArrProcess().get(row);
            switch (col - 1) {
                case 0:
                    return process.getCommand();
                case 1:
                    return process.getSTAT();
                case 2:
                    return process.getPID();
                case 3:
                    return process.getPPID();
                case 4:
                    return process.getpCpu();
                case 5:
                    return process.getpMem();
                case 6:
                    return process.getVSZ();
                case 7:
                    return process.getUSER();
                default:
                    return null;
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
        new CpuToolMain();
//        System.out.println(DateFormat.getTimeInstance().format(new Date()));
//        System.out.println(DateFormat.getTimeInstance(0).format(new Date()));
//        System.out.println(DateFormat.getTimeInstance(1).format(new Date()));
//        System.out.println(DateFormat.getTimeInstance(2).format(new Date()));
//        System.out.println(DateFormat.getTimeInstance(3).format(new Date()));
//
//        System.out.println(DateFormat.getDateInstance().format(new Date()));
//        System.out.println(DateFormat.getDateInstance(0).format(new Date()));
//        System.out.println(DateFormat.getDateInstance(1).format(new Date()));
//        System.out.println(DateFormat.getDateInstance(2).format(new Date()));
//        System.out.println(DateFormat.getDateInstance(3).format(new Date()));
//        SQLiteJDBC databaseOffline = new SQLiteJDBC("resource", ProcessOnOfflineEvent.class);
//        Map map = databaseOffline.select();
//        System.out.println(map);
//        SQLiteJDBC databaseExceed = new SQLiteJDBC("123.db", ExceedThresholdEvent.class);
    }
}