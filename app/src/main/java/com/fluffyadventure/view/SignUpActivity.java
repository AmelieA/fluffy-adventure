package com.fluffyadventure.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.fluffyadventure.controller.Controller;
import com.fluffyadventure.model.User;
import com.fluffyadventure.view.R;

import java.util.concurrent.ExecutionException;

public class SignUpActivity extends Activity {

    Button btnSignIn;
    EditText etUserName;
    EditText etPass;
    //ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //progressBar = (ProgressBar)findViewById(R.id.progressBar);
        //progressBar.setVisibility(View.GONE);

        Typeface font = Typeface.createFromAsset(getAssets(), "GrandHotel" +
                "" +
                "-Regular.otf");

        etUserName = (EditText)findViewById(R.id.etUserName);
        etPass = (EditText)findViewById(R.id.etPass);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);
        btnSignIn.setTypeface(font);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateUserTask task = new CreateUserTask(etUserName.getText().toString(),etPass.getText().toString(), SignUpActivity.this);
                task.execute();
            }
        });

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
            this.dialog.setTitle("Creating user...");
            this.dialog.show();

        }
        protected Boolean doInBackground(Void... params){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Boolean result = Controller.createUser(username,password);


            return result;
        }
        protected  void onPostExecute(Boolean unused) {
            System.out.println("done");

            dialog.dismiss();
            Intent intent = new Intent(SignUpActivity.this, AnimalChoiceSlider.class);
            System.out.println("Activitychange");
            startActivity(intent);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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


