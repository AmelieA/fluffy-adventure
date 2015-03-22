package com.fluffyadventure.controller;


import android.util.Log;

import com.fluffyadventure.model.AbstractSpawn;
import com.fluffyadventure.model.Animal;
import com.fluffyadventure.model.Creature;
import com.fluffyadventure.model.Friend;
import com.fluffyadventure.model.DamageSpell;
import com.fluffyadventure.model.HealSpell;
import com.fluffyadventure.model.Mail;
import com.fluffyadventure.model.MailWanted;
import com.fluffyadventure.model.Monster;
import com.fluffyadventure.model.AbstractSpell;
import com.fluffyadventure.model.User;
import com.fluffyadventure.model.Dungeon;
import com.fluffyadventure.model.Spawn;
import com.fluffyadventure.model.Treasure;
import com.google.android.gms.internal.m;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Johan on 17/02/2015.
 */
public class Controller {

    private static Animal animal1;
    private static Animal animal2;
    private static Server server;
    private static User user;
    private static ArrayList<AbstractSpawn> objectives;
    private static LatLng QGLocation;
    private static ArrayList<Integer> succeededSpawns= new ArrayList<>();
    private static AbstractSpawn currentObjective;
    private static ArrayList<Friend> friends = new ArrayList<>();
    private static ArrayList<Mail> mails = new ArrayList<>();

    private static final double COORDINATES_COEFFICIENT = 0.005;
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
        ArrayList<Creature> opponentSolo = new ArrayList<Creature>();
        ArrayList<Creature> opponentsDuo = new ArrayList<Creature>();
        AbstractSpell evilSpell = new DamageSpell(42,"Dynamite", "Blesse une cible ennemie pour 120% de l'attaque", false, 120, AbstractSpell.THROW, "hazelnut",100);
        AbstractSpell evilHeal = new HealSpell(43,"Carotte Nom Nom", "Om nm nom", false, 25, AbstractSpell.HEAL, null, 2);
        ArrayList<AbstractSpell> spells = new ArrayList<>();
        spells.add(evilSpell);
        spells.add(evilHeal);
        opponentSolo.add(new Monster("Evil Bunny",Creature.EVILBUNNY, 100, 10, 90, 35, spells));
        opponentsDuo.add(new Monster("Evil Bunny",Creature.EVILBUNNY, 100, 10, 90, 35, spells));
        opponentsDuo.add(new Monster("Evil Bunny",Creature.EVILBUNNY, 100, 10, 90, 35, spells));

        AbstractSpawn fightSpawn1 = new Spawn(0,0,0,0,45.780035, 4.856392,
                "Pourfendre le méchant zombie mangeur de carottes","Bwaaarg",1,opponentSolo, true);
        objectives.add(fightSpawn1);
        Log.i("FA", fightSpawn1.toString());

        AbstractSpawn fightSpawn2 = new Spawn(1,0,0,0,45.7767953, 4.8482761,
                "Des monstres s'amusent à chatouiller des chatons, sauve les vite !","Sauver les chatons !",2,opponentsDuo, false);
        objectives.add(fightSpawn2);
        Log.i("FA", fightSpawn2.toString());

        AbstractSpawn fightSpawn3 = new Spawn(2,0,0,0,45.7813447, 4.8513660,
                "Un méchant sorcier a lancé une malédiction sur les arbres de la forêt","Activité bûcheronnage",3,opponentSolo, true);
        objectives.add(fightSpawn3);
        Log.i("FA", fightSpawn3.toString());

        AbstractSpawn fightSpawn4 = new Spawn(3,0,0,0,45.780666, 4.856438,
               "Des zombies échaffaudent un plan de conquète mondiale, arrête les !","Les non-génies du mal",4,opponentSolo, true);
        objectives.add(fightSpawn4);
        Log.i("FA", fightSpawn4.toString());

        AbstractSpawn dungeon1 = new Dungeon(4,0,0,0,45.7853447, 4.8563660,
                "Much evil, such dungeon, so dangerous, very intense, wow","L'antre du mal",5,opponentSolo, true);
        objectives.add(dungeon1);
        Log.i("FA", dungeon1.toString());

