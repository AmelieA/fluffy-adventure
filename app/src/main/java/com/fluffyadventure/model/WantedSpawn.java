package com.fluffyadventure.model;

import java.util.ArrayList;

/**
 * Created by denis on 23/03/15.
 */
public class WantedSpawn extends AbstractSpawn{

    private ArrayList<Creature> opponents = new ArrayList<>();

    public WantedSpawn(int spellReward, int healthReward,
                       int strengthReward, double latitude, double longitude,
                       String text, String name, Integer level,
                       ArrayList<Creature> opponents, boolean isSoloFight) {

        super(-1, spellReward, healthReward, strengthReward, latitude, longitude, text, name, level, isSoloFight);
        this.opponents = opponents;
    }

    @Override
    public String getStandardIcon() {
        return "target_icon";
    }

    @Override
    public ArrayList<Creature> getOpponents() {
        return opponents;
    }

    @Override
    public void setOpponents(ArrayList<Creature> opponents) {
        this.opponents = opponents;
    }


}