package com.example.erik.SensorPipes.utilities;

import android.location.Location;

/**
 * Created by fritzonfire on 1/8/16.
 */
public class Asset {

	private Location location;
	private Location my_location;
	private int rowid;
	private String feature_id;
	private int feat_type;
	private String type_descr;
	private int network_id;

	public Asset() {
	}

	public void set_location(double coord[]) {
		this.location = new Location("Center");
		this.location.setLongitude(coord[0]);
		this.location.setLatitude(coord[1]);
	}

	public Location get_location() {
		return this.location;
	}

	public void set_my_location (double coord[]) {
		this.my_location = new Location("Me");
		this.my_location.setLongitude(coord[0]);
		this.my_location.setLatitude(coord[1]);
	}

	public Location get_my_location() {
		return this.my_location;
	}

	public void set_rowid(int rowid) {
		this.rowid = rowid;
	}

	public int get_rowid() {
		return this.rowid;
	}

	public void set_feature_id(String feature_id) {
		this.feature_id = feature_id;
	}

	public String get_feature_id() {
		return this.feature_id;
	}

	public void set_feat_type(int feat_type) {
		this.feat_type = feat_type;
	}

	public int get_feat_type() {
		return this.feat_type;
	}

	public void set_type_descr(String type_descr) {
		this.type_descr = type_descr;
	}

	public String get_type_descr() {
		return this.type_descr;
	}

	public void set_network_id(int network_id) {
		this.network_id = network_id;
	}

	public int get_network_id() {
		return this.network_id;
	}
}

