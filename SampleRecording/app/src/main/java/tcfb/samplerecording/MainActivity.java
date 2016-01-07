package tcfb.samplerecording;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sm;
    private Sensor accelerometer;
    private Sensor accel, gyro, compass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Get all available sensors */
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accel = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyro = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        compass = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        System.out.println("THIS THIS IS STARTING------------------------");
    }

    protected void onPause() {
        sm.unregisterListener(this, accel);
        sm.unregisterListener(this, gyro);
        sm.unregisterListener(this, compass);
        super.onPause();
    }

    protected void onResume() {
        sm.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, gyro, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, compass, SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        System.out.println("Accel: Accuracy Changed");
    }

    public void onSensorChanged(SensorEvent event) {
        float[] vals = {event.values[0], event.values[1], event.values[2]};
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                System.out.println("Accel: " + "X: " + vals[0] + " Y: " + vals[1] + " Z: " + vals[2]);
                break;
            case Sensor.TYPE_GYROSCOPE:
                System.out.println("Gyro: " + "X: " + vals[0] + " Y: " + vals[1] + " Z: " + vals[2]);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                System.out.println("Compass: " + "X: " + vals[0] + " Y: " + vals[1] + " Z: " + vals[2]);
                break;
        }
    }
}
