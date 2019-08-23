package com.diholapp.android.shaking;

import java.util.ArrayList;

interface ShakingAPIInterface {
    void onInit(ArrayList<ShakingCodes> errors);
    void onShaking(ArrayList<ShakingCodes> errors);
    void onResult(ArrayList<String> result, ArrayList<ShakingCodes> errors);
}
