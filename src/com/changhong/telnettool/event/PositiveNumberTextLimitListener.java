package com.changhong.telnettool.event;

import com.changhong.telnettool.tool.Tool;

import java.awt.*;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.util.Arrays;

public class PositiveNumberTextLimitListener implements TextListener {
    @Override
    public void textValueChanged(TextEvent e) {
        if (e.getSource() instanceof TextComponent) {
            TextComponent tc = ((TextComponent) e.getSource());
            if (!isPositiveNumber(tc.getText())) {
                tc.removeTextListener(this);
                tc.setText(onlyPositiveNumber(tc.getText()));
                tc.setSelectionStart(tc.getText().length());
                tc.setSelectionEnd(tc.getText().length());
                tc.addTextListener(this);
            }
        }
    }

    public static String onlyPositiveNumber(String str) {
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

    private boolean isPositiveNumber(String str) {
        if (str == null || str.isEmpty())
            return false;

        if (str.charAt(0) == '0') {
            return false;
        }

        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }
}
