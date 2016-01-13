package com.example.erik.drawPipes;

/**
 * Created by fritzonfire on 1/8/16.
 */
public class Pipe extends Asset {

	private double myGPS[] = new double[] {18.83744091, -33.96500441, 0};
	private double start_coord[], end_coord[];
	private float length;
	private float angle;
	private Cylinder cylinder;

	public Pipe(double[] start_coord, double[] end_coord) {
		this.start_coord = start_coord;
		this.end_coord = end_coord;

		double coord[] = new double[3];
		coord[0] = (this.start_coord[0] + this.end_coord[0]) / 2;
		coord[1] = (this.start_coord[1] + this.end_coord[1]) / 2;
		coord[2] = (this.start_coord[2] + this.end_coord[2]) / 2;

		set_coord(coord);

		this.length = coords_to_meters(start_coord, end_coord);
		this.angle = (float) Math.toDegrees(Math.atan((start_coord[1] - end_coord[1]) / (start_coord[0] - end_coord[0])));

		this.cylinder = new Cylinder(length, 1.0f, 16);
		this.cylinder.set_rotation(0, 0, angle);

		float x = coords_to_meters(new double[]{myGPS[0], myGPS[1], 0}, new double[]{coord[0], myGPS[1], 0});
		if (myGPS[0] > coord[0]) {
			x *= -1;
		}

		float y = coords_to_meters(new double[]{myGPS[0], myGPS[1], 0}, new double[]{myGPS[0], coord[1], 0});
		if (myGPS[1] > coord[1]) {
			y *= -1;
		}

		float z = 0;

		this.cylinder.set_translation(x, y, z);
	}

	private float coords_to_meters(double start_coord[], double end_coord[]) {
		double dlong = Math.toRadians(end_coord[0] - start_coord[0]);
		double dlat = Math.toRadians(end_coord[1] - start_coord[1]);

		double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(Math.toRadians(start_coord[1])) *
				Math.cos(Math.toRadians(end_coord[1])) * Math.pow(Math.sin(dlong / 2), 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = 6367 * c * 1000;

		return (float) d;
	}

	public Cylinder get_Cylinder() {
		return cylinder;
	}

	public float get_length() {
		return length;
	}

}
