package com.changhong.telnettool.net.tftpserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class TFtpServer2 extends Thread {

    public static final int DEFAULT_PORT = 69;

    private int port = DEFAULT_PORT;

    private Callback<ReceiveFile> callback;

    private Timer timer = new Timer(true);

    public TFtpServer2(int port) {
        this.port = port;
    }

    public TFtpServer2() {
    }

    public void setObserver(Callback<ReceiveFile> callback) {
        this.callback = callback;
    }

    public void run() {
        DatagramSocket ds;
        try {
            ds = new DatagramSocket(new InetSocketAddress(port));
        } catch (SocketException e) {
            System.out.println("error when create datagram socket:"
                    + e.getMessage());

            if (callback != null) {
                callback.onError(e);
            }
            return;
        }

        DatagramPacket packet = new DatagramPacket(new byte[516], 516);

        boolean running = true;
        HashMap<String, Client> map = new HashMap<>();
        while (running) {
            try {
                ds.receive(packet);
//                System.err.print("recv packet:");
//                TFtpPacketUtil.displayBytes(packet.getData());//
                process(ds, packet, map);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    protected void process(DatagramSocket ds, DatagramPacket packet, HashMap<String, Client> map) {
        try {
            byte[] buf = new byte[packet.getLength()];
            System.arraycopy(packet.getData(), 0, buf, 0, buf.length);
            TFtpPacket tfp = TFtpPacketUtil.decode(buf);
            InetSocketAddress address = new InetSocketAddress(packet
                    .getAddress(), packet.getPort());
            String key = address.getHostName() + ":" + address.getPort();
            System.out.println("key=" + key);
            System.out.println("packet:" + tfp);
            if (tfp.getOperateCode() == EnumTftpActCode.RRQ
                    || tfp.getOperateCode() == EnumTftpActCode.WRQ) {
                Client client = new Client(address);
                boolean result = client.doAccept(ds, (RWPacket) tfp);
                if (result) {
                    map.put(key, client);
                }
            } else if (tfp.getOperateCode() == EnumTftpActCode.DATA) {
                if (!map.containsKey(key))
                    return;
                Client client = map.get(key);
                boolean result = client.doProcess(ds, (DATAPacket) tfp);
                if (result) {
                    map.remove(key);
                    client.destroy();
                }
            } else if (tfp.getOperateCode() == EnumTftpActCode.ACK) {
                Client client = (Client) map.get(key);
                if (client == null) {
                    return;
                }
                boolean result = client.doProcess(ds, (ACKPacket) tfp);
                if (result) {
                    map.remove(key);
                    client.destroy();
                }
            } else if (tfp.getOperateCode() == EnumTftpActCode.ERROR) {
                System.out.println(tfp);
                Client client = (Client) map.remove(key);
                client.destroy();
            }
        } catch (BadPacketFormatException e) {
            e.printStackTrace();
            System.out.println("recv unknown packet.");
        }
    }

    protected void send(DatagramSocket ds, InetSocketAddress address, byte[] data) {
        DatagramPacket packet = new DatagramPacket(data, data.length, address
                .getAddress(), address.getPort());
        try {
            ds.send(packet);
            System.out.println("send packet:");
            TFtpPacketUtil.displayBytes(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class Client {
        public static final int DEFAULT_DATA_SIZE = 512;

        public static final int DEFAULT_TIME_OUT = 1000;

        private InetSocketAddress address;

        private String fileName;

        private int block;

        private boolean checked;//上次发送的包已收到回应

        private RandomAccessFile raf;

        private boolean accepted = false;

        private byte[] buf = new byte[DEFAULT_DATA_SIZE];

        private byte[] data;

        private int times = 0;//每几次重发

        private ResendTask task;

        public Client(InetSocketAddress address) {
            this.address = address;
        }

        public void destroy() {
            if (task != null) {
                task.cancel();
            }
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                raf = null;
            }
        }

        /**
         * if accept,return true;else return false.
         *
         * @param ds
         * @param packet
         * @return
         */
        boolean doAccept(DatagramSocket ds, RWPacket packet) {
            this.fileName = packet.getFileName();
            if (accepted) {
                return true;
            }
            if (packet.getOperateCode() == EnumTftpActCode.RRQ) {
                try {
                    File file = new File(".", fileName);
                    System.out.println(file.getPath() + " " + file.exists());
                    raf = new RandomAccessFile(file, "r");
                    data = new byte[DEFAULT_DATA_SIZE];
                    int size = raf.read(data);
                    DATAPacket dp = new DATAPacket();
                    dp.setBlock(1);
                    if (size != DEFAULT_DATA_SIZE) {
                        byte[] buf = new byte[size];
                        System.arraycopy(data, 0, buf, 0, size);
                        data = buf;
                    }
                    dp.setData(data);
                    data = TFtpPacketUtil.encode(dp);
                    send(ds, address, data);
                    block = 1;
                    task = new ResendTask(ds,address, data);
                    timer.schedule(task, DEFAULT_TIME_OUT);
                } catch (FileNotFoundException e) {
                    System.out
                            .println("file:" + e.getMessage() + " not found.");
                    ERRORPacket ep = new ERRORPacket();
                    ep.setErrorCode(ERRORPacket.FILE_NOT_FOUND_CODE);
                    byte[] data = TFtpPacketUtil.encode(ep);
                    send(ds, address, data);
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                File file = new File(".", fileName);
                if (file.exists()) {
                    ERRORPacket ep = new ERRORPacket();
                    ep.setErrorCode(ERRORPacket.FILE_EXIST_CODE);
                    ep.setErrorMessage("file:" + fileName + " has exist");
                    byte[] data = TFtpPacketUtil.encode(ep);
                    send(ds,address, data);
                    return false;
                }
                try {
                    file.createNewFile();
                    raf = new RandomAccessFile(file, "rwd");
                } catch (IOException e) {
                    System.out.println("create file:" + fileName + " failed.");
                    ERRORPacket ep = new ERRORPacket();
                    ep.setErrorCode(ERRORPacket.NOT_DEFINED_CODE);
                    byte[] data = TFtpPacketUtil.encode(ep);
                    send(ds,address, data);
                    return false;
                }
                ACKPacket ap = new ACKPacket();
                ap.setBlock(0);
                data = TFtpPacketUtil.encode(ap);
                send(ds,address, data);
                task = new ResendTask(ds,address, data);
                timer.schedule(task, DEFAULT_TIME_OUT);
            }
            accepted = true;
            return accepted;
        }

        /**
         * if transfer end,return true,else return false.
         *
         *
         * @param ds
         * @param packet
         * @return
         */
        public boolean doProcess(DatagramSocket ds, ACKPacket packet) {
            if (task != null) {
                task.cancel();
                task = null;
            }
            if (packet.getBlock() == block) {
                try {
                    if (raf == null
                            || raf.length() <= block * DEFAULT_DATA_SIZE) {
                        return true;
                    }
                    raf.seek(block * DEFAULT_DATA_SIZE);
                    int size = raf.read(buf);
                    data = buf;
                    if (size < DEFAULT_DATA_SIZE) {
                        data = new byte[size];
                        System.arraycopy(buf, 0, data, 0, size);
                        raf.close();
                        raf = null;
                    }
                    DATAPacket dp = new DATAPacket();
                    block++;
                    dp.setBlock(block);
                    dp.setData(data);
                    data = TFtpPacketUtil.encode(dp);
                    send(ds,address, data);
                    TFtpPacketUtil.displayBytes(data);
                    task = new ResendTask(ds,address, data);
                    timer.schedule(task, DEFAULT_TIME_OUT);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        /**
         * if transfer end,return true,else return false.
         *
         *
         * @param ds
         * @param packet
         * @return
         */
        public boolean doProcess(DatagramSocket ds, DATAPacket packet) {
            if (task != null) {
                task.cancel();
                task = null;
            }
            byte[] data = packet.getData();
            if (packet.getBlock() != block + 1) {
                return false;
            }
            block++;
            ACKPacket ap = new ACKPacket();
            ap.setBlock(block);
            if (data != null) {
                try {
                    raf.write(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (data == null || data.length < DEFAULT_DATA_SIZE) {
                try {
                    raf.close();
                    raf = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            data = TFtpPacketUtil.encode(ap);
            send(ds,address, data);
            task = new ResendTask(ds,address, data);
            timer.schedule(task, DEFAULT_TIME_OUT);
            return packet.getData() == null
                    || packet.getData().length < DEFAULT_DATA_SIZE;
        }

    }

    class ResendTask extends TimerTask {
        private InetSocketAddress address;

        private byte[] data;

        private int times = 1;
        private final DatagramSocket ds;

        public ResendTask(DatagramSocket ds, InetSocketAddress address, byte[] data) {
            this.ds = ds;
            this.address = address;
            this.data = data;
        }

        public ResendTask(DatagramSocket ds, InetSocketAddress address, byte[] data, int times) {
            this.ds = ds;
            this.address = address;
            this.data = data;
            this.times = times;
        }

        public void run() {
            send(ds, address, data);
            if (times < 3) {//最多发3次
                timer.schedule(new ResendTask(ds, address, data, times + 1), (int) (Math.pow(2, times + 1))
                        * Client.DEFAULT_TIME_OUT);
            }
        }

        public byte[] getData() {
            return data;
        }
    }

    public static void main(String[] args) {
        new TFtpServer2().start();
    }

}
