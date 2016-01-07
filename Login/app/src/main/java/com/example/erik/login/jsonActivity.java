package com.example.erik.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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

        try {
            answer = task.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView t = (TextView)findViewById(R.id.json_content);
        t.append(answer);
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
            conn.setRequestProperty("Content-Type", "application/json");

            try {
                String query = "{\"Tables\":{\"WorkOrderType\":{\"IncludeFields\":[\"rowid\",\"Code\",\"Description\"]}}}";
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

                if (conn.getResponseCode() == 200) {
                    result = conn.getHeaderField(0);
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
