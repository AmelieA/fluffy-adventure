package com.fluffyadventure.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 17/02/2015.
 */
public class Monster extends Creature{

    public Monster() {
    }

    public Monster(String name, String imagePath) {

    }

    public Monster(String name, int type, int health, int strength, int accuracy, int evasiveness, List<AbstractSpell> spells) {
        super(name, type, health, strength, accuracy, evasiveness);
        this.activeSpells = spells;
    }

    public void addSpell(AbstractSpell spell, Boolean active){
        activeSpells.add(spell);
    }

}
