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
    private long maxSpeed = 1024;

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
        long[] yAxis = new long[11];
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

    public void appendDownloadSpeedNode(long speed) {
        if (mDownloadNode == null) {
            mDownloadNode = new NodeStrQueue(COUNT_NODE);
            mXyAxisCvs.addLine(new LineBeen("下行速度", mDownloadSpeedColor, mDownloadNode));
        }

        mDownloadNode.push(new Pair<>(speed, getSpeedString(speed)));

        setMaxSpeed(speed);
        mXyAxisCvs.repaint();
    }

    public void appendUploadSpeedNode(long speed) {

        if (mUploadNode == null) {
            mUploadNode = new NodeStrQueue(COUNT_NODE);
            mXyAxisCvs.addLine(new LineBeen("上行速度", mUploadSpeedColor, mUploadNode));
        }
        mUploadNode.push(new Pair<>(speed, getSpeedString(speed)));

        setMaxSpeed(speed);
        mXyAxisCvs.repaint();
    }

    private void setMaxSpeed(long speed) {

        if (speed > maxSpeed) {
            maxSpeed = getTotleValue(speed);
        } else {
            long maxValue = 0;
            for (int i = 0; i < mUploadNode.size(); i++)
                maxValue = Math.max(mUploadNode.elementAt(i).getKey(), maxValue);
            for (int i = 0; i < mDownloadNode.size(); i++)
                maxValue = Math.max(mDownloadNode.elementAt(i).getKey(), maxValue);
            if (maxValue < maxSpeed / 5)
                maxSpeed = getTotleValue(maxValue);
        }

        resetYAxis();
    }

    private long getTotleValue(long origin) {
        long exponent = 0;
        int level = 0;
        while (origin > 1024) {
            level++;
            origin >>>= 10;
        }

        while (origin > 9) {
            origin /= 10;
            ++exponent;
        }
        ++origin;
        long result = origin * Math.round(Math.pow(10, exponent));
        while (level > 0) {
            result <<= 10;
            level--;
        }
        return result;
    }

    private void resetYAxis() {
        long[] yAxis = new long[11];
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
