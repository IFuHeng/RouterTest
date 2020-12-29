package com.changhong.telnettool.function.sta;

import com.changhong.telnettool.tool.Tool;
import com.changhong.telnettool.webinterface.been.StaInfo;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class MyTableMode2 extends AbstractTableModel {

    public static final String[] TITLE_STA = {"名称", "MAC", "IP", "上挂点", "方式", "在线时间", "上行", "下行"};
    private final List<StaInfo> mData;

    public MyTableMode2(List<StaInfo> mData) {
        this.mData = mData;
    }

    public int getColumnCount() {
        return TITLE_STA.length + 1;
    }

    public int getRowCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0)
            return "序号";
        return TITLE_STA[column - 1];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {//"名称", "MAC", "IP", "2.4G / 5G", "信号强度（dMb）", "state", "上行", "下行"
        if (columnIndex == 0)
            return Integer.class;

        switch (columnIndex - 1) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                return String.class;
            default:
                return Object.class;
        }
    }


    public Object getValueAt(int row, int col) {
        if (col == 0)
            return row + 1;

        StaInfo info = mData.get(row);
        switch (col - 1) {
            case 0:
                return info.getName();
            case 1:
                return info.getMac();
            case 2:
                return info.getIp();
            case 3:
                return info.getSuperiorNode();
            case 4:
                return info.getConnectType().getName();
            case 5:
                return turn2Time(info.getLink_time());
            case 6:
                return Tool.getSpeedStringBit(info.speedRx == null ? 0 : info.speedRx);
            case 7:
                return Tool.getSpeedStringBit(info.speedTx == null ? 0 : info.speedTx);
            default:
                return null;
        }
    }

    private String turn2Time(int seconds) {
        int hour = seconds / 3600;
        int minute = seconds / 60 % 60;
        int second = seconds % 60;
        if (hour == 0) {
            if (minute == 0)
                return String.format("%02d秒", second);
            else
                return String.format("%02d分%2d秒", minute, second);
        } else
            return String.format("%02d时%02d分%2d秒", hour, minute, second);
    }
}
