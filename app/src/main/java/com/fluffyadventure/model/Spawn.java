package com.fluffyadventure.model;

import android.location.Location;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Johan on 17/02/2015.
 */
public class Spawn extends AbstractSpawn {

        private ArrayList<Monster> opponents = new ArrayList<>();

        public Spawn(){
        }

        public Spawn(String name, int type){
            super (name, type);
        }

        public Spawn( Integer spawnId, int spellReward, int healthReward, int strengthReward, double latitude, double longitude, String text, String name, Integer level, ArrayList<Monster> opponents) {
            super(spawnId, spellReward, healthReward, strengthReward, latitude, longitude, text, name, level);
            this.opponents = opponents;
        }
        public  Spawn(JSONObject jsonObject) throws JSONException {
            super(jsonObject);
        }

        @Override
        public String getStandardIcon() {
            return "spawn_icon";
        }
}

