package com.fluffyadventure.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fluffyadventure.controller.Controller;


public class Status extends Activity {

    ImageButton btnMap;
    ImageButton btnMailbox;
    Button btnMngSkills;
    Button btnMoveHQ;
    ImageView imgPC1;
    TextView textViewHealth1;
    TextView textViewStrength1;
    TextView textViewAccuracy1;
    TextView textViewEvasiveness1;
    TextView textViewName1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Typeface font = Typeface.createFromAsset(getAssets(), "GrandHotel-Regular.otf");

        imgPC1 = (ImageView) findViewById(R.id.imgPC1);

        String imagePath = Controller.getAnimal(1).getImagePath();

        imgPC1.setImageResource(
                getResources().getIdentifier(
                        imagePath, "drawable", getPackageName()));

        textViewName1 = (TextView) findViewById(R.id.textViewName1);
        textViewName1.setTypeface(font);
        textViewName1.setText(Controller.getAnimal1().getName());

        textViewHealth1 = (TextView) findViewById(R.id.textViewHealth1);
        textViewHealth1.setText(String.valueOf(Controller.getAnimal1().getHealth()));

        textViewStrength1 = (TextView) findViewById(R.id.textViewStrength1);
        textViewStrength1.setText(String.valueOf(Controller.getAnimal1().getStrength()));

        textViewAccuracy1 = (TextView) findViewById(R.id.textViewAccuracy1);
        textViewAccuracy1.setText(String.valueOf(Controller.getAnimal1().getAccuracy()));

        textViewEvasiveness1 = (TextView) findViewById(R.id.textViewEvasiveness1);
        textViewEvasiveness1.setText(String.valueOf(Controller.getAnimal1().getEvasiveness()));

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
        btnMoveHQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Status.this, MoveQGActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnMap = (ImageButton)findViewById(R.id.BtnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Status.this, MapComponent.class);

                startActivity(intent);
                finish();
            }
        });

        btnMailbox = (ImageButton)findViewById(R.id.BtnMail);
        btnMailbox.setImageResource(getResources().getIdentifier(
                (Controller.checkForUnreadMails()?"newmail":"mail"), "drawable", getPackageName()));
        btnMailbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GetMailsTask task = new GetMailsTask(Status.this);
                task.execute();
            }
        });
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Désirez-vous quitter Fluffy Adventure ?")
                .setTitle("Quitter l'application ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("EXIT", true);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .setNeutralButton("Déconnexion", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Controller.flush();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private class GetMailsTask extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog dialog;
        Context ctx;

        public GetMailsTask(Context ctx) {
            this.ctx = ctx;
            this.dialog = new ProgressDialog(this.ctx);
            //this.dialog.setCancelable(true);

        }

        protected void onPreExecute(){
            this.dialog.setTitle("Récupération des mails...");
            this.dialog.show();

        }
        protected Boolean doInBackground(Void... params){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Boolean result = Controller.retrieveMailsFromServer();


            return result;
        }
        protected  void onPostExecute(Boolean login) {
            System.out.println("done");
            dialog.dismiss();

            if (!login){
                Toast.makeText(ctx, "Impossible de récupérer les mails", Toast.LENGTH_LONG).show();

            }


            Intent intent = new Intent(ctx, MailBox.class);
            startActivity(intent);
            finish();
        }



    }
}

