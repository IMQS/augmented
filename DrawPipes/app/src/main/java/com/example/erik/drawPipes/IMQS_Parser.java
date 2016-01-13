package com.example.erik.drawPipes;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by fritzonfire on 1/13/16.
 */
public class IMQS_Parser {

	private Pipe[] pipes;

	public IMQS_Parser(String data) {
		parseJSON(data);
	}

	public Pipe[] get_Pipes() {
		return this.pipes;
	}

	private Pipe[] parseJSON(String unparsed) {
		JSONParser parser = new JSONParser();

		try {
			Object obj = parser.parse("[" + unparsed + "]");
			JSONArray array = (JSONArray) obj;

			String temp = ((JSONObject) array.get(1)).get("Tables").toString();
			String data[] = temp.split("v\":\\[\\[");

			this.pipes = new Pipe[data.length - 1];

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

				this.pipes[i] = new Pipe(start, end, polyline);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pipes;
	}
}
