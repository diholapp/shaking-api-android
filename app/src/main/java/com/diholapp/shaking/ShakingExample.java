package com.diholapp.shaking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.diholapp.android.shaking.ShakingAPI;
import com.diholapp.android.shaking.ShakingIntents;

import java.util.ArrayList;

public class ShakingExample extends AppCompatActivity {

    private final String TAG = "SHAKING_EXAMPLE";
    private ShakingAPI api;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            final String action = intent.getAction();

            switch (action){
                case ShakingIntents.SHAKING:
                    Log.i(TAG, "SHAKING");
                    break;
                case ShakingIntents.MATCHED:
                    Log.i(TAG, "MATCHED");
                    ArrayList<String> result = intent.getStringArrayListExtra("result");
                    break;
                case ShakingIntents.NOT_MATCHED:
                    Log.i(TAG, "NOT_MATCHED");
                    break;
                case ShakingIntents.LOCATION_PERMISSION_ERROR:
                    Log.i(TAG, "LOCATION_PERMISSION_ERROR");
                    requestLocationPermission();
                    break;
                case ShakingIntents.AUTHENTICATION_ERROR:
                    Log.i(TAG, "AUTHENTICATION_ERROR");
                    break;
                case ShakingIntents.API_KEY_EXPIRED:
                    Log.i(TAG, "API_KEY_EXPIRED");
                    break;
                case ShakingIntents.TIMEOUT:
                    Log.i(TAG, "TIMEOUT");
                    break;
                case ShakingIntents.SERVER_ERROR:
                    Log.i(TAG, "SERVER_ERROR");
                    break;
            }
        }
    };

    private void requestLocationPermission(){
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ShakingIntents.SHAKING);
        filter.addAction(ShakingIntents.MATCHED);
        filter.addAction(ShakingIntents.NOT_MATCHED);
        filter.addAction(ShakingIntents.LOCATION_PERMISSION_ERROR);
        filter.addAction(ShakingIntents.AUTHENTICATION_ERROR);
        filter.addAction(ShakingIntents.API_KEY_EXPIRED);
        filter.addAction(ShakingIntents.TIMEOUT);
        filter.addAction(ShakingIntents.SERVER_ERROR);
        registerReceiver(receiver, filter);

        api = new ShakingAPI("1", "qwerty", this)
                .setMaxTimeSearch(3000)
                .setDistanceFilter(80)
                .start();
    }


    @Override
    public void onResume() {
        super.onResume();
        api.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        api.stop();
    }

}
