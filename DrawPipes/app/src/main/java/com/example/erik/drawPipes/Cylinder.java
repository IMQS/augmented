package com.example.erik.drawPipes;

import com.example.erik.drawPipes.Mesh;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by erik on 2016/01/08.
 */
public class Cylinder extends Mesh {

        public Cylinder(float length, float radius, int segments) {
            double theta = 2 * Math.PI / segments;
            double local_theta = 0;

            float[] vertices = new float[(2+2*segments)*3];

            // center vertices of the two circles:
            vertices[0] = -length/2;    // x
            vertices[1] = 0;            // y
            vertices[2] = 0;            // z

            vertices[segments * 3 + 3] = length/2;    // x
            vertices[segments * 3 + 4] = 0;            // y
            vertices[segments * 3 + 5] = 0;            // z

            System.out.println(" >> length: " + length);
            for (int i=0; i<segments; i++) {
                // "front" circle
                vertices[3 + 3*i + 0] = -length/2;
                vertices[3 + 3*i + 1] = (float) (radius * Math.cos(local_theta));
                vertices[3 + 3*i + 2] = (float) (radius * Math.sin(local_theta));

                // "end" circle
                vertices[6 + 3*segments + 3*i + 0] = length/2;
                vertices[6 + 3*segments + 3*i + 1] = (float) (radius * Math.cos(local_theta));
                vertices[6 + 3*segments + 3*i + 2] = (float) (radius * Math.sin(local_theta));

                local_theta += theta;
            }

            short indices[] = new short[segments* 4 * 3];

            short i, j;
            for (i = 0, j = 0; i < segments - 1; i++) {
                indices[j++] = (short) 0;			// Create Triangle on start
                indices[j++] = (short) (i + 1);	// of pipe.
                indices[j++] = (short) (i + 2);

                indices[j++] = (short) (segments + 1);		// Create Triangel on end
                indices[j++] = (short) (segments + i + 2);	// of pipe.
                indices[j++] = (short) (segments + i + 3);

                indices[j++] = (short) (i + 1);		// Create Triangle between
                indices[j++] = (short) (i + 2);		// the top two points of the
                indices[j++] = (short) (i + segments + 2);	// start triangle and one top
                // point of the end triangle

                indices[j++] = (short) (i + 2);		// Create Triangle between
                indices[j++] = (short) (i + segments + 2);	// one top point on the start
                indices[j++] = (short) (i + segments + 3);	// triangle and two top points
                // on the end triangle
            }

            indices[j++] = 0;		// Create Triangle on start of pipe using the last
            indices[j++] = (short) segments;	// vertice on the start of the pipe and the second
            indices[j++] = 1;		// vertice.

            indices[j++] = (short) (segments + 1);		// Create a Triangle on the end of
            indices[j++] = (short) (segments + i + 2);	// the pipe using the last vertice
            indices[j++] = (short) (segments + 2);		// and the second vertice on the end
            // of the pipe

            indices[j++] = (short) (i + 1);		// Create a triangle between the top
            indices[j++] = 1;						// two points of the start triangle
            indices[j++] = (short) (segments + i + 2);	// and one top point on the end
            // triangle

            indices[j++] = 1;						// Create a Trinagle between one top
            indices[j++] = (short) (segments + i + 2);	// point on the end triangle and two
            indices[j] = (short) (segments + 2);		// top points on the start

//            System.out.println("****************************************************");
//            System.out.println("  |v| : " + vertices.length);
//            System.out.println("  |i| : " + indices.length);
//            System.out.println();
//            System.out.println("  vertices: " + Arrays.toString(vertices));
//            for (int c = 0; c < vertices.length;) {
//                System.out.println("(" + vertices[c++] + "," + vertices[c++] + "," + vertices[c++] + ")");
//            }
//            for (int c = 0; c < indices.length;) {
//                System.out.println("(" + indices[c++] + "," + indices[c++] + "," + indices[c++] + ")");
//            }
//            System.out.println("****************************************************");

            setIndices(indices);
            setVertices(vertices);
        }

}
