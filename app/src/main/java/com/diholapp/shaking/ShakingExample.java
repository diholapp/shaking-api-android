package com.diholapp.shaking;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.diholapp.android.shaking.ShakingConfig;
import com.diholapp.android.shaking.ShakingAPI;
import com.diholapp.android.shaking.ShakingCodes;

import java.security.SecureRandom;
import java.util.ArrayList;

public class ShakingExample extends AppCompatActivity {

    private final String TAG = "SHAKING_EXAMPLE";

    private final String API_KEY = "Get one at www.diholapp.com";
    private final String USER_ID = randomString(10);

    private ShakingAPI shakingAPI;
    private ShakingConfig shakingConfig;

    TextView myIdTextView;
    TextView resultTextView;

    Button startStopBtn;

    @Override
    public void onPause() {
        super.onPause();
        stopAPI();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shakingConfig = new ShakingConfig(USER_ID, API_KEY)
                .setDistanceFilter(100);


        shakingAPI = new ShakingAPI(shakingConfig, this){

            @Override
            public void onInit(ArrayList<ShakingCodes> errors) {

                boolean error = false;

                if(errors.contains(ShakingCodes.LOCATION_PERMISSION_ERROR)){
                    error = true;
                    requestLocationPermission();
                }

                if(!error) resultTextView.setText("Shake the device");

                Log.i(TAG, "Error(s): " + errors.toString());
            }

            @Override
            public void onShaking(ArrayList<ShakingCodes> errors) {
                Log.i(TAG, "SHAKING");
                Log.i(TAG, "Error(s): " + errors.toString());
                resultTextView.setText("Loading...");
            }

            @Override
            public void onResult(ArrayList<String> result, ArrayList<ShakingCodes> errors) {

                Log.i(TAG, "RESULT");
                Log.i(TAG, "User(s) found: " + result.toString());
                Log.i(TAG, "Error(s): " + errors.toString());

                resultTextView.setText("User(s) found: " + result.toString());
            }
        };

        configButtons();
        configText();
    }

    private void configText(){
        resultTextView = findViewById(R.id.resultTextView);
        myIdTextView = findViewById(R.id.myIdTextView);
        myIdTextView.setText("My ID: " + USER_ID);
    }

    private void configButtons(){

        startStopBtn = findViewById(R.id.startStopBtn);
        startStopBtn.setOnClickListener((View view) -> {
            if(shakingAPI.isRunning()) stopAPI();
            else startAPI();
        });
    }

    private void startAPI(){
        shakingAPI.start();
        startStopBtn.setText("Stop");
    }

    private void stopAPI(){
        shakingAPI.stop();
        resultTextView.setText("");
        startStopBtn.setText("Start");
    }

    private void requestLocationPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    private String randomString(int len){
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

}
