package com.example.erik.drawPipes;

/**
 * Created by erik on 2016/01/07.
 */

import android.opengl.GLSurfaceView;


        import javax.microedition.khronos.egl.EGLConfig;
        import javax.microedition.khronos.opengles.GL10;

        import android.opengl.GLU;
        import android.opengl.GLSurfaceView.Renderer;

import com.example.erik.drawPipes.FlatColoredSquare;

public class OpenGLRenderer implements GLSurfaceView.Renderer {
    /*
     * (non-Javadoc)
     *
     * @see
     * android.opengl.GLSurfaceView.Renderer#onSurfaceCreated(javax.
         * microedition.khronos.opengles.GL10, javax.microedition.khronos.
         * egl.EGLConfig)
     */

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];


    FlatColoredSquare flatSquare = new FlatColoredSquare();
    SmoothColoredSquare smoothSquare = new SmoothColoredSquare();

    Cube cube;
    Plane plane;

    Cube cube_a, cube_b, cube_c, cube_d;
    Group world;

    Cylinder pipe;

    Group g = new Group();

    float angle = 0f;

    float lookout = -2f;
    float look_speed = 0.1f;

    public OpenGLRenderer() {
        cube = new Cube();
        float[] cube_colour = {
                1, 0, 0, 1,
                0, 1, 0, 1,
                0, 0, 1, 1,
                1, 1, 0, 1,
                1, 0, 1, 1,
                0, 1, 1, 1,
                1, 1, 1, 1,
                0.5f, 0.5f, 0.5f, 1,
        };
        //cube.setColors(cube_colour);
        cube.setColor(0.5f, 0f, 1.0f, 1.0f);
        cube.translate(0, 1.5f, 0);

        plane = new Plane(2,2);
        plane.setColor(1f, 1f, 1f, 1f);
//        plane.translate(0f, -2f, 0f);


        /**************************************************/
        cube_a = new Cube(1f, 1f, 1f);
        cube_a.setColor(1f, 0f, 0f, 1f);
        cube_a.translate(-2f, -2f, 0);

        cube_b = new Cube(1f, 1f, 1f);
        cube_b.setColor(0f, 1f, 0f, 1f);
        cube_b.translate(2f, -2f, 0);

        cube_c = new Cube(1f, 1f, 1f);
        cube_c.setColor(0f, 0f, 1f, 1f);
        cube_c.translate(-2f, 2f, 0);

        cube_d = new Cube(1f, 1f, 1f);
        cube_d.setColor(0f, 1f, 1f, 1f);
        cube_d.translate(2f, 2f, 0);

        world = new Group();
        world.add(cube_a);
        world.add(cube_b);
        world.add(cube_c);
        world.add(cube_d);

        pipe = new Cylinder(4f, 1f, 16);
//        pipe.setColor(0f, 0.6f, 0.12f, 1f);
        pipe.setColor(Hex2float.parseHex("9d2fb4ff"));
        pipe.setCullEnabled(false);
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the background color to black ( rgba ).
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
        // Enable Smooth Shading, default not really needed.
        gl.glShadeModel(GL10.GL_SMOOTH);
        // Depth buffer setup.
        gl.glClearDepthf(1.0f);
        // Enables depth testing.
        gl.glEnable(GL10.GL_DEPTH_TEST);
        // The type of depth testing to do.
        gl.glDepthFunc(GL10.GL_LEQUAL);
        // Really nice perspective calculations.
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                GL10.GL_NICEST);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.opengl.GLSurfaceView.Renderer#onDrawFrame(javax.
         * microedition.khronos.opengles.GL10)
     */
    public void onDrawFrame(GL10 gl) {
        // Clears the screen and depth buffer.
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl.glLoadIdentity();
        // Translates 10 units into the screen.
        gl.glTranslatef(0,0, -800f);

        g.draw(gl);
        plane.draw(gl);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.opengl.GLSurfaceView.Renderer#onSurfaceChanged(javax.
         * microedition.khronos.opengles.GL10, int, int)
     */
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (height == 0)
            height = 1; // To prevent divide by zero
        float aspect = (float) width / height;

        // Set the viewport (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);

        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL10.GL_PROJECTION); // Select projection matrix
        gl.glLoadIdentity(); // Reset projection matrix
        // Use perspective projection
        GLU.gluPerspective(gl, 45, aspect, 0.1f, 10000.f);

        gl.glMatrixMode(GL10.GL_MODELVIEW); // Select model-view matrix
        gl.glLoadIdentity(); // Reset
    }

    public void setPipes(Pipe[] pipes) {
        Cylinder c;
        Pipe p;
        for (int i=0; i<pipes.length; i++) {
            p = pipes[i];
            c = p.get_Cylinder();
            c.setColor(Hex2float.parseHex(ColourUtil.getNextColour()));
            g.add(c);
        }
    }
}