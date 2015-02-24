package com.fluffyadventure.model;

/**
 * Created by Johan on 18/02/2015.
 */
public class Treasure extends AbstractSpawn {

    public Treasure(){
    }

    public Treasure( Integer spawnId, int spellReward, int healthReward, int strengthReward, double longitude, double latitude, String text, String name,  Integer level) {
        super(spawnId, spellReward, healthReward, strengthReward, longitude, latitude, text, name, level);
    }

    @Override
    public String getStandardIcon() {
        return "treasure_icon";
    }
}
