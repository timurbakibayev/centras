package com.gii.insreport;

import android.location.Location;

/**
 * Created by Timur_hnimdvi on 15-Dec-16.
 */

public interface LocationTracker {
    public interface LocationUpdateListener{
        public void onUpdate(Location oldLoc, long oldTime, Location newLoc, long newTime);
    }

    public void start();
    public void start(LocationUpdateListener update);

    public void stop();

    public boolean hasLocation();

    public boolean hasPossiblyStaleLocation();

    public Location getLocation();

    public Location getPossiblyStaleLocation();

}