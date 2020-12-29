package com.changhong.telnettool.function.download;

import com.changhong.telnettool.event.MyWindowAdapter;
import com.changhong.telnettool.event.PositiveNumberTextLimitListener;
import com.changhong.telnettool.event.TextInputLengthLimitListener;
import com.changhong.telnettool.function.download.download.DownloadProgressListener;
import com.changhong.telnettool.function.download.download.FileDownloader;
import com.changhong.telnettool.tool.Tool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class DownloadTest extends Frame implements ActionListener {

    private static final String ACTION_PAUSE = "停止";
    private static final String ACTION_PLAY = "启动";
    private static final String TITLE_FRAME = "测试满负荷下载";

    private JButton mBtnPlayOrPause;
    private JTextField mViewUrl;
    private TextField mViewThreadCount;
    private Label mViewSpeed;
    private Label mViewSize;
    private JPanel mPanelThread;

    public DownloadTest() {
        super(TITLE_FRAME);
        createUi();

        this.addWindowListener(new MyWindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stopPlay();
                dispose();
                Tool.log(e);
                super.windowClosing(e);
            }

        });
        this.setResizable(true);
        this.setVisible(true);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        try {
            URLConnection connection = new URL("https://tu.66vod.net/2020/4486.jpg").openConnection();
            System.out.println(connection.getContentLength());
            System.out.println(connection.getContent());
            System.out.println(connection.getDate());
        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.println(Arrays.toString(new File("F://temp/").list()));
        new DownloadTest();
    }

    private void createUi() {
        this.setLayout(new BorderLayout());
        {
            JPanel panel = new JPanel();
            panel.setBorder(BorderFactory.createTitledBorder("下载链接"));
            {// ip port play in north
                {
                    mViewUrl = new JTextField("https://tu.66vod.net/2020/4486.jpg");
                    mViewUrl.setColumns(40);

                    mBtnPlayOrPause = new JButton(ACTION_PLAY);
                    mBtnPlayOrPause.setActionCommand(ACTION_PLAY);
                    mBtnPlayOrPause.addActionListener(this);

                    panel.add(new JLabel("下载链接:"));
                    panel.add(mViewUrl);
                }
                {
                    panel.add(new Label());
                    panel.add(new JLabel("线程数:"));
                    mViewThreadCount = new TextField("1");
                    mViewThreadCount.addTextListener(new PositiveNumberTextLimitListener());
                    mViewThreadCount.addTextListener(new TextInputLengthLimitListener(1));
                    panel.add(mViewThreadCount);
                }
                {
                    panel.add(new Label());
                    panel.add(mBtnPlayOrPause);
                }
            }
            this.add(panel, BorderLayout.NORTH);
        }
        {//显示各个线程速度
            mPanelThread = new JPanel();
            mPanelThread.setLayout(new BoxLayout(mPanelThread, BoxLayout.Y_AXIS));
            mPanelThread.setMinimumSize(new Dimension(400, 400));
            JScrollPane jScrollPane = new JScrollPane(mPanelThread);
            jScrollPane.setBorder(BorderFactory.createTitledBorder("下载进程"));

            this.add(jScrollPane, BorderLayout.CENTER);
        }
        {// 显示速度
            JPanel panel = new JPanel();
            panel.setBorder(BorderFactory.createTitledBorder("速度"));
            mViewSpeed = new Label("849.3MB/s");
            mViewSpeed.setForeground(Color.GREEN);
            panel.add(mViewSpeed);

            panel.add(new Label());

            mViewSize = new Label("354k/1394M  (0.1%)");
            mViewSpeed.setForeground(Color.RED);

            panel.add(mViewSize);
            this.add(panel, BorderLayout.SOUTH);
        }
    }

    private Panel createThreadSpeedUi() {
        Panel panel = new Panel();
        JProgressBar progressbar = new JProgressBar();
        panel.add(progressbar);
//        int progress = (int) Math.round(Math.random() * 100);
//        System.out.println(progress);
//        progressbar.setValue(progress);
        {
            JLabel label = new JLabel("0%", JLabel.CENTER);
            label.setForeground(Color.DARK_GRAY);
            panel.add(label);
        }
        {
            JLabel label = new JLabel("", JLabel.CENTER);
            label.setForeground(Color.BLACK);
            panel.add(label);
        }

        return panel;
    }

    private void startPlay() {

        mBtnPlayOrPause.setText(ACTION_PAUSE);
        mBtnPlayOrPause.setActionCommand(ACTION_PAUSE);

        mViewUrl.setEnabled(false);
        mViewThreadCount.setEnabled(false);

        String url = mViewUrl.getText();
        int threadSize = 1;
        {
            if (mViewThreadCount.getText().length() > 0) {
                int temp = Integer.parseInt(mViewThreadCount.getText());
                if (temp > threadSize)
                    threadSize = temp;
            }
        }
        mPanelThread.removeAll();

        final HashMap<Integer, Panel> viewMap = new HashMap<>();
        final long startTime = System.currentTimeMillis();

        FileDownloader fileDownloader = new FileDownloader(url, new File("E://"), threadSize);
        fileDownloader.setListener(new DownloadProgressListener() {

            @Override
            public void onDownloadSize(int size, int total) {
                if (size == total)
                    stopPlay();

                long costTime = System.currentTimeMillis() - startTime;
                float rate = size * 100f / total;

                mViewSpeed.setText(Tool.getSpeedStringBit(size * 1000 / costTime));
                mViewSize.setText(String.format("%s / %s (%.1f)", Tool.getCountString(size), Tool.getCountString(total), rate));
            }

            @Override
            public void onChildDownloadSize(int id, int length, int size) {
                Panel panel;
                if (viewMap.containsKey(id)) {
                    panel = viewMap.get(id);
                } else {
                    panel = createThreadSpeedUi();
                    mPanelThread.add(panel);
                }

                float rate = length * 100f / size;
                JProgressBar bar = (JProgressBar) panel.getComponent(0);
                bar.setValue((int) rate);
                Label label1 = (Label) panel.getComponent(1);
                label1.setText(String.format("%.1f%%", rate));
                Label label2 = (Label) panel.getComponent(2);
                label2.setText(length + " / " + size);
            }
        });
    }

    private void stopPlay() {
        mBtnPlayOrPause.setText(ACTION_PLAY);
        mBtnPlayOrPause.setActionCommand(ACTION_PLAY);

        mViewUrl.setEnabled(true);
        mViewThreadCount.setEnabled(true);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(ACTION_PLAY)) {
            startPlay();
        } else if (e.getActionCommand().equals(ACTION_PAUSE)) {

        }
    }

}
