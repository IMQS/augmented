package tcfb.samplerecording;
/**
 * Created by Ben Harper.
 */
import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;

public class OpenGLView extends GLSurfaceView {

	public OpenGLRenderer renderer;

	public OpenGLView(Context context){
		super(context);
		setEGLContextClientVersion(1);
		setEGLConfigChooser(8,8,8,8,16,0);
		getHolder().setFormat(PixelFormat.TRANSLUCENT);
		renderer = new OpenGLRenderer();
		setRenderer(renderer);
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}
}
