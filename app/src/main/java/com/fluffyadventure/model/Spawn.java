package com.fluffyadventure.model;

import android.location.Location;

/**
 * Created by Johan on 17/02/2015.
 */
public class Spawn extends AbstractSpawn {

    public Spawn(){
    }

    public Spawn(String name, String type){
        super (name, type);
    }

    public Spawn( Integer spawnId, int spellReward, int healthReward, int strengthReward, double latitude, double longitude, String text, String name, Integer level) {
        super(spawnId, spellReward, healthReward, strengthReward, latitude, longitude, text, name, level);
    }

    @Override
    public String getStandardIcon() {
        return "spawn_icon";
    }
}

