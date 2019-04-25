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

public class MainActivity extends AppCompatActivity {

    private ShakingAPI api;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            final String action = intent.getAction();

            switch (action){
                case ShakingIntents.SHAKING:
                    Log.i("HH2", "SHAKED");
                    break;
                case ShakingIntents.MATCHED:
                    ArrayList<String> result = intent.getStringArrayListExtra("result");
                    break;
                case ShakingIntents.NOT_MATCHED:
                    Log.i("HH2", "NOT_MATCHED");
                    break;
                case ShakingIntents.LOCATION_PERMISSION_ERROR:
                    askPermmission();
                    Log.i("HH2", "LOCATION_PERMISSION_ERROR");
                    break;
                case ShakingIntents.AUTHENTICATION_ERROR:
                    Log.i("HH2", "AUTHENTICATION_ERROR");
                    break;
                case ShakingIntents.API_KEY_EXPIRED:
                    Log.i("HH2", "API_KEY_EXPIRED");
                    break;
                case ShakingIntents.TIMEOUT:
                    Log.i("HH2", "TIMEOUT");
                    break;
                case ShakingIntents.SERVER_ERROR:
                    Log.i("HH2", "SERVER_ERROR");
                    break;
            }
        }
    };

    private void askPermmission(){
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 3456);
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

        api = new ShakingAPI("1", "qwerty", this);
        api.start();

        //ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 3456);
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
