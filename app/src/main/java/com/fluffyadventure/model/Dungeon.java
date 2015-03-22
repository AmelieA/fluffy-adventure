package com.fluffyadventure.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Johan on 18/02/2015.
 */
public class Dungeon extends AbstractSpawn {

    private ArrayList<Creature> opponents = new ArrayList<>();

    public Dungeon(){
    }

    public Dungeon(Integer spawnId, int spellReward, int healthReward, int strengthReward, double latitude, double longitude, String text, String name, Integer level,  ArrayList<Creature> opponents, boolean isSoloFight) {
        super(spawnId, spellReward, healthReward, strengthReward, latitude, longitude, text, name, level, isSoloFight);
        this.opponents = opponents;
    }

    public Dungeon(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
        JSONArray opponentsJSONArray = jsonObject.getJSONArray("Opponents");
        for (int i = 0; i < opponentsJSONArray.length(); i++ ){
            opponents.add(new Monster(opponentsJSONArray.getJSONObject(i)));
        }
    }



    public ArrayList<Creature> getOpponents() {
        return opponents;
    };

    public void setOpponents(ArrayList<Creature> opponents) {
        this.opponents = opponents;
    }

    @Override
    public String getStandardIcon() {
        return "dungeon_icon";
    }
}
