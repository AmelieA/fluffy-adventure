package com.fluffyadventure.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fluffyadventure.controller.Controller;

public class LoginActivity extends Activity {

    Button btnSignIn;
    TextView Logo1;
    TextView Logo2;

    EditText etUserName;
    EditText etPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Typeface font = Typeface.createFromAsset(getAssets(), "GrandHotel-Regular.otf");

        etUserName = (EditText)findViewById(R.id.etUserName);
        etUserName.addTextChangedListener(new TextWatcher() {
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

        etPass = (EditText)findViewById(R.id.etPass);

        btnSignIn = (Button)findViewById(R.id.btnAddFriend);
        btnSignIn.setTypeface(font);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUserTask task = new LoginUserTask(etUserName.getText().toString(),etPass.getText().toString(), LoginActivity.this);
                task.execute();
            }
        });

        Logo1 = (TextView)findViewById(R.id.Logo1);
        Logo1.setTypeface(font);
        Logo2 = (TextView)findViewById(R.id.Logo2);
        Logo2.setTypeface(font);
    }

    public void enableSubmitIfReady() {
        boolean isReady = etUserName.getText().toString().length() > 0;
        btnSignIn.setEnabled(isReady);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    private class LoginUserTask extends AsyncTask<Void, Void, Boolean> {
        private String username;
        private String password;
        ProgressDialog dialog;
        Context ctx;

        public LoginUserTask(String username, String password, Context ctx) {
            this.username = username;
            this.password = password;
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
            Boolean result = Controller.login(username, password);


            return result;
        }
        protected  void onPostExecute(Boolean login) {
            System.out.println("done");
            dialog.dismiss();

            if (!login){
                Toast.makeText(LoginActivity.this, "Utilisateur inconnu", Toast.LENGTH_LONG).show();

            }
            else {

                Intent intent;
                if (Controller.getAnimal1() != null) {
                    intent = new Intent(LoginActivity.this, Status.class);
                } else {
                    intent = new Intent(LoginActivity.this, AnimalChoiceSlider.class);
                }

                System.out.println("Activitychange");
                startActivity(intent);
                finish();
            }
        }



    }
}
