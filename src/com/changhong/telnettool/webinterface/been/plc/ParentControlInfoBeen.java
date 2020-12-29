package com.changhong.telnettool.webinterface.been.plc;

import com.changhong.telnettool.webinterface.been.BaseResponseBeen;
import com.changhong.telnettool.webinterface.been.Group;

import java.util.List;

public class ParentControlInfoBeen extends BaseResponseBeen {
    /**
     * 实施父母控制的设备列表
     */
    List<Group> dev_info;

    public List<Group> getDev_info() {
        return dev_info;
    }
}
