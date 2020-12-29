package com.changhong.telnettool;

import com.changhong.telnettool.event.MyWindowAdapter;
import com.changhong.telnettool.function.cpu.CpuToolMain;
import com.changhong.telnettool.function.other.ResetExam;
import com.changhong.telnettool.function.sta.StaListMain2;
import com.changhong.telnettool.function.test_exam.Test2p2p8;
import com.changhong.telnettool.function.test_exam.Test2p2p9;
import com.changhong.telnettool.function.test_exam.TestWan;
import com.changhong.telnettool.tool.DataManager;
import com.changhong.telnettool.tool.Tool;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.HashMap;

public class MainMenu extends Frame implements ActionListener {

    private static final String ACTION_WLAN = "Wlan监视器";
    private static final String ACTION_CPU_MEM = "资源监视器";
    private static final String ACTION_TEST_2_2_2 = "2.2.2~2.2.4. wan口测试";
    private static final String ACTION_TEST_2_2_8 = "2.2.8.连接设备已连接功能测试";
    private static final String ACTION_TEST_2_2_9 = "2.2.9.路由器信息正确性测试";
    private static final String ACTION_FLASH_RESET = "测试flash reset";
    private static final Font FONT_DEFAULT = new Font(Font.SANS_SERIF, Font.BOLD, 35);

    private static final String[] ITEMS_FUNCTION = {ACTION_WLAN, ACTION_CPU_MEM, ACTION_FLASH_RESET, ACTION_TEST_2_2_2, ACTION_TEST_2_2_8, ACTION_TEST_2_2_9,};

    private HashMap<Class<? extends Frame>, Frame> mMapFrame = new HashMap();

    public MainMenu() {
        super("WIFI自动化工具");
        MyWindowAdapter.openedWindow++;
        GraphicsEnvironment ge = GraphicsEnvironment.
                getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        for (GraphicsDevice g : gs) {
            System.out.println(g.getDisplayMode().getWidth() + " / " + g.getDisplayMode().getHeight());
        }

        Pair<String, Integer> version = DataManager.getVersionInfo();
        if (version != null)
            setTitle("WIFI自动化工具 v" + version.getKey());
        GridLayout layout = new GridLayout(ITEMS_FUNCTION.length, 1);
//        GridBagLayout layout = new GridBagLayout();
//        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(layout);
//        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//        gbc.fill = GridBagConstraints.HORIZONTAL;
        for (int i = 0; i < ITEMS_FUNCTION.length; i++) {
//            gbc.gridy = i;
            JButton button = new JButton(ITEMS_FUNCTION[i]);
            button.setActionCommand(ITEMS_FUNCTION[i]);
            button.setFont(FONT_DEFAULT);
            button.addActionListener(this);
//            layout.setConstraints(button, gbc);
            add(button);
        }

        addWindowListener(new MyWindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        setResizable(false);
//        setSize(300,160);
        setVisible(true);
        pack();
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == ACTION_CPU_MEM) {
            setFrameLocation(getFrame(CpuToolMain.class));
        } else if (e.getActionCommand() == ACTION_WLAN) {
            setFrameLocation(getFrame(StaListMain2.class));
        } else if (e.getActionCommand() == ACTION_TEST_2_2_2) {
            setFrameLocation(getFrame(TestWan.class));
        } else if (e.getActionCommand() == ACTION_TEST_2_2_8) {
            setFrameLocation(getFrame(Test2p2p8.class));
        } else if (e.getActionCommand() == ACTION_TEST_2_2_9) {
            setFrameLocation(getFrame(Test2p2p9.class));
        } else if (e.getActionCommand() == ACTION_FLASH_RESET) {
            setFrameLocation(getFrame(ResetExam.class));
        }
    }

    private Frame getFrame(Class<? extends Frame> frameCls) {
        MyWindowAdapter.openedWindow++;
        if (mMapFrame.containsKey(frameCls)) {
            return mMapFrame.get(frameCls);
        } else {
            try {
                Frame frame = frameCls.newInstance();
                mMapFrame.put(frameCls, frame);
                return frame;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private void setFrameLocation(Frame frame) {
        if (!frame.isVisible())
            frame.setVisible(true);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = 0, y = 0;
        switch (MyWindowAdapter.openedWindow % 4) {
            case 2:
                x = screenSize.width - frame.getWidth();
                break;
            case 3:
                x = screenSize.width - frame.getWidth();
                y = screenSize.height - frame.getHeight();
                break;
            case 0:
                y = screenSize.height - frame.getHeight();
                break;
        }
        frame.setLocation(x, y);
//        frame.setLocation(this.getLocation().x + this.getSize().width, Math.max(screenSize.height - frame.getSize().height >> 1, 0));
//        frame.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        Tool.setGlobalFontRelative(Tool.getScreenSizeLevel());
        new MainMenu();
//        String string = DosCmdHelper.readDosRuntime("netsh interface ip set address name=\"以太网\" source=static addr=192.168.10.88 mask=255.255.255.0 gateway=192.168.88.1 1");
//        System.out.println(string);
//        string = DosCmdHelper.readDosRuntime("netsh interface ip set address name=\"以太网\" source=dhcp");
//        System.out.println(string);
//        while (true) {
//            try {
//                String deviceType = BWR510LocalConnectionHelper.getInstance().getBase_DeviceType(Tool.getGuessGateway());
//                System.out.println(deviceType);
//
//                Thread.sleep(2000);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }
}
