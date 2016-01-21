package com.example.erik.SensorPipes.utilities;

/**
 * Created by erik on 2016/01/13.
 */
public class ColourUtil {

	public final static String PIPE_COLOUR = "3251adff";
	public final static String PIPE_HIGLIGHT_COLOUR = "328eadff";

	private static String[] colours = {"be4248ff", "ba42beff", "426dbeff", "42b3beff", "42be49ff", "beba42ff"};
	private static int current_colour = 0;

	public static String getNextColour() {
		if (current_colour >= colours.length) {
			current_colour = 0;
		}
		return colours[current_colour++];
	}
}
