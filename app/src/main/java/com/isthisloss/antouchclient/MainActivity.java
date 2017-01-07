package com.isthisloss.antouchclient;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;



// TODO: 07.01.17
// I still don't know how to programming on android
// so first of all figure it out
//
// Make some optimization (line 72)
// replace TCP with UPD (or not... it needs to some tests)
//
// add one tap action
// others depends on server development
public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private TextView display;
    private ImageView imageView;
    private float last_x;
    private float last_y;

    private Socket socket;
    private Handler handler;

    private static final String SERVERIP = "192.168.1.71";
    private static final int SERVERPORT = 12345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.imageView);

        imageView.setOnTouchListener(this);

        handler = new Handler();
        new Thread(new ClientThread()).start();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        String off = "";
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                last_x = x;
                last_y = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float offset_x = x - last_x;
                last_x = x;
                float offset_y = y - last_y;
                last_y = y;
                off = "" + offset_x + " " + offset_y;

                try {
                    PrintWriter out = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream())), true);
                    out.print(off);
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        display.setText(off);
        return true;
    }

    class ClientThread implements Runnable {
        private BufferedReader incoming;

        @Override
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(SERVERIP);
                socket = new Socket(serverAddr, SERVERPORT);
                incoming = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            while (!Thread.currentThread().isInterrupted()) {
                String buff;
                try {
                    buff = incoming.readLine();
                    handler.post(new UpdateUI(buff));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class UpdateUI implements Runnable {
        private String msg;

        public UpdateUI(String buff) {
            msg = buff;
        }

        @Override
        public void  run() {
            display.append("\n");
            display.append(msg);
        }
    }
}
