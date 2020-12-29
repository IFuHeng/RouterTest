package com.changhong.telnettool.dialog;

import com.changhong.telnettool.component.XyAxisCanvas;
import com.changhong.telnettool.been.LineBeen;
import com.changhong.telnettool.been.NodeStrQueue;
import javafx.util.Pair;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SpeedShowDialog extends Dialog {

    private static final int COUNT_NODE = 10;

    private XyAxisCanvas mXyAxisCvs;
    private int maxSpeed = 1024;

    private Color mUploadSpeedColor = Color.magenta;
    private Color mDownloadSpeedColor = Color.blue;
    private NodeStrQueue mUploadNode;
    private NodeStrQueue mDownloadNode;

    public SpeedShowDialog(Frame owner) {
        super(owner, "无线网速甘特图");
        String[] xAxis = new String[COUNT_NODE];
        for (int i = 0; i < xAxis.length; i++) {
            xAxis[i] = String.valueOf(i);
        }
        int[] yAxis = new int[11];
        String[] yAxisStr = new String[yAxis.length];
        yAxisStr[0] = String.valueOf(0);
        for (int i = 1; i < yAxis.length; i++) {
            yAxis[i] = maxSpeed * i / 10;
            yAxisStr[i] = getSpeedString(yAxis[i]);
        }
        mXyAxisCvs = new XyAxisCanvas(yAxis, yAxisStr, xAxis);
        mXyAxisCvs.setSize(600, 400);
        add(mXyAxisCvs);
        pack();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (e.getSource() instanceof Dialog) {
                    Dialog dialog = (Dialog) e.getSource();
                    dialog.dispose();
                }
                super.windowClosing(e);
            }
        });
    }

    public void appendDownloadSpeedNode(int speed) {
        if (mDownloadNode == null) {
            mDownloadNode = new NodeStrQueue(COUNT_NODE);
            mXyAxisCvs.addLine(new LineBeen("下行速度", mDownloadSpeedColor, mDownloadNode));
        }

        mDownloadNode.push(new Pair<>(speed, getSpeedString(speed)));

        setMaxSpeed(speed);
        mXyAxisCvs.repaint();
    }

    public void appendUploadSpeedNode(int speed) {

        if (mUploadNode == null) {
            mUploadNode = new NodeStrQueue(COUNT_NODE);
            mXyAxisCvs.addLine(new LineBeen("上行速度", mUploadSpeedColor, mUploadNode));
        }
        mUploadNode.push(new Pair<>(speed, getSpeedString(speed)));

        setMaxSpeed(speed);
        mXyAxisCvs.repaint();
    }

    private void setMaxSpeed(int speed) {
        if (speed > maxSpeed) {
            int temp = speed;
            int exponent = 0;
            while (temp > 9) {
                temp /= 10;
                ++exponent;
            }
            ++temp;
            temp = (int) (temp * Math.pow(10, exponent));
            maxSpeed = temp;
        }

        resetYAxis();
    }

    private void resetYAxis() {
        int[] yAxis = new int[11];
        String[] yAxisStr = new String[yAxis.length];
        yAxisStr[0] = String.valueOf(0);
        for (int i = 1; i < yAxis.length; i++) {
            yAxis[i] = maxSpeed * i / 10;
            yAxisStr[i] = getSpeedString(yAxis[i]);
        }
        mXyAxisCvs.setYAxis(yAxis, yAxisStr);
    }

    private String getSpeedString(long speed) {
        if (speed >= 1024 * 1024)
            return String.format("%.1fM/s", speed / 1024f / 1024).replace(".0", "");
        if (speed >= 1024)
            return String.format("%.1fK/s", speed / 1024f).replace(".0", "");

        return speed + " b/s";
    }
}
