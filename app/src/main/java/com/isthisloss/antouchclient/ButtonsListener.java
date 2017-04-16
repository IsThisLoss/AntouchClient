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
        byte key = 0;
        switch (id) {
            case R.id.btnUp:
                key = ProtoAtci.BUTTON_UP;
                break;
            case R.id.btnDown:
                key = ProtoAtci.BUTTON_DOWN;
                break;
            case R.id.btnLeft:
                key = ProtoAtci.BUTTON_LEFT;
                break;
            case R.id.btnRight:
                key = ProtoAtci.BUTTON_RIGHT;
                break;
            case R.id.btnVolDown:
                key = ProtoAtci.BUTTON_VOL_DOWN;
                break;
            case R.id.btnVolUp:
                key = ProtoAtci.BUTTON_VOL_UP;
                break;
            default:
                return;
        }
        networking.send(ProtoAtci.command(key));
    }
}
