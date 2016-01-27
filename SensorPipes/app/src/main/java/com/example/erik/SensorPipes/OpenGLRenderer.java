package com.example.erik.SensorPipes;

/**
 * Created by erik on 2016/01/07.
 */

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.GLSurfaceView;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLU;
import android.util.Log;
import android.view.Surface;

import com.example.erik.SensorPipes.geometry.Mesh;
import com.example.erik.SensorPipes.geometry.SmoothColoredSquare;
import com.example.erik.SensorPipes.geometry.Cylinder;
import com.example.erik.SensorPipes.geometry.FlatColoredSquare;
import com.example.erik.SensorPipes.geometry.Group;
import com.example.erik.SensorPipes.geometry.Plane;
import com.example.erik.SensorPipes.geometry.TexturedPlane;
import com.example.erik.SensorPipes.orientationProvider.OrientationProvider;
import com.example.erik.SensorPipes.representation.Quaternion;
import com.example.erik.SensorPipes.utilities.Asset;
import com.example.erik.SensorPipes.utilities.ColourUtil;
import com.example.erik.SensorPipes.utilities.GLObjectPicker;
import com.example.erik.SensorPipes.utilities.Hex2float;
import com.example.erik.SensorPipes.utilities.Pipe;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;

public class OpenGLRenderer implements GLSurfaceView.Renderer {

	ARActivity ar_activity;

	private SurfaceTexture surfaceTexture = null;
	Surface surface;
	private SharedPreferences prefs;

	public final int RENDER_MODE_NORMAL = 0;
	public final int RENDER_MODE_PICKING = 1;

	private int viewport_width = 0, viewport_height = 0;

	private int rendermode = RENDER_MODE_NORMAL;
	private float touch_x = 0f, touch_y = 0f;
	private int last_picked_id = 0;
	private boolean touch_dirty = false;

    OrientationProvider orient;

    FlatColoredSquare flatSquare = new FlatColoredSquare();
    SmoothColoredSquare smoothSquare = new SmoothColoredSquare();

    Plane plane;
	float angle = 0;
    Group g = new Group();

	TexturedPlane info_view;


	private static final int DEFAULT_TEXTURE_WIDTH = 500;
	private static final int DEFAULT_TEXTURE_HEIGHT = 500;

	private SurfaceTexture mSurfaceTexture;
	private Surface mSurface;

	private int mGlSurfaceTexture;
	private Canvas mSurfaceCanvas;

	private int mTextureWidth = DEFAULT_TEXTURE_WIDTH;
	private int mTextureHeight = DEFAULT_TEXTURE_HEIGHT;


    public OpenGLRenderer(ARActivity parent_activity) {
        plane = new Plane(2,2);
        plane.setColor(1f, 1f, 1f, 1f);


		//info_view.setColor(Hex2float.parseHex("0999797F"));

		ar_activity = parent_activity;
		surface = ar_activity.surface;
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the background color to black ( rgba ).
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        // Enable Smooth Shading, default not really needed.
        gl.glShadeModel(GL10.GL_SMOOTH);
        // Depth buffer setup.
        gl.glClearDepthf(1.0f);
        // Enables depth testing.
        gl.glEnable(GL10.GL_DEPTH_TEST);
        // The type of depth testing to do.
        gl.glDepthFunc(GL10.GL_LEQUAL);
        // Really nice perspective calculations
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
				GL10.GL_NICEST);

		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		info_view = new TexturedPlane(0.3f, 1f, createTexture(gl));
		info_view.setCullEnabled(false);
    }

    public void onDrawFrame(GL10 gl) {
		// Update the info panel texture from the WebView..
		synchronized ( this ) {
			mSurfaceTexture.updateTexImage();
		}

        // Clears the screen and depth buffer.
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl.glLoadIdentity();

		/**For some reason, if you first translate, THEN rotate, the camera moves along
		* the XY plane as well.
		* TODO: figure out why
		*/

	   // Get the rotation from the current orientationProvider as quaternion
		Quaternion q = orient.getQuaternion();

		//TODO:
		//use the following rotation if the app is in portrait mode
//		    gl.glRotatef((float) (2.0f * Math.acos(q.getW()) * 180.0f / Math.PI), q.getX(), q.getY(), q.getZ());

		// For landscape mode, we need to swap the X and Y axes, and invert the new X axis.
		float angle_offset = Float.parseFloat(prefs.getString("angle_offset", "0.0"));
		gl.glRotatef((float) (2.0f * Math.acos(q.getW()) * 180.0f / Math.PI) + angle_offset, -1 * q.getY(), q.getX(), q.getZ());

		//move the camera up a bit
		gl.glTranslatef(0, 0, -3);

		//draw the group of all pipes
		if (touch_dirty) {

			// Disable blending, otherwise the IDs get mixed
			gl.glDisable(GL10.GL_BLEND);
			g.draw_for_picking(gl);
			gl.glEnable(GL10.GL_BLEND);

			ByteBuffer pixelBuffer = ByteBuffer.allocateDirect(4);
			pixelBuffer.order(ByteOrder.nativeOrder());

			gl.glReadPixels((int) touch_x, (viewport_height - (int) touch_y), 1, 1, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE,
					pixelBuffer);


			/**
			 * To improve selection, look 10 pixels to all directions from the touched pixel,
			 * and check which colour / id occurs most in this region.
			 * If we are closer than that to the edge, just ignore it and look at the one pixel
			 * that was touched.
			 *
			 * This can be improved by getting the amount of pixels in a certain dp for this device,
			 * and using that, since, for example, 10px will not be the same physical distance on
			 * say a phone and tablet.
 			 */

			int touch_expansion = 10;


			byte b[] = new byte[4];
			pixelBuffer.get(b);
			last_picked_id = GLObjectPicker.colour_to_int(b);
			System.out.println("*****************************************");
			System.out.println("*     YOU TOUCHED ME!!    (づ￣ ³￣)づ   *");
			System.out.println("*          X:  " + touch_x);
			System.out.println("*          Y:  " + touch_y);
			System.out.println("*                                       *");
			System.out.println("*          ID: " + last_picked_id);
			System.out.println("*****************************************");

			// update the info panel
//			ar_activity.updateInfoDisplay(last_picked_id)
			ar_activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ar_activity.updateInfoDisplay(last_picked_id);
				}
			});
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

			// reset the dirty bit and clear the buffer, to render the actual frame
			touch_dirty = false;
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			gl.glLoadIdentity();
			gl.glRotatef((float) (2.0f * Math.acos(q.getW()) * 180.0f / Math.PI) + angle_offset, -1 * q.getY(), q.getX(), q.getZ());
			gl.glTranslatef(0, 0, -3);
		}
        g.draw_higlighted(gl, last_picked_id);
        plane.draw(gl);

		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glTranslatef(0.52f, 0f, -1.2f);

