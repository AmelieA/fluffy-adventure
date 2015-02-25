package com.fluffyadventure.controller;

import android.util.Base64;

import com.fluffyadventure.model.User;
import com.google.android.gms.analytics.i;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

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

    public Server() {
        this.ipAddress = "thulin.fr";
        this.port = 5000;
    }

    public User createUser(String name, String password) {
        String uri = "http://" + this.ipAddress + ":" + Integer.toString(this.port) + "/api/" + "users/new";
        try {
            URL url = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("Content-Type", "application/json");


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

            urlConnection.connect();

            int httpResult = urlConnection.getResponseCode();

            if (httpResult == HttpURLConnection.HTTP_CREATED) {
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


}














