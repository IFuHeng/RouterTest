package com.changhong.telnettool.dialog;


import com.changhong.telnettool.webinterface.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AlertDialog extends JDialog implements ActionListener, TextListener {
    public Observer<String> observer;
    public static final String ACTION_OK = "OK";
    private JTextArea mTvMsg;
    private JButton mBtnOk;

    public AlertDialog(Frame owner, String title, String msg) {
        super(owner, title);
        init(msg);
        setLocationRelativeTo(owner);
    }

    public AlertDialog(Dialog owner, String title, String msg) {
        super(owner, title);
        init(msg);
        setLocationRelativeTo(owner);
    }

    public AlertDialog(Frame owner, String title, String msg, Observer<String> observer) {
        super(owner, title);
        this.observer = observer;
        init(msg);
        setLocationRelativeTo(owner);
    }

    public AlertDialog(Dialog owner, String title, String msg, Observer<String> observer) {
        super(owner, title);
        this.observer = observer;
        init(msg);
        setLocationRelativeTo(owner);
    }

    private void init(String msg) {
        setLayout(new BorderLayout());
        mTvMsg = new JTextArea();
        mTvMsg.setText(msg);
        mTvMsg.setForeground(Color.RED);
        mTvMsg.setFont(new Font(Font.DIALOG, Font.PLAIN, 24));
        mTvMsg.setEditable(false);
        add(mTvMsg, "North");

        {
            JPanel jPanel = new JPanel();
            mBtnOk = new JButton(ACTION_OK);
            mBtnOk.setActionCommand(ACTION_OK);
            mBtnOk.addActionListener(this);
            jPanel.add(mBtnOk);
            add(jPanel, BorderLayout.SOUTH);
        }

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
                observer.onComplete();
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
