package com.fluffyadventure.model;

/**
 * Created by amelie on 2/27/15.
 */
public class Mail {

    private int id;
    private String header;
    private String message;

    public Mail(int id, String header, String message) {
        this.id = id;
        this.header = header;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public String getHeader() {
        return header;
    }

    public String getMessage() {
        return message;
    }
}
