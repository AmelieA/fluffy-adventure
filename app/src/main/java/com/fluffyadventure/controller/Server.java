package com.fluffyadventure.controller;

import android.util.Base64;
import android.util.Log;

import com.fluffyadventure.model.AbstractSpawn;
import com.fluffyadventure.model.AbstractSpell;
import com.fluffyadventure.model.Animal;
import com.fluffyadventure.model.BuffSpell;
import com.fluffyadventure.model.Creature;
import com.fluffyadventure.model.DamageSpell;
import com.fluffyadventure.model.DebuffSpell;
import com.fluffyadventure.model.Dungeon;
import com.fluffyadventure.model.Friend;
import com.fluffyadventure.model.HealSpell;
import com.fluffyadventure.model.Mail;
import com.fluffyadventure.model.Spawn;
import com.fluffyadventure.model.Treasure;
import com.fluffyadventure.model.User;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by denis on 23/02/15.
 */

//TODO: MOVE TO CONTROLLER EVERYTHING THAT CAN BE MOVED TO CONTROLLER :
//NOT TO BE MOVED :
// JSONObject connectWithAuth(URL url, User user, int responseCode, Boolean input, Boolean output, JSONObject outputJson) throws IOException, JSONException
// JSONObject connectWithoutAuth(URL url, int responseCode, Boolean input, Boolean output, JSONObject outputJson) throws IOException, JSONException
// Boolean testConnection()
//GETOKEN
//
public class Server {
    private String ipAddress;
    private int port;



    public Server(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public String getToken(User user){
        String uri = "http://" + this.ipAddress + ":" + Integer.toString(this.port) + "/api/" + "get_token";
        try {
            URL url = new URL(uri);
            JSONObject returnJson = this.connectWithPassword(url, user, HttpURLConnection.HTTP_OK, true, false, null,false);
            JSONObject inputJson = returnJson.getJSONObject("json");
            String token = inputJson.getString("Token");
            return token;

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    public Boolean testConnection(){
        String uri = "http://" + this.ipAddress + ":" + Integer.toString(this.port) + "/api/" + "online";
        try {
            URL url = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            int httpResult = urlConnection.getResponseCode();

            if (httpResult == HttpURLConnection.HTTP_OK) {

                return true;

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;

    }

    public JSONObject connectWithAuth(URL url, User user, int responseCode, Boolean input, Boolean output, JSONObject outputJson) throws IOException, JSONException {
        //TODO: Get tokenz!
        HttpURLConnection urlConnection1 = (HttpURLConnection) url.openConnection();
        if (input) {
            urlConnection1.setDoInput(true);
            urlConnection1.setRequestProperty("Accept", "application/json");
        }


        if (output){
            urlConnection1.setDoOutput(true);
            urlConnection1.setRequestMethod("POST");
            urlConnection1.setRequestProperty("Content-Type", "application/json");
        }

        if ((user.getToken() != null) && (user.getToken() != "")){
            String encoded = Base64.encodeToString((String.format("%s:%s", user.getToken(), "unused")).getBytes(), Base64.NO_WRAP);
            urlConnection1.setRequestProperty("Authorization", String.format("Basic %s", encoded));
            if (output){
                OutputStream out = urlConnection1.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                writer.write(outputJson.toString());
                writer.flush();
                writer.close();
                out.close();
            }
            urlConnection1.connect();
            if (urlConnection1.getResponseCode() == responseCode){
                JSONObject inputJson = new JSONObject();
                if (input){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection1.getInputStream()));
                    StringBuilder inputString = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        inputString.append(line + "\n");
                    }
                    bufferedReader.close();
                    inputJson.put("json",new JSONObject(inputString.toString()));
                }
                inputJson.put("return",responseCode);
                return  inputJson;
            }
            urlConnection1.disconnect();

        }

        urlConnection1 = (HttpURLConnection) url.openConnection();
        if (input) {
            urlConnection1.setDoInput(true);
            urlConnection1.setRequestProperty("Accept", "application/json");
        }


        if (output){
            urlConnection1.setDoOutput(true);
            urlConnection1.setRequestMethod("POST");
            urlConnection1.setRequestProperty("Content-Type", "application/json");
        }


        String encoded = Base64.encodeToString((String.format("%s:%s", user.getName(), user.getPassword())).getBytes(), Base64.NO_WRAP);
        urlConnection1.setRequestProperty("Authorization", String.format("Basic %s", encoded));
        if (output){
            OutputStream out = urlConnection1.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(outputJson.toString());
            writer.flush();
            writer.close();
            out.close();
        }
        urlConnection1.connect();
        if (urlConnection1.getResponseCode() == responseCode){
            JSONObject inputJson = new JSONObject();
            if (input){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection1.getInputStream()));
                StringBuilder inputString = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    inputString.append(line + "\n");
                }
                bufferedReader.close();
                inputJson.put("json",new JSONObject(inputString.toString()));
            }
            inputJson.put("return",responseCode);
            String token = this.getToken(user);
            user.setToken(token);
            return  inputJson;
        }

