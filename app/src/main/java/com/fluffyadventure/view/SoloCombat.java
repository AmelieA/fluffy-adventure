package com.fluffyadventure.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.ImageView;
import android.widget.Toast;

import com.fluffyadventure.controller.Controller;
import com.fluffyadventure.model.AbstractSpell;
import com.fluffyadventure.model.Animal;
import com.fluffyadventure.model.Creature;
import com.fluffyadventure.model.DamageSpell;
import com.fluffyadventure.model.HealSpell;
import com.fluffyadventure.model.Monster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.ResourceBundle;

public class SoloCombat extends Activity {

    //opponent
    private TextView opponentsName;
    private ProgressBar opponentsLife;
    private int opponentsLifePoint;
    private ImageView opponentImage;
    private ImageView opponentsGainLifeFilter;

    //fighter
    private TextView fightersName;
    private ProgressBar fightersLife;
    private int fightersLifePoint;
    private ImageView fighterImage;
    private ImageView fightersGainLifeFilter;

    //animation components
    private ImageView throwableObjectToOpponent;
    private ImageView throwableObjectToFighter;

    //buttons and texts
    private TextView instruction;
    private Button action1;
    private Button action2;
    private Button action3;
    private Button action4;


    //needed for services part
    private Animal animal;
    private boolean opponnentsTurn = false;
    private Animal tempAnimal;
    private ArrayList<Creature> tempOpponents = new ArrayList<>();
    private int currentOpponentIdx;
    private ArrayList<Creature> opponents = new ArrayList<>();
    private ArrayList<Creature> fighters= new ArrayList<>();

    //Time in ms when the last animation is oven from the moment you launch the first animation
    private int animationOffset;

    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("FA", "Solo fight...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solo_combat);

        opponentsName = (TextView) findViewById(R.id.OpponentsName);
        opponentsLife = (ProgressBar) findViewById(R.id.OpponentsLife);
        opponentImage = (ImageView) findViewById(R.id.OpponentImage);
        opponentsGainLifeFilter = (ImageView) findViewById(R.id.OpponentsLifeFilter);
        fightersName = (TextView) findViewById(R.id.FightersName);
        fightersLife = (ProgressBar) findViewById(R.id.FightersLife);
        fighterImage = (ImageView) findViewById(R.id.FighterImage);
        fightersGainLifeFilter = (ImageView) findViewById(R.id.FightersLifeFilter);
        instruction = (TextView) findViewById(R.id.Instruction);
        throwableObjectToOpponent = (ImageView) findViewById(R.id.ThrowableObjectToOpponent);
        throwableObjectToFighter = (ImageView) findViewById(R.id.ThrowableObjectToFighter);
        action1 = (Button) findViewById(R.id.Action1);
        action2 = (Button) findViewById(R.id.Action2);
        action3 = (Button) findViewById(R.id.Action3);
        action4 = (Button) findViewById(R.id.Action4);

    }

