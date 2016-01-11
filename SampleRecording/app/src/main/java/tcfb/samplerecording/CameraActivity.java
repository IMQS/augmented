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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {

    CameraPreview cameraPreview;
    OpenGLView openGLView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //make fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        show3D();
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }

    /*
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();        // release the camera for other applications
            mCamera = null;
            cameraPreview.getHolder().removeCallback(cameraPreview);
        }
    }
    */

    void show3D() {
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_rect);
        if (preview.getChildCount() == 0) {
            cameraPreview = new CameraPreview(this);
            openGLView = new OpenGLView(this);
            preview.addView(openGLView);
            preview.addView(cameraPreview);
        }
        else {
            preview.removeAllViews();
            cameraPreview = null;
            openGLView = null;
        }
    }
}