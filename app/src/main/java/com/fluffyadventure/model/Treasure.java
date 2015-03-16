package com.fluffyadventure.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Johan on 18/02/2015.
 */
public class Treasure extends AbstractSpawn {

    public Treasure(){
    }

    public Treasure( Integer spawnId, int spellReward, int healthReward, int strengthReward, double latitude, double longitude, String text, String name,  Integer level) {
        super(spawnId, spellReward, healthReward, strengthReward, latitude, longitude, text, name, level);
    }

    public Treasure(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
    }

    public ArrayList<Creature> getOpponents() {
        return new ArrayList<>();
    };

    public void setOpponents(ArrayList<Creature> opponents) {};

    @Override
    public String getStandardIcon() {
        return "treasure_icon";
    }
}
