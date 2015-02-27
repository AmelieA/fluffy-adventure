package com.fluffyadventure.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fluffyadventure.controller.Controller;
import com.fluffyadventure.view.R;

public class Status extends Activity {

    ImageButton BtnMap;
    Button btnMngSkills;
    Button btnMoveHQ;
    TextView textViewType1;
    TextView textViewHealth1;
    TextView textViewStrength1;
    TextView textViewAccuracy1;
    TextView textViewEvasiveness1;
    TextView textViewName1;

    ImageView imgPC1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Typeface font = Typeface.createFromAsset(getAssets(), "GrandHotel-Regular.otf");

        btnMngSkills = (Button) findViewById(R.id.BtnMngSkills);
        btnMngSkills.setTypeface(font);
        btnMngSkills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Status.this, AttackChoice.class);
                startActivity(intent);
                //finish();
            }
        });

        btnMoveHQ = (Button) findViewById(R.id.BtnMoveHQ);
        btnMoveHQ.setTypeface(font);

        BtnMap = (ImageButton)findViewById(R.id.BtnMap);
        BtnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Status.this, MapComponent.class);
                startActivity(intent);
                //finish();
            }
        });

        textViewType1 = (TextView) findViewById(R.id.textViewType1);

        textViewType1.setText(Controller.getAnimal().getType());

        textViewHealth1 = (TextView) findViewById(R.id.textViewHealth1);
        textViewHealth1.setText(Integer.toString(Controller.getAnimal().getHealth()));

        textViewStrength1 = (TextView) findViewById(R.id.textViewStrength1);
        textViewStrength1.setText(Integer.toString(Controller.getAnimal().getStrength()));

        textViewAccuracy1 = (TextView) findViewById(R.id.textViewAccuracy1);
        textViewAccuracy1.setText(Integer.toString(Controller.getAnimal().getAccuracy()));

        textViewEvasiveness1 = (TextView) findViewById(R.id.textViewEvasiveness1);
        textViewEvasiveness1.setText(Integer.toString(Controller.getAnimal().getEvasiveness()));

        textViewName1 = (TextView) findViewById(R.id.textViewName1);
        textViewName1.setText(Controller.getAnimal().getName());

        imgPC1 = (ImageView) findViewById(R.id.imgPC1);
        int imgId = getResources().getIdentifier(Controller.getAnimal().getImagePath(),"drawable",getPackageName());
        imgPC1.setImageDrawable(getResources().getDrawable(imgId));




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_status, menu);
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
