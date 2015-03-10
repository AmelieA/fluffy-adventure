
package com.fluffyadventure.model;

/**
 * Created by amelie on 2/27/15.
 */
public class Mail {

    private int id;
    private String sender;
    private int senderId;
    private String object;
    private String message;

    public Mail(int id, String sender, String object, String message, int senderId) {
        this.id = id;
        this.sender = sender;
        this.message = message;
        this.object = object;
        this.senderId = senderId;
    }

    public int getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }
}
