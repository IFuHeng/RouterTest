package com.changhong.telnettool.function.wlan;

import com.changhong.telnettool.been.CommandBeen;
import com.changhong.telnettool.been.WifiStateBeen;
import com.changhong.telnettool.dialog.SpeedShowDialog;
import com.changhong.telnettool.dialog.TemperatureShowDialog;
import com.changhong.telnettool.dialog.WebInterfaceDialog;
import com.changhong.telnettool.event.PositiveNumberTextLimitListener;
import com.changhong.telnettool.event.TextInputLengthLimitListener;
import com.changhong.telnettool.net.TelnetClientHelper;
import com.changhong.telnettool.tool.DataManager;
import com.changhong.telnettool.tool.Tool;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
public class WlanRuntimeMain extends JFrame implements ActionListener, WindowStateListener, Runnable, ItemListener {

    /********序号*******/
    private static final long serialVersionUID = 7745711326889285938L;

    private static final String ACTION_PLAY = "PLAY";
    private static final String ACTION_PAUSE = "PAUSE";
    private static final String ACTION_ADD = "ADD";
    private static final String ACTION_DELETE = "DELETE";
    private static final String ACTION_EXPORT = "EXPORT";
    private static final String ACTION_WEB_INTERFACE = "TOOL_WEB_INTERFACE";
    private static final int DEFAULT_INTERVAL = 1;

    private final JPanel mViewListCommand;
    private final GridBagConstraints mGBC = new GridBagConstraints();
    private final GridBagLayout mGridBagLayout = new GridBagLayout();

    private final Button mBtnPlayOrPause;
    private final TextField mViewPort;
    private final TextField mViewIP;
    private final TextField mViewAccount;
    private final JPasswordField mViewPassword;
    private TextField mViewCmdAdd;
    private final TextField mViewInterval;
    private TextArea mViewResponse;
    private Button mBtnAdd;

    /************* value *********************/
    private List<CommandBeen> mDataCommand = new ArrayList<>();
    private final List<Button> mListButtonDel = new ArrayList<>();
    private final List<Checkbox> mListCheckboxEnable = new ArrayList<>();
    private final List<Checkbox> mListCheckboxUnlimited = new ArrayList<>();
    private ExecutorService mThreadPool;

    private TextField mViewOutPutField;
    private Button mBtnExport;

    private static final String CMD_PREPARE = "killall -9 ther_control;" +
            "setmib dbg 1;" +
            "setmib1 dbg 1;" +
            "echo -e '/*id,    max,  hi,   low,  funcoff, txduty, path, power*/;wlan0    200   200   200   0        0       0     0;wlan1    200   200   200   0        0       0     0' > /var/ther/conf;" +
            "ther_control &";
    private static final String CMD_LOAD_TX_SPEED = "cat /proc/wlan0/sta_info | grep tx_bytes |grep -v \" -99\"";
    private static final String CMD_LOAD_RX_SPEED = "cat /proc/wlan0/sta_info | grep rx_bytes |grep -v \" -99\"";

