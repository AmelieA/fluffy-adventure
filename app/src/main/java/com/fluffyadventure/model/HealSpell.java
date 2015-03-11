package com.fluffyadventure.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 09/03/2015.
 */
public class HealSpell extends AbstractSpell {

    private int heal;

    public HealSpell(int id, String name, String description, boolean isAoE, int heal) {
        super(id, name, description, isAoE);
        this.heal = heal;
    }
    public HealSpell(JSONObject json) throws JSONException {
        super(json);
        heal = json.getInt("Health");
    }

    public  ArrayList<ArrayList<Creature>> use(ArrayList<Creature> fighters, ArrayList<Creature> opponents, Integer target) {
        ArrayList<ArrayList<Creature>> returnedArray = new ArrayList<>();

        if ( target != null ) {
            fighters.get(target).setHealth(fighters.get(target).getHealth() + heal);
        } else if ( target == null ) {
            fighters.get(0).setHealth(fighters.get(0).getHealth() + heal);
            if (fighters.size() > 1) {
                fighters.get(1).setHealth(fighters.get(1).getHealth() + heal);
            }
        }

        returnedArray.add(fighters);
        returnedArray.add(opponents);

        return returnedArray;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = super.toJson();

        json.put("Type",AbstractSpell.HEAL);
        json.put("Health",heal);
        return json;
    }
}
