package com.changhong.telnettool.function.test_exam;

import com.changhong.telnettool.been.BSSIDPoint;
import com.changhong.telnettool.been.SSIDInfo;
import com.changhong.telnettool.been.WlanBssidList;
import com.changhong.telnettool.been.WlanInfo;
import com.changhong.telnettool.event.MyWindowAdapter;
import com.changhong.telnettool.task.DosCmdHelper;
import com.changhong.telnettool.tool.Tool;
import com.changhong.telnettool.webinterface.BWR510LocalConnectionHelper;
import com.changhong.telnettool.webinterface.Observer;
import com.changhong.telnettool.webinterface.been.wifi.WifiRequireAllBeen;
import com.changhong.telnettool.webinterface.been.wifi.WifiResponseAllBeen;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

/**
 * 2.2.7. 无线功率模式修改功能测试
 */
public class Test2p2p7 extends WanSettingBase implements Runnable, ActionListener {

    private static final String ACTION_CONNECT = "connect";
    private JTextField mTvRouterPwd;
    private JLabel mTvSSID2$4G;
    private JLabel mTvSSID5G;
    private Pair<WifiResponseAllBeen, WifiResponseAllBeen> wifiSettings;

    public Test2p2p7() {
    }

    public Test2p2p7(String password, Observer<Pair<Boolean, String>> callback) {
        super(password, callback);
    }

    @Override
    public void run() {
        //获得类型
        if (!getDeviceType())
            return;

        //登录
        if (!login())
            return;

        final int[] averages = new int[6];//记录结果

        Pair<WifiResponseAllBeen, WifiResponseAllBeen> wifiSettings = null;
        try {
            callbackNext(false, "读取wifi设置中……");
            wifiSettings = BWR510LocalConnectionHelper.getInstance().getWifiSetting(mGateWay);
            callbackNext(true, "成功\n" +
                    "    2.4G SSID = " + wifiSettings.getKey().getSsid() + "\n    5G SSID = " + wifiSettings.getValue().getSsid() + "\n    双频优选: " + (wifiSettings.getKey().getPrefer_5g() == 1 ? '开' : '关'));
        } catch (Exception e) {
            e.printStackTrace();
            callbackError(new Exception("未获取到wifi设置，中断"));
            return;
        }


        callbackNext(false, "step2 : ");
//        setTxPower(wifiSettings.getKey(), wifiSettings.getValue(), 0);//设置信号强度为强

//        callbackNext(true, "等待10秒让路由器完成操作……");
//        waitForSecounds(10);

        averages[0] = getAverageRssi(wifiSettings.getKey(), 10, false);
        averages[1] = getAverageRssi(wifiSettings.getValue(), 10, true);

//        callbackNext(false, "step3 : ");
//        setTxPower(wifiSettings.getKey(), wifiSettings.getValue(), 1);//设置信号强度为中
//
//        callbackNext(true, "等待10秒让路由器完成操作……");
//        waitForSecounds(10);
//
//        callbackNext(false, "step4 : ");
//        averages[2] = getAverageRssi(wifiSettings.getKey(), 10, false);
//        averages[3] = getAverageRssi(wifiSettings.getValue(), 10, true);
//
//        callbackNext(false, "step5 : ");
//        setTxPower(wifiSettings.getKey(), wifiSettings.getValue(), 2);//设置信号强度为弱
//
//        callbackNext(true, "等待10秒让路由器完成操作……");
//        waitForSecounds(10);
//
//        callbackNext(false, "step6 : ");
//        averages[4] = getAverageRssi(wifiSettings.getKey(), 10, false);
//        averages[5] = getAverageRssi(wifiSettings.getValue(), 10, true);

        callbackNext(true, String.format("    结果：信号强度分别为    2.4G    5G" +
                        "\n                  强    %d%%    %d%%" +
                        "\n                  中    %d%%    %d%%" +
                        "\n                  弱    %d%%    %d%%"
                , averages[0], averages[1], averages[2], averages[3], averages[4], averages[5]));
        callbackComplete();
    }

