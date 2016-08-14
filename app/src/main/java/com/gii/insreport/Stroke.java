package com.gii.insreport;

import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by Timur on 06-Jul-16.
 */
public class Stroke {
    ArrayList<Point> points = new ArrayList<>();
    ArrayList<Integer> intervals = new ArrayList<>();

    public ArrayList<Point> getPoints() {
        return points;
    }

    public ArrayList<Integer> getIntervals() {
        return intervals;
    }
}
