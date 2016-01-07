package tcfb.samplerecording;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by marvin on 2016/01/06.
 * This class stores data gathered from device sensors so that they may be polled at any
 * time during execution.
 *
 * Rationale: No simple method for polling device sensors in Java. Need to group data so that a
 * measurement for each sensor can be attached to each camera frame.
 */
public class SensorPoll implements SensorEventListener {
    SensorEvent accelEvent = null, gyroEvent = null, magneticFieldEvent = null;

    /* Returns last recorded acceleration measurement. Returns null if no measurement has been
     * made yet.
     */
    public SensorEvent pollAccel() {
       return accelEvent;
    }

    /* Returns last recorded gyroscope measurement. Returns null if no measurement has been
     * made yet.
     */
    public SensorEvent pollGyro() {
        return gyroEvent;
    }

    /* Returns last recoreded acceleration measurement. Returns null if no measurement has been
     * made yet.
     */
    public SensorEvent pollMagneticField() {
        return magneticFieldEvent;
    }

    /* Do nothing because I don't even care right now */
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    /* Update sensor data */
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER: accelEvent = event; break;
            case Sensor.TYPE_GYROSCOPE: gyroEvent = event; break;
            case Sensor.TYPE_MAGNETIC_FIELD: magneticFieldEvent = event; break;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        if (accelEvent != null)
            sb.append("Accel: X: " + accelEvent.values[0] + " Y: " + accelEvent.values[1] + " Z: " + accelEvent.values[2] + "\n");
        else
            sb.append("Accel: Null");
        if (gyroEvent != null)
            sb.append("Gyro: X: " + gyroEvent.values[0] + " Y: " + gyroEvent.values[1] + " Z: " + gyroEvent.values[2] + "\n");
        else
            sb.append("Gyro: Null");
        if (magneticFieldEvent != null)
            sb.append("Magnetic Field: X: " + magneticFieldEvent.values[0] + " Y: " + magneticFieldEvent.values[1] + " Z: " + magneticFieldEvent.values[2] + "\n");
        else
            sb.append("Magnetic Field: Null");
        sb.append("-----                                                    ------");

        return sb.toString();
    }
}
