package com.example.erik.SensorPipes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.erik.SensorPipes.utilities.jRoot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

	private Button login_btn;

	private double radius = 0.1; // km
	private static final String TAG = "LoginActivity";
	private final String pref_db = "PREFERENCE";
	private String responce = "{ \"RecordCount\" : 0, \"SyncTime\" : 1453720300755.0, \"Tables\" : { \"g_table_6\" : { \"Fields\" : [ { \"Name\" : \"rowid\", \"Type\" : \"int64\", \"Unique\" : true }, { \"Name\" : \"FEATURE_ID\", \"Type\" : \"text\" }, { \"Name\" : \"FEAT_TYPE\", \"Type\" : \"int64\" }, { \"Name\" : \"TYPE_DESCR\", \"Type\" : \"text\" }, { \"Name\" : \"NETWORK_ID\", \"Type\" : \"int64\" }, { \"Name\" : \"Geometry\", \"Type\" : \"polyline\" } ], \"Records\" : [ [ 5, \"WL10765\", 300, \"Water Pipes (Existing)\", 1030, { \"t\" : \"pl3\", \"v\" : [ [ 18.83658341025202, -33.96569875190032, 0.0, 18.83622798955257, -33.96529628505452, 0.0 ] ] } ], [ 10, \"WL10760\", 300, \"Water Pipes (Existing)\", 1030, { \"t\" : \"pl3\", \"v\" : [ [ 18.83625927771085, -33.96489126700617, 0.0, 18.83648179228304, -33.96514318124966, 0.0, 18.83622798955257, -33.96529628505452, 0.0 ] ] } ], [ 11, \"WL10780\", 300, \"Water Pipes (Existing)\", 1030, { \"t\" : \"pl3\", \"v\" : [ [ 18.83733771434309, -33.96504893299696, 0.0, 18.83669672219771, -33.96551462855484, 0.0, 18.83658341025202, -33.96569875190032, 0.0 ] ] } ], [ 29, \"WL10825\", 300, \"Water Pipes (Existing)\", 1030, { \"t\" : \"pl3\", \"v\" : [ [ 18.83733771434309, -33.96504893299696, 0.0, 18.83759473711192, -33.96547732122450, 0.0, 18.83773986381645, -33.96551754078708, 0.0, 18.83785591860145, -33.96571116313154, 0.0, 18.83797563220302, -33.96574440711490, 0.0, 18.83804092802630, -33.96571717654539, 0.0 ] ] } ], [ 37, \"WL10775\", 300, \"Water Pipes (Existing)\", 1030, { \"t\" : \"pl3\", \"v\" : [ [ 18.83658341025202, -33.96569875190032, 0.0, 18.83638577105910, -33.96618594924053, 0.0 ] ] } ], [ 29, \"WL10825\", 300, \"Water Pipes (Existing)\", 1030, { \"t\" : \"pl3\", \"v\" : [ [ 18.83733771434309, -33.96504893299696, 0.0, 18.83759473711192, -33.96547732122450, 0.0, 18.83773986381645, -33.96551754078708, 0.0, 18.83785591860145, -33.96571116313154, 0.0, 18.83797563220302, -33.96574440711490, 0.0, 18.83804092802630, -33.96571717654539, 0.0 ] ] } ], [ 36, \"WL10880\", 300, \"Water Pipes (Existing)\", 1030, { \"t\" : \"pl3\", \"v\" : [ [ 18.83804092802630, -33.96571717654539, 0.0, 18.83856588031589, -33.96659253534913, 0.0, 18.83847301079184, -33.96672475881325, 0.0 ] ] } ], [ 10, \"WL10760\", 300, \"Water Pipes (Existing)\", 1030, { \"t\" : \"pl3\", \"v\" : [ [ 18.83625927771085, -33.96489126700617, 0.0, 18.83648179228304, -33.96514318124966, 0.0, 18.83622798955257, -33.96529628505452, 0.0 ] ] } ], [ 11, \"WL10780\", 300, \"Water Pipes (Existing)\", 1030, { \"t\" : \"pl3\", \"v\" : [ [ 18.83733771434309, -33.96504893299696, 0.0, 18.83669672219771, -33.96551462855484, 0.0, 18.83658341025202, -33.96569875190032, 0.0 ] ] } ], [ 12, \"WL10820\", 300, \"Water Pipes (Existing)\", 1030, { \"t\" : \"pl3\", \"v\" : [ [ 18.83801482039859, -33.96453522976692, 0.0, 18.83799106838846, -33.96456512947088, 0.0, 18.83733771434309, -33.96504893299696, 0.0 ] ] } ], [ 29, \"WL10825\", 300, \"Water Pipes (Existing)\", 1030, { \"t\" : \"pl3\", \"v\" : [ [ 18.83733771434309, -33.96504893299696, 0.0, 18.83759473711192, -33.96547732122450, 0.0, 18.83773986381645, -33.96551754078708, 0.0, 18.83785591860145, -33.96571116313154, 0.0, 18.83797563220302, -33.96574440711490, 0.0, 18.83804092802630, -33.96571717654539, 0.0 ] ] } ], [ 12, \"WL10820\", 300, \"Water Pipes (Existing)\", 1030, { \"t\" : \"pl3\", \"v\" : [ [ 18.83801482039859, -33.96453522976692, 0.0, 18.83799106838846, -33.96456512947088, 0.0, 18.83733771434309, -33.96504893299696, 0.0 ] ] } ], [ 13, \"WL10860\", 300, \"Water Pipes (Existing)\", 1030, { \"t\" : \"pl3\", \"v\" : [ [ 18.83801482039859, -33.96453522976692, 0.0, 18.83921412998291, -33.96445386107290, 0.0 ] ] } ], [ 15, \"WL10865\", 300, \"Water Pipes (Existing)\", 1030, { \"t\" : \"pl3\", \"v\" : [ [ 18.838148500450, -33.96313478051105, 0.0, 18.83819908401373, -33.96316234384349, 0.0, 18.83835288967891, -33.96330273467392, 0.0, 18.83847832491963, -33.96357002424922, 0.0, 18.83828303445425, -33.96419660525067, 0.0, 18.83801482039859, -33.96453522976692, 0.0 ] ] } ], [ 29, \"WL10825\", 300, \"Water Pipes (Existing)\", 1030, { \"t\" : \"pl3\", \"v\" : [ [ 18.83733771434309, -33.96504893299696, 0.0, 18.83759473711192, -33.96547732122450, 0.0, 18.83773986381645, -33.96551754078708, 0.0, 18.83785591860145, -33.96571116313154, 0.0, 18.83797563220302, -33.96574440711490, 0.0, 18.83804092802630, -33.96571717654539, 0.0 ] ] } ] ] } } }";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		final Button offline_btn;

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		login_btn = (Button) findViewById(R.id.login_button);
		login_btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				// Get Username and password.
				EditText mUN = (EditText) findViewById(R.id.edit_un);
				EditText mPASS = (EditText) findViewById(R.id.edit_pass);

				String text = "Signing in to Server";
				Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

				// Send credentials to login server
				SendHTTPLoginTask loginTask = new SendHTTPLoginTask();
				loginTask.execute(mUN.getText().toString(), mPASS.getText().toString());
				String answer = "empty";
				try {
					answer = loginTask.get();
				} catch (Exception e) {
					e.printStackTrace();
				}

				// Evaluate server response
				if (!answer.startsWith("session=")) {
					int code = Integer.parseInt(answer.split("=")[1]);

					// Switch used for future expansion.
					switch (code) {
						case 403:
							makeAlert("Login incorrect", "Your username or password is wrong.");
							break;

						default:
							makeAlert(answer, "An error with code:" + code + " has occurred.");
							break;
					}
					return;
				}

				text = "Requesting pipe data";
				Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

				// Request data from server
				sendGPSLocationTask gpsTask = new sendGPSLocationTask();
				gpsTask.execute(answer);
				try {
					answer = gpsTask.get();
				} catch (Exception e) {
					e.printStackTrace();
				}

				// Evaluate server response (Successful json response starts with an '{')
				if (answer.charAt(0) != '{') {
					int code = Integer.parseInt(answer.split("=")[1]);

					// switch used for future expansion.
					switch (code) {
						default:
							makeAlert(answer, "An error with code:" + code + " has occurred.");
							break;
					}
					return;
				}

				// Switch to camera view with opengl layer.
				startARActivity(answer);
			}
		});
		login_btn.setEnabled(false);

		if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == -1) {
			return;
		}

		// Acquire a reference to the system Location Manager
		final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location provider.
				SharedPreferences prefs = getSharedPreferences(pref_db, Context.MODE_PRIVATE);

				if (!prefs.getBoolean("spoof_gps", false)) {
					prefs.edit().putString("latitude", location.getLatitude() + "").commit();
					prefs.edit().putString("longitude", location.getLongitude() + "").commit();
//					System.out.println("************* Location Update ************");
					login_btn.setEnabled(true);
				}
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};

		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 0, locationListener);

		offline_btn = (Button) (findViewById(R.id.offline_button));
		offline_btn.setOnClickListener(new View.OnClickListener() {
										   public void onClick(View view) {
											   startARActivity(responce);
										   }
									   }
		);

	}

	private void makeAlert(String title, String msg) {
		new AlertDialog.Builder(LoginActivity.this)
				.setTitle(title)
				.setMessage(msg)
				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				})
				.setIcon(android.R.drawable.ic_dialog_alert)
				.show();
	}

	/**
	 * Starts the ARActivity activity with an extra 'DATA'
	 *
	 * @params data	unparsed json
	 */
	private void startARActivity(String data) {
		Intent intent = new Intent(this, ARActivity.class);
		intent.putExtra("DATA", data);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			Intent settings = new Intent(this, SettingsActivity.class);
			startActivity(settings);

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();

		SharedPreferences prefs = getSharedPreferences(pref_db, Context.MODE_PRIVATE);
		login_btn.setEnabled(prefs.getBoolean("spoof_gps", false));

	}

	private class SendHTTPLoginTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... credentials) {

			HttpURLConnection conn;
			SharedPreferences prefs = getSharedPreferences(pref_db, Context.MODE_PRIVATE);
			try {
				conn = (HttpURLConnection) new URL(prefs.getString("login_server", "http://uat.imqs.co.za/auth2/login")).openConnection();
			} catch (Exception e) {
				Log.v(TAG, "Can not open connection to login server: "
						+ prefs.getString("login_server", "http://uat.imqs.co.za/auth2/login"));
				e.printStackTrace();
				return "No Internet";
			}

			String auth = credentials[0] + ":" + credentials[1];
			conn.setRequestProperty("Authorization", "Basic " +
					Base64.encodeToString(auth.getBytes(), Base64.URL_SAFE));

			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);

			String result = "No Intornet :'(";
			try {
				conn.setRequestMethod("POST");
				conn.connect();

				switch (conn.getResponseCode()) {
					case 200:   // OK
						result = conn.getHeaderField("Set-Cookie");
						result = result.substring(0, result.indexOf(';'));
						break;

					default:    // Some other non-OK response
						result = "error=" + conn.getResponseCode();
						break;
				}
			} catch (Exception e) {
				Log.v(TAG, "Failed to POST login info to server");
				e.printStackTrace();
			}

			try {
				conn.disconnect();
			} catch (Exception e) {
			}

			return result;
		}
	}

	private class sendGPSLocationTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... details) {
			HttpURLConnection conn;
			double lat, lng;

			SharedPreferences prefs = getSharedPreferences(pref_db, Context.MODE_PRIVATE);
			lat = Double.parseDouble(prefs.getString("latitude", "-33.96525801"));
			lng = Double.parseDouble(prefs.getString("longitude", "18.83710027"));

			try {
				conn = (HttpURLConnection) new URL(prefs.getString("pull_server", "http://uat.imqs.co.za/db/1/generic/pull")).openConnection();
				conn.setRequestMethod("POST");
			} catch (Exception e) {
				Log.v(TAG, "Can not connect to pull server:"
						+ prefs.getString("pull_server", "http://uat.imqs.co.za/db/1/generic/pull"));
				e.printStackTrace();
				return "No Internet";
			}

			conn.setRequestProperty("Cookie", details[0]);
			conn.setRequestProperty("Content-Type", "text/plain");

			double y_radius = radius / 110.54;
			double x_radius = radius / (111.32 * Math.cos(y_radius));
			try {
				String query = "{\"Tables\":{\"g_table_6\":{\"SpatialBoxes\":" +
						"[[[" + (lng - x_radius) + "," + (lat - y_radius) +
						"," + lng + "," + lat + "],0]," +
						"[[" + lng + "," + (lat - y_radius) +
						"," + (lng + x_radius) + "," + lat + "],0]," +
						"[[" + (lng - x_radius) + "," + lat + "," +
						lng + "," + (lat + y_radius) + "],0]," +
						"[[" + lng + "," + lat + "," +
						(lng + x_radius) + "," + (lat + y_radius) + "],0]]}}}";

//				Log.v(TAG, query);

				byte[] queryInBytes = query.getBytes("UTF-8");
				OutputStream os = conn.getOutputStream();
				os.write(queryInBytes);
				os.close();
			} catch (Exception e) {
				Log.v(TAG, "Connection outputStream has caused an error");
				e.printStackTrace();
				return null;
			}

			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);

			String result = "No Intornet :'(";
			try {
				conn.connect();

				System.out.println("Received Response");
				if (conn.getResponseCode() == 200) {
					InputStream in = new BufferedInputStream(conn.getInputStream());

					BufferedReader reader = new BufferedReader(new InputStreamReader(in));

					String line;
					result = "";
					while ((line = reader.readLine()) != null) {
						result += line;
					}
//					Log.v(TAG, "Finished storing json in sting.");
				} else {
					result = "error=" + conn.getResponseCode();
				}

			} catch (Exception e) {
				Log.e(TAG, "Can not POST data");
				e.printStackTrace();
			}

			try {
				conn.disconnect();
			} catch (Exception e) {
			}

			return result;
		}
	}
}
