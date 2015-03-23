package com.fluffyadventure.controller;


import android.util.Log;

import com.fluffyadventure.model.AbstractSpawn;
import com.fluffyadventure.model.Animal;
import com.fluffyadventure.model.BuffSpell;
import com.fluffyadventure.model.Creature;
import com.fluffyadventure.model.DebuffSpell;
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
import com.fluffyadventure.model.WanderingSpawn;
import com.google.android.gms.maps.model.LatLng;

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
    //TODO QG -> HQ

    private static Animal animal1;
    private static final int MAX_SPAWN = 1000;
    private static Animal animal2 = null;
    private static Server server;
    private static User user;
    private static ArrayList<AbstractSpawn> objectives;
    private static ArrayList<Creature> monsters = new ArrayList<>();
    private static LatLng QGLocation;
    private static ArrayList<Integer> succeededSpawns= new ArrayList<>();
    private static AbstractSpawn currentObjective;
    private static ArrayList<Friend> friends = new ArrayList<>();
    private static ArrayList<Mail> mails = new ArrayList<>();

    private static final double COORDINATES_COEFFICIENT = 0.005;

    public  static  void setUpObjectivesWithHq() {
        objectives = get_spawns();
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

    public  static  void setUpWanderingSpawns() {
        Random randomGenerator = new Random();

        if (!monsters.isEmpty()) {
            for (int i = 0; i < 10; i++) {
                double coefLat = Math.cos(i) * COORDINATES_COEFFICIENT;
                double coefLong = Math.sin(i) * COORDINATES_COEFFICIENT;
                double randLat = (randomGenerator.nextDouble() - 0.5) * (COORDINATES_COEFFICIENT * 0.5);
                double randLong = (randomGenerator.nextDouble() - 0.5) * COORDINATES_COEFFICIENT;

                ArrayList<Creature> randomEnemies = new ArrayList<>(Arrays.asList(monsters.get(randomGenerator.nextInt(monsters.size()))));
                WanderingSpawn spawn = new WanderingSpawn(1000 + i, 0, 0, 0, QGLocation.latitude + coefLat + randLat,
                        QGLocation.longitude + coefLong + randLong, "text", "name", 0, randomEnemies, true);
                objectives.add(spawn);
                Log.d("Objective " + Integer.toString(i), objectives.get(i).toString());
            }
        }
    }

    public static void moveWanderingSpawns(){
        Random randomGenerator = new Random();

        for(int i=0; i<10; i++) {
            double coefLat = Math.cos(i) * COORDINATES_COEFFICIENT;
            double coefLong = Math.sin(i) * COORDINATES_COEFFICIENT;
            double randLat = (randomGenerator.nextDouble() - 0.5) * (COORDINATES_COEFFICIENT * 0.5);
            double randLong = (randomGenerator.nextDouble() - 0.5) * COORDINATES_COEFFICIENT;

            objectives.get(1000+i).setCoordinates(QGLocation.latitude + coefLat + randLat, QGLocation.longitude + coefLong + randLong);
        }
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

    public static void setUser(User user) {
        Controller.user = user;
    }

    public static ArrayList<Mail> getMails(){
        return mails;
    }

    public static Boolean checkForUnreadMails(){
        Boolean result = false;
        for (Mail m:mails){
            if(!m.getRead()){
                result=true;
                break;
            }
        }
        return result;
    }

    public static void setMailAsRead(Boolean read, Mail mail){
        for (Mail m:mails){
            if (m.equals(mail)){
                m.setRead(read);
                //save mail ?
                break;
            }
        }
    }

    public static void flush(){
        animal1 = null;
        animal2 = null;
        server = null;
        user = null;
        objectives = null;
        QGLocation =  null;
        succeededSpawns= new ArrayList<>();
        currentObjective = null;
        mails = new ArrayList<>();
        friends = new ArrayList<>();
    }


//METHODS BELOW THIS POINT CONNECT TO SERVER, PLEASE USE INSIDE ASYNCTASKS

    public static Boolean connectToServer(String name, int port){
        server = new Server(name,port);
        return server.testConnection();

    }

    public static Boolean userExists(String name) {
        String uri = "http://" + server.getIpAddress() + ":" + Integer.toString(server.getPort()) + "/api/" + "users/search/"+ name;
        try {
            URL url = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            int httpResult = urlConnection.getResponseCode();

            if (httpResult == HttpURLConnection.HTTP_OK) {

                return true;

            }
            Log.d("HTTP ERRROR",Integer.toString(httpResult));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;

    }

    public static int createUserAnimalHQ(){

        if (!(createUser(user.getName(),user.getPassword()))){
            return -1;
        }
        if (!(createAnimal(animal1.getName(), 1))){
            deleteUser();
            return -2;
        }
        Boolean hasHQbeenMoved = moveHQ2();
        if (!hasHQbeenMoved){
            deleteUser();
            Log.e("ERROR","HQ CREATION");
            return -3;
        }
        Log.d("User Created","TRUE");

        return 0;

    }

    private static Boolean createUser(String name, String password) {
        String uri = "http://" + server.getIpAddress() + ":" + Integer.toString(server.getPort()) + "/api/" + "users/new";
        try {
            URL url = new URL(uri);
            JSONObject json = new JSONObject();
            json.put("username", name);
            json.put("password", password);
            JSONObject returnJson = server.connectWithoutAuth(url,HttpURLConnection.HTTP_CREATED,true,true,json);
            if (returnJson == null){
                return false;
            }
            JSONObject inputJson = returnJson.getJSONObject("json");
            User newUser = new User(name, password, inputJson.getInt("Id"));
            String token = server.getToken(newUser);
            user.setToken(token);
            user = newUser;

        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    private static Boolean createAnimal(String name, int id){
        Animal animal;
        if (id == 1){
            animal = new Animal(animal1);
        }
        else {
            animal = new Animal(animal2);
        }

        String uri = "http://" + server.getIpAddress() + ":" + Integer.toString(server.getPort()) + "/api/" + "users/add_animal";
        try {

            ArrayList<Integer> ids = new ArrayList<>();
            ids.add(0);
            ids.add(1);
            ids.add(2);
            ids.add(3);
            for (AbstractSpell spell : getSpells(ids, animal.getType())){

                animal.addSpell(spell, true);
            }
            animal.setName(name);

            URL url = new URL(uri);
            JSONObject json = animal.toJson();
            JSONObject returnJson = server.connectWithAuth(url, user, HttpURLConnection.HTTP_OK, true, true, json);

            if (returnJson == null){
                return false;
            }

        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
            return false;
        }
        if (id == 1){
            animal1 = animal;
        }
        else {
            animal2 = animal;
        }

        return true;
    }

    public static Boolean moveHQ2(){
        //Boolean hasHQbeenMoved = server.moveHQ(user, QGLocation.latitude, QGLocation.longitude);
        setUpObjectivesWithHq();
        getMonsters();
        setUpWanderingSpawns();

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

    private static JSONObject moveHQSpawns() throws JSONException {
        JSONObject HQLocation = new JSONObject();
        HQLocation.put("Latitude", QGLocation.latitude);
        HQLocation.put("Longitude",QGLocation.longitude);

        JSONArray spawnsLocationsJson = new JSONArray();
        for (AbstractSpawn spawn: objectives){
            Log.d("moved spawn",spawn.getName());
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

    public static Boolean new_login(String name, String password) {
        String uri = "http://" + server.getIpAddress() + ":" + Integer.toString(server.getPort()) + "/api/" + "login2";
        JSONObject returnJson;
        try {
            URL url = new URL(uri);
            User user1 = new User(name,password);
            JSONObject outputJson =  server.connectWithPassword(url,user1,HttpURLConnection.HTTP_OK,true,false,null,true);

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
                    Log.d("new objective",abstractSpawn.getName());
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

    public static Boolean changeSpells(ArrayList<AbstractSpell> active, ArrayList<AbstractSpell> unused){
        if (active.equals(animal1.getActiveSpells()) && unused.equals(animal1.getUnusedSpells())){
            return  true;
        }
        String uri = "http://" + server.getIpAddress() + ":" + Integer.toString(server.getPort()) + "/api/" + "users/change_spells";
        try {
            URL url = new URL(uri);
            JSONObject json = new JSONObject();
            JSONArray activeJson = new JSONArray();
            for (AbstractSpell spell : active){
                activeJson.put(spell.toJson());
            }
            json.put("Active",activeJson);

            JSONArray unusedJson = new JSONArray();
            for (AbstractSpell spell: unused){
                unusedJson.put(spell.toJson());
            }
            json.put("Unused", unusedJson);
            JSONObject returnJson = server.connectWithAuth(url, user, HttpURLConnection.HTTP_OK, false, true, json);
            if (returnJson == null){
                return false;
            }

        }  catch (JSONException | IOException e) {
            e.printStackTrace();
            return false;
        }
        animal1.setActiveSpells(active);
        animal1.setUnusedSpells(unused);
        return true;

    }

    public static ArrayList<AbstractSpell> getSpells(ArrayList<Integer> ids, int type){
        String uri = "http://" + server.getIpAddress() + ":" + Integer.toString(server.getPort()) + "/api/" + "spells/type/"  + Integer.toString(type) ;
        try {
            URL url = new URL(uri);

            JSONArray spellIds = new JSONArray(ids);
            JSONObject json = new JSONObject();
            json.put("Ids",spellIds);

            JSONObject returnJson = server.connectWithoutAuth(url, HttpURLConnection.HTTP_OK, true, true, json);

            Log.d("inputJson", returnJson.toString());


            if (returnJson != null) {

                JSONObject inputJson = returnJson.getJSONObject("json");
                Log.d("inputJson", inputJson.toString());
                JSONArray array = inputJson.getJSONArray("Spells");
                ArrayList<AbstractSpell> spells = new ArrayList<>();
                AbstractSpell spell;
                for (int i = 0; i < array.length();i++) {
                    JSONObject spellJson = array.getJSONObject(i);
                    switch (spellJson.getInt("Type")) {
                        case AbstractSpell.DAMAGE:
                            Log.e("Damage",spellJson.toString());
                            spell = new DamageSpell(spellJson);
                            break;
                        case AbstractSpell.HEAL:
                            spell = new HealSpell(spellJson);
                            break;
                        case AbstractSpell.BUFF:
                            spell = new BuffSpell(spellJson);
                            break;
                        case AbstractSpell.DEBUFF:
                            spell = new DebuffSpell(spellJson);
                            break;
                        default:
                            spell = new DamageSpell(spellJson);

                    }
                    spells.add(spell);
                }
                return spells;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean getMonsters(){
        String uri = "http://" + server.getIpAddress() + ":" + Integer.toString(server.getPort()) + "/api/monsters" ;
        try {
            URL url = new URL(uri);

            JSONObject returnJson = server.connectWithoutAuth(url, HttpURLConnection.HTTP_OK, true, false, null);

            if (returnJson != null) {
                JSONObject inputJson = returnJson.getJSONObject("json");
                Log.d("Monsters", inputJson.toString());
                JSONArray array = inputJson.getJSONArray("Monsters");
                monsters = new ArrayList<>();
                for (int i = 0; i < array.length();i++) {
                    JSONObject jSonEnemy = array.getJSONObject(i);
                    Monster monster = new Monster(jSonEnemy);
                    monsters.add(monster);
                }
                return true;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static ArrayList<AbstractSpawn> get_spawns(){
        String uri = "http://" + server.getIpAddress() + ":" + Integer.toString(server.getPort()) + "/api/" + "spawns";
        try {
            URL url = new URL(uri);


            JSONObject jsonObject = server.connectWithoutAuth(url, HttpURLConnection.HTTP_OK, true, false, null);

            ArrayList<AbstractSpawn> spawnList = new ArrayList<AbstractSpawn>();
            JSONObject jsonObject1 = jsonObject.getJSONObject("json");
            JSONArray array = jsonObject1.getJSONArray("Spawns");
            for (int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                AbstractSpawn abstractSpawn;
                if (object.getString("Type").equals("Spawn")) {
                    abstractSpawn = new Spawn(object);
                }
                else if (object.getString("Type").equals("Dungeon")){
                    abstractSpawn = new Dungeon(object);
                }
                else {
                    abstractSpawn = new Treasure(object);
                }
                spawnList.add(abstractSpawn);
            }
            return spawnList;

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Boolean retrieveMailsFromServer(){
        String uri = "http://" + server.getIpAddress() + ":" + Integer.toString(server.getPort()) + "/api/" + "mails";
        try {

            URL url = new URL(uri);
            JSONObject returnJson = server.connectWithAuth(url, user, HttpURLConnection.HTTP_OK, true, false, null);

            if (returnJson != null){
                mails = new ArrayList<>();
                Log.d("Mails get",returnJson.toString());
                if (returnJson.getJSONObject("json").get("Mails") != null) {

                    JSONArray mailsArray = returnJson.getJSONObject("json").getJSONArray("Mails");
                    Log.d("Mail Array",Integer.toString(mailsArray.length()));
                    for (int i = 0; i < mailsArray.length(); i++) {
                        JSONObject object = mailsArray.getJSONObject(i);
                        Mail mail;
                        if (object.getString("Type").equals("Mail")){
                            mail = new Mail(object);
                        }
                        else {
                            mail = new MailWanted(object);
                        }
                        mails.add(mail);
                        Log.d("Mail:",mail.toJson().toString());
                    }
                    return true;

                }

            }

        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
            mails = new ArrayList<>();
        }
        return false;
    }

    public static Boolean sendMail(String receiver, String object, String content){
        String uri = "http://" + server.getIpAddress() + ":" + Integer.toString(server.getPort()) + "/api/" + "mails/new";
        try {
            URL url = new URL(uri);
            JSONObject json = new JSONObject();
            json.put("To",receiver);
            json.put("Object",object);
            json.put("Content",content);
            json.put("Timestamp",System.currentTimeMillis());
            json.put("Type","Mail");
            JSONObject returnJson = server.connectWithAuth(url, user, HttpURLConnection.HTTP_OK, false, true, json);

            if (returnJson != null){
                return true;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean saveMails(){
        String uri = "http://" + server.getIpAddress() + ":" + Integer.toString(server.getPort()) + "/api/" + "mails/save";
        try {
            URL url = new URL(uri);
            JSONObject json = new JSONObject();
            JSONArray mailsJson = new JSONArray();
            for (Mail m:mails){
                mailsJson.put(m.toJson());
            }
            json.put("Mails",mailsJson);
            JSONObject returnJson = server.connectWithAuth(url, user, HttpURLConnection.HTTP_OK, true, true, json);

            if (returnJson != null){
                ArrayList<Mail> mailsTemp = new ArrayList<>();
                Log.d("Mails get",returnJson.toString());
                if (returnJson.getJSONObject("json").get("Mails") != null) {

                    JSONArray mailsArray = returnJson.getJSONObject("json").getJSONArray("Mails");
                    Log.d("Mail Array",Integer.toString(mailsArray.length()));
                    for (int i = 0; i < mailsArray.length(); i++) {
                        JSONObject object = mailsArray.getJSONObject(i);
                        Mail mail;
                        if (object.getString("Type").equals("Mail")){
                            mail = new Mail(object);
                        }
                        else {
                            mail = new MailWanted(object);
                        }
                        mailsTemp.add(mail);
                        Log.d("Mail:",mail.toJson().toString());
                    }
                    mails = mailsTemp;
                    return true;

                }

            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static Boolean addFriend(String name){
        String uri = "http://" + server.getIpAddress() + ":" + Integer.toString(server.getPort()) + "/api/" + "friends/add";
        try {

            URL url = new URL(uri);
            JSONObject json = new JSONObject();
            json.put("Name", name);
            JSONObject returnJson = server.connectWithAuth(url, user, HttpURLConnection.HTTP_OK, true, true, json);


            if (returnJson != null){
                JSONObject jsonObject = returnJson.getJSONObject("json");
                Log.d("Friend",returnJson.toString());
                Friend friend = new Friend(jsonObject);
                friends.add(friend);
                return true;
            }

        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        }
        return false;
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

    public static Boolean deleteUser() {
        String uri = "http://" + server.getIpAddress() + ":" + Integer.toString(server.getPort()) + "/api/" + "users/delete";
        try {
            URL url = new URL(uri);
            JSONObject returnJson = server.connectWithAuth(url, user, HttpURLConnection.HTTP_OK, true, false, null);
            if (returnJson != null){
                int ret = returnJson.getInt("return");
                return true;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return false;
    }


    //TO BE DELETED


    public static void setupBob() {
        animal1 = new Animal("Bob","rabbit1", Creature.RABBIT);

        animal2 = new Animal("BobTwin","rabbit2", Creature.RABBIT);
    }


}
