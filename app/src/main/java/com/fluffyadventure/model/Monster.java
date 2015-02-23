package com.fluffyadventure.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 17/02/2015.
 */
public class Monster {

    private String name;
    private String imagePath;

    private int health = 0;
    private int strength = 0;
    private int accuracy = 0;
    private int evasiveness = 0;


    public Monster() {
    }

    public Monster(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
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
}
