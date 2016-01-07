package tcfb.samplerecording;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by marvin on 2016/01/06.
 * This class stores data gathered from device sensors so that they may be polled at any
 * time during execution.
 *
 * Rationale: No simple method for polling device sensors in Java. Need to group data so that a
 * measurement for each sensor can be attached to each camera frame.
 */
public class SensorPoll implements SensorEventListener {
    private static final String SENSOR_DATA_DIR= "sensor_data";
    private long startTime, elapsedTime;
    private SensorEvent accelEvent = null, gyroEvent = null, magneticFieldEvent = null;
    private File appDir, outFile, dataDir;
    private BufferedWriter bw = null;
    private boolean start = false;

    public SensorPoll(File appDir, long startTime) {
        this.startTime = startTime;
        this.appDir = appDir;


        dataDir = new File( appDir, SENSOR_DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }


    }

    public void start() {
        int numFiles = dataDir.listFiles().length;
        outFile = new File(dataDir, new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".txt");
        try {
            bw = new BufferedWriter(new FileWriter(outFile));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        System.out.println("----------------------> outFile " + outFile);
        start = true;
    }

    public void stop() {
        start = false;
        try {
            bw.flush();
            bw.close();
        } catch (Exception e) {

        }
    }

    /* Returns last recorded acceleration measurement. Returns null if no measurement has been
     * made yet.
     */
    public SensorEvent pollAccel() {
        elapsedTime = System.currentTimeMillis() - startTime;
        return accelEvent;
    }
    /* Returns last recorded gyroscope measurement. Returns null if no measurement has been
     * made yet.
     */
    public SensorEvent pollGyro() {
        elapsedTime = System.currentTimeMillis() - startTime;
        return gyroEvent;
    }

    /* Returns last recoreded acceleration measurement. Returns null if no measurement has been
     * made yet.
     */
    public SensorEvent pollMagneticField() {
        elapsedTime = System.currentTimeMillis() - startTime;
        return magneticFieldEvent;
    }

    public Map<String, Object> pollAll() {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("accelerometer", pollAccel());
        map.put("gyroscope", pollGyro());
        map.put("magnetic_field", pollMagneticField());
        map.put("time", elapsedTime);
        return map;
    }

    public void setTime(long time) { this.startTime = time; }

    /* Do nothing because I don't even care right now */
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    /* Update sensor data */
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER: accelEvent = event; break;
            case Sensor.TYPE_MAGNETIC_FIELD: magneticFieldEvent = event; break;
            case Sensor.TYPE_GYROSCOPE: gyroEvent = event; break;
        }

        if (!start) return;
        try {
            bw.write(accelEvent.values[0] + " " + accelEvent.values[1] + " " + accelEvent.values[2] + " ");
            bw.write(gyroEvent.values[0] + " " + gyroEvent.values[1] + " " + gyroEvent.values[2] + " ");
            bw.write(magneticFieldEvent.values[0] + " " + magneticFieldEvent.values[1] + " " + magneticFieldEvent.values[2] + " ");
            bw.write((System.currentTimeMillis() - startTime) + "\n");
        } catch (IOException ioe) {
            ioe.printStackTrace();
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
