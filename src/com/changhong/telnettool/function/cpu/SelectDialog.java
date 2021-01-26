package com.changhong.telnettool.function.cpu;

import com.changhong.telnettool.webinterface.Observer;

import javax.swing.*;
import javax.swing.plaf.InsetsUIResource;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class SelectDialog extends Dialog implements ItemListener, ActionListener {

    private static final int COUNT_NODE = 10;
    private static final String ACTION_OK = "确定";
    private static final String ACTION_CANCEL = "取消";

    private Observer<Set<String>> observer;
    private String[] choices;
    private Set<String> result;

    public SelectDialog(Frame owner, String title, String[] choices, boolean[] arrChecked, Observer<Set<String>> observer) {
        super(owner, title);
        this.choices = choices;
        this.observer = observer;
        result = new HashSet<>();

        initView(title, choices, arrChecked);
        pack();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (e.getSource() instanceof Dialog) {
                    Dialog dialog = (Dialog) e.getSource();
                    dialog.dispose();
                    SelectDialog.this.observer.onComplete();
                }
                super.windowClosing(e);
            }
        });

        setLocationRelativeTo(owner);
    }

    private void initView(String title, String[] choices, boolean[] arrChecked) {

        this.setLayout(new BorderLayout());
        {//选项ui
            JPanel panel = new JPanel();
            JScrollPane jScrollPane = new JScrollPane(panel);
            jScrollPane.setBorder(BorderFactory.createTitledBorder(title));
            panel.setLayout(new GridLayout(choices.length / 3 + (choices.length % 3 == 0 ? 0 : 1), 3));
            for (int i = 0; i < choices.length; i++) {
                JCheckBox cb = new JCheckBox(choices[i]);
                cb.setBackground(new Color(i % 2 == 1 ? 0x88eeeeee : 0x00ffffff));
                cb.setActionCommand(String.valueOf(i));
                cb.setMargin(new InsetsUIResource(1,1,1,1));
                System.out.println(cb.getMargin());
                if (arrChecked[i]) {
                    cb.getModel().setSelected(true);
                    result.add(choices[i]);
                }
                cb.addItemListener(this);
                panel.add(cb);
            }
//            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//            jScrollPane.setPreferredSize(new Dimension(panel.getWidth(), Math.min(panel.getHeight(), screenSize.height - 200)));
//            jScrollPane.setMaximumSize(new Dimension((int) screenSize.getWidth(), (int) screenSize.getHeight() - 200));
            this.add(jScrollPane, BorderLayout.CENTER);
        }
        {//确认或取消
            JPanel jPanel = new JPanel();
            jPanel.setBorder(BorderFactory.createLoweredSoftBevelBorder());
            jPanel.setLayout(new GridLayout(1, 2));
            {
                JButton jButton1 = new JButton(ACTION_OK);
                jButton1.setActionCommand(ACTION_OK);
                jButton1.addActionListener(this);
                jPanel.add(jButton1);
            }
            {
                JButton jButton2 = new JButton(ACTION_CANCEL);
                jButton2.setActionCommand(ACTION_CANCEL);
                jButton2.addActionListener(this);
                jPanel.add(jButton2);
            }
            this.add(jPanel, BorderLayout.SOUTH);
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getSource();
        if (source instanceof JToggleButton) {
            JToggleButton rtb = ((JToggleButton) source);
            String str = rtb.getText();
            int index = Arrays.binarySearch(this.choices, str);
            if (rtb.getModel().isSelected()) {
                result.add(str);
            } else
                result.remove(str);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == ACTION_CANCEL) {
            this.dispose();
            this.setVisible(false);
        } else if (e.getActionCommand() == ACTION_OK) {
            this.observer.onNext(result);
            this.setVisible(false);
            this.dispose();
        }
    }
}