        return null;

    }

    public JSONObject connectWithPassword(URL url, User user, int responseCode, Boolean input, Boolean output, JSONObject outputJson, Boolean needToken) throws IOException, JSONException {
        HttpURLConnection urlConnection1 = (HttpURLConnection) url.openConnection();

        urlConnection1 = (HttpURLConnection) url.openConnection();
        if (input) {
            urlConnection1.setDoInput(true);
            urlConnection1.setRequestProperty("Accept", "application/json");
        }


        if (output){
            urlConnection1.setDoOutput(true);
            urlConnection1.setRequestMethod("POST");
            urlConnection1.setRequestProperty("Content-Type", "application/json");
        }


        String encoded = Base64.encodeToString((String.format("%s:%s", user.getName(), user.getPassword())).getBytes(), Base64.NO_WRAP);
        urlConnection1.setRequestProperty("Authorization", String.format("Basic %s", encoded));
        if (output){
            OutputStream out = urlConnection1.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(outputJson.toString());
            writer.flush();
            writer.close();
            out.close();
        }
        urlConnection1.connect();
        if (urlConnection1.getResponseCode() == responseCode){
            JSONObject inputJson = new JSONObject();
            if (input){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection1.getInputStream()));
                StringBuilder inputString = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    inputString.append(line + "\n");
                }
                bufferedReader.close();
                inputJson.put("json",new JSONObject(inputString.toString()));
            }
            inputJson.put("return",responseCode);
            if (needToken){
                String token = this.getToken(user);
                user.setToken(token);
            }
            return  inputJson;
        }

        return null;

    }

    public JSONObject connectWithoutAuth(URL url, int responseCode, Boolean input, Boolean output, JSONObject outputJson) throws IOException, JSONException {
        //TODO: Get tokenz!
        HttpURLConnection urlConnection1 = (HttpURLConnection) url.openConnection();
        if (input) {
            urlConnection1.setDoInput(true);
            urlConnection1.setRequestProperty("Accept", "application/json");
        }


        if (output){
            urlConnection1.setDoOutput(true);
            urlConnection1.setRequestMethod("POST");
            urlConnection1.setRequestProperty("Content-Type", "application/json");

            OutputStream out = urlConnection1.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(outputJson.toString());
            writer.flush();
            writer.close();
            out.close();
        }
        urlConnection1.connect();
        if (urlConnection1.getResponseCode() == responseCode){
            JSONObject inputJson = new JSONObject();
            if (input){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection1.getInputStream()));
                StringBuilder inputString = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    inputString.append(line + "\n");
                }
                bufferedReader.close();
                inputJson.put("json",new JSONObject(inputString.toString()));
            }
            inputJson.put("return",responseCode);
            return  inputJson;
        }
        urlConnection1.disconnect();





        return null;

    }



    //TODO Delete stuff below
    public AbstractSpell getSpell(int id, int type){
        String uri = "http://" + this.ipAddress + ":" + Integer.toString(this.port) + "/api/" + "spells/"  + Integer.toString(type) + "/"+ Integer.toString(id);
        try {
            URL url = new URL(uri);

            JSONObject returnJson = this.connectWithoutAuth(url, HttpURLConnection.HTTP_OK, true, false, null);

            Log.d("inputJson", returnJson.toString());


            if (returnJson != null) {

                JSONObject inputJson = returnJson.getJSONObject("json");
                Log.d("inputJson", inputJson.toString());
                AbstractSpell spell;
                switch (inputJson.getInt("Type")){
                    case AbstractSpell.DAMAGE:
                        spell = new DamageSpell(inputJson);
                        break;
                    case AbstractSpell.HEAL:
                        spell = new HealSpell(inputJson);
                        break;
                    case AbstractSpell.BUFF:
                        spell = new BuffSpell(inputJson);
                        break;
                    case AbstractSpell.DEBUFF:
                        spell = new DebuffSpell(inputJson);
                        break;
                    default:
                        spell = new DamageSpell(inputJson);

                }
                return spell;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User createUser(String name, String password) {
        String uri = "http://" + this.ipAddress + ":" + Integer.toString(this.port) + "/api/" + "users/new";
        try {
            URL url = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


            //construction du header
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("Content-Type", "application/json");


            //construction du json
            OutputStream out = urlConnection.getOutputStream();
            JSONObject json = new JSONObject();
            json.put("username", name);
            json.put("password", password);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(json.toString());
            writer.flush();
            writer.close();
            out.close();


            //envoi des données
            urlConnection.connect();

            int httpResult = urlConnection.getResponseCode();

            if (httpResult == HttpURLConnection.HTTP_CREATED) {
                //utilisateur créé sur le server
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder inputString = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    inputString.append(line + "\n");
                }
                bufferedReader.close();

                JSONObject inputJson = new JSONObject(inputString.toString());
                User user = new User(name, password, inputJson.getInt("Id"));

                String token = this.getToken(user);
                user.setToken(token);
                return user;

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    public Boolean moveHQ(User user, double latitude, double longitude){
        String uri = "http://" + this.ipAddress + ":" + Integer.toString(this.port) + "/api/" + "users/move_HQ";
        try {

            URL url = new URL(uri);
            JSONObject json = new JSONObject();
            json.put("Latitude",latitude);
            json.put("Longitude",longitude);
            JSONObject returnJson = connectWithAuth(url, user, HttpURLConnection.HTTP_OK, true, true, json);

            if (returnJson != null){

                return true;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public Animal createAnimal(User user, Animal in_animal, String name) {
        Animal animal = new Animal(in_animal);
        String uri = "http://" + this.ipAddress + ":" + Integer.toString(this.port) + "/api/" + "users/add_animal";
        try {

            ArrayList<Integer> ids = new ArrayList<>();
            ids.add(0);
            ids.add(1);
            ids.add(2);
            ids.add(3);
            for (AbstractSpell spell : this.getSpells(ids,animal.getType())){
                animal.addSpell(spell, true);
            }
            animal.setName(name);

            URL url = new URL(uri);
            JSONObject json = animal.toJson();
            JSONObject returnJson = connectWithAuth(url, user, HttpURLConnection.HTTP_OK, true, true, json);

            if (returnJson != null){
                return animal;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;


    }



    public User login(String name, String password) {
        String uri = "http://" + this.ipAddress + ":" + Integer.toString(this.port) + "/api/" + "login";
        try {
            URL url = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Accept", "application/json");


            String encoded = Base64.encodeToString((String.format("%s:%s", name, password)).getBytes(), Base64.NO_WRAP);
            urlConnection.setRequestProperty("Authorization", String.format("Basic %s", encoded));

            urlConnection.connect();

            int httpResult = urlConnection.getResponseCode();

            if (httpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder inputString = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    inputString.append(line + "\n");
                }
                bufferedReader.close();

                JSONObject inputJson = new JSONObject(inputString.toString());
                User user = new User(name, password, inputJson.getInt("Id"));

                String token = this.getToken(user);
                user.setToken(token);
                return user;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    public Animal getAnimal(User user) {
        String uri = "http://" + this.ipAddress + ":" + Integer.toString(this.port) + "/api/" + "users/get_animal";
        try {
            URL url = new URL(uri);
            JSONObject returnJson = connectWithAuth(url, user, HttpURLConnection.HTTP_OK, true, false, null);
            if (returnJson != null){
                JSONObject animalJson = returnJson.getJSONObject("json");
                Log.d("Animal FROM SERVER",animalJson.toString());
                Animal animal = new Animal(animalJson);
                return animal;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Boolean deleteUser(User user) {
        String uri = "http://" + this.ipAddress + ":" + Integer.toString(this.port) + "/api/" + "users/delete";
        try {
            URL url = new URL(uri);
            JSONObject returnJson = connectWithAuth(url, user, HttpURLConnection.HTTP_OK, true, false, null);
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

    public Boolean changeSpells(User user, ArrayList<AbstractSpell> active, ArrayList<AbstractSpell> unused){
        String uri = "http://" + this.ipAddress + ":" + Integer.toString(this.port) + "/api/" + "users/change_spells";
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
            JSONObject returnJson = connectWithAuth(url, user, HttpURLConnection.HTTP_OK, false, true, json);

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


    public ArrayList<AbstractSpell> getSpells(ArrayList<Integer> ids, int type){
        String uri = "http://" + this.ipAddress + ":" + Integer.toString(this.port) + "/api/" + "spells/type/"  + Integer.toString(type) ;
        try {
            URL url = new URL(uri);

            JSONArray spellIds = new JSONArray(ids);
            JSONObject json = new JSONObject();
            json.put("Ids",spellIds);

            JSONObject returnJson = this.connectWithoutAuth(url, HttpURLConnection.HTTP_OK, true, true, json);

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

    public ArrayList<AbstractSpawn> get_spawns(){
        String uri = "http://" + this.ipAddress + ":" + Integer.toString(this.port) + "/api/" + "spawns";
        try {
            URL url = new URL(uri);


            JSONObject jsonObject = this.connectWithoutAuth(url,HttpURLConnection.HTTP_OK,true,false,null);

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


    public Boolean getUser(String username){
        String uri = "http://" + this.ipAddress + ":" + Integer.toString(this.port) + "/api/" + "users/search/"+username;
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

    public LatLng getHQ(User user) {
        String uri = "http://" + this.ipAddress + ":" + Integer.toString(this.port) + "/api/" + "users/get_HQ";
        try {
            URL url = new URL(uri);
            JSONObject returnJson = connectWithAuth(url, user, HttpURLConnection.HTTP_OK, true, false, null);
            if (returnJson != null){
                JSONObject HQJson = returnJson.getJSONObject("json");
                LatLng hq = new LatLng(HQJson.getDouble("Latitude"),HQJson.getDouble("Longitude"));
                return hq;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Friend addFriend(User user, String friendName) {
        String uri = "http://" + this.ipAddress + ":" + Integer.toString(this.port) + "/api/" + "friends/add";
        try {

            URL url = new URL(uri);
            JSONObject json = new JSONObject();
            json.put("Name", friendName);
            JSONObject returnJson = connectWithAuth(url, user, HttpURLConnection.HTTP_OK, true, true, json);


            if (returnJson != null) {
                JSONObject jsonObject = returnJson.getJSONObject("json");
                Log.d("Friend", returnJson.toString());
                Friend friend = new Friend(jsonObject);
                return friend;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;


    }

    public ArrayList<Friend> getFriends (User user){
        String uri = "http://" + this.ipAddress + ":" + Integer.toString(this.port) + "/api/" + "friends";
        try {

            URL url = new URL(uri);
            JSONObject returnJson = connectWithAuth(url, user, HttpURLConnection.HTTP_OK, true, false, null);

            if (returnJson != null){
                ArrayList<Friend> friends = new ArrayList<>();
                Log.d("Friends get",returnJson.toString());
                JSONArray friendsArray = returnJson.getJSONObject("json").getJSONArray("Friends");
                for (int i = 0; i < friendsArray.length(); i++){
                    Friend friend = new Friend(friendsArray.getJSONObject(i));
                    friends.add(friend);
                }
                return friends;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;


    }

    public Boolean sendMail(User user, String receiver, String object, String content){
        String uri = "http://" + this.ipAddress + ":" + Integer.toString(this.port) + "/api/" + "mails/new";
        try {
            URL url = new URL(uri);
            JSONObject json = new JSONObject();
            json.put("To",receiver);
            json.put("Object",object);
            json.put("Content",content);
            json.put("Timestamp",System.currentTimeMillis());
            JSONObject returnJson = connectWithAuth(url, user, HttpURLConnection.HTTP_OK, false, true, json);

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


    public ArrayList<Mail> getMails(User user){
        String uri = "http://" + this.ipAddress + ":" + Integer.toString(this.port) + "/api/" + "mails";
        try {

            URL url = new URL(uri);
            JSONObject returnJson = connectWithAuth(url, user, HttpURLConnection.HTTP_OK, true, false, null);

            if (returnJson != null){
                ArrayList<Mail> mails = new ArrayList<>();
                Log.d("Mails get",returnJson.toString());
                if (returnJson.getJSONObject("json").get("Mails") != null) {

                    JSONArray mailsArray = returnJson.getJSONObject("json").getJSONArray("Mails");
                    Log.d("Mail Array",Integer.toString(mailsArray.length()));
                    for (int i = 0; i < mailsArray.length(); i++) {
                        Mail mail = new Mail(mailsArray.getJSONObject(i));
                        mails.add(mail);
                        Log.d("Mail:",mail.toJson().toString());
                    }

                }
                return mails;

            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException ex) {
            ex.printStackTrace();
            ArrayList<Mail> mails = new ArrayList<>();
            return mails;
        }
        return null;
    }


}














