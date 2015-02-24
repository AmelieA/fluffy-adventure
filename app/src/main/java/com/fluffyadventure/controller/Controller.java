package com.fluffyadventure.controller;

import android.util.Log;

import com.fluffyadventure.model.AbstractSpawn;
import com.fluffyadventure.model.Animal;
import com.fluffyadventure.model.Dungeon;
import com.fluffyadventure.model.Spawn;
import com.fluffyadventure.model.Treasure;

import java.util.ArrayList;

/**
 * Created by Johan on 17/02/2015.
 */
public class Controller {

    private static Animal animal;
    private static ArrayList<AbstractSpawn> objectives;

    public static void setupObjectives() {
        Log.i("FA", "Setting up objectives");

        AbstractSpawn fightSpawn1 = new Spawn(0,0,0,0,45.780035, 4.856392,"Pourfendre le méchant zombie mangeur de carottes","Bwaaarg");
        Log.i("FA", fightSpawn1.toString());

        AbstractSpawn fightSpawn2 = new Spawn(1,0,0,0,45.7767953, 4.8482761,"Des monstres s'amusent à chatouiller des chatons, sauve les vite !","Sauver les chatons !");
        Log.i("FA", fightSpawn2.toString());

        AbstractSpawn fightSpawn3 = new Spawn(2,0,0,0,45.7813447, 4.8513660,"Un méchant sorcier a lancé une malédiction sur les arbres de la forêt","Activité bûcheronnage");
        Log.i("FA", fightSpawn3.toString());

        AbstractSpawn fightSpawn4 = new Spawn(3,0,0,0,45.780666, 4.856438,"Des zombies échaffaudent un plan de conquète mondiale, arrête les !","Les non-génies du mal");
        Log.i("FA", fightSpawn4.toString());

        AbstractSpawn dungeon1 = new Dungeon(4,0,0,0,45.7853447, 4.8563660,"Much evil, such dungeon, so dangerous, very intense, wow","L'antre du mal");
        Log.i("FA", dungeon1.toString());

        AbstractSpawn treasure1 = new Treasure(5,0,0,0,45.7853447, 4.8563660,"Mon préééécieeeuux ..","Trésor enfoui");
        Log.i("FA", treasure1.toString());

    }
    public static void setupBob() {
        animal = new Animal("Bob","");
    }

    public static Animal getAnimal() {
        return animal;
    }
}
