package com.changhong.telnettool.component;

import com.changhong.telnettool.been.LineBeen;
import com.changhong.telnettool.been.NodeStrQueue;
import javafx.util.Pair;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class XyAxisCanvas extends Canvas {

    private ArrayList<LineBeen> mLines;

    private long[] yAxis;
    private String[] yAxisStr;
    private String[] xAxis;

    private static final int PADDING_DEFAULT = 10;
    private int mPaddingLeft = PADDING_DEFAULT;
    private int mPaddingTop = PADDING_DEFAULT;
    private int mPaddingRight = PADDING_DEFAULT;
    private int mPaddingBottom = PADDING_DEFAULT;

    private boolean isUseCacheGraphic = true;

    private BufferedImage bi;

    private Graphics2D tg;

    private final Font FONT_LINE_STR = new Font(Font.SANS_SERIF, Font.PLAIN, 10);

    private boolean isShowPointStr = true;//是否显示每个点文字

    public XyAxisCanvas(long[] yAxis, String[] xAxis) {
        setYAxis(yAxis);
        this.xAxis = xAxis;
        mLines = new ArrayList<>();
        setPreferredSize(new Dimension(400, 160));
        setMinimumSize(new Dimension(300, 120));
    }

    public XyAxisCanvas(long[] yAxis, String[] yAxisStr, String[] xAxis) {
        setYAxis(yAxis, yAxisStr);
        this.xAxis = xAxis;

        mLines = new ArrayList<>();
        setPreferredSize(new Dimension(400, 160));
        setMinimumSize(new Dimension(300, 120));
    }


    public void addLine(String name, Color color, NodeStrQueue points) {
        mLines.add(new LineBeen(name, color, points));
        repaint();
    }

    public void addLine(LineBeen been) {
        mLines.add(been);
        repaint();
    }

    public void removeLine(LineBeen been) {
        mLines.remove(been);
        repaint();
    }

    public void clearLines() {
        mLines.clear();
        repaint();
    }

    public void setYAxis(long[] axis) {
        this.yAxis = axis;
        this.yAxisStr = new String[axis.length];
        for (int i = 0; i < axis.length; i++) {
            yAxisStr[i] = String.valueOf(axis[i]);
        }
    }

    public void setYAxis(long[] axis, String[] axisStr) {
        if (axis == null)
            return;
        if (axisStr == null || axis.length != axisStr.length) {
            setYAxis(axis);
            return;
        }

        this.yAxis = axis;
        this.yAxisStr = axisStr;
    }

    public void setXAxis(String[] xAxis) {
        this.xAxis = xAxis;
    }

    @Override
    public void repaint() {
        if (isUseCacheGraphic) {
            if (bi == null || (bi.getWidth() != getWidth() || bi.getHeight() != getHeight()))
                initBufferedCache();
            tg.setColor(getBackground());
            tg.fillRect(0, 0, bi.getWidth(), bi.getHeight());
            onDraw(tg);
        }
        super.repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (isUseCacheGraphic && bi != null) {
            g.drawImage(bi, 0, 0, this);
        } else
            onDraw(g);

    }

    private void onDraw(Graphics g) {
        Font originalFont = g.getFont();

        int maxWidth = 0;
        for (int i = 0; i < yAxisStr.length; i++) {
            maxWidth = Math.max(maxWidth, g.getFontMetrics().stringWidth(yAxisStr[i]));
        }

        int x = mPaddingLeft + maxWidth;
        int y = getHeight() - mPaddingBottom - g.getFont().getSize();
        int h = y - mPaddingTop - g.getFont().getSize();
        int w = getWidth() - mPaddingRight - x;
        long valueRange = yAxis[yAxis.length - 1] - yAxis[0];

        g.setColor(Color.red);
        // draw x axis
        g.drawLine(x, y, x + w, y);
        g.drawLine(x + w - 10, y - 5, x + w, y);
        g.drawLine(x + w - 10, y + 5, x + w, y);

        // draw y axis
        g.drawLine(x, mPaddingTop, x, y);
        g.drawLine(x, mPaddingTop, x - 5, mPaddingTop + 10);
        g.drawLine(x, mPaddingTop, x + 5, mPaddingTop + 10);

        h -= 10;
        w -= 10;

        // draw y axis words
        int perY = h / (yAxis.length - 1);
        for (int i = 0; i < yAxis.length; i++) {
            String str = yAxisStr[i];
            int offX = g.getFontMetrics().stringWidth(str);
            int offY = g.getFontMetrics().getHeight() >> 1;
            int cY = y - perY * i;
//            if (i == 0)
//                continue;
            g.setColor(Color.red);
            g.drawLine(x, cY, x - 1, cY);
            g.setColor(Color.darkGray);
            g.drawString(str, x - offX, cY);
        }

        int perX = w / (xAxis.length - 1);
        for (int i = 0; i < xAxis.length; i++) {
            String str = xAxis[i];
            int offX = g.getFontMetrics().stringWidth(str) >> 1;
            int offY = g.getFontMetrics().getHeight();
            int tx = x + perX * i;
            if (i != 0) {
                g.setColor(Color.lightGray);
                g.drawLine(tx, y, tx, y - h);
                g.setColor(Color.red);
                g.drawLine(tx, y, tx, y - 2);
            }

            g.setColor(Color.darkGray);
            g.drawString(str, tx - offX, y + offY);
        }

        // draw lines
        if (mLines.isEmpty())
            return;

        // draw line name and color
        {
            int maxWidthTitle = 0;
            int[] titleWidth = new int[mLines.size()];
            for (int i = 0; i < mLines.size(); i++) {
                LineBeen line = mLines.get(i);
                titleWidth[i] = g.getFontMetrics().stringWidth(line.getName());
                maxWidth += 30 + titleWidth[i];
            }
            int startX = w - maxWidthTitle >> 1;
            int startY = mPaddingTop;
            int center = startY - g.getFontMetrics().getHeight() / 4;
            for (int i = 0; i < mLines.size(); i++) {
                LineBeen line = mLines.get(i);
                g.setColor(line.getColor());
                g.drawLine(startX, center, startX + 24, center);
                switch (i % 5) {
                    case 2:
                        g.drawOval(startX + 10, center - 2, 4, 4);
                        break;
                    case 1:
                        g.drawRect(startX + 10, center - 2, 4, 4);
                        break;
                    case 0:
                        g.drawLine(startX + 10, center + 2, startX + 12, center - 2);
                        g.drawLine(startX + 12, center - 2, startX + 14, center + 2);
                        g.drawLine(startX + 10, center + 2, startX + 14, center + 2);
                        break;
                    case 3:
                        g.fillOval(startX + 10, center - 2, 4, 4);
                        break;
                    case 4:
                        g.fillRect(startX + 10, center - 2, 4, 4);
                        break;
                }
                g.setColor(Color.black);
                g.drawString(line.getName(), startX + 25, startY);
                startX += titleWidth[i] + 30;
            }
        }

        g.setFont(FONT_LINE_STR);

        for (int j = 0; j < mLines.size(); j++) {
            LineBeen line = mLines.get(j);
            Color color = line.getColor();
            NodeStrQueue nodeQueue = line.getQueue();
            g.setColor(color);
            if (nodeQueue.isEmpty())
                continue;

            for (int i = 0; i < nodeQueue.size() && i < xAxis.length; ++i) {
                Pair<Long, String> child = nodeQueue.elementAt(i);
                Long item = child.getKey();
                int axIndex = xAxis.length - 1 - i;
                int tx = perX * axIndex;
                tx += x;
                int ty = (int) (item - yAxis[0]);
                ty *= h;
                ty /= valueRange;
                ty = y - ty;

                switch (j % 5) {
                    case 2:
                        g.drawOval(tx - 2, ty - 2, 4, 4);
                        break;
                    case 1:
                        g.drawRect(tx - 2, ty - 2, 4, 4);
                        break;
                    case 0:
                        g.drawLine(tx - 2, ty + 2, tx, ty - 2);
                        g.drawLine(tx, ty - 2, tx + 2, ty + 2);
                        g.drawLine(tx - 2, ty + 2, tx + 2, ty + 2);
                        break;
                    case 3:
                        g.fillOval(tx - 2, ty - 2, 4, 4);
                        break;
                    case 4:
                        g.fillRect(tx - 2, ty - 2, 4, 4);
                        break;
                }


                String str = child.getValue();
                int offX = g.getFontMetrics().stringWidth(str) >> 1;
                if (axIndex > 0 && isShowPointStr)
                    g.drawString(str, tx - offX, ty - 3);
                if (i > 0) {
                    Long itemLast = nodeQueue.elementAt(i - 1).getKey();
                    int tyl = (int) (itemLast - yAxis[0]);
                    tyl *= h;
                    tyl /= valueRange;
                    tyl = y - tyl;
                    g.drawLine(tx + 2, ty, tx + perX - 2, tyl);
                }
            }
        }

        g.setFont(originalFont);
    }

    public static final void main(String[] args) {
        Frame frame = new Frame();
        String[] xAxis = new String[20];
        for (int i = 0; i < xAxis.length; i++) {
            xAxis[i] = String.valueOf(i);
        }
        long[] yAxis = new long[11];
        for (int i = 1; i < yAxis.length; i++) {
            yAxis[i] = i * 10;
        }
        XyAxisCanvas canvas = new XyAxisCanvas(yAxis, xAxis);
        frame.add(canvas);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                super.windowClosing(e);
            }
        });
        frame.setSize(800, 400);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);

        {
            NodeStrQueue points1 = new NodeStrQueue(20);
            for (int i = 0; i < 10; i++) {
                Long value = Math.round(Math.random() * 100);
                String str = String.valueOf(value);
                points1.push(new Pair<>(value, str));
            }
            canvas.addLine("RX", Color.BLUE, points1);
            System.out.println(points1);
        }
        {
            NodeStrQueue points1 = new NodeStrQueue(20);
            for (int i = 0; i < 19; i++) {
                int value = (int) (Math.random() * 50);
                String str = String.valueOf(value);
                points1.push(new Pair(value, str));
            }
            canvas.addLine("TX", Color.CYAN, points1);
            System.out.println(points1);
        }
        canvas.setYAxis(yAxis);
    }

    private BufferedImage createAlphaBufferedImage(int w, int h) {
        // 创建BufferedImage对象
        BufferedImage src = new BufferedImage(w, h,
                BufferedImage.TYPE_BYTE_INDEXED);
        // 获取Graphics2D
        Graphics2D g2d = src.createGraphics();
        src = null;
        // ---------- 增加下面的代码使得背景透明 -----------------
        src = g2d.getDeviceConfiguration().createCompatibleImage(w, h,
                Transparency.TRANSLUCENT);

        return src;
    }

    private void initBufferedCache() {
        bi = createAlphaBufferedImage(getWidth(), getHeight());
        tg = bi.createGraphics();
    }

    public boolean isShowPointStr() {
        return isShowPointStr;
    }

    public void setShowPointStr(boolean showPointStr) {
        isShowPointStr = showPointStr;
    }
}