    public WlanRuntimeMain() {
        super("WIFI自动化工具");

        Pair<String, Integer> version = DataManager.getVersionInfo();
        if (version != null)
            setTitle("WIFI自动化工具 v" + version.getKey());

        setSize(800, 600);

        initToolBar();

        setLayout(new BorderLayout());
        {
            Panel panel1 = new Panel();
            panel1.setLayout(new BorderLayout());
            {// ip port play in north
                mViewIP = new TextField(Tool.getGuessGateway());
                mViewIP.setColumns(25);
                mViewIP.addTextListener(new TextInputLengthLimitListener(15));
                mViewPort = new TextField("23");
                mViewPort.setColumns(5);
                mViewPort.addTextListener(new TextInputLengthLimitListener(5));
                mViewPort.addTextListener(new PositiveNumberTextLimitListener());

                mViewInterval = new TextField("1");
                mViewInterval.addTextListener(new TextInputLengthLimitListener(3));
                mViewInterval.addTextListener(new PositiveNumberTextLimitListener());
                mViewInterval.setColumns(6);

                mBtnPlayOrPause = new Button(ACTION_PLAY);
                mBtnPlayOrPause.setActionCommand(ACTION_PLAY);
                mBtnPlayOrPause.addActionListener(this);
                JPanel panel = new JPanel();
                panel.setBorder(BorderFactory.createTitledBorder("服务地址和端口设置"));
                panel.add(new Label("IP:"));
                panel.add(mViewIP);
                panel.add(new Label("  port:"));
                panel.add(mViewPort);
                panel.add(new Label("  Interval of each round:"));
                panel.add(mViewInterval);
                panel.add(new Label("s"));

                panel.add(mBtnPlayOrPause);
                panel.setMinimumSize(new Dimension(600, 300));
                panel1.add(panel, BorderLayout.NORTH);
            }
            {// ip port play in north
                mViewAccount = new TextField("root");
                mViewAccount.setColumns(25);
                mViewAccount.addTextListener(new TextInputLengthLimitListener(25));
                mViewPassword = new JPasswordField();
                mViewPassword.setColumns(5);

                JPanel panel = new JPanel();
                panel.setBorder(BorderFactory.createTitledBorder("用户名和密码"));
                panel.add(new Label("User:"));
                panel.add(mViewAccount);
                panel.add(new Label("  password:"));
                panel.add(mViewPassword);
                panel1.add(panel, BorderLayout.SOUTH);
            }
            this.add(panel1, "North");
        }

        mViewListCommand = new JPanel(mGridBagLayout);

        this.add(initOutputBar(), BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
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
        addWindowStateListener(this);
        setResizable(false);
        setVisible(true);
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * 初始化菜单栏
     */
    private void initToolBar() {
        JMenuBar menuBar = new JMenuBar();
        {
            JMenu menu = new JMenu("工具");
            JMenuItem item = new JMenuItem("Web接口测试");
            item.setActionCommand(ACTION_WEB_INTERFACE);
            item.addActionListener(this);
            menu.add(item);
            menuBar.add(menu);
        }
        setJMenuBar(menuBar);
//        setMenuBar(menuBar);
    }

    private void initResultView() {
        Panel panel = new Panel(new BorderLayout());
        Label title = new Label("Response:");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        panel.add(title);
        mViewResponse = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
        mViewResponse.setEditable(false);
        mViewResponse.setBackground(Color.LIGHT_GRAY);
        panel.add(mViewResponse, "South");
        this.add(panel, "South");
    }

    private Component initOutputBar() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("输出设置："));
        panel.add(new Label("Output path:"));
        mViewOutPutField = new TextField();
        mViewOutPutField.setColumns(40);
        panel.add(mViewOutPutField);
        mBtnExport = new Button(ACTION_EXPORT);
        mBtnExport.setActionCommand(ACTION_EXPORT);
        mBtnExport.addActionListener(this);
        panel.add(mBtnExport);
        return panel;
    }

    /**
     * 创建表格中新的一行命令信息
     *
     * @param parent 表格
     * @param index  序号
     * @param been   命令对象
     */
    private void createNewCmdRow(JPanel parent, int index, CommandBeen been) {

        mGBC.fill = GridBagConstraints.NONE;
        mGBC.gridwidth = 1;

        Checkbox cb = new Checkbox("");
        cb.setState(been.isCheck);
        cb.addItemListener(this);
        cb.setName(String.valueOf(index));
        mListCheckboxEnable.add(cb);
        parent.add(cb);

        Label labelIndex = new Label(String.valueOf(index + 1));
        labelIndex.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        labelIndex.setForeground(new Color(Color.HSBtoRGB((float) Math.random(), 1, 1)));
        labelIndex.getFont().deriveFont(Font.BOLD);
        mGridBagLayout.setConstraints(labelIndex, mGBC);
        parent.add(labelIndex);

        mGBC.fill = GridBagConstraints.HORIZONTAL;
        JLabel labelCmd = new JLabel(Tool.turnTxt2HtmlFormat(been.command));
        mGridBagLayout.setConstraints(labelCmd, mGBC);
        parent.add(labelCmd);

        mGBC.fill = GridBagConstraints.NONE;

        Checkbox cbUnlimited = new Checkbox("∞");
        cbUnlimited.setState(been.isUnlimited);
        cbUnlimited.addItemListener(this);
        mListCheckboxUnlimited.add(cbUnlimited);
        parent.add(cbUnlimited);

        mGBC.gridwidth = GridBagConstraints.REMAINDER;
        Button button = new Button(ACTION_DELETE);
        button.setActionCommand(ACTION_DELETE);
        mListButtonDel.add(button);
        button.addActionListener(this);
        mGridBagLayout.setConstraints(button, mGBC);

        parent.add(button);
    }

