package com.isthisloss.antouchclient;

import android.app.Activity;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Locale;

/**
 * Created by Dima on 11.03.2017.
 */

class Networking {
    private DataOutputStream dataOutputStream;
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

    void send(final byte[] msg) {
        Log.d("NET", String.format("0x%X", msg[0]));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dataOutputStream.write(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private class StartNetworking implements Runnable {
        @Override
        public void run() {
            try {
                Socket socket = new Socket(InetAddress.getByName(serverIp), Constants.TCP_PORT);
                //incoming = new BufferedReader(new InputStreamReader(socket.getInputStream())); maybe for future use
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
            } catch (Exception e) {
                activity.runOnUiThread(errorListener);
                e.printStackTrace();
            }
        }
    }
}
