package com.isthisloss.antouchclient;

import android.view.View;

/**
 * Created by Dima on 30.03.2017.
 */


/**
 * Maps this keys to {@link ProtoAtci} key's codes and send one
 *
 * @author isthisloss
 */
class ButtonsListener implements View.OnClickListener {
    private Networking networking;

    ButtonsListener(Networking networking) {
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

            case R.id.btnEsc:
                key = ProtoAtci.BUTTON_ESC;
                break;
            case R.id.btnEnter:
                key = ProtoAtci.BUTTON_ENTER;
                break;
            case R.id.btnBackspace:
                key = ProtoAtci.BUTTON_BACKSPACE;
                break;

            case R.id.btnMediaBack:
                key = ProtoAtci.BUTTON_MEDIA_LAST;
                break;
            case R.id.btnMediaNext:
                key = ProtoAtci.BUTTON_MEDIA_NEXT;
                break;
            case R.id.btnMediaPause:
                key = ProtoAtci.BUTTON_MEDIA_PAUSE;
                break;
            case R.id.btnMediaPlay:
                key = ProtoAtci.BUTTON_MEDIA_PLAY;
                break;

            case R.id.btnVolUp:
                key = ProtoAtci.BUTTON_VOL_UP;
                break;
            case R.id.btnVolDown:
                key = ProtoAtci.BUTTON_VOL_DOWN;
                break;

            default:
                return;
        }
        networking.send(ProtoAtci.command(key));
    }
}