    /**
     * 创建命令表格标题
     *
     * @param parent 表格
     ***/
    private void createCmdTitle(JPanel parent) {

        mGBC.fill = GridBagConstraints.NONE;
        mGBC.gridwidth = 1;

        JButton label1 = new JButton("选择");
        label1.setEnabled(false);
        label1.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        parent.add(label1);

        JButton label2 = new JButton("序号");
        label2.setEnabled(false);
        label2.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        mGridBagLayout.setConstraints(label2, mGBC);
        parent.add(label2);

        mGBC.fill = GridBagConstraints.HORIZONTAL;
        JButton label3 = new JButton("命令");
        label3.setEnabled(false);
        label3.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        mGridBagLayout.setConstraints(label3, mGBC);
        parent.add(label3);

        mGBC.fill = GridBagConstraints.NONE;

        JButton label4 = new JButton("无限");
        label4.setEnabled(false);
        label4.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        mGridBagLayout.setConstraints(label4, mGBC);
        parent.add(label4);

        mGBC.gridwidth = GridBagConstraints.REMAINDER;
        JButton label5 = new JButton("操作");
        label5.setEnabled(false);
        label5.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        mGridBagLayout.setConstraints(label5, mGBC);
        parent.add(label5);
    }

    /**
     * 重置下列表panel的尺寸
     */
    private void resetViewListCommandSize() {
        Component parent = mViewListCommand.getParent().getParent();
        Container grandParent = parent.getParent();
        int height = 0;
        for (Component child : grandParent.getComponents()) {
            if (child != parent)
                height += child.getHeight();
        }
        Rectangle bounds = parent.getBounds();
        bounds.height = grandParent.getHeight() - height;
        parent.setBounds(bounds);
    }

    private boolean isCommandListNotEmpty() {
        if (mDataCommand.isEmpty()) {
            return false;
        }

        for (CommandBeen commandBeen : mDataCommand) {
            if (commandBeen.isCheck)
                return true;
        }

        return false;
    }

    private void startPlay() {

        mViewResponse.setText(null);
        mBtnPlayOrPause.setLabel(ACTION_PAUSE);
        mBtnPlayOrPause.setActionCommand(ACTION_PAUSE);

        if (mThreadPool == null) {
            mThreadPool = Executors.newSingleThreadExecutor();
        }
        mViewIP.setEnabled(false);
        mViewPort.setEnabled(false);
        mViewInterval.setEnabled(false);
        mViewOutPutField.setEnabled(false);
        mBtnExport.setEnabled(false);
        for (Button button : mListButtonDel) {
            button.setEnabled(false);
        }
        for (Checkbox cb : mListCheckboxEnable) {
            cb.setEnabled(false);
        }
        for (Checkbox cb : mListCheckboxUnlimited) {
            cb.setEnabled(false);
        }

        mBtnAdd.setEnabled(false);
        mThreadPool.submit(this);
    }

