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
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.ImageView;
import android.widget.Toast;

import com.fluffyadventure.controller.Controller;
import com.fluffyadventure.model.AbstractSpell;
import com.fluffyadventure.model.Animal;
import com.fluffyadventure.model.BuffSpell;
import com.fluffyadventure.model.Creature;
import com.fluffyadventure.model.DamageSpell;
import com.fluffyadventure.model.DebuffSpell;
import com.fluffyadventure.model.HealSpell;
import com.fluffyadventure.model.Monster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.ResourceBundle;

public class SoloCombat extends Activity {

    //opponent 1
    private TextView opponentsName;
    private ProgressBar opponentsLife;
    private int opponentsLifePoint;
    private ImageView opponentImage;
    private ImageView opponentsGainLifeFilter;

    //opponent 2
    private TextView opponentsName2;
    private ProgressBar opponentsLife2;
    private int opponentsLifePoint2;
    private ImageView opponentImage2;
    private ImageView opponentsGainLifeFilter2;

    //fighter 1
    private TextView fightersName;
    private ProgressBar fightersLife;
    private int fightersLifePoint;
    private ImageView fighterImage;
    private ImageView fightersGainLifeFilter;

    //fighter 2
    private TextView fightersName2;
    private ProgressBar fightersLife2;
    private int fightersLifePoint2;
    private ImageView fighterImage2;
    private ImageView fightersGainLifeFilter2;

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
//    private boolean soloCombat=Controller.getCurrentObjective().isSoloFight();
    private boolean soloCombat=true;
    private Animal tempAnimal;
    private ArrayList<Creature> tempOpponents = new ArrayList<>();
    private int currentOpponentIdx;
    private ArrayList<Creature> opponents = new ArrayList<>();
    private ArrayList<Creature> fighters= new ArrayList<>();
    private int nbOfAnims = 0;
    private int currentAnim = 0;
    AbstractSpell spell;

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
        animal.addSpell(new HealSpell(0, "Soin", "zut", false, 15, AbstractSpell.HEAL, null, 5), true);
        animal.addSpell(new DamageSpell(1, "Jet de noisette", "zut", false, 170, AbstractSpell.THROW, "hazelnut", 15), true);
        animal.addSpell(new DamageSpell(2, "Charge", "zut", false, 130 , AbstractSpell.ATTACK, null, 30), true);
        animal.addSpell(new BuffSpell(3, "Concentration", "Buff précision et force de 20%", false, 120, 120, 100, AbstractSpell.HEAL, null, 3), true);
        tempAnimal = new Animal(Controller.getAnimal1());
        fighters.add(animal);

        opponents = Controller.getCurrentObjective().getOpponents();
        for (Creature opponent : opponents) {
            tempOpponents.add(new Monster(opponent.getName(), opponent.getType(), opponent.getHealth(), opponent.getStrength(), opponent.getAccuracy(),opponent.getEvasiveness(), opponent.getActiveSpells()));
        }
        currentOpponentIdx = 0;

        //bind the activity
        if(soloCombat){
            setContentView(R.layout.activity_solo_combat);
        }else{
            setContentView(R.layout.activity_duo_combat);
        }

        instruction = (TextView) findViewById(R.id.Instruction);

        //Set up the combat
        if (opponents.size() > 0){
            setupFight(currentOpponentIdx);
            if (!soloCombat) {
                setupFightDuo(1);
            }
        }

