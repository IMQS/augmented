package com.example.erik.SensorPipes;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import es.ava.aruco.Board;
import es.ava.aruco.BoardConfiguration;
import es.ava.aruco.BoardDetector;
import es.ava.aruco.CameraParameters;
import es.ava.aruco.Marker;
import es.ava.aruco.MarkerDetector;
import es.ava.aruco.Utils;
import es.ava.aruco.exceptions.CPException;

import java.io.IOException;
import java.util.List;
import java.util.MissingFormatArgumentException;
import java.util.Vector;

class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {


	/* Aruco things and board configuration */
	public final static int numBuffers = 10;
	public static boolean avail = false;
	/*public final static double[] cameraMatrix =  {3.5940654374921087e+02, 0., 1.6747001104532444e+02, 0.,
			3.5885540304154000e+02, 1.1343682346741998e+02, 0., 0., 1. };
	public final int[] ids = {	4, 	 161, 739,
								463, 59,  521,
								546, 987, 12 };
	public final static float markerSize = 0.034f;
	public final static double[] distortionMatrix =  { 1.0096514128783986e-01, 6.8812602979104931e-01,
			-7.4430379376755349e-03, 4.9815116529327295e-04,
			-3.8218497442838379e+00 };*/


	public final static double[] cameraMatrix = {
			1.0362012241605178e+03, 0., 6.6315238818418379e+02,
			0., 1.0362012241605178e+03, 3.5829450976451926e+02,
			0., 0., 1.
	};
	public final static double[] distortionMatrix = {
			1.6605223878769676e-01, -1.8693046379956177e-01,
			-6.1050784647111540e-03, -6.1688408552766126e-03,
			-4.3692550609131953e-01
	};

	public final static float markerSize = 0.134f;
//	public final static float markerSize = 2.f;

	public final static int height = 720, width = 1280;
	public final int[] ids = {
			963, 481, 103,
			402, 419, 406,
			374,  29, 31
	};

	private BoardConfiguration bConf;

	private CameraParameters camParams = new CameraParameters();
	private float prob;
	private Mat[] rtvecs = new Mat[2];
	public Board boardDetected;

	Mat myuv = new Mat(height + height/2, width, CvType.CV_8UC1), mrgba = new Mat();


	/* Camera thread things */
	private byte[] frameData = null;
	private boolean busyProccessing = false;
	private int tcount = 0;


	private class PoseCalculation extends AsyncTask<byte[], Void, Mat[]> {
		Mat[] vecs = new Mat[2];
		@Override protected Mat[] doInBackground(byte[]... params) {
			MarkerDetector md = new MarkerDetector();
			boardDetected = new Board();
			BoardDetector bd = new BoardDetector();
			Vector<Marker> mDetectedMarkers = new Vector<Marker>();
			Marker m = null;
			//if (tcount % 10 == 0)
			//Log.i("CALLBACK", "CALLING DETECT------------------------");
			busyProccessing = true;
			myuv.put(0, 0, params[0]);
			Imgproc.cvtColor(myuv, mrgba, Imgproc.COLOR_YUV2RGBA_NV21, 4);

			md.detect(mrgba, mDetectedMarkers, camParams, markerSize, mrgba);
			//Log.i("MARKER NUM", String.valueOf(mDetectedMarkers.size()));
			if (mDetectedMarkers.size() != 0) {
				m = mDetectedMarkers.get(0);
				//prob = bd.detect(mDetectedMarkers, bConf, boardDetected, camParams, markerSize);
				rtvecs[0] = m.Rvec; rtvecs[1] = m.Tvec;
				System.out.printf("%.3f %.3f %.3f   %.3f %.3f %.3f\n",
						m.Rvec.get(0, 0)[0], m.Rvec.get(1, 0)[0], m.Rvec.get(2, 0)[0],
						m.Tvec.get(0, 0)[0], m.Tvec.get(1, 0)[0], m.Tvec.get(2, 0)[0]);

				//Log.i("ROTATION||R", rtvecs[0].get(0,0)[0] + " " + rtvecs[0].get(1, 0)[0] + " " + rtvecs[0].get(2, 0)[0]);
				//Log.i("ROTATION||T", rtvecs[1].get(0,0)[0] + " " + rtvecs[1].get(1, 0)[0] + " " + rtvecs[1].get(2, 0)[0]);
				OpenGLRenderer.updateVecs(rtvecs[0], rtvecs[1]);

				busyProccessing = false;
			} else {
			//	Log.i("MARKER", "NO MARKER");
				rtvecs[0] = new Mat(3, 1, CvType.CV_32F);
				rtvecs[1] = new Mat(3, 1, CvType.CV_32F);
			}
			if (camera != null) {
				camera.addCallbackBuffer(params[0]);
			} else avail = false;
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected  void onPostExecute(Mat[] result) {
			rtvecs = vecs;
			busyProccessing = false;
		}
	}

	private class CVCallback implements Camera.PreviewCallback {
		public void onPreviewFrame(byte[] frame, Camera camera) {
			tcount++;
			 if (!busyProccessing) {
				new PoseCalculation().execute(frame);
			}
		}
	}

	Camera camera;
	Camera.Parameters cp;
	byte[][] cameraFrameBuffers = null;

	CameraSurfaceView(Context context) {
		super(context);
		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		SurfaceHolder holder = this.getHolder();
		holder.addCallback(this);

		camParams.cameraMatrix.put(0, 0, cameraMatrix[0], cameraMatrix[1], cameraMatrix[2],
				cameraMatrix[3], cameraMatrix[4], cameraMatrix[5],
				cameraMatrix[6], cameraMatrix[7], cameraMatrix[8]);
		camParams.distorsionMatrix.fromArray(distortionMatrix);

			/* Set up board configuration ----- Such hardcode */
			/* int[] ids = {	4, 	161, 739, 620, 943,
				 		463, 59, 521, 916, 5,
						546, 987, 12, 1001, 762}; */
		final int[][] markerIds = new int[3][3];
		int x = 3, y = 3, idx = 0;
		for (int i = 0; i < y; i++) {
			for (int j = 0; j < x; j++) {
				markerIds[i][j] = ids[idx++];
			}
		}
		bConf = new BoardConfiguration(3, 3, markerIds, 100, 20);
		boardDetected = new Board();
		rtvecs[0] = new Mat();
		rtvecs[1] = new Mat();
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
		List<Camera.Size> s = cp.getSupportedPreviewSizes();
		for (Camera.Size size : s) Log.i("SIZE", size.width + ": " + size.height);
		cp.setPreviewSize(this.width, this.height);
		camera.setParameters(cp);
		camera.startPreview();
		avail = true;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			// Open the Camera in preview mode
			this.camera = Camera.open();
			Camera.Size s = this.camera.getParameters().getPreviewSize();
			int format = this.camera.getParameters().getPreviewFormat();
			//int bpi = s.height*s.width*(ImageFormat.getBitsPerPixel(format)/8);
			int f = 1382400;

			cameraFrameBuffers = new byte[numBuffers][f];

			for (int i = 0; i < numBuffers; i++) {
				this.camera.addCallbackBuffer(cameraFrameBuffers[i]);
			}
			this.camera.setPreviewCallbackWithBuffer(new CVCallback());
			//this.camera.setPreviewCallbackWithBuffer(new CVCallback());


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
		avail = false;
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