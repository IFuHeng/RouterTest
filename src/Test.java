import com.changhong.telnettool.tool.IOUtils;
import com.changhong.telnettool.webinterface.Observer;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class Test {
    public static void main(String[] args) {
        new Test();
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
                        send(ip, 18882, i + " = 你咩咩立即释放垃圾", observer);
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
