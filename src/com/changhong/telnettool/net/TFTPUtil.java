package com.changhong.telnettool.net;

import com.changhong.telnettool.net.tftpserver.TFtpServer;
import org.apache.commons.net.tftp.TFTP;
import org.apache.commons.net.tftp.TFTPClient;

import java.io.*;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import java.net.*;
import java.io.*;

public class TFTPUtil {
    private static TFTPClient tftp = new TFTPClient();

    public static boolean downloadFile(String hostname, String localFilename,
                                       String remoteFilename, int port) {

        // 设置超时时间为60秒
        tftp.setDefaultTimeout(60000);

        // 打开本地socket
        try {
            tftp.open();
        } catch (SocketException e) {
            System.err.println("无法打开本地 UDP socket!");
            System.err.println(e.getMessage());
        }

        boolean closed, success;
        closed = false;
        success = false;
        FileOutputStream output = null;
        File file;

        file = new File(localFilename);
        if (file.exists()) {
            System.err.println("文件: " + localFilename + " 已经存在!");
            return success;
        }

        try {
            output = new FileOutputStream(file);
        } catch (IOException e) {
            tftp.close();
            System.err.println("无法打开要写入的本地文件!");
            System.err.println(e.getMessage());
            return success;
        }

        try {
            tftp.receiveFile(remoteFilename, TFTP.BINARY_MODE, output, hostname, port);
            //tftp.receiveFile(remoteFilename, TFTP.BINARY_MODE, output, hostname);
            success = true;
        } catch (UnknownHostException e) {
            System.err.println("无法解析主机!");
            System.err.println(e.getMessage());
            return success;
        } catch (IOException e) {
            System.err.println("接收文件时有I/O异常!");
            System.err.println(e.getMessage());
            return success;
        } finally {
            // 关闭本地 socket 和输出的文件
            tftp.close();
            try {
                if (null != output) {
                    output.close();
                }
                closed = true;
            } catch (IOException e) {
                closed = false;
                System.err.println("关闭文件时出错!");
                System.err.println(e.getMessage());
            }
        }
        if (!closed)
            return false;

        return success;
    }

    public static byte[] downloadFile(String hostname, String remoteFilename, int port) {

        // 设置超时时间为60秒
        tftp.setDefaultTimeout(6000);

        // 打开本地socket
        try {
            tftp.open();
        } catch (SocketException e) {
            System.err.println("无法打开本地 UDP socket!");
            System.err.println(e.getMessage());
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            tftp.receiveFile(remoteFilename, TFTP.BINARY_MODE, baos, hostname, port);
            //tftp.receiveFile(remoteFilename, TFTP.BINARY_MODE, output, hostname);
            baos.toByteArray();
        } catch (UnknownHostException e) {
            System.err.println("无法解析主机!");
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println("接收文件时有I/O异常!");
            System.err.println(e.getMessage());
        } finally {
            // 关闭本地 socket 和输出的文件
            tftp.close();
        }

        return null;
    }

    public static boolean uploadFile(String hostname, String remoteFilename, InputStream input, int port) {
        // 设置超时时间为10秒
        tftp.setDefaultTimeout(10000);

        // 打开本地socket
        try {
            tftp.open();
        } catch (SocketException e) {
            System.err.println("无法打开本地 UDP socket!");
            System.err.println(e.getMessage());
        }

        boolean success, closed;
        closed = false;
        success = false;

        try {
            tftp.sendFile(remoteFilename, TFTP.BINARY_MODE, input, hostname, port);
            success = true;
        } catch (UnknownHostException e) {
            System.err.println("无法解析主机!");
            System.err.println(e.getMessage());
            return success;
        } catch (IOException e) {
            System.err.println("发送文件时有I/O异常!");
            System.err.println(e.getMessage());
            return success;
            //System.exit(1);
        } finally {
            // 关闭本地 socket 和输出的文件
            tftp.close();
            try {
                if (null != input) {
                    input.close();
                }
                closed = true;
            } catch (IOException e) {
                closed = false;
                System.err.println("关闭文件时出错!");
                System.err.println(e.getMessage());
            }
        }

        if (!closed)
            return false;

        return success;
    }


    public static boolean uploadFile(String hostname, String remoteFilename, String localFilePath, int port) {

        FileInputStream fileInput = null;
        try {
            fileInput = new FileInputStream(localFilePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return uploadFile(hostname, remoteFilename, fileInput, port);
    }


    public static void main(String[] args) {
		/*String hostname="192.168.20.200";
		String remoteFilename="/tftpboot/4.txt";
		String localFilePath="D:/1.txt";
		int port=69;
		TFTPUtil.uploadFile(hostname, remoteFilename, localFilePath, port);*/


//        TFTPUtil.downloadFile("192.168.20.200", "D:/44.txt", "/tftpboot/4.txt", 69);
//        byte[] data = TFTPUtil.downloadFile("192.168.20.200", "/var/port_wan_mask", 69);
//        if (data != null)
//            System.out.println(Arrays.toString(data));
//        else
//            System.out.println("TFTP LOAD ERROR");
        new TFtpServer("E://").start();

    }
}
