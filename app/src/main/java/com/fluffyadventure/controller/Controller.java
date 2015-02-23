package com.fluffyadventure.controller;

import com.fluffyadventure.model.Animal;

/**
 * Created by Johan on 17/02/2015.
 */
public class Controller {

    private static Animal animal;

    public static void setupBob() {
        animal = new Animal("Bob","");
    }

    public static Animal getAnimal() {
        return animal;
    }
}
