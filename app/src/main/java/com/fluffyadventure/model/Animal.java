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
    private String QGImage;
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

    public Animal(Animal animal) {
        this.name = animal.getName();
        this.imagePath = animal.getImagePath();
        this.type = animal.getType();
        this.health = animal.getHealth();
        this.strength = animal.getStrength();
        this.accuracy = animal.getAccuracy();
        this.evasiveness = animal.getEvasiveness();
        this.activeSpells = new ArrayList<>(animal.getActiveSpells());
        this.unusedSpells = new ArrayList<>(animal.getUnusedSpells());
        this.succeededSpawns = new ArrayList<>(animal.getSucceededSpawns());
    }

    public Animal(String imagePath, String type) {
        this.imagePath = imagePath;
        this.type = type;
        switch (type){
            case "Sheep":
                this.health = 135;
                this.strength = 8;
                this.accuracy = 100;
                this.evasiveness = 5;
                this.QGImage = "grassicon";
                break;
            case "Squirrel":
                this.health = 100;
                this.strength = 12;
                this.accuracy = 90;
                this.evasiveness = 5;
                this.QGImage = "nuticon";
                break;
            case "Rabbit":
                this.health = 100;
                this.strength = 10;
                this.accuracy = 100;
                this.evasiveness = 15;
                this.QGImage = "carroticon";
                break;
            default:
                this.health = 100;
                this.strength = 10;
                this.accuracy = 100;
                this.evasiveness = 10;
                this.type = "Rabbit";
                this.QGImage = "carroticon";
                break;
        }
    }


    public Animal(String name, String imagePath, String type) {
        this(imagePath, type);
        this.name = name;
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

    public List<Integer> getSucceededSpawns() {
        return succeededSpawns;
    }

    public String getQGImage() {
        return QGImage;
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
            active.put(spell.toJson());
        }
        spells_object.put("Active",active);

        JSONArray unused = new JSONArray();
        for (Spell spell: this.getUnusedSpells()){
            unused.put(spell.toJson());
        }

        spells_object.put("Unused",unused);

        json.put("Spells",spells_object);

        return json;
    }

    public Animal(JSONObject json) throws JSONException {
        JSONObject spells = json.getJSONObject("Spells");
        JSONArray unused = spells.getJSONArray("Unused");
        for (int i = 0; i < unused.length(); i++){
            Spell spell = new Spell(unused.getJSONObject(i));
            unusedSpells.add(spell);
        }

        JSONArray active = spells.getJSONArray("Unused");
        for (int i = 0; i < active.length(); i++){
            Spell spell = new Spell(active.getJSONObject(i));
            activeSpells.add(spell);
        }

        this.name = json.getString("Name");
        this.imagePath =  json.getString("ImgPath");
        this.type = json.getString("Type");
        this.health = json.getInt("Health");
        this.strength = json.getInt("Strength");
        this.accuracy = json.getInt("Accuracy");
        this.evasiveness = json.getInt("Evasiveness");

    }

    public void setActiveSpells(List<Spell> activeSpells) {
        this.activeSpells = activeSpells;
    }

    public void setUnusedSpells(List<Spell> unusedSpells) {
        this.unusedSpells = unusedSpells;
    }
}
