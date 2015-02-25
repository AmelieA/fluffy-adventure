package com.fluffyadventure.model;

/**
 * Created by amelie on 2/25/15.
 */
public class Spell {

    private int id;
    private String Name;
    private String Description;

    public Spell(int id, String name, String description) {
        this.id = id;
        Name = name;
        Description = description;
    }

    public String getName() {
        return Name;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return Description;
    }
}
