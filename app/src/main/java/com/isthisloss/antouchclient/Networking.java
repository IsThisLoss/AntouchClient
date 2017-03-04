package com.isthisloss.antouchclient;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.DatagramSocketImpl;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

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

    private class BroadcastSender implements Runnable {
        DatagramSocket dgramSock;

        @Override
        public void run() {
            byte[] key = new byte[128];
            key = "test".getBytes();
            try {
                dgramSock = new DatagramSocket();
                dgramSock.setBroadcast(true);
                DatagramPacket packet = new DatagramPacket(key, key.length,
                        InetAddress.getByName("192.168.43.255"), Constants.BROADCAST_PORT);
                dgramSock.send(packet);
                while (!Thread.currentThread().isInterrupted()) {
                    dgramSock.receive(packet);
                    final byte[] recived = packet.getData();
                    Log.d("NET", new String(recived));
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, new String(recived), Toast.LENGTH_SHORT).show();
                        }
                    });
                    setServerInfo(new String(recived), Constants.TCP_PORT);
                    new Thread(new StartNetworking()).start();
                    break;
                }
            } catch (Exception e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "Ошибка бродкаста", Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
            }
        }
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
