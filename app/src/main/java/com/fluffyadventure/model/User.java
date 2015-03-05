package com.fluffyadventure.model;

/**
 * Created by denis on 23/02/15.
 */
public class User {
    private String name;
    private int id;
    private String password;
    private String token;

    public User(String name, String password, int id){
        this.name = name;
        this.id = id;
        this.password = password;
        this.token = "";

    }

    public User(String name, String password){
        this.name = name;
        this.password = password;
        this.token = "";
    }

    public int getId(){
        return this.id;
    }

    public void setToken (String token) {
        this.token = token;
    }

    public String getName(){
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }

    public String getToken() {
        return this.token;
    }


}
