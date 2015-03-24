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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fluffyadventure.controller.Controller;


public class Status extends Activity {

    ImageButton btnMap;
    ImageButton btnMailbox;
    ImageButton btnFriends;
    Button btnMngSkills;
    Button btnMoveHQ;
    ImageView imgPC1;
    TextView textViewHealth1;
    TextView textViewStrength1;
    TextView textViewAccuracy1;
    TextView textViewEvasiveness1;
    TextView textViewName1;
    ImageView imgPC2;
    TextView textViewHealth2;
    TextView textViewStrength2;
    TextView textViewAccuracy2;
    TextView textViewEvasiveness2;
    TextView textViewName2;
    LinearLayout animal2Block;

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
        textViewName1.setText(Controller.getAnimal(1).getName());

        textViewHealth1 = (TextView) findViewById(R.id.textViewHealth1);
        textViewHealth1.setText(String.valueOf(Controller.getAnimal(1).getHealth()));

        textViewStrength1 = (TextView) findViewById(R.id.textViewStrength1);
        textViewStrength1.setText(String.valueOf(Controller.getAnimal(1).getStrength()));

        textViewAccuracy1 = (TextView) findViewById(R.id.textViewAccuracy1);
        textViewAccuracy1.setText(String.valueOf(Controller.getAnimal(1).getAccuracy()));

        textViewEvasiveness1 = (TextView) findViewById(R.id.textViewEvasiveness1);
        textViewEvasiveness1.setText(String.valueOf(Controller.getAnimal(1).getEvasiveness()));

        animal2Block = (LinearLayout)findViewById(R.id.animal2block);

        if (Controller.getAnimal(2)!=null){
            imgPC2 = (ImageView) findViewById(R.id.imgPC2);
            String imagePath2 = Controller.getAnimal(2).getImagePath();
            imgPC2.setImageResource(
                    getResources().getIdentifier(
                            imagePath2, "drawable", getPackageName()));

            textViewName2 = (TextView) findViewById(R.id.textViewName2);
            textViewName2.setTypeface(font);
            textViewName2.setText(Controller.getAnimal(2).getName());

            textViewHealth2 = (TextView) findViewById(R.id.textViewHealth2);
            textViewHealth2.setText(String.valueOf(Controller.getAnimal(2).getHealth()));

            textViewStrength2 = (TextView) findViewById(R.id.textViewStrength2);
            textViewStrength2.setText(String.valueOf(Controller.getAnimal(2).getStrength()));

            textViewAccuracy2 = (TextView) findViewById(R.id.textViewAccuracy2);
            textViewAccuracy2.setText(String.valueOf(Controller.getAnimal(2).getAccuracy()));

            textViewEvasiveness2 = (TextView) findViewById(R.id.textViewEvasiveness2);
            textViewEvasiveness2.setText(String.valueOf(Controller.getAnimal(2).getEvasiveness()));

            animal2Block.setVisibility(View.VISIBLE);
        } else {
            animal2Block.setVisibility(View.GONE);
        }

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

        btnFriends = (ImageButton)findViewById(R.id.BtnFriends);
        btnFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Status.this, FriendListActivity.class);
                startActivity(intent);
                finish();
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

