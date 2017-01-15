package com.isthisloss.antouchclient;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Locale;


// TODO: 08.01.17
// I still don't know how to programming on android
// so first of all figure it out
//
// Make some optimization
// replace TCP with UPD (or not... it needs to some tests)
//
// refactoring
// others depends on server development
public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    private static final String TAG = "MainActivity";

    private TextView twDisplay;
    private ImageView iwTouch;
    private int last_x;
    private int last_y;
    private boolean isClick;

    private PrintWriter out;

    private Handler handler;

    public void onClick(View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        twDisplay = (TextView) findViewById(R.id.twDisplay);
        iwTouch = (ImageView) findViewById(R.id.iwTouch);
        handler = new Handler();
    }

    public void onConnect(View view) {
        iwTouch.setOnTouchListener(this);
        EditText editText = (EditText) findViewById(R.id.etIp);
        new Thread(new ClientThread(editText.getText().toString())).start();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        String off = "";
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                last_x = x;
                last_y = y;
                isClick = true;
                break;
            case MotionEvent.ACTION_UP:
                last_x = x;
                last_y = y;

                if (isClick) {
                    off = String.format(Locale.ENGLISH, "%d", Constants.TAP);
                    out.println(off);
                }
                isClick = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int offset_x = x - last_x;
                int offset_y = y - last_y;
                last_x = x;
                last_y = y;
                isClick = false;
                off = String.format(Locale.ENGLISH, "%d %d %d", Constants.DRAG, offset_x, offset_y);
                out.println(off);
                break;
        }
        twDisplay.setText(off);
        return true;
    }

    class ClientThread implements Runnable {
        private Socket socket;
        private BufferedReader incoming;
        private String servIp;
        private static final int SERVERPORT = 12345;

        public ClientThread(String servIp) {
            this.servIp = servIp;
        }

        @Override
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(servIp);
                socket = new Socket(serverAddr, SERVERPORT);
                incoming = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())), true);

            } catch (Exception e) {
                handler.post(new ToastFromThread("Ошибка подключения"));
                e.printStackTrace();
                return;
            }

            handler.post(new ToastFromThread("Подключено"));

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

    class ToastFromThread implements Runnable {
        private String msg;

        public ToastFromThread(String buff) {
            msg = buff;
        }

        @Override
        public void  run() {
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    class UpdateUI implements Runnable {
        private String msg;

        public UpdateUI(String buff) {
            msg = buff;
        }

        @Override
        public void  run() {
            twDisplay.append("\n");
            twDisplay.append(msg);
        }
    }
}
