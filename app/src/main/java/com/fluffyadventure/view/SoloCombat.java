package com.fluffyadventure.view;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.ImageView;

import com.fluffyadventure.controller.Controller;
import com.fluffyadventure.model.Animal;
import com.fluffyadventure.model.Creature;
import com.fluffyadventure.model.Monster;
import com.fluffyadventure.tools.CircularLinkedList;

import java.util.ArrayList;
import java.util.ResourceBundle;

public class SoloCombat extends Activity {

    private TextView opponentsName;
    private ProgressBar opponentsLife;
    private int opponentsLifePoint;
    private ImageView opponentImage;
    private TextView fightersName;
    private ProgressBar fightersLife;
    private int fightersLifePoint;
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

        opponentsName.setText("Evil Bunny");
        String imagePath = "evilbunny";
        opponentImage.setImageResource(
                getResources().getIdentifier(
                        imagePath, "drawable", getPackageName()));
        opponentsLifePoint=100;

        fightersName.setText(Controller.getAnimal1().getName());
        imagePath = Controller.getAnimal1().getImagePath();
        fighterImage.setImageResource(
                getResources().getIdentifier(
                        imagePath, "drawable", getPackageName()));
        fightersLifePoint=100;

        action1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                opponentsLifePoint = LosesLifeAnimation(opponentsLife, opponentsLifePoint, 15, opponentImage, true);
            }
        });

        action2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                opponentsLifePoint = GainLifeAnimation(opponentsLife, opponentsLifePoint, 25, opponentImage);
            }
        });

        action3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fightersLifePoint = LosesLifeAnimation(fightersLife, fightersLifePoint, 15, fighterImage, false);
            }
        });

    }

    public int LosesLifeAnimation(ProgressBar lifeProgressBar, int lifeProgressBarPoint, int lifePointLost, ImageView opponentImage, boolean isOpponent) {

        //animate injury (blinking animal)
        Animation injuryAnimation = AnimationUtils.loadAnimation(this, R.anim.injury_animation);
        injuryAnimation.restrictDuration(20);
        opponentImage.startAnimation(injuryAnimation);

        int offset = 300;
        Integer[] listTriggerPoints = {100, 40, 20, 0};
        Integer[] listProgressBarColors = {R.drawable.orange_progress_bar, R.drawable.red_progress_bar, R.drawable.red_progress_bar};
        for (int i = 0; i < listProgressBarColors.length; i++) {
            //if life is between two trigger point and you still have life point to loose, programs animation
            if ((lifeProgressBarPoint > listTriggerPoints[i + 1]) && (lifeProgressBarPoint <= listTriggerPoints[i]) && (lifePointLost >= 0)) {
                //pointToLose: point life to lose under this iteration ie with this color progress bar
                //lifePointLost act as a global pointToLose
                int pointToLose = lifePointLost;
                //if you need a change of progress bar color in the middle of the lost of point
                if ((lifeProgressBarPoint-pointToLose)<listTriggerPoints[i+1]){
                    pointToLose = lifeProgressBarPoint-listTriggerPoints[i+1];
                }
                LosePointAnimation(lifeProgressBar, lifeProgressBarPoint, lifeProgressBarPoint - pointToLose, offset);
                lifePointLost -= pointToLose;
                lifeProgressBarPoint -= pointToLose;
                offset += pointToLose * 15;

                //change color of the process bar if needed after removal of life point
                if (lifeProgressBarPoint <= listTriggerPoints[i + 1]) {
                    Resources res = getResources();
                    Rect bounds = lifeProgressBar.getProgressDrawable().getBounds();
                    lifeProgressBar.setProgressDrawable(res.getDrawable(listProgressBarColors[i]));
                    lifeProgressBar.getProgressDrawable().setBounds(bounds);
                }
            }
        }

        // animate the death of the animal
        if (lifeProgressBarPoint<=0) {
            Animation deadAnimation;
            if (isOpponent) {
                deadAnimation = AnimationUtils.loadAnimation(this, R.anim.death_opponent_animation);
            }else{
                deadAnimation = AnimationUtils.loadAnimation(this, R.anim.death_fighter_animation);
            }
            deadAnimation.setStartOffset(offset+600);
            opponentImage.startAnimation(deadAnimation);
        }

        return (lifeProgressBarPoint);
    }

    public int GainLifeAnimation(ProgressBar lifeProgressBar, int lifeProgressBarPoint, int lifePointGain, ImageView opponentImage) {

        int offset = 0;
        Integer[] listTriggerPoints = {0, 20, 40, 100};
        Integer[] listProgressBarColors = {R.drawable.green_progress_bar, R.drawable.green_progress_bar, R.drawable.orange_progress_bar};
        for (int i = 0; i < listProgressBarColors.length; i++) {
            //if life is between two trigger point and you still have life point to loose, programs animation
            if ((lifeProgressBarPoint < listTriggerPoints[i + 1]) && (lifeProgressBarPoint >= listTriggerPoints[i]) && (lifePointGain >= 0)) {
                //pointToGain: point life to gain under this iteration ie with this color progress bar
                //lifePointGain act as a global pointToGain
                int pointToGain = lifePointGain;
                //if you need a change of progress bar color in the middle of the lost of point
                if ((lifeProgressBarPoint+pointToGain)>listTriggerPoints[i+1]){
                    pointToGain = listTriggerPoints[i+1] - lifeProgressBarPoint;

                    //change progress bar color
                    Resources res = getResources();
                    Rect bounds = lifeProgressBar.getProgressDrawable().getBounds();
                    lifeProgressBar.setProgressDrawable(res.getDrawable(listProgressBarColors[i]));
                    lifeProgressBar.getProgressDrawable().setBounds(bounds);
                }
                LosePointAnimation(lifeProgressBar, lifeProgressBarPoint, lifeProgressBarPoint + pointToGain, offset);
                lifePointGain -= pointToGain;
                lifeProgressBarPoint += pointToGain;
                offset += pointToGain * 15;

                //change color of the process bar if needed after removal of life point
//                if (lifeProgressBarPoint >= listTriggerPoints[i]) {
//                    Resources res = getResources();
//                    Rect bounds = lifeProgressBar.getProgressDrawable().getBounds();
//                    lifeProgressBar.setProgressDrawable(res.getDrawable(listProgressBarColors[i]));
//                    lifeProgressBar.getProgressDrawable().setBounds(bounds);
//                }
            }
        }

        return (lifeProgressBarPoint);
    }

        private void LosePointAnimation(ProgressBar lifeProgressBar, int start, int end, int timeOffset){
        ObjectAnimator animation = ObjectAnimator.ofInt(lifeProgressBar, "progress", start, end);
        animation.setDuration( Math.abs(start-end)*15);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setStartDelay(timeOffset+300);
        animation.start();
    }

//    @Override
//    public void onBackPressed() {
//        return;
//    }

}
