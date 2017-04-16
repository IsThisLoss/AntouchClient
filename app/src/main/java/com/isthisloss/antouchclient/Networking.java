package com.isthisloss.antouchclient;

import android.app.Activity;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Locale;

/**
 * Created by Dima on 11.03.2017.
 */

class Package {
    // 0 or 1 are half-click events
    final static int HOLD_ON = 1;
    final static int HOLD_OFF = 0;

    // 10 < x < 20 are mouse click events
    final static int LEFT_CLICK = 14;
    final static int RIGHT_CLICK = 15;

    // 20 < x < 30 are mouse motions events
    final static int MOVE = 28;
    final static int V_SCROLL = 29;

    // x > 50 are keys' events
    static final int BUTTON_UP = 51;
    static final int BUTTON_DOWN = 52;
    static final int BUTTON_LEFT = 54;
    static final int BUTTON_RIGHT = 55;
    static final int BUTTON_VOL_DOWN = 57;
    static final int BUTTON_VOL_UP = 58;



    static String mouseKeyEvent(int cmd) {
        return String.valueOf(cmd);
    }

    static String mouseMove(int dx, int dy) {
        return String.format(Locale.ENGLISH, "%d %d %d", MOVE, dx, dy);
    }

    static String mouseScroll(int dy) {
        return String.format(Locale.ENGLISH, "%d %d", V_SCROLL, dy);
    }

    static String keyEvent(int cmd) {
        return String.valueOf(cmd);
    }
}


class Networking {
    private Socket socket;
    private PrintWriter output;
    private Activity activity;
    private Runnable errorListener;
    private String serverIp;

    Networking(Activity activity, Runnable errorListener) {
        this.activity = activity;
        this.errorListener = errorListener;
    }

    void connect(String ip) {
        this.serverIp = ip;
        new Thread(new StartNetworking()).start();
    }

    void send(final String string) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                output.println(string);
            }
        }).start();
    }

    void send(final byte[] msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                output.println(msg);
            }
        }).start();
    }

    private class StartNetworking implements Runnable {
        @Override
        public void run() {
            try {
                socket = new Socket(InetAddress.getByName(serverIp), Constants.TCP_PORT);
                //incoming = new BufferedReader(new InputStreamReader(socket.getInputStream())); maybe for future use
                output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            } catch (Exception e) {
                activity.runOnUiThread(errorListener);
                e.printStackTrace();
            }
        }
    }
}
