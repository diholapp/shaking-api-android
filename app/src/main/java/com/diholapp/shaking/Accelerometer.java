package com.diholapp.shaking;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

import static java.lang.Math.abs;

public class Accelerometer implements SensorEventListener {

    private final Context mContext;

    private int interval;
    private int intervalTrigger;

    private double lastReading = (double) System.currentTimeMillis();
    private double lastTrigger;

    private final Sensor sensor;
    private final SensorManager sensorManager;

    private Shaking delegate = null;

    public Accelerometer(Shaking delegate) {
        lastTrigger = 0;
        //interval = 2000;
        intervalTrigger = 2000;
        this.delegate = delegate;
        mContext = delegate.getContext();
        sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Log.e("Compass MainActivity", "NOT Registerered ACCELEROMETER");
            Toast.makeText(mContext, "ORIENTATION Sensor not found", Toast.LENGTH_LONG).show();
            //finish();
        }

    }

    public void startUpdates(){
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void stopUpdates(){
        sensorManager.unregisterListener(this);
    }

    public void setUpdateInterval(int newInterval) {
        this.interval = newInterval;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent){
        double tempMs = (double) System.currentTimeMillis();
        if (tempMs - lastReading >= interval){
            lastReading = tempMs;

            if(mustTriggerEvent(sensorEvent, tempMs)){
                lastTrigger = tempMs;
                delegate.onShakingEvent();
            }
        }
    }


    private boolean mustTriggerEvent(SensorEvent sensorEvent, double tempMs){

        double totalValue = abs(sensorEvent.values[0]) + abs(sensorEvent.values[1]) + abs(sensorEvent.values[2]);
        return totalValue > 20 && (tempMs - lastTrigger >= intervalTrigger);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}