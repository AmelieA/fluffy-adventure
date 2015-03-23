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


    /**
     * Constructor for Server Class
     * @param ipAddress   of the server
     * @param port of the server
     *
     */
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


    /**
     * Create an identification token for authentication purposes
     * @param user user to get token for
     * @return token
     */
    public String getToken(User user){

        String uri = "http://" + this.ipAddress + ":" + Integer.toString(this.port) + "/api/" + "get_token";
        try {
            URL url = new URL(uri);
            JSONObject returnJson = this.connectWithPassword(url, user, HttpURLConnection.HTTP_OK, true, false, null,false);
            JSONObject inputJson = returnJson.getJSONObject("json");
            String token = inputJson.getString("Token");
            return token;

        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        }
        return null;

    }

    /**
     * Test if server is online
     * @return true if server is online
     */
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

    /**
     * Connect to a given url using authentication
     * @param user user to get token for.
     * @param url to be accessed
     * @param responseCode valid http response code
     * @param input set to true if you are supposed to receive something from server
     * @param output set to true if you want to send a JSON to server
     * @param outputJson the JSON you want to send
     * @return token
     */
    public JSONObject connectWithAuth(URL url, User user, int responseCode, Boolean input, Boolean output, JSONObject outputJson) throws IOException, JSONException {
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


        //first try : with token
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

        //second try : with password
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

    /**
     * Connect to a given url using password only authentication
     * @param user user to get token for.
     * @param url to be accessed
     * @param responseCode valid http response code
     * @param input set to true if you are supposed to receive something from server
     * @param output set to true if you want to send a JSON to server
     * @param outputJson the JSON you want to send
     * @param needToken true if you want to get a token for your user
     * @return token
     */
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

    /**
     * Connect to a given url
     * @param url to be accessed
     * @param responseCode valid http response code
     * @param input set to true if you are supposed to receive something from server
     * @param output set to true if you want to send a JSON to server
     * @param outputJson the JSON you want to send
     * @return token
     */
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


}














