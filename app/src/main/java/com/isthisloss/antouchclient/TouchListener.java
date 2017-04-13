package com.isthisloss.antouchclient;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by isthisloss on 11.02.17.
 */

class TouchListener extends GestureDetector.SimpleOnGestureListener implements GestureDetector.OnDoubleTapListener {

    private Networking networking;
    private int last_click;
    private Calendar calendar;
    private static String TAG = "TouchListener";

    private final static int HOLD_ON = 101;
    private final static int HOLD_OFF = 102;
    private boolean holdOn;

    TouchListener(Networking networking) {
        this.networking = networking;
        last_click = 0;
        holdOn = true;
        calendar = Calendar.getInstance();
    }

    private void move(int offset_x, int offset_y) {
        final String msg = String.format(Locale.ENGLISH, "%d %d %d", Constants.DRAG, -offset_x, -offset_y);
        networking.send(msg);
        Log.d(TAG, "move");
    }

    private void scroll(int offset_y) {
        int cmd;

        if (offset_y > 0) {
            cmd = Constants.WHEEL_DOWN;
        } else if (offset_y < 0) {
            cmd = Constants.WHEEL_UP;
        } else {
            return;
        }

        String msg = String.format(Locale.ENGLISH, "%d %d", cmd, offset_y);
        networking.send(msg);
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d(TAG, "onSingleTapUp");
        return super.onSingleTapUp(e);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        String msg = String.format(Locale.ENGLISH, "%d", Constants.LEFT_CLICK);
        networking.send(msg);
        Log.d(TAG, "onSingleTapConfirmed");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent first, MotionEvent last, float distanceX, float distanceY) {
        int pointerCount = last.getPointerCount();
        if (pointerCount == 1) {
            move((int) distanceX, (int) distanceY);
        } else if (pointerCount == 2) {
            scroll((int) distanceY);
        }
        return true;
    }
}
