package com.example.erik.drawPipes;

/**
 * Created by erik on 2016/01/13.
 */
public class ColourUtil {
	private static String[] colours = {"be4248ff", "ba42beff", "426dbeff", "42b3beff", "42be49ff", "beba42ff"};
	private static int current_colour = 0;

	public static String getNextColour() {
		if (current_colour >= colours.length) {
			current_colour = 0;
		}
		return colours[current_colour++];
	}
}
