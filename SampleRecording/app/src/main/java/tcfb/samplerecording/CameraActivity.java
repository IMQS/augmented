package tcfb.samplerecording;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import org.artoolkit.ar.base.ARActivity;
import org.artoolkit.ar.base.rendering.ARRenderer;


public class CameraActivity extends ARActivity {

    CameraPreview cameraPreview;
    OpenGLView openGLView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //make fullscreen
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //        WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        show3D();
    }

    /**
     * Provide our own SimpleRenderer.
     */
    @Override
    protected ARRenderer supplyRenderer() {
        return new OpenGLRenderer();
    }

    /**
     * Use the FrameLayout in this Activity's UI.
     */
    @Override
    protected FrameLayout supplyFrameLayout() {
        return (FrameLayout)this.findViewById(R.id.camera_rect);
    }

    void show3D() {
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_rect);
        if (preview.getChildCount() == 0) {
           // cameraPreview = new CameraPreview(this);
           // openGLView = new OpenGLView(this);
           // preview.addView(openGLView);
           // preview.addView(cameraPreview);
        }
        else {
            preview.removeAllViews();
            cameraPreview = null;
            openGLView = null;
        }
    }
}