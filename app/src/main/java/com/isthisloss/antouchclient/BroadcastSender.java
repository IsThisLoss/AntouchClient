package com.isthisloss.antouchclient;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Dima on 11.03.2017.
 */

class BroadcastSender {
    private Context context;
    private Runnable errorListener;

    private DatagramSocket dgramSock;

    BroadcastSender(Context context, Runnable errorListener) {
        this.context = context;
        this.errorListener = errorListener;
    }

    String getStableAddress() {
        try {
            dgramSock = new DatagramSocket();
            dgramSock.setBroadcast(true);

            byte[] broadcastKey = Constants.BROADCAST_KEY.getBytes();
            DatagramPacket packet = new DatagramPacket(broadcastKey, broadcastKey.length, getBroadcastAddress(), Constants.BROADCAST_PORT);
            dgramSock.send(packet);
            while (true) {
                byte[] recievedBytes = new byte[128];

                DatagramPacket recievePack = new DatagramPacket(recievedBytes, recievedBytes.length);

                dgramSock.receive(recievePack);
                return new String(recievedBytes).trim();
            }
        } catch (Exception e) {
            errorListener.run();
            e.printStackTrace();
        }
        return null;
    }

    private InetAddress getBroadcastAddress() throws UnknownHostException {
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE); // wtf
        DhcpInfo dhcpInfo = wifi.getDhcpInfo();

        int broadcast = (dhcpInfo.ipAddress & dhcpInfo.netmask) | ~dhcpInfo.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) (broadcast >> (k * 8));

        return InetAddress.getByAddress(quads);
    }


}
