package com.fluffyadventure.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fluffyadventure.controller.Controller;


public class MainActivity extends Activity {

    Button BtnStatus;
    Button BtnCombat;
    Button MapBtn;
    Button loginBtn;
    Button signinBtn;
    Button SlideBtn;
    TextView Logo1;
    TextView Logo2;
    Class<?> nextIntentClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("FA", "Starting...");

        //nextIntentClass = XXX.class;
        //checkFirstLaunchAndSetupApplication();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupApplication();

        Typeface font = Typeface.createFromAsset(getAssets(), "GrandHotel-Regular.otf");

        /*MapBtn = (Button)findViewById(R.id.MapBtn);
        MapBtn.setTypeface(font);
        MapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapComponent.class);
                startActivity(intent);
            }
        });

        SlideBtn = (Button)findViewById(R.id.SlideBtn);
        SlideBtn.setTypeface(font);
        SlideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AnimalChoiceSlider.class);
                startActivity(intent);
            }
        });

        BtnStatus = (Button)findViewById(R.id.BtnStatus);
        BtnStatus.setTypeface(font);
        BtnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Status.class);
                startActivity(intent);
            }
        });

        BtnCombat = (Button)findViewById(R.id.BtnCombat);
        BtnCombat.setTypeface(font);
        BtnCombat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SoloCombat.class);
                startActivity(intent);
            }
        });*/

        loginBtn = (Button)findViewById(R.id.loginBtn);
        loginBtn.setTypeface(font);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signinBtn = (Button)findViewById(R.id.signInBtn);
        signinBtn.setTypeface(font);
        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        Logo1 = (TextView)findViewById(R.id.Logo1);
        Logo1.setTypeface(font);
        Logo2 = (TextView)findViewById(R.id.Logo2);
        Logo2.setTypeface(font);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void checkFirstLaunchAndSetupApplication()
    {
        Log.i("FA", "Checking for first launch...");
        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        boolean firstLaunch = settings.getBoolean("firstLaunch", true);
        if (firstLaunch) {
            Log.i("FA", "This is first launch");
            setupApplication();
        }
    }

    private  void setupApplication() {

        Controller.setupBob();
        Controller.setupObjectives();

        //nextIntentClass = XXX.class;

       // SharedPreferences settings = getPreferences(MODE_PRIVATE);
       // SharedPreferences.Editor editor = settings.edit();
       // editor.putBoolean("firstLaunch", false);
       // editor.commit();
    }


}