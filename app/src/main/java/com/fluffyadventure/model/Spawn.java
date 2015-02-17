package com.fluffyadventure.model;

import android.location.Location;

/**
 * Created by Johan on 17/02/2015.
 */
public class Spawn {

    private String name;
    private String text;
    private int level;
    private Location location;

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public int getLevel() {
        return level;
    }

    public Location getLocation() {
        return location;
    }
}
