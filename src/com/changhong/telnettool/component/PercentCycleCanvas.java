package com.changhong.telnettool.component;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadPoolExecutor;

public class PercentCycleCanvas extends Canvas implements Runnable {

    private static final int PADDING_DEFAULT = 10;
    private static final int DURATION = 1000;
    private static final long PER_INTERVAL = 25;

    private int mPaddingLeft = PADDING_DEFAULT;
    private int mPaddingTop = PADDING_DEFAULT;
    private int mPaddingRight = PADDING_DEFAULT;
    private int mPaddingBottom = PADDING_DEFAULT;

    private boolean isUseCacheGraphic = true;

    private BufferedImage bi;

    private Graphics2D tg;

    private Font FONT_VALUE_STR = new Font(Font.SANS_SERIF, Font.BOLD, 30);
    private Font FONT_TITLE_STR = new Font(Font.SANS_SERIF, Font.PLAIN, 15);

    private String title;

    private int percent;
    private double showPercent;
    private boolean isRunning;
    private ThreadPoolExecutor mPoolExecutor;

    public PercentCycleCanvas(String title) {
        this.title = title;
        setMinimumSize(new Dimension(100, 100));
        setSize(160, 160);
    }

    public PercentCycleCanvas(String title, ThreadPoolExecutor poolExecutor) {
        this.title = title;
        setMinimumSize(new Dimension(100, 100));
        setSize(160, 160);
        this.mPoolExecutor = poolExecutor;
    }

    public void setmPoolExecutor(ThreadPoolExecutor poolExecutor) {
        this.mPoolExecutor = poolExecutor;
    }

    @Override
    public void setSize(int width, int height) {
        FONT_VALUE_STR = new Font(Font.SANS_SERIF, Font.BOLD, (int) Math.round(Math.sqrt(width * height) / 5));
        FONT_TITLE_STR = new Font(Font.SANS_SERIF, Font.PLAIN, FONT_VALUE_STR.getSize() / 2);
        super.setSize(width, height);
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

    /**
     * @param percent 0~1
     */
    public void setPercent(double percent) {
        if (percent > 1)
            percent = 1;
        else if (percent < 0)
            percent = 0;
        this.percent = (int) Math.round(percent * 100);

        if (!isRunning) {
            if (Math.round(showPercent * 100) != this.percent) {
                isRunning = true;
                if (mPoolExecutor != null)
                    mPoolExecutor.submit(this);
                else
                    new Thread(this).start();
            }
        }
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

        int h = getHeight() - mPaddingTop - mPaddingBottom;
        int w = getWidth() - mPaddingRight - mPaddingLeft;

        int radius = Math.min(w, h);


        {//绘制进度
            Graphics2D g_2d = (Graphics2D) g;
            BasicStroke bs_1 = new BasicStroke(16, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
            BasicStroke bs_2 = new BasicStroke(10f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
            BasicStroke bs_3 = new BasicStroke(16f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND);
            g.setColor(Color.lightGray);
            Arc2D.Float af = new Arc2D.Float(getWidth() - radius >> 1, getHeight() - radius >> 1, radius, radius, -135, -270, Arc2D.OPEN);
            g_2d.setStroke(bs_2);
            g_2d.draw(af);
            if (showPercent > 0) {
                g.setColor(new Color(0xff00BFFF));
                af = new Arc2D.Float(getWidth() - radius >> 1, getHeight() - radius >> 1, radius, radius, -135, Math.round(-270 * showPercent), Arc2D.OPEN);
                g_2d.draw(af);
            }
        }

        {//绘制文字
            g.setColor(Color.darkGray);
            g.setFont(FONT_TITLE_STR);
            int strWidth = (int) g.getFontMetrics().stringWidth(title);
            int strHeight = (int) g.getFont().getSize();
            g.drawString(title, getWidth() - strWidth >> 1, (getHeight() + radius) / 2 - FONT_TITLE_STR.getSize());

            String percentStr = String.format("%d%%", Math.round(showPercent * 100));
            g.setFont(FONT_VALUE_STR);
            strWidth = (int) g.getFontMetrics().stringWidth(percentStr);
            strHeight = (int) g.getFont().getSize();
//            System.out.println(strHeight);
            g.drawString(percentStr, getWidth() - strWidth >> 1, getHeight() + strHeight >> 1);

        }

        g.setFont(originalFont);
    }

//    public static final void main(String[] args) {
//        Frame frame = new Frame();
//
//        PercentCycleCanvas canvas = new PercentCycleCanvas("cpu 使用率");
//        frame.add(canvas);
//        frame.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                frame.dispose();
//                super.windowClosing(e);
//            }
//        });
//        frame.setSize(200, 200);
//        frame.setResizable(false);
//        frame.setVisible(true);
//        frame.pack();
//        frame.setLocationRelativeTo(null);
//        frame.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                super.windowClosing(e);
//                System.exit(0);
//            }
//        });
//
////        for (float i = 0; i <100; i++) {
////            System.out.println(canvas.getInterpolation(i/100));
////        }
//
//        for (int i = 0; i < Integer.MAX_VALUE; i++) {
//            canvas.setPercent(Math.random());
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }

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

    public float getInterpolation(float input) {
        return (float) (Math.cos((input + 1) * Math.PI) / 2.0f) + 0.5f;
    }

    @Override
    public void run() {
        double per = 1.0 * PER_INTERVAL / DURATION;
        while (isRunning) {
            double x = percent * 1.0 / 100 - showPercent;
//            double offX = getInterpolation();
            showPercent += x > 0 ? Math.min(x, per) : Math.max(x, -per);
//            System.out.println("showPercent = " + showPercent);
            repaint();

            if (percent == Math.round(showPercent * 100)) {
                isRunning = false;
                break;
            }

            try {
                Thread.sleep(PER_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
