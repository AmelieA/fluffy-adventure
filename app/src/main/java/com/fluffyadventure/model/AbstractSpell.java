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

    public static final int ATTACK = 6;
    public static final int THROW = 7;


    private int id;
    private String name;
    private String description;
    Boolean isAoE;
    private int animationType;
    private String throwedObject;
    private int value;

    protected AbstractSpell(int id, String name, String description, boolean isAoE, int animationType, String throwedObject) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isAoE = isAoE;
        this.animationType = animationType;
        this.throwedObject = throwedObject;
    }
    protected AbstractSpell(JSONObject json) throws JSONException {
        this.id = json.getInt("Id");
        this.name = json.getString("Name");
        this.description = json.getString("Description");
        this.isAoE = isAoE = json.getBoolean("IsAoE");
        //this.animationType = json.getBoolean("AnimationType");
        //this.throwedObject = json.getBoolean("ThrowedObject");
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

    public int getAnimationType() {
        return animationType;
    }

    public String getThrowedObject() {
        return throwedObject;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("Name", name);
        json.put("Id",id);
        json.put("Description", description);
        json.put("IsAoE",isAoE);
        return json;
    }

    public abstract ArrayList<ArrayList<Creature>> use(ArrayList<Creature> fighters, ArrayList<Creature> opponents, Integer source, Integer target);

    public abstract int getValue();

    @Override
    public String toString(){
        return getName();
    }
}
