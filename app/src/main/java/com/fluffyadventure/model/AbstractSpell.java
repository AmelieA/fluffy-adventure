package com.fluffyadventure.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amelie on 2/25/15.
 */
public abstract class AbstractSpell {

    public static final int BUFF = 1;
    public static final int DEBUFF = 2;
    public static final int HEAL = 3;
    public static final int DAMAGE = 4;
    public static final int STATE = 5;

    private int id;
    private String name;
    private String description;
    Boolean isAoE;

    protected AbstractSpell(int id, String name, String description, boolean isAoE) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isAoE = isAoE;
    }
    protected AbstractSpell(JSONObject json) throws JSONException {
        this.id = json.getInt("Id");
        this.name = json.getString("Name");
        this.description = json.getString("Description");
        //this.isAoE = isAoE = json.getString("IsAoE");
    }


    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Boolean getIsAoE() {
        return isAoE;
    }

    public String getDescription() {
        return description;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("Name", name);
        json.put("Id",id);
        json.put("Description", description);
        //json.put("IsAoE",AoE);
        return json;
    }

    public abstract List<List<Creature>> use(List<Creature> fighters, List<Creature> opponents, Integer target);

    @Override
    public String toString(){
        return getName();
    }
}
