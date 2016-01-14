package com.example.erik.drawPipes;

import android.app.Activity;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class ARActivity extends Activity {
	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); // (NEW)
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // (NEW)
		GLSurfaceView view = new GLSurfaceView(this);
		OpenGLRenderer renderer = new OpenGLRenderer();
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