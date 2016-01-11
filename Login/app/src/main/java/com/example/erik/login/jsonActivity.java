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
            double start[], end[];

            pipes = parseJSON("[" + task.get() + "]");

            answer = "";
            for (int i = 0; i < pipes.length; i++) {
                start = pipes[i].get_start_coord();
                end = pipes[i].get_end_coord();

                answer += "Pipe nr: " + i + "\n";
                answer += "Start coord x: " + start[0] + "\n";
                answer += "Start coord y: " + start[1] + "\n";
                answer += "Start coord z: " + start[2] + "\n";

                answer += "End coord x: " + end[0] + "\n";
                answer += "End coord y: " + end[1] + "\n";
                answer += "End coord z: " + end[2] + "\n";

                answer += "Ployline type: " + pipes[i].get_poly_type() + "\n\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView t = (TextView)findViewById(R.id.json_content);
        t.append(answer);
    }

    private Pipe[] parseJSON(String unparsed) {
        JSONParser parser = new JSONParser();
        Pipe pipes[] = null;

        try {
            Object obj = parser.parse(unparsed);
            JSONArray array = (JSONArray) obj;

            String temp = ((JSONObject) array.get(1)).get("Tables").toString();
            String data[] = temp.split("v\":\\[\\[");

            pipes = new Pipe[data.length - 1];

            for (int i = 0; i < data.length - 1; i++) {
                String item = data[i + 1];
                String gps[] = item.split(",");

                double x = Double.parseDouble(gps[0]);
                double y = Double.parseDouble(gps[1]);
                double z = Double.parseDouble(gps[2]);

                double start[] = {x, y, z};

                x = Double.parseDouble(gps[3]);
                y = Double.parseDouble(gps[4]);
                z = Double.parseDouble(gps[5].substring(0, gps[5].indexOf("]")));

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
                conn = (HttpURLConnection) new URL("http://uat.imqs.co.za/db/1/main/pull").openConnection();
                conn.setRequestMethod("POST");
            } catch (Exception e) {
                System.err.println("!!!!!!!!!!!!!!!!!!!\nCan not connect to server\n!!!!!!!!!!!!!!!");
                e.printStackTrace();
                return null;
            }

            conn.setRequestProperty("Cookie", "session=" + details[0]);
            conn.setRequestProperty("Content-Type", "text/plain");

            try {
                String query = "{\"Limit\": [0,10]," +
                    "\"Tables\":{\"SewerAuxHouseConnection_Existing\":" +
                        "{\"IncludeFields\":[\"S_X_COORD\",\"S_Y_COORD\",\"Geometry\"]}}}";
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
