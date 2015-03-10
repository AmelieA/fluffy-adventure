package com.fluffyadventure.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Johan on 18/02/2015.
 */
public class Dungeon extends AbstractSpawn {

    private ArrayList<Monster> opponents = new ArrayList<>();

    public Dungeon(){
    }

    public Dungeon(Integer spawnId, int spellReward, int healthReward, int strengthReward, double latitude, double longitude, String text, String name, Integer level,  ArrayList<Monster> opponents) {
        super(spawnId, spellReward, healthReward, strengthReward, latitude, longitude, text, name, level);
        this.opponents = opponents;
    }
    public Dungeon(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
    }

    public ArrayList<Monster> getOpponents() {
        return opponents;
    };



    @Override
    public String getStandardIcon() {
        return "dungeon_icon";
    }
}
