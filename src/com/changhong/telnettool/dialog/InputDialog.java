package com.changhong.telnettool.dialog;

import com.changhong.telnettool.event.TextInputLengthLimitListener;
import com.changhong.telnettool.webinterface.Observer;

import java.awt.*;
import java.awt.event.*;

public class InputDialog extends Dialog implements ActionListener, TextListener {
    public Observer<String> observer;
    public static final String ACTION_OK = "OK";
    public static final String ACTION_CANCEL = "CANCEL";
    private TextField mTvMsg;
    private Button mBtnOk;
    private Button mBtnCancel;

    public InputDialog(Frame owner, String title, Observer<String> observer) {
        super(owner, title);
        this.observer = observer;
        init();
        setLocationRelativeTo(owner);
    }

    public InputDialog(Dialog owner, String title, Observer<String> observer) {
        super(owner, title);
        this.observer = observer;
        init();
        setLocationRelativeTo(owner);
    }

    private void init() {
        setLayout(new BorderLayout());
        mTvMsg = new TextField();
        mTvMsg.setColumns(25);
        mTvMsg.addTextListener(new TextInputLengthLimitListener(100));
        mTvMsg.addTextListener(this);
        mTvMsg.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getSource() instanceof TextComponent) {
                    TextComponent tc = ((TextComponent) e.getSource());
                    if (tc.getText().isEmpty()) {
                        return;
                    }


                    mBtnOk.setEnabled(!tc.getText().isEmpty());
                }
            }
        });
        add(mTvMsg, "North");

        Panel panel = new Panel();
        mBtnOk = new Button(ACTION_OK);
        mBtnOk.setActionCommand(ACTION_OK);
        mBtnCancel = new Button(ACTION_CANCEL);
        mBtnCancel.setActionCommand(ACTION_CANCEL);
        panel.add(mBtnOk);
        panel.add(mBtnCancel);
        mBtnOk.addActionListener(this);
        mBtnCancel.addActionListener(this);
        add(panel, "South");

        pack();
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

    @Override
    public void actionPerformed(ActionEvent e) {
        dispose();
        if (e.getActionCommand() == ACTION_OK) {
            if (observer != null) {
                observer.onNext(mTvMsg.getText());
            }
        }
    }

    @Override
    public void textValueChanged(TextEvent e) {
        if (e.getSource() instanceof TextComponent) {
            TextComponent tc = ((TextComponent) e.getSource());
            mBtnOk.setEnabled(!tc.getText().isEmpty());
        }
    }
}
