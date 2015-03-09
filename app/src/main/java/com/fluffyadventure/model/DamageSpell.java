package com.fluffyadventure.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 09/03/2015.
 */
public class DamageSpell extends AbstractSpell {

    private int damage;

    public DamageSpell(int id, String name, String description, boolean isAoE, int damage) {
        super(id, name, description, isAoE);
        this.damage = damage;
    }

    public DamageSpell(JSONObject json) throws JSONException {
        super(json);
    }

    public  List<List<Creature>> use(List<Creature> fighters, List<Creature> opponents, Integer target) {
        ArrayList<List<Creature>> returnedArray = new ArrayList<>();
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
}
