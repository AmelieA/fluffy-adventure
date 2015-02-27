package com.fluffyadventure.view;

import android.content.Intent;
import android.graphics.Typeface;
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

import com.fluffyadventure.controller.Controller;
import com.fluffyadventure.view.R;

public class AnimalName extends ActionBarActivity {

    EditText name;
    Button btnOkName;
    ImageView imageViewAnimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_name);

        Typeface font = Typeface.createFromAsset(getAssets(), "GrandHotel-Regular.otf");

        btnOkName = (Button) findViewById(R.id.BtnOkName);
        btnOkName.setTypeface(font);
        btnOkName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //TODO :  CALL TO CONTROLLER
                Intent intent = new Intent(AnimalName.this, Status.class);
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

        imageViewAnimal = (ImageView) findViewById(R.id.imageViewAnimal);
        int imgId = getResources().getIdentifier(Controller.getAnimal().getImagePath(),"drawable",getPackageName());
        imageViewAnimal.setImageDrawable(getResources().getDrawable(imgId));
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
}