    @Override
    protected void onStart() {
        super.onStart();

        currentOpponentIdx = 0;
        animal = Controller.getAnimal1();
        animal.clearSpells();
        animal.addSpell(new HealSpell(0, "Soin", "zut", false, 15, AbstractSpell.HEAL, null), true);
        animal.addSpell(new DamageSpell(1, "Jet de noisette", "zut", false, 150, AbstractSpell.THROW, "hazelnut"), true);
        animal.addSpell(new DamageSpell(2, "Charge", "zut", false, 500 , AbstractSpell.ATTACK, null), true);

        tempAnimal = Controller.getAnimal1();
        fighters.add(animal);

        opponents = Controller.getCurrentObjective().getOpponents();
        for (Creature opponent : opponents) {
            tempOpponents.add(new Monster(opponent.getName(), opponent.getType(), opponent.getHealth(), opponent.getStrength(), opponent.getAccuracy(),opponent.getEvasiveness(), opponent.getActiveSpells()));
        }
        currentOpponentIdx = 0;

        if (opponents.size() > 0){
            setupFight(currentOpponentIdx);
        }

        action1.setText(animal.getActiveSpells().get(0).getName());
        action1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                animationOffset=0;
                useSpell(0);
                opponnentsTurn = true;
            }
        });

        if (animal.getActiveSpells().size() > 1) {
            action2.setText(animal.getActiveSpells().get(1).getName());
            action2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    animationOffset=0;
                    useSpell(1);
                    opponnentsTurn = true;
                }
            });
        }else{
            action2.setEnabled(false);
            action2.setText("");
        }

        if (animal.getActiveSpells().size() > 2) {
            action3.setText(animal.getActiveSpells().get(2).getName());
            action3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    animationOffset=0;
                    useSpell(2);
                    opponnentsTurn = true;
                }
            });
        }else{
            action3.setEnabled(false);
            action3.setText("");
        }

        if (animal.getActiveSpells().size() > 3) {
            action4.setText(animal.getActiveSpells().get(3).getName());
            action4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    animationOffset=0;
                    useSpell(3);
                    opponnentsTurn = true;
                }
            });
        }else{
            action4.setEnabled(false);
            action4.setText("");
        }
   }

    private void setupFight(int opponentIdx){
        opponentsLifePoint = opponents.get(opponentIdx).getHealth();
        opponentsLife.setMax(opponentsLifePoint);
        opponentsName.setText(opponents.get(opponentIdx).getName());
        String imagePath = "evilbunny";
        opponentImage.setImageResource(
                getResources().getIdentifier(
                        imagePath, "drawable", getPackageName()));
        fightersLifePoint = animal.getHealth();
        fightersLife.setMax(fightersLifePoint);
        fightersName.setText(Controller.getAnimal1().getName());
        imagePath = animal.getImagePath();
        fighterImage.setImageResource(
                getResources().getIdentifier(
                        imagePath, "drawable", getPackageName()));

        instruction.setText(getResources().getString(R.string.combat_instruction_1)+" "+animal.getName()+" "+getResources().getString(R.string.combat_instruction_2));
    }

    private void setButtonsEnabled(boolean value) {
        switch (animal.getActiveSpells().size()) {
            case 1:
                action1.setEnabled(value);
                break;
            case 2:
                action1.setEnabled(value);
                action2.setEnabled(value);
                break;
            case 3:
                action1.setEnabled(value);
                action2.setEnabled(value);
                action3.setEnabled(value);
                break;
            case 4:
                action1.setEnabled(value);
                action2.setEnabled(value);
                action3.setEnabled(value);
                action4.setEnabled(value);
        }
    }

    private void useSpell(int spellIndex) {
        setButtonsEnabled(false);
        AbstractSpell spell = animal.getActiveSpells().get(spellIndex);
        instruction.setText(animal.getName() + " lance " + spell.getName() + " !");
        ArrayList<ArrayList<Creature>> fightResult = spell.use(fighters,opponents,0,null);
        fighters = fightResult.get(0);
        opponents = fightResult.get(1);
        int animationType = spell.getAnimationType();
        switch (animationType){
            case AbstractSpell.ATTACK:
                attackFromFighterAnimation();
                opponentsLifePoint = LosesLifeAnimation(opponentsLife, opponentsLifePoint, spell.getValue(), opponentImage, true);
                break;
            case AbstractSpell.THROW:
                throwObjectToOpponent(spell.getThrowedObject());
                opponentsLifePoint = LosesLifeAnimation(opponentsLife, opponentsLifePoint, spell.getValue(), opponentImage, true);
                break;
            case AbstractSpell.HEAL:
                fightersLifePoint = GainLifeAnimation(fightersLife, fightersLifePoint, spell.getValue(), fightersGainLifeFilter);
                break;
            case AbstractSpell.BUFF:
                fightersLifePoint = GainLifeAnimation(fightersLife, fightersLifePoint, spell.getValue(), fightersGainLifeFilter);
                break;
            case AbstractSpell.DEBUFF:
                attackFromFighterAnimation();
                opponentsLifePoint = LosesLifeAnimation(opponentsLife, opponentsLifePoint, spell.getValue(), opponentImage, true);
                break;
            default:
                attackFromFighterAnimation();
                opponentsLifePoint = LosesLifeAnimation(opponentsLife, opponentsLifePoint, spell.getValue(), opponentImage, true);
        }
    }

    private void ennemyTurn() {
        opponnentsTurn = false;
        if (opponents.get(0).getHealth() > 0)
            ennemyAttack();
        else
            win();

        if (fighters.get(0).getHealth() <= 0)
            lose();
    }

    private void ennemyAttack() {
        Random randomGenerator = new Random();
        int numberOfSpells = opponents.get(0).getActiveSpells().size();
        int randomInt = randomGenerator.nextInt(numberOfSpells);
        AbstractSpell spell = opponents.get(0).getActiveSpells().get(randomInt);
        instruction.setText(opponents.get(0).getName() + " lance " + spell.getName() + " !");
        ArrayList<ArrayList<Creature>> fightResult = spell.use(opponents,fighters,0,null);
        fighters = fightResult.get(1);
        opponents = fightResult.get(0);
        int animationType = spell.getAnimationType();
        switch (animationType){
            case AbstractSpell.ATTACK:
                attackFromOpponentAnimation();
                fightersLifePoint = LosesLifeAnimation(fightersLife, fightersLifePoint, spell.getValue(), fighterImage, false);
                break;
            case AbstractSpell.THROW:
                throwObjectToFighter(spell.getThrowedObject());
                fightersLifePoint = LosesLifeAnimation(fightersLife, fightersLifePoint, spell.getValue(), fighterImage, false);
                break;
            case AbstractSpell.HEAL:
                opponentsLifePoint = GainLifeAnimation(opponentsLife, opponentsLifePoint, spell.getValue(), opponentsGainLifeFilter);
                break;
            case AbstractSpell.BUFF:
                opponentsLifePoint = GainLifeAnimation(opponentsLife, opponentsLifePoint, spell.getValue(), opponentsGainLifeFilter);
                break;
            case AbstractSpell.DEBUFF:
                attackFromOpponentAnimation();
                fightersLifePoint = LosesLifeAnimation(fightersLife, fightersLifePoint, spell.getValue(), fighterImage, false);
                break;
            default:
                attackFromOpponentAnimation();
                fightersLifePoint = LosesLifeAnimation(fightersLife, fightersLifePoint, spell.getValue(), fighterImage, false);
        }
    }

    private void win(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Tu as gagné les récompenses suivantes : Rien")
                .setTitle("Victoire !")
                .setPositiveButton("Youpi !", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        resetFight();
                        Intent intent = new Intent(getApplicationContext(), MapComponent.class);
                        startActivity(intent);
                        finish();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private void lose(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Ton animal s'éteint dans un mignon petit gazouilli.")
                .setTitle("Défaite !")
                .setPositiveButton(":'(", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        resetFight();
                        Intent intent = new Intent(getApplicationContext(), MapComponent.class);
                        startActivity(intent);
                        finish();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void resetFight() {
        Controller.setAnimal1(tempAnimal);
        Controller.getCurrentObjective().setOpponents(tempOpponents);
    }

    public int LosesLifeAnimation(ProgressBar lifeProgressBar, int lifeProgressBarPoint, int lifePointLost, ImageView creatureImage, boolean isOpponent) {

        //you can only have one animation per imageVIew so creating an animationSet of multiple animation
        AnimationSet as = new AnimationSet(true);
        //please stay dead command
        as.setFillAfter(true);

        //animate injury (blinking animal) last 400ms
        Animation injuryAnimation = AnimationUtils.loadAnimation(this, R.anim.injury_animation);
        injuryAnimation.setStartOffset(animationOffset);
        as.addAnimation(injuryAnimation);

        animationOffset += 400;
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
                LosePointAnimation(lifeProgressBar, lifeProgressBarPoint, lifeProgressBarPoint - pointToLose);
                lifePointLost -= pointToLose;
                lifeProgressBarPoint -= pointToLose;
                animationOffset += pointToLose * 15;

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
            deadAnimation.setStartOffset(animationOffset+300);
            deadAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (opponnentsTurn)
                        ennemyTurn();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            as.addAnimation(deadAnimation);
        }


        creatureImage.startAnimation(as);

        return (lifeProgressBarPoint);
    }

    public int GainLifeAnimation(ProgressBar lifeProgressBar, int lifeProgressBarPoint, int lifePointGain, ImageView lifeFilter) {


        Animation lifeAnimation = AnimationUtils.loadAnimation(this, R.anim.gain_life_animation);
        lifeAnimation.setStartOffset(animationOffset);
        lifeFilter.startAnimation(lifeAnimation);

        Integer[] listTriggerPoints = {0, 20, 40, 100};
        Integer[] listProgressBarColors = {R.drawable.orange_progress_bar, R.drawable.green_progress_bar, R.drawable.green_progress_bar};
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
                LosePointAnimation(lifeProgressBar, lifeProgressBarPoint, lifeProgressBarPoint + pointToGain);
                lifePointGain -= pointToGain;
                lifeProgressBarPoint += pointToGain;
                animationOffset += pointToGain * 15;
            }
        }

        return (lifeProgressBarPoint);
    }

    private void LosePointAnimation(ProgressBar lifeProgressBar, final int start, final int end){
        ObjectAnimator animation = ObjectAnimator.ofInt(lifeProgressBar, "progress", start, end);
        animation.setDuration(Math.abs(start - end) * 15);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setStartDelay(animationOffset+300);
        animation.start();

        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                int value = start - end;
                if (value > 0) {
                    if (opponnentsTurn) {
                        instruction.setText(opponents.get(0).getName() + " perd " + value + "pv");
                    }else{
                        instruction.setText(animal.getName() + " perd " + value + "pv");
                    }
                } else {
                    if (!opponnentsTurn) {
                        instruction.setText(opponents.get(0).getName() + " gagne " + (end - start) + "pv");
                    }else{
                        instruction.setText(animal.getName() + " gagne " + (end - start) + "pv");
                    }
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (opponnentsTurn)
                    ennemyTurn();
                else {
                    setButtonsEnabled(true);
                    instruction.setText(getResources().getString(R.string.combat_instruction_1)+" "+animal.getName()+" "+getResources().getString(R.string.combat_instruction_2));
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void throwObjectToOpponent(String imageNameToThrow){
        throwableObjectToOpponent.setImageResource(getResources().getIdentifier(
                imageNameToThrow, "drawable", getPackageName()));
        Animation throwObjectAnimation = AnimationUtils.loadAnimation(this, R.anim.throw_object_to_opponent_animation);
        throwableObjectToOpponent.startAnimation(throwObjectAnimation);
        animationOffset += 400;
    }

    public void throwObjectToFighter(String imageNameToThrow){
        throwableObjectToFighter.setImageResource(getResources().getIdentifier(
                imageNameToThrow, "drawable", getPackageName()));
        Animation throwObjectAnimation = AnimationUtils.loadAnimation(this, R.anim.throw_object_to_fighter_animation);
        throwableObjectToFighter.startAnimation(throwObjectAnimation);
        animationOffset += 400;
    }

    public void attackFromFighterAnimation(){
        Animation attackAnimation = AnimationUtils.loadAnimation(this, R.anim.attack_fighter_animation);
        fighterImage.startAnimation(attackAnimation);
        animationOffset += 100;
    }

    public void attackFromOpponentAnimation(){
        Animation attackAnimation = AnimationUtils.loadAnimation(this, R.anim.attack_opponent_animation);
        opponentImage.startAnimation(attackAnimation);
        animationOffset += 100;
    }

    @Override
    public void onBackPressed() {
        return;
    }

}
