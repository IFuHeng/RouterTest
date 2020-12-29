package com.changhong.telnettool.event;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MyWindowAdapter extends WindowAdapter {
    public static int openedWindow;

//    @Override
//    public void windowOpened(WindowEvent e) {
//        super.windowOpened(e);
//        System.out.println(e.getWindow().getName() + " --------->  windowOpened  : openedWindow = " + openedWindow);
//    }
//
//    @Override
//    public void windowActivated(WindowEvent e) {
//        super.windowActivated(e);
//        System.out.println(e.getWindow().getName() + " --------->  windowActivated ");
//    }
//
//    @Override
//    public void windowIconified(WindowEvent e) {
//        super.windowIconified(e);
//        System.out.println(e.getWindow().getName() + " --------->  windowIconified ");
//    }
//
//    @Override
//    public void windowClosing(WindowEvent e) {
//        System.out.println(e.getWindow().getName() + " --------->  windowClosing ");
//        super.windowClosing(e);
//    }
//
//    @Override
//    public void windowDeiconified(WindowEvent e) {
//        System.out.println(e.getWindow().getName() + " --------->  windowDeiconified ");
//        super.windowDeiconified(e);
//    }
//
//    @Override
//    public void windowGainedFocus(WindowEvent e) {
//        System.out.println(e.getWindow().getName() + " --------->  windowGainedFocus ");
//        super.windowGainedFocus(e);
//    }
//
//    @Override
//    public void windowDeactivated(WindowEvent e) {
//        System.out.println(e.getWindow().getName() + " --------->  windowDeactivated ");
//        super.windowDeactivated(e);
//    }
//
//    @Override
//    public void windowLostFocus(WindowEvent e) {
//        System.out.println(e.getWindow().getName() + " --------->  windowLostFocus ");
//        super.windowLostFocus(e);
//    }
//
//    @Override
//    public void windowStateChanged(WindowEvent e) {
//        System.out.println(e.getWindow().getName() + " --------->  windowStateChanged =  " + e.getOldState() + " -> " + e.getNewState());
//        super.windowStateChanged(e);
//    }

    @Override
    public void windowClosed(WindowEvent e) {
        super.windowClosed(e);
        --openedWindow;
//        System.out.println(e.getWindow().getName() + " --------->  windowClosed : openedWindow = " + openedWindow);
        if (openedWindow == 0)
            System.exit(0);
    }
}
