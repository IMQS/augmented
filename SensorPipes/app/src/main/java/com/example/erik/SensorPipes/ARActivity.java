package com.example.erik.SensorPipes;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.erik.SensorPipes.orientationProvider.ImprovedOrientationSensor1Provider;
import com.example.erik.SensorPipes.orientationProvider.OrientationProvider;
import com.example.erik.SensorPipes.utilities.Asset;
import com.example.erik.SensorPipes.utilities.IMQS_Parser;


import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class ARActivity extends Activity {
	private final String pref_db = "PREFERENCE";

	/**
	 * Called when the activity is first created.
	 */
	SensorManager sensorManager;
	CameraSurfaceView camera_view;
	OpenGLRenderer renderer;
	HashMap<Integer, Asset> assetList;

	WebView info_panel;
	private Picture pic = null;
	private Timer myTimer; // timer for waiting until last picture loaded


	private final int TEXTURE_WIDTH  = ( 720 );
	private final int TEXTURE_HEIGHT    = ( 720 );

	public Bitmap info_texture;
	public boolean info_texture_dirty = false;

	// Variables
	public Surface surface = null;


	// XXX XXX XXX XXX
	int file_num = 0;
	// XXX XXX XXX XXX

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		SharedPreferences prefs = getSharedPreferences(pref_db, Context.MODE_PRIVATE);

		// Set up the OrientationProvider
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		OrientationProvider orient = new ImprovedOrientationSensor1Provider(sensorManager);

		// Set up the 3D things
		GLSurfaceView view = new GLSurfaceView(this);
		view.setEGLContextClientVersion(1);
		view.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		view.getHolder().setFormat(PixelFormat.TRANSLUCENT);

		renderer = new OpenGLRenderer(this);
		renderer.setSharedPreferences(prefs);
		renderer.setOrientationProvider(orient);
		view.setRenderer(renderer);
//		setContentView(view);

		setContentView(R.layout.activity_ar);
//		info_panel = (WebView) findViewById(R.id.infoPanel);

		Intent intent = getIntent();
		String data = intent.getStringExtra("DATA");

		double lat, lng;
		lat = Double.parseDouble(prefs.getString("latitude", "-33.96525801"));
		lng = Double.parseDouble(prefs.getString("longitude", "18.83710027"));

		IMQS_Parser parser = new IMQS_Parser(data, lat, lng);
		assetList = parser.get_assets();
		renderer.setAssets(assetList);

		FrameLayout preview = (FrameLayout) findViewById(R.id.composite);

		// Set up the info panel WebView
		info_panel = new WebView(this);

//		info_panel.loadUrl("file:///android_asset/info_display/index.html");

		info_panel.loadData("<html><body>"
						+ "..."
						+ "</body></html>",
				"text/html", "utf-8");


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

	@TargetApi(Build.VERSION_CODES.KITKAT)
	public void updateInfoDisplay(int id) {
		String html;
		// 0 means no asset was selected (blank area was touched)
		if (id == 0) {
			html = "<html><head><link REL=StyleSheet HREF=\"file:///android_asset/info_display/info.css\" TYPE=\"text/css\"></head><body>"
					+ "..."
					+ "</body></html>";
		} else {
			html = "<html><head><link REL=StyleSheet HREF=\"info.css\" TYPE=\"text/css\"></head><body>"
					+ assetList.get(id).generate_html_info()
					+ "</body></html>";
		}

		System.out.println("about to create bitmap...");
		HTML2Bitmap(info_panel, 720, 720, "file:///android_asset/info_display/", html);
		System.out.println("... bitmap done");
		info_texture_dirty = true;

		// write it to a file for testing..
		File myDir = new File(Environment.getExternalStorageDirectory(), "Sample");
		if (myDir.exists())
		{
		}
		else
		{
			myDir.mkdir();
		}
		String fname = "sample_" + file_num++ + ".png";
		File file1 = new File(myDir, fname);

		if(info_texture!=null)
		{
			try
			{
				if (!file1.exists()) {
					file1.createNewFile();
				}
				FileOutputStream out = new FileOutputStream(file1);
				info_texture.compress(Bitmap.CompressFormat.PNG, 10, out);
				out.flush();
				out.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}}
	}

	/**
	 * Generates a bitmap from some HTML.
	 * From here: http://stackoverflow.com/questions/4633988/generate-bitmap-from-html-in-android
	 * @param w A WebView instance
	 * @param containerWidth width
	 * @param containerHeight height
	 * @param baseURL Base URL to render the content in
	 * @param content HTML string
	 * @return
	 */

	public void HTML2Bitmap(final WebView w, final int containerWidth, final int containerHeight, final String baseURL, final String content) {
		System.out.println("START HTML2BITMAP");

		info_panel.loadDataWithBaseURL(baseURL, content, "text/html", "UTF-8", null);
		info_panel.measure(View.MeasureSpec.makeMeasureSpec(
						View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
				View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		info_panel.layout(0, 0, info_panel.getMeasuredWidth(),
				info_panel.getMeasuredHeight());
		info_panel.setDrawingCacheEnabled(true);
		info_panel.buildDrawingCache();
		//xxx
		info_texture = Bitmap.createBitmap(info_panel.getMeasuredWidth(),
				info_panel.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

		Canvas bigcanvas = new Canvas(info_texture);
		Paint paint = new Paint();
		int iHeight = info_texture.getHeight();
		bigcanvas.drawBitmap(info_texture, 0, iHeight, paint);
		info_panel.draw(bigcanvas);


		System.out.println("END HTML2BITMAP");
	}
}