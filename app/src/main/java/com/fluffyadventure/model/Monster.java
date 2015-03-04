package com.fluffyadventure.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 17/02/2015.
 */
public class Monster extends Creature{

    private List<Spell> spells = new ArrayList<>();

    public Monster() {
    }

    public Monster(String name, String imagePath) {

    }

    public void addSpell(Spell spell, Boolean active){
        spells.add(spell);
    }

}
