package com.isthisloss.antouchclient;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by isthisloss on 09.04.17.
 */

class TouchListener2 implements View.OnTouchListener {
    private final String TAG = "TL2";

    private final static int UP = 101;
    private boolean holdOn;
    private int lastUp;
    private Networking networking;
    private GestureDetector gestureDetector;
    private Calendar calendar;


    TouchListener2(Context context, Networking networking) {
        this.networking = networking;
        gestureDetector = new GestureDetector(context, new GestrureListener());
        holdOn = false;
        calendar = Calendar.getInstance();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (calendar.get(Calendar.SECOND) - lastUp <= 1)
                networking.send(String.valueOf(101));
                break;
            case MotionEvent.ACTION_UP:
                networking.send(String.valueOf(102));
                break;
        }
        gestureDetector.onTouchEvent(event);
        return true;
    }


    private class GestrureListener extends GestureDetector.SimpleOnGestureListener {
        private int last_click;

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
        public boolean onSingleTapConfirmed(MotionEvent e) {
            String msg = String.format(Locale.ENGLISH, "%d", Constants.LEFT_CLICK);
            networking.send(msg);
            Log.d(TAG, "onSingleTapConfirmed");
            last_click = calendar.get(Calendar.SECOND);
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
}
