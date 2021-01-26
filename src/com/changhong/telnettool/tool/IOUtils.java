package com.changhong.telnettool.tool;

import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class IOUtils {

    /**
     * 转字符串为压缩字节数组
     *
     * @param str
     * @return
     * @throws IOException
     */
    public static byte[] turnStr2Gzip(String str) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(baos)));
        bw.write(str);
        bw.flush();
        bw.close();
        return baos.toByteArray();
    }

    /**
     * 转GZIP格式字节数组为字符串
     *
     * @param zipData
     * @return
     * @throws IOException
     */
    public static String turnGzip2Str(byte[] zipData) throws IOException {
        DataInputStream dis = new DataInputStream(new GZIPInputStream(new ByteArrayInputStream(zipData)));
        return readFile(dis);
    }

    public static String loadLocalFile(String path) {
        InputStream inputStream = null;
        try {
            URL url = IOUtils.class.getClassLoader().getResource(path);
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
//            if (!outFile.exists()) {
//                File parent = outFile.getParentFile();
//                if (!parent.exists())
//                    parent.mkdirs();
//                outFile.createNewFile();
//            }

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

    public static Image loadLocalImage(String path) {
        URL url = IOUtils.class.getClassLoader().getResource(path);
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

        return Toolkit.getDefaultToolkit().getImage(url);
    }
}
