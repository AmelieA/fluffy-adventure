package com.fluffyadventure.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 09/03/2015.
 */
public class StateSpell extends AbstractSpell {

    public StateSpell(int id, String name, String description, boolean isAoE, int animationType, String throwedObject) {
        super(id, name, description, isAoE, animationType, throwedObject);
    }

    public StateSpell(JSONObject json) throws JSONException {
        super(json);
    }

    public  ArrayList<ArrayList<Creature>> use(ArrayList<Creature> fighters, ArrayList<Creature> opponents, Integer target) {
        ArrayList<ArrayList<Creature>> returnedArray = new ArrayList<>();

        return returnedArray;
    }

    public int getValue() {
        return 0;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = super.toJson();
        json.put("Type",AbstractSpell.STATE);
        return json;
    }
}
