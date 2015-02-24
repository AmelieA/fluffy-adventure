package com.fluffyadventure.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AttackChoice extends Activity {

    // The adapter that we gonna use
    AttackChoiceAdapter adapter;

    // List of attacks
    ArrayList<String> attacks= new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attack_choice);

        ListView listview = (ListView) findViewById(R.id.ListOfPossibleAttack);
        adapter = new AttackChoiceAdapter(this, attacks);
        listview.setAdapter(adapter);
    }

    private class AttackChoiceAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final ArrayList<String> values;

        public AttackChoiceAdapter(Context context, ArrayList<String> values) {
            super(context, R.layout.attack_choice_row_layout, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = null;
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.attack_choice_row_layout, parent, false);

            // Displaying a textview
            TextView attackName = (TextView) rowView.findViewById(R.id.AttackName);
            attackName.setText("Attack");
            TextView attackDescription = (TextView) rowView.findViewById(R.id.AttackDescription);
            attackDescription.setText("Description");


            return rowView;
        }
    }
}

