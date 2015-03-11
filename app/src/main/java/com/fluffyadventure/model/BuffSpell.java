package com.fluffyadventure.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 09/03/2015.
 */
public class BuffSpell extends AbstractSpell {

    private int strModif;
    private int accuModif;
    private int evaModif;

    public BuffSpell(int id, String name, String description, boolean isAoE, int strModif, int accuModif, int evaModif) {
        super(id, name, description, isAoE);
        this.strModif = strModif;
        this.accuModif = accuModif;
        this.evaModif = evaModif;
    }

    public BuffSpell(JSONObject json) throws JSONException {
        super(json);
        strModif = json.getInt("Strength");
        accuModif = json.getInt("Accuracy");
        evaModif = json.getInt("Evasiveness");
    }

    public  ArrayList<ArrayList<Creature>> use(ArrayList<Creature> fighters, ArrayList<Creature> opponents, Integer target) {
        ArrayList<ArrayList<Creature>> returnedArray = new ArrayList<>();

        if ( target != null ) {
            fighters.get(target).setStrength(fighters.get(target).getStrength() + strModif);
            fighters.get(target).setAccuracy(fighters.get(target).getAccuracy() + accuModif);
            fighters.get(target).setEvasiveness(fighters.get(target).getEvasiveness() + evaModif);
        } else if ( target == null ) {
            fighters.get(0).setStrength(fighters.get(0).getStrength() + strModif);
            fighters.get(0).setAccuracy(fighters.get(0).getAccuracy() + accuModif);
            fighters.get(0).setEvasiveness(fighters.get(0).getEvasiveness() + evaModif);
            if (fighters.size() > 1) {
                fighters.get(1).setStrength(fighters.get(1).getStrength() + strModif);
                fighters.get(1).setAccuracy(fighters.get(1).getAccuracy() + accuModif);
                fighters.get(1).setEvasiveness(fighters.get(1).getEvasiveness() + evaModif);
            }
        }

        returnedArray.add(fighters);
        returnedArray.add(opponents);

        return returnedArray;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = super.toJson();
        json.put("Strength",strModif);
        json.put("Accuracy",accuModif);
        json.put("Evasiveness",evaModif);
        json.put("Type","Buff");
        return json;
    }
}
