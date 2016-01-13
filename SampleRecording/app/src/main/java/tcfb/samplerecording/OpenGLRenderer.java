package tcfb.samplerecording;

/**
 * Created by erik on 2016/01/08.
 */

import org.artoolkit.ar.base.ARToolKit;
import org.artoolkit.ar.base.rendering.ARRenderer;
import org.artoolkit.ar.base.rendering.Cube;
import javax.microedition.khronos.opengles.GL10;

public class OpenGLRenderer extends ARRenderer {
    /*
     * (non-Javadoc)
     *
     * @see
     * android.opengl.GLSurfaceView.Renderer#onSurfaceCreated(javax.
         * microedition.khronos.opengles.GL10, javax.microedition.khronos.
         * egl.EGLConfig)
     */



    public static String TAG = "OverlayRenderer";

    private int markerID = -1;
    Square square;
    private Cube cube = new Cube(40.0f, 0.0f, 0.0f, 20.0f);
    public OpenGLRenderer() {
        square = new Square();
    }

    /**
     * Markers can be configured here.
     */
    @Override
    public boolean configureARScene() {

        markerID = ARToolKit.getInstance().addMarker("single;Data/patt.hiro;80");
        if (markerID < 0) return false;

        return true;
    }

    /**
     * Override the draw function from ARRenderer.
     */
    @Override
    public void draw(GL10 gl) {

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // Apply the ARToolKit projection matrix
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadMatrixf(ARToolKit.getInstance().getProjectionMatrix(), 0);

        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glFrontFace(GL10.GL_CW);

        // If the marker is visible, apply its transformation, and draw a cube
        if (ARToolKit.getInstance().queryMarkerVisible(markerID)) {
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            //Log.v(TAG, ARToolKit.getInstance().queryMarkerTransformation(markerID)[0] + " " + ARToolKit.getInstance().queryMarkerTransformation(markerID)[1] + " " + ARToolKit.getInstance().queryMarkerTransformation(markerID)[2] + " " + ARToolKit.getInstance().queryMarkerTransformation(markerID)[3]+ " " + ARToolKit.getInstance().queryMarkerTransformation(markerID)[4]);
            gl.glLoadMatrixf(ARToolKit.getInstance().queryMarkerTransformation(markerID), 0);
            //square.draw(gl);
            cube.draw(gl);
        }
    }
    public static float[] transpose(float[]m) {
        float[] result = new float[16];
        for (int i=0;i<4;i++) {
            for (int j = 0; j < 4; j++) {
                result[j * 4 + i] = m[i * 4 + j];
            }
        }
        return result;
    }
}