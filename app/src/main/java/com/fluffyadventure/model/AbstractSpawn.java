package com.fluffyadventure.model;

import android.location.Location;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Johan on 18/02/2015.
 */
public abstract class AbstractSpawn {

    public final class Status {
        public static final String DONE = "done";
        public static final String AVAILABLE = "available";
        public static final String REQUIREMENT_NOT_MET= "requirement not met";
        //public static final String COMPETENCES_INSUFFISANTES= "competences insuffisantes";
    }

    protected Integer spawnId;

    public Integer requirement = null;

    public String name;
    public String text;
    public int level;

    public double latitude;
    public double longitude;
    private String icon;

    public int strengthReward;
    public int healthReward;
    public int spellReward;


    public AbstractSpawn(){
    }

    public AbstractSpawn(String name, String type){
        this.spawnId = -1;
        this.spellReward = 0;
        this.healthReward = 0;
        this.strengthReward = 0;
        this.longitude = 0;
        this.latitude = 0;
        this.text = "";
        if (type.equals("Rabbit")) {
            this.name = "Terrier de " + name;
        } else if (type.equals("Squirrel")) {
            this.name = "Nid de " + name;
        } else if (type.equals("Sheep")) {
            this.name = "Bergerie de " + name;
        }
        this.level = 0;
    }

    protected AbstractSpawn( Integer spawnId, int spellReward, int healthReward, int strengthReward, double latitude, double longitude, String text, String name, Integer level) {
        this.spellReward = spellReward;
        this.healthReward = healthReward;
        this.strengthReward = strengthReward;
        this.longitude = longitude;
        this.latitude = latitude;
        this.text = text;
        this.name = name;
        this.spawnId = spawnId;
        this.level = level;
    }

    protected AbstractSpawn(JSONObject json) throws JSONException {
        this.spellReward = json.getInt("SpellReward");
        this.healthReward = json.getInt("HealthReward");
        this.strengthReward = json.getInt("StrengthReward");
        this.text = json.getString("Text");
        this.name = json.getString("Name");
        this.spawnId = json.getInt("Id");
        this.level = json.getInt("Level");
    }

    public void setCoordinates(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getStatus(Animal animal){
        if(animal.hasSucceeded(this.spawnId)){
            return Status.DONE;
        }

        if(null != requirement) {
            if (!animal.hasSucceeded(requirement)) {
                return Status.REQUIREMENT_NOT_MET;
            }
        }

        return Status.AVAILABLE;
    }

     public String getIcon(){
        if(this.icon == null){
            return getStandardIcon();
        }else{
            return icon;
        }
    }

    public Location getLocation(){
        Location location = new Location(name);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return  location;
    }

    public abstract String getStandardIcon();

    public int getSpellReward() {
        return spellReward;
    }

    public Integer getSpawnId() {
        return spawnId;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public int getStrengthReward() {
        return strengthReward;
    }

    public int getHealthReward() {
        return healthReward;
    }

    public Integer getRequirement() {
        return requirement;
    }

    public int getLevel() {
        return level;
    }

    public void setRequirement(Integer requirement) {
        this.requirement = requirement;
    }

    @Override
    public String toString() {
        return "AbstractSpawn{" +
                "name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", icon='" + icon + '\'' +
                '}';
    }
}

