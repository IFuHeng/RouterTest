package com.changhong.telnettool.webinterface.been.plc;

import com.changhong.telnettool.webinterface.been.BaseResponseBeen;

import java.util.List;

public class DeviceCustomInfoBeen extends BaseResponseBeen {

    private List<CuntomerInfo> customer_info;

    public List<CuntomerInfo> getCustomer_info() {
        return customer_info;
    }

    public static class CuntomerInfo {
        String mac;
        String dev_name;
        String dev_location;
        Integer index;

        public CuntomerInfo(String mac, String dev_name, String dev_location) {
            this.mac = mac;
            this.dev_name = dev_name;
            this.dev_location = dev_location;
        }

        public CuntomerInfo() {
        }

        public String getMac() {
            return mac;
        }

        public String getDev_name() {
            return dev_name;
        }

        public String getDev_location() {
            return dev_location;
        }

        public Integer getIndex() {
            return index;
        }
    }

}
