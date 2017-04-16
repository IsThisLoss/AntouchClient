package com.isthisloss.antouchclient;

/**
 * Created by Dima on 15.04.2017.
 */

class ProtoATCI {
    final static byte COMMAND  = 0x00;
    final static byte V_SCROLL = 0x01;
    final static byte MOVE     = 0x02;
    final static byte TEXT     = 0x03;
    
    final static byte HOLD_ON =  0x01 << 2;
    final static byte HOLD_OFF = 0x00; // means 0x0 << 2

    // 10 < x < 20 are mouse click events
    final static byte LEFT_CLICK  = 0x02 << 2;
    final static byte RIGHT_CLICK = 0x03 << 2;

    // 20 < x < 30 are mouse motions events
    

    // x > 50 are keys' events
    final static byte BUTTON_UP       = 0x06 << 2;
    final static byte BUTTON_DOWN     = 0x07 << 2;
    final static byte BUTTON_LEFT     = 0x08 << 2;
    final static byte BUTTON_RIGHT    = 0x09 << 2;
    final static byte BUTTON_VOL_DOWN = 0x0A << 2;
    final static byte BUTTON_VOL_UP   = 0x0B << 2;


    static byte[] mouseScroll(short dx) {
        byte[] msg = new byte[3];
        msg[0] = V_SCROLL;
        msg[1] = (byte) (dx & 0xFF);
        msg[2] = (byte) ((dx >> 8) & 0xFF);
        return msg;
    }

    static byte[] mouseMove(short dx, short dy) {
        byte[] msg = new byte[5];
        msg[0] = MOVE;
        msg[1] = (byte) (dx & 0xFF);
        msg[2] = (byte) ((dx >> 8) & 0xFF);
        msg[3] = (byte) (dy & 0xFF);
        msg[4] = (byte) ((dy >> 8) & 0xFF);
        return msg;
    }

    // doesn't need backend
    static byte[] mouseKeyEvent(byte cmd) {
        byte[] msg = new byte[1];
        msg[0] = cmd;  // 'cause cmd | 0x00 does't really change anything
        return msg;
    }
}
