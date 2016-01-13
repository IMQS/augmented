package com.example.erik.drawPipes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class jsonActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_json);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		Intent intent = getIntent();
		String data = intent.getStringExtra("DATA");

		IMQS_Parser parser = new IMQS_Parser(data);

		try {
			double coord[];
			Pipe[] pipes = parser.get_Pipes();
			for (int i = 0; i < pipes.length; i++) {
				coord = pipes[i].get_coord();

				System.out.println("Pipe nr: " + i);
				System.out.println("coord x: " + coord[0]);
				System.out.println("coord y: " + coord[1]);
				System.out.println("coord z: " + coord[2]);
				System.out.println("length: " + pipes[i].get_length());

				System.out.println("Ployline type: " + pipes[i].get_poly_type() + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}