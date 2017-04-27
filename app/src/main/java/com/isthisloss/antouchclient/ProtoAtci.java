package com.isthisloss.antouchclient;

/**
 * Created by Dima on 15.04.2017.
 */

/**
 * ProtoAtci is a class which provide functions to work with
 * Android Touch Code Interconnection protocol
 *
 * @author isthisloss
 */
class ProtoAtci {
    // headers state
    final static byte COMMAND  = 0x00;
    final static byte V_SCROLL = 0x01;
    final static byte MOVE     = 0x02;
    final static byte TEXT     = 0x03;

    // half mouse codes < 2
    final static byte HOLD_ON =  0x01;
    final static byte HOLD_OFF = 0x00;

    // mouse key codes < BUTTON_UP
    final static byte LEFT_CLICK  = 0x02;
    final static byte RIGHT_CLICK = 0x03;

    // key codes >= BUTTON_UP
    final static byte BUTTON_UP       = 0x06;
    final static byte BUTTON_DOWN     = 0x07;
    final static byte BUTTON_LEFT     = 0x08;
    final static byte BUTTON_RIGHT    = 0x09;
    final static byte BUTTON_ESC      = 0x0A;
    final static byte BUTTON_ENTER    = 0x0B;
    final static byte BUTTON_BACKSPACE= 0x0C;


    final static byte BUTTON_VOL_DOWN   = 0x0D;
    final static byte BUTTON_VOL_UP     = 0x0E;
    final static byte BUTTON_MEDIA_LAST = 0x0F;
    final static byte BUTTON_MEDIA_NEXT = 0x10;
    final static byte BUTTON_MEDIA_PLAY = 0x11;
    final static byte BUTTON_MEDIA_PAUSE = 0x12;


    /**
     * Generate message which contains scroll event
     *
     * @param dx vertical offset
     * @return ATCI message
     */
    static byte[] mouseScroll(short dx) {
        byte[] msg = new byte[3];
        msg[0] = V_SCROLL;
        msg[1] = (byte) (dx & 0xFF);
        msg[2] = (byte) ((dx >> 8) & 0xFF);
        return msg;
    }

    /**
     * Generate message about mouse move event based on
     * vertical and horizontal offsets
     *
     * @param dx horizontal offset
     * @param dy vertical offset
     * @return ATCI message
     */
    static byte[] mouseMove(short dx, short dy) {
        byte[] msg = new byte[5];
        msg[0] = MOVE;
        msg[1] = (byte) (dx & 0xFF);
        msg[2] = (byte) ((dx >> 8) & 0xFF);
        msg[3] = (byte) (dy & 0xFF);
        msg[4] = (byte) ((dy >> 8) & 0xFF);
        return msg;
    }

    /**
     * Generate message with one defined command
     *
     * @param cmd command to send
     * @return ATCI message
     */
    static byte[] command(byte cmd) {
        byte[] msg = new byte[1];
        msg[0] = (byte)(cmd << 2);  // 'cause cmd << 2 | 0x00 does't really change anything
        return msg;
    }
    /**
     * Generate message with text
     *
     * @param string is a text to send
     * @return ATCI message
     */
    static byte[] text(String string) {
        byte[] text = string.getBytes();
        byte[] msg = new byte[text.length + 1];
        msg[0] = TEXT;
        System.arraycopy(text, 0, msg, 1, text.length);
        return msg;
    }
}
