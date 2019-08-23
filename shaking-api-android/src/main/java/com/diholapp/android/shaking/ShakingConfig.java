package com.diholapp.android.shaking;

import java.util.ArrayList;

public class ShakingConfig {

    private String userId;
    private String API_KEY;

    private double sensibility = 25;
    private int timingFilter = 2000;
    private int distanceFilter = 100;
    private boolean keepSearching = false;
    private boolean manualLocation = false;

    private boolean onlineEnabled = true;
    private boolean offlineEnabled = true;

    private String mode = "duplex";

    private ArrayList<String> connectOnlyWith = new ArrayList<>();

    public ShakingConfig(String userId, String API_KEY){
        this.userId = userId;
        this.API_KEY = API_KEY;
    }

    public double getSensibility() {
        return sensibility;
    }

    public int getDistanceFilter() {
        return distanceFilter;
    }

    public int getTimingFilter() {
        return timingFilter;
    }

    public boolean getKeepSearching(){
        return keepSearching;
    }

    public String getMode(){
        return mode;
    }

    public String getUserId(){
        return userId;
    }

    public String getApiKey(){
        return API_KEY;
    }

    boolean isOnlineEnabled(){
        return onlineEnabled;
    }

    boolean isOfflineEnabled(){
        return offlineEnabled;
    }

    boolean isLocationManual() {
        return manualLocation;
    }

    public ShakingConfig setManualLocation(boolean manualLocation){
        this.manualLocation = manualLocation;
        return this;
    }

    public ShakingConfig setUserId(String userId){
        this.userId = userId;
        return this;
    }

    public ShakingConfig setDistanceFilter(int distanceFilter) {
        this.distanceFilter = distanceFilter;
        return this;
    }

    public ShakingConfig setSensibility(double sensibility) {
        this.sensibility = sensibility;
        return this;
    }

    public ShakingConfig setTimingFilter(int timingFilter) {
        this.timingFilter = timingFilter;
        return this;
    }

    public ShakingConfig setKeepSearching(boolean keepSearching){
        this.keepSearching = keepSearching;
        return this;
    }

    public ShakingConfig setMode(String mode){
        this.mode = mode;
        return this;
    }


    public ShakingConfig setConnectOnlyWith(ArrayList<String> connectOnlyWith){
        this.connectOnlyWith = new ArrayList<>(connectOnlyWith);
        return this;
    }

    public ArrayList<String> getConnectOnlyWith(){
        return connectOnlyWith;
    }
}