        AbstractSpawn treasure1 = new Treasure(5,0,0,0,45.773716, 4.856081,
                "Mon préééécieeeuux ...","Trésor enfoui",0, true);
        objectives.add(treasure1);
        Log.i("FA", treasure1.toString());

    }

    public  static  void setUpObjectivesFromServer() {
        objectives = server.get_spawns();
        if (objectives != null ){
            objectives.get(0).setCoordinates(45.780035,4.856392);
            objectives.get(1).setCoordinates(45.7767953,4.8482761);
            objectives.get(2).setCoordinates(45.7813447, 4.8513660);
            objectives.get(3).setCoordinates(45.780666, 4.856438);
            objectives.get(4).setCoordinates(45.7853447, 4.8563660);
            objectives.get(5).setCoordinates(45.773716, 4.856081);
        }

    }


    public  static  void setUpObjectivesWithHq() {
        objectives = server.get_spawns();
        Log.d("Objectives","Received");
        Random randomGenerator = new Random();

        if (objectives != null && QGLocation != null){
            for (int i = 0; i < objectives.size(); i++){
                double coefLat = Math.cos(i) * COORDINATES_COEFFICIENT;
                double coefLong = Math.sin(i) * COORDINATES_COEFFICIENT;
                double randLat = (randomGenerator.nextDouble() - 0.5) * (COORDINATES_COEFFICIENT * 0.5);
                double randLong = (randomGenerator.nextDouble() - 0.5) * COORDINATES_COEFFICIENT;// * 0.0025;
                objectives.get(i).setCoordinates(
                        QGLocation.latitude + coefLat + randLat,
                        QGLocation.longitude + coefLong + randLong
                );
                Log.d("Objective " + Integer.toString(i), objectives.get(i).toString());
            }

        }

    }

    public static void setupBob() {
        animal1 = new Animal("Bob","rabbit1", Creature.RABBIT);

        animal2 = new Animal("BobTwin","rabbit2", Creature.RABBIT);
    }

    public static void createAnimal1(String name, String imagePath, int type) {
        animal1 = new Animal(name,imagePath,type);
    }

    public static void setAnimal1(Animal animal1) {
        Controller.animal1 = animal1;
    }

    public static void setAnimal2(Animal animal2) {
        Controller.animal2 = animal2;
    }

    public static Animal getAnimal1() {
        return animal1;
    }
    public static Animal getAnimal(int id) {
        if (id == 1){
            return animal1;
        }
        else{
            return animal2;
        }
    }

    public static ArrayList<Friend> getFriends() {
        return friends;
    }

    public static ArrayList<AbstractSpawn> getObjectives() {
        return objectives;
    }
    public static User getUser() { return user; }
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

    public static Boolean changeSpells(ArrayList<AbstractSpell> active, ArrayList<AbstractSpell> unused){
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

    public static LatLng getQGLocation() {
        return QGLocation;
    }

    public static void setQGLocation(double latitude, double longitude) {
        Controller.QGLocation = new LatLng(latitude,longitude);
    }

    public static void success(Integer spawnID) {
        succeededSpawns.add(spawnID);
    }

    public static boolean hasSucceeded(Integer spawnID) {
        return succeededSpawns.contains(spawnID);
    }

    public static ArrayList<Integer> getSucceededSpawns() {
        return succeededSpawns;
    }

    public static AbstractSpawn getCurrentObjective() {
        return currentObjective;
    }

    public static void setCurrentObjective(AbstractSpawn currentObjective) {
        Controller.currentObjective = currentObjective;
    }

    public static Boolean userExists(String name) {
        return server.getUser(name);
    }

    public static void setUser(User user) {
        Controller.user = user;
    }

    public static int createUserAnimalHQ(){
        user = server.createUser(user.getName(),user.getPassword());
        if (user == null){
            return -1;
        }
        animal1 = server.createAnimal(user,animal1,animal1.getName());
        if (animal1 == null){
            server.deleteUser(user);
            return -2;
        }
        Log.d("ordre","bon");

        Boolean hasHQbeenMoved = moveHQ();
        if (!hasHQbeenMoved){
            server.deleteUser(user);
            Log.d("ERROR","HQ CREATION");
            return -3;
        }
        Log.d("User Created","TRUE");

        return 0;

    }



    public static Boolean retrieveMailsFromServer(){
        ArrayList<Mail> mails2 = server.getMails(user);
        if (mails2 == null) {
            return false;
        }
        mails = mails2;
        return true;
    }

    public static ArrayList<Mail> getMails(){
        /*Mail mail1 = new Mail(1, "Expediteur1", "Ceci est un mail 1","heya1",10);
        Mail mail2 = new Mail(2, "Expediteur2", "Ceci est un mail 2","heya2",11);
        Mail mail3 = new Mail(3, "Expediteur3", "Ceci est un mail 3","heya3",12);
        Mail mail4 = new Mail(4, "Expediteur4", "Ceci est un mail 4","heya4",13);
        Mail mailWanted = new MailWanted(5,"Mission !","RechercherTartampion","",999,"Bobby","squirrel1","Robert","squirrel2");
        ArrayList<Mail> mails = new ArrayList<>(Arrays.asList(mail1, mail2, mail3, mail4, mailWanted));*/
        return mails;
    }

    public static Boolean addFriend(String name){
        Friend friend = server.addFriend(user,name);
        if (friend == null){
            return false;
        }
        friends.add(friend);
        return true;
    }

    public static Boolean sendMail(String receiver, String object, String content){
        return server.sendMail(user,receiver,object,content);
    }



    public static void flush(){
        animal1 = null;
        animal2 = null;
        server = null;
        user = null;
        objectives = null;
        QGLocation =  null;
        succeededSpawns= new ArrayList<>();

    }


//METHODS BELOW THIS POINT CONNECT TO SERVER, PLEASE USE INSIDE ASYNCTASKS



    public static Boolean login(String name, String password) {
        user =  server.login(name,password);
        if (user == null) {
            return false;
        }
        animal1 = server.getAnimal(user);
        if (animal1 == null) {
            return false;
        }

        QGLocation = server.getHQ(user);
        if (QGLocation == null){
            return false;
        }
        friends = server.getFriends(user);
        if (friends == null){
            return false;
        }
        mails = server.getMails(user);
        Log.d("Mails login", mails.toString());
        if (mails == null) {
            return false;
        }

        setUpObjectivesWithHq();
        //setupObjectives();

        return true;
    }

    public static Boolean new_login(String name, String password) {
        user =  server.login(name,password);
        String uri = "http://" + server.getIpAddress() + ":" + Integer.toString(server.getPort()) + "/api/" + "login2";
        JSONObject returnJson;
        try {
            URL url = new URL(uri);
            User user1 = new User(name,password);
            JSONObject outputJson =  server.connectWithPassword(url,user1,HttpURLConnection.HTTP_OK,true,false,null);

            if (outputJson == null) {
                Log.e("HTTP LOGIN :", Integer.toString(HttpURLConnection.HTTP_OK));
                return false;
            }
            returnJson = outputJson.getJSONObject("json");
        } catch (IOException | JSONException ex) {
            Log.e("LOGIN :",ex.getMessage());
            ex.printStackTrace();
            return false;
        }

        //GET ID BLOCK :
        try {
            user = new User(name,password,returnJson.getInt("Id"));
        } catch (JSONException e) {

            Log.e("LOGIN ID :",e.getMessage());
            e.printStackTrace();
            return false;
        }
        if (user == null) {
            return false;
        }

        //GET ANIMALS BLOCK
        try {
            animal1 = new Animal(returnJson.getJSONArray("Animals").getJSONObject(0));
            if (returnJson.getJSONArray("Animals").length() > 1){
                animal2 = new Animal(returnJson.getJSONArray("Animals").getJSONObject(1));
            }
        } catch (JSONException e) {
            Log.e("LOGIN Animals :", e.getMessage());
            e.printStackTrace();
        }
        if (animal1 == null) {
            return false;
        }


        //GET HQ BLOCK
        try {
            QGLocation = new LatLng(returnJson.getJSONObject("HQ").getDouble("Latitude"), returnJson.getJSONObject("HQ").getDouble("Longitude"));
        } catch (JSONException e) {
            Log.e("LOGIN HQ :", e.getMessage());
            e.printStackTrace();
            return false;
        }
        if (QGLocation == null){
            return false;
        }

        //GET FRIENDLIST BLOCK
        friends = server.getFriends(user);

        try {
            JSONArray friendsArray = returnJson.getJSONArray("Friends");
            for (int i = 0; i < friendsArray.length(); i++){
                Friend friend = new Friend(friendsArray.getJSONObject(i));
                friends.add(friend);
            }
        } catch (JSONException e) {

            Log.e("LOGIN FRIENDS :", e.getMessage());
            e.printStackTrace();
            return false;
        }
        if (friends == null){
            return false;
        }

        //MAILSBLOCK
        JSONArray mailsArray = null;
        try {
            mailsArray = returnJson.getJSONArray("Mails");
            for (int i = 0; i < mailsArray.length(); i++) {
                Mail mail = new Mail(mailsArray.getJSONObject(i));
                mails.add(mail);
            }

        } catch (JSONException e) {
            Log.e("LOGIN MAILS :", e.getMessage());
            e.printStackTrace();
            return false;
        }
        Log.d("Mails login", mails.toString());
        if (mails == null) {
            return false;
        }

        //SPAWNSBLOCK
        JSONArray array = null;
        objectives = new ArrayList<>();
        try {
            array = returnJson.getJSONArray("Spawns");

            Random randomGenerator = new Random();
            for (int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                AbstractSpawn abstractSpawn;
                Log.d("Spawn",object.toString());
                String type = object.getString("Type");



                //TODO: REPLACE WITH SWITCH
                if (type.equals("Spawn")) {
                    abstractSpawn = new Spawn(object);
                }
                else if (type.equals("Dungeon")){
                    abstractSpawn = new Dungeon(object);
                }
                else {
                    abstractSpawn = new Treasure(object);
                }
                if (abstractSpawn == null){
                    Log.e("Spawn", "is null");
                }

                if (object.has("Location")){
                    abstractSpawn.setCoordinates(
                            object.getJSONObject("Location").getDouble("Latitude"),
                            object.getJSONObject("Location").getDouble("Longitude")
                    );
                }
                else {
                    double coefLat = Math.cos(i) * COORDINATES_COEFFICIENT;
                    double coefLong = Math.sin(i) * COORDINATES_COEFFICIENT;
                    double randLat = (randomGenerator.nextDouble() - 0.5) * COORDINATES_COEFFICIENT/2;
                    double randLong = (randomGenerator.nextDouble() - 0.5) * COORDINATES_COEFFICIENT;
                    abstractSpawn.setCoordinates(
                            QGLocation.latitude + coefLat + randLat,
                            QGLocation.longitude + coefLong + randLong
                    );
                }
                objectives.add(abstractSpawn);
            }
        } catch (JSONException e) {
            Log.e("LOGIN SPAWNS :", e.getMessage());
            e.printStackTrace();
            return false;
        }


        //SUCCEEDED SPAWNS BLOCK
        succeededSpawns = new ArrayList<>();
        try {
            JSONArray succeededArray = returnJson.getJSONArray("Progress");

            for (int i = 0; i < succeededArray.length(); i++){
                succeededSpawns.add(succeededArray.getInt(i));
            }
        } catch (JSONException e) {
            Log.e("LOGIN SUCCEEDED SPAWNS:", e.getMessage());
            e.printStackTrace();
            return false;
        }

        //setupObjectives();

        return true;
    }

    public static Boolean moveHQ(){
        Boolean hasHQbeenMoved = server.moveHQ(user, QGLocation.latitude, QGLocation.longitude);
        setUpObjectivesWithHq();
        //setupObjectives();

        return hasHQbeenMoved;
    }

    public static Boolean moveHQ2(){
        //Boolean hasHQbeenMoved = server.moveHQ(user, QGLocation.latitude, QGLocation.longitude);
        setUpObjectivesWithHq();

        String uri = "http://" + server.getIpAddress() + ":" + Integer.toString(server.getPort()) + "/api/" + "users/move_HQ2";
        try {

            URL url = new URL(uri);
            JSONObject HQAndSpawns = moveHQSpawns();
            JSONObject returnJson = server.connectWithAuth(url, user, HttpURLConnection.HTTP_OK, true, true, HQAndSpawns);
            if (returnJson == null){
                return false;
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return false;
        }


        return true;
    }

    public static JSONObject moveHQSpawns() throws JSONException {
        JSONObject HQLocation = new JSONObject();
        HQLocation.put("Latitude",QGLocation.latitude);
        HQLocation.put("Longitude",QGLocation.longitude);

        JSONArray spawnsLocationsJson = new JSONArray();
            for (AbstractSpawn spawn: objectives){
                JSONObject spawnJson = new JSONObject();
                //Todo: Make AbstractSpawn.toJson()
                spawnJson.put("Id",spawn.getSpawnId());
                spawnJson.put("Latitude", spawn.getLocation().getLatitude());
                spawnJson.put("Longitude", spawn.getLocation().getLongitude());
                spawnsLocationsJson.put(spawnJson);
            }
        JSONObject returnJson = new JSONObject();
        returnJson.put("HQ",HQLocation);
        returnJson.put("Spawns",spawnsLocationsJson);

        return returnJson;

    }

    public static Boolean saveGame(){
        String uri  = "http://" + server.getIpAddress() + ":" + Integer.toString(server.getPort()) + "/api/" + "save";
        try {

            URL url = new URL(uri);
            JSONObject json = new JSONObject();
            JSONArray succeededIds = new JSONArray(succeededSpawns);
            Log.d("Succeeded spawns:", succeededIds.toString());
            json.put("Progress",succeededIds);
            JSONObject returnJson = server.connectWithAuth(url, user, HttpURLConnection.HTTP_OK, false, true, json);

            if (returnJson != null){
                return true;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException ex) {
            ex.printStackTrace();
            return false;
        }
        return false;
    }


}
