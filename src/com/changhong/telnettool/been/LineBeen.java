package com.changhong.telnettool.been;

import java.awt.*;

public class LineBeen {
    String name;
    Color color;
    NodeStrQueue queue;

    public LineBeen(String name, Color color, NodeStrQueue queue) {
        this.name = name;
        this.color = color;
        this.queue = queue;
    }

    public String getName() {
        return name;
    }

    public NodeStrQueue getQueue() {
        return queue;
    }

    public Color getColor() {
        return color;
    }
}
