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
import com.fluffyadventure.model.AbstractSpell;
import com.fluffyadventure.model.DamageSpell;
import com.fluffyadventure.model.HealSpell;

import org.json.JSONException;

import java.util.ArrayList;


public class AttackChoice extends Activity {

    // The adapter that we gonna use
    AttackChoiceAdapter activateAdapter;
    AttackChoiceAdapter inactiveAdapter;

    // List of attacks
    ArrayList<AbstractSpell> activeAttack;

    ArrayList<AbstractSpell> inactiveAttack;
    Button saveBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Typeface font = Typeface.createFromAsset(getAssets(), "GrandHotel-Regular.otf");

        activeAttack = new ArrayList<AbstractSpell>((ArrayList) Controller.getAnimal1().getActiveSpells());
        inactiveAttack = new ArrayList<AbstractSpell>((ArrayList) Controller.getAnimal1().getUnusedSpells());
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
                AbstractSpell toAdd = inactiveAttack.remove(position);
                activeAttack.add(toAdd);
            }
            else {
                Toast.makeText(AttackChoice.this, "Maximum 4 sorts actifs", Toast.LENGTH_LONG).show();
            }
        }else{
            if (activeAttack.size() > 1)
            {
            AbstractSpell toAdd =  activeAttack.remove(position);
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

    public class AttackChoiceAdapter extends ArrayAdapter<AbstractSpell> {
        private final boolean activated;
        private final Context context;
        private final ArrayList<AbstractSpell> values;
        ViewHolderItem viewHolder;

        public AttackChoiceAdapter(Context context, ArrayList<AbstractSpell> values, boolean activated) {
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
                    AttackChoice.this.updateListViews(position, isChecked);
                    Log.i("TIME", "end of updade listViews");
                }
            });

            return rowView;
        }
    }

    private class ChangeSpellsTask extends AsyncTask<Void, Void, Boolean> {
        private ArrayList<AbstractSpell> active;
        private ArrayList<AbstractSpell> unused;
        ProgressDialog dialog;
        Context ctx;

        public ChangeSpellsTask(ArrayList<AbstractSpell> active, ArrayList<AbstractSpell> unused, Context ctx) {
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

                Intent intent = new Intent(AttackChoice.this, com.fluffyadventure.view.Status.class);
                System.out.println("Activitychange");
                startActivity(intent);
                finish();
            }
        }



    }
}

