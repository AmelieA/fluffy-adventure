package com.fluffyadventure.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 17/02/2015.
 */
public class Animal {

    private String name;
    private String imagePath;
    private String type;

    private int health = 0;
    private int strength = 0;
    private int accuracy = 0;
    private int evasiveness = 0;

    private List<Spell> activeSpells = new ArrayList<>();
    private List<Spell> unusedSpells = new ArrayList<>();

    private List<Integer> succeededSpawns= new ArrayList<>();

    public Animal() {
    }

    public Animal(String name, String imagePath, String type) {
        this.name = name;
        this.imagePath = imagePath;
        this.type = type;
        switch (type){
            case "Sheep":
                this.health = 125;
                this.strength = 8;
                this.accuracy = 100;
                this.evasiveness = 0;
                break;
            case "Squirrel":
                this.health = 100;
                this.strength = 12;
                this.accuracy = 90;
                this.evasiveness = 0;
                break;
            case "Rabbit":
                this.health = 100;
                this.strength = 10;
                this.accuracy = 100;
                this.evasiveness = 10;
                break;
            default:
                this.health = 100;
                this.strength = 10;
                this.accuracy = 100;
                this.evasiveness = 10;
                this.type = "Rabbit";
                break;
        }
    }

    public Animal(String imagePath, String type) {
        this.imagePath = imagePath;
        this.type = type;
        switch (type){
            case "Sheep":
                this.health = 125;
                this.strength = 8;
                this.accuracy = 100;
                this.evasiveness = 0;
                break;
            case "Squirrel":
                this.health = 100;
                this.strength = 12;
                this.accuracy = 90;
                this.evasiveness = 0;
                break;
            case "Rabbit":
                this.health = 100;
                this.strength = 10;
                this.accuracy = 100;
                this.evasiveness = 10;
                break;
            default:
                this.health = 100;
                this.strength = 10;
                this.accuracy = 100;
                this.evasiveness = 10;
                this.type = "Rabbit";
                break;
        }
    }

    public void success(Integer spawnID) {
        this.succeededSpawns.add(spawnID);
    }

    public boolean hasSucceeded(Integer spawnID) {
        return this.succeededSpawns.contains(spawnID);
    }

    public int gainHealth(Integer gain) {
        health += gain;
        return health;
    }
    public int gainStrength(Integer gain) {
        strength += gain;
        return strength;
    }

    public int getEvasiveness() {
        return evasiveness;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public int getStrength() {
        return strength;
    }

    public int getHealth() {
        return health;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getType() {
        return type;
    }

    public List<Spell> getActiveSpells() {
        return activeSpells;
    }

    public List<Spell> getUnusedSpells() {
        return unusedSpells;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addSpell(Spell spell, Boolean active){
        if ((this.activeSpells.indexOf(spell) ==  -1 ) && (this.unusedSpells.indexOf(spell) ==  -1 )) {

            if (active) {
                this.activeSpells.add(spell);
            } else {
                this.unusedSpells.add(spell);
            }
        }

    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("Name",this.getName());
        json.put("ImgPath",this.getImagePath());
        json.put("Type",this.getType());
        json.put("Health",this.getHealth());
        json.put("Strength", this.getStrength());
        json.put("Accuracy",this.getAccuracy());
        json.put("Evasiveness",this.getEvasiveness());
        JSONObject spells_object = new JSONObject();
        JSONArray active = new JSONArray();
        for (Spell spell : this.getActiveSpells()){
            active.put(spell);
        }
        spells_object.put("Active",active);

        JSONArray unused = new JSONArray();
        for (Spell spell: this.getUnusedSpells()){
            unused.put(spell);
        }

        spells_object.put("Unused",unused);

        json.put("Spells",spells_object);

        return json;
    }
}
