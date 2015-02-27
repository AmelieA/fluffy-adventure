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

    public Animal createAnimal(User user, Animal animal) {
        String uri = "http://" + this.ipAddress + ":" + Integer.toString(this.port) + "/api/" + "users/add_animal";
        try {
            int spell_id;
            switch (animal.getType()){
                case "Squirrel" :
                    spell_id =1;
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


            URL url = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("Content-Type", "application/json");



            OutputStream out = urlConnection.getOutputStream();


            JSONObject json = animal.toJson();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(json.toString());
            System.out.println(json.toString());
            System.out.println("yy");
            //System.out.println(out.toString());
            writer.flush();
            writer.close();
            out.close();

            urlConnection = connectWithAuth(urlConnection, user, HttpURLConnection.HTTP_OK);

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
                //User user = new User(name, password, inputJson.getInt("Id"));
                //return user;

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;


        }
    private HttpURLConnection connectWithAuth(HttpURLConnection urlConnection, User user, int responseCode) throws IOException {
        String encoded;
        if (user.getToken() != "")
        {
            encoded = Base64.encodeToString((String.format("%s:%s", user.getToken(), "unused")).getBytes(), Base64.NO_WRAP);
            urlConnection.setRequestProperty("Authorization", String.format("Basic %s", encoded));
            urlConnection.connect();
            if (urlConnection.getResponseCode() == responseCode){
                return urlConnection;
            }
        }
        encoded = Base64.encodeToString((String.format("%s:%s", user.getName(), user.getPassword())).getBytes(), Base64.NO_WRAP);
        urlConnection.setRequestProperty("Authorization", String.format("Basic %s", encoded));
        urlConnection.connect();
        return urlConnection;

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














