package com.example.erik.SensorPipes.utilities;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.internal.LinkedTreeMap;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Created by fritzonfire on 1/13/16.
 */
public class IMQS_Parser {

	private Pipe[] pipes;

	public IMQS_Parser(String data, double myLat, double myLong) {
		parseJSON(data, myLat, myLong);
	}

	public Pipe[] get_Pipes() {
		return this.pipes;
	}

	private Pipe[] parseJSON(String unparsed, double myLat, double myLong) {
		JSONParser parser = new JSONParser();
		ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

		try {
			System.out.println("unparsed: " + unparsed.substring(4));

			Gson gson = new GsonBuilder().create();
			jRoot root = gson.fromJson(unparsed.substring(4), jRoot.class);
			
			ArrayList<ArrayList<Object>> records = root.Tables.get("g_table_6").Records;
			for (int i = 0; i < records.size(); i++) {

				ArrayList<Double> gps = ((ArrayList<ArrayList<Double>>)((LinkedTreeMap<String, Object>) records.get(i).get(5)).get("v")).get(0);

				for (int j = 0; j < gps.size() - 3;) {
					double x = gps.get(j);
					double y = gps.get(j + 1);
					double z = gps.get(j + 2);
					double start[] = {x, y, z};

					j += 3;

					x = gps.get(j);
					y = gps.get(j + 1);
					z = gps.get(j + 2);
					double end[] = {x, y, z};

					pipeList.add(new Pipe(start, end, myLat, myLong));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.pipes = new Pipe[pipeList.size()];
		for (int i = 0; i < this.pipes.length; i++) {
			this.pipes[i] = pipeList.get(i);
		}
		return this.pipes;
	}
}
