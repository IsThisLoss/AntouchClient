package com.isthisloss.antouchclient;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.CountDownTimer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Dima on 16.03.2017.
 */

class BroadcastSender extends AsyncTask<Void, Void, String> {
    private MainActivity activity;
    private CountDownTimer countDownTimer;
    private ProgressDialog progressDialog;

    BroadcastSender(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(activity, "Поиск приставки", "Пожалуйста подождите", true);

        countDownTimer = new CountDownTimer(5000, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                BroadcastSender.this.onPostExecute(null);
            }
        }.start();

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        countDownTimer.cancel();
        progressDialog.dismiss();
        activity.onTaskFinished(s);
        activity = null; // due to free memory
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            DatagramSocket dgramSock = new DatagramSocket();
            dgramSock.setBroadcast(true);

            byte[] broadcastKey = Constants.BROADCAST_KEY.getBytes();
            DatagramPacket packet = new DatagramPacket(broadcastKey, broadcastKey.length, getBroadcastAddress(), Constants.BROADCAST_PORT);
            dgramSock.send(packet);
            byte[] recievedBytes = new byte[128];
            DatagramPacket recievePack = new DatagramPacket(recievedBytes, recievedBytes.length);
            dgramSock.receive(recievePack);
            return new String(recievedBytes).trim();
        } catch (Exception e) {
            return null;
        }
    }

    private InetAddress getBroadcastAddress() throws UnknownHostException {
        WifiManager wifi = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE); // wtf
        DhcpInfo dhcpInfo = wifi.getDhcpInfo();

        int broadcast = (dhcpInfo.ipAddress & dhcpInfo.netmask) | ~dhcpInfo.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) (broadcast >> (k * 8));

        return InetAddress.getByAddress(quads);
    }
}
