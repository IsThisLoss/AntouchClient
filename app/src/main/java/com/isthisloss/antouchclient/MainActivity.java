package com.isthisloss.antouchclient;

import android.content.DialogInterface;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Networking networking;
    private ButtonsListener buttonsListener;
    private GestureDetectorCompat gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        /*
         * The {@link android.support.v4.view.PagerAdapter} that will provide
         * fragments for each of the sections. We use a
         * {@link FragmentPagerAdapter} derivative, which will keep every
         * loaded fragment in memory. If this becomes too memory intensive, it
         * may be best to switch to a
         * {@link android.support.v4.app.FragmentStatePagerAdapter}.
         */

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        /*
         * The {@link ViewPager} that will host the section contents.
         */

        CustomViewPager mViewPager = (CustomViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setPagingEnabled(false);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //  mine

        BroadcastSender broadcastSender = new BroadcastSender(this);
        networking = null;
        buttonsListener = null;
        Log.d(TAG, "AsyncTask started");
        broadcastSender.execute();
    }

    public void onTaskFinished(String ip) {
        Log.d(TAG, "AsyncTask callback was called");
        if (ip != null) {
            initNetworking(ip);
        } else {
            failedToBroadcast();
        }
    }

    private void initNetworking(String ip) {
        Log.d("TAG", "Start initialising of networking");
        ImageView iw = (ImageView) findViewById(R.id.iwTouch);
        networking = new Networking(MainActivity.this, new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Все сломалось", Toast.LENGTH_SHORT).show();
            }
        });
        networking.connect(ip);
        gestureDetector = new GestureDetectorCompat(this, new TouchListener(networking));
        iw.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
        buttonsListener = new ButtonsListener(networking);
    }

    private void failedToBroadcast() {
        Log.d(TAG, "Broadcast failed");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ошибка")
                .setMessage("Ошибка подключения")
                .setCancelable(false)
                .setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        builder.create().show();
    }

    public void onButtonClick(View v) {
        if (buttonsListener != null) {
            buttonsListener.onClick(v);
        }
    }

    // auto-generated stuff

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class TabFragmentFactory extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public TabFragmentFactory() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static TabFragmentFactory newInstance(int sectionNumber) {
            TabFragmentFactory fragment = new TabFragmentFactory();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = null;
            int num = getArguments().getInt(ARG_SECTION_NUMBER);
            if (num == 1) {
                rootView = inflater.inflate(R.layout.fragment_touch_tab, container, false);
            } else if (num == 2) {
                rootView = inflater.inflate(R.layout.fragment_others, container, false);
            }
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a TabFragmentFactory (defined as a static inner class below).
            return TabFragmentFactory.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.tab_one);
                case 1:
                    return getResources().getString(R.string.tab_two);
            }
            return null;
        }
    }
}
