package com.diholapp.android.shaking;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import static java.lang.Math.abs;

public class Accelerometer implements SensorEventListener {

    private final Context mContext;

    private final int interval = 100;

    private double lastReading = (double) System.currentTimeMillis();

    private final Sensor sensor;
    private final SensorManager sensorManager;

    private ShakingAPI delegate;

    public Accelerometer(ShakingAPI delegate) {

        this.delegate = delegate;
        mContext = delegate.getContext();
        sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    public void startUpdates(){
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else {
            delegate.sendBroadcast(ShakingIntents.SENSOR_ERROR);
        }
    }

    public void stopUpdates(){
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent){
        double tempMs = (double) System.currentTimeMillis();
        if (tempMs - lastReading >= interval){
            lastReading = tempMs;

            if(mustTriggerEvent(sensorEvent, tempMs)){
                delegate.onShakingEvent();
            }
        }
    }


    private boolean mustTriggerEvent(SensorEvent sensorEvent, double tempMs){

        double totalValue = abs(sensorEvent.values[0]) + abs(sensorEvent.values[1]) + abs(sensorEvent.values[2]);
        return totalValue > delegate.getSensibility();
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}