package com.example.erik.SensorPipes;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.example.erik.SensorPipes.orientationProvider.ImprovedOrientationSensor1Provider;

public class OverlayView extends GLSurfaceView {
	
	public OpenGLRenderer renderer;
	SensorManager sensorManager;

	public OverlayView(Context context){
		super(context);
		setEGLContextClientVersion(1);
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		getHolder().setFormat(PixelFormat.TRANSLUCENT);
//		renderer = new OverlayRenderer();
		renderer = new OpenGLRenderer();

		sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		renderer.setOrientationProvider(new ImprovedOrientationSensor1Provider(sensorManager));

		setRenderer(renderer);
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

	}


}
