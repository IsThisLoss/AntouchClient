package com.isthisloss.antouchclient;

import android.view.MotionEvent;
import android.view.View;

import java.util.Locale;

/**
 * Created by Dima on 30.03.2017.
 */

class ButtonsListener implements View.OnClickListener, View.OnTouchListener {
    private Networking networking;

    public ButtonsListener(Networking networking) {
        this.networking = networking;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String out = null;
        switch (id) {
            case R.id.btnUp:
                out = String.format(Locale.ENGLISH, "%d", Constants.BUTTON_UP);
                break;
            case R.id.btnDown:
                out = String.format(Locale.ENGLISH, "%d", Constants.BUTTON_DOWN);
                break;
            case R.id.btnLeft:
                out = String.format(Locale.ENGLISH, "%d", Constants.BUTTON_LEFT);
                break;
            case R.id.btnRight:
                out = String.format(Locale.ENGLISH, "%d", Constants.BUTTON_RIGHT);
                break;
            case R.id.btnVolDown:
                out = String.format(Locale.ENGLISH, "%d", Constants.BUTTON_VOL_DOWN);
                break;
            case R.id.btnVolUp:
                out = String.format(Locale.ENGLISH, "%d", Constants.BUTTON_VOL_UP);
                break;
            default:
                return;
        }
        networking.send(out);
    }
}
