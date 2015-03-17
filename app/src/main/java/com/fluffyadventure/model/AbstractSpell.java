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
    private boolean hasHit = true;
    private int maxUses;
    private int uses = 0;

    protected AbstractSpell(int id, String name, String description, boolean isAoE, int animationType, String throwedObject, int maxUses) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isAoE = isAoE;
        this.animationType = animationType;
        this.throwedObject = throwedObject;
        this.maxUses = maxUses;
    }
    protected AbstractSpell(JSONObject json) throws JSONException {
        this.id = json.getInt("Id");
        this.name = json.getString("Name");
        this.description = json.getString("Description");
        this.isAoE = isAoE = json.getBoolean("IsAoE");
//        this.animationType = json.getInt("Animation");
//        this.throwedObject = json.getString("Projectile");
//        this.maxUses = json.getInt("Uses");
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

    public boolean hasHit() {
        return hasHit;
    }

    public void setHasHit(boolean hasHit) {
        this.hasHit = hasHit;
    }

    public int getMaxUses() {
        return maxUses;
    }

    public int getUses() {
        return uses;
    }

    protected void addUse(){
        uses++;
    }

    public void resetUses() {
        uses = 0;
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
