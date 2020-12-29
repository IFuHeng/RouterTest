package com.changhong.telnettool.function.test_exam;

import com.changhong.telnettool.tool.Tool;
import com.changhong.telnettool.webinterface.Observer;
import javafx.util.Pair;

import java.io.IOException;

public class TestExam {
    public static void main(String[] args) {
//        new Test2p2p3("00000000", "192.168.58.201", "192.168.58.1", "255.255.255.0", "192.168.58.1", null, new Observer<Pair<Boolean, String>>() {
//        new Test2p2p2("00000000", new Observer<Pair<Boolean, String>>() {
//        new Test2p2p5("00000000", "BWR-5102_6D82", null, "BWR-5102_6D82_5G", null, new Observer<Pair<Boolean, String>>() {
//        new Test2p2p7("00000000", new Observer<Pair<Boolean, String>>() {
//            @Override
//            public void onError(Throwable throwable) {
//                System.err.println(throwable);
//            }
//
//            @Override
//            public void onComplete() {
//                System.out.println("\n测试完成");
//            }
//
//            @Override
//            public void onNext(Pair<Boolean, String> p) {
//                if (p.getKey()) {
//                    System.out.println("    " + p.getValue());
//                } else {
//                    System.out.print(p.getValue());
//                }
//            }
//        }).run();
        new TestWan();
//        try {
//            String string = new String(Tool.readFromIputStream(Runtime.getRuntime().exec("ping -n 10 baidu.com ").getInputStream()), "GBK");
//            System.out.println(string);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
