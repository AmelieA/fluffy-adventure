package com.fluffyadventure.model;

/**
 * Created by Jérémy on 04/03/2015.
 */
public class Friend {
    private String name;
    private String image;

    public Friend(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }
}
