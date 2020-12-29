package com.changhong.telnettool.dialog;

import com.changhong.telnettool.component.XyAxisCanvas;
import com.changhong.telnettool.been.LineBeen;
import com.changhong.telnettool.been.NodeQueue;
import com.changhong.telnettool.been.NodeStrQueue;
import javafx.util.Pair;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

public class TemperatureShowDialog extends Dialog {

    private static final int COUNT_NODE = 10;

    private XyAxisCanvas mXyAxisCvs;
    private int maxTemperature = 40;
    private int minTemperature = 30;

    private NodeStrQueue m5GTemperatureNode;
    private NodeStrQueue m2GTemperatureNode;

    public TemperatureShowDialog(Frame owner) {
        super(owner, "无线温度甘特图");
        String[] xAxis = new String[COUNT_NODE];
        for (int i = 0; i < xAxis.length; i++) {
            xAxis[i] = String.valueOf(i);
        }
        int[] yAxis = new int[11];
        for (int i = 1; i < yAxis.length; i++) {
            yAxis[i] = maxTemperature * i / 10;
        }
        mXyAxisCvs = new XyAxisCanvas(yAxis, xAxis);
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

    public void append2GTemperatureNode(int speed) {
        if (m2GTemperatureNode == null) {
            m2GTemperatureNode = new NodeStrQueue(COUNT_NODE);
            mXyAxisCvs.addLine(new LineBeen("2.4G ℃", Color.blue, m2GTemperatureNode));
        }

        m2GTemperatureNode.push(new Pair<>(speed, speed + "℃"));

        setValueRange(speed);
        mXyAxisCvs.repaint();
    }

    public void append5GTemperatureNode(int speed) {

        if (m5GTemperatureNode == null) {
            m5GTemperatureNode = new NodeStrQueue(COUNT_NODE);
            mXyAxisCvs.addLine(new LineBeen("5G ℃", Color.magenta, m5GTemperatureNode));
        }
        m5GTemperatureNode.push(new Pair<>(speed, speed + "℃"));

        setValueRange(speed);
        mXyAxisCvs.repaint();
    }

    private void setValueRange(int speed) {
        if (speed > maxTemperature) {
            int temp = speed;
            int exponent = 0;
            while (temp > 9) {
                temp /= 10;
                ++exponent;
            }
            ++temp;
            temp = (int) (temp * Math.pow(10, exponent));
            maxTemperature = temp;
        }

        if (speed < minTemperature) {
            int temp = speed;
            int exponent = 0;
            while (temp > 9) {
                temp /= 10;
                ++exponent;
            }
            --temp;
            temp = (int) (temp * Math.pow(10, exponent));
            minTemperature = temp;
        }

        resetYAxis();
    }

    private void resetYAxis() {
        int[] yAxis = new int[11];
        int per = maxTemperature - minTemperature;
        per /= 10;
        yAxis[0] = minTemperature;
        yAxis[10] = maxTemperature;
        for (int i = 1; i < yAxis.length - 1; i++) {
            yAxis[i] = i * per;
            yAxis[i] += minTemperature;
        }
        mXyAxisCvs.setYAxis(yAxis);
    }

}
