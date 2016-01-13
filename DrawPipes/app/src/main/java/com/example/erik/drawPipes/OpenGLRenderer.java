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
//        // Replace the current matrix with the identity matrix
//        gl.glLoadIdentity();
//        // Translates 10 units into the screen.
//        gl.glTranslatef(0, 0, -10);
//
//        // SQUARE A
//        // Save the current matrix.
//        gl.glPushMatrix();
//        // Rotate square A counter-clockwise.
//        gl.glRotatef(angle, 0, 0, 1);
//        // Draw square A.
//        square.draw(gl);
//        // Restore the last matrix.
//        gl.glPopMatrix();
//
//        // SQUARE B
//        // Save the current matrix
//        gl.glPushMatrix();
//        // Rotate square B before moving it, making it rotate around A.
//        gl.glRotatef(-angle, 0, 0, 1);
//        // Move square B.
//        gl.glTranslatef(2, 0, 0);
//        // Scale it to 50% of square A
//        gl.glScalef(.5f, .5f, .5f);
//        // Draw square B.
//        square.draw(gl);
//
//        // SQUARE C
//        // Save the current matrix
//        gl.glPushMatrix()Group;
//        // Make the rotation around B
//        gl.glRotatef(-angle, 0, 0, 1);
//        gl.glTranslatef(2, 0, 0);
//        // Scale it to 50% of square B
//        gl.glScalef(.5f, .5f, .5f);
//        // Rotate around it's own center.
//        gl.glRotatef(angle*10, 0, 0, 1);
//        // Draw square C.
//        square.draw(gl);
//
//        // Restore to the matrix as it was before C.
//        gl.glPopMatrix();
//        // Restore to the matrix as it was before B.
//        gl.glPopMatrix();
//
//        // Increse the angle.
//        angle++;


//        gl.glLoadIdentity();
//        // Translates 7 units into the screen and 1.5 units up.
//        gl.glTranslatef(0, 1.5f, -7);
//        // Draw our flat square.
//        flatSquare.draw(gl);
//
//        // Translate to end up under the flat square.
//        gl.glTranslatef(0, -3f, 0);
//        // Draw our smooth square.
//        smoothSquare.draw(gl);


        gl.glLoadIdentity();
        // Translates 10 units into the screen.
        gl.glTranslatef(0,0, -1000f);
//        gl.glRotatef((float)Math.PI/2, 0, 0f, 1f);

//        cube.setColor(1,0,0.5f,1);
//        cube.rotate(angle, 0.5f * angle, 0.25f*angle);
//        cube.rotate(1, 0.5f, 0.25f);
//        cube.draw(gl);
//
//        plane.rotate(1, 0, 0);
//        plane.draw(gl);
        //angle++;

//        lookout += look_speed;
//        if (lookout > 2f) look_speed = -0.1f;
//        else if (lookout < -2f) look_speed = 0.11f;
//
//        GLU.gluLookAt(gl, 0, 0, -20, lookout, lookout, 0, 0, 0, 1);
//        world.draw(gl);
//        cube_a.rotate(1,0,0);
//        cube_b.rotate(0,1,0);
//        cube_c.rotate(0,0,1);
//        cube_d.rotate(1,0,1);

//        pipe.rotate(0.25f, 0.125f, 1f);
//        pipe.draw(gl);

//        GLU.gluLookAt(gl,0,10,50,0,0,0,0,1,0);
//        gl.glRotatef(3*angle, 1, 0, 0);
//        gl.glRotatef(2*angle, 1, 0, 0);
//        gl.glRotatef(angle, 0, 0, 1);
//        angle += 0.05;
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
//        // Sets the current view port to the new size.
//        gl.glViewport(0, 0, width, height);
//        // Select the projection matrix
//        gl.glMatrixMode(GL10.GL_PROJECTION);
//        // Reset the projection matrix
//        gl.glLoadIdentity();
//        // Calculate the aspect ratio of the window
//        GLU.gluPerspective(gl, 45.0f,
//                (float) width / (float) height,
//                0.1f, 100.0f);
        ////////GLU.gluPerspective(gl, 60, 1, 0.1f, 1000.0f );
//        // Select the modelview matrix
//        gl.glMatrixMode(GL10.GL_MODELVIEW);
//        // Reset the modelview matrix
//        gl.glLoadIdentity();

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
            c.setCullEnabled(false);
            g.add(c);
        }
    }
}