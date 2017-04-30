package com.isthisloss.antouchclient;

import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by isthisloss on 09.04.17.
 */

/**
 * Touch Listener is a class which listen touch events of certain view
 *
 * @author isthisloss
 */
class TouchListener implements View.OnTouchListener {
    private final String TAG = "TouchListener";

    private boolean holdOn;
    private boolean waitForClick;
    private long lastClick;

    private  Networking networking;

    private Handler longPressHandler;
    private Runnable onLongPressCallback;

    private int lastX;
    private int lastY;
    private int scrollArea;

    /**
     * Constructor
     *
     * @param networking is a connected to set-top box instance of {@link Networking} class
     * @param scrollArea is the offset from right corner for scrolling area
     */
    TouchListener(Networking networking, int scrollArea) {
        this.networking = networking;
        this.scrollArea = scrollArea;
        this.longPressHandler = new Handler();
        this.onLongPressCallback = new Runnable() {
            @Override
            public void run() {
                TouchListener.this.networking.send(ProtoAtci.command(ProtoAtci.RIGHT_CLICK));
            }
        };
        this.holdOn = false;
        this.lastClick = 0;
        this.waitForClick = false;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "ACTION_DOWN");
                actionDown(event);
                longPressHandler.postDelayed(onLongPressCallback, ViewConfiguration.getLongPressTimeout());
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "ACTION_UP");
                longPressHandler.removeCallbacks(onLongPressCallback);
                actionUp(event);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "ACTION_MOVE");
                longPressHandler.removeCallbacks(onLongPressCallback);
                actionMove(event);
                break;
        }
        return true;
    }

    /**
     * It is called when one finger touch the view
     *
     * @param e is an Android Motion Event instance
     */
    private void actionDown(MotionEvent e) {
        lastX = Math.round(e.getX());
        lastY = Math.round(e.getY());

        if (e.getPointerCount() == 1 && System.currentTimeMillis() - lastClick <= 250) {
            holdOn = true;
            networking.send(ProtoAtci.command(ProtoAtci.HOLD_ON));
        } else {
            waitForClick = true;
        }
    }

    /**
     * It is called when one finger releases
     *
     * @param e is an Android Motion Event instance
     */
    private void actionUp(MotionEvent e) {
        if (holdOn) {
            networking.send(ProtoAtci.command(ProtoAtci.HOLD_OFF));
            holdOn = false;
        } else if (waitForClick) {
            lastClick = System.currentTimeMillis();
            networking.send(ProtoAtci.command(ProtoAtci.LEFT_CLICK));
            waitForClick = false;
        }
    }

    /**
     * Callback for move event
     *
     * @param e is an Android Motion Event instance
     */
    private void actionMove(MotionEvent e) {
        int x = Math.round(e.getX());
        int y = Math.round(e.getY());

        if (e.getPointerCount() != 1) {
            return;
        }


        if (x < scrollArea) { // (x, y) does not belong to scroll area
            int dx = x - lastX;
            int dy = y - lastY;
            pointerMove(dx, dy);
        } else {
            int dy = y - lastY;
            scroll(dy);
        }

        lastX = x;
        lastY = y;
    }

    /**
     * Send {@link ProtoAtci} message if dx and dy do not belong to unit circle
     * otherwise do nothing
     *
     * @param dx is a horizontal offset
     * @param dy is a vertical offset
     */
    private void pointerMove(int dx, int dy) {
        if (Math.abs(dx) <= 1 && Math.abs(dy) <= 1) {
            return;
        }
        waitForClick = false;
        networking.send(ProtoAtci.mouseMove((short) dx, (short) dy));
    }

    /**
     * Send {@link ProtoAtci} message if dy more that 1
     * otherwise do nothing
     *
     * @param dy is a vertical offset
     */
    private void scroll(int dy) {
        waitForClick = false;
        if (Math.abs(dy) > 2) {
            networking.send(ProtoAtci.mouseScroll((short) dy));
        }
    }
}
