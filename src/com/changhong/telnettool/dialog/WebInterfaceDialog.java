package com.changhong.telnettool.dialog;

import com.changhong.telnettool.event.PositiveNumberTextLimitListener;
import com.changhong.telnettool.event.TextInputLengthLimitListener;
import com.changhong.telnettool.been.HttpInterfaceData;
import com.changhong.telnettool.tool.Tool;
import com.changhong.telnettool.webinterface.BWR510LocalConnectionHelper;
import com.changhong.telnettool.webinterface.Observer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WebInterfaceDialog extends Dialog implements ActionListener {

    private static final String ACTION_SEND = "Send";
    private static final String ACTION_CONNECT = "Connect";
    private HttpInterfaceData mData;

    private final GridBagConstraints mGBC = new GridBagConstraints();
    private final GridBagLayout mGridBagLayout = new GridBagLayout();

    private Button mBtnSend;
    private Button mBtnConnect;
    private TextField mViewPort;
    private TextField mViewIP;
    private TextArea mTvInfo;

    public WebInterfaceDialog(Frame owner) {
        super(owner);
        setTitle("HTTP测试访问");

        initView();

        mData = new HttpInterfaceData();

        pack();
        setLocationRelativeTo(owner);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (e.getSource() instanceof Dialog) {
                    Dialog dialog = (Dialog) e.getSource();
                    dialog.dispose();
                }
            }
        });
    }

    private void initView() {
        // north
        mViewIP = new TextField(Tool.getGuessGateway());
        mViewIP.setColumns(25);
        mViewIP.addTextListener(new TextInputLengthLimitListener(15));
        mViewPort = new TextField("23");
        mViewPort.setColumns(5);
        mViewPort.addTextListener(new TextInputLengthLimitListener(5));
        mViewPort.addTextListener(new PositiveNumberTextLimitListener());

        mBtnSend = new Button(ACTION_SEND);
        mBtnSend.setActionCommand(ACTION_SEND);
        mBtnSend.addActionListener(this);
        mBtnConnect = new Button(ACTION_CONNECT);
        mBtnConnect.setActionCommand(ACTION_CONNECT);
        mBtnConnect.addActionListener(this);
        Panel panel = new Panel();
        panel.add(new Label("IP:"));
        panel.add(mViewIP);
        panel.add(new Label("  port:"));
        panel.add(mViewPort);
        panel.add(mBtnConnect);

        panel.setMinimumSize(new Dimension(600, 300));
        add(panel, "North");

        // center
        mTvInfo = new TextArea();
        mTvInfo.setEditable(false);
        add(mTvInfo, "Center");

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == ACTION_CONNECT) {
            checkDeviceType();
        }
    }

    private void checkDeviceType() {
        BWR510LocalConnectionHelper.getInstance().getBase_DeviceType(mViewIP.getText(), new Observer<String>() {
            @Override
            public void onError(Throwable throwable) {
                mTvInfo.setForeground(Color.red);
                mTvInfo.setText('\n' + throwable.getMessage());
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onNext(String s) {
                mTvInfo.setForeground(Color.black);
                mTvInfo.setText("Get device type is : " + s);
                checkWizard();
            }
        });
    }

    private void login() {
        new InputDialog(this, "请输入登录密码", new Observer<String>() {
            @Override
            public void onNext(String s) {
                BWR510LocalConnectionHelper.getInstance().login(mViewIP.getText() + "", s, new Observer<String>() {
                    @Override
                    public void onNext(String s) {
                        mTvInfo.setForeground(Color.black);
                        mTvInfo.append("login successes: cookie = " + s);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        new AlertDialog(WebInterfaceDialog.this, "错误", throwable.getMessage()).setVisible(true);
                        mTvInfo.setForeground(Color.red);
                        mTvInfo.setText("login failed");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {
                mTvInfo.setForeground(Color.red);
                mTvInfo.setText("cancel login.");
            }

            @Override
            public void onComplete() {

            }
        }).setVisible(true);


    }

    private void checkWizard() {
        BWR510LocalConnectionHelper.getInstance().checkWizardGuide(mViewIP.getText(), new Observer<Integer>() {
            @Override
            public void onError(Throwable throwable) {
                mTvInfo.setForeground(Color.red);
                mTvInfo.setText("checkWizard failed");
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onNext(Integer integer) {
                mTvInfo.setForeground(Color.black);
                switch (integer) {
                    case 1:
                        mTvInfo.append("\n已完成向导");
                        break;
                    case 2:
                        mTvInfo.append("\n未完成向导，已设置系统密码");
                        break;
                    case 0:
                        mTvInfo.append("\n未完成向导");
                        break;
                }
                login();
            }
        });
    }

}
