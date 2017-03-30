package com.isthisloss.antouchclient;

import android.app.Activity;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Dima on 11.03.2017.
 */

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

    void send(String string) {
        output.println(string);
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
