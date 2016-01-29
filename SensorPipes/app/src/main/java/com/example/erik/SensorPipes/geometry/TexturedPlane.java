package com.example.erik.SensorPipes.geometry;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by erik on 2016/01/25.
 */
public class TexturedPlane extends Mesh {
	public int textureId = -1;
	private FloatBuffer mTextureBuffer;
	private Bitmap tex_bmp;

	// Indicates if we need to load the texture.
	private boolean mShouldLoadTexture = false;

	private boolean cull_enabled = false;

	public TexturedPlane(float width, float height, int textureId) {
		this.textureId = textureId;
		float vertices[] = {
				-0.5f * width,  0.5f*height, 0.0f,  // 0, Top Left
				-0.5f*width, -0.5f*height, 0.0f,  // 1, Bottom Left
				0.5f*width, -0.5f*height, 0.0f,  // 2, Bottom Right
				0.5f*width,  0.5f*height, 0.0f,  // 3, Top Right
		};

		short[] indices = {0, 1, 2, 0, 2, 3 };

		float textureCoordinates[] = { 0.0f, 0.0f, //
				0.0f, 1.0f, //
				1.0f, 1.0f, //
				1.0f, 0.0f, //
		};

		// by defualt, disable face culling for planes, otherwise they disappear when rotated
		setCullEnabled(false);

		setIndices(indices);
		setVertices(vertices);
		setTextureCoordinates(textureCoordinates);
	}

	public void loadBitmap(Bitmap bitmap) {
		this.tex_bmp = bitmap;
		mShouldLoadTexture = true;
	}

	protected void setTextureCoordinates(float[] textureCoords) {
		// float is 4 bytes, therefore we multiply the number if
		// vertices with 4.
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(
				textureCoords.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		mTextureBuffer = byteBuf.asFloatBuffer();
		mTextureBuffer.put(textureCoords);
		mTextureBuffer.position(0);
	}

	private void loadGLTexture(GL10 gl) {
		// Generate one texture pointer...
		int[] textures = new int[1];
		gl.glGenTextures(1, textures, 0);
		textureId = textures[0];

		// ...and bind it to our array
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);

		// Create Nearest Filtered Texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);

		// Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
				GL10.GL_CLAMP_TO_EDGE);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
				GL10.GL_REPEAT);

		// Use the Android GLUtils to specify a two-dimensional texture image
		// from our bitmap
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, tex_bmp, 0);
	}


	@Override
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
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, super.getVerticesBuffer());


		if (mShouldLoadTexture) {
			loadGLTexture(gl);
			mShouldLoadTexture = false;
		}
		if (textureId != -1 && mTextureBuffer != null) {
			gl.glEnable(GL10.GL_TEXTURE_2D);
			// Enable the texture state
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

			// Point to our buffers
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		}
/*
		// preserve the gl matrix
		gl.glPushMatrix();
		// do rotation + translation
		gl.glTranslatef(tx, ty, tz);
		gl.glRotatef(rx, 1, 0, 0);
		gl.glRotatef(ry, 0, 1, 0);
		gl.glRotatef(rz, 0, 0, 1);
*/
		gl.glDrawElements(GL10.GL_TRIANGLES, getNumOfIndices(),
				GL10.GL_UNSIGNED_SHORT, getIndicesBuffer());

		if (textureId != -1 && mTextureBuffer != null) {
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glDisable(GL10.GL_TEXTURE_2D);
		}

		// restore the gl matrix after drawing and translation
		//gl.glPopMatrix();

		// Disable the vertices buffer.
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		// Disable face culling.
		gl.glDisable(GL10.GL_CULL_FACE);

	}

	public void setTextureId(int textureId) {
		this.textureId = textureId;
	}
}
