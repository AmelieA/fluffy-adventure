
package com.fluffyadventure.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;

/**
 * Created by amelie on 2/27/15.
 */
public class Mail implements Parcelable{

    private int timestamp;
    private String sender;
    private int senderId;
    private String object;
    private String content;
    private boolean read;

    public Mail(int timestamp, String sender, String object, String content, int senderId) {
        this.timestamp = timestamp;
        this.sender = sender;
        this.content = content;
        this.object = object;
        this.senderId = senderId;
        this.read = false;
    }

    public Mail(JSONObject jsonObject) throws JSONException {
        timestamp = jsonObject.getInt("Timestamp");
        sender = jsonObject.getString("Sender");
        senderId = jsonObject.getInt("SenderId");
        object = jsonObject.getString("Object");
        content = jsonObject.getString("Content");
        read = jsonObject.getBoolean("Read");
    }

    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Timestamp", timestamp);
        jsonObject.put("Sender",sender);
        jsonObject.put("SenderId",senderId);
        jsonObject.put("Object",object);
        jsonObject.put("Content",content);
        jsonObject.put("Read",read);
        jsonObject.put("Type","Mail");
        return jsonObject;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public String getObject() {
        return object;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public int getSenderId() {
        return senderId;
    }

    public Boolean getRead(){
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mail)) return false;

        Mail mail = (Mail) o;

        if (read != mail.read) return false;
        if (senderId != mail.senderId) return false;
        if (timestamp != mail.timestamp) return false;
        if (content != null ? !content.equals(mail.content) : mail.content != null) return false;
        if (!object.equals(mail.object)) return false;
        if (!sender.equals(mail.sender)) return false;

        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(timestamp);
        dest.writeString(sender);
        dest.writeString(object);
        dest.writeString(content);
        dest.writeInt(senderId);
    }

    private Mail(Parcel in){
        timestamp = in.readInt();
        sender = in.readString();
        object = in.readString();
        content = in.readString();
        senderId = in.readInt();
    }
    public static final Parcelable.Creator<Mail> CREATOR = new Parcelable.Creator<Mail>() {
        public Mail createFromParcel(Parcel in){return new Mail(in);}
        public Mail[] newArray(int size){return new Mail[size];}
    };

    public static final Comparator<Mail> mailComparator = new Comparator<Mail>() {
        @Override
        public int compare(Mail lhs, Mail rhs) {
            return rhs.getTimestamp()-lhs.getTimestamp();
        }
    };

}
