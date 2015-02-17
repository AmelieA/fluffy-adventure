package com.fluffyadventure.model;

public class Animal {

    private String name;
    private String imagePath;

    private int health = 0;
    private int strength = 0;
    private int accuracy = 0;
    private int evasiveness = 0;

    public Animal() {
    }

    public Animal(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }
}
