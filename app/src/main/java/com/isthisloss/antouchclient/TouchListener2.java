package com.isthisloss.antouchclient;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;

/**
 * Created by isthisloss on 09.04.17.
 */

class TouchListener2 implements View.OnTouchListener {
    private final String TAG = "TL2";

    private boolean holdOn;
    private boolean waitForClick;
    private int lastClick;

    private Networking networking;
    private Calendar calendar;

    private int lastX;
    private int lastY;

    TouchListener2(Networking networking) {
        this.networking = networking;
        calendar = Calendar.getInstance();
        holdOn = false;
        lastClick = 0;
        waitForClick = false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                actionDown(event);
                break;
            case MotionEvent.ACTION_UP:
                actionUp(event);
                break;
            case MotionEvent.ACTION_MOVE:
                actionMove(event);
                break;
        }
        return true;
    }

    private void actionDown(MotionEvent e) {
        lastX = (int) e.getX();
        lastY = (int) e.getY();

        if (calendar.get(Calendar.SECOND) - lastClick <= 1) {
            holdOn = true;
            networking.send(Package.mouseKeyEvent(Package.HOLD_ON));
        } else {
            waitForClick = true;
        }
    }

    private void actionUp(MotionEvent e) {
        if (holdOn) {
            networking.send(Package.mouseKeyEvent(Package.HOLD_OFF));
            holdOn = false;
        } else if (waitForClick) {
            lastClick = calendar.get(Calendar.SECOND);
            switch (e.getPointerCount()) {
                case 1:
                    networking.send(Package.mouseKeyEvent(Package.LEFT_CLICK));
                    break;
                case 2:
                    networking.send(Package.mouseKeyEvent(Package.RIGHT_CLICK));
                    break;
                case 3:
                    networking.send(Package.mouseKeyEvent(Package.WHELE_CLICK));
                    break;
                default:
                    break;
            }
            waitForClick = false;
        }
    }

    private void actionMove(MotionEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();

        if (e.getPointerCount() == 1) {
            pointerMove(x - lastX, y - lastY);
            lastX = x;
            lastY = y;
        } else if (e.getPointerCount() == 2) {
            scroll(y - lastY);
            lastX = x;
            lastY = y;
        }
    }

    private void pointerMove(int dx, int dy) {
        waitForClick = false;
        networking.send(Package.mouseMove(dx, dy));
    }

    private void scroll(int dy) {
        networking.send(Package.mouseScroll(dy));
    }
}
