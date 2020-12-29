package com.changhong.telnettool.function.cpu;

import java.util.regex.Pattern;

/**
 * 内存使用情况
 */
public class MemUseInfo {
    public String used;
    public String free;
    public String share;
    public String buff;
    public String cached;
    public String total;

    /**
     * 范例： Mem: 78256K used, 28064K free, 0K shrd, 3104K buff, 13952K cached
     *
     * @param infoString 解析top过来的内存数据一行
     */
    public MemUseInfo(String infoString) {
        String info = infoString;
        if (infoString.indexOf(':') != -1)
            info = infoString.substring(infoString.indexOf(':') + 1).trim();

        String[] tempArr = info.split(",");
        for (String s : tempArr) {
            String[] tempArr2 = s.trim().split(" ");
            if ("used".equalsIgnoreCase(tempArr2[1])) {
                used = tempArr2[0];
            } else if ("free".equalsIgnoreCase(tempArr2[1])) {
                free = tempArr2[0];
            } else if ("shrd".equalsIgnoreCase(tempArr2[1])) {
                share = tempArr2[0];
            } else if ("buff".equalsIgnoreCase(tempArr2[1])) {
                buff = tempArr2[0];
            } else if ("cached".equalsIgnoreCase(tempArr2[1])) {
                cached = tempArr2[0];
            }
        }

        total = CalculateTotal();
    }

    private String CalculateTotal() {
        long used = Utils.getMemery(getUsed());
        long free = Utils.getMemery(getFree());
//        long share = Utils.getMemery(getShare());
//        long buff = Utils.getMemery(getBuff());
//        long cached = Utils.getMemery(getCached());
        String unit = Pattern.compile("[0-9]").matcher(getUsed()).replaceAll("").toLowerCase();
        long total = used + free;// + share + buff + cached;
        String lUnit = unit.toLowerCase();
        if (lUnit.indexOf('k') != -1) {
            total >>= 10;
        } else if (lUnit.indexOf('m') != -1) {
            total >>= 20;
        } else if (lUnit.indexOf('g') != -1) {
            total >>= 30;
        }
        return String.format("%d%s", total, unit);
    }

    public String getUsed() {
        return used;
    }

    public String getFree() {
        return free;
    }

    public float getFreePercent() {
        long free = Utils.getMemery(this.free);
        long totle = Utils.getMemery(this.total);
        return free * 1f / totle;
    }

    public float getUsedPercent() {
        long free = Utils.getMemery(this.free);
        long totle = Utils.getMemery(this.total);
        return (totle - free) * 1f / totle;
    }

    public String getShare() {
        return share;
    }

    public String getBuff() {
        return buff;
    }

    public String getCached() {
        return cached;
    }

    public String getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "MemUseInfo{" +
                "used='" + used + '\'' +
                ", free='" + free + '\'' +
                ", share='" + share + '\'' +
                ", buff='" + buff + '\'' +
                ", cached='" + cached + '\'' +
                ", total='" + total + '\'' +
                '}';
    }
}
