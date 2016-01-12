package com.example.erik.login;

/**
 * Created by fritzonfire on 1/8/16.
 */
public class Pipe extends Asset {

	private double myGPS[] = new double[] {18.83744091, -33.96500441, 0};
	private double start_coord[], end_coord[];
	private float length;
	private float angle;
	private Cylinder cylinder;
	private String poly_type;

	public Pipe(double[] start_coord, double[] end_coord, String poly_type) {
		this.start_coord = start_coord;
		this.end_coord = end_coord;

		double coord[] = new double[3];
		coord[0] = (this.start_coord[0] + this.end_coord[0]) / 2;
		coord[1] = (this.start_coord[1] + this.end_coord[1]) / 2;
		coord[2] = (this.start_coord[2] + this.end_coord[2]) / 2;

		set_coord(coord);

		this.poly_type = poly_type;

		this.length = coords_to_meters(start_coord, end_coord);
		this.angle = (float) Math.atan((start_coord[1] - end_coord[1]) / (start_coord[0] - end_coord[0]));

		this.cylinder = new Cylinder(length, 1, 16);
		this.cylinder.set_rotation(0, 0, angle);

		float x = coords_to_meters(new double[]{myGPS[0], myGPS[1], 0}, new double[]{coord[0], myGPS[1], 0});
		float y = coords_to_meters(new double[]{myGPS[0], myGPS[1], 0}, new double[]{myGPS[0], coord[1], 0});
		float z = 0;

		this.cylinder.set_translation(x, y, z);
	}

	private float coords_to_meters(double start_coord[], double end_coord[]) {
		double phi1 = Math.toRadians(start_coord[1]);
		double phi2 = Math.toRadians(end_coord[1]);
		double delta_phi = Math.toRadians(end_coord[1] - start_coord[1]);

		double delta_long = Math.toRadians(end_coord[0] - start_coord[0]);

		double a = Math.sin(delta_phi) * Math.sin(delta_phi / 2) *
				Math.cos(phi1) * Math.cos(phi2) *
				Math.sin(delta_long / 2) * Math.sin(delta_long / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return (float) (6371000 * c * 1000000);

	}

	public String get_poly_type() {
		return poly_type;
	}

	public Cylinder get_Cylinder() {
		return cylinder;
	}

	public float get_length() {
		return length;
	}

}
