package com.example.erik.SensorPipes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.erik.SensorPipes.orientationProvider.ImprovedOrientationSensor1Provider;
import com.example.erik.SensorPipes.orientationProvider.OrientationProvider;
import com.example.erik.SensorPipes.utilities.IMQS_Parser;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import java.util.Vector;

import es.ava.aruco.Board;
import es.ava.aruco.BoardConfiguration;
import es.ava.aruco.Marker;
import es.ava.aruco.android.Aruco3dActivity;
import es.ava.aruco.exceptions.ExtParamException;
import min3d.animation.AnimationObject3d;
import min3d.parser.IParser;
import min3d.parser.Parser;
import min3d.vos.Light;

public class ARActivity extends Activity {

	static {
		System.loadLibrary("opencv_java");
	}

	/**
	 * Called when the activity is first created.
	 */
	SensorManager sensorManager;
	CameraSurfaceView camera_view;
	OpenGLRenderer renderer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// Set up the OrientationProvider
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		OrientationProvider orient = new ImprovedOrientationSensor1Provider(sensorManager);

		// Set up the 3D things
		GLSurfaceView view = new GLSurfaceView(this);
		view.setEGLContextClientVersion(1);
		view.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		view.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		renderer = new OpenGLRenderer(camera_view);
		renderer.setOrientationProvider(orient);
		view.setRenderer(renderer);
//		setContentView(view);

		setContentView(R.layout.activity_ar);

		Intent intent = getIntent();
		String data = intent.getStringExtra("DATA");
		double myLat = intent.getDoubleExtra("Lat", 0.0);
		double myLong = intent.getDoubleExtra("Long", 0.0);

		IMQS_Parser parser = new IMQS_Parser(data, myLat, myLong);
		renderer.setPipes(parser.get_Pipes().clone());


		FrameLayout preview = (FrameLayout) findViewById(R.id.composite);
		if (preview == null) {
			System.out.println("composite is null!");
			System.exit(0);
		}
		if (preview.getChildCount() == 0) {
			camera_view = new CameraSurfaceView(this);
			preview.addView(view);
			preview.addView(camera_view);
		}
		else {
			preview.removeAllViews();
			camera_view = null;
			view = null;
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		// MotionEvent reports input details from the touch screen
		// and other input controls. In this case, you are only
		// interested in events where the touch position changed.

		float x = e.getX();
		float y = e.getY();

		switch (e.getAction()) {
			case MotionEvent.ACTION_UP:
				System.out.println("TOUCH!   X: " + x + "  Y: " + y);
				renderer.pick(x, y);
				break;
		}
		return true;
	}

}