package com.example.erik.drawPipes;

/**
 * Created by erik on 2016/01/12.
 */
public class Hex2float {
    public static float[] parseHex(String h)
    {
        if (h.length() != 8) {
            return new float[]{1f, 0f, 1f, 1f};
        }

        float[] rgba = new float[4];
        rgba[0] = (float)(Integer.parseInt(h.substring(0,2), 16)) / 255;
        rgba[1] = (float)(Integer.parseInt(h.substring(2,4), 16)) / 255;
        rgba[2] = (float)(Integer.parseInt(h.substring(4,6), 16)) / 255;
        rgba[3] = (float)(Integer.parseInt(h.substring(6,8), 16)) / 255;

        return rgba;
    }
}
