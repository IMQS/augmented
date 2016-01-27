package com.example.erik.SensorPipes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.example.erik.SensorPipes.R;

public class SettingsActivity extends AppCompatActivity {

	private final String pref_db = "PREFERENCE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		load_defaults();

		Button save = (Button) findViewById(R.id.save);
		save.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				EditText latitude, longitude, angle_offset, login_server, pull_server;
				SharedPreferences prefs;
				SharedPreferences.Editor edit;
				Switch marker, spoof;

				prefs = getSharedPreferences(pref_db, Context.MODE_PRIVATE);
				edit = prefs.edit();

				marker = (Switch) findViewById(R.id.marker_tracker);
				edit.putBoolean("marker_tracker", marker.isChecked());

				spoof = (Switch) findViewById(R.id.gps_spoof);

				if (spoof.isChecked()) {
					latitude = (EditText) findViewById(R.id.latitude);
					longitude = (EditText) findViewById(R.id.longitude);

					edit.putString("latitude", latitude.getText().toString());
					edit.putString("longitude", longitude.getText().toString());
				}

				edit.putBoolean("spoof_gps", spoof.isChecked());

				angle_offset = (EditText) findViewById(R.id.angle_offset);
				edit.putString("angle_offset", angle_offset.getText().toString());

				login_server = (EditText) findViewById(R.id.login_server);
				edit.putString("login_server", login_server.getText().toString());

				pull_server = (EditText) findViewById(R.id.pull_server);
				edit.putString("pull_server", pull_server.getText().toString());

				edit.commit();
				finish();
			}
		});

	}

	private void load_defaults() {
		Switch marker, spoof_gps;
		EditText latitude, longitude, angle_offset, login_server, pull_server;
		SharedPreferences prefs = getSharedPreferences(pref_db, Context.MODE_PRIVATE);

		marker = (Switch) findViewById(R.id.marker_tracker);
		marker.setChecked(prefs.getBoolean("marker", true));

		spoof_gps = (Switch) findViewById(R.id.gps_spoof);
		spoof_gps.setChecked(prefs.getBoolean("spoof_gps", false));

		latitude = (EditText) findViewById(R.id.latitude);
		latitude.setText(prefs.getString("latitude", "-33.96525801"));

		longitude = (EditText) findViewById(R.id.longitude);
		longitude.setText(prefs.getString("longitude", "18.83710027"));

		angle_offset = (EditText) findViewById(R.id.angle_offset);
		angle_offset.setText(prefs.getString("angle_offset", "0.0"));

		login_server = (EditText) findViewById(R.id.login_server);
		login_server.setText(prefs.getString("login_server", "http://uat.imqs.co.za/auth2/login"));

		pull_server = (EditText) findViewById(R.id.pull_server);
		pull_server.setText(prefs.getString("pull_server", "http://uat.imqs.co.za/db/1/generic/pull"));
	}
}