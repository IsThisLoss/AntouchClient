package com.isthisloss.antouchclient;

import android.app.Activity;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by isthisloss on 11.02.17.
 */

class Networking {
    private Activity activity;
    private Socket socket;

    private String server_ip;
    private int server_port;

    // private BufferedReader incoming;
    private PrintWriter output;

    Networking(Activity activity) {
        this.activity = activity;
        this.server_ip = null;
        this.server_port = -1;
    }

    void setServerInfo(String ip, int port) {
        this.server_ip = ip;
        this.server_port = port;
    }

    void send(String string) {
        output.println(string);
    }

    void start() {
        new Thread(new StartNetworking()).start();
    }

    private class StartNetworking implements Runnable {
        @Override
        public void run() {
            try {
                socket = new Socket(InetAddress.getByName(server_ip), server_port);
                //incoming = new BufferedReader(new InputStreamReader(socket.getInputStream())); maybe for future use
                output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            } catch (Exception e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "Ошибка подключения", Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
                return;
            }

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "Подключено", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
