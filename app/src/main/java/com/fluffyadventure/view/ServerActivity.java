package com.fluffyadventure.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fluffyadventure.controller.Controller;

public class ServerActivity extends Activity {

    Button btnSignIn;
    TextView Logo1;
    TextView Logo2;

    EditText etServer;
    EditText etPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        //
         Typeface font = Typeface.createFromAsset(getAssets(), "GrandHotel-Regular.otf");

        etServer = (EditText)findViewById(R.id.etServer);
        etPort = (EditText)findViewById(R.id.etPort);

       btnSignIn = (Button)findViewById(R.id.btnConnect);
        btnSignIn.setTypeface(font);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUserTask task = new LoginUserTask(etServer.getText().toString(), Integer.parseInt(etPort.getText().toString()), ServerActivity.this);
                task.execute();
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
        private String server;
        private int port;
        ProgressDialog dialog;
        Context ctx;

        public LoginUserTask(String server, int port, Context ctx) {
            this.server = server;
            this.port = port;
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
            Boolean result = Controller.connectToServer(server, port);
            //Boolean result = true;


            return result;
        }
        protected  void onPostExecute(Boolean login) {
            System.out.println("done");
            dialog.dismiss();

            if (!login){
                Toast.makeText(ServerActivity.this, "Server inconnu", Toast.LENGTH_LONG).show();

            }
            else {

                Intent intent;
                intent = new Intent(ServerActivity.this, MainActivity.class);


                System.out.println("Activitychange");
                startActivity(intent);
                finish();
            }
        }



    }
}
