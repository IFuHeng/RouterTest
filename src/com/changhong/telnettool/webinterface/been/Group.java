package com.changhong.telnettool.webinterface.been;

public class Group {
    /**
     * 设备所属分组的分组名称，最大 不超过 63 个ASCII字符
     */
    private String group_name;
    private String list_name;
    /**
     * 电力猫的mac含有：，其它不含
     */
    private String mac;
    /**
     * 设备索引
     */
    private Integer index;
    /**
     * null：删除
     * true：新增
     * false：修改
     */
    private Boolean state;

    public Group() {
    }

    public Group(String group_name, String mac) {
        this.group_name = group_name;
        this.mac = mac;
    }

    public Group(String group_name, String mac, Integer index) {
        this.group_name = group_name;
        this.mac = mac;
        this.index = index;
    }

    public String getGroup_name() {
        if (group_name != null)
            return group_name;
        return list_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Boolean getState() {
        return state;
    }

    /**
     * @param state null：删除; true：新增; false：修改
     */
    public Group setState(Boolean state) {
        this.state = state;
        return this;
    }

    public Group cloneOne() {
        Group group = new Group();
        group.group_name = getGroup_name();
        group.setMac(mac);
        group.index = this.index;
        return group;
    }

    @Override
    public String toString() {
        return "Group{" +
                "group_name='" + group_name + '\'' +
                ", list_name='" + list_name + '\'' +
                ", mac='" + mac + '\'' +
                ", index=" + index +
                ", state=" + state +
                '}';
    }
}
