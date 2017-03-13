package com.isthisloss.antouchclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LauncherActivity extends AppCompatActivity {
    private TextView textMsg;
    private ProgressBar progressBar;
    private Button searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        textMsg = (TextView) findViewById(R.id.textMsg);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        searchBtn = (Button) findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new OnSearchListener());
    }

    private class OnSearchListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            searchBtn.setEnabled(false);
            progressBar.setVisibility(ProgressBar.VISIBLE);
            BroadcastSender broadcastSender = new BroadcastSender(getApplicationContext(), new ErrorListener());
            String ip = broadcastSender.getStableAddress();
            Intent mainActivityIntent = new Intent(getApplicationContext(), Main2Activity.class);
            mainActivityIntent.putExtra("Launcher", ip);
            startActivity(mainActivityIntent);
        }
    }

    private class ErrorListener implements Runnable {
        @Override
        public void run() {
            searchBtn.setEnabled(true);
            progressBar.setVisibility(ProgressBar.INVISIBLE);
            textMsg.setText(R.string.NetError);
        }
    }
}