//		System.out.println("pre tex-load");
		if (ar_activity.info_texture_dirty) {
			System.out.println("texture is dirty");
			info_view.loadBitmap(ar_activity.info_texture);
			ar_activity.info_texture_dirty = false;
		}
//		System.out.println("post bitmap load");
		info_view.setCullEnabled(false);
		info_view.draw(gl);
		gl.glPopMatrix();
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
		releaseSurface();
		mGlSurfaceTexture = createTexture(gl);
		if (mGlSurfaceTexture > 0){
			//attach the texture to a surface.
			//It's a clue class for rendering an android view to gl level
			mSurfaceTexture = new SurfaceTexture(mGlSurfaceTexture);
			mSurfaceTexture.setDefaultBufferSize(mTextureWidth, mTextureHeight);
			mSurface = new Surface(mSurfaceTexture);
			ar_activity.surface = mSurface;
		}

		if (height == 0)
            height = 1; // To prevent divide by zero
        float aspect = (float) width / height;

		// set our global variable dimensions, used to calculate touch coordinates.
		viewport_height = height;
		viewport_width = width;

        // Set the viewport (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);

        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL10.GL_PROJECTION); // Select projection matrix
        gl.glLoadIdentity(); // Reset projection matrix
        // Use perspective projection
        GLU.gluPerspective(gl, 45, aspect, 0.1f, 10000.f);

        gl.glMatrixMode(GL10.GL_MODELVIEW); // Select model-view matrix
        gl.glLoadIdentity(); // Reset
    }

    public void setAssets(HashMap assets) {
        Mesh m;
		Asset a;
        for (Object o : assets.values()) {
			a = (Asset) o;
            m = a.get_mesh();
            m.setColor(Hex2float.parseHex(ColourUtil.PIPE_COLOUR));
            g.add(m);
        }
    }

	public void setOrientationProvider(OrientationProvider orientationProvider) {
		this.orient = orientationProvider;
		orient.start();
	}

	public void setRenderMode (int mode) {
		this.rendermode = mode;
	}

	public int pick(float touch_X, float touch_y) {
		this.touch_dirty = true;
		this.touch_x = touch_X;
		this.touch_y = touch_y;

		return 0; //XXX
	}

	// The following methods are used for texture creation from the WebView:
	public Canvas onDrawViewBegin(){
		mSurfaceCanvas = null;
		if (mSurface != null) {
			try {
				mSurfaceCanvas = mSurface.lockCanvas(null);
			}catch (Exception e){
				Log.e("GL", "error while rendering view to gl: " + e);
			}
		}
		return mSurfaceCanvas;
	}

	public void onDrawViewEnd(){
		if(mSurfaceCanvas != null) {
			mSurface.unlockCanvasAndPost(mSurfaceCanvas);
		}
		mSurfaceCanvas = null;
	}

	public void releaseSurface(){
		if(mSurface != null){
			mSurface.release();
		}
		if(mSurfaceTexture != null){
			mSurfaceTexture.release();
		}
		mSurface = null;
		mSurfaceTexture = null;

	}

	private int createTexture(GL10 gl) {
		int textureId;
		int[] textures = new int[1];
		gl.glGenTextures(1, textures, 0);

		textureId = textures[0];

		gl.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId);

		// Create Nearest Filtered Texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);

		// Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
				GL10.GL_CLAMP_TO_EDGE);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
				GL10.GL_REPEAT);

		return textureId;
	}

	public void setSharedPreferences(SharedPreferences prefs) {
		this.prefs = prefs;
	}
}
