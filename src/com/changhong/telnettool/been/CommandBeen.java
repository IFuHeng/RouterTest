package com.changhong.telnettool.been;

public class CommandBeen {
    private static final String SEPARATOR = "<Separator/>";
    public boolean isCheck;
    public boolean isUnlimited;
    public String command;

    public CommandBeen(boolean isCheck, String command, boolean isUnlimited) {
        super();
        this.isCheck = isCheck;
        this.command = command;
        this.isUnlimited = isUnlimited;
    }

    public CommandBeen(String data) {
        super();
        if (data == null)
            return;
        String[] list = data.split(SEPARATOR);
        if (list.length > 0)
            this.command = list[0];
        if (list.length > 1)
            this.isCheck = Boolean.parseBoolean(list[1]);
        if (list.length > 2)
            this.isUnlimited = Boolean.parseBoolean(list[2]);
    }

    @Override
    public String toString() {
        return "CommandBeen{" +
                "isCheck=" + isCheck +
                ", command='" + command + '\'' +
                ", isUnlimited=" + isUnlimited +
                '}';
    }

    public String toSaveString() {
        return (command == null ? new String() : command)
                + SEPARATOR
                + isCheck
                + SEPARATOR
                + isUnlimited
                ;
    }
}
