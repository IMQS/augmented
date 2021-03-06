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
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Objects;

/**
 * Created by fritzonfire on 1/13/16.
 */
public class IMQS_Parser {

	HashMap<Integer, Asset> assetList;

	public IMQS_Parser(String data, double myLat, double myLong) {
		parseJSON(data, myLat, myLong);
	}

	public HashMap<Integer, Asset> get_assets() {
		return this.assetList;
	}

	private void parseJSON(String unparsed, double myLat, double myLong) {
		// id used for selecting and highlighting assets. Has to start at 1, because 0 := no asset selected
		int id = 1;
		this.assetList = new HashMap<Integer, Asset>();

		try {
			System.out.println("unparsed: " + unparsed);

			Gson gson = new GsonBuilder().create();
			jRoot root = gson.fromJson(unparsed, jRoot.class);

			ArrayList<jField> fields = root.Tables.get("g_table_6").Fields;
			ArrayList<ArrayList<Object>> records = root.Tables.get("g_table_6").Records;
			for (int i = 0; i < records.size(); i++) {
				Pipe pipe = new Pipe();
				for (int j = 0; j < fields.size(); j++) {
					switch (fields.get(j).Type) {
						case "rowid":
							pipe.set_rowid((int) records.get(i).get(j));
							break;

						case "FEATURE_ID":
							pipe.set_feature_id((String) records.get(i).get(j));
							break;

						case "FEAT_TYPE":
							pipe.set_feat_type((int) records.get(i).get(j));
							break;

						case "TYPE_DESCR":
							pipe.set_type_descr((String) records.get(i).get(j));
							break;

						case "NETWORK_ID":
							pipe.set_network_id((int) records.get(i).get(j));
							break;

						case "polyline":
							ArrayList<Double> gps = ((ArrayList<ArrayList<Double>>) ((LinkedTreeMap<String, Object>) records.get(i).get(j)).get("v")).get(0);

							for (int k = 0; k < gps.size() - 3; ) {
								Pipe segment = (Pipe) pipe.clone();
								double x = gps.get(k);
								double y = gps.get(k + 1);
								double z = gps.get(k + 2);
								double start[] = {x, y, z};

								k += 3;

								x = gps.get(k);
								y = gps.get(k + 1);
								z = gps.get(k + 2);
								double end[] = {x, y, z};

								segment.setId(id);
								segment.set_my_location(new double[]{myLong, myLat});
								segment.set_start_end_coords(start, end);
								this.assetList.put(id++, segment);
							}
							break;

						default:
							break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
