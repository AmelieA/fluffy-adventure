package com.fluffyadventure.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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
import com.fluffyadventure.model.User;

public class SignUpActivity extends Activity {

    Button btnSignIn;
    EditText etUserName;
    EditText etPass;
    TextView Logo1;
    TextView Logo2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

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
                CreateUserTask task = new CreateUserTask(etUserName.getText().toString(),etPass.getText().toString(), SignUpActivity.this);
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

    private class CreateUserTask extends AsyncTask<Void, Void, Boolean> {
        private String username;
        private String password;
        ProgressDialog dialog;
        Context ctx;

        public CreateUserTask(String username, String password, Context ctx) {
            this.username = username;
            this.password = password;
            this.ctx = ctx;
            this.dialog = new ProgressDialog(this.ctx);
            //this.dialog.setCancelable(true);

        }

        protected void onPreExecute(){
            this.dialog.setTitle("Création...");
            this.dialog.show();
            //affichage de la popup

        }
        protected Boolean doInBackground(Void... params){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Boolean result = Controller.userExists(username);


            return result;
        }
        protected  void onPostExecute(Boolean result) {
            System.out.println("done");

            dialog.dismiss();
            //disparition de la popup

            if (result){
                Toast.makeText(SignUpActivity.this, "L'utilisateur existe déja", Toast.LENGTH_LONG).show();
            }
            else {
                Controller.setUser(new User(username, password));

                Intent intent = new Intent(SignUpActivity.this, AnimalChoiceSlider.class);
                System.out.println("Activitychange");
                startActivity(intent);
                finish();
            }
        }



    }
}


