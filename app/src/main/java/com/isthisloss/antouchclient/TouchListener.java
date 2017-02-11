package com.isthisloss.antouchclient;

import android.view.MotionEvent;
import android.view.View;

import java.util.Locale;

/**
 * Created by isthisloss on 11.02.17.
 */

class TouchListener implements View.OnTouchListener {
    private int last_x;
    private int last_y;
    private boolean isClick;

    private Networking networking;

    public TouchListener(Networking networking) {
        this.networking = networking;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        String off = "";
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                last_x = x;
                last_y = y;
                isClick = true;
                break;
            case MotionEvent.ACTION_UP:
                last_x = x;
                last_y = y;

                if (isClick) {
                    off = String.format(Locale.ENGLISH, "%d", Constants.LEFT_CLICK);
                    networking.send(off);
                }
                isClick = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int offset_x = x - last_x;
                int offset_y = y - last_y;
                last_x = x;
                last_y = y;
                if (event.getPointerCount() == 2) {
                    if (offset_y > 4) {
                        off = String.format(Locale.ENGLISH, "%d", Constants.WHEEL_UP);
                    } else if (offset_y < -4) {
                        off = String.format(Locale.ENGLISH, "%d", Constants.WHEEL_DOWN);
                    }
                    else {
                        return true;
                    }

                } else {
                    isClick = false;
                    off = String.format(Locale.ENGLISH, "%d %d %d", Constants.DRAG, offset_x, offset_y);
                }
                networking.send(off);
                break;
        }
        return true;
    }
}
