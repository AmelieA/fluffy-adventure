package com.fluffyadventure.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Johan on 04/03/2015.
 */
public abstract class Creature {

    public static final int SHEEP = 0;
    public static final int SQUIRREL = 1;
    public static final int RABBIT = 2;
    public static final int ZOMBIE = 3;
    public static final int EVILBUNNY = 4;
    public static final int TREANT = 5;
    public static final int BLOB = 6;

    protected String name;
    protected String imagePath;
    protected int type;
    protected String QGImage;

    protected int health = 0;
    protected int strength = 0;
    protected int accuracy = 0;
    protected int evasiveness = 0;

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
    }

    public Creature(String imagePath, int type) {
        this.imagePath = imagePath;
        this.type = type;
        switch (type) {
            case Creature.SHEEP:
                this.health = 135;
                this.strength = 8;
                this.accuracy = 100;
                this.evasiveness = 5;
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
                this.type = Creature.RABBIT;
                this.QGImage = "carroticon";
                break;
        }
    }

    public Creature(String name, String imagePath, int type) {
        this(imagePath, type);
        this.name = name;
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

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract void addSpell(Spell spell, Boolean active);


}