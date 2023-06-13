package com.example.myfinalproject;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class ShakeDetector implements SensorEventListener {

    /**
     * The threshold value to determine shake detection sensitivity.
     * Higher values make it less sensitive to shakes.
     */
    private static final float SHAKE_THRESHOLD = 50f; // Adjust this value as needed

    /**
     * The minimum interval (in milliseconds) between two shake events.
     */
    private static final int SHAKE_INTERVAL = 500; // Adjust this value as needed

    /**
     * The listener to be notified when a shake gesture is detected.
     */
    private OnShakeListener listener;

    /**
     * The timestamp of the last shake event.
     */
    private long lastShakeTime;

    public ShakeDetector(OnShakeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float acceleration = (float) Math.sqrt(x * x + y * y + z * z);
            long currentTime = System.currentTimeMillis();

            if (acceleration > SHAKE_THRESHOLD) {
                if (currentTime - lastShakeTime >= SHAKE_INTERVAL) {
                    lastShakeTime = currentTime;
                    listener.onShakeDetected();
                }
            }
        }
    }

    /**
     * Called when the accuracy of a sensor changes.
     * Not used in this implementation.
     * @param sensor The Sensor object whose accuracy changed.
     * @param accuracy The new accuracy value.
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }

    /**
     * The interface to be implemented by listeners to receive shake detection events.
     */
    public interface OnShakeListener {
        /**
         * Called when a shake gesture is detected.
         */
        void onShakeDetected();
    }
}