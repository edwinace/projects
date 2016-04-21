package com.avans.ronald.snschatapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.avans.ronald.snschatapp.GcmIntentService;
import com.avans.ronald.snschatapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;


public class MainActivity extends ActionBarActivity {

    private static final String EXTRA_MESSAGE = "message";
    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    //TextView mDisplay;
    private GoogleCloudMessaging gcm;
    private AtomicInteger msgId;  //= new AtomicInteger();
    private SharedPreferences prefs;
    private String regid;
    private String SENDER_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onPause() {
        GcmIntentService.checkRunningMode = false;
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        GcmIntentService.checkRunningMode = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.view_chats_button :
                startActivity(new Intent(this, ViewAllChatsActivity.class));
                break;
            case R.id.faq_button :
                startActivity(new Intent(this, FaqActivity.class));
                System.out.println("ok");
                break;
            case R.id.settings_button :
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
    }
}