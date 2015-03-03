package com.fluffyadventure.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Johan on 18/02/2015.
 */
public class Dungeon extends AbstractSpawn {

    public Dungeon(){
    }

    public Dungeon(Integer spawnId, int spellReward, int healthReward, int strengthReward, double latitude, double longitude, String text, String name, Integer level) {
        super(spawnId, spellReward, healthReward, strengthReward, latitude, longitude, text, name, level);
    }
    public Dungeon(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
    }



    @Override
    public String getStandardIcon() {
        return "dungeon_icon";
    }
}
