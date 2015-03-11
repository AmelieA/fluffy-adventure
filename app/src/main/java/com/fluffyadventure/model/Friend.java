package com.fluffyadventure.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jérémy on 04/03/2015.
 */
public class Friend implements Parcelable {
    private String name;
    private int id;
    private String image;

    public Friend(String name, String image, int id) {
        this.name = name;
        this.image = image;
        this.id = id;
    }
    public Friend(JSONObject jsonObject) throws JSONException {
        this.name = jsonObject.getString("Name");
        this.id = jsonObject.getInt("Id");
        this.image = jsonObject.getString("Img");

    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public int getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(image);
    }

    public static final Parcelable.Creator<Friend> CREATOR = new Parcelable.Creator<Friend>() {
        public Friend createFromParcel(Parcel in) {
            return new Friend(in);
        }

        public Friend[] newArray(int size) {
            return new Friend[size];
        }
    };
    private Friend(Parcel in) {
        name = in.readString();
        image = in.readString();
    }
}
