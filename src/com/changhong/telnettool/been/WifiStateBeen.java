package com.changhong.telnettool.been;

public class WifiStateBeen {

    private boolean is5g;

    private int curTemp;
    private int maxTemp;
    private int avgTemp;
    private int tx;
    private int rx;

    private String state;

    private int maxSpd;
    private int hiSpd;
    private int lowSpd;


    public WifiStateBeen(String content) {
        if (content == null || content.length() < 6 || !content.startsWith("wlan"))
            return;

        is5g = content.charAt(4) == '0';
        curTemp = loadDigitValue(content, "Ther:");
        avgTemp = loadDigitValue(content, "avg:");
        maxTemp = loadDigitValue(content, "max:");

        tx = loadDigitValue(content, "tx:");
        rx = loadDigitValue(content, "rx:");

        hiSpd = loadDigitValue(content, "hi-");
        maxSpd = loadDigitValue(content, "max-");
        lowSpd = loadDigitValue(content, "low-");

        state = loadStringValue(content, "state:");
    }

    private int loadDigitValue(String content, String key) {
        int index = content.indexOf(key);
        if (index == -1)
            return -1;

        int start = index + key.length();
        int end = content.length();
        for (int i = start; i < content.length(); ++i) {
            if (!Character.isDigit(content.charAt(i))) {
                end = i;
                break;
            }
        }
        return Integer.parseInt(content.substring(start, end));
    }

    private String loadStringValue(String content, String key) {
        int index = content.indexOf(key);
        if (index == -1)
            return null;

        int start = index + key.length();
        int end = content.length();
        for (int i = start; i < content.length(); ++i) {
            if (!Character.isLetterOrDigit(content.charAt(i))) {
                end = i;
                break;
            }
        }
        return content.substring(start, end);
    }

    @Override
    public String toString() {
        return "WifiStateBeen{" +
                "is5g=" + is5g +
                ", curTemp=" + curTemp +
                ", maxTemp=" + maxTemp +
                ", avgTemp=" + avgTemp +
                ", tx=" + tx +
                ", rx=" + rx +
                ", state='" + state + '\'' +
                ", maxSpd=" + maxSpd +
                ", hiSpd=" + hiSpd +
                ", lowSpd=" + lowSpd +
                '}';
    }

    public boolean is5g() {
        return is5g;
    }

    public int getCurTemp() {
        return curTemp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public int getAvgTemp() {
        return avgTemp;
    }

    public String getState() {
        return state;
    }
}
