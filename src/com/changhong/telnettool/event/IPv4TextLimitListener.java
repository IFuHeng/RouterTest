package com.changhong.telnettool.event;

import com.changhong.telnettool.tool.Tool;

import java.awt.*;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.util.Arrays;

public class IPv4TextLimitListener implements TextListener {

    public static final String PATTERN_IPV4 = "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$";

    @Override
    public void textValueChanged(TextEvent e) {
        if (e.getSource() instanceof TextComponent) {
            TextComponent tc = ((TextComponent) e.getSource());
            if (!isIpv4Input(tc.getText())) {
                tc.removeTextListener(this);
                tc.setText(onlyIpV4(tc.getText()));
                tc.setSelectionStart(tc.getText().length());
                tc.setSelectionEnd(tc.getText().length());
                tc.addTextListener(this);
            }
        }
    }

    public static String onlyIpV4(String str) {
        if (str.length() < 1)
            return str;
        if (str.length() > 0 && str.charAt(0) == '0') {
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) != '0') {
                    str = str.substring(i);
                    break;
                }
            }
            Tool.log("str = " + str);
        }

        char[] chars = new char[str.length()];
        for (int i = 0, j = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i)))
                continue;
            Tool.log("str[" + i + "] = " + str.charAt(i));

            chars[j++] = str.charAt(i);
        }
        Tool.log("chars = " + Arrays.toString(chars));
        Tool.log("new str = " + new String(chars));
        return new String(chars);
    }

    private boolean isIpv4Input(String str) {
        if (str == null || str.isEmpty())
            return true;

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!Character.isDigit(c) && c != '.') {
                return false;
            }
        }

        if (str.indexOf('.') == -1) {
            if (str.length() > 2 && Integer.parseInt(str) > 255)
                return false;
        } else {
            String[] temp = str.split(".");
            for (String s : temp) {
                if (str.length() > 2 && Integer.parseInt(str) > 255)
                    return false;
            }
        }

        return true;
    }
}
