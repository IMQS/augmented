package com.example.erik.SensorPipes.geometry;

/**
 * Created by erik on 2016/01/08.
 */

import com.example.erik.SensorPipes.utilities.ColourUtil;
import com.example.erik.SensorPipes.utilities.Hex2float;

import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

public class Group extends Mesh {
    private Vector<Mesh> children = new Vector<Mesh>();

    @Override
    public void draw(GL10 gl) {
        int size = children.size();
        for( int i = 0; i < size; i++)
            children.get(i).draw(gl);
    }

    @Override
    public void draw_for_picking(GL10 gl) {
        int size = children.size();
        for( int i = 0; i < size; i++)
            children.get(i).draw_for_picking(gl);
    }

    public void draw_higlighted(GL10 gl, int higlight_id) {
        Mesh m;
        int size = children.size();
        for (int i = 0; i < size; i++) {
            m = children.get(i);
            if (m.getPick_id() == higlight_id) {
                m.setColor(Hex2float.parseHex(ColourUtil.PIPE_HIGLIGHT_COLOUR));
            } else {
                m.setColor(Hex2float.parseHex((ColourUtil.PIPE_COLOUR)));
            }
            m.draw(gl);
        }
    }

    public void add(int location, Mesh object) {
        children.add(location, object);
    }

    public boolean add(Mesh object) {
        return children.add(object);
    }

    public void clear() {
        children.clear();
    }

    public Mesh get(int location) {
        return children.get(location);
    }

    public Mesh remove(int location) {
        return children.remove(location);
    }

    public boolean remove(Object object) {
        return children.remove(object);
    }

    public int size() {
        return children.size();
    }
}