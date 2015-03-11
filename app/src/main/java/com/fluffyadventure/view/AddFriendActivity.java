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
import android.widget.Toast;

import com.fluffyadventure.controller.Controller;


public class AddFriendActivity extends Activity {
    Button btnAddFriend;
    EditText etFriendName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);



        Typeface font = Typeface.createFromAsset(getAssets(), "GrandHotel-Regular.otf");


        etFriendName = (EditText)findViewById(R.id.etFriendName);
        etFriendName.addTextChangedListener(new TextWatcher() {
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

        btnAddFriend = (Button) findViewById(R.id.btnAddFriend);
        btnAddFriend.setTypeface(font);
        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFriendTask task = new AddFriendTask(etFriendName.getText().toString(), AddFriendActivity.this);
                task.execute();
            }
        });
    }

    public void enableSubmitIfReady() {
        boolean isReady = etFriendName.getText().toString().length() > 0;
        btnAddFriend.setEnabled(isReady);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_friend, menu);
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

    private class AddFriendTask extends AsyncTask<Void, Void, Boolean> {
        private String username;
        ProgressDialog dialog;
        Context ctx;

        public AddFriendTask(String username, Context ctx) {
            this.username = username;
            this.ctx = ctx;
            this.dialog = new ProgressDialog(this.ctx);
            //this.dialog.setCancelable(true);

        }

        protected void onPreExecute(){
            this.dialog.setTitle("Ajout en cours...");
            this.dialog.show();

        }
        protected Boolean doInBackground(Void... params){
            Boolean result = Controller.addFriend(username);
            return result;
        }
        protected  void onPostExecute(Boolean login) {
            System.out.println("done");
            dialog.dismiss();

            if (!login){
                Toast.makeText(AddFriendActivity.this, "Utilisateur inconnu", Toast.LENGTH_LONG).show();

            }
            else {

                Intent intent;
                intent = new Intent(AddFriendActivity.this, FriendListActivity.class);


                System.out.println("Activitychange");
                startActivity(intent);
                finish();
            }
        }



    }
}
