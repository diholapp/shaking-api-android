package com.diholapp.shaking;

public interface AsyncResponse {
    void processFinish(String output);

    void onShakingEvent();
}