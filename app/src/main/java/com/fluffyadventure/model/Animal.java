package com.fluffyadventure.model;

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

    private List<Integer> succeededSpawns= new ArrayList<>();

    public Animal() {
    }

    public Animal(String name, String imagePath, String type) {
        this.name = name;
        this.imagePath = imagePath;
        this.type = type;
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

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setName(String name) {
        this.name = name;
    }
}
