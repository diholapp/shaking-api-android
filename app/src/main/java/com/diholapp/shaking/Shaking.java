package com.diholapp.shaking;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;


// TODO: Stop in background
// TODO: Location Permission

public class Shaking implements AsyncResponse {

    private final String API_KEY;

    private String user;

    private double lat;
    private double lng;

    private double timingFilter;
    private double maxTimeSearch;
    private double refreshInterval;
    private double distanceFilter;

    private boolean keepSearching;

    private final Context mContext;

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;

    private Accelerometer mAccelerometer;

    public Shaking(String user, String API_KEY, Context context){

        this.user = user;
        this.API_KEY = API_KEY;
        this.mContext = context;

        this.timingFilter = 2;
        this.maxTimeSearch = 2;
        this.distanceFilter = 100;
        this.refreshInterval = 0.25;
        this.keepSearching = false;

        this.mAccelerometer = new Accelerometer(this);

        configLocationListener();
    }

    public void start(){

        // Register the listener with the Location Manager to receive location updates
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);

    }

    public void stop(){
        mLocationManager.removeUpdates(mLocationListener);
    }

    public void simulate(){

    }

    @Override
    public void onShakingEvent(){
        new HTTPAsyncTask(this).execute();
    }

    @Override
    public void processFinish(String output){
        //Here you will receive the result fired from async class
        //of onPostExecute(result) method.
        Log.i("a ver...", output);
    }

    private void configLocationListener(){

        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        Location lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(lastKnownLocation != null){
            setLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
        }

        mLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {

                Location lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Location bestLocation = GPSLocation.getBestLocation(location, lastKnownLocation);
                setLocation(bestLocation.getLatitude(), bestLocation.getLongitude());

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

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

    public String getApiKey(){
        return API_KEY;
    }

    public Shaking setUser(String user){
        this.user = user;
        return this;
    }

    public String getUser(){
        return user;
    }

    public Shaking setTimingFilter(double timingFilter){
        this.timingFilter = timingFilter;
        return this;
    }

    public double getTimingFilter(){
        return timingFilter;
    }

    public Shaking setMaxTimeSearch(double maxTimeSearch){
        this.maxTimeSearch = maxTimeSearch;
        return this;
    }

    public double getMaxTimeSearch(){
        return maxTimeSearch;
    }

    public Shaking setRefreshInterval(double refreshInterval){
        this.refreshInterval = refreshInterval;
        return this;
    }

    public double getRefreshInterval(){
        return refreshInterval;
    }

    public Shaking setDistanceFilter(double distanceFilter){
        this.distanceFilter = distanceFilter;
        return this;
    }

    public double getDistanceFilter(){
        return distanceFilter;
    }

    public Shaking setKeepSearching(boolean keepSearching){
        this.keepSearching = keepSearching;
        return this;
    }

    public boolean getKeepSearching(){
        return keepSearching;
    }

}
