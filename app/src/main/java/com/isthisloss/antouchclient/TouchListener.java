package com.isthisloss.antouchclient;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

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
    private boolean waitForRightClick;
    private long lastClick;

    private Networking networking;

    private int lastX;
    private int lastY;

    /**
     * Constructor
     *
     * @param networking is a connected to set-top box instance of {@link Networking} class
     */
    TouchListener(Networking networking) {
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
     * It is called when two (or maybe more) fingers release
     *
     * @param e is an Android Motion Event instance
     */
    private void actionPointerUp(MotionEvent e) {
        networking.send(ProtoAtci.command(ProtoAtci.RIGHT_CLICK));
    }

    /**
     * Callback for move event
     *
     * @param e is an Android Motion Event instance
     */
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
        if (Math.abs(dy) > 1) {
            waitForRightClick = false;
            networking.send(ProtoAtci.mouseScroll((short) dy));
        }
    }
}
