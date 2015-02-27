package com.fluffyadventure.controller;

import android.util.Base64;

import com.fluffyadventure.model.Animal;
import com.fluffyadventure.model.Spell;
import com.fluffyadventure.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

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

    public Animal createAnimal(User user, Animal in_animal, String name) {
        Animal animal = new Animal(in_animal);
        String uri = "http://" + this.ipAddress + ":" + Integer.toString(this.port) + "/api/" + "users/add_animal";
        try {
            int spell_id;
            switch (animal.getType()){
                case "Squirrel" :
                    spell_id = 1;
                    break;

                case "Rabbit" :
                    spell_id = 2;
                    break;

                case "Sheep" :
                    spell_id = 3;
                    break;

                default:
                    spell_id = 2;
                    return  null;
            }
            Spell spell = this.get_spell(spell_id);
            animal.addSpell(spell, true);
            animal.setName(name);


            URL url = new URL(uri);
            HttpURLConnection urlConnection1 = (HttpURLConnection) url.openConnection();
            HttpURLConnection urlConnection2 = (HttpURLConnection) url.openConnection();

            urlConnection1.setDoOutput(true);
            urlConnection1.setDoInput(true);
            urlConnection1.setRequestMethod("POST");
            urlConnection1.setRequestProperty("Accept", "application/json");
            urlConnection1.setRequestProperty("Content-Type", "application/json");


            urlConnection2.setDoOutput(true);
            urlConnection2.setDoInput(true);
            urlConnection2.setRequestMethod("POST");
            urlConnection2.setRequestProperty("Accept", "application/json");
            urlConnection2.setRequestProperty("Content-Type", "application/json");
            String encoded1;

            if (user.getToken() != null)
            {
                encoded1 = Base64.encodeToString((String.format("%s:%s", user.getToken(), "unused")).getBytes(), Base64.NO_WRAP);


            }
            else {
                encoded1 = Base64.encodeToString((String.format("%s:%s", user.getName(), user.getPassword())).getBytes(), Base64.NO_WRAP);
            }
            urlConnection1.setRequestProperty("Authorization", String.format("Basic %s", encoded1));
            OutputStream out1 = urlConnection1.getOutputStream();
            JSONObject json = animal.toJson();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out1, "UTF-8"));
            writer.write(json.toString());
            writer.flush();
            writer.close();
            out1.close();

            urlConnection1.connect();

            int httpResult = urlConnection1.getResponseCode();
            if (httpResult != HttpURLConnection.HTTP_OK && user.getToken() != null)
            {

                String encoded2 = Base64.encodeToString((String.format("%s:%s", user.getName(), user.getPassword())).getBytes(), Base64.NO_WRAP);
                urlConnection2.setRequestProperty("Authorization", String.format("Basic %s", encoded2));
                out1 = urlConnection2.getOutputStream();
                writer = new BufferedWriter(new OutputStreamWriter(out1, "UTF-8"));
                writer.write(json.toString());
                writer.flush();
                writer.close();
                out1.close();

                urlConnection2.connect();
                httpResult = urlConnection2.getResponseCode();

            }

            if (httpResult == HttpURLConnection.HTTP_OK) {

                return animal;

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;


        }
    private HttpURLConnection connectWithAuth(HttpURLConnection urlConnection, User user, int responseCode) throws IOException {
        //TODO: REMOVE DAT SHIT
        HttpURLConnection urlConnection1 = (HttpURLConnection) urlConnection;
        urlConnection1.disconnect();
        HttpURLConnection urlConnection2 = (HttpURLConnection) urlConnection;
        urlConnection2.disconnect();
        String encoded;
        if (user.getToken() != null)
        {
            encoded = Base64.encodeToString((String.format("%s:%s", user.getToken(), "unused")).getBytes(), Base64.NO_WRAP);
            urlConnection1.setRequestProperty("Authorization", String.format("Basic %s", encoded));
            urlConnection1.connect();
            if (urlConnection1.getResponseCode() == responseCode){
                return urlConnection1;
            }
        }
        encoded = Base64.encodeToString((String.format("%s:%s", user.getName(), user.getPassword())).getBytes(), Base64.NO_WRAP);
        urlConnection2.setRequestProperty("Authorization", String.format("Basic %s", encoded));
        urlConnection2.connect();
        return urlConnection2;

    }
    public Spell get_spell(int id){
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

}














