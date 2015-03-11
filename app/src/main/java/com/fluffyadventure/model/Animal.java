package com.fluffyadventure.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 17/02/2015.
 */
public class Animal extends Creature{

    private List<AbstractSpell> unusedSpells = new ArrayList<>();

    public Animal() {}

    public Animal(Animal animal) {
        super(animal);
        this.unusedSpells = new ArrayList<>(animal.getUnusedSpells());
    }

    public Animal(String imagePath, int type) {
        super(imagePath, type);
    }

    public Animal(String name, String imagePath, int type) {
        super(name, imagePath, type);
    }

    public int gainHealth(Integer gain) {
        health += gain;
        return health;
    }
    public int gainStrength(Integer gain) {
        strength += gain;
        return strength;
    }

    public List<AbstractSpell> getUnusedSpells() {
        return unusedSpells;
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
        for (AbstractSpell spell : this.getActiveSpells()){
            active.put(spell.toJson());
        }
        spells_object.put("Active",active);

        JSONArray unused = new JSONArray();
        for (AbstractSpell spell: this.getUnusedSpells()){
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
           AbstractSpell spell = new DamageSpell(unused.getJSONObject(i));
           unusedSpells.add(spell);
        }

        JSONArray active = spells.getJSONArray("Active");
        Log.d("activeJson", active.toString());
        for (int i = 0; i < active.length(); i++){
            AbstractSpell spell = new DamageSpell(active.getJSONObject(i));
            activeSpells.add(spell);
        }
        Log.d("activetoucour",activeSpells.toString());

        this.name = json.getString("Name");
        this.imagePath =  json.getString("ImgPath");
        this.type = Integer.parseInt(json.getString("Type"));
        this.health = json.getInt("Health");
        this.strength = json.getInt("Strength");
        this.accuracy = json.getInt("Accuracy");
        this.evasiveness = json.getInt("Evasiveness");

        switch (type){
            case Creature.SHEEP:
                this.QGImage = "grassicon";
                break;
            case Creature.SQUIRREL:
                this.QGImage = "nuticon";
                break;
            case Creature.RABBIT:
                this.QGImage = "carroticon";
                break;
            default:
                this.type = Creature.RABBIT;
                this.QGImage = "carroticon";
                break;
        }

    }

    public void setUnusedSpells(List<AbstractSpell> unusedSpells) {
        this.unusedSpells = unusedSpells;
    }

    public void addSpell(AbstractSpell spell, Boolean active){
        if ((this.activeSpells.indexOf(spell) ==  -1 ) && (this.unusedSpells.indexOf(spell) ==  -1 )) {
            if (active) {
                this.activeSpells.add(spell);
            } else {
                this.unusedSpells.add(spell);
            }
        }
    }
}
