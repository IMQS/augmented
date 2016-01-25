package com.example.erik.SensorPipes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class ARActivity extends Activity {
	private final String pref_db = "PREFERENCE";

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
		renderer = new OpenGLRenderer();
		renderer.setOrientationProvider(orient);
		view.setRenderer(renderer);
//		setContentView(view);

		setContentView(R.layout.activity_ar);

		Intent intent = getIntent();
		String data = intent.getStringExtra("DATA");

		SharedPreferences prefs = getSharedPreferences(pref_db, Context.MODE_PRIVATE);
		double lat, lng;

		lat = Double.parseDouble(prefs.getString("latitude", "-33.96525801"));
		lng = Double.parseDouble(prefs.getString("longitude", "18.83710027"));

		IMQS_Parser parser = new IMQS_Parser(data, lat, lng);
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