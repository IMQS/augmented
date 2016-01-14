package tcfb.samplerecording;

import android.os.Bundle;
import android.widget.FrameLayout;
import org.artoolkit.ar.base.ARActivity;
import org.artoolkit.ar.base.rendering.ARRenderer;


public class CameraActivity extends ARActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
    }

    /**
     * Provide our own Renderer.
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
}