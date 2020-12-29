package com.changhong.telnettool.webinterface.been.plc;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

/**
 * 单个设备限制信息
 */
public class DevControlRuleBeen {

    String mac;// 设备MAC 地址
    String start_time;//     起始时间(00:00~23:59)
    String end_time;//  结束时间 (00:00~23:59)
    String repeat_day;// 1-7(星期一到星期天)，0 表示每天
    /**
     * 主要用在6.3.4	internet_time_limit_setting，用于判断表单行是新增、修改、删除
     * null：删除
     * true：新增
     * false：修改
     */
    private Boolean state;

    public String getMac() {
        return mac;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getRepeat_day() {
        return repeat_day;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public void setRepeat_day(String repeat_day) {
        this.repeat_day = repeat_day;
    }

    public Boolean getState() {
        return state;
    }

    /**
     * 主要用在6.3.4	internet_time_limit_setting，用于判断表单行是新增、修改、删除
     * null：删除
     * true：新增
     * false：修改
     */
    public void setState(Boolean state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "DevControlRuleBeen{" +
                "mac='" + mac + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", repeat_day='" + repeat_day + '\'' +
                '}';
    }

    public DevControlRuleBeen getCopy() {
        DevControlRuleBeen been = new DevControlRuleBeen();
        been.mac = this.mac;
        been.start_time = this.start_time;
        been.end_time = this.end_time;
        been.repeat_day = this.repeat_day;
        return been;
    }

    /**
     * 将上网健康时间限制的内容转化成文字
     *
     * @return
     */
    public String getHealthString(@NotNull String[] weekday, @NotNull String to, @NotNull String weekends, @Nullable String weekdays, @Nullable String everyday) {
        return String.format("%s - %s %s"
                , getStart_time()
                , getEnd_time()
                , getWeekEndShow(repeat_day, weekday, to, weekends, weekdays, everyday));
    }

    private String getWeekEndShow(String value, @NotNull String[] weekday, @NotNull String to, @NotNull String weekends, @Nullable String weekdays, @Nullable String everyday) {
        if (value == null || (value.length() == 1 && value.charAt(0) == '0'))
            return everyday;

        if (value.length() == 1)
            return weekday[Integer.parseInt(value) - 1];

        if (value.length() == 5
                && value.equals("12345"))
            return weekdays;

        if (value.length() == 2
                && value.charAt(0) == '6'
                && value.charAt(1) == '7')
            return weekends;

        StringBuilder sb = new StringBuilder();
        for (int i = 0, num = 0; i < value.length(); i++) {
            char charAt = value.charAt(i);
            if (num != 0) {
                if (num + value.charAt(i - num) == charAt) {//连续的
                    if (i == value.length() - 1) {//末尾
                        if (sb.length() > 0)
                            sb.append(',');

                        if (num == 4 && value.charAt(i - num) == '1')
                            sb.append(weekdays);
                        else if (num == 1 && value.charAt(i - num) == '6')
                            sb.append(weekends);
                        else {
                            sb.append(weekday[value.charAt(i - num) - '1']).append(to).append(weekday[charAt - '1']);
                        }
                    }
                } else if (num > 1) {//断开的
                    if (sb.length() > 0)
                        sb.append(',');

                    if (num == 5 && value.charAt(i - num) == '1')
                        sb.append(weekdays);
                    else {
                        sb.append(weekday[value.charAt(i - num) - '1']).append(to).append(weekday[value.charAt(i - 1) - '1']);
                    }

                    if (i == value.length() - 1) {//末尾
                        sb.append(',').append(weekday[charAt - '1']);
                    } else
                        num = 0;
                } else { // 单一的 非连续的
                    if (sb.length() > 0)
                        sb.append(',');
                    sb.append(weekday[value.charAt(i - num) - '1']);
                    if (i == value.length() - 1) {//末尾
                        sb.append(',').append(weekday[charAt - '1']);
                    } else
                        num = 0;
                }
            }
            num++;
        }

        return sb.toString();
    }

    public void setWeekStartAndEnd(@NotNull boolean[] weekChoice) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < weekChoice.length; i++) {
            if (weekChoice[i])
                sb.append(i + 1);
        }
        if (sb.length() == 7)
            setRepeat_day("0");
        else
            setRepeat_day(sb.toString());
    }

    public DevControlRuleBeen cloneOneForInternetTimeLimit() {
        DevControlRuleBeen result = new DevControlRuleBeen();
        result.mac = this.mac;
        result.start_time = this.start_time;
        result.end_time = this.end_time;
        result.repeat_day = this.repeat_day;
        result.state = this.state;
        return result;
    }

    /**
     * @return 获取星期选中状态 星期7654321
     */
    public byte getWeekDays() {

        if (repeat_day == null || repeat_day.length() == 0
                || (repeat_day.length() == 1 && repeat_day.charAt(0) == '0'))
            return 0x7f;

        byte value = 0;
        for (int i = 0; i < repeat_day.length(); i++) {
            char charAt = repeat_day.charAt(i);
            charAt -= '1';
            value |= 1 << charAt;
        }
        return value;
    }
}
