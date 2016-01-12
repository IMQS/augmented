package com.example.erik.login;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by erik on 2016/01/08.
 */
public class Mesh {
    // Our vertex buffer.
    private FloatBuffer verticesBuffer = null;

    // Our index buffer.
    private ShortBuffer indicesBuffer = null;

    // The number of indices.
    private int numOfIndices = -1;

    // Flat Color
    private float[] rgba = new float[]{1.0f, 1.0f, 1.0f, 1.0f};

    // Smooth Colors
    private FloatBuffer colorBuffer = null;

    // Rotation and translation variables
    private float tx = 0;
    private float ty = 0;
    private float tz = 0;

    private float rx = 0;
    private float ry = 0;
    private float rz = 0;

    private boolean cull_enabled = true;

    public void draw(GL10 gl) {
        // Counter-clockwise winding.
        gl.glFrontFace(GL10.GL_CCW);
        // Enable face culling.
        if (cull_enabled) {
            gl.glEnable(GL10.GL_CULL_FACE);
        }
        // What faces to remove with the face culling.
        gl.glCullFace(GL10.GL_BACK);
        // Enabled the vertices buffer for writing and to be used during
        // rendering.
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        // Specifies the location and data format of an array of vertex
        // coordinates to use when rendering.
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticesBuffer);
        // Set flat color
        gl.glColor4f(rgba[0], rgba[1], rgba[2], rgba[3]);
        // Smooth color
        if ( colorBuffer != null ) {
            // Enable the color array buffer to be used during rendering.
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
            // Point out the where the color buffer is.
            gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
        }

        // preserve the gl matrix
        gl.glPushMatrix();
        // do rotation + translation
        gl.glTranslatef(tx, ty, tz);
        gl.glRotatef(rx, 1, 0, 0);
        gl.glRotatef(ry, 0, 1, 0);
        gl.glRotatef(rz, 0, 0, 1);

        gl.glDrawElements(GL10.GL_TRIANGLES, numOfIndices,
                GL10.GL_UNSIGNED_SHORT, indicesBuffer);

        // restore the gl matrix after drawing and translation
        gl.glPopMatrix();

        // Disable the vertices buffer.
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        // Disable face culling.
        gl.glDisable(GL10.GL_CULL_FACE);

    }

    protected void setVertices(float[] vertices) {
        // a float is 4 bytes, therefore we multiply the number if
        // vertices with 4.
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        verticesBuffer = vbb.asFloatBuffer();
        verticesBuffer.put(vertices);
        verticesBuffer.position(0);
    }

    protected void setIndices(short[] indices) {
        // short is 2 bytes, therefore we multiply the number if
        // vertices with 2.
        ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        indicesBuffer = ibb.asShortBuffer();
        indicesBuffer.put(indices);
        indicesBuffer.position(0);
        numOfIndices = indices.length;
    }

    protected void setColor(float red, float green, float blue, float alpha) {
//        // Setting the flat color.
//        float[] flat_colour = new float[numOfIndices*4];
//        for (int i=0; i< numOfIndices*4; ) {
//            flat_colour[i++] = red;
//            flat_colour[i++] = green;
//            flat_colour[i++] = blue;
//            flat_colour[i++] = alpha;
//        }
//
//        System.out.println("Plane has colour array size " + flat_colour.length);
//
//        ByteBuffer cbb = ByteBuffer.allocateDirect(rgba.length * 4);
//        cbb.order(ByteOrder.nativeOrder());
//        colorBuffer = cbb.asFloatBuffer();
//        colorBuffer.put(rgba);
//        colorBuffer.position(0);

        colorBuffer = null;
        rgba[0] = red;
        rgba[1] = green;
        rgba[2] = blue;
        rgba[3] = alpha;
    }
    protected void setColor(float[] colour) {
        rgba[0] = colour[0];
        rgba[1] = colour[1];
        rgba[2] = colour[2];
        rgba[3] = colour[3];
    }

    protected void setColors(float[] colors) {
        // float has 4 bytes.
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        colorBuffer = cbb.asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);
    }

    protected void translate(float x, float y, float z) {
        tx += x;
        ty += y;
        tz += z;
    }

    protected void rotate(float x, float y, float z) {
        rx += x;
        ry += y;
        rz += z;
    }

    protected void set_translation(float x, float y, float z) {
        tx = x;
        ty = y;
        tz = z;
    }

    protected void set_rotation(float x, float y, float z) {
        rx = x;
        ry = y;
        rz = z;
    }

    protected void setCullEnabled(boolean enabled) {
        cull_enabled = enabled;
    }
}
