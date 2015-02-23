package com.fluffyadventure.view;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.ImageView;

public class SoloCombat extends Activity {

    private static final int PROGRESS = 0x1;

    private TextView opponentsName;
    private ProgressBar opponentsLife;
    private int opponentsLifeStatus = 100;
    private ImageView opponentImage;
    private TextView fightersName;
    private ProgressBar fightersLife;
    private int fightersLifeStatus = 100;
    private TextView instruction;
    private ImageView fighterImage;
    private Button action1;
    private Button action2;
    private Button action3;
    private Button action4;

    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("FA", "Solo fight...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solo_combat);

        opponentsName = (TextView) findViewById(R.id.OpponentsName);
        opponentsLife = (ProgressBar) findViewById(R.id.OpponentsLife);
        opponentImage = (ImageView) findViewById(R.id.OpponentImage);
        fightersName = (TextView) findViewById(R.id.FightersName);
        fightersLife = (ProgressBar) findViewById(R.id.FightersLife);
        fighterImage = (ImageView) findViewById(R.id.FighterImage);
        instruction = (TextView) findViewById(R.id.Instruction);
        action1 = (Button) findViewById(R.id.Action1);
        action2 = (Button) findViewById(R.id.Action2);
        action3 = (Button) findViewById(R.id.Action3);
        action4 = (Button) findViewById(R.id.Action4);

        action1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Start lengthy operation in a background thread
//                new Thread(new Runnable() {
//                    public void run() {
//                        for (int i = 0; i < 50; i++, opponentsLifeStatus--) {
//
//                            // Update the progress bar
//                            try {
//                                //Display progress slowly
//                                Thread.sleep(40);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            handler.post(new Runnable() {
//                                public void run() {
//                                    opponentsLife.setProgress(opponentsLifeStatus);
//                                }
//                            });
//                        }
//                    }
//                }).start();

            LosesLifeAnimation(opponentsLife, opponentsLifeStatus, 50);
            }
        });


    }

    public void LosesLifeAnimation(ProgressBar lifeProgressBar, int lifeProgressBarStatus, int lifePointLost) {

        ObjectAnimator animation = ObjectAnimator.ofInt(lifeProgressBar, "progress", lifeProgressBarStatus, lifeProgressBarStatus-lifePointLost);
        animation.setDuration(990);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();

        if (lifeProgressBarStatus<15){

        }

    }

}
