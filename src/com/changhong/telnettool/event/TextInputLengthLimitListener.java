package com.changhong.telnettool.event;

import java.awt.*;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;

public class TextInputLengthLimitListener implements TextListener {
    final int length;

    public TextInputLengthLimitListener(int length) {
        this.length = length;
    }

    @Override
    public void textValueChanged(TextEvent e) {
        if (e.getSource() instanceof TextComponent) {
            TextComponent tc = ((TextComponent) e.getSource());
            if (tc.getText().length() > length) {
                tc.removeTextListener(this);
                tc.setText(tc.getText().substring(0, length));
                tc.setSelectionStart(tc.getText().length());
                tc.setSelectionEnd(tc.getText().length());
                tc.addTextListener(this);
            }
        }
    }
}
