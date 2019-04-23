package com.diholapp.shaking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    private Shaking api;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            switch (action){
                case ShakingIntents.SHAKING:
                    break;
                case ShakingIntents.MATCHED:
                    break;
                case ShakingIntents.NOT_MATCHED:
                    break;
                case ShakingIntents.LOCATION_PERMISSION_ERROR:
                    break;
                case ShakingIntents.AUTHENTICATION_ERROR:
                    break;
                case ShakingIntents.API_KEY_EXPIRED:
                    break;
                case ShakingIntents.TIMEOUT:
                    break;
                case ShakingIntents.SERVER_ERROR:
                    break;
            }
        }
    };

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

        api = new Shaking("1", "qwerty", this);
        api.start();

        //ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
        //                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 3456);
    }




}
