package com.changhong.telnettool.webinterface.been.wifi;

import java.util.List;

/**
 * 7.3.4	wlan_ access_del
 * 描述：删除无线访问控制设备
 */
public class ReqireWlanAccessDelBeen {
    /**
     * 1为WEB,2为APP，3为其他
     */
    private Integer src_type = 1;
    /**
     * 1:保存并执行； 0：只保存
     */
    private Integer action_flag;
    private List<String> list;

    public void setSrc_type(Integer src_type) {
        this.src_type = src_type;
    }

    public void setAction_flag(Integer action_flag) {
        this.action_flag = action_flag;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ReqireWlanAccessDel{" +
                "src_type=" + src_type +
                ", action_flag=" + action_flag +
                ", list=" + list +
                '}';
    }
}
