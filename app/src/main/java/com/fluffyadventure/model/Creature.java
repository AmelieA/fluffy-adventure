package com.fluffyadventure.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 04/03/2015.
 */
public abstract class Creature {

    public static final int SHEEP = 0;
    public static final int SQUIRREL = 1;
    public static final int RABBIT = 2;
    public static final int EVILHAMSTER = 3;
    public static final int EVILBUNNY = 4;
    public static final int EVILMONKEY1 = 5;
    public static final int EVILMONKEY2 = 6;
    public static final int EVILTEDDY1 = 7;
    public static final int EVILTEDDY2 = 8;

    protected String name;
    protected String imagePath;
    protected int type;
    protected String QGImage;

    protected int health = 0;
    protected int strength = 0;
    protected int accuracy = 0;
    protected int evasiveness = 0;

    protected List<AbstractSpell> activeSpells = new ArrayList<>();

    public Creature() {
    }

    public Creature(Creature creature) {
        this.name = creature.getName();
        this.imagePath = creature.getImagePath();
        this.type = creature.getType();
        this.health = creature.getHealth();
        this.strength = creature.getStrength();
        this.accuracy = creature.getAccuracy();
        this.evasiveness = creature.getEvasiveness();
        this.QGImage = creature.getQGImage();
        this.activeSpells = new ArrayList<>(creature.getActiveSpells());
    }
    public Creature(JSONObject json) throws JSONException {
        JSONObject spells = json.getJSONObject("Spells");

        JSONArray active = spells.getJSONArray("Active");
        Log.d("activeJson", active.toString());
        for (int i = 0; i < active.length(); i++){
            AbstractSpell spell;
            JSONObject inputJson = active.getJSONObject(i);
            Log.d("Type",active.getJSONObject(i).toString());
            Log.d("TYPE??",Integer.toString(inputJson.getInt("Type")));
            switch (inputJson.getInt("Type")){
                case AbstractSpell.DAMAGE:
                    spell = new DamageSpell(inputJson);
                    break;
                case AbstractSpell.HEAL:
                    spell = new HealSpell(inputJson);
                    break;
                case AbstractSpell.BUFF:
                    spell = new BuffSpell(inputJson);
                    break;
                case AbstractSpell.DEBUFF:
                    spell = new DebuffSpell(inputJson);
                    break;
                default:
                    Log.d("Default","why");
                    spell = new DamageSpell(inputJson);
                    break;

            }
            Log.d("Spell added",spell.toJson().toString());
            activeSpells.add(spell);
        }

        Log.d("activetoucour",activeSpells.toString());

        this.name = json.getString("Name");
        this.type = Integer.parseInt(json.getString("Type"));
        this.health = json.getInt("Health");
        this.strength = json.getInt("Strength");
        this.accuracy = json.getInt("Accuracy");
        this.evasiveness = json.getInt("Evasiveness");

        switch (type) {
            case Creature.EVILBUNNY:
                this.imagePath = "evilbunny";
                break;
            case Creature.EVILMONKEY1:
                this.imagePath = "evil_monkey";
                break;
            case Creature.EVILMONKEY2:
                this.imagePath = "evil_monkey2";
                break;
            case Creature.EVILHAMSTER:
                this.imagePath = "evil_hamster";
                break;
            case Creature.EVILTEDDY1:
                this.imagePath = "evil_teddy";
                break;
            case Creature.EVILTEDDY2:
                this.imagePath = "evil_teddy2";
                break;
            default:
                this.imagePath = "evilbunny";
                break;
        }


    }

    public Creature(String imagePath, int type) {
        this.imagePath = imagePath;
        this.type = type;
        switch (type) {
            case Creature.SHEEP:
                this.health = 100;
                this.strength = 9;
                this.accuracy = 100;
                this.evasiveness = 10;
                this.QGImage = "grassicon";
                break;
            case Creature.SQUIRREL:
                this.health = 100;
                this.strength = 12;
                this.accuracy = 90;
                this.evasiveness = 5;
                this.QGImage = "nuticon";
                break;
            case Creature.RABBIT:
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
                this.QGImage = "carroticon";
                break;
        }
    }

    public Creature(String name, String imagePath, int type) {
        this(imagePath, type);
        this.name = name;
    }

    protected Creature(String name, int type, int health, int strength, int accuracy, int evasiveness) {
        this.name = name;
        this.type = type;
        this.health = health;
        this.strength = strength;
        this.accuracy = accuracy;
        this.evasiveness = evasiveness;
        switch (type) {
            case Creature.EVILBUNNY:
                this.imagePath = "evilbunny";
                break;
            case Creature.EVILHAMSTER:
                this.imagePath = "zombie";
                break;
            case Creature.EVILMONKEY1:
                this.imagePath = "treant";
                break;
            default:
                this.imagePath = "evilbunny";
                break;
        }
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

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getQGImage() {
        return QGImage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public void setEvasiveness(int evasiveness) {
        this.evasiveness = evasiveness;
    }

    public List<AbstractSpell> getActiveSpells() {
        return activeSpells;
    }

    public void setActiveSpells(List<AbstractSpell> activeSpells) {
        this.activeSpells = activeSpells;
    }

    public abstract void addSpell(AbstractSpell spell, Boolean active);


}
