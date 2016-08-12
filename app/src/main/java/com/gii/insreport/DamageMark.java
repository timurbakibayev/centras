package com.gii.insreport;

import android.graphics.PointF;
import android.graphics.Rect;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by Timur on 20-Jul-16.
 */
public class DamageMark {
    String id = "";
    PointF relativeCoordinates = new PointF(0,0);
    String description = "";
    @JsonIgnore
    Rect toDraw = new Rect(0,0,1,1);

    public String getId() {
        return id;
    }

    public PointF getRelativeCoordinates() {
        return relativeCoordinates;
    }

    public String getDescription() {
        return description;
    }

    public void generateNewId() {
        SecureRandom random = new SecureRandom();
        id = (new BigInteger(64, random).toString(32));
    }
}
