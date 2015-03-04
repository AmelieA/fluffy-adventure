package com.fluffyadventure.controller;

import android.util.Base64;
import android.util.Log;

import com.fluffyadventure.model.AbstractSpawn;
import com.fluffyadventure.model.Animal;
import com.fluffyadventure.model.Creature;
import com.fluffyadventure.model.Dungeon;
import com.fluffyadventure.model.Spawn;
import com.fluffyadventure.model.Spell;
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
public class Server {
    private String ipAddress;
    private int port;



    public Server(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
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
            System.out.println(json.toString());
            System.out.println("yy");
            //System.out.println(out.toString());
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
                System.out.println(inputJson.toString());
                User user = new User(name, password, inputJson.getInt("Id"));
                return user;

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
                System.out.println(inputJson.toString());
                User user = new User(name, password, inputJson.getInt("Id"));
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



    public Animal createAnimal(User user, Animal in_animal, String name) {
        Animal animal = new Animal(in_animal);
        String uri = "http://" + this.ipAddress + ":" + Integer.toString(this.port) + "/api/" + "users/add_animal";
        try {
            int spell_id;
            switch (animal.getType()){
                case Creature.SQUIRREL :
                    spell_id = 1;
                    break;

                case Creature.RABBIT :
                    spell_id = 2;
                    break;

                case Creature.SHEEP :
                    spell_id = 3;
                    break;

                default:
                    spell_id = 2;
                    return  null;
            }
            Spell spell = this.getSpell(spell_id);
            animal.addSpell(spell, true);
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
    private JSONObject connectWithAuth(URL url, User user, int responseCode, Boolean input, Boolean output, JSONObject outputJson) throws IOException, JSONException {
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
            return  inputJson;
        }

        return null;

    }

    public String getToken(User user){
        String uri = "http://" + this.ipAddress + ":" + Integer.toString(this.port) + "/api/" + "get_token";
        try {
            URL url = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Accept", "application/json");


            String encoded = Base64.encodeToString((String.format("%s:%s", user.getName(), user.getPassword())).getBytes(), Base64.NO_WRAP);
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
                System.out.println(inputJson.toString());
                String token = inputJson.getString("Token");
                return token;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }


    public Boolean changeSpells(User user, ArrayList<Spell> active, ArrayList<Spell> unused){
        String uri = "http://" + this.ipAddress + ":" + Integer.toString(this.port) + "/api/" + "users/change_spells";
        try {
            URL url = new URL(uri);
            JSONObject json = new JSONObject();
            JSONArray activeJson = new JSONArray();
            for (Spell spell : active){
                activeJson.put(spell.toJson().toString());
            }
            json.put("Active",activeJson);

            JSONArray unusedJson = new JSONArray();
            for (Spell spell: unused){
                unusedJson.put(spell.toJson().toString());
            }

            json.put("Unused", unused);
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

    public Spell getSpell(int id){
        String uri = "http://" + this.ipAddress + ":" + Integer.toString(this.port) + "/api/" + "spells/"  + Integer.toString(id);
        try {
            URL url = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Accept", "application/json");

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
                System.out.println(inputJson.toString());
                Spell spell = new Spell(inputJson);
                return spell;
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
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Accept", "application/json");

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
                ArrayList<AbstractSpawn> spawnList = new ArrayList<AbstractSpawn>();
                Log.d("input", inputString.toString());

                //JSONObject inputJson = new JSONObject(inputString.toString());
                JSONArray array = new JSONArray(inputString.toString());
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
                //System.out.println(inputJson.toString());
                return spawnList;
            }
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
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
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


    

}














