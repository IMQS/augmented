package com.example.erik.SensorPipes.utilities;

import android.util.Log;

import java.nio.ByteBuffer;

/**
 * Created by erik on 2016/01/20.
 */
public class GLObjectPicker {
	public static float[] int_to_colour(int i) {
		// since colours are 24-bit, our id must fit inside 24 bits
		if (i > 0xFFFFFF) {
			Log.e("GLObjectPicker", "id may not exceed 24 bits. Supplied id " + i + " is too large");
			i = 0xFFFFFF;
		}

		byte[] bytes = ByteBuffer.allocate(4).putInt(i).array();

		float[] c = new float[4];
		c[0] = (float)bytes[1] / 255.0f;
		c[1] = (float)bytes[2] / 255.0f;
		c[2] = (float)bytes[3] / 255.0f;
		c[3] = 1f;

		System.out.println(c[0] + "  " + c[1] + "  " + c[2]);

		System.out.println("==============");
		return c;
	}

	public static int colour_to_int(float[] colour) {
		if (colour.length != 4) {
			Log.e("GLObjectPicker", "Colour array must have four entries (rgba)");
			return 0;
		}

		//byte[] bytes = new ByteBuffer.allocate(4);
		byte[] bytes = new byte[4];

		bytes[0] = 0; // msb is 0, as colours are 24-bit
		bytes[1] = (byte) (255.0f * colour[0]);
		bytes[2] = (byte) (255.0f * colour[1]);
		bytes[3] = (byte) (255.0f * colour[2]);

		// Since java has never heard of unsigned bytes, we have to do this
		// magical conversion to create an int from the byte[]. If only this
		// were C..
		int result = 0;
		try {
			result = ((ByteBuffer) ByteBuffer.allocate(4).put(bytes).flip()).getInt();
		} catch (Exception e) {
			System.err.println("byte buffer overflow:\n"+e);
		}

		return result;
	}

	public static int colour_to_int(byte[] colour) {
		if (colour.length != 4) {
			Log.e("GLObjectPicker", "Colour array must have four entries (rgba)");
			return 0;
		}

		//byte[] bytes = new ByteBuffer.allocate(4);
		byte[] bytes = new byte[4];

		bytes[0] = 0; // msb is 0, as colours are 24-bit
		bytes[1] = colour[0];
		bytes[2] = colour[1];
		bytes[3] = colour[2];

		// Since java has never heard of unsigned bytes, we have to do this
		// magical conversion to create an int from the byte[]. If only this
		// were C..
		int result = 0;
		try {
			result = ((ByteBuffer) ByteBuffer.allocate(4).put(bytes).flip()).getInt();
		} catch (Exception e) {
			System.err.println("byte buffer overflow:\n"+e);
		}

		return result;
	}
}
