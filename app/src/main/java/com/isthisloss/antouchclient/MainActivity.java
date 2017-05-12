package com.isthisloss.antouchclient;

import android.content.DialogInterface;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Main activity of the application
 *
 * @author isthisloss
 */
public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";

    private ButtonsListener buttonsListener;
    private Networking networking;

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

        networking = new Networking(this);
        networking.connect(ip);

        int w = iw.getWidth();
        iw.setOnTouchListener(new TouchListener(networking, (int) (w - 0.1 * w))); // 10% from right corner for scroll area
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
     * Opens dialog with EditText to input text message
     *
     * @param v is the keyboard button
     */
    public void onTextInputClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.text_input);
        final EditText input = new EditText(this);
        builder.setView(input);
        builder.setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                networking.send(ProtoAtci.text(input.getText().toString()));
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
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
