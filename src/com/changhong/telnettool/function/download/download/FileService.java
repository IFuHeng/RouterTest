package com.changhong.telnettool.function.download.download;

import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FileService {

    /**
     * @param path
     * @return
     */
    public synchronized Map<Byte, Integer> getData(String path) {
        String fileName = new String(Base64.getEncoder().encode(path.getBytes()));
        return read(fileName);
    }

    /**
     * @param path
     * @param map
     */
    public synchronized void save(String path, Map<Byte, Integer> map) {//int threadid, int position
        String fileName = new String(Base64.getEncoder().encode(path.getBytes()));
        write(fileName, map);
    }


    /**
     * @param path
     */
    public synchronized void update(String path, int threadId, int pos) {
        String fileName = new String(Base64.getEncoder().encode(path.getBytes()));

        Map<Byte, Integer> map = read(fileName);
        map.put((byte) threadId, pos);
        write(fileName, map);
    }

    /**
     * @param path
     */
    public synchronized void delete(String path) {
        String fileName = new String(Base64.getEncoder().encode(path.getBytes()));
        new File(fileName).deleteOnExit();
    }

    private void write2(String fileName, Map<Integer, Integer> map) {
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(fileName));
            Iterator<Map.Entry<Integer, Integer>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, Integer> next = iterator.next();
                bufferedWriter.write(String.valueOf(next.getKey()));
                bufferedWriter.write('=');
                bufferedWriter.write(String.valueOf(next.getValue()));
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedWriter != null)
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    private Map<Integer, Integer> read2(String fileName) {
        Map<Integer, Integer> data = new HashMap<Integer, Integer>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileName));

            for (String temp = br.readLine(); temp != null; temp = br.readLine()) {
                if (temp.length() == 0)
                    continue;

                int index = temp.indexOf('=');
                if (index == -1)
                    continue;

                int key = Integer.parseInt(temp.substring(0, index));
                int value = Integer.parseInt(temp.substring(index + 1));
                data.put(key, value);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return data;
    }

    /**
     * 存储key value 对，key仅1字节，value4字节
     *
     * @param fileName
     * @param map
     */
    private void write(String fileName, Map<Byte, Integer> map) {
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(fileName));
            Iterator<Map.Entry<Byte, Integer>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Byte, Integer> next = iterator.next();
                byte key = next.getKey();
                bufferedOutputStream.write(key);
                int value = next.getValue();
                for (int i = 0; i < 4; i++) {
                    bufferedOutputStream.write(value >> ((3 - i) * 8));
                }
            }
            bufferedOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedOutputStream != null)
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    /**
     * 读取key value 对，key仅1字节，value4字节
     *
     * @param fileName
     * @return
     */
    private Map<Byte, Integer> read(String fileName) {
        Map<Byte, Integer> data = new HashMap<>();
        BufferedInputStream is = null;

        try {
            is = new BufferedInputStream(new FileInputStream(fileName));
            byte[] tBuf = new byte[5];
            int size;
            while ((size = is.read(tBuf)) != -1) {
                if (size != 5)
                    break;

                byte key = tBuf[0];
                int value = 0;
                for (int i = 0; i < 4; i++) {
                    value = (value << 8) | (tBuf[i + 1] & 0xff);
                }
                data.put(key, value);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return data;
    }

}
