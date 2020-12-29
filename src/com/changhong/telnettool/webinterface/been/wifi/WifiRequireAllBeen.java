package com.changhong.telnettool.webinterface.been.wifi;

import com.changhong.telnettool.webinterface.been.Group;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

public class WifiRequireAllBeen {

    /**
     * 1为WEB,2为APP，3为其他
     */
    private Integer src_type = 1;
    /**
     * 0:disabled;1:enabled
     * when 7.3.1	wlan_access_setting:0:disabled;1:allow list;2:deny list
     */
    private Integer enabled;
    /**
     * required when enabled is 1
     * 0:2.4G和5G同时设置(prefer_5g 为1的时候该值默认为0)
     * 1：只配置2.4G
     * 2：只配置5G
     */
    private Integer flag;
    /**
     * 0:关闭；1：开启
     */
    private Integer prefer_5g;
    /**
     * 不超过32字符
     */
    private String ssid;
    /**
     * none/wpa2-psk/wpa2_mixed_psk
     */
    private String encryption;
    /**
     * 不超过64字符
     */
    private String key;
    /**
     * 0:穿墙；1:标准；2：睡眠
     */
    private Integer txpower_mode;
    /**
     * save_action
     */
    private Integer save_action;


    /**
     * 0-13有效范围。0：为自动
     */
    private Integer channel_2;
    /**
     * when band_2 is not 0 、1 、 2	0:20Mhz;1:40Mhz;3: 20/40Mhz
     */
    private Integer channel_width_2;
    /**
     * when band_2 is not 0 、1 、 2	0:长间隔；1：短间隔
     */
    private Integer shortgi_2;
    /**
     * 0:b;1:g;2:bg;7:n;9:gn;10:bgn
     */
    private Integer band_2;
    /**
     * 0:关闭；1：开启
     */
    private Integer hidden_2;
    /**
     * 0:关闭；1：开启
     */
    private Integer wmm_2;
    /**
     * 0：为自动，其余值为：
     * 36、40、44、48、52、56、60、149、153、157、161（仅中国区域）
     */
    private Integer channel_5;
    /**
     * when band_2 is not 3 	0:20Mhz;1: 40Mhz;2:80Mhz;
     */
    private Integer channel_width_5;
    /**
     * when band_2 is not 3	0:长间隔；1：短间隔
     */
    private Integer shortgi_5;
    /**
     * 3: a;7:n; 11:an; 63: ac; 71: n+ac;75: a+n+ac
     */
    private Integer band_5;
    /**
     * 0:关闭；1：开启
     */
    private Integer hidden_5;
    /**
     * 0:关闭；1：开启
     */
    private Integer wmm_5;
    /**
     * 1:保存并执行； 0：只保存
     */
    private Integer action_flag;

    private List<WlanLevel2Been> list;

    /**
     * 不超过32字符
     */
    private String name;
    /**
     * 校验mac是否合法，以及校验是否存在（可以先掉获取列表再对比该mac）
     */
    private String mac;
    /**
     * 0：延时30分钟。其他值不延时
     */
    private Integer delay;
    /**
     * 0:不限时间，1-24表示上网小时数.该字段只有在delay不为0才有效
     */
    private Integer long_time;
    /**
     * 1:2.4G，0:5G
     */
    private Integer wlan_index;

    /**
     * 信道
     */
    private Integer channel;
    /**
     * 3: a;7:n; 11:an; 63: ac; 71: n+ac;75: a+n+ac, 0:b;1:g;2:bg;7:n;9:gn;10:bgn
     */
    private Integer type;
    /**
     * 分组设备总数
     */
    private Integer mac_num;
    /**
     * 分组信息
     */
    private List<Group> mac_list;

    public WifiRequireAllBeen() {
    }

