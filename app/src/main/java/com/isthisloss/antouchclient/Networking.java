package com.isthisloss.antouchclient;

import android.app.Activity;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
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
        new Thread(new BroadcastSender()).start();
    }

    void showToastOnUi(final String string) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, string, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class BroadcastSender implements Runnable {
        DatagramSocket dgramSock;

        private InetAddress getBroadcastAddress() throws UnknownHostException {
            Context context = activity.getApplicationContext();
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            DhcpInfo dhcpInfo = wifi.getDhcpInfo();

            int broadcast = (dhcpInfo.ipAddress & dhcpInfo.netmask) | ~dhcpInfo.netmask;
            byte[] quads = new byte[4];
            for (int k = 0; k < 4; k++)
                quads[k] = (byte) (broadcast >> (k * 8));

            return InetAddress.getByAddress(quads);
        }

        @Override
        public void run() {
            try {
                dgramSock = new DatagramSocket();
                dgramSock.setBroadcast(true);

                byte[] broadcastKey = Constants.BROADCAST_KEY.getBytes();
                DatagramPacket packet = new DatagramPacket(broadcastKey, broadcastKey.length, getBroadcastAddress(), Constants.BROADCAST_PORT);
                dgramSock.send(packet);
                while (!Thread.currentThread().isInterrupted()) {
                    byte[] recievedBytes = new byte[128];

                    DatagramPacket recievePack = new DatagramPacket(recievedBytes, recievedBytes.length);

                    dgramSock.receive(recievePack);
                    String recivedString = new String(recievedBytes).trim();

                    setServerInfo(recivedString, Constants.TCP_PORT);
                    new Thread(new StartNetworking()).start();
                    break;
                }
            } catch (Exception e) {
                showToastOnUi("Ошибка бродкаста");
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
                showToastOnUi("Ошибка подключения");
                e.printStackTrace();
                return;
            }
            showToastOnUi("Подключено");
        }
    }
}
