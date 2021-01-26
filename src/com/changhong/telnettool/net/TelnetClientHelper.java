package com.changhong.telnettool.net;

import org.apache.commons.net.telnet.TelnetClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.SocketException;
import java.util.Arrays;

public class TelnetClientHelper {
    private TelnetClient telnet = new TelnetClient("VT220");

    InputStream in;
    PrintStream out;

    String prompt = "#";
    String promptByte = "#";
    String PROMPT_PWD = "Password:";
    String PROMPT_LOGIN = "login:";

    public TelnetClientHelper(String ip, int port) throws IOException {
        telnet.connect(ip, port);
        in = telnet.getInputStream();
        out = new PrintStream(telnet.getOutputStream());
    }

    /**
     * 登录
     *
     * @param user
     * @param password
     */
    public String login(String user, String password) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(readUntil("login:"));
        write(user, false);
        stringBuilder.append(user);

        //读取内容，直到获得 "PASSWORD:",结束循环 ； 或者读取到PS1（#）结束
        char lastChar = PROMPT_PWD.charAt(PROMPT_PWD.length() - 1);
        char ch = (char) in.read();

        while (true) {
            stringBuilder.append(ch);
            if (ch == lastChar && stringBuilder.toString().endsWith(PROMPT_PWD)) {
                break;
            } else if (ch == prompt.charAt(prompt.length() - 1)) {
                return stringBuilder.toString();
            }
            ch = (char) in.read();
        }

        if (password == null || password.length() == 0) {
            throw new RuntimeException("Telnet need password but input password is empty");
        }

        //输入password
        write(password, false);
        boolean isPasswordCorrect = true;
        try {//读取内容，直到获得 "PASSWORD:",结束循环 ； 或者读取到PS1（#）结束
            lastChar = PROMPT_LOGIN.charAt(PROMPT_LOGIN.length() - 1);
            ch = (char) in.read();

            while (true) {
                stringBuilder.append(ch);
                if (ch == lastChar && stringBuilder.toString().endsWith(PROMPT_LOGIN)) {
                    isPasswordCorrect = false;
                    break;
                } else if (ch == prompt.charAt(prompt.length() - 1)) {
                    return stringBuilder.toString();
                }
                ch = (char) in.read();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!isPasswordCorrect)
            throw new RuntimeException("Telnet user or password wrong.");

        stringBuilder.append(readUntil(prompt + ""));
        return stringBuilder.toString();
    }

    /**
     * 读取分析结果
     *
     * @param pattern
     * @return
     */
    public String readUntil(String pattern) throws IOException {
        char lastChar = pattern.charAt(pattern.length() - 1);
        StringBuffer sb = new StringBuffer();
        char ch = (char) in.read();

        while (true) {
            sb.append(ch);
            if (ch == lastChar && sb.toString().endsWith(pattern)) {
                return sb.toString();
            }
            ch = (char) in.read();
        }
    }

    /**
     * 读取分析结果
     *
     * @param pattern
     * @return
     */
    public byte[] readUntil(byte[] pattern) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int tmp;
            int index = 0;// 匹配序号
            System.out.println("available = " + in.available());
            while ((tmp = in.read()) != -1) {
                byte b = (byte) tmp;
                if (b == 32 && byteArrayOutputStream.size() == 0)
                    continue;
                byteArrayOutputStream.write(b);
                if (b == pattern[index]) {
                    index++;
                    if (index == pattern.length) {
                        Thread.sleep(100);
                        if (in.available() == 1)
                            break;
                        else {
                            index--;
                        }
                    }
                } else index = 0;
            }
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 写操作
     *
     * @param value
     */
    public void write(String value, boolean dealException) {
        try {
            out.println(value);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            if (!dealException)
                throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 向目标发送命令字符串
     *
     * @param command
     * @return
     */
    public String sendCommand(String command) throws Exception {
        command = command.trim();
        write(command, true);
        String result = readUntil(prompt + "").trim();
        if (result.startsWith(command))
            result = result.substring(command.length()).trim();
        if (result.endsWith(prompt))
            result = result.substring(0, result.length() - prompt.length()).trim();

        return result;
    }

    /**
     * 向目标发送命令字符串
     *
     * @param command
     * @return
     */
    public byte[] sendCommandForBytes(String command) {
        try {
            command = command.trim();
            write(command, true);
            byte[] result = readUntil(promptByte.getBytes());
            int start = 0;
            int end = result.length;
            {
                byte[] temp = command.getBytes();
                if (isInStart(result, temp)) {
                    start = temp.length;
                    for (int i = 0; i < 4; i++) {
                        byte c = result[temp.length + i];
//                        System.out.println(c + " = " + (char) c);
                        if (c == 13 || c == 10) {
                            start++;
                        } else
                            break;
                    }
                }
            }
            {
                byte[] temp = prompt.getBytes();
                if (isInEnd(result, temp))
                    end -= temp.length;
            }
            if (start != 0 || end != result.length)
                result = Arrays.copyOfRange(result, start, end);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isInStart(byte[] data1, byte[] data2) {
        int length = Math.min(data1.length, data2.length);
        for (int i = 0; i < length; i++) {
            if (data1[i] != data2[i])
                return false;
        }
        return true;
    }

    private boolean isInEnd(byte[] data1, byte[] data2) {
        int length = Math.min(data1.length, data2.length);
        for (int i = 0; i < length; i++) {
            if (data1[data1.length - i - 1] != data2[data2.length - 1 - i])
                return false;
        }
        return true;
    }

    /**
     * 关闭连接
     */
    public void disconnect() {
        try {
            telnet.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) throws UnsupportedEncodingException {
//        TelnetClientHelper ws = new TelnetClientHelper("192.168.58.1", 23);
//        System.out.println(ws.login("root", "root"));
////			System.out.println(ws);
//        // 执行的命令
//        String str = ws.sendCommand("ls -al");
//        str = new String(str.getBytes("ISO-8859-1"), "GBK");
//        System.out.println(str);
//        str = ws.sendCommand("grep -nr abc *");
//        str = new String(str.getBytes("ISO-8859-1"), "GBK");
//        System.out.println(str);
//        ws.disconnect();
//    }

}