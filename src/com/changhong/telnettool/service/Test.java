package com.changhong.telnettool.service;

import com.alibaba.fastjson.JSONObject;
import com.changhong.telnettool.tool.IOUtils;
import com.changhong.telnettool.webinterface.Observer;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.BufferedOutputStream;
import java.io.IOException;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) {
        new Test();
//        System.out.println(getTotleValue(58394));

//        String data = "5O82OxVGintrP4p17UcrqTXCZrBJZUcbbfbh6XdQ9Ts+73eDzeY2iGmoZDmaGNVDDlgx2TUFgcrmlo3ZxliCU9hKVgV9mE6D6iN0h65ciEs+m8u/XJv5apwmhXcBqtguI5IXNCPqhdJdqJ95ker95dt3hGpFVIaEQgBaFlJqNOUWQyeQHxKPQFHuMp8yaogFWipco+tVIL0eqDCU3hD2opVORF6juPYGtUnSuk9NG+PqN3jGli/hVieNe6rbvO373bjT2BxKAlfBDHYv+fSV66e65CL98MbgxiXY2HV3VD2UGk/eC1kkvnhuDYguqap2n2F7oOW7aNfr1BZr5IzxS9kSWSDYzS4hcSTGH+C82P+/E4230MwI6+0bsddE+t5S";
//        String key = "91096204804670";
//        System.out.println(decrypt(data, key));
//        System.out.println(Pattern.matches("^[0-9]*$", "1234343"));
//        System.out.println(Pattern.compile("[^0-9]*$").matcher("12324324.gfh").replaceAll(""));
//
//        String mac = "00-E0-4C-36-14-2A";
//        System.out.println(Pattern.matches("^[A-Fa-f0-9]+$", mac));
//        System.out.println(Pattern.compile("[^A-Fa-f0-9]*").matcher(mac).replaceAll(""));
//
//        System.out.println("1====> " + clearMac(mac));
//        System.out.println("2====> " + clearMac("AABBCCDEEFF"));
//
//        System.out.println();
//
//        System.out.println(compareMac("aabbccddeeff","aa-bb-cc-dd-ee-ff"));
//        System.out.println(compareMac("aabbccddeeff","AA:bB:CC:DD:EE:FF"));


    }

    private static String clearMac(String mac) {
        if (Pattern.matches("^[A-Za-z]+$", mac))
            return mac;

        char[] chars = new char[12];
        for (int i = 0, j = 0; i < mac.length() && j < chars.length; i++) {
            char c = mac.charAt(i);
            if ((c <= 'z' && c >= 'a') || (c >= 'A' && c <= 'Z')) {
                chars[j++] = c;
            }
        }
        return new String(chars);
    }
    public static boolean compareMac(String mac1, String mac2) {
        if (mac1 == null && mac2 == null)
            return true;

        if (mac1 == null || mac2 == null)
            return false;

        if (!Pattern.matches("^[A-Fa-f0-9]+$", mac1))
            mac1 = Pattern.compile("[^A-Fa-f0-9]*").matcher(mac1).replaceAll("");
        if (!Pattern.matches("^[A-Fa-f0-9]+$", mac2))
            mac2 = Pattern.compile("[^A-Fa-f0-9]*").matcher(mac2).replaceAll("");

        return mac1.equalsIgnoreCase(mac2);
    }
    private static String loadJson(String response) {
        JSONObject jsobj = JSONObject.parseObject(response);
        String key = jsobj.getString("key");
        if (!jsobj.containsKey("data"))
            return null;
        String data = jsobj.getString("data");
        return decrypt(data, key);
    }

    /**
     * DES解密
     *
     * @return 解密出的字符串
     */
    public static String decrypt(String data, String key) {
        if (data == null) return null;
        if (key == null || key.length() == 0) return null;
        if (key.length() < 10) return null;
        key = key.substring(key.length() - 10, key.length() - 2);
        try {
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(data)), "GBK");
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    private static long getTotleValue(long origin) {
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

    public Test() {
        long startTime = System.currentTimeMillis();
        getServiceIp(new Observer<Set<String>>() {
            @Override
            public void onError(Throwable throwable) {
                System.err.println(throwable.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("----------------------complete --------------------");
            }

            @Override
            public void onNext(Set<String> strings) {
                System.out.println("获取到运行服务的ip ： " + strings + " , 耗时：" + (System.currentTimeMillis() - startTime));
                Iterator<String> iterator = strings.iterator();
                while (iterator.hasNext()) {
                    Observer<String> observer = new Observer<String>() {
                        @Override
                        public void onError(Throwable throwable) {
                            System.err.println("send error : " + throwable.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            System.out.println("----------------------send complete --------------------");
                        }

                        @Override
                        public void onNext(String s) {
                            System.out.println("send back: " + s);
                        }
                    };
                    String ip = iterator.next();
                    for (int i = 0; i < 20; i++) {
                        send(ip, 18882, i + "{\"method\":\"regist\"}", observer);
                    }
                }
            }
        });
    }

    private synchronized void getServiceIp(Observer<Set<String>> observer) {
        final int threadNum = 100;
        ExecutorService threadPool = Executors.newFixedThreadPool(threadNum);
        threadPool.submit(
                () -> {
                    try {
                        HashSet<String> result = new HashSet<>();
                        Vector arrTask = new Vector();
                        byte[] ip = InetAddress.getLocalHost().getAddress();
                        byte[] tempIp = new byte[ip.length];
                        System.arraycopy(ip, 0, tempIp, 0, tempIp.length);
                        System.out.println(Arrays.toString(tempIp));
                        for (int i = 1; i < 255; i++) {
                            while (arrTask.size() > threadNum - 2) {//等待线程池中线程执行完，再添加
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            tempIp[3] = (byte) i;
                            FindServerTask task = new FindServerTask(getIpName(tempIp), 18882, result, arrTask);
                            arrTask.add(task);
                            threadPool.submit(task);
                        }

                        //等待线程全部完成
                        while (arrTask.size() > 0) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        observer.onNext(result);
                        threadPool.shutdown();

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    private static String getIpName(byte[] ip) {
        StringBuilder sb = new StringBuilder();
        sb.append(ip[0] & 0xff).append('.').append(ip[1] & 0xff).append('.').append(ip[2] & 0xff).append('.').append(ip[3] & 0xff);
        return sb.toString();
    }


    private class FindServerTask implements Runnable {
        private String address;
        private int port;
        private Set<String> set;
        private Vector arrTask;

        public FindServerTask(String address, int port, Set<String> set, Vector arrTask) {
            this.address = address;
            this.port = port;
            this.set = set;
            this.arrTask = arrTask;
        }

        @Override
        public void run() {
            try {
                Socket client = new Socket();
                client.setSoTimeout(100);
                client.connect(new InetSocketAddress(address, port), 1000);
                System.out.println("远程主机地址：" + client.getRemoteSocketAddress());
                set.add(address);
                client.close();
            } catch (IOException e) {
                System.err.println("无法连接到" + address + ':' + port);
            } finally {
                arrTask.remove(this);
            }
        }
    }

    private void send(String ip, int port, String data, Observer<String> observer) {
        try {
            Socket client = new Socket();
            client.setSoTimeout(10000);
            client.connect(new InetSocketAddress(ip, port), 5000);
            client.setKeepAlive(true);
            BufferedOutputStream bos = new BufferedOutputStream(client.getOutputStream());
            byte[] temp = IOUtils.turnStr2Gzip(data);
            bos.write(temp);
            bos.flush();
            client.shutdownOutput();
//            Thread.sleep(1000);
            String str = IOUtils.turnGzip2Str(IOUtils.readFromIputStream(client.getInputStream()));
            client.shutdownInput();
            observer.onNext(str);
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
            observer.onError(e);
        } finally {
            observer.onComplete();
        }
    }

    private class SendTask extends FutureTask<String> {

        public SendTask(Callable<String> callable) {
            super(callable);
        }

        @Override
        public void run() {
            super.run();
        }
    }

}
