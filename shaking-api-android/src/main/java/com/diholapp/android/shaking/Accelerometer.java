package com.diholapp.android.shaking;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import static java.lang.Math.abs;

class Accelerometer implements SensorEventListener {

    private final int interval = 100;

    private double lastReading = (double) System.currentTimeMillis();

    private long lastShakingTimestamp;

    private Sensor sensor;
    private final SensorManager sensorManager;

    private ShakingAPI delegate;

    Accelerometer(ShakingAPI delegate) {

        this.delegate = delegate;
        sensorManager = (SensorManager) delegate.getContext().getSystemService(Context.SENSOR_SERVICE);

        try {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        } catch (NullPointerException e){
            sensor = null;
        }
    }

    void start(){
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else {
            delegate.addError(ShakingCodes.SENSOR_ERROR);
        }
    }

    void stop(){
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent){
        double tempMs = (double) System.currentTimeMillis();
        if (tempMs - lastReading >= interval){
            lastReading = tempMs;

            if(mustTriggerEvent(sensorEvent)){
                onShakingEvent();
            }
        }
    }

    void onShakingEvent(){
        lastShakingTimestamp = System.nanoTime();
        delegate.onShakingEvent();
    }

    long getLastShakingTimestamp(){
        return lastShakingTimestamp;
    }

    private boolean mustTriggerEvent(SensorEvent sensorEvent){

        double totalValue = abs(sensorEvent.values[0]) + abs(sensorEvent.values[1]) + abs(sensorEvent.values[2]);
        return totalValue > delegate.getConfig().getSensibility();
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}