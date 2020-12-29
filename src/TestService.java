import com.changhong.telnettool.tool.IOUtils;

import java.net.*;
import java.io.*;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestService extends Observable implements Runnable {

    private final ExecutorService threadPool;
    private ServerSocket serverSocket;
    private boolean isRunning = true;

    public TestService(int port, Observer observer) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        addObserver(observer);
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

    public void run() {
        while (isRunning) {
            try {
                System.out.println("等待远程连接，端口号为：" + serverSocket.getLocalPort() + "...");
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
                System.out.println("远程主机地址：" + socket.getRemoteSocketAddress());
                String inStr = IOUtils.turnGzip2Str(IOUtils.readFromIputStream(socket.getInputStream()));
                System.out.println(inStr);
                socket.shutdownInput();
                byte[] temp = IOUtils.turnStr2Gzip("谢谢连接我：" + socket.getLocalSocketAddress());
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(temp);
                outputStream.flush();
                socket.shutdownOutput();
            } catch (IOException e) {
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

    public static void main(String[] args) {
        new TestService(18882, new Observer() {
            @Override
            public void update(Observable o, Object arg) {

            }
        });
    }
}
