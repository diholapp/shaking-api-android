package com.diholapp.android.shaking;

public interface AsyncResponse {
    void processFinish(String output);

    void onShakingEvent();
}