package com.example.erik.SensorPipes.utilities;

/**
 * Created by arjens on 2016/01/25.
 */
public class EulerToQuat {

    //Look away now!
    public static double[] convertEulerAnglesToQuaternion(double xRotation,double yRotation,double zRotation){

        //yRot = attitude (pitch)
        //zRot = heading (yaw)
        //xRot = bank (roll)
        //double yRot = yRotation * Math.PI/360;
        //double zRot = zRotation * Math.PI/360;
        //double xRot = xRotation;
        double yRot = yRotation;
        double zRot = zRotation;
        double xRot = xRotation;


        //Your last chance to look away!
        double [] quaternion = new double[4];
        quaternion[0] = cos(zRot) * cos(yRot) * cos(xRot) - sin(zRot) * sin(yRot) * sin(xRot);
        quaternion[1] = sin(zRot) * sin(yRot) * cos(xRot) + cos(zRot) * cos(yRot) * sin(xRot);
        quaternion[2] = sin(zRot) * cos(yRot) * cos(xRot) + cos(zRot) * sin(yRot) * sin(xRot);
        quaternion[3] = cos(zRot) * sin(yRot) * cos(xRot) - sin(zRot) * cos(yRot) * sin(xRot);
        return quaternion;
    }

    //You got this far, nice! But srs. Stop looking.
    private static double cos(double in){
        return (double)Math.cos(in);
    }
    private static double sin(double in){
        return (double)Math.sin(in);
    }
    //JK. Not that bad was it?
}