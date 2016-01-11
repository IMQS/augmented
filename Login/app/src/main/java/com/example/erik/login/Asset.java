package com.example.erik.login;

/**
 * Created by fritzonfire on 1/8/16.
 */
public class Asset {

    double start_coord[];
    double end_coord[];

    public Asset(double[] start_coord, double[] end_coord) {
        this.start_coord = start_coord;
        this.end_coord = end_coord;
    }

    public double[] get_start_coord() {
        return start_coord;
    }

    public double[] get_end_coord() {
        return end_coord;
    }
}
