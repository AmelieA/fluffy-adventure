package com.fluffyadventure.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(10000);

        if ( target != null ) {
            if (randomInt <= (100 - opponents.get(target).getEvasiveness()) * fighters.get(source).getAccuracy()) {
                opponents.get(target).setHealth(opponents.get(target).getHealth() - damage);
                setHasHit(true);
            } else {
                setHasHit(false);
            }
        } else if ( target == null ) {
            if (opponents.size() < 2 ) {
                if (randomInt <= (100 - opponents.get(0).getEvasiveness()) * fighters.get(source).getAccuracy()) {
                    opponents.get(0).setHealth(opponents.get(0).getHealth() - damage);
                    setHasHit(true);
                } else {
                    setHasHit(false);
                }
            } else {
                int hitChances = (((100 - opponents.get(0).getEvasiveness()) * fighters.get(source).getAccuracy()) + ((100 - opponents.get(1).getEvasiveness()) * fighters.get(source).getAccuracy())) / 2;
                if (randomInt <= hitChances) {
                    opponents.get(0).setHealth(opponents.get(0).getHealth() - damage);
                    opponents.get(1).setHealth(opponents.get(1).getHealth() - damage);
                } else {
                    setHasHit(false);
                }
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
