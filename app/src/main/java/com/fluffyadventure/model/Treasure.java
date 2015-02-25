package com.fluffyadventure.model;

/**
 * Created by Johan on 18/02/2015.
 */
public class Treasure extends AbstractSpawn {

    public Treasure(){
    }

    public Treasure( Integer spawnId, int spellReward, int healthReward, int strengthReward, double latitude, double longitude, String text, String name,  Integer level) {
        super(spawnId, spellReward, healthReward, strengthReward, latitude, longitude, text, name, level);
    }

    @Override
    public String getStandardIcon() {
        return "treasure_icon";
    }
}
