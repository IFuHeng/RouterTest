package com.changhong.telnettool.function.sta;

import com.changhong.telnettool.tool.Tool;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class MyTableMode extends AbstractTableModel {

    private final String[] TITLES;
    private final List<StaInfo> mData;

    public MyTableMode(String[] TITLES, List<StaInfo> mData) {
        this.TITLES = TITLES;
        this.mData = mData;
    }

    public int getColumnCount() {
        return TITLES.length;
    }

    public int getRowCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public String getColumnName(int column) {
        return TITLES[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {//"名称", "MAC", "IP", "2.4G / 5G", "信号强度（dMb）", "state", "上行", "下行"
        switch (columnIndex) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 6:
            case 7:
            case 8:
                return String.class;
            case 4:
            case 5:
                return Integer.class;
            default:
                return Object.class;
        }
    }

    public Object getValueAt(int row, int col) {

        StaInfo info = mData.get(row);
        switch (col) {
            case 0:
                return info.getName();
            case 1:
                return Tool.turnMacString(info.getMAC());
            case 2:
                return Tool.turn2IpV4(info.getIp());
            case 3:
                return info.is5g() ? "5G" : "2.4G";
            case 4:
                return info.getRssi();
            case 5:
                return info.getState();
            case 6:
                return Tool.getSpeedStringBit(info.speedTx);
            case 7:
                return Tool.getSpeedStringBit(info.speedRx);
            case 8:
                return info.getBand_width();
            default:
                return null;
        }
    }

}
