package com.example.erik.SensorPipes.utilities;

import android.location.Location;

import com.example.erik.SensorPipes.geometry.Cylinder;

/**
 * Created by fritzonfire on 1/8/16.
 */
public class Pipe extends Asset {

	private Location start_location, end_location;
	private float length = -1;
	private float angle = 361;
	private Cylinder cylinder = null;

	public Pipe() {
	}

	public void set_start_end_coords(double[] start_coord, double[] end_coord) {
		this.start_location = new Location("start");
		this.start_location.setLongitude(start_coord[0]);
		this.start_location.setLatitude(start_coord[1]);

		this.end_location = new Location("end");
		this.end_location.setLongitude(end_coord[0]);
		this.end_location.setLatitude(end_coord[1]);

		double coord[] = new double[2];
		coord[0] = (start_coord[0] + end_coord[0]) / 2;
		coord[1] = (start_coord[1] + end_coord[1]) / 2;

		this.set_location(coord);
	}

	public float get_length() {
		if (this.length == -1) {
			this.length = this.start_location.distanceTo(this.end_location);
		}
		return this.length;
	}

	public float get_angle() {
		if (this.angle == 361) {
			this.angle = 90 - this.start_location.bearingTo(this.end_location);
		}
		return this.angle;
	}

	public Cylinder get_cylinder() {
		if (this.cylinder == null) {
			this.cylinder = new Cylinder(this.get_length(), 0.3f, 16);
			this.cylinder.set_rotation(0, 0, this.get_angle());

			this.cylinder.setPick_id(getId());
			
			Location xcenter = new Location(this.get_location());
			xcenter.setLatitude(this.get_my_location().getLatitude());
			float x = xcenter.distanceTo(this.get_my_location());
			if (this.get_my_location().getLongitude() > this.get_location().getLongitude()) {
				x *= -1;
			}

			Location ycenter = new Location(this.get_location());
			ycenter.setLongitude(this.get_my_location().getLongitude());
			float y = ycenter.distanceTo(this.get_my_location());
			if (this.get_my_location().getLatitude() > this.get_location().getLatitude()) {
				y *= -1;
			}

			float z = 0;

			this.cylinder.set_translation(x, y, z);
		}
		return this.cylinder;
	}
}
