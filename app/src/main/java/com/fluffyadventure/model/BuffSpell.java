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

    public BuffSpell(int id, String name, String description, boolean isAoE, int strModif, int accuModif, int evaModif, int animationType,  String throwedObject, int maxUses) {
        super(id, name, description, isAoE, animationType, throwedObject, maxUses);
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

    public  ArrayList<ArrayList<Creature>> use(ArrayList<Creature> fighters, ArrayList<Creature> opponents, Integer source, Integer target) {
        ArrayList<ArrayList<Creature>> returnedArray = new ArrayList<>();
        addUse();

        if ( target != null ) {
            fighters.get(target).setStrength((int)Math.round((double)fighters.get(target).getStrength() * (double)strModif/100.0));
            fighters.get(target).setAccuracy((int)Math.round((double)fighters.get(target).getAccuracy() * (double)accuModif/100.0));
            fighters.get(target).setEvasiveness((int)Math.round((double)fighters.get(target).getEvasiveness() * (double)evaModif/100.0));
        } else if ( target == null ) {
            fighters.get(0).setStrength((int)Math.round((double)fighters.get(0).getStrength() * (double)strModif/100.0));
            fighters.get(0).setAccuracy((int)Math.round((double)fighters.get(0).getAccuracy() * (double)accuModif/100.0));
            fighters.get(0).setEvasiveness((int)Math.round((double)fighters.get(0).getEvasiveness() * (double)evaModif/100.0));
            if (fighters.size() > 1) {
                fighters.get(1).setStrength((int)Math.round((double)fighters.get(1).getStrength() * (double)strModif/100.0));
                fighters.get(1).setAccuracy((int)Math.round((double)fighters.get(1).getAccuracy() * (double)accuModif/100.0));
                fighters.get(1).setEvasiveness((int)Math.round((double)fighters.get(1).getEvasiveness() * (double)evaModif/100.0));
            }
        }

        returnedArray.add(fighters);
        returnedArray.add(opponents);

        return returnedArray;
    }

    public int getValue() {
        return 0;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = super.toJson();
        json.put("Strength",strModif);
        json.put("Accuracy",accuModif);
        json.put("Evasiveness",evaModif);
        json.put("Type",AbstractSpell.BUFF);
        return json;
    }
}
