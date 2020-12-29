package com.changhong.telnettool.webinterface.been.plc;



import com.changhong.telnettool.webinterface.been.BaseResponseBeen;

import java.lang.reflect.Field;

public class PLCGuestInfo extends BaseResponseBeen {
    private Integer enable;//		required	使能访客网络，0-禁用，1-启动
    private String name;//	required	访客网络名称
    private String passwd;//	required	访客网络密码
    private Integer valid_time;//required	访客网络预置可用时间，单位：秒
    private Integer time_left;//	required	访客网络剩余可用时间，单位：秒

    public PLCGuestInfo() {
    }

    public PLCGuestInfo(PLCGuestInfo mPLCInfo) {
        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                Object obj = field.get(mPLCInfo);
                if (obj != null)
                    field.set(this, obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public Integer getValid_time() {
        return valid_time;
    }

    public void setValid_time(Integer valid_time) {
        this.valid_time = valid_time;
    }

    public Integer getTime_left() {
        return time_left;
    }

    public void setTime_left(Integer time_left) {
        this.time_left = time_left;
    }
}
