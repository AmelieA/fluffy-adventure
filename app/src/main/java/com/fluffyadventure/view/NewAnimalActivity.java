package com.fluffyadventure.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fluffyadventure.controller.Controller;
import com.fluffyadventure.model.AbstractSpell;
import com.fluffyadventure.model.Animal;
import com.fluffyadventure.model.Creature;
import com.fluffyadventure.model.DamageSpell;
import com.fluffyadventure.model.DebuffSpell;
import com.fluffyadventure.model.HealSpell;
import com.fluffyadventure.view.R;

import java.util.ArrayList;

public class NewAnimalActivity extends Activity {

    TextView newAnimalDisc;
    Button btnNewAnimalYes;
    Button btnNewAnimalNo;
    Button btnNewAnimalOK;
    ListView attackListView;
    ImageView newAnimalImageView;
    Animal friend;
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

        btnNewAnimalYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NewAnimalActivity.this, "Compagnon ajouté", Toast.LENGTH_LONG).show();
                Controller.setAnimal2(friend);
                Intent intent = new Intent(NewAnimalActivity.this, MapComponent.class);
                startActivity(intent);
                finish();
            }
        });

        btnNewAnimalOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NewAnimalActivity.this, "Compagnon ajouté", Toast.LENGTH_LONG).show();
                Controller.setAnimal2(friend);
                Intent intent = new Intent(NewAnimalActivity.this, MapComponent.class);
                startActivity(intent);
                finish();
            }
        });

        btnNewAnimalNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewAnimalActivity.this, MapComponent.class);
                startActivity(intent);
                finish();
            }
        });

        friend = new Animal("Squeeky","squirrel4", Creature.SQUIRREL);
        friend.clearSpells();
        friend.addSpell(new HealSpell(0, "Soin de groupe", "Soigne tout le groupe pour 10 pv", true, 10, AbstractSpell.HEAL, null, 5), true);
        friend.addSpell(new DebuffSpell(1, "Jet de boue", "Réduit l'esquive et la précision de 20 %", false, 100, 80, 80, AbstractSpell.DEBUFF, null, 5), true);
        friend.addSpell(new DamageSpell(2, "Charge mignonne", "Charge l'ennemi, le blessant pour 130% de ta force", false, 130 , AbstractSpell.ATTACK, null, 30), true);
        friend.addSpell(new DamageSpell(3, "Pluie de noisettes", "Lance des noisettes sur les ennemis, les blessant pour 140% de ta force", true, 140 , AbstractSpell.THROW, "hazelnut", 10), true);

        newAnimalImageView = (ImageView) findViewById(R.id.newAnimalImageView);

        String imagePath = friend.getImagePath();

        newAnimalImageView.setImageResource(
                getResources().getIdentifier(
                        imagePath, "drawable", getPackageName()));
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
        attackList = (ArrayList) friend.getActiveSpells();
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
