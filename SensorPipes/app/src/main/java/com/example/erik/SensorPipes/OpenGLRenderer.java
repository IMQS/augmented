package com.example.erik.SensorPipes;

/**
 * Created by erik on 2016/01/07.
 */

import android.opengl.GLSurfaceView;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLU;
import com.example.erik.SensorPipes.geometry.SmoothColoredSquare;
import com.example.erik.SensorPipes.geometry.Cylinder;
import com.example.erik.SensorPipes.geometry.FlatColoredSquare;
import com.example.erik.SensorPipes.geometry.Group;
import com.example.erik.SensorPipes.geometry.Plane;
import com.example.erik.SensorPipes.orientationProvider.OrientationProvider;
import com.example.erik.SensorPipes.representation.Quaternion;
import com.example.erik.SensorPipes.utilities.ColourUtil;
import com.example.erik.SensorPipes.utilities.GLObjectPicker;
import com.example.erik.SensorPipes.utilities.Hex2float;
import com.example.erik.SensorPipes.utilities.Pipe;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class OpenGLRenderer implements GLSurfaceView.Renderer {

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

    public OpenGLRenderer() {
        plane = new Plane(2,2);
        plane.setColor(1f, 1f, 1f, 1f);
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the background color to black ( rgba ).
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
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
    }

    public void onDrawFrame(GL10 gl) {
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
		gl.glRotatef((float) (2.0f * Math.acos(q.getW()) * 180.0f / Math.PI), -1 * q.getY(), q.getX(), q.getZ());

		//move the camera up a bit
		gl.glTranslatef(0, 0, -3);

		//draw the group of all pipes
		if (touch_dirty) {
			g.draw_for_picking(gl);

			ByteBuffer pixelBuffer = ByteBuffer.allocateDirect(4);
			pixelBuffer.order(ByteOrder.nativeOrder());

			gl.glReadPixels((int) touch_x, (viewport_height - (int) touch_y), 1, 1, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE,
					pixelBuffer);
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

			// reset the dirty bit and clear the buffer, to render the actual frame
			touch_dirty = false;
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			gl.glLoadIdentity();
			gl.glRotatef((float) (2.0f * Math.acos(q.getW()) * 180.0f / Math.PI), -1 * q.getY(), q.getX(), q.getZ());
			gl.glTranslatef(0, 0, -3);
		}
        g.draw_higlighted(gl, last_picked_id);
        plane.draw(gl);
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

    public void setPipes(Pipe[] pipes) {
        Cylinder c;
        Pipe p;
        for (int i=0; i<pipes.length; i++) {
            p = pipes[i];
            c = p.get_cylinder();
            c.setColor(Hex2float.parseHex(ColourUtil.PIPE_COLOUR));
            g.add(c);
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
}
