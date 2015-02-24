package com.fluffyadventure.view;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.ImageView;

public class SoloCombat extends Activity {

    private TextView opponentsName;
    private ProgressBar opponentsLife;
    private int opponentsLifePoint = 100;
    private ImageView opponentImage;
    private TextView fightersName;
    private ProgressBar fightersLife;
    private int fightersLifePoint = 100;
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

            opponentsLifePoint = LosesLifeAnimation(opponentsLife, opponentsLifePoint, 15);
            }
        });


    }

    public int LosesLifeAnimation(ProgressBar lifeProgressBar, int lifeProgressBarPoint, int lifePointLost) {

        ObjectAnimator animation = ObjectAnimator.ofInt(lifeProgressBar, "progress", lifeProgressBarPoint, lifeProgressBarPoint-lifePointLost);
        animation.setDuration(lifePointLost*15);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
        lifeProgressBarPoint-=lifePointLost;

        Resources res = getResources();
        Rect bounds = lifeProgressBar.getProgressDrawable().getBounds();
        if (lifeProgressBarPoint<50){
            if (lifeProgressBarPoint<25){
                lifeProgressBar.setProgressDrawable(res.getDrawable(R.drawable.red_progress_bar));
            }else {
                lifeProgressBar.setProgressDrawable(res.getDrawable(R.drawable.orange_progress_bar));
            }
        }
        lifeProgressBar.getProgressDrawable().setBounds(bounds);
        lifeProgressBar.setProgress(lifeProgressBarPoint);

        return (lifeProgressBarPoint);

    }

}
