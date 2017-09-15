package com.rowdybeats.rowdybeats;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rowdybeats.rowdybeats.fragment.AlarmFragment;
import com.rowdybeats.rowdybeats.fragment.NewsFeedFragment;
import com.rowdybeats.rowdybeats.fragment.StreamFragment;


import com.rowdybeats.rowdybeats.radio.RadioManager;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private NewsFeedFragment newsFeedFragment;
    private StreamFragment streamFragment;
    private AlarmFragment alarmFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            //Return Fragment Manager
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            Intent in;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if(newsFeedFragment == null)
                        newsFeedFragment = NewsFeedFragment.newInstance(MainActivity.this);

                    transaction.replace(R.id.content, newsFeedFragment).commit();
                    return true;
                case R.id.navigation_dashboard:
                    if(streamFragment == null)
                        streamFragment = StreamFragment.newInstance(MainActivity.this);

                    transaction.replace(R.id.content, streamFragment).commit();
                    return true;
                case R.id.navigation_notifications:
                    if(alarmFragment == null)
                        alarmFragment = AlarmFragment.newInstance(MainActivity.this);

                    transaction.replace(R.id.content, alarmFragment).commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        try {
            RadioManager.getInstance(this).fetchStream();
        } catch(Exception e) {
            e.printStackTrace();
            Log.d("debugerino", "RadioManager error: " + e.getMessage());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_home);
    }
}
