package com.fluffyadventure.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by amelie on 2/25/15.
 */
public class Spell {

    private int id;
    private String Name;
    private String Description;


    public Spell(int id, String name, String description) {
        this.id = id;
        Name = name;
        Description = description;
    }
    public Spell(JSONObject json) throws JSONException {
        this.id = json.getInt("Id");
        this.Name = json.getString("Name");
        this.Description = json.getString("Description");
    }


    public String getName() {
        return Name;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return Description;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("Name", this.getName());
        json.put("Id",this.getId());
        json.put("Description",this.getDescription());
        return json;
    }

    @Override
    public String toString(){
        return getName();
    }
}