    private void createUi() {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println("The width and the height of the screen are " + screenSize.getWidth() + " x " + screenSize.getHeight());
        double value = screenSize.getWidth() * screenSize.getHeight();
        value /= 1000000;
        if (value >= 8)
            Tool.setGlobalFontRelative(3);
        else if (value >= 3)
            Tool.setGlobalFontRelative(2);
        else if (Math.round(value) >= 2)
            Tool.setGlobalFontRelative(1.5f);

        Frame frame = new Frame("2.2.7. 无线功率模式修改功能测试");

        BoxLayout boxLayout = new BoxLayout(frame, BoxLayout.Y_AXIS);
        frame.setLayout(boxLayout);

        {
            JPanel jPanel = new JPanel();
            jPanel.setBorder(BorderFactory.createTitledBorder("路由器密码"));
            jPanel.add(new JLabel("路由密码"));
            mTvRouterPwd = new JTextField();
            mTvRouterPwd.setColumns(20);
            jPanel.add(mTvRouterPwd);
            JButton mBtnConnect = new JButton("连接");
            mBtnConnect.setActionCommand(ACTION_CONNECT);
            mBtnConnect.addActionListener(this);
            jPanel.add(mBtnConnect);
            frame.add(jPanel);
        }
        {//WiFi 信息
            JPanel jPanel = new JPanel();
            GridBagLayout layout = new GridBagLayout();
            GridBagConstraints bgc = new GridBagConstraints();
            jPanel.setLayout(layout);
            jPanel.setBorder(BorderFactory.createTitledBorder("Wi-Fi"));

            mTvSSID2$4G = new JLabel("", JLabel.HORIZONTAL);
            mTvSSID5G = new JLabel("", JLabel.HORIZONTAL);

            JButton jButton2$4G = new JButton("连接");
            JButton jButton5G = new JButton("连接");

            {
                JLabel label = new JLabel("SSID 2.4G");
                jPanel.add(label);

                bgc.weightx = 2;
                layout.setConstraints(mTvSSID2$4G, bgc);
                jPanel.add(mTvSSID2$4G);
                jPanel.add(jButton2$4G);
            }
            {
                bgc.gridy = 1;
                JLabel label = new JLabel("SSID 5G");
                layout.setConstraints(label, bgc);
                jPanel.add(label);

                bgc.weightx = 2;
                layout.setConstraints(mTvSSID5G, bgc);
                jPanel.add(mTvSSID5G);
                layout.setConstraints(jButton5G, bgc);
                jPanel.add(jButton5G);
            }

            frame.add(jPanel);
        }

        {//WiFi 信息
            JPanel jPanel = new JPanel();
            GridBagLayout layout = new GridBagLayout();
            GridBagConstraints bgc = new GridBagConstraints();
            jPanel.setLayout(layout);
            jPanel.setBorder(BorderFactory.createTitledBorder("测试"));

            JLabel[] mArrTvStrength = new JLabel[6];
            for (int i = 0; i < mArrTvStrength.length; i++) {
                mArrTvStrength[i] = new JLabel("", JLabel.HORIZONTAL);
            }

            JButton mBtnT1 = new JButton("测试1");
            mBtnT1.setActionCommand("test1");
            JButton mBtnT2 = new JButton("测试2");
            mBtnT2.setActionCommand("test2");
            JButton mBtnT3 = new JButton("测试3");
            mBtnT3.setActionCommand("test3");

            {
                jPanel.add(new JLabel(""));
                bgc.weightx = 2;
                JLabel temp = new JLabel("2.4G");
                layout.setConstraints(temp, bgc);
                jPanel.add(temp);
                temp = new JLabel("5G");
                layout.setConstraints(temp, bgc);
                jPanel.add(temp);


                bgc.weightx = 1;
                jPanel.add(new JLabel(""));
            }
            {
                bgc.gridy = 1;
                bgc.weightx = 0;
                JLabel temp = new JLabel("强");
                layout.setConstraints(temp, bgc);
                jPanel.add(temp);
                bgc.weightx = 2;
                temp = mArrTvStrength[0];
                layout.setConstraints(temp, bgc);
                jPanel.add(temp);
                temp = mArrTvStrength[1];
                layout.setConstraints(temp, bgc);
                jPanel.add(temp);

                bgc.weightx = 1;
                layout.setConstraints(mBtnT1, bgc);
                jPanel.add(mBtnT1);
            }
            {
                bgc.gridy = 2;
                bgc.weightx = 0;
                JLabel temp = new JLabel("中");
                layout.setConstraints(temp, bgc);
                jPanel.add(temp);
                bgc.weightx = 2;
                temp = mArrTvStrength[2];
                layout.setConstraints(temp, bgc);
                jPanel.add(temp);
                temp = mArrTvStrength[3];
                layout.setConstraints(temp, bgc);
                jPanel.add(temp);

                bgc.weightx = 1;
                layout.setConstraints(mBtnT2, bgc);
                jPanel.add(mBtnT2);
            }
            {
                bgc.gridy = 3;
                bgc.weightx = 0;
                JLabel temp = new JLabel("弱");
                layout.setConstraints(temp, bgc);
                jPanel.add(temp);
                bgc.weightx = 2;
                temp = mArrTvStrength[4];
                layout.setConstraints(temp, bgc);
                jPanel.add(temp);
                temp = mArrTvStrength[5];
                layout.setConstraints(temp, bgc);
                jPanel.add(temp);

                bgc.weightx = 1;
                layout.setConstraints(mBtnT3, bgc);
                jPanel.add(mBtnT3);
            }

            frame.add(jPanel);
        }

        frame.addWindowListener(new MyWindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                Tool.log(e);

                super.windowClosing(e);
            }
        });
        frame.setResizable(true);
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    /**
     * @param been2G
     * @param been5G
     * @param txPower 0: 强 ； 1：中 ； 2：弱。
     */
    private void setTxPower(WifiResponseAllBeen been2G, WifiResponseAllBeen been5G, int txPower) {
        try {//step 2：PC 通过无线连接路由器广播出的 SSID，打开 inssider 软件，记录 2.4G 与 5G ssid在 10S 时间内的一个稳定的信号强度
            if (been2G.getPrefer_5g() == 1) {
                WifiRequireAllBeen wifiSetting = new WifiRequireAllBeen(been2G);
                wifiSetting.setTxpower_mode(0);
                BWR510LocalConnectionHelper.getInstance().setWifiSetting(mGateWay, wifiSetting);
            } else {
                callbackNext(false, "分别设置 2.4G 与 5G 的 信号强度" + (txPower == 0 ? '强' : txPower == 1 ? '中' : '弱') + "  ……");
                WifiRequireAllBeen wifiSetting2$4G = new WifiRequireAllBeen(been2G);
                wifiSetting2$4G.setTxpower_mode(0);
                wifiSetting2$4G.setSave_action(0);
                WifiRequireAllBeen wifiSetting5G = new WifiRequireAllBeen(been5G);
                wifiSetting5G.setTxpower_mode(0);
                wifiSetting5G.setSave_action(1);
                BWR510LocalConnectionHelper.getInstance().setWifiSetting(mGateWay, wifiSetting2$4G, wifiSetting5G);
                callbackNext(true, "成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            callbackError(new Exception("设置wifi失败，中断"));
            return;
        }
    }

    private int getCurrentRssi() {
        WlanInfo obj = WlanInfo.load();
        return obj.getPer_signal_strength();
    }

    private void connectTo(WifiResponseAllBeen been) {
        try {// 分别连接 2.4G和 5G的ssid
            String ssid = been.getSsid();
            String key = been.getKey();
            DosCmdHelper.createWlanProfile(ssid, ssid, key);
            callbackNext(true, "等待3秒让wifi完成连接……");
            waitForSecounds(3);

            callbackNext(false, "检查当前ssid……");
            String tempSSID = DosCmdHelper.getCurrentWlanSSID();

            if (tempSSID == null)
                callbackError(new RuntimeException("未获取到ssid，失败"));
            else
                callbackNext(true, "连接到" + ssid + (tempSSID.equals(ssid) ? "成功" : "失败"));

            DosCmdHelper.readDosRuntime("netsh wlan delete profile " + ssid);
        } catch (Exception e) {
            callbackError(e);
        }
    }

    private int getAverageRssi(WifiResponseAllBeen been, int times, boolean is5G) {
//        connectTo(been);
        callbackNext(true, "测试" + been.getSsid() + " " + times + "s平均信号强度…… ");
        int max = 0;
        for (int i = 0; i < times; i++) {
//            WlanInfo obj = WlanInfo.load();
            for (SSIDInfo ssidInfo : WlanBssidList.load().getArrSSID()) {
                if (ssidInfo.getSSID().equals(been.getSsid())) {
                    for (BSSIDPoint bssidPoint : ssidInfo.getArrBSSID()) {
                        if (bssidPoint.getChannel() > 13 == is5G) {
                            max += bssidPoint.getPer_signal_strength();
                            callbackNext(true, String.format("    %d :  %d%%", (times - i), max / (i + 1)));
                            break;
                        }
                    }
                    break;
                }
            }
        }

        return max / times;
    }

    protected void waitForSecounds(int secounds) {
        for (int i = 0; i < secounds; i++) {
            callbackNext(true, (secounds - i) + "秒");
            sleep(1000);
        }
        callbackNext(true, "等待结束");
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand() == ACTION_CONNECT) {
            password = mTvRouterPwd.getText();
            //获得类型
            if (!getDeviceType())
                return;

            //登录
            if (!login())
                return;

            try {
                callbackNext(false, "读取wifi设置中……");
                wifiSettings = BWR510LocalConnectionHelper.getInstance().getWifiSetting(mGateWay);
                callbackNext(true, "成功\n" +
                        "    2.4G SSID = " + wifiSettings.getKey().getSsid() + "\n    5G SSID = " + wifiSettings.getValue().getSsid() + "\n    双频优选: " + (wifiSettings.getKey().getPrefer_5g() == 1 ? '开' : '关'));

                mTvSSID2$4G.setText(wifiSettings.getKey().getSsid());
                mTvSSID5G.setText(wifiSettings.getValue().getSsid());
            } catch (Exception e) {
                e.printStackTrace();
                callbackError(new Exception("未获取到wifi设置，中断"));
                return;
            }
        }
    }
}
