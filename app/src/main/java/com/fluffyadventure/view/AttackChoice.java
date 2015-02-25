package com.fluffyadventure.view;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fluffyadventure.model.Spell;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class AttackChoice extends Activity {

    // The adapter that we gonna use
    AttackChoiceAdapter activateAdapter;
    AttackChoiceAdapter deactivateAdapter;

    // List of attacks
    Spell spell = new Spell(0,"attack", "bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla.");
    ArrayList<Spell> attacks = new ArrayList<>(Arrays.asList(spell, spell, spell, spell, spell, spell, spell, spell));

    Spell spell2 = new Spell(0,"attack Deactivated", "bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla.");
    ArrayList<Spell> attacks2 = new ArrayList<>(Arrays.asList(spell2, spell2, spell2, spell2, spell2, spell2, spell2, spell2));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attack_choice);

        activateAdapter = new AttackChoiceAdapter(this, attacks);
        ListView activatedAttackView = (ListView) findViewById(R.id.ActivatedAttackView);
        activatedAttackView.setAdapter(activateAdapter);

        deactivateAdapter = new AttackChoiceAdapter(this, attacks);
        ListView deactivatedAttackView = (ListView) findViewById(R.id.DeactivatedAttackView);
        deactivatedAttackView.setAdapter(deactivateAdapter);

    }





    private class AttackChoiceAdapter extends ArrayAdapter<Spell> {
        private final Context context;
        private final ArrayList<Spell> values;

        public AttackChoiceAdapter(Context context, ArrayList<Spell> values) {
            super(context, R.layout.attack_choice_row_layout, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            // reuse views
           // if (rowView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.attack_choice_row_layout, parent, false);

                TextView attackName = (TextView) rowView.findViewById(R.id.AttackName);
                attackName.setText(values.get(position).getName());
                TextView attackDescription = (TextView) rowView.findViewById(R.id.AttackDescription);
                attackDescription.setText(values.get(position).getDescription());
            //}

            return rowView;
        }
    }
}

