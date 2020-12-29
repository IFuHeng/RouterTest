package com.changhong.telnettool.function.sta;

import com.sun.org.glassfish.gmbal.Description;
import com.sun.org.glassfish.gmbal.DescriptorFields;

import java.util.Date;

@Description("设备上下线事件")
public class StaOnOffEvent {

    @Description("设备名称")
    String name;
    String mac;
    String ip;
    @Description("上级节点")
    String superior_route;
    @Description("在线时间(秒)")
    int lease;
    @Description("事件")
    @DescriptorFields({"离线", "上线"})
    boolean onOff;
    @Description("系统时间")
    Date time;

    public StaOnOffEvent() {

    }

    public StaOnOffEvent(String name, String mac, String ip, String superior_route, int lease, long time, boolean onOff) {
        this.name = name;
        this.mac = mac;
        this.ip = ip;
        this.lease = lease;
        this.time = new Date(time);
        this.onOff = onOff;
        this.superior_route = superior_route;
    }

    @Override
    public String toString() {
        return "StaOnOffEvent{" +
                ", name='" + name + '\'' +
                "mac='" + mac + '\'' +
                ", ip='" + ip + '\'' +
                ", lease=" + lease +
                ", onOff='" + onOff + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
