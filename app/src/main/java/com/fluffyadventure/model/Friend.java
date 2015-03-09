package com.fluffyadventure.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jérémy on 04/03/2015.
 */
public class Friend implements Parcelable {
    private String name;
    private String image;

    public Friend(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
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
