package com.example.erik.SensorPipes;

import android.app.Activity;
import android.content.Intent;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.erik.SensorPipes.orientationProvider.ImprovedOrientationSensor1Provider;
import com.example.erik.SensorPipes.orientationProvider.OrientationProvider;
import com.example.erik.SensorPipes.utilities.IMQS_Parser;

public class ARActivity extends Activity {
	/**
	 * Called when the activity is first created.
	 */
	SensorManager sensorManager;
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
		OpenGLRenderer renderer = new OpenGLRenderer();
		renderer.setOrientationProvider(orient);
		view.setRenderer(renderer);
		setContentView(view);

		Intent intent = getIntent();
		String data = intent.getStringExtra("DATA");
		double myLat = intent.getDoubleExtra("Lat", 0.0);
		double myLong = intent.getDoubleExtra("Long", 0.0);

		IMQS_Parser parser = new IMQS_Parser(data, myLat, myLong);
		renderer.setPipes(parser.get_Pipes().clone());
	}
}