    public WifiRequireAllBeen(WifiResponseAllBeen been) {
        this.setFlag(been.getFlag());
        this.setEncryption(been.getEncryption());
        this.setKey(been.getKey());
        this.setPrefer_5g(been.getPrefer_5g());
        this.setSsid(been.getSsid());
        this.setBand_2(been.getBand_2());
        this.setBand_5(been.getBand_5());
        this.setChannel_2(been.getChannel_2());
        this.setChannel_5(been.getChannel_5());
        this.setChannel_width_2(been.getChannel_width_2());
        this.setChannel_width_5(been.getChannel_width_5());
        this.setDelay(been.getDelay());
        this.setEnabled(been.getEnabled());
        this.setHidden_2(been.getHidden_2());
        this.setHidden_5(been.getHidden_5());
        this.setWlan_index(been.getWlan_index());
        this.setMac_num(been.getMac_num());
        this.setLong_time(been.getLong_time());
        this.setWmm_2(been.getWmm_2());
        this.setWmm_5(been.getWmm_5());
        this.setTxpower_mode(been.getTxpower_mode());
        this.setShortgi_2(been.getShortgi_2());
        this.setShortgi_5(been.getShortgi_5());
    }

    public Integer getSrc_type() {
        return src_type;
    }

    public void setSrc_type(Integer src_type) {
        this.src_type = src_type;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Integer getPrefer_5g() {
        return prefer_5g;
    }

    public void setPrefer_5g(Integer prefer_5g) {
        this.prefer_5g = prefer_5g;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getEncryption() {
        return encryption;
    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getTxpower_mode() {
        return txpower_mode;
    }

    public void setTxpower_mode(Integer txpower_mode) {
        this.txpower_mode = txpower_mode;
    }

    public Integer getSave_action() {
        return save_action;
    }

    public void setSave_action(Integer save_action) {
        this.save_action = save_action;
    }

    public Integer getChannel_2() {
        return channel_2;
    }

    public void setChannel_2(Integer channel_2) {
        this.channel_2 = channel_2;
    }

    public Integer getChannel_width_2() {
        return channel_width_2;
    }

    public void setChannel_width_2(Integer channel_width_2) {
        this.channel_width_2 = channel_width_2;
    }

    public Integer getShortgi_2() {
        return shortgi_2;
    }

    public void setShortgi_2(Integer shortgi_2) {
        this.shortgi_2 = shortgi_2;
    }

    public Integer getBand_2() {
        return band_2;
    }

    public void setBand_2(Integer band_2) {
        this.band_2 = band_2;
    }

    public Integer getHidden_2() {
        return hidden_2;
    }

    public void setHidden_2(Integer hidden_2) {
        this.hidden_2 = hidden_2;
    }

    public Integer getWmm_2() {
        return wmm_2;
    }

    public void setWmm_2(Integer wmm_2) {
        this.wmm_2 = wmm_2;
    }

    public Integer getChannel_5() {
        return channel_5;
    }

    public void setChannel_5(Integer channel_5) {
        this.channel_5 = channel_5;
    }

    public Integer getChannel_width_5() {
        return channel_width_5;
    }

    public void setChannel_width_5(Integer channel_width_5) {
        this.channel_width_5 = channel_width_5;
    }

    public Integer getShortgi_5() {
        return shortgi_5;
    }

    public void setShortgi_5(Integer shortgi_5) {
        this.shortgi_5 = shortgi_5;
    }

    public Integer getBand_5() {
        return band_5;
    }

    public void setBand_5(Integer band_5) {
        this.band_5 = band_5;
    }

    public Integer getHidden_5() {
        return hidden_5;
    }

    public void setHidden_5(Integer hidden_5) {
        this.hidden_5 = hidden_5;
    }

    public Integer getWmm_5() {
        return wmm_5;
    }

    public void setWmm_5(Integer wmm_5) {
        this.wmm_5 = wmm_5;
    }

    public Integer getAction_flag() {
        return action_flag;
    }

    public void setAction_flag(Integer action_flag) {
        this.action_flag = action_flag;
    }

    public List<WlanLevel2Been> getList() {
        return list;
    }

    public void setList(List<WlanLevel2Been> list) {
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public Integer getLong_time() {
        return long_time;
    }

    public void setLong_time(Integer long_time) {
        this.long_time = long_time;
    }

    public Integer getWlan_index() {
        return wlan_index;
    }

    public void setWlan_index(Integer wlan_index) {
        this.wlan_index = wlan_index;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getMac_num() {
        return mac_num;
    }

    public void setMac_num(Integer mac_num) {
        this.mac_num = mac_num;
    }

    public List<Group> getMac_list() {
        return mac_list;
    }

    public void setMac_list(List<Group> groupList) {
        this.mac_list = groupList;
    }

    @Override
    public String toString() {
        return "RequireAllBeen{" +
                "src_type=" + src_type +
                ", enabled=" + enabled +
                ", flag=" + flag +
                ", prefer_5g=" + prefer_5g +
                ", ssid='" + ssid + '\'' +
                ", encryption='" + encryption + '\'' +
                ", key='" + key + '\'' +
                ", txpower_mode=" + txpower_mode +
                ", save_action=" + save_action +
                ", channel_2=" + channel_2 +
                ", channel_width_2=" + channel_width_2 +
                ", shortgi_2=" + shortgi_2 +
                ", band_2=" + band_2 +
                ", hidden_2=" + hidden_2 +
                ", wmm_2=" + wmm_2 +
                ", channel_5=" + channel_5 +
                ", channel_width_5=" + channel_width_5 +
                ", shortgi_5=" + shortgi_5 +
                ", band_5=" + band_5 +
                ", hidden_5=" + hidden_5 +
                ", wmm_5=" + wmm_5 +
                ", action_flag=" + action_flag +
                ", list=" + list +
                ", name='" + name + '\'' +
                ", mac='" + mac + '\'' +
                ", delay=" + delay +
                ", long_time=" + long_time +
                ", wlan_index=" + wlan_index +
                ", channel=" + channel +
                ", type=" + type +
                ", mac_num=" + mac_num +
                ", mac_list=" + mac_list +
                '}';
    }

    public WifiRequireAllBeen clone() {
        WifiRequireAllBeen newOne = new WifiRequireAllBeen();
        for (Field field : getClass().getDeclaredFields()) {
            try {
//                System.out.println(field.getName() + ":" + field.getType().getSimpleName() + " = " + field.get(this));
                field.set(newOne, field.get(this));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return newOne;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WifiRequireAllBeen)) return false;
        WifiRequireAllBeen that = (WifiRequireAllBeen) o;
        return Objects.equals(src_type, that.src_type) &&
                Objects.equals(enabled, that.enabled) &&
                Objects.equals(flag, that.flag) &&
                Objects.equals(prefer_5g, that.prefer_5g) &&
                Objects.equals(ssid, that.ssid) &&
                Objects.equals(encryption, that.encryption) &&
                Objects.equals(key, that.key) &&
                Objects.equals(txpower_mode, that.txpower_mode) &&
                Objects.equals(save_action, that.save_action) &&
                Objects.equals(channel_2, that.channel_2) &&
                Objects.equals(channel_width_2, that.channel_width_2) &&
                Objects.equals(shortgi_2, that.shortgi_2) &&
                Objects.equals(band_2, that.band_2) &&
                Objects.equals(hidden_2, that.hidden_2) &&
                Objects.equals(wmm_2, that.wmm_2) &&
                Objects.equals(channel_5, that.channel_5) &&
                Objects.equals(channel_width_5, that.channel_width_5) &&
                Objects.equals(shortgi_5, that.shortgi_5) &&
                Objects.equals(band_5, that.band_5) &&
                Objects.equals(hidden_5, that.hidden_5) &&
                Objects.equals(wmm_5, that.wmm_5) &&
                Objects.equals(action_flag, that.action_flag) &&
                Objects.equals(list, that.list) &&
                Objects.equals(name, that.name) &&
                Objects.equals(mac, that.mac) &&
                Objects.equals(delay, that.delay) &&
                Objects.equals(long_time, that.long_time) &&
                Objects.equals(wlan_index, that.wlan_index) &&
                Objects.equals(channel, that.channel) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(src_type, enabled, flag, prefer_5g, ssid, encryption, key, txpower_mode, save_action, channel_2, channel_width_2, shortgi_2, band_2, hidden_2, wmm_2, channel_5, channel_width_5, shortgi_5, band_5, hidden_5, wmm_5, action_flag, list, name, mac, delay, long_time, wlan_index, channel, type);
    }
}
