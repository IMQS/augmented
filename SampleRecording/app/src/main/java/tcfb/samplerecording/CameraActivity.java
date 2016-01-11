package tcfb.samplerecording;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;


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