package com.fluffyadventure.view;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.fluffyadventure.controller.Controller;
import com.fluffyadventure.model.AbstractSpell;
import com.fluffyadventure.view.R;

import java.util.ArrayList;

public class NewAnimalActivity extends Activity {

    TextView newAnimalDisc;
    Button btnNewAnimalYes;
    Button btnNewAnimalNo;
    Button btnNewAnimalOK;
    ListView attackListView;
    AttackAdapter attackAdapter;
    ArrayList<AbstractSpell> attackList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_animal);

        newAnimalDisc = (TextView)findViewById(R.id.newAnimalDisclaimer);
        btnNewAnimalYes = (Button)findViewById(R.id.btnNewAnimalYes);
        btnNewAnimalNo = (Button)findViewById(R.id.btnNewAnimalNo);
        btnNewAnimalOK = (Button)findViewById(R.id.btnNewAnimalOK);
        attackListView = (ListView)findViewById(R.id.newAnimalListView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //changer le texte si animal 2 = null, "un nouveau compagnon rejoint ton équipe"
        //sinon"veux-tu changer etc."
        //bouton OK ou OUI/NON
        if (Controller.getAnimal(2)==null){
            newAnimalDisc.setText(R.string.newAnimalNew);
            btnNewAnimalYes.setVisibility(View.GONE);
            btnNewAnimalNo.setVisibility(View.GONE);
            btnNewAnimalOK.setVisibility(View.VISIBLE);
        }else{
            newAnimalDisc.setText(R.string.newAnimalReplace);
            btnNewAnimalYes.setVisibility(View.VISIBLE);
            btnNewAnimalNo.setVisibility(View.VISIBLE);
            btnNewAnimalOK.setVisibility(View.GONE);
        }
        //afficher liste des sorts
        //TODO: get new animal skills list
    }

    public class AttackAdapter extends ArrayAdapter<AbstractSpell> {
        private final Context context;
        private final ArrayList<AbstractSpell> values;

        public AttackAdapter(Context context, ArrayList<AbstractSpell> values) {
            super(context, R.layout.new_animal_skills_row_layout, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            // reuse views
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.new_animal_skills_row_layout, parent, false);

            TextView attackName = (TextView) rowView.findViewById(R.id.AttackName);
            attackName.setText(values.get(position).getName());
            TextView attackDescription = (TextView) rowView.findViewById(R.id.AttackDescription);
            attackDescription.setText(values.get(position).getDescription());

            return rowView;
        }
    }
}
