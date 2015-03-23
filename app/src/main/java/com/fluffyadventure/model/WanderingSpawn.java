package com.fluffyadventure.model;

import java.util.ArrayList;

/**
 * Created by amelie on 3/23/15.
 */
public class WanderingSpawn extends AbstractSpawn{

    private ArrayList<Creature> opponents = new ArrayList<>();

    public WanderingSpawn(int id, int spellReward, int healthReward, int strengthReward, double latitude, double longitude,
                          String text, String name, Integer level, ArrayList<Creature> opponents, boolean isSoloFight) {
        super(id, spellReward, healthReward, strengthReward, latitude, longitude, text, name, level, isSoloFight);
        this.opponents = opponents;
    }

    @Override
    public String getStandardIcon() {
        return opponents.get(0).getImagePath()+"_black";
//        return "interrogation";
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
