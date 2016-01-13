package com.example.erik.drawPipes;


/**
 * Created by erik on 2016/01/08.
 */
public class Plane extends Mesh {

    public Plane(float width, float height) {
        float vertices[] = {
                -0.5f*width,  0.5f*height, 0.0f,  // 0, Top Left
                -0.5f*width, -0.5f*height, 0.0f,  // 1, Bottom Left
                 0.5f*width, -0.5f*height, 0.0f,  // 2, Bottom Right
                 0.5f*width,  0.5f*height, 0.0f,  // 3, Top Right
        };

        short[] indices = { 0, 1, 2, 0, 2, 3 };

        // by defualt, disable face culling for planes, otherwise they disappear when rotated
        setCullEnabled(false);

        setIndices(indices);
        setVertices(vertices);
    }
}
