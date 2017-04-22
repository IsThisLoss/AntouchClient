package com.isthisloss.antouchclient;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;


/**
 * Created by Dima on 11.03.2017.
 */

/**
 * Class which provide networking function
 *
 * @author isthisloss
 */
class Networking {
    private final static String TAG = "Networking";

    private MainActivity mainActivity;
    private DataOutputStream dataOutputStream;
    private String serverIp;

    /**
     * Constructor
     * @param mainActivity is a reference to main activity of the application
     */
    Networking(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    /**
     * Tries to connect with owner of this IP-address
     *
     * @param ip is an IP-address of endpoint
     */
    void connect(String ip) {
        serverIp = ip;
        new Thread(new StartNetworking()).start();
    }

    /**
     * Sends {@link ProtoAtci} message to connected set top box
     *
     * @param msg is an array of byte which contains {@link ProtoAtci} message
     */
    void send(final byte[] msg) {
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

    /**
     * Initialize TCP connection
     * It has to be due to network rules of android system
     * Because every network must be outside UI thread
     */
    private class StartNetworking implements Runnable {
        @Override
        public void run() {
            try {
                Socket socket = new Socket(InetAddress.getByName(serverIp), Constants.TCP_PORT);
                //incoming = new BufferedReader(new InputStreamReader(socket.getInputStream())); maybe for future use
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
            } catch (Exception e) {
                Log.d(TAG, "Exception in StartNetworking");
                mainActivity.onConnectionError();
            }
        }
    }
}
