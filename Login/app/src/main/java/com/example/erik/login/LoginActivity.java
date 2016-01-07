package com.example.erik.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Button login_btn = (Button) findViewById(R.id.login_button);
        login_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditText mUN   = (EditText)findViewById(R.id.edit_un);
                EditText mPASS   = (EditText)findViewById(R.id.edit_pass);

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
                    startJsonActivity(answer);
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

    private void startJsonActivity(String cookie) {
        Intent intent = new Intent(this, jsonActivity.class);
        intent.putExtra("COOKIE", cookie);
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
            System.out.println("===============\n" + conn.getRequestProperties().toString() + "\n--------------------");
            System.out.println("request: " + Base64.encodeToString("Intern:temp371".getBytes(), Base64.NO_WRAP));
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);

            String result = null;
            try {
                conn.setRequestMethod("POST");
                conn.connect();

                switch(conn.getResponseCode()) {
                    case 200:   // OK
                        result = conn.getHeaderField("Set-Cookie");
                        result = result.substring(0, result.indexOf(';'));
                        System.out.println(result);
                        System.out.println("YAAAAY=============================");
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
}
