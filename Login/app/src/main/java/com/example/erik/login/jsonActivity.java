package com.example.erik.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

public class jsonActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_json);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		Intent intent = getIntent();
		String cookie = intent.getStringExtra("COOKIE").substring(8);

		sendGPSLocationTask task = new sendGPSLocationTask();
		task.execute(cookie);

		String answer = "empty";
		Pipe pipes[];

		try {
			double coord[];

			pipes = parseJSON("[" + task.get() + "]");

			answer = "";
			for (int i = 0; i < pipes.length; i++) {
				coord = pipes[i].get_coord();

				answer += "Pipe nr: " + i + "\n";
				answer += "coord x: " + coord[0] + "\n";
				answer += "coord y: " + coord[1] + "\n";
				answer += "coord z: " + coord[2] + "\n";
				answer += "length: " + pipes[i].get_length() + "\n";

				answer += "Ployline type: " + pipes[i].get_poly_type() + "\n\n";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		TextView t = (TextView) findViewById(R.id.json_content);
		t.append(answer);
	}

	private Pipe[] parseJSON(String unparsed) {
		JSONParser parser = new JSONParser();
		Pipe pipes[] = null;

		try {
			Object obj = parser.parse(unparsed);
			JSONArray array = (JSONArray) obj;

			String temp = ((JSONObject) array.get(1)).get("Tables").toString();
			System.out.println(temp);

			String data[] = temp.split("v\":\\[\\[");

			pipes = new Pipe[data.length - 1];

			for (int i = 0; i < data.length - 1; i++) {
				String item = data[i + 1];
				String gps[] = item.split(",");

				double x = Double.parseDouble(gps[0]);
				double y = Double.parseDouble(gps[1]);
				double z = 0;

				double start[] = {x, y, z};

				x = Double.parseDouble(gps[3]);
				y = Double.parseDouble(gps[4]);
				z = 0;

				double end[] = {x, y, z};

				String polyline = data[i + 1].split("\"t\":\"")[1].substring(0, 3);

				pipes[i] = new Pipe(start, end, polyline);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pipes;
	}

	private class sendGPSLocationTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... details) {
			HttpURLConnection conn;

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

			try {
				String query = "{\"Tables\":{\"g_table_6\":{\"SpatialBoxes\":" +
						"[[[18.8360595703125,-33.96614226559744,18.837432861328125,-33.96500329452543],0]," +
						"[[18.837432861328125,-33.96614226559744,18.83880615234375,-33.96500329452543],0]," +
						"[[18.8360595703125,-33.96500329452543,18.837432861328125,-33.96386430820155],0]," +
						"[[18.837432861328125,-33.96500329452543,18.83880615234375,-33.96386430820155],0]]" +
						",\"IncludeFields\":[\"rowid\",\"Geometry\"]}}}";
			/*	String query = "{\"Limit\": [0,10]," +
                    "\"Tables\":{\"SewerAuxHouseConnection_Existing\":" +
                        "{\"IncludeFields\":[\"S_X_COORD\",\"S_Y_COORD\",\"Geometry\"]}}}";
			*/
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
