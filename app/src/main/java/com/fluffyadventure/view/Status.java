package com.fluffyadventure.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fluffyadventure.controller.Controller;

public class Status extends Activity {

    ImageButton BtnMap;
    Button btnMngSkills;
    Button btnMoveHQ;
    ImageView imgPC1;
    TextView status_name1;
    TextView status_hlth1;
    TextView status_str1;
    TextView status_acc1;
    TextView status_eva1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Typeface font = Typeface.createFromAsset(getAssets(), "GrandHotel-Regular.otf");

        imgPC1 = (ImageView) findViewById(R.id.imgPC1);

        String imagePath = Controller.getAnimal1().getImagePath();

        imgPC1.setImageResource(
                getResources().getIdentifier(
                        imagePath, "drawable", getPackageName()));

        status_name1 = (TextView) findViewById(R.id.status_Name1);
        status_name1.setTypeface(font);
        status_name1.setText(Controller.getAnimal1().getName());

        status_hlth1 = (TextView) findViewById(R.id.status_hlth1);
        status_hlth1.setText(String.valueOf(Controller.getAnimal1().getHealth()));

        status_str1 = (TextView) findViewById(R.id.status_str1);
        status_str1.setText(String.valueOf(Controller.getAnimal1().getStrength()));

        status_acc1 = (TextView) findViewById(R.id.status_acc1);
        status_acc1.setText(String.valueOf(Controller.getAnimal1().getAccuracy()));

        status_eva1 = (TextView) findViewById(R.id.status_eva1);
        status_eva1.setText(String.valueOf(Controller.getAnimal1().getEvasiveness()));

        btnMngSkills = (Button) findViewById(R.id.BtnMngSkills);
        btnMngSkills.setTypeface(font);
        btnMngSkills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Status.this, AttackChoice.class);
                startActivity(intent);
                finish();
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
                finish();
            }
        });
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
