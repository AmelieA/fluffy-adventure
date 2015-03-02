package com.fluffyadventure.controller;


import android.os.AsyncTask;

import android.util.Log;

import com.fluffyadventure.model.AbstractSpawn;
import com.fluffyadventure.model.Animal;
import com.fluffyadventure.model.Spell;
import com.fluffyadventure.model.User;
import com.fluffyadventure.model.Dungeon;
import com.fluffyadventure.model.Spawn;
import com.fluffyadventure.model.Treasure;

import java.util.ArrayList;

/**
 * Created by Johan on 17/02/2015.
 */
public class Controller {

    private static Animal animal1;
    private static Server server;
    private static User user;
    private static ArrayList<AbstractSpawn> objectives;


    public static Boolean createUser(String name, String password) {
        user = server.createUser(name,password);
        if (user == null) {
            return false;
        }
        return true;
    }

    public static void setupObjectives() {
        Log.i("FA", "Setting up objectives");

        objectives = new ArrayList<AbstractSpawn>();

        AbstractSpawn fightSpawn1 = new Spawn(0,0,0,0,45.780035, 4.856392,"Pourfendre le méchant zombie mangeur de carottes","Bwaaarg",1);
        objectives.add(fightSpawn1);
        Log.i("FA", fightSpawn1.toString());

        AbstractSpawn fightSpawn2 = new Spawn(1,0,0,0,45.7767953, 4.8482761,"Des monstres s'amusent à chatouiller des chatons, sauve les vite !","Sauver les chatons !",2);
        objectives.add(fightSpawn2);
        Log.i("FA", fightSpawn2.toString());

        AbstractSpawn fightSpawn3 = new Spawn(2,0,0,0,45.7813447, 4.8513660,"Un méchant sorcier a lancé une malédiction sur les arbres de la forêt","Activité bûcheronnage",3);
        objectives.add(fightSpawn3);
        Log.i("FA", fightSpawn3.toString());

        AbstractSpawn fightSpawn4 = new Spawn(3,0,0,0,45.780666, 4.856438,"Des zombies échaffaudent un plan de conquète mondiale, arrête les !","Les non-génies du mal",4);
        objectives.add(fightSpawn4);
        Log.i("FA", fightSpawn4.toString());

        AbstractSpawn dungeon1 = new Dungeon(4,0,0,0,45.7853447, 4.8563660,"Much evil, such dungeon, so dangerous, very intense, wow","L'antre du mal",5);
        objectives.add(dungeon1);
        Log.i("FA", dungeon1.toString());

        AbstractSpawn treasure1 = new Treasure(5,0,0,0,45.773716, 4.856081,"Mon préééécieeeuux ...","Trésor enfoui",0);
        objectives.add(treasure1);
        Log.i("FA", treasure1.toString());

    }
    public static void setupBob() {
        animal1 = new Animal("Bob","rabbit1","Rabbit");
    }

    public static void createAnimal1(String name, String imagePath, String type) {
        animal1 = new Animal(name,imagePath,type);
    }

    public static Animal getAnimal1() {
        return animal1;
    }

    public static ArrayList<AbstractSpawn> getObjectives() {
        return objectives;
    }
    public static User getUser() { return user; }

    public static Boolean login(String name, String password) {
        user =  server.login(name,password);
        if (user == null) {
            return false;
        }
        animal1 = server.getAnimal(user);
        if (animal1 == null) {
            setupBob();
        }
        return true;
    }

    public static Boolean sendAnimalToServer(String name){
        Animal my_animal = server.createAnimal(user,animal1,name);
        if (my_animal != null){
           animal1 = my_animal;
            return true;
        }
        return false;
    }

    public static Boolean connectToServer(String name, int port){
        server = new Server(name,port);
        return server.testConnection();

    }

    public static Boolean changeSpells(ArrayList<Spell> active, ArrayList<Spell> unused){
        if (active.equals(animal1.getActiveSpells()) && unused.equals(animal1.getUnusedSpells())){
            return  true;
        }
        Boolean done = server.changeSpells(user, active, unused);
        if (done){
            animal1.setActiveSpells(active);
            animal1.setUnusedSpells(unused);
            return true;
        }

        return false;
    }

    public static Server getServer() {
        return server;
    }
}
