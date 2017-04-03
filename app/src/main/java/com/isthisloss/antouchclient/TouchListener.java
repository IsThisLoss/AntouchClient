package com.isthisloss.antouchclient;

import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.Locale;

/**
 * Created by isthisloss on 11.02.17.
 */

class TouchListener extends GestureDetector.SimpleOnGestureListener {

    private Networking networking;



    TouchListener(Networking networking) {
        this.networking = networking;
    }

    private void move(int offset_x, int offset_y) {
        String msg = String.format(Locale.ENGLISH, "%d %d %d", Constants.DRAG, -offset_x, -offset_y);
        networking.send(msg);
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
    public boolean onSingleTapConfirmed(MotionEvent e) {
        String msg = String.format(Locale.ENGLISH, "%d", Constants.LEFT_CLICK);
        networking.send(msg);
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        // todo send drag mode enable
        Log.d("GEST", "DOUBLE TAP");
        return true;
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
