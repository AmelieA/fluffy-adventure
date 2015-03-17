package com.fluffyadventure.model;

import android.location.Location;

import com.fluffyadventure.controller.Controller;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Johan on 18/02/2015.
 */
public abstract class AbstractSpawn {

    public final class SpawnStatus {
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

    private boolean soloFight;

    public AbstractSpawn(){
    }

    public AbstractSpawn(String name, int type){
        this.spawnId = -1;
        this.spellReward = 0;
        this.healthReward = 0;
        this.strengthReward = 0;
        this.longitude = 0;
        this.latitude = 0;
        this.text = "";
        if (type == Creature.RABBIT) {
            this.name = "Terrier de " + name;
        } else if (type == Creature.SQUIRREL) {
            this.name = "Nid de " + name;
        } else if (type == Creature.SHEEP) {
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

    public String getStatus(){
        if(Controller.hasSucceeded(this.spawnId)){
            return SpawnStatus.DONE;
        }

        if(null != requirement) {
            if (!Controller.hasSucceeded(requirement)) {
                return SpawnStatus.REQUIREMENT_NOT_MET;
            }
        }
        return SpawnStatus.AVAILABLE;
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

    public abstract ArrayList<Creature> getOpponents();

    public abstract void setOpponents(ArrayList<Creature> opponents);

    @Override
    public String toString() {
        return "AbstractSpawn{" +
                "name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", icon='" + icon + '\'' +
                '}';
    }

    public boolean isSoloFight() {
        return soloFight;
    }

    public void setSoloFight(boolean soloFight) {
        this.soloFight = soloFight;
    }
}

