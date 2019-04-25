package com.diholapp.android.shaking;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;


public class ShakingAPI implements AsyncResponse {

    private final String API_KEY;

    private String user;

    private double lat;
    private double lng;

    private double timingFilter = 2;
    private double maxTimeSearch = 2;
    private double refreshInterval = 0.25;
    private double distanceFilter = 100;
    private double sensibility = 20;

    private boolean keepSearching = false;

    private final Context mContext;

    private Accelerometer mAccelerometer;

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;

    private boolean running = false;

    public ShakingAPI(String user, String API_KEY, Context context){

        this.user = user;
        this.API_KEY = API_KEY;
        this.mContext = context;

        this.mAccelerometer = new Accelerometer(this);

        configLocationListener();
    }

    public ShakingAPI start(){

        if(!running){
            running = true;
            registerLocationListener();
            mAccelerometer.startUpdates();
        }

        return this;
    }

    public ShakingAPI stop(){

        if(running){
            running = false;
            unregisterLocationListener();
            mAccelerometer.stopUpdates();
        }

        return this;
    }

    public void simulate(){
        new HTTPAsyncTask(this).execute();
    }

    public void sendBroadcast(String s) {
        mContext.sendBroadcast(new Intent(s));
    }

    @Override
    public void onShakingEvent(){

        sendBroadcast(ShakingIntents.SHAKING);
        new HTTPAsyncTask(this).execute();
    }

    @Override
    public void processFinish(String output){

        Intent intent = new Intent();

        try {
            ArrayList<String> response = processJsonResponse(output);

            if(response.size() > 0){
                intent.setAction(ShakingIntents.MATCHED);
                intent.putExtra("result", response);
            }
            else {
                intent.setAction(ShakingIntents.NOT_MATCHED);
            }

        }
        catch (Exception e){

            if ("401".equals(e.getMessage())) {
                intent.setAction(ShakingIntents.AUTHENTICATION_ERROR);
            }
            else if ("403".equals(e.getMessage())){
                intent.setAction(ShakingIntents.API_KEY_EXPIRED);
            }
            else {
                intent.setAction(ShakingIntents.SERVER_ERROR);
            }
        }

        mContext.sendBroadcast(intent);
    }

    private ArrayList<String> processJsonResponse(String jsonString) throws Exception{

        Object obj = new JSONParser().parse(jsonString);

        // typecasting obj to JSONObject
        JSONObject jo = (JSONObject) obj;

        // getting address
        Map status = ((Map)jo.get("status"));

        // iterating address Map
        Iterator<Map.Entry> itr1 = status.entrySet().iterator();
        while (itr1.hasNext()) {
            Map.Entry pair = itr1.next();

            String key = pair.getKey().toString();

            if(key.equals("code")) {
                String value = pair.getValue().toString();
                if (!value.equals("200")){
                    throw new Exception(value);
                }
            }

        }

        JSONArray jArray = (JSONArray) jo.get("response");

        ArrayList<String> result = new ArrayList<>();
        if (jArray != null) {
            for (int i=0;i<jArray.size();i++){
                result.add(jArray.get(i).toString());
            }
        }

        return result;
    }

    private boolean checkLocationPermission(){
        return (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    private Location getLastKnownLocation(){
        Location lastKnownLocationGPS = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location lastKnownLocationNetwork = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        return GPSLocation.getBestLocation(lastKnownLocationGPS, lastKnownLocationNetwork);
    }

    private void configLocationListener(){

        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        if(checkLocationPermission()){
            setLocation(getLastKnownLocation());
        }

        mLocationListener = new LocationListener() {

            public void onLocationChanged(Location location) {

                Location bestLocation = GPSLocation.getBestLocation(location,  getLastKnownLocation());
                setLocation(bestLocation);

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

    }

    /**
     * Register for location updates if location permissions
     * have been accepted. Sends a broadcast if not.
     */
    private void registerLocationListener(){

        if(checkLocationPermission()){
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
        }
        else {
            sendBroadcast(ShakingIntents.LOCATION_PERMISSION_ERROR);
        }
    }

    private void unregisterLocationListener(){
        mLocationManager.removeUpdates(mLocationListener);
    }

    public Context getContext(){
        return mContext;
    }

    public double getLat(){
        return lat;
    }

    public double getLng(){
        return lng;
    }

    public void setLocation(double lat, double lng){
        this.lat = lat;
        this.lng = lng;
    }

    public void setLocation(Location location){
        if(location != null) {
            this.lat = location.getLatitude();
            this.lng = location.getLongitude();
        }
    }

    public String getApiKey(){
        return API_KEY;
    }

    public ShakingAPI setUser(String user){
        this.user = user;
        return this;
    }

    public String getUser(){
        return user;
    }

    public ShakingAPI setTimingFilter(double timingFilter){
        this.timingFilter = timingFilter;
        return this;
    }

    public double getTimingFilter(){
        return timingFilter;
    }

    public ShakingAPI setMaxTimeSearch(double maxTimeSearch){
        this.maxTimeSearch = maxTimeSearch;
        return this;
    }

    public double getMaxTimeSearch(){
        return maxTimeSearch;
    }

    public ShakingAPI setRefreshInterval(double refreshInterval){
        this.refreshInterval = refreshInterval;
        return this;
    }

    public double getRefreshInterval(){
        return refreshInterval;
    }

    public ShakingAPI setDistanceFilter(double distanceFilter){
        this.distanceFilter = distanceFilter;
        return this;
    }

    public double getDistanceFilter(){
        return distanceFilter;
    }

    public ShakingAPI setKeepSearching(boolean keepSearching){
        this.keepSearching = keepSearching;
        return this;
    }

    public boolean getKeepSearching(){
        return keepSearching;
    }

    public ShakingAPI setSensibility(double sensibility){
        this.sensibility = sensibility;
        return this;
    }

    public double getSensibility(){
        return sensibility;
    }

}
