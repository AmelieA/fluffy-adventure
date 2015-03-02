package com.fluffyadventure.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.fluffyadventure.controller.Controller;
import com.fluffyadventure.model.Spell;

import java.util.ArrayList;
import java.util.Arrays;


public class AttackChoice extends Activity {

    // The adapter that we gonna use
    AttackChoiceAdapter activateAdapter;
    AttackChoiceAdapter inactiveAdapter;

    // List of attacks
    Spell spell = new Spell(0,"attack", "bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla.");
    ArrayList<Spell> activeAttack;// = new ArrayList<>(Arrays.asList(spell, spell, spell, spell));

    Spell spell2 = new Spell(0,"attack Deactivated", "bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla.");
    ArrayList<Spell> inactiveAttack;// = new ArrayList<>(Arrays.asList(spell2, spell2, spell2, spell2, spell2, spell2, spell2, spell2));
    Button saveBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Typeface font = Typeface.createFromAsset(getAssets(), "GrandHotel-Regular.otf");

        activeAttack = new ArrayList<Spell>((ArrayList) Controller.getAnimal1().getActiveSpells());
        inactiveAttack = new ArrayList<Spell>((ArrayList) Controller.getAnimal1().getUnusedSpells());
        System.out.println(inactiveAttack.toString());
        System.out.println(activeAttack.toString());

        //TODO: import attack list

        setContentView(R.layout.activity_attack_choice);

        saveBtn = (Button) findViewById(R.id.saveBtn);
        saveBtn.setTypeface(font);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeSpellsTask task = new ChangeSpellsTask(activeAttack,inactiveAttack, AttackChoice.this);
                task.execute();

            }
        });

        activateAdapter = new AttackChoiceAdapter(this, activeAttack, true);
        ListView activatedAttackView = (ListView) findViewById(R.id.ActivatedAttackView);
        activatedAttackView.setAdapter(activateAdapter);

        inactiveAdapter = new AttackChoiceAdapter(this, inactiveAttack, false);
        ListView deactivatedAttackView = (ListView) findViewById(R.id.DeactivatedAttackView);
        deactivatedAttackView.setAdapter(inactiveAdapter);


    }

    public void updateListViews(int position, boolean isCheck) {
        Log.i("TIME", "start of updade listViews");
        if (isCheck){
            if (activeAttack.size()<4) {
                Spell toAdd = inactiveAttack.remove(position);
                activeAttack.add(toAdd);
            }
            else {
                Toast.makeText(AttackChoice.this, "Maximum 4 sorts actifs", Toast.LENGTH_LONG).show();
            }
        }else{
            if (activeAttack.size() > 1)
            {
            Spell toAdd =  activeAttack.remove(position);
            inactiveAttack.add(toAdd);
            }
            else {
                Toast.makeText(AttackChoice.this, "Minimum 1 sort actif", Toast.LENGTH_LONG).show();
            }

        }
        System.out.println(inactiveAttack.toString());
        System.out.println(activeAttack.toString());

        activateAdapter.notifyDataSetChanged();
        inactiveAdapter.notifyDataSetChanged();
    }





    static class ViewHolderItem {
        TextView attackName;
        TextView attackDescription;
        Switch toggleButton;
    }

    public class AttackChoiceAdapter extends ArrayAdapter<Spell> {
        private final boolean activated;
        private final Context context;
        private final ArrayList<Spell> values;
        ViewHolderItem viewHolder;

        public AttackChoiceAdapter(Context context, ArrayList<Spell> values, boolean activated) {
            super(context, R.layout.attack_choice_row_layout, values);
            this.context = context;
            this.values = values;
            this.activated = activated;
        }

        @Override
        public View getView(final int position, View rowView, ViewGroup parent) {

            if(rowView==null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.attack_choice_row_layout, parent, false);

                viewHolder = new ViewHolderItem();
                viewHolder.attackName = (TextView) rowView.findViewById(R.id.AttackName);
                viewHolder.attackDescription = (TextView) rowView.findViewById(R.id.AttackDescription);
                viewHolder.toggleButton = (Switch) rowView.findViewById(R.id.ToggleButton);
                rowView.setTag(viewHolder);

            }else{
                viewHolder = (ViewHolderItem) rowView.getTag();
            }

            viewHolder.attackName.setText(values.get(position).getName());
            viewHolder.attackDescription.setText(values.get(position).getDescription());
            viewHolder.toggleButton.setChecked(activated);
            viewHolder.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    AttackChoice.this.updateListViews(position, isChecked);
                    Log.i("TIME", "end of updade listViews");
                }
            });

            return rowView;
        }
    }

    private class ChangeSpellsTask extends AsyncTask<Void, Void, Boolean> {
        private ArrayList<Spell> active;
        private ArrayList<Spell> unused;
        ProgressDialog dialog;
        Context ctx;

        public ChangeSpellsTask(ArrayList<Spell> active, ArrayList<Spell> unused, Context ctx) {
            this.active = active;
            this.unused = unused;
            this.ctx = ctx;
            this.dialog = new ProgressDialog(this.ctx);
            //this.dialog.setCancelable(true);

        }

        protected void onPreExecute(){
            this.dialog.setTitle("Connexion...");
            this.dialog.show();

        }
        protected Boolean doInBackground(Void... params){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Boolean result = Controller.changeSpells(active,unused);


            return result;
        }
        protected  void onPostExecute(Boolean login) {
            System.out.println("done");
            dialog.dismiss();

            if (!login){
                Toast.makeText(AttackChoice.this, "Erreur au changement", Toast.LENGTH_LONG).show();

            }
            else {

                Intent intent = new Intent(AttackChoice.this, Status.class);
                System.out.println("Activitychange");
                startActivity(intent);
                finish();
            }
        }



    }
}