        action1.setText((animal.getActiveSpells().get(0).getMaxUses()- animal.getActiveSpells().get(0).getUses()) +"/"+ animal.getActiveSpells().get(0).getMaxUses() +" "+animal.getActiveSpells().get(0).getName());
        action1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                animationOffset=0;
                useSpell(0);
                opponnentsTurn = true;
            }
        });

        if (animal.getActiveSpells().size() > 1) {
            action2.setText((animal.getActiveSpells().get(1).getMaxUses()- animal.getActiveSpells().get(1).getUses()) +"/"+ animal.getActiveSpells().get(1).getMaxUses() +" "+animal.getActiveSpells().get(1).getName());
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
            action3.setText((animal.getActiveSpells().get(2).getMaxUses()- animal.getActiveSpells().get(2).getUses()) +"/"+ animal.getActiveSpells().get(2).getMaxUses() +" "+animal.getActiveSpells().get(2).getName());
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
            action4.setText((animal.getActiveSpells().get(3).getMaxUses()- animal.getActiveSpells().get(3).getUses()) +"/"+ animal.getActiveSpells().get(3).getMaxUses() +" "+animal.getActiveSpells().get(3).getName());
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

    private void setupFightDuo(int opponentIdx){

        opponentsName2 = (TextView) findViewById(R.id.OpponentsName2);
        opponentsLife2 = (ProgressBar) findViewById(R.id.OpponentsLife2);
        opponentImage2 = (ImageView) findViewById(R.id.OpponentImage2);
        opponentsGainLifeFilter2 = (ImageView) findViewById(R.id.OpponentsLifeFilter2);
        fightersName2 = (TextView) findViewById(R.id.FightersName2);
        fightersLife2 = (ProgressBar) findViewById(R.id.FightersLife2);
        fighterImage2 = (ImageView) findViewById(R.id.FighterImage2);
        fightersGainLifeFilter2 = (ImageView) findViewById(R.id.FightersLifeFilter2);

        opponentsLifePoint2 = opponents.get(opponentIdx).getHealth();
        opponentsLife2.setMax(opponentsLifePoint2);
        opponentsName2.setText(opponents.get(opponentIdx).getName());
        String imagePath = "evilbunny";
        opponentImage2.setImageResource(
                getResources().getIdentifier(
                        imagePath, "drawable", getPackageName()));
        fightersLifePoint2 = animal.getHealth();
        fightersLife2.setMax(fightersLifePoint2);
        fightersName2.setText(Controller.getAnimal1().getName());
        imagePath = animal.getImagePath();
        fighterImage2.setImageResource(
                getResources().getIdentifier(
                        imagePath, "drawable", getPackageName()));
    }

    private void updateSpellNames() {
        action1.setText((animal.getActiveSpells().get(0).getMaxUses()- animal.getActiveSpells().get(0).getUses()) +"/"+ animal.getActiveSpells().get(0).getMaxUses() +" "+animal.getActiveSpells().get(0).getName());

        if (animal.getActiveSpells().size() > 1)
            action2.setText((animal.getActiveSpells().get(1).getMaxUses()- animal.getActiveSpells().get(1).getUses()) +"/"+ animal.getActiveSpells().get(1).getMaxUses() +" "+animal.getActiveSpells().get(1).getName());

        if (animal.getActiveSpells().size() > 2)
            action3.setText((animal.getActiveSpells().get(2).getMaxUses()- animal.getActiveSpells().get(2).getUses()) +"/"+ animal.getActiveSpells().get(2).getMaxUses() +" "+animal.getActiveSpells().get(2).getName());

        if (animal.getActiveSpells().size() > 3)
            action4.setText((animal.getActiveSpells().get(3).getMaxUses()- animal.getActiveSpells().get(3).getUses()) +"/"+ animal.getActiveSpells().get(3).getMaxUses() +" "+animal.getActiveSpells().get(3).getName());
    }

    private void setButtonsEnabled(boolean value) {


//        ArrayList<Button> buttonsList = new ArrayList<>();
//        buttonsList.add(action1);
//        buttonsList.add(action2);
//        buttonsList.add(action3);
//        buttonsList.add(action4);
//
//        for (int i = 0; i < animal.getActiveSpells().size();i++){
//            if (animal.getActiveSpells().get(i).getUses() != animal.getActiveSpells().get(i).getMaxUses())
//                buttonsList.get(i).setEnabled(value);
//        }

        switch (animal.getActiveSpells().size()) {
            case 1:
                if (animal.getActiveSpells().get(0).getUses() != animal.getActiveSpells().get(0).getMaxUses())
                    action1.setEnabled(value);
                break;
            case 2:
                if (animal.getActiveSpells().get(0).getUses() != animal.getActiveSpells().get(0).getMaxUses())
                    action1.setEnabled(value);
                if (animal.getActiveSpells().get(1).getUses() != animal.getActiveSpells().get(1).getMaxUses())
                    action2.setEnabled(value);
                break;
            case 3:
                if (animal.getActiveSpells().get(0).getUses() != animal.getActiveSpells().get(0).getMaxUses())
                    action1.setEnabled(value);
                if (animal.getActiveSpells().get(1).getUses() != animal.getActiveSpells().get(1).getMaxUses())
                    action2.setEnabled(value);
                if (animal.getActiveSpells().get(2).getUses() != animal.getActiveSpells().get(2).getMaxUses())
                    action3.setEnabled(value);
                break;
            case 4:
                if (animal.getActiveSpells().get(0).getUses() != animal.getActiveSpells().get(0).getMaxUses())
                    action1.setEnabled(value);
                if (animal.getActiveSpells().get(1).getUses() != animal.getActiveSpells().get(1).getMaxUses())
                    action2.setEnabled(value);
                if (animal.getActiveSpells().get(2).getUses() != animal.getActiveSpells().get(2).getMaxUses())
                    action3.setEnabled(value);
                if (animal.getActiveSpells().get(3).getUses() != animal.getActiveSpells().get(3).getMaxUses())
                    action4.setEnabled(value);
        }
    }

    private void useSpell(int spellIndex) {
        setButtonsEnabled(false);
        spell = animal.getActiveSpells().get(spellIndex);
        instruction.setText(animal.getName() + " lance " + spell.getName() + " !");
        ArrayList<ArrayList<Creature>> fightResult = spell.use(fighters,opponents,0,null);
        fighters = fightResult.get(0);
        opponents = fightResult.get(1);
        updateSpellNames();
        int animalPosition=0;

        Runnable ennemyTurn = new Runnable() {
            @Override
            public void run(){
                ennemyTurn(); //<-- put your code in here.
            }
        };
        Runnable missedDisclaimer = new Runnable() {
            @Override
            public void run(){
                instruction.setText(animal.getName() + " rate son attaque !");
            }
        };
        Handler h = new Handler();

        int animationType = spell.getAnimationType();
        switch (animationType){
            case AbstractSpell.ATTACK:
                attackFromFighterAnimation(animalPosition);
                if (!spell.hasHit()) {
                    h.postDelayed(missedDisclaimer, 1300);
                    h.postDelayed(ennemyTurn, 2800);
                } else {
                    opponentsLifePoint = LosesLifeAnimation(opponentsLife, opponentsLifePoint, spell.getValue(), opponentImage, true);
                }
                break;
            case AbstractSpell.THROW:
                throwObjectToOpponent(spell.getThrowedObject(),animalPosition);
                if (!spell.hasHit()) {
                    h.postDelayed(missedDisclaimer, 1300);
                    h.postDelayed(ennemyTurn, 2800);
                } else {
                    opponentsLifePoint = LosesLifeAnimation(opponentsLife, opponentsLifePoint, spell.getValue(), opponentImage, true);
                }
                break;
            case AbstractSpell.HEAL:
                if (animal.getHealth() > tempAnimal.getHealth()) {
                    int value = animal.getHealth() - tempAnimal.getHealth();
                    fightersLifePoint = GainLifeAnimation(fightersLife, fightersLifePoint, spell.getValue() - value, fightersGainLifeFilter);
                } else {
                    fightersLifePoint = GainLifeAnimation(fightersLife, fightersLifePoint, spell.getValue(), fightersGainLifeFilter);
                }
                    animal.setHealth(tempAnimal.getHealth());
                break;
            case AbstractSpell.BUFF:
                fightersLifePoint = GainLifeAnimation(fightersLife, fightersLifePoint, spell.getValue(), fightersGainLifeFilter);
                break;
            case AbstractSpell.DEBUFF:
                attackFromFighterAnimation(animalPosition);
                if (!spell.hasHit()) {
                    h.postDelayed(missedDisclaimer, 1300);
                    h.postDelayed(ennemyTurn, 2800);
                } else {
                    opponentsLifePoint = LosesLifeAnimation(opponentsLife, opponentsLifePoint, spell.getValue(), opponentImage, true);
                }
                break;
            default:
                attackFromFighterAnimation(animalPosition);
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
        int numberOfSpells = opponents.get(0).getActiveSpells().size();
        boolean usableSpell = false;
        Random randomGenerator = new Random();
        while(!usableSpell) {
            int randomInt = randomGenerator.nextInt(numberOfSpells);
            spell = opponents.get(0).getActiveSpells().get(randomInt);
            if (spell.getUses() != spell.getMaxUses() - 1)
                usableSpell = true;
        }
        instruction.setText(opponents.get(0).getName() + " lance " + spell.getName() + " !");
        ArrayList<ArrayList<Creature>> fightResult = spell.use(opponents,fighters,0,null);
        fighters = fightResult.get(1);
        opponents = fightResult.get(0);
        int animalPosition=0;

        Runnable fighterTurn = new Runnable() {
            @Override
            public void run(){
                setButtonsEnabled(true);
                instruction.setText(getResources().getString(R.string.combat_instruction_1) + " " + animal.getName() + " " + getResources().getString(R.string.combat_instruction_2));
            }
        };
        Runnable missedDisclaimer = new Runnable() {
            @Override
            public void run(){
                instruction.setText(opponents.get(0).getName() + " rate son attaque !");
            }
        };

        Handler h = new Handler();

        int animationType = spell.getAnimationType();
        switch (animationType){
            case AbstractSpell.ATTACK:
                attackFromOpponentAnimation(animalPosition);
                if (!spell.hasHit()) {
                    h.postDelayed(missedDisclaimer, 1300);
                    h.postDelayed(fighterTurn, 2800);
                } else {
                    fightersLifePoint = LosesLifeAnimation(fightersLife, fightersLifePoint, spell.getValue(), fighterImage, false);
                }
                break;
            case AbstractSpell.THROW:
                throwObjectToFighter(spell.getThrowedObject(),animalPosition);
                if (!spell.hasHit()) {
                    h.postDelayed(missedDisclaimer, 1300);
                    h.postDelayed(fighterTurn, 2800);
                } else {
                    fightersLifePoint = LosesLifeAnimation(fightersLife, fightersLifePoint, spell.getValue(), fighterImage, false);
                }
                break;
            case AbstractSpell.HEAL:
                if (opponents.get(currentOpponentIdx).getHealth() > tempOpponents.get(currentOpponentIdx).getHealth()) {
                    int value = opponents.get(currentOpponentIdx).getHealth() - tempOpponents.get(currentOpponentIdx).getHealth();
                    opponentsLifePoint = GainLifeAnimation(opponentsLife, opponentsLifePoint, spell.getValue() - value, opponentsGainLifeFilter);
                } else {
                    opponentsLifePoint = GainLifeAnimation(opponentsLife, opponentsLifePoint, spell.getValue(), opponentsGainLifeFilter);
                }
                break;
            case AbstractSpell.BUFF:
                opponentsLifePoint = GainLifeAnimation(opponentsLife, opponentsLifePoint, spell.getValue(), opponentsGainLifeFilter);
                break;
            case AbstractSpell.DEBUFF:
                attackFromOpponentAnimation(animalPosition);
                if (!spell.hasHit()) {
                    h.postDelayed(missedDisclaimer, 1300);
                    h.postDelayed(fighterTurn, 2800);
                } else {
                    fightersLifePoint = LosesLifeAnimation(fightersLife, fightersLifePoint, spell.getValue(), fighterImage, false);
                }
                break;
            default:
                attackFromOpponentAnimation(animalPosition);
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
                        setButtonsEnabled(false);
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
                .setPositiveButton("Tant pis !", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        resetFight();
                        setButtonsEnabled(false);
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
        for (int i = 0; i < tempOpponents.size(); i++) {
            for (int j = 0; j < tempOpponents.get(i).getActiveSpells().size(); j++) {
                tempOpponents.get(i).getActiveSpells().get(j).resetUses();
            }
        }
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

        lifeFilter.setImageResource(R.drawable.life4);
        Animation lifeAnimation = AnimationUtils.loadAnimation(this, R.anim.gain_life_animation);
        lifeAnimation.setStartOffset(animationOffset);
        lifeFilter.startAnimation(lifeAnimation);

        if (lifeProgressBarPoint >= 100 && lifePointGain == 0) {
            LosePointAnimation(lifeProgressBar, lifeProgressBarPoint, lifeProgressBarPoint);
            return lifeProgressBarPoint;
        }

        animationOffset += 800;
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
        nbOfAnims ++;
        ObjectAnimator animation = ObjectAnimator.ofInt(lifeProgressBar, "progress", start, end);
        animation.setDuration(Math.abs(start - end) * 15);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setStartDelay(animationOffset+300);
        animation.start();

        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (currentAnim == 0) {
                    int value = spell.getValue();
                    if (spell.getAnimationType() != AbstractSpell.HEAL) {
                        if (opponnentsTurn) {
                            instruction.setText(opponents.get(0).getName() + " perd " + value + "pv");
                        } else {
                            instruction.setText(animal.getName() + " perd " + value + "pv");
                        }
                    } else {
                        if (!opponnentsTurn) {
                            instruction.setText(opponents.get(0).getName() + " gagne " + value + "pv");
                        } else {
                            instruction.setText(animal.getName() + " gagne " + value + "pv");
                        }
                    }
                }
                currentAnim++;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (currentAnim == nbOfAnims) {
                    currentAnim = 0;
                    nbOfAnims = 0;
                    try {
                        Thread.sleep(1200);
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
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void throwObjectToOpponent(String imageNameToThrow, int positionOffset){
        throwableObjectToOpponent.setImageResource(getResources().getIdentifier(
                imageNameToThrow, "drawable", getPackageName()));

        String animationPath="throw_object_to_opponent_animation";
        if (positionOffset>0){
            animationPath +="_right";
        }if (positionOffset<0){
            animationPath +="_left";
        }

        int animationId = getResources().getIdentifier(animationPath, "anim", getPackageName());
        Animation throwObjectAnimation = AnimationUtils.loadAnimation(this, animationId);
        throwableObjectToOpponent.startAnimation(throwObjectAnimation);
        animationOffset += 400;
    }

    public void throwObjectToFighter(String imageNameToThrow, int positionOffset){
        throwableObjectToFighter.setImageResource(getResources().getIdentifier(
                imageNameToThrow, "drawable", getPackageName()));

        String animationPath="throw_object_to_fighter_animation";
        if (positionOffset>0){
            animationPath +="_right";
        }if (positionOffset<0){
            animationPath +="_left";
        }

        int animationId = getResources().getIdentifier(animationPath, "anim", getPackageName());
        Animation throwObjectAnimation = AnimationUtils.loadAnimation(this, animationId);
        throwableObjectToFighter.startAnimation(throwObjectAnimation);
        animationOffset += 400;
    }

    public void attackFromFighterAnimation(int fighterPosition){
        Animation attackAnimation = AnimationUtils.loadAnimation(this, R.anim.attack_fighter_animation);
        if (fighterPosition==0) {
            fighterImage.startAnimation(attackAnimation);
        }else{
            fighterImage2.startAnimation(attackAnimation);
        }
        animationOffset += 100;
    }

    public void attackFromOpponentAnimation(int opponentPosition){
        Animation attackAnimation = AnimationUtils.loadAnimation(this, R.anim.attack_opponent_animation);
        if (opponentPosition==0) {
            opponentImage.startAnimation(attackAnimation);
        }else{
            opponentImage2.startAnimation(attackAnimation);
        }
        animationOffset += 100;
    }

    public void buffAnimation(ImageView lifeFilter){

        lifeFilter.setImageResource(R.drawable.buff);
        Animation lifeAnimation = AnimationUtils.loadAnimation(this, R.anim.buff_animation);
        lifeAnimation.setStartOffset(animationOffset);
        lifeFilter.startAnimation(lifeAnimation);

        animationOffset += 800;
    }
    @Override
    public void onBackPressed() {
        return;
    }

}