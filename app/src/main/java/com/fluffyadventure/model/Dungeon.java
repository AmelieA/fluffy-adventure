package com.fluffyadventure.model;

/**
 * Created by Johan on 18/02/2015.
 */
public class Dungeon extends AbstractSpawn {

    public Dungeon(){
    }

    public Dungeon( Integer spawnId, int spellReward, int healthReward, int strengthReward, double longitude, double latitude, String text, String name) {
        super(spawnId, spellReward, healthReward, strengthReward, longitude, latitude, text, name);
    }

    @Override
    public String getStandardIcon() {
        return "dunjeon_icon";
    }
}
