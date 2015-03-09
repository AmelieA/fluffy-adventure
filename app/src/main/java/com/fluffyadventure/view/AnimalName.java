package com.fluffyadventure.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.fluffyadventure.controller.Controller;
import com.fluffyadventure.view.R;

public class AnimalName extends Activity {

    EditText name;
    Button btnOkName;
    TextView nameChoiceDisclaimer;
    ImageView animalNameImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_name);

        Typeface font = Typeface.createFromAsset(getAssets(), "GrandHotel-Regular.otf");

        animalNameImageView = (ImageView) findViewById(R.id.animalNameImageView);

        String imagePath = Controller.getAnimal1().getImagePath();

        animalNameImageView.setImageResource(
                getResources().getIdentifier(
                        imagePath, "drawable", getPackageName()));

        nameChoiceDisclaimer = (TextView) findViewById(R.id.nameChoiceDisclaimer);
        nameChoiceDisclaimer.setTypeface(font);

        btnOkName = (Button) findViewById(R.id.BtnOkName);
        btnOkName.setTypeface(font);
        btnOkName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controller.getAnimal1().setName(name.getText().toString());
                Intent intent = new Intent(AnimalName.this, MoveQGActivity.class);
                startActivity(intent);
                finish();

            }
        });

        name = (EditText) findViewById(R.id.name_choice);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                enableSubmitIfReady();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }

    public void enableSubmitIfReady() {

       boolean isReady = name.getText().toString().length() > 0 &&  name.getText().toString().length() < 13;

       btnOkName.setEnabled(isReady);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_animal_name, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    private class SendAnimalTask extends AsyncTask<Void, Void, Boolean> {
        private String name;
        ProgressDialog dialog;
        Context ctx;

        public SendAnimalTask(String name, Context ctx) {
            this.name = name;
            this.ctx = ctx;
            this.dialog = new ProgressDialog(this.ctx);
            //this.dialog.setCancelable(true);

        }

        protected void onPreExecute(){
            this.dialog.setTitle("Création...");
            this.dialog.show();

        }
        protected Boolean doInBackground(Void... params){

            Boolean result = Controller.sendAnimalToServer(this.name);
            //Boolean result = true;


            return result;
        }
        protected  void onPostExecute(Boolean login) {
            System.out.println("done");
            dialog.dismiss();

            if (!login){
                Toast.makeText(AnimalName.this, "Echec de la création de l'animal", Toast.LENGTH_LONG).show();

            }
            else
            {
                Toast.makeText(AnimalName.this, "Animal Créé", Toast.LENGTH_LONG).show();
            }

            Intent intent = new Intent(AnimalName.this, Status.class);
            startActivity(intent);
            finish();



        }



    }
}
