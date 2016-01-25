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

		Button save = (Button) findViewById(R.id.save);
		save.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
//				Button login = (Button) findViewById(R.id.login_button);
				SharedPreferences prefs = getSharedPreferences(pref_db, Context.MODE_PRIVATE);
				Switch spoof = (Switch) findViewById(R.id.gps_spoof);

				if (spoof.isChecked()) {
					EditText latitude = (EditText) findViewById(R.id.latitude);
					EditText longitude = (EditText) findViewById(R.id.longitude);

					prefs.edit().putString("latitude", latitude.getText().toString()).commit();
					prefs.edit().putString("longitude", longitude.getText().toString()).commit();
					prefs.edit().putBoolean("spoof_gps", true).commit();
//					login.setEnabled(true);
				} else {
					prefs.edit().putBoolean("spoof_gps", false).commit();
//					login.setEnabled(false);
				}
				finish();
			}
		});

	}
}