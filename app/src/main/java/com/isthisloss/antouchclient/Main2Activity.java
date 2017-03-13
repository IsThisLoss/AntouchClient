package com.isthisloss.antouchclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class Main2Activity extends AppCompatActivity {
    private static final String TAG = "Main2Activity";
    private TextView twDisplay;
    private ImageView iwTouch;
    private Networking2 networking;
    private TouchListener touchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_back);
        twDisplay = (TextView) findViewById(R.id.twDisplay);
        iwTouch = (ImageView) findViewById(R.id.iwTouch);
    }

    public void onConnect(View view) {
        //EditText editText = (EditText) findViewById(R.id.etIp);

        networking = new Networking2(this);
        //networking.setServerInfo(editText.getText().toString(), 12345);
        networking.start();
        touchListener = new TouchListener(networking);

        iwTouch.setOnTouchListener(touchListener);
    }
}
