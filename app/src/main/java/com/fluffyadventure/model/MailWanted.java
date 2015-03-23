package com.fluffyadventure.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jérémy on 11/03/2015.
 */
public class MailWanted extends Mail{

    private String animal1Name;
    private String animal2Name;
    private String animal1Pic;
    private String animal2Pic;
    private double latitude;
    private double longitude;
    private String enemyName;
    private Animal animal1;
    private Animal animal2;
    private Boolean solo;




    public MailWanted(int id, String sender, String object, String message, int senderId) {
        super(id, sender, object, message, senderId);
    }

    public MailWanted(int id, String sender, String object, String message, int senderId, String animal1Name, String animal2Name, String animal1Pic, String animal2Pic) {
        super(id, sender, object, message, senderId);
        this.animal1Name = animal1Name;
        this.animal2Name = animal2Name;
        this.animal1Pic = animal1Pic;
        this.animal2Pic = animal2Pic;
    }

    public MailWanted (JSONObject jsonObject) throws JSONException {
        super (jsonObject);
        animal1 = new Animal(jsonObject.getJSONObject("Animal1"));
        animal2 = new Animal(jsonObject.getJSONObject("Animal2"));
        animal1Name = animal1.getName();
        animal2Name = animal2.getName();
        animal1Pic = animal1.getImagePath();
        animal2Pic = animal2.getImagePath();
        latitude = jsonObject.getJSONObject("Location").getDouble("Latitude");
        longitude = jsonObject.getJSONObject("Location").getDouble("Longitude");
        enemyName = jsonObject.getString("Name");
        solo = jsonObject.getBoolean("Solo");

    }
    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = super.toJson();
        jsonObject.put("Type","Wanted");
        jsonObject.put("Animal1",animal1.toJson());
        jsonObject.put("Animal2",animal2.toJson());
        JSONObject location = new JSONObject();
        location.put("Latitude", latitude);
        location.put("Longitude", longitude);
        jsonObject.put("Location",location);
        jsonObject.put("Name",enemyName);
        jsonObject.put("Solo",solo);
        return jsonObject;
    }

    public String getAnimal1Name() {
        return animal1Name;
    }

    public String getAnimal2Name() {
        return animal2Name;
    }

    public String getAnimal1Pic() {
        return animal1Pic;
    }

    public String getAnimal2Pic() {
        return animal2Pic;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(animal1Name);
        dest.writeString(animal1Pic);
        dest.writeString(animal2Name);
        dest.writeString(animal2Pic);
    }

    private MailWanted(Parcel in){
        super(in.readInt(),in.readString(),in.readString(),in.readString(),in.readInt()); //id, sender, object, content, senderId
        animal1Name = in.readString();
        animal1Pic = in.readString();
        animal2Name = in.readString();
        animal2Pic = in.readString();
    }
    public static final Parcelable.Creator<Mail> CREATOR = new Parcelable.Creator<Mail>() {
        public MailWanted createFromParcel(Parcel in){return new MailWanted(in);}
        public MailWanted[] newArray(int size){return new MailWanted[size];}
    };
}
