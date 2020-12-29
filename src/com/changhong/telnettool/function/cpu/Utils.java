package com.changhong.telnettool.function.cpu;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Utils {

    static String[] split(String info) {
        info = info.trim();
        int start = 0;
        ArrayList<String> list = new ArrayList();
        for (int i = 0; i < info.length(); ++i) {
            char c = info.charAt(i);
            if (start == -1) {
                if (!Character.isWhitespace(c))
                    start = i;
                continue;
            } else {
                if (Character.isWhitespace(c)) {
                    list.add(info.substring(start, i));
                    start = -1;
                }
            }
        }

        list.add(info.substring(start));

        String[] result = new String[list.size()];
        return list.toArray(result);
    }

    static long getMemery(String mem) {
        Matcher matcher = Pattern.compile("[0-9]").matcher(mem);
        if (matcher.matches())
            return Integer.parseInt(mem);

        long value = Integer.parseInt(Pattern.compile("[^0-9]").matcher(mem).replaceAll(""));
        String unit = matcher.replaceAll("").toLowerCase();
        if (unit.indexOf('k') != -1) {
            value <<= 10;
        } else if (unit.indexOf('m') != -1) {
            value <<= 20;
        } else if (unit.indexOf('g') != -1) {
            value <<= 30;
        }

        return value;
    }

    static String getMemery(long mem) {
        if (mem > 1000L * 1024 * 1024) {
            return String.format("%.1fGB", mem * 1.0 / (1L << 30));
        } else if (mem > 1000L * 1024) {
            return String.format("%.1fMB", mem * 1.0 / (1L << 20));
        } else if (mem > 1000L) {
            return String.format("%.1fKB", mem * 1.0 / (1L << 10));
        }

        return mem + "B";
    }
}
