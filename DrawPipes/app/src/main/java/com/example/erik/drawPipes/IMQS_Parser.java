package com.example.erik.drawPipes;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;

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
			System.out.println(unparsed);

			Object obj = parser.parse("[" + unparsed + "]");
			JSONArray array = (JSONArray) obj;

			String temp = ((JSONObject) array.get(1)).get("Tables").toString();
			String data[] = temp.split("v\":\\[\\[");

			for (int i = 0; i < data.length - 1; i++) {
				String item = data[i + 1];
				String gps[] = item.split(",");

				int j = 0;
				do {
					double x = Double.parseDouble(gps[j]);
					double y = Double.parseDouble(gps[j + 1]);
					double z = 0;
					double start[] = {x, y, z};

					j += 3;

					x = Double.parseDouble(gps[j]);
					y = Double.parseDouble(gps[j + 1]);
					z = 0;
					double end[] = {x, y, z};

					pipeList.add(new Pipe(start, end, myLat, myLong));
				} while (!gps[j + 2].startsWith("0.0]]"));
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
