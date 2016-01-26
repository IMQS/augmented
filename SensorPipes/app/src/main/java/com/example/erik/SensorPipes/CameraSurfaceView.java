package com.example.erik.SensorPipes;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import es.ava.aruco.Board;
import es.ava.aruco.BoardConfiguration;
import es.ava.aruco.BoardDetector;
import es.ava.aruco.CameraParameters;
import es.ava.aruco.Marker;
import es.ava.aruco.MarkerDetector;

import java.io.IOException;
import java.util.MissingFormatArgumentException;
import java.util.Vector;

class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

	public Board boardDetected;
	private class CVCallback implements Camera.PreviewCallback {
		MarkerDetector md;
		BoardDetector bd;
		BoardConfiguration bConf;
		Vector<Marker> mDetectedMarkers;
		CameraParameters camParams;
		Mat myuv, mrgba;
		float prob;
		int height, width;

		public CVCallback() {
			bd = new BoardDetector();
			md = new MarkerDetector();
			camParams = new CameraParameters();
			double[] cameraMatrix =  {3.5940654374921087e+02, 0., 1.6747001104532444e+02, 0.,
					3.5885540304154000e+02, 1.1343682346741998e+02, 0., 0., 1. };
			double[] distortionMatrix =  { 1.0096514128783986e-01, 6.8812602979104931e-01,
					-7.4430379376755349e-03, 4.9815116529327295e-04,
					-3.8218497442838379e+00 };

			camParams.cameraMatrix.put(0, 0, 	cameraMatrix[0], cameraMatrix[1], cameraMatrix[2],
												cameraMatrix[3], cameraMatrix[4], cameraMatrix[5],
												cameraMatrix[6], cameraMatrix[7], cameraMatrix[8]);
			camParams.distorsionMatrix.fromArray(distortionMatrix);

			/* Set up board configuration ----- Such hardcode */
			/* int[] ids = {	4, 	161, 739, 620, 943,
				 		463, 59, 521, 916, 5,
						546, 987, 12, 1001, 762}; */
			int[] ids = {	4, 	161, 739,
					463, 59, 521,
					546, 987, 12 };
			int[][] markerIds = new int[3][3];
			int x = 3, y = 3, idx = 0;
			for (int i = 0; i < y; i++) {
				for (int j = 0; j < x; j++) {
					markerIds[i][j] = ids[idx++];
				}
			}
			bConf = new BoardConfiguration(3, 3, markerIds, 100, 20);
			boardDetected = new Board();
			mDetectedMarkers = new Vector<Marker>();
			markerSize = 0.034f;
			prob = 0;
			height = 240;
			width =  320;
		}

		public void onPreviewFrame(byte[] frame, Camera camera) {
			if (myuv != null) myuv.release();
			myuv = new Mat(height + height/2, width, CvType.CV_8UC1);
			myuv.put(0, 0, frame);
			mrgba = new Mat();
			Imgproc.cvtColor(myuv, mrgba, Imgproc.COLOR_YUV2RGBA_NV21, 4);


			System.out.println("CALLING DETECT-------------------");
			md.detect(mrgba, mDetectedMarkers, camParams, markerSize, mrgba);
			for (Marker marker : mDetectedMarkers) {
				System.out.print("ID: " + marker.getMarkerId() + " ");
			}
			System.out.println();
			prob = bd.detect(mDetectedMarkers, bConf, boardDetected, camParams, markerSize);
			System.out.println("PROB: " + prob);
			mrgba.release();
		}
	}

	Camera camera;
	Camera.Parameters cp;

	 /* Aruco Things */
	float markerSize;

	CameraSurfaceView(Context context) {
		super(context);

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		SurfaceHolder holder = this.getHolder();
		holder.addCallback(this);

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

		// The default orientation is landscape, so for a portrait app like this
		// one we need to rotate the view 90 degrees.
//		camera.setDisplayOrientation(90);
		
		// IMPORTANT: We must call startPreview() on the camera before we take
		// any pictures

		cp = camera.getParameters();
		cp.setPreviewSize(320, 240);
		camera.setParameters(cp);
		camera.startPreview();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			// Open the Camera in preview mode
			this.camera = Camera.open();
			this.camera.setPreviewCallback(new CVCallback());
			this.camera.setPreviewDisplay(holder);
		} catch (IOException ioe) {
			ioe.printStackTrace(System.out);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// Surface will be destroyed when replaced with a new screen
		// Always make sure to release the Camera instance
		camera.stopPreview();
		camera.setPreviewCallback(null);
		camera.release();
		camera = null;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
	}

	public void takePicture(PictureCallback imageCallback) {
		camera.takePicture(null, null, imageCallback);
	}

}