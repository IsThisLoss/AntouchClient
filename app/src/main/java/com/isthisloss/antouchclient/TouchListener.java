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
                    tap();
                }

                isClick = false;

                break;
            case MotionEvent.ACTION_MOVE:
                int offset_x = x - last_x;
                int offset_y = y - last_y;

                last_x = x;
                last_y = y;

                int pointerCount = event.getPointerCount();
                isClick = false;

                if (pointerCount == 1) {
                    move(offset_x, offset_y);
                } else if (pointerCount == 2) {
                    scroll(offset_y);
                }

                break;
        }
        return true;
    }

    private void tap() {
        String msg = String.format(Locale.ENGLISH, "%d", Constants.LEFT_CLICK);
        networking.send(msg);
    }

    private void move(int offset_x, int offset_y) {
        String msg = String.format(Locale.ENGLISH, "%d %d %d", Constants.DRAG, offset_x, offset_y);
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

}
