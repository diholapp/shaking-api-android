package com.diholapp.android.shaking;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

class Geolocator {

    private ShakingAPI delegate;
    private Location location;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;

    public Geolocator(ShakingAPI delegate){

        this.delegate = delegate;
        this.location = new Location("");
    }

    public void start(){

        this.mLocationManager = (LocationManager) delegate.getContext().getSystemService(Context.LOCATION_SERVICE);

        this.mLocationListener = new LocationListener() {

            public void onLocationChanged(Location location) {

                Location bestLocation = getBestLocation(location,  getLastKnownLocation());
                updateLocation(bestLocation);

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        if(checkLocationPermission()){
            updateLocation(getLastKnownLocation());
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
        }
        else {
            delegate.addError(ShakingCodes.LOCATION_PERMISSION_ERROR);
        }
    }

    public void stop(){
        mLocationManager.removeUpdates(mLocationListener);
    }

    public Location getLocation(){
        return location;
    }

    public void setLocation(Location location){
        if(location != null){
            this.location = location;
        }
    }

    private void updateLocation(Location location){
        if(!delegate.getConfig().isLocationManual() && location != null) {
            this.location = location;
        }
    }

    private Location getLastKnownLocation(){
        Location lastKnownLocationGPS = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location lastKnownLocationNetwork = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        return getBestLocation(lastKnownLocationGPS, lastKnownLocationNetwork);
    }

    boolean isLocationEnabled(){

        boolean gpsEnabled = false;
        boolean networkEnabled = false;

        try {
            gpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            networkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        return gpsEnabled || networkEnabled;
    }

    private boolean checkLocationPermission(){
        return (ActivityCompat.checkSelfPermission(delegate.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    /** Determines whether one Location reading is better than the current Location fix
     * @param location  The new Location that you want to evaluate
     * @param currentBestLocation  The current Location fix, to which you want to compare the new one
     */
    private Location getBestLocation(Location location, Location currentBestLocation) {

        if (currentBestLocation == null) {
            // A new location is always better than no location
            return location;
        }

        if (location == null) {
            // Avoids fatal crash
            return currentBestLocation;
        }

        final int TWO_MINUTES = 1000 * 60 * 2;

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return location;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return currentBestLocation;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return location;
        } else if (isNewer && !isLessAccurate) {
            return location;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return location;
        }
        return currentBestLocation;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

}
