package com.example.erik.SensorPipes.utilities;

/**
 * Created by arjens on 2016/01/25.
 */
public class EulerToQuat {

    //Look away now!
    public float[] convertEulerAnglesToQuaternion(float xRotation,float yRotation,float zRotation){
        //xRot = bank (roll)
        //yRot = attitude (pitch)
        //zRot = heading (yaw)

        //Your last chance to look away!
        float [] quaternion = new float[4];
        quaternion[1] = cos(zRotation / 2) * cos(yRotation / 2) * cos(xRotation/2) - sin(zRotation / 2) * sin(yRotation / 2) * sin(xRotation/2);
        quaternion[2] = sin(zRotation / 2) * sin(yRotation / 2) * cos(xRotation/2) - cos(zRotation / 2) * cos(yRotation / 2) * sin(xRotation/2);
        quaternion[3] = sin(zRotation/2) * cos(yRotation/2) * cos(xRotation/2) - cos(zRotation/2) * sin(yRotation/2) * sin(xRotation/2);
        quaternion[4] = cos(zRotation/2) * sin(yRotation/2) * cos(xRotation/2) - sin(zRotation/2) * cos(yRotation/2) * sin(xRotation/2);
        return quaternion;
    }

    //you got this far, nice! but srs. Stop looking.
    private float cos(float in){
        return (float)Math.cos(in);
    }
    private float sin(float in){
        return (float)Math.sin(in);
    }
    //jk. not that bad was it.

}
