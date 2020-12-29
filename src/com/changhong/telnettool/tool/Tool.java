package com.changhong.telnettool.tool;

import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Tool {
    public static boolean isDigit(String str) {
        if (str == null)
            return false;

        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static final String PATTERN_IPV4 = "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$";

    public static boolean isIpv4(String ip) {
        return Pattern.matches(PATTERN_IPV4, ip);
    }

    public static String onlyDigit(String str) {
        return Pattern.compile("[^0-9]").matcher(str).replaceAll("");
    }

    public static int turnString2Int(String str, int defaultValue) {
        if (str == null || str.isEmpty())
            return defaultValue;
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static String getIp() {
        try {
            InetAddress ip4 = Inet4Address.getLocalHost();
            return ip4.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "0.0.0.0";
        }
    }

    public static String getGuessGateway() {
        try {
            InetAddress ip4 = Inet4Address.getLocalHost();
            byte[] ip = ip4.getAddress();
            return String.format("%d.%d.%d.1", ip[0] & 0xff, ip[1] & 0xff, ip[2] & 0xff);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "0.0.0.0";
        }
    }

    public static String turnTxt2HtmlFormat(String str) {
        if (!str.contains("\\n"))
            return str;
        StringBuilder sb = new StringBuilder();
        sb.append("<html><HTML><body>");

        int start = 0;
        int end = str.indexOf("\\n");

        do {
            if (start != 0)
                sb.append("<br>");

            sb.append(str, start, end);

            start = end + 2;
            if (start > str.length())
                break;
            end = str.indexOf("\\n", start);
            if (end == -1)
                end = str.length();
        } while (true);

        sb.append("</body></html>");

        return sb.toString();
    }

    private static final boolean SHOW_LOG = false;

    public static final <T> void log(T t) {
        if (SHOW_LOG)
            System.out.println(t);
    }

    static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public static String getTime() {
        long time = System.currentTimeMillis();
        return sdf.format(new Date(time)) + ' ' + String.format("%03d", time % 1000);
    }

    public static String loadLocalFile(String path) {
        InputStream inputStream = null;
        try {
            URL url = Tool.class.getClassLoader().getResource(path);
            if (url == null) {
                File file = new File(path);
                if (!file.exists())
                    return null;
                try {
                    url = new URL("file:/" + file.getAbsolutePath());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            inputStream = url.openStream();
            return readFile(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String loadFile(String path) {
        File file = new File(path);
        if (!file.exists())
            return null;
        InputStream inputStream = null;
        try {
            URL url = new URL("file:/" + file.getAbsolutePath());
            inputStream = url.openStream();
            return readFile(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void writeFile(File outFile, String content, String charset) {
        OutputStreamWriter osw = null;
        try {
            osw = new OutputStreamWriter(new FileOutputStream(outFile), charset);
            osw.write(content);
            osw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String readFile(InputStream is) throws IOException {
        if (is == null)
            return null;

        return new String(readFromIputStream(is));
    }

    public static byte[] readFromIputStream(InputStream is) throws IOException {
        if (is == null)
            return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[128];
        do {
            int length = is.read(buf);
            if (length == -1)
                break;
            baos.write(buf, 0, length);
        } while (true);
        return baos.toByteArray();
    }

    public static boolean compareMac(String mac1, String mac2) {
        if (mac1 == null || mac2 == null)
            return false;

        if ((mac1.length() == 17 && mac2.length() == 17)
                || (mac1.length() == 12 && mac2.length() == 12)) {
            return mac1.toUpperCase().compareTo(mac2.toUpperCase()) == 0;
        }
        return false;
    }

    public static URL getUrl(String path) {
        URL url = DataManager.class.getClassLoader().getResource(path);
        if (url == null) {
            File file = new File(path);
            if (!file.exists())
                return null;
            try {
                url = new URL("file:/" + file.getAbsolutePath());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
        }
        return url;
    }

    /**
     * @return 获取屏幕尺寸后的缩放倍率
     */
    public static final float getScreenSizeLevel() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double value = screenSize.getWidth() * screenSize.getHeight();
        value /= 1000000;
        if (value >= 8)
            return 2.5f;
        else if (value >= 3)
            return 2;
        else if (Math.round(value) >= 2)
            return 1.5f;
        return 1;
    }

    /**
     * 统一设置字体，父界面设置之后，所有由父界面进入的子界面都不需要再次设置字体
     */
    public static void setGlobalFontRelative(float relative) {

        log("===================InitGlobalFont START======================");
        HashSet<String> set = new HashSet<>();
        for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);

            if (value == null) {
//                System.out.println(">>>> empty value  :       " + key + " , " + value);
//                System.out.println("other ui resource  :       " + key + " , " + value);
            } else {
                String className = value.getClass().getName();
                if (!set.contains(className)) {
                    set.add(className);
                }
            }
            if (key.toString().contains("Border")) {
                if (value instanceof FontUIResource) {
                    FontUIResource old = (FontUIResource) value;
                    log("******find new class type : " + key + "  ,   " + old.getName() + " , " + old.getFontName() + ", " + old.getPSName() + " , " + old.getStyle() + " , " + old.getSize());
                }
            }

            if (value instanceof FontUIResource) {
                FontUIResource old = (FontUIResource) value;
                FontUIResource newOne = new FontUIResource(new Font(old.getName(), old.getStyle(), Math.round(old.getSize() * relative)));
//                System.out.println(key + " : " + old.getSize2D() + " ====> " + newOne.getSize2D());
                UIManager.put(key, newOne);
            } else if (value instanceof DimensionUIResource) {
                DimensionUIResource old = (DimensionUIResource) value;
                old.width *= relative;
                old.height *= relative;
            } else if (value instanceof InsetsUIResource) {
                InsetsUIResource old = (InsetsUIResource) value;
                old.bottom *= relative;
                old.top *= relative;
                old.left *= relative;
                old.right *= relative;
            } else {

            }
        }

        log("===================InitGlobalFont  END======================");
    }

    public static final void showByteArrayData(byte[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (i == 0)
                System.out.print("[\n\t");
            if (i != 0)
                System.out.print(",\t");
            System.out.printf("%02X", arr[i]);

            if (i == arr.length - 1)
                System.out.println("\n]");
            if (i % 16 == 15)
                System.out.println('\t');
        }
    }

    public static final String turnByteArrayData2HexString(byte[] arr) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < arr.length; i++) {
            if (i != 0)
                sb.append(", ");
            sb.append(String.format("%02X", arr[i]));
        }
        sb.append(']');
        return sb.toString();
    }

    /**
     * 解析hexdump命令得到的结果转为字节数组
     *
     * @param str hexdump命令的返回数据
     * @return
     */
    public static final byte[] analysisHexDump(String str) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        BufferedReader br = new BufferedReader(new StringReader(str));
        String tempStr;
        try {
            while ((tempStr = br.readLine()) != null) {
                if (tempStr.charAt(0) == '*')
                    continue;

                String[] tempArr = tempStr.split(" ");
                if (tempArr[0].length() > 4) {
                    int index = Integer.parseInt(tempArr[0], 16);
                    if (baos.size() < index)
                        for (int i = baos.size(); i < index; i++) {
                            baos.write(0);
                        }
                }
                for (int i = 1; i < tempArr.length; i++) {
                    int value = Integer.parseInt(tempArr[i], 16);
                    byte b1 = (byte) (value & 0xff);
                    byte b2 = (byte) ((value >> 8) & 0xff);
                    baos.write(b1);
                    if (i == tempArr.length - 1 && b2 == 0)
                        continue;

                    baos.write(b2);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }


    /**
     * 将long型mac转为字符串，带冒号
     *
     * @param mac
     * @return
     */
    public static final String turnMacString(long mac) {
        byte[] bs = new byte[6];
        long temp = mac;
        for (int i = 0; i < bs.length; i++) {
            bs[bs.length - 1 - i] = (byte) ((temp >> (i * 8)) & 0xff);
        }
        return String.format("%02X:%02X:%02X:%02X:%02X:%02X", bs[0], bs[1], bs[2], bs[3], bs[4], bs[5]);
    }

    /**
     * 将long型mac转为字符串，不带冒号
     *
     * @param mac
     * @return
     */
    public static final String turnMacStringNoColon(long mac) {
        return String.format("%012X", mac);
    }

    /**
     * 将int转为ipv4格式
     *
     * @param ip
     * @return
     */
    public static final String turn2IpV4(int ip) {
        int[] ips = new int[4];
        for (int i = 0; i < 4; i++) {
            ips[3 - i] = (ip >>> (i * 8)) & 0xff;
        }
        return String.format("%d.%d.%d.%d", ips[0], ips[1], ips[2], ips[3]);
    }

    /**
     * 将mac字符串型转为长整型
     *
     * @param mac
     * @return
     */
    public static final long analysisMacString2Long(String mac) {
        if (mac == null)
            return 0;
        if (mac.indexOf(':') != -1)
            mac = mac.replace(":", "");
        try {
            return Long.parseLong(mac, 16);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * @param speed unit byte
     * @return
     */
    public static String getSpeedString(long speed) {
        if (speed >= 1024 * 1024)
            return String.format("%.1fMB/s", speed / 1024f / 1024).replace(".0", "");
        if (speed >= 1024)
            return String.format("%.1fKB/s", speed / 1024f).replace(".0", "");

        return speed + " B/s";
    }

    /**
     * @param speed unit bit
     * @return
     */
    public static String getSpeedStringBit(long speed) {
//        speed *= 8;
        if (speed >= 1024 * 1024)
            return String.format("%.1fMbps", speed / 1024f / 1024).replace(".0", "");
        if (speed >= 1024)
            return String.format("%.1fKbps", speed / 1024f).replace(".0", "");

        return speed + " bps";
    }

    /**
     * @param count unit byte
     * @return
     */
    public static String getCountString(long count) {
        if (count > (1l << 30))
            return String.format("%.1fGB", (count >> 20) / 1024f).replace(".0", "");
        if (count >= 1024 * 1024)
            return String.format("%.1fMB", count / 1024f / 1024).replace(".0", "");
        if (count >= 1024)
            return String.format("%.1fKB", count / 1024f).replace(".0", "");

        return count + " B";
    }

    /**
     * @param count unit bit
     * @return
     */
    public static String getCountStringBit(long count) {
        count *= 8;
        if (count > (1l << 30))
            return String.format("%.1fGb", (count >> 20) / 1024f).replace(".0", "");
        if (count >= 1024 * 1024)
            return String.format("%.1fMb", (count >> 10) / 1024f).replace(".0", "");
        if (count >= 1024)
            return String.format("%.1fKb", count / 1024f).replace(".0", "");

        return count + " b";
    }

    public static String turnTimeString(int secounds) {
        int minute = secounds / 60;
        int hour = minute / 60;
        minute %= 60;
        secounds %= 60;
        if (hour > 0) {
            return String.format("%d时%d分%d秒", hour, minute, secounds);
        } else if (minute > 0) {
            return String.format("%d分%d秒", minute, secounds);
        } else
            return secounds + "秒";
    }

    /**
     * 将字符串转为16进制字符串
     *
     * @param str
     * @return
     */
    public static String turnString2HexString(String str) {
        byte[] ssidb = str.getBytes();
        StringBuilder sb = new StringBuilder(ssidb.length * 2);
        for (byte b : ssidb) {
            sb.append(String.format("%02X", b & 0xff));
        }
        return sb.toString();
    }

    /**
     * @title 使用默认浏览器打开
     * @author Xingbz
     */
    private static void browse2(String url) throws Exception {
        Desktop desktop = Desktop.getDesktop();
        if (Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE)) {
            URI uri = new URI(url);
            desktop.browse(uri);
        }
    }

    public static byte[] gzip(byte[] data) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            GZIPOutputStream out = new GZIPOutputStream(baos);
            out.write(data);
            out.flush();
            out.close();
//            System.out.print(baos.size() + " / " + data.length + " = ");
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] zip(byte[] data) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream out = new ZipOutputStream(baos);
            out.putNextEntry(new ZipEntry("123"));
            out.write(data);
            out.flush();
            out.close();
//            System.out.print(baos.size() + " / " + data.length + " = ");
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
