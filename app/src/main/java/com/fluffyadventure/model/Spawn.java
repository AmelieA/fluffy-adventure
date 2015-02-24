package com.fluffyadventure.model;

import android.location.Location;

/**
 * Created by Johan on 17/02/2015.
 */
public class Spawn extends AbstractSpawn {

    public Spawn(){
    }

    public Spawn( Integer spawnId, int spellReward, int healthReward, int strengthReward, double longitude, double latitude, String text, String name, Integer level) {
        super(spawnId, spellReward, healthReward, strengthReward, longitude, latitude, text, name, level);
    }

    @Override
    public String getStandardIcon() {
        return "spawn_icon";
    }
}

