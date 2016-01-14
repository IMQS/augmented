package com.example.erik.drawPipes;

import android.location.Location;

/**
 * Created by fritzonfire on 1/8/16.
 */
public class Pipe extends Asset {

	private Location myGPS = new Location("myGPS");
	private double start_coord[], end_coord[];
	private float length;
	private float angle;
	private Cylinder cylinder;

	public Pipe(double[] start_coord, double[] end_coord) {
		this.myGPS.setLatitude(-33.96500441);
		this.myGPS.setLongitude(18.83744091);
		this.start_coord = start_coord;
		this.end_coord = end_coord;

		double coord[] = new double[3];
		coord[0] = (this.start_coord[0] + this.end_coord[0]) / 2;
		coord[1] = (this.start_coord[1] + this.end_coord[1]) / 2;
		coord[2] = 0;

		set_coord(coord);

		Location center = new Location("center");
		center.setLongitude(coord[0]);
		center.setLatitude(coord[1]);

		Location start = new Location("start");
		start.setLongitude(start_coord[0]);
		start.setLatitude(start_coord[1]);

		Location end = new Location("end");
		end.setLongitude(end_coord[0]);
		end.setLatitude(end_coord[1]);

		this.length = start.distanceTo(end);
		this.angle = 90 - start.bearingTo(end);

		this.cylinder = new Cylinder(length, 1.0f, 16);
		this.cylinder.set_rotation(0, 0, angle);

		Location xcenter = new Location(center);
		xcenter.setLatitude(myGPS.getLatitude());
		float x = xcenter.distanceTo(myGPS);
		if (myGPS.getLongitude() > coord[0]) {
			x *= -1;
		}

		Location ycenter = new Location(center);
		ycenter.setLongitude(myGPS.getLongitude());
		float y = ycenter.distanceTo(myGPS);
		if (myGPS.getLatitude() > coord[1]) {
			y *= -1;
		}

		float z = 0;

		this.cylinder.set_translation(x, y, z);
	}

	public Cylinder get_Cylinder() {
		return cylinder;
	}

	public float get_length() {
		return length;
	}

}
