package tcfb.samplerecording;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private Camera mCamera;
    private SurfaceView mPreview;
    private MediaRecorder mMediaRecorder;
    private SensorManager sm;
    private SensorPoll sp;
    private Sensor accel, gyro, compass;
    private boolean isRecording = false;

    private String TAG = "MyCameraApp";

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;

        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mCamera = getCameraInstance();

        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        /* Get all available sensors */
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sp = new SensorPoll(new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp"), System.currentTimeMillis());
        accel = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyro = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        compass = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // Add a listener to the Capture button
        final Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isRecording) {
                            // stop recording and release camera
                            mMediaRecorder.stop();  // stop the recording
                            releaseMediaRecorder(); // release the MediaRecorder object
                            mCamera.lock();         // take camera access back from MediaRecorder

                            sp.stop();
                            isRecording = false;
                            // inform the user that recording has stopped
                            captureButton.setText("Capture");
                        } else {
                            // initialize video camera
                            if (prepareVideoRecorder()) {
                                // Camera is available and unlocked, MediaRecorder is prepared,
                                // now you can start recording
                                mMediaRecorder.start();

                                sp.setTime(System.currentTimeMillis());
                                sp.start();
                                isRecording = true;
                                // inform the user that recording has started
                                captureButton.setText("Stop");
                            } else {
                                // prepare didn't work, release the camera
                                releaseMediaRecorder();
                            }
                        }
                    }
                }
        );
    }

    protected void onPause() {
        sm.unregisterListener(sp, accel);
        sm.unregisterListener(sp, gyro);
        sm.unregisterListener(sp, compass);
        super.onPause();

        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }

    protected void onResume() {
        sm.registerListener(sp, accel, SensorManager.SENSOR_DELAY_FASTEST);
        sm.registerListener(sp, gyro, SensorManager.SENSOR_DELAY_FASTEST);
        sm.registerListener(sp, compass, SensorManager.SENSOR_DELAY_FASTEST);
        super.onResume();
    }

    private void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    //Prepare the video recorder and set all relavent settings.
    private boolean prepareVideoRecorder() {

        mCamera.unlock();
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setCamera(mCamera);

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());

        mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());

        mMediaRecorder.setCaptureRate(60);
        mMediaRecorder.setMaxDuration(5000);

        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }
}