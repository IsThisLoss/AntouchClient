package com.isthisloss.antouchclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView twDisplay;
    private ImageView iwTouch;
    private Networking networking;
    private TouchListener touchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        twDisplay = (TextView) findViewById(R.id.twDisplay);
        iwTouch = (ImageView) findViewById(R.id.iwTouch);
    }

    public void onConnect(View view) {
        EditText editText = (EditText) findViewById(R.id.etIp);
        networking = new Networking(this);
        networking.setServerInfo(editText.getText().toString(), 12345);
        networking.start();
        touchListener = new TouchListener(networking);

        iwTouch.setOnTouchListener(touchListener);
//        iwTouch.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                event.getAction()
//                String s = String.format(Locale.ENGLISH, "x = %f y = %f\n", event.getX(), event.getY());
//                twDisplay.setText(s);
//                return false;
//            }
//        });
    }
}
