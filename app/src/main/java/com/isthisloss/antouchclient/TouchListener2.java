package com.isthisloss.antouchclient;


import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by isthisloss on 09.04.17.
 */

class TouchListener2 implements View.OnTouchListener {
    private final String TAG = "TL2";

    private boolean holdOn;
    private boolean waitForClick;
    private boolean waitForRightClick;
    private long lastClick;

    private Networking networking;

    private int lastX;
    private int lastY;

    TouchListener2(Networking networking) {
        this.networking = networking;
        holdOn = false;
        lastClick = 0;
        waitForClick = false;
        waitForRightClick = false;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "ACTION_DOWN");
                actionDown(event);
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "ACTION_UP");
                actionUp(event);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.d(TAG, "ACTION_POINTER_DOWN");
                waitForRightClick = true;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.d(TAG, "ACTION_POINTER_UP");
                if (waitForRightClick)
                    actionPointerUp(event);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "ACTION_MOVE");
                actionMove(event);
                break;
        }
        return true;
    }

    private void actionDown(MotionEvent e) {
        lastX = Math.round(e.getX());
        lastY = Math.round(e.getY());

        if (e.getPointerCount() == 1 && System.currentTimeMillis() - lastClick <= 250) {
            holdOn = true;
            networking.send(ProtoATCI.mouseKeyEvent(ProtoATCI.HOLD_ON));
        } else {
            waitForClick = true;
        }
    }

    private void actionUp(MotionEvent e) {
        if (holdOn) {
            networking.send(ProtoATCI.mouseKeyEvent(ProtoATCI.HOLD_OFF));
            holdOn = false;
        } else if (waitForClick) {
            lastClick = System.currentTimeMillis();
            networking.send(ProtoATCI.mouseKeyEvent(ProtoATCI.LEFT_CLICK));
            waitForClick = false;
        }
    }

    private void actionPointerUp(MotionEvent e) {
        networking.send(ProtoATCI.mouseKeyEvent(ProtoATCI.RIGHT_CLICK));
    }

    private void actionMove(MotionEvent e) {
        int x = Math.round(e.getX());
        int y = Math.round(e.getY());

        if (e.getPointerCount() == 1) {
            int dx = x - lastX;
            int dy = y - lastY;
            pointerMove(dx, dy);
            lastX = x;
            lastY = y;
        } else if (e.getPointerCount() == 2) {
            scroll(y - lastY);
            lastX = x;
            lastY = y;
        }
    }

    private void pointerMove(int dx, int dy) {
        if (Math.abs(dx) <= 1 && Math.abs(dy) <= 1) {
            return;
        }
        waitForClick = false;
        networking.send(ProtoATCI.mouseMove((short) dx, (short) dy));
    }

    private void scroll(int dy) {
        waitForClick = false;
        if (Math.abs(dy) > 1) {
            waitForRightClick = false;
            networking.send(ProtoATCI.mouseScroll((short) dy));
        }
    }
}
