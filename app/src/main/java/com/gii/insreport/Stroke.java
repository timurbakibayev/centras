package com.gii.insreport;

import android.graphics.PointF;

import java.util.ArrayList;

/**
 * Created by Timur on 06-Jul-16.
 */
public class Stroke {
    public String id = AnimaView.generateNewId();
    ArrayList<PointF> points = new ArrayList<>();
    ArrayList<Integer> intervals = new ArrayList<>();

    public ArrayList<PointF> getPoints() {
        return points;
    }

    public ArrayList<Integer> getIntervals() {
        return intervals;
    }
}
