package com.changhong.telnettool.service;

import com.alibaba.fastjson.JSONObject;
import com.changhong.telnettool.service.function.RegistFunction;
import com.changhong.telnettool.tool.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestService extends Observable implements Runnable {

    public static final String SOFT_VER = "1.0";
    public static final int PORT_OF_CLIENT = 18883;

    private final ExecutorService threadPool;
    private ServerSocket serverSocket;
    private boolean isRunning = true;

    private HashMap<String, ServiceCallback> mHashCallback = new HashMap<>();

    public TestService(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        threadPool = Executors.newFixedThreadPool(10);
        threadPool.submit(this);
    }

    public void close() {
        isRunning = false;
        if (threadPool != null && !threadPool.isShutdown())
            threadPool.shutdownNow();

        if (!serverSocket.isClosed())
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    /**
     * 注册功能
     *
     * @param name
     * @param o
     */
    public synchronized void registFunction(String name, ServiceCallback o) {
        super.addObserver(o);
        if (mHashCallback.containsKey(name)) {
            System.err.printf("TestService.register([%s,%s])  and replace the callback([%s,%s]) \n", name, o, name, o);
        } else {
            System.err.printf("TestService.register([%s,%s]) \n", name, o);
            mHashCallback.put(name, o);
        }
    }

    /**
     * 注销
     *
     * @param name
     */
    public synchronized void unregistFunction(String name) {
        System.err.printf("TestService.unregister([%s,%s])\n", name, mHashCallback.get(name));
        if (mHashCallback.containsKey(name)) {
            super.deleteObserver(mHashCallback.get(name));
            mHashCallback.remove(name);
        }
    }

    public void run() {
        System.out.println("等待远程连接，端口号为：" + serverSocket.getLocalPort() + "...");
        while (isRunning) {
            try {
                Socket server = serverSocket.accept();
                threadPool.submit(new Task(server));
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private class Task implements Runnable {
        Socket socket;

        private Task(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                socket.setSoTimeout(10000);

                String ip = socket.getInetAddress().getHostAddress();

                System.out.println("远程主机地址：" + socket.getRemoteSocketAddress());


                String inStr = read(socket);
                socket.shutdownInput();
                if (inStr == null || inStr.length() == 0) {//未读取到数据，退出
                    write(socket, "empty content");
                    return;
                }
                System.out.println(inStr);
                JSONObject json = JSONObject.parseObject(inStr);
                String method = json.getString("method");

                String response;
                if (!mHashCallback.containsKey(method)) {
                    response = JSONObject.toJSONString(new ResponseBeen<>(404, "no that method", null));
                } else try {
                    ServiceCallback callback = mHashCallback.get(method);
                    response = JSONObject.toJSONString(callback.callback(socket.getInetAddress().getHostAddress(), json.getString("data")));
                } catch (Exception e) {
                    e.printStackTrace();
                    response = JSONObject.toJSONString(new ResponseBeen<>(505, e.getMessage(), null));
                }

                write(socket, response);
                socket.shutdownOutput();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String read(Socket socket) throws IOException {
        byte[] inputData = IOUtils.readFromIputStream(socket.getInputStream());
        if (inputData == null || inputData.length == 0)//未读取到数据，退出
            return null;

        String inStr = IOUtils.turnGzip2Str(inputData);
        return inStr;
    }

    private void write(Socket socket, String str) throws IOException {
        byte[] temp = IOUtils.turnStr2Gzip(str);
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(temp);
        outputStream.flush();
    }

    public static void main(String[] args) {
        TestService service = new TestService(18882);

        RegistFunction f = new RegistFunction();
        service.registFunction("register", f);
    }
}
