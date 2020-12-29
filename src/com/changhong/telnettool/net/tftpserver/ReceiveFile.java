package com.changhong.telnettool.net.tftpserver;

import java.util.Arrays;

public class ReceiveFile {
    String ip;
    String port;
    String fileName;
    byte[] data;

    public ReceiveFile() {
    }

    public ReceiveFile(String ip, String port,String fileName, byte[] data) {
        this.fileName = fileName;
        this.data = data;
        this.ip = ip;
        this.port = port;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "ReceiveFile{" +
                "ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", fileName='" + fileName + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
