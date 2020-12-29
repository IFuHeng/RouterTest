package com.changhong.telnettool.webinterface.been.plc;


import com.changhong.telnettool.webinterface.been.BaseResponseBeen;

import java.lang.reflect.Field;

public class WifiAdvanceInfo extends BaseResponseBeen {
    private Integer channel_2;//2.4G信道:  max:13  0: auto
    private String country_code;//国家码, 如CN表示中国，IN表示印度
    private Integer bandwidth_2;//频宽 0: 20MHz  1: 40MHz  2: 20/40MHz
    private Integer gi_2;//保护间隔，0: short    1: long
    private Integer trans_power_2;//发射功率0: 20%  1: 40%  2: 60%  3: 80%  4: 100%
    private Integer rf_mode_2;//工作模式2:b  3:g  4:n  9:bg  10:gn  5:bgn

    private Integer channel_5;//5G信道： max: 254   0:auto
    private Integer bandwidth_5;//频宽 0: 20MHz  1: 40MHz  2: 20/40MHz 3: 80MHz  4: 20/40/80MHz
    private Integer gi_5;//保护间隔：0: short    1: long
    private Integer trans_power_5;//	Integer	required	发射功率0: 20%  1: 40%  2: 60%  3: 80%  4: 100%
    private Integer rf_mode_5;//	Integer	required	工作模式1:a  11:na  6:ac  8: a/n/ac

    public Integer getChannel_2() {
        return channel_2;
    }

    public void setChannel_2(Integer channel_2) {
        this.channel_2 = channel_2;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public Integer getBandwidth_2() {
        return bandwidth_2;
    }

    public void setBandwidth_2(Integer bandwidth_2) {
        this.bandwidth_2 = bandwidth_2;
    }

    public Integer getGi_2() {
        return gi_2;
    }

    public void setGi_2(Integer gi_2) {
        this.gi_2 = gi_2;
    }

    public Integer getTrans_power_2() {
        if (trans_power_2 > 10)
            return trans_power_2 / 20 - 1;
        return trans_power_2;
    }

    public void setTrans_power_2(Integer trans_power_2) {
        if (trans_power_2 > 10) {
            this.trans_power_2 = (trans_power_2 + 1) * 20;
            return;
        }
        this.trans_power_2 = trans_power_2;
    }

    public Integer getRf_mode_2() {
        return rf_mode_2;
    }

    public void setRf_mode_2(Integer rf_mode_2) {
        this.rf_mode_2 = rf_mode_2;
    }

    public Integer getChannel_5() {
        return channel_5;
    }

    public void setChannel_5(Integer channel_5) {
        this.channel_5 = channel_5;
    }

    public Integer getBandwidth_5() {
        return bandwidth_5;
    }

    public void setBandwidth_5(Integer bandwidth_5) {
        this.bandwidth_5 = bandwidth_5;
    }

    public Integer getGi_5() {
        return gi_5;
    }

    public void setGi_5(Integer gi_5) {
        this.gi_5 = gi_5;
    }

    public Integer getTrans_power_5() {
        if (trans_power_5 > 10)
            return trans_power_5 / 20 - 1;
        return trans_power_5;
    }

    public void setTrans_power_5(Integer trans_power_5) {
        if (trans_power_5 > 10) {
            this.trans_power_5 = (trans_power_5 + 1) * 20;
            return;
        }
        this.trans_power_5 = trans_power_5;
    }

    public Integer getRf_mode_5() {
        return rf_mode_5;
    }

    public void setRf_mode_5(Integer rf_mode_5) {
        this.rf_mode_5 = rf_mode_5;
    }

    public void read(WifiAdvanceInfo been) {
        for (Field declaredField : getClass().getDeclaredFields()) {
            try {
                Object value = declaredField.get(been);
                if (value != null)
                    declaredField.set(this, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }
}
