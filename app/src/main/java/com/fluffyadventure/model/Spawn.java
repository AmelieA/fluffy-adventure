package com.fluffyadventure.model;

import android.location.Location;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Johan on 17/02/2015.
 */
public class Spawn extends AbstractSpawn {

    public Spawn(){
    }

    public Spawn( Integer spawnId, int spellReward, int healthReward, int strengthReward, double latitude, double longitude, String text, String name, Integer level) {
        super(spawnId, spellReward, healthReward, strengthReward, latitude, longitude, text, name, level);
    }
    public  Spawn(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
    }

    @Override
    public String getStandardIcon() {
        return "spawn_icon";
    }
}

