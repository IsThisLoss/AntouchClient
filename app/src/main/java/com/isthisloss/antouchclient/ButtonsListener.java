package com.isthisloss.antouchclient;

import android.view.MotionEvent;
import android.view.View;

import java.util.Locale;

/**
 * Created by Dima on 30.03.2017.
 */

class ButtonsListener implements View.OnClickListener {
    private Networking networking;

    public ButtonsListener(Networking networking) {
        this.networking = networking;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        int key = 0;
        switch (id) {
            case R.id.btnUp:
                key = Package.BUTTON_UP;
                break;
            case R.id.btnDown:
                key = Package.BUTTON_DOWN;
                break;
            case R.id.btnLeft:
                key = Package.BUTTON_LEFT;
                break;
            case R.id.btnRight:
                key = Package.BUTTON_RIGHT;
                break;
            case R.id.btnVolDown:
                key = Package.BUTTON_VOL_DOWN;
                break;
            case R.id.btnVolUp:
                key = Package.BUTTON_VOL_UP;
                break;
            default:
                return;
        }
        networking.send(Package.keyEvent(key));
    }
}
