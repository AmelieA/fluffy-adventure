
package com.fluffyadventure.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by amelie on 2/27/15.
 */
public class Mail implements Parcelable{

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

    public int getSenderId() {
        return senderId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(sender);
        dest.writeInt(senderId);
        dest.writeString(object);
        dest.writeString(message);
    }

    private Mail(Parcel in){
        id = in.readInt();
        sender = in.readString();
        senderId = in.readInt();
        object = in.readString();
        message = in.readString();
    }
    public static final Parcelable.Creator<Mail> CREATOR = new Parcelable.Creator<Mail>() {
        public Mail createFromParcel(Parcel in){return new Mail(in);}
        public Mail[] newArray(int size){return new Mail[size];}
    };

}
