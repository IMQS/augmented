package com.example.erik.drawPipes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.jar.Manifest;

public class LoginActivity extends AppCompatActivity {

	private Location myGPS = null;
	private double radius = 0.2; // km

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		final Button login_btn;

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		login_btn = (Button) findViewById(R.id.login_button);
		login_btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				EditText mUN = (EditText) findViewById(R.id.edit_un);
				EditText mPASS = (EditText) findViewById(R.id.edit_pass);

				String text = "Connecting to Server.";
				Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

				SendHTTPLoginTask loginTask = new SendHTTPLoginTask();
				loginTask.execute(mUN.getText().toString(), mPASS.getText().toString());
				String answer = "empty";
				try {
					answer = loginTask.get();
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (answer.startsWith("session=")) {
					sendGPSLocationTask gpsTask = new sendGPSLocationTask();
					gpsTask.execute(answer.substring(8));

					try {
						answer = gpsTask.get();
					} catch (Exception e) {
						e.printStackTrace();
					}

					startARActivity(answer);
				} else if (answer.startsWith("error=")) {
					int code = Integer.parseInt(answer.split("=")[1]);

					switch (code) {
						case 403:
							makeAlert("Login incorrect", "Your username or password is wrong.");
							break;

						default:
							break;
					}
				}
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
				myGPS = new Location(location);
				System.out.println("************* Location Update ************");
				login_btn.setEnabled(true);
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {}

			public void onProviderEnabled(String provider) {}

			public void onProviderDisabled(String provider) {}
		};

		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 0, locationListener);
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

	private void startARActivity(String data) {
		Intent intent = new Intent(this, ARActivity.class);
		intent.putExtra("DATA", data);
		intent.putExtra("Lat", myGPS.getLatitude());
		intent.putExtra("Long", myGPS.getLongitude());
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
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private class SendHTTPLoginTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... credentials) {

			HttpURLConnection conn = null;
			try {
				conn = (HttpURLConnection) new URL("http://uat.imqs.co.za/auth2/login").openConnection();
			} catch (Exception e) {
				System.err.println("WAWAWAWAWAWA");
				e.printStackTrace();
			}

			String auth = credentials[0] + ":" + credentials[1];
			conn.setRequestProperty("Authorization", "Basic " +
					Base64.encodeToString(auth.getBytes(), Base64.URL_SAFE));

			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);

			String result = null;
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
				System.err.println("boooo :'(");
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

			System.out.println("Latitude " + myGPS.getLatitude() + " Long" + myGPS.getLongitude());

			try {
				conn = (HttpURLConnection) new URL("http://uat.imqs.co.za/db/1/generic/pull").openConnection();
				conn.setRequestMethod("POST");
			} catch (Exception e) {
				System.err.println("!!!!!!!!!!!!!!!!!!!\nCan not connect to server\n!!!!!!!!!!!!!!!");
				e.printStackTrace();
				return null;
			}

			conn.setRequestProperty("Cookie", "session=" + details[0]);
			conn.setRequestProperty("Content-Type", "text/plain");

			double yradius = radius / 110.54;
			double xradius = radius / (111.32 * Math.cos(yradius));
			try {
				String query = "{\"Tables\":{\"g_table_6\":{\"SpatialBoxes\":" +
						"[[[" + (myGPS.getLongitude() - xradius) + "," + (myGPS.getLatitude() - yradius) +
						"," + myGPS.getLongitude() + "," + myGPS.getLatitude() + "],0]," +
						"[[" + myGPS.getLongitude() + "," + (myGPS.getLatitude() - yradius) +
						"," + (myGPS.getLongitude() + xradius) + "," + myGPS.getLatitude() + "],0]," +
						"[[" + (myGPS.getLongitude() - xradius) + "," + myGPS.getLatitude() + "," +
						myGPS.getLongitude() + "," + (myGPS.getLatitude() + yradius) + "],0]," +
						"[[" + myGPS.getLongitude() + "," + myGPS.getLatitude() + "," +
						(myGPS.getLongitude() + xradius) + "," + (myGPS.getLatitude() + yradius) + "],0]]" +
						",\"IncludeFields\":[\"rowid\",\"Geometry\"]}}}";

				byte[] queryInBytes = query.getBytes("UTF-8");
				OutputStream os = conn.getOutputStream();
				os.write(queryInBytes);
				os.close();
			} catch (Exception e) {
				System.err.println("!!!!!!!!!!!!!!!!!!!!\nConnection outputStream has caused an error"
						+ "\n!!!!!!!!!!!!!!!!!!!!!!");
				e.printStackTrace();
				return null;
			}

			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);

			String result = null;
			try {
				conn.connect();

				System.out.println("Received Response");
				if (conn.getResponseCode() == 200) {
					InputStream in = new BufferedInputStream(conn.getInputStream());

					BufferedReader reader = new BufferedReader(new InputStreamReader(in));

					String line;
					while ((line = reader.readLine()) != null) {
						result += line;
					}
					System.out.println("Finished storing json in sting.");
				} else {
					result = "error=" + conn.getResponseCode();
				}

			} catch (Exception e) {
				System.err.println("!!!!!!!!!!!!!!!!!!\nCan not POST data\n!!!!!!!!!!!!!!!!!");
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
