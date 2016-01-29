package com.example.erik.SensorPipes;

/**
 * Created by erik on 2016/01/07.
 */

import android.opengl.GLES11Ext;
import android.content.SharedPreferences;
import android.opengl.GLSurfaceView;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLU;
import android.view.Surface;

import com.example.erik.SensorPipes.geometry.Mesh;
import com.example.erik.SensorPipes.geometry.Group;
import com.example.erik.SensorPipes.geometry.Plane;
import com.example.erik.SensorPipes.geometry.TexturedPlane;
import com.example.erik.SensorPipes.orientationProvider.OrientationProvider;
import com.example.erik.SensorPipes.representation.Quaternion;
import com.example.erik.SensorPipes.utilities.Asset;
import com.example.erik.SensorPipes.utilities.ColourUtil;
import com.example.erik.SensorPipes.utilities.GLObjectPicker;
import com.example.erik.SensorPipes.utilities.Hex2float;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;

public class OpenGLRenderer implements GLSurfaceView.Renderer {

	ARActivity ar_activity;

	private SharedPreferences prefs;

	private int viewport_width = 0, viewport_height = 0;

	private float touch_x = 0f, touch_y = 0f;
	private int last_picked_id = 0;
	private boolean touch_dirty = false;

	private int touch_scan_width = 25;
	private int touch_scan_height = 25;

    OrientationProvider orient;

    Plane plane;
    Group g = new Group();

	TexturedPlane info_view;

    public OpenGLRenderer(ARActivity parent_activity) {
        plane = new Plane(2,2);
        plane.setColor(1f, 1f, 1f, 1f);

		ar_activity = parent_activity;
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
        // Clears the screen and depth buffer.
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl.glLoadIdentity();

	   // Get the rotation from the current orientationProvider as quaternion
		Quaternion q = orient.getQuaternion();

		// use the following rotation if the app is in portrait mode
		// gl.glRotatef((float) (2.0f * Math.acos(q.getW()) * 180.0f / Math.PI), q.getX(), q.getY(), q.getZ());

		// For landscape mode, we need to swap the X and Y axes, and invert the new X axis.
		float angle_offset = Float.parseFloat(prefs.getString("angle_offset", "0.0"));
		gl.glRotatef((float) (2.0f * Math.acos(q.getW()) * 180.0f / Math.PI) + angle_offset, -1 * q.getY(), q.getX(), q.getZ());

		//move the camera 3 meters into the air
		gl.glTranslatef(0, 0, -3);

		//draw the group of all pipes
		if (touch_dirty) {

			// Disable blending, otherwise the IDs get mixed
			gl.glDisable(GL10.GL_BLEND);
			g.draw_for_picking(gl);
			gl.glEnable(GL10.GL_BLEND);

			/**
			 * To improve touch accuracy, look a a region around the pixel that was touched, and
			 * find the pipe with the smallest distance to the touched pixel.
			 *
			 * This can be improved by getting the amount of pixels in a certain dp for this device,
			 * and using that, since, for example, 10px will not be the same physical distance on
			 * say a phone and tablet.
 			 */
			int x = (int) touch_x;
			int y = (int) touch_y;

			last_picked_id = 0;
			// Are we further than the scan distance from the edge? If not, just look at
			// the touched pixel
			if (	(x > touch_scan_width)
				&&	(x < (viewport_width - touch_scan_width))
				&&	(y > touch_scan_height)
				&&	(y < (viewport_height - touch_scan_height))) {

				int min_dist = Integer.MAX_VALUE;
				int xx, yy, distance, id = 0;
				for (int i = -1*touch_scan_width; i < touch_scan_width; i++) {
					for (int j = -1*touch_scan_height; j < touch_scan_height; j++) {
						xx = x + i;
						yy = y + j;

						ByteBuffer pixelBuffer = ByteBuffer.allocateDirect(4);
						pixelBuffer.order(ByteOrder.nativeOrder());
						gl.glReadPixels(xx, (viewport_height - yy), 1, 1,
								GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, pixelBuffer);
						byte b[] = new byte[4];
						pixelBuffer.get(b);
						id = GLObjectPicker.colour_to_int(b);

						if (id == 0 ) {
							continue;
						}

						distance = (int) Math.sqrt( (x - xx)*(x - xx) + (y - yy)*(y - yy) );

						if (distance < min_dist) {
							last_picked_id = id;
							min_dist = distance;
						}
					}
				}
			} else {
				ByteBuffer pixelBuffer = ByteBuffer.allocateDirect(4);
				pixelBuffer.order(ByteOrder.nativeOrder());
				gl.glReadPixels((int) touch_x, (viewport_height - (int) touch_y), 1, 1,
						GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, pixelBuffer);
				byte b[] = new byte[4];
				pixelBuffer.get(b);
				last_picked_id = GLObjectPicker.colour_to_int(b);
			}

			System.out.println("*****************************************");
			System.out.println("*     YOU TOUCHED ME!!    (づ￣ ³￣)づ   *");
			System.out.println("*          X:  " + touch_x);
			System.out.println("*          Y:  " + touch_y);
			System.out.println("*                                       *");
			System.out.println("*          ID: " + last_picked_id);
			System.out.println("*****************************************");

			// update the info panel
			ar_activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ar_activity.updateInfoDisplay(last_picked_id);
				}
			});

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

		if (ar_activity.info_texture_dirty) {
				info_view.loadBitmap(ar_activity.info_texture);
				ar_activity.info_texture_dirty = false;
		}
		info_view.setCullEnabled(false);
		info_view.draw(gl);
		gl.glPopMatrix();
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
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

	public void pick (float touch_X, float touch_y) {
		this.touch_dirty = true;
		this.touch_x = touch_X;
		this.touch_y = touch_y;
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
