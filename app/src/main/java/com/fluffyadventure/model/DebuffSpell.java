package com.fluffyadventure.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 09/03/2015.
 */
public class DebuffSpell extends AbstractSpell {

    private int strModif;
    private int accuModif;
    private int evaModif;

    public DebuffSpell(int id, String name, String description, boolean isAoE, int strModif, int accuModif, int evaModif, int animationType, String throwedObject) {
        super(id, name, description, isAoE, animationType, throwedObject);
        this.strModif = strModif;
        this.accuModif = accuModif;
        this.evaModif = evaModif;
    }

    public DebuffSpell(JSONObject json) throws JSONException {
        super(json);
        strModif = json.getInt("Strength");
        accuModif = json.getInt("Accuracy");
        evaModif = json.getInt("Evasiveness");
    }

    public  ArrayList<ArrayList<Creature>> use(ArrayList<Creature> fighters, ArrayList<Creature> opponents, Integer source, Integer target) {
        ArrayList<ArrayList<Creature>> returnedArray = new ArrayList<>();
        returnedArray.add(fighters);

        if ( target != null ) {
            opponents.get(target).setStrength(opponents.get(target).getStrength() - strModif);
            opponents.get(target).setAccuracy(opponents.get(target).getAccuracy() - accuModif);
            opponents.get(target).setEvasiveness(opponents.get(target).getEvasiveness() - evaModif);
        } else if ( target == null ) {
            opponents.get(0).setStrength(opponents.get(0).getStrength() - strModif);
            opponents.get(0).setAccuracy(opponents.get(0).getAccuracy() - accuModif);
            opponents.get(0).setEvasiveness(opponents.get(0).getEvasiveness() - evaModif);
            if (opponents.size() > 1) {
                opponents.get(1).setStrength(opponents.get(1).getStrength() - strModif);
                opponents.get(1).setAccuracy(opponents.get(1).getAccuracy() - accuModif);
                opponents.get(1).setEvasiveness(opponents.get(1).getEvasiveness() - evaModif);
            }
        }

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
        json.put("Type",AbstractSpell.DEBUFF);
        return json;
    }
}
