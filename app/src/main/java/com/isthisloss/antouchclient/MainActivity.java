package com.isthisloss.antouchclient;

import android.content.DialogInterface;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * Main activity of the application
 *
 * @author isthisloss
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ButtonsListener buttonsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        buttonsListener = null;

        BroadcastSender broadcastSender = new BroadcastSender(this);
        Log.d(TAG, "AsyncTask started");
        broadcastSender.execute();
    }

    /**
     * Callback for async task which send broadcast message
     *
     * @param ip IP-address of found set-top box
     */
    public void onBroadcastSenderFinished(String ip) {
        Log.d(TAG, "AsyncTask callback was called");
        if (ip != null) {
            initNetworking(ip);
        } else {
            Log.d(TAG, "Broadcast failed");
            onConnectionError();
        }
    }

    /**
     * Creates TCP-connection between this device and an endpoint station,
     * also initialize {@link TouchListener} and {@link ButtonsListener}
     *
     * @param ip is an endpoint IP-address
     */
    private void initNetworking(String ip) {
        Log.d(TAG, "Start initialising of networking");
        ImageView iw = (ImageView) findViewById(R.id.imageView);

        Networking networking = new Networking(this);
        networking.connect(ip);

        iw.setOnTouchListener(new TouchListener(networking));
        buttonsListener = new ButtonsListener(networking);
    }

    /**
     * Callback for each pressed Button
     * @param v is a reference to pressed Button
     */
    public void onButtonClick(View v) {
        if (buttonsListener != null) {
            buttonsListener.onClick(v);
        }
    }

    /**
     * Shows error message and terminate the application
     */
    void onConnectionError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.error)
                .setMessage(R.string.connection_error)
                .setCancelable(false)
                .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        builder.create().show();
    }
}
