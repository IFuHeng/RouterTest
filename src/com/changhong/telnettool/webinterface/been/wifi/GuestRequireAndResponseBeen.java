package com.changhong.telnettool.webinterface.been.wifi;

import com.changhong.telnettool.webinterface.been.BaseResponseBeen;

public class GuestRequireAndResponseBeen extends BaseResponseBeen {

    /**
     * 1为WEB,2为APP，3为其它
     */
    private Integer src_type = 1;
    /**
     * 0:关闭；1：开启
     */
    private Integer enabled;
    /**
     * required when enabled is 1
     * 0：延时30分钟。其他值不延时
     */
    private Integer delay;
    /**
     * 不超过32字符
     */
    private String ssid;
    /**
     * 0：不加密。1：加密
     */
    private Integer encryption;
    /**
     * 不超过64字符
     */
    private String key;
    /**
     * 0:不限时间，1-24表示上网小时数.该字段只有在delay不为0才有效
     */
    private Integer long_time;
    /**
     * 返回还剩余的秒数
     */
    private Integer disable_time;

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

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public Integer getEncryption() {
        return encryption;
    }

    public void setEncryption(Integer encryption) {
        this.encryption = encryption;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getLong_time() {
        return long_time;
    }

    public void setLong_time(Integer long_time) {
        this.long_time = long_time;
    }

    public Integer getDisable_time() {
        return disable_time;
    }

    public void setDisable_time(Integer disable_time) {
        this.disable_time = disable_time;
    }

    @Override
    public String toString() {
        return "GuestRequireAndResponseBeen{" +
                "src_type=" + src_type +
                ", enabled=" + enabled +
                ", delay=" + delay +
                ", ssid='" + ssid + '\'' +
                ", encryption=" + encryption +
                ", key='" + key + '\'' +
                ", long_time=" + long_time +
                ", disable_time=" + disable_time +
                ", err_code=" + err_code +
                ", message='" + message + '\'' +
                ", waite_time=" + waite_time +
                '}';
    }
}
