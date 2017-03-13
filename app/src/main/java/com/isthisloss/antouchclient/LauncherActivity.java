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
    private boolean errorOccurred;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        textMsg = (TextView) findViewById(R.id.textMsg);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        searchBtn = (Button) findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new OnSearchListener());
        errorOccurred = false;
    }

    private class OnSearchListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            BroadcastSender broadcastSender = new BroadcastSender(LauncherActivity.this, new ErrorListener());
            textMsg.setText(R.string.SearchInstr);
            progressBar.setVisibility(ProgressBar.VISIBLE);
            String ip = broadcastSender.getStableAddress();
            //if (!errorOccurred) {
                Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                mainActivityIntent.putExtra("Launcher", ip);
                startActivity(mainActivityIntent);
            //}
        }
    }

    private class ErrorListener implements Runnable {
        @Override
        public void run() {
            textMsg.setText(R.string.NetError);
            progressBar.setVisibility(ProgressBar.INVISIBLE);
            errorOccurred = true;
        }
    }
}
