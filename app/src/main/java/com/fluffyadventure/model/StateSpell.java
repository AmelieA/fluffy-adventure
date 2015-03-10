package com.fluffyadventure.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 09/03/2015.
 */
public class StateSpell extends AbstractSpell {

    public StateSpell(int id, String name, String description, boolean isAoE) {
        super(id, name, description, isAoE);
    }

    public StateSpell(JSONObject json) throws JSONException {
        super(json);
    }

    public  List<List<Creature>> use(List<Creature> fighters, List<Creature> opponents, Integer target) {
        ArrayList<List<Creature>> returnedArray = new ArrayList<>();

        return returnedArray;
    }
    public JSONObject toJson() throws JSONException {
        JSONObject json = super.toJson();
        json.put("Type","State");
        return json;
    }
}
