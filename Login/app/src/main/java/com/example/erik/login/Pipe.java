package com.example.erik.login;

/**
 * Created by fritzonfire on 1/8/16.
 */
public class Pipe extends Asset {

    private String poly_type;

    public Pipe(double[] start_coord, double[] end_coord, String poly_type) {
        super(start_coord, end_coord);

        this.poly_type = poly_type;
    }

    public String get_poly_type() {
        return poly_type;
    }

}
