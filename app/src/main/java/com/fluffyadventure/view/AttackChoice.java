package com.fluffyadventure.view;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.fluffyadventure.model.Spell;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class AttackChoice extends Activity {

    // The adapter that we gonna use
    AttackChoiceAdapter activateAdapter;
    AttackChoiceAdapter inactiveAdapter;

    // List of attacks
    Spell spell = new Spell(0,"attack", "bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla.");
    ArrayList<Spell> activeAttack = new ArrayList<>(Arrays.asList(spell, spell, spell, spell));

    Spell spell2 = new Spell(0,"attack Deactivated", "bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla.");
    ArrayList<Spell> inactiveAttack = new ArrayList<>(Arrays.asList(spell2, spell2, spell2, spell2, spell2, spell2, spell2, spell2));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attack_choice);

        activateAdapter = new AttackChoiceAdapter(this, activeAttack, true);
        ListView activatedAttackView = (ListView) findViewById(R.id.ActivatedAttackView);
        activatedAttackView.setAdapter(activateAdapter);

        inactiveAdapter = new AttackChoiceAdapter(this, inactiveAttack, false);
        ListView deactivatedAttackView = (ListView) findViewById(R.id.DeactivatedAttackView);
        deactivatedAttackView.setAdapter(inactiveAdapter);


    }

    public void updateListViews(int position, boolean isCheck) {
        if (isCheck){
            if (activeAttack.size()<4) {
                Spell toAdd = inactiveAttack.remove(position);
                activeAttack.add(toAdd);
            }
        }else{
            Spell toAdd =  activeAttack.remove(position);
            inactiveAttack.add(toAdd);
        }
        activateAdapter.notifyDataSetChanged();
        inactiveAdapter.notifyDataSetChanged();
    }





    public class AttackChoiceAdapter extends ArrayAdapter<Spell> {
        private final boolean activated;
        private final Context context;
        private final ArrayList<Spell> values;
        private AttackChoiceAdapter otherListView;

        public AttackChoiceAdapter(Context context, ArrayList<Spell> values, boolean activated) {
            super(context, R.layout.attack_choice_row_layout, values);
            this.context = context;
            this.values = values;
            this.activated = activated;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            // reuse views
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.attack_choice_row_layout, parent, false);

            TextView attackName = (TextView) rowView.findViewById(R.id.AttackName);
            attackName.setText(values.get(position).getName());
            TextView attackDescription = (TextView) rowView.findViewById(R.id.AttackDescription);
            attackDescription.setText(values.get(position).getDescription());
            final Switch toggleButton = (Switch) rowView.findViewById(R.id.ToggleButton);
            toggleButton.setChecked(activated);
            toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    AttackChoice.this.updateListViews(position, isChecked);
                    if (isChecked){
                        if (AttackChoice.this.activeAttack.size()<4) {
                            Spell toAdd = AttackChoice.this.inactiveAttack.remove(position);
                            AttackChoice.this.activeAttack.add(toAdd);
                        }
                    }else{
                        Spell toAdd =  AttackChoice.this.activeAttack.remove(position);
                        AttackChoice.this.inactiveAttack.add(toAdd);
                    }
                    AttackChoice.this.activateAdapter.notifyDataSetChanged();
                    AttackChoice.this.inactiveAdapter.notifyDataSetChanged();
                }
            });

            return rowView;
        }
    }
}

