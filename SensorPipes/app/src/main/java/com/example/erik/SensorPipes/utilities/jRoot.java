package com.example.erik.SensorPipes.utilities;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by fritzonfire on 1/20/16.
 */
public class jRoot {
	HashMap<String, jTabel> Tables;
}

class jTabel {
	ArrayList<jField> Fields;
	ArrayList<ArrayList<Object>> Records;
}

class jField {
	String Name;
	String Type;
	boolean Unique;
}