    private void stopPlay() {
        mViewIP.setEnabled(true);
        mViewPort.setEnabled(true);
        mViewInterval.setEnabled(true);
        mViewOutPutField.setEnabled(true);
        mBtnExport.setEnabled(true);
        for (Button button : mListButtonDel) {
            button.setEnabled(true);
        }
        for (Checkbox cb : mListCheckboxEnable) {
            cb.setEnabled(true);
        }
        for (Checkbox cb : mListCheckboxUnlimited) {
            cb.setEnabled(true);
        }
        mBtnAdd.setEnabled(true);
        mBtnPlayOrPause.setLabel(ACTION_PLAY);
        mBtnPlayOrPause.setActionCommand(ACTION_PLAY);
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

    public static void main(String[] args) {
        new WlanRuntimeMain();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Tool.log(e.getActionCommand());

        // 删除
        if (e.getActionCommand().equals(ACTION_DELETE) && e.getSource() instanceof Button) {
            int column = mViewListCommand.getComponentCount() / (mListButtonDel.size() + 1);
            int index = mListButtonDel.indexOf(e.getSource());
            mDataCommand.remove(index);
            mListButtonDel.remove(index);
            for (int i = 0; i < column; ++i) {//删除
                Component view = mViewListCommand.getComponent((index + 1) * column);
                if (view instanceof Checkbox) {//删除勾选框
                    if (mListCheckboxEnable.contains(view))
                        mListCheckboxEnable.remove(view);
                    else mListCheckboxUnlimited.remove(view);
                }
                mViewListCommand.remove(view);
            }

            //刷新序号
            for (int i = index; i < mDataCommand.size(); i++) {
                Component view = mViewListCommand.getComponent(i * column);
                if (view instanceof Label) {
                    ((Label) view).setText(String.valueOf(i + 1));
                }
            }
            DataManager.save(mDataCommand);
            mViewListCommand.revalidate();
        }

        // 添加
        else if (e.getActionCommand().equals(ACTION_ADD)) {
            String cmd = mViewCmdAdd.getText();
            if (cmd == null || cmd.length() == 0 || cmd.trim().length() == 0)
                return;
            CommandBeen been = new CommandBeen(true, cmd, false);
            createNewCmdRow(mViewListCommand, mDataCommand.size(), been);
            mDataCommand.add(been);
            DataManager.save(mDataCommand);
            mViewListCommand.revalidate();
        }

        // play or pause
        else if (e.getActionCommand().equals(ACTION_PLAY)) {
            if (!Tool.isIpv4(mViewIP.getText())) {
                mViewIP.requestFocus();
                return;
            } else if (mViewPort.getText().isEmpty()) {
                mViewPort.requestFocus();
                return;
            } else if (mViewInterval.getText().isEmpty()) {
                mViewInterval.requestFocus();
                return;
            } else if (!isCommandListNotEmpty()) {
                mViewCmdAdd.requestFocus();
                return;
            }
            startPlay();
        } else if (e.getActionCommand().equals(ACTION_PAUSE)) {
            stopPlay();
        }

        //export
        else if (e.getActionCommand().equals(ACTION_EXPORT)) {
            showFileDialog2ChooseFile();
        }
        // {@link #initToolBar()}
        else if (e.getActionCommand().equals(ACTION_WEB_INTERFACE)) {
            WebInterfaceDialog webInterfaceDialog = new WebInterfaceDialog(this);
            webInterfaceDialog.setVisible(true);
        }
    }

    @Override
    public void windowStateChanged(WindowEvent e) {
        Tool.log(e);
        if ((e.getNewState() & Frame.MAXIMIZED_VERT) == Frame.MAXIMIZED_VERT) {
            resetViewListCommandSize();
        }
    }

    @Override
    public void run() {

        String IP = mViewIP.getText();
        int port = Tool.turnString2Int(mViewPort.getText(), 23);
        int interval = Tool.turnString2Int(mViewInterval.getText(), DEFAULT_INTERVAL);//每次请求间隔 <=1s
        String USER = mViewAccount.getText();
        String PASSWORD = new String(mViewPassword.getPassword());
        interval *= 1000;
        int column = mViewListCommand.getComponentCount() / (mListButtonDel.size() + 1);
        String outPath = mViewOutPutField.getText();

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
        mViewResponse.setText("----------------Start----------------\n");
        if (fileWriter != null)
            try {
                fileWriter.write("----------------Start----------------\n");
                fileWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        // 初始化telnet连接，并登录
        TelnetClientHelper telnetManager;
        try {
            telnetManager = new TelnetClientHelper(IP, port);
            mViewResponse.append(telnetManager.login(USER, PASSWORD));
        } catch (Exception e) {
            e.printStackTrace();
            // 初始化telnet失败，输出结束信息，并关闭输出流，返回
            stopPlay();
            mViewResponse.append(e.getMessage() + "\n");
            mViewResponse.append("----------------End----------------\n");
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
            return;
        }

        //将即将运行的命令装入集合
        List<CommandBeen> commandBeans = new ArrayList<>();
        for (CommandBeen commandBeen : mDataCommand) {
            if (commandBeen.isCheck)
                commandBeans.add(commandBeen);
        }

        int step = 0;
        // 启动按钮命令是 ACTION_PAUSE 并且 要运行的命令集合非空，执行循环
        while (mBtnPlayOrPause.getActionCommand().equals(ACTION_PAUSE) && !commandBeans.isEmpty()) {
            long costTime = System.currentTimeMillis();//每次循环消耗时间
            Tool.log("----------------round---------------------");

            CommandBeen commandBeen = commandBeans.get(step % commandBeans.size());
            //当前命令所在位置
            Component component = mViewListCommand.getComponent((mDataCommand.indexOf(commandBeen) + 1) * column + 2);
            Color originalForegroundColor = component.getForeground();// 原始文字颜色
            // 显示命令，背景设置蓝色
            component.setForeground(Color.blue);

            String cmd = commandBeen.command;
            if (cmd.contains("\\n"))
                cmd = cmd.replace("\\n", ";");
            String temp = telnetManager.sendCommand(cmd);
            try {
                testWlanSpeed(temp);
                testWlanTemperature(temp);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mViewResponse.append(temp);
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

            if (!commandBeen.isUnlimited)
                commandBeans.remove(commandBeen);
            else
                ++step;

            costTime = System.currentTimeMillis() - costTime;
            if (costTime < interval)
                try {
                    Thread.sleep(interval - costTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            //命令行所在view还原
            component.setForeground(originalForegroundColor);
        }

        stopPlay();
        //输出结束信息并关闭输出流
        mViewResponse.append("----------------End----------------\n");
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

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == null || !(e.getSource() instanceof Checkbox))
            return;

        Checkbox cb = (Checkbox) e.getSource();
        boolean isChecked = e.getStateChange() == ItemEvent.SELECTED;
//        Tool.log("isChecked=" + isChecked);
        if (mListCheckboxEnable.contains(cb)) {
            int index = mListCheckboxEnable.indexOf(cb);
            CommandBeen been = mDataCommand.get(index);
            if (been.isCheck != isChecked) {
                been.isCheck = isChecked;
                DataManager.save(mDataCommand);
            }
        } else if (mListCheckboxUnlimited.contains(cb)) {
            int index = mListCheckboxUnlimited.indexOf(cb);
            CommandBeen been = mDataCommand.get(index);
            if (been.isUnlimited != isChecked) {
                been.isUnlimited = isChecked;
                DataManager.save(mDataCommand);
            }
        }
    }


    private SpeedShowDialog mSpeedDialog;
    private TemperatureShowDialog mTemperatureShowDialog;
    private long mTxTemp;
    private long mRxTemp;
    private long mTimeTxLast;
    private long mTimeRxLast;

    private void testWlanSpeed(String str) {
        if (str == null || str.length() == 0 || !(str.contains("tx_bytes:") || str.contains("rx_bytes:")))
            return;

        boolean isRx = str.contains("rx_bytes");

        BufferedReader br = new BufferedReader(new StringReader(str));
        String temp;
        long value = 0;
        try {
            while ((temp = br.readLine()) != null) {
                if (isRx) {
                    if (temp.contains("rx_bytes"))
                        value += Long.parseLong(Tool.onlyDigit(temp));
                } else if (temp.contains("tx_bytes")) {
                    value += Long.parseLong(Tool.onlyDigit(temp));
                }
            }
            long speed;
            if (isRx) {
                speed = value - mRxTemp;
                long costTime = System.currentTimeMillis() - mTimeRxLast;
                mRxTemp = value;
                if (mTimeRxLast == 0) {
                    mTimeRxLast = System.currentTimeMillis();
                    return;
                } else
                    mTimeRxLast = System.currentTimeMillis();

                speed *= 1000;
                speed /= costTime;
            } else {
                speed = value - mTxTemp;
                long costTime = System.currentTimeMillis() - mTimeTxLast;
                mTxTemp = value;
                mTimeTxLast = System.currentTimeMillis();
                if (mTimeTxLast == 0) {
                    mTimeTxLast = System.currentTimeMillis();
                    return;
                } else
                    mTimeTxLast = System.currentTimeMillis();

                speed *= 1000;
                speed /= costTime;
            }

            if (mSpeedDialog == null) {
                mSpeedDialog = new SpeedShowDialog(this);
            }

            if (isRx)
                mSpeedDialog.appendDownloadSpeedNode((int) speed);
            else
                mSpeedDialog.appendUploadSpeedNode((int) speed);

            if (!mSpeedDialog.isVisible())
                mSpeedDialog.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void testWlanTemperature(String str) {
        if (str == null || str.length() == 0 || !(str.contains("wlan0:") || str.contains("wlan1:")) || !str.contains("Ther"))
            return;

        BufferedReader br = new BufferedReader(new StringReader(str));
        String temp;
        int tpr;
        try {
            while ((temp = br.readLine()) != null) {
                if (!temp.contains("Ther") || temp.contains("Clean Thermal") || temp.contains("Enter Thermal"))
                    continue;
                temp = temp.trim();
                boolean is5G = temp.contains("wlan0");
                boolean is2G = temp.contains("wlan1");

                tpr = new WifiStateBeen(temp).getCurTemp();

                if (mTemperatureShowDialog == null) {
                    mTemperatureShowDialog = new TemperatureShowDialog(this);
                }

//                System.out.println("temp = " + temp + ", tpr = " + tpr + ", is5G = " + is5G + ", is2G = " + is2G);
                if (is5G)
                    mTemperatureShowDialog.append5GTemperatureNode(tpr);
                else if (is2G)
                    mTemperatureShowDialog.append2GTemperatureNode(tpr);
            }
            if (mTemperatureShowDialog != null && !mTemperatureShowDialog.isVisible()) {
                mTemperatureShowDialog.setVisible(true);
                int screen_width = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth();
                mTemperatureShowDialog.setLocation(screen_width + mTemperatureShowDialog.getWidth() >> 1, 0);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}