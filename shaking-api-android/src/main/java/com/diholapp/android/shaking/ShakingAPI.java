package com.diholapp.android.shaking;

import android.os.Handler;
import android.content.Context;
import android.location.Location;

import com.diholapp.android.shaking.server.ServerRequestConnect;
import com.diholapp.android.shaking.server.ServerResponse;

import java.security.SecureRandom;
import java.util.ArrayList;

public class ShakingAPI implements ShakingAPIInterface {

    private Status mStatus;
    private ShakingConfig mConfig;
    private Geolocator mGeolocator;
    private Accelerometer mAccelerometer;
    private ServerRequestConnect mServerRequestConnect;
    private ArrayList<ShakingCodes> errors = new ArrayList<>();

    private final Context mContext;

    private String idRequest;

    public ShakingAPI(ShakingConfig config, Context context){

        this.mConfig = config;
        this.mContext = context;

        this.mStatus = new Status();

        this.mGeolocator = new Geolocator(this);
        this.mAccelerometer = new Accelerometer(this);
    }

    public ShakingAPI start(){

        if(mStatus.start()){

            mAccelerometer.start();

            if(!mConfig.isLocationManual()) {
                mGeolocator.start();
            }

            executeCallback("init");
        }

        return this;
    }

    public ShakingAPI stop(){

        if(mStatus.stop()){
            mGeolocator.stop();
            mAccelerometer.stop();
            cancelServerRequest();
        }

        return this;
    }

    private void restart(){

        if(mStatus.restart()){
            mServerRequestConnect = null;
            mAccelerometer.start();
            if(!mConfig.isLocationManual()) mGeolocator.start();
        }

    }

    private void pause(){

        if(mStatus.pause()){
            mGeolocator.stop();
            mAccelerometer.stop();
        }
    }

    public void simulate(){
        mAccelerometer.onShakingEvent();
    }

    private void executeCallback(String callback) {
        if(callback.equals("init")) onInit(errors);
        else if (callback.equals("shaking")) onShaking(errors);
        errors = new ArrayList<>();
    }

    private void executeCallback(String callback, ArrayList result) {
        if(callback.equals("result")) onResult(result, errors);
        errors = new ArrayList<>();
    }

    void onShakingEvent(){

        updateRequestId();

        if(!mGeolocator.isLocationEnabled()){
            addError(ShakingCodes.LOCATION_DISABLED);
        }

        mStatus.setProcessing(true);

        pause();
        executeCallback("shaking");

        decideResult();
    }

    private void updateRequestId(){
        int length = 10;
        String AB = "0123456789abcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for( int i = 0; i < length; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        idRequest = sb.toString();
    }

    private void decideResult(){

        if(mServerRequestConnect == null){

            mServerRequestConnect = new ServerRequestConnect(this);
            mServerRequestConnect.execute();

        }

    }

    void onResponseProcessed(ArrayList result){

        if(!isRunning() || !isProcessing()) return;

        mStatus.setProcessing(false);

        executeCallback("result", result);
        restart();
    }

    public void processServerResponse(ServerResponse serverResponse){

        if(!isRunning()) return;

        boolean invalidKey = false;
        boolean serverError = false;
        ArrayList<String> result = new ArrayList<>();

        try {
            result = serverResponse.getArrayFromResponse();
        }
        catch (Exception e){

            if ("401".equals(e.getMessage())) {
                invalidKey = true;
                addError(ShakingCodes.AUTHENTICATION_ERROR);
            }
            else if ("403".equals(e.getMessage())){
                invalidKey = true;
                addError(ShakingCodes.API_KEY_EXPIRED);
            }
            else {
                if(mConfig.isOnlineEnabled()){
                    serverError = true;
                    addError(ShakingCodes.SERVER_ERROR);
                }
            }
        }

        ArrayList<String> finalResult = new ArrayList<>(result);

        // Avoids overlapping in case of no internet connection
        Handler handler = new Handler();
        handler.postDelayed(() -> onResponseProcessed(finalResult), serverError ? 2000 : 0);
    }

    void cancelServerRequest(){
        if(mServerRequestConnect != null){
            mServerRequestConnect.cancel(true);
            mServerRequestConnect = null;
        }
    }

    void addError(ShakingCodes err){
        if(!errors.contains(err)){
            errors.add(err);
        }
    }

    public String getIdRequest(){
        return idRequest;
    }


    public ShakingAPI setLocation(double lat, double lng){

        Location location = new Location("");
        location.setLatitude(lat);
        location.setLongitude(lng);

        mConfig.setManualLocation(true);
        mGeolocator.setLocation(location);

        return this;
    }

    public ShakingAPI setLocation(Location location){
        mGeolocator.setLocation(location);
        mConfig.setManualLocation(true);

        return this;
    }

    public ShakingAPI setConfig(ShakingConfig config){
        mConfig = config;
        return this;
    }

    public ShakingConfig getConfig(){
        return mConfig;
    }
    public boolean isRunning() { return !mStatus.isStopped(); }
    public boolean isProcessing() { return mStatus.isProcessing(); }

    Context getContext(){
        return mContext;
    }

    public String getUserId(){
        return mConfig.getUserId();
    }
    public Location getLocation(){
        return mGeolocator.getLocation();
    }

    @Override
    public void onInit(ArrayList<ShakingCodes> errors) {

    }

    @Override
    public void onResult(ArrayList<String> result, ArrayList<ShakingCodes> errors) {

    }

    @Override
    public void onShaking(ArrayList<ShakingCodes> errors) {

    }
}
