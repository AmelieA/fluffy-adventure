package com.fluffyadventure.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fluffyadventure.controller.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class MainActivity extends Activity {

    Button BtnStatus;
    Button BtnCombat;
    Button MapBtn;
    Button loginBtn;
    Button signinBtn;
    Button SlideBtn;
    TextView Logo1;
    TextView Logo2;
    Button BtnSignUp;
    Class<?> nextIntentClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("FA", "Starting...");
        super.onCreate(savedInstanceState);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        } else {
            setContentView(R.layout.activity_main);


            setupApplication();

            Typeface font = Typeface.createFromAsset(getAssets(), "GrandHotel-Regular.otf");

            SlideBtn = (Button)findViewById(R.id.SlideBtn);
            SlideBtn.setTypeface(font);
            SlideBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, MoveQGActivity.class);
                    startActivity(intent);
                }
            });

            loginBtn = (Button)findViewById(R.id.loginBtn);
            loginBtn.setTypeface(font);
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Controller.getServer() != null){
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Server inconnu", Toast.LENGTH_LONG).show();
                    }

                }
            });

            signinBtn = (Button)findViewById(R.id.signInBtn);
            signinBtn.setTypeface(font);
            signinBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Controller.getServer() != null){
                        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Server inconnu", Toast.LENGTH_LONG).show();
                    }
                }
            });


            Logo1 = (TextView)findViewById(R.id.Logo1);
            Logo1.setTypeface(font);
            Logo2 = (TextView)findViewById(R.id.Logo2);
            Logo2.setTypeface(font);
        }
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



        //Controller.setupBob();
        //Controller.setupObjectives();

        Resources resources = this.getResources();
        AssetManager assetManager = resources.getAssets();


        try {
            InputStream inputStream = assetManager.open("server.properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            String server_name = properties.getProperty("address");
            int server_port = Integer.parseInt(properties.getProperty("port"));
            System.out.println(server_name);
            LoginUserTask task = new LoginUserTask(server_name, server_port, MainActivity.this);
            task.execute();
        } catch (IOException e) {
            Log.i("Server","Failed to open server property file");
            Log.i("Server", "Failed to open server property file");
            e.printStackTrace();


        }


        //nextIntentClass = XXX.class;

       // SharedPreferences settings = getPreferences(MODE_PRIVATE);
       // SharedPreferences.Editor editor = settings.edit();
       // editor.putBoolean("firstLaunch", false);
       // editor.commit();



    }


    private class LoginUserTask extends AsyncTask<Void, Void, Boolean> {
        private String server;
        private int port;
        ProgressDialog dialog;
        Context ctx;
        Boolean login;

        public LoginUserTask(String server, int port, Context ctx) {
            this.server = server;
            this.port = port;
            this.ctx = ctx;
            this.dialog = new ProgressDialog(this.ctx);
            this.login = false;
            //this.dialog.setCancelable(true);

        }

        protected void onPreExecute(){
            this.dialog.setTitle("Connexion...");
            this.dialog.show();

        }
        protected Boolean doInBackground(Void... params){

            Boolean result = Controller.connectToServer(server, port);
            Controller.setUpObjectivesFromServer();
            if (Controller.getUser() != null){
                login = Controller.login(Controller.getUser().getName(),Controller.getUser().getPassword());
            }
            //Boolean result = true;


            return result;
        }
        protected  void onPostExecute(Boolean connected) {
            Log.i("Server","Attempted connection");
            dialog.dismiss();

            if (!connected){
                Toast.makeText(MainActivity.this, "Serveur inconnu", Toast.LENGTH_LONG).show();

            }
            else
            {
                Toast.makeText(MainActivity.this, "Connecté au serveur", Toast.LENGTH_LONG).show();
            }


            if (login){
                Intent intent;
                intent = new Intent(MainActivity.this, MapComponent.class);


                System.out.println("Activitychange");
                startActivity(intent);
            }
            else {
                Controller.setupBob();
            }

        }



    }


}