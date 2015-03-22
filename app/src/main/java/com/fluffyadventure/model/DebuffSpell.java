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

    public DebuffSpell(int id, String name, String description, boolean isAoE, int strModif, int accuModif, int evaModif, int animationType, String throwedObject, int maxUses) {
        super(id, name, description, isAoE, animationType, throwedObject, maxUses);
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
        addUse();
        ArrayList<ArrayList<Creature>> returnedArray = new ArrayList<>();
        returnedArray.add(fighters);

        if ( target != null ) {
            opponents.get(target).setStrength((int)Math.round((double)opponents.get(target).getStrength() * (double)strModif/100.0));
            opponents.get(target).setAccuracy((int)Math.round((double)opponents.get(target).getAccuracy() * (double)accuModif/100.0));
            opponents.get(target).setEvasiveness((int)Math.round((double)opponents.get(target).getEvasiveness() * (double)evaModif/100.0));
        } else if ( target == null ) {
            opponents.get(0).setStrength((int)Math.round((double)opponents.get(0).getStrength() * (double)strModif/100.0));
            opponents.get(0).setAccuracy((int)Math.round((double)opponents.get(0).getAccuracy() * (double)accuModif/100.0));
            opponents.get(0).setEvasiveness((int)Math.round((double)opponents.get(0).getEvasiveness() * (double)evaModif/100.0));
            if (fighters.size() > 1) {
                opponents.get(1).setStrength((int)Math.round((double)opponents.get(1).getStrength() * (double)strModif/100.0));
                opponents.get(1).setAccuracy((int)Math.round((double)opponents.get(1).getAccuracy() * (double)accuModif/100.0));
                opponents.get(1).setEvasiveness((int)Math.round((double)opponents.get(1).getEvasiveness() * (double)evaModif/100.0));
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
