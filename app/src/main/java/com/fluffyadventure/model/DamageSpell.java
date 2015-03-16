package com.fluffyadventure.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 09/03/2015.
 */
public class DamageSpell extends AbstractSpell {

    private int damagePercent;
    private int damage;

    public DamageSpell(int id, String name, String description, boolean isAoE, int damagePercent, int animationType, String throwedObject) {
        super(id, name, description, isAoE,animationType, throwedObject);
        this.damagePercent = damagePercent;
        this.damage = 0;
    }

    public DamageSpell(JSONObject json) throws JSONException {
        super(json);
        damagePercent = json.getInt("Health");
    }

    public  ArrayList<ArrayList<Creature>> use(ArrayList<Creature> fighters, ArrayList<Creature> opponents, Integer source, Integer target) {
        this.damage =(int)Math.round((double)fighters.get(source).getStrength() * (double)(damagePercent)/100.0);
        ArrayList<ArrayList<Creature>> returnedArray = new ArrayList<>();
        returnedArray.add(fighters);

        if ( target != null ) {
            opponents.get(target).setHealth(opponents.get(target).getHealth() - damage);
        } else if ( target == null ) {
            opponents.get(0).setHealth(opponents.get(0).getHealth() - damage);
            if (opponents.size() > 1) {
                opponents.get(1).setHealth(opponents.get(1).getHealth() - damage);
            }
        }

        returnedArray.add(opponents);

        return returnedArray;
    }

    public int getValue() {
        return damage;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = super.toJson();

        json.put("Type",AbstractSpell.DAMAGE);
        json.put("Health",damage);
        return json;
    }
}
