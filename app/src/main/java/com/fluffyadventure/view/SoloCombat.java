package com.fluffyadventure.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
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
import com.fluffyadventure.model.Monster;

import java.util.ArrayList;
import java.util.Random;

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
    private boolean waitingForTarget = false;
    private boolean opponnentsTurn = false;
    private boolean soloCombat;
    private ArrayList<Animal> tempAnimals = new ArrayList<>();
    private ArrayList<Creature> tempOpponents = new ArrayList<>();
    private int currentOpponentIdx;
    private int currentFighterIdx;
    private int targetIdx = 0;
    private ArrayList<Creature> opponents = new ArrayList<>();
    private ArrayList<Creature> fighters= new ArrayList<>();
    private int nbOfAnims = 0;
    private int currentAnim = 0;
    private boolean opponentZeroDown = false;
    private boolean opponentOneDown = false;
    private boolean allyZeroDown = false;
    private boolean allyOneDown = false;
    AbstractSpell spell;

    //Time in ms when the last animation is oven from the moment you launch the first animation
    private int animationOffset;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("FA", "Solo fight...");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupActivity();
        setupFight();
        updateButtons();
    }

    private void updateButtons() {
        action1.setText(getSpellText(0));
        action1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                animationOffset=0;
                useSpell(0);
                opponnentsTurn = true;
            }
        });
        if (fighters.get(currentFighterIdx).getActiveSpells().size() > 1) {
            action2.setText(getSpellText(1));
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
        if (fighters.get(currentFighterIdx).getActiveSpells().size() > 2) {
            action3.setText(getSpellText(2));
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
        if (fighters.get(currentFighterIdx).getActiveSpells().size() > 3) {
            action4.setText(getSpellText(3));
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

    private String getSpellText(int spellIdx) {
        if (currentFighterIdx < fighters.size()) {
            return (fighters.get(currentFighterIdx).getActiveSpells().get(spellIdx).getMaxUses() - fighters.get(currentFighterIdx).getActiveSpells().get(spellIdx).getUses()) + "/" + fighters.get(currentFighterIdx).getActiveSpells().get(spellIdx).getMaxUses() + " " + fighters.get(currentFighterIdx).getActiveSpells().get(spellIdx).getName();
        }
        return "";
    }


    private void setupActivity() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        soloCombat = Controller.getCurrentObjective().isSoloFight();
        if(soloCombat)
            setContentView(R.layout.activity_solo_combat);
        else
            setContentView(R.layout.activity_duo_combat);

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

        if(!soloCombat)
            setupActivityDuo();
    }

    private void setupActivityDuo(){
        opponentsName2 = (TextView) findViewById(R.id.OpponentsName2);
        opponentsLife2 = (ProgressBar) findViewById(R.id.OpponentsLife2);
        opponentImage2 = (ImageView) findViewById(R.id.OpponentImage2);
        opponentsGainLifeFilter2 = (ImageView) findViewById(R.id.OpponentsLifeFilter2);
        fightersName2 = (TextView) findViewById(R.id.FightersName2);
        fightersLife2 = (ProgressBar) findViewById(R.id.FightersLife2);
        fighterImage2 = (ImageView) findViewById(R.id.FighterImage2);
        fightersGainLifeFilter2 = (ImageView) findViewById(R.id.FightersLifeFilter2);
    }

    private void setupFight(){

        currentOpponentIdx = 0;
        currentFighterIdx = 0;

        opponentZeroDown = false;
        opponentOneDown = false;
        allyZeroDown = false;
        allyOneDown = false;

        Animal animal = Controller.getAnimal(1);

        tempAnimals.add(new Animal(Controller.getAnimal1()));
        fighters.add(animal);

        if (!soloCombat) {
            currentOpponentIdx = 1;
            Animal animal2 = Controller.getAnimal(2);
            tempAnimals.add(new Animal(animal2));
            fighters.add(animal2);
        }

        opponents = Controller.getCurrentObjective().getOpponents();


        for (Creature opponent : opponents) {
            tempOpponents.add(new Monster(opponent.getName(), opponent.getType(), opponent.getHealth(), opponent.getStrength(), opponent.getAccuracy(),opponent.getEvasiveness(), opponent.getActiveSpells()));
        }

        setupOpponent(0);
        if (!soloCombat)
            setupOpponent(1);

    }

    private void setupOpponent(int opponentIndex){
        if (opponentIndex == 0) {
            opponentsLifePoint = opponents.get(opponentIndex).getHealth();
            opponentsLife.setMax(opponentsLifePoint);
            opponentsLife.setProgress(opponentsLifePoint);
            opponentsName.setText(opponents.get(opponentIndex).getName());
            String imagePath = opponents.get(opponentIndex).getImagePath();
            opponentImage.setImageResource(
                    getResources().getIdentifier(
                            imagePath, "drawable", getPackageName()));
            opponentImage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (waitingForTarget && opponents.get(0).getHealth() > 0) {
                        useSpellOnEnnemy(0);
                    }
                    return false;
                }
            });
            fightersLifePoint = fighters.get(0).getHealth();
            fightersLife.setMax(fightersLifePoint);
            fightersLife.setProgress(fightersLifePoint);
            fightersName.setText(Controller.getAnimal(1).getName());
            imagePath = fighters.get(0).getImagePath();
            fighterImage.setImageResource(
                    getResources().getIdentifier(
                            imagePath, "drawable", getPackageName()));
        } else if (opponentIndex == 1){
            opponentsLifePoint2 = opponents.get(opponentIndex).getHealth();
            opponentsLife2.setMax(opponentsLifePoint2);
            opponentsLife2.setProgress(opponentsLifePoint2);
            opponentsName2.setText(opponents.get(opponentIndex).getName());
            String imagePath = opponents.get(opponentIndex).getImagePath();
            opponentImage2.setImageResource(
                    getResources().getIdentifier(
                            imagePath, "drawable", getPackageName()));
            opponentImage2.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (waitingForTarget) {
                        int target = 0;
                        if (opponents.size() > 1)
                            target = 1;

                        if (opponents.get(target).getHealth() > 0)
                            useSpellOnEnnemy(target);
                    }
                    return false;
                }
            });
            fightersLifePoint2 = fighters.get(1).getHealth();
            fightersLife2.setMax(fightersLifePoint2);
            fightersLife2.setProgress(fightersLifePoint2);
            fightersName2.setText(Controller.getAnimal(2).getName());
            imagePath = fighters.get(1).getImagePath();
            fighterImage2.setImageResource(
                    getResources().getIdentifier(
                            imagePath, "drawable", getPackageName()));
        }
        instruction.setText(getResources().getString(R.string.combat_instruction_1) + " " + fighters.get(0).getName() + " " + getResources().getString(R.string.combat_instruction_2));
    }


    private void setButtonsEnabled(boolean value) {


//        ArrayList<Button> buttonsList = new ArrayList<>();
//        buttonsList.add(action1);
//        buttonsList.add(action2);
//        buttonsList.add(action3);
//        buttonsList.add(action4);
//
//        for (int i = 0; i < fighters.get(currentFighterIdx).getActiveSpells().size();i++){
//            if (fighters.get(currentFighterIdx).getActiveSpells().get(i).getUses() != fighters.get(currentFighterIdx).getActiveSpells().get(i).getMaxUses())
//                buttonsList.get(i).setEnabled(value);
//        }

        updateButtons();

        switch (fighters.get(currentFighterIdx).getActiveSpells().size()) {
            case 1:
                if (fighters.get(currentFighterIdx).getActiveSpells().get(0).getUses() != fighters.get(currentFighterIdx).getActiveSpells().get(0).getMaxUses())
                    action1.setEnabled(value);
                break;
            case 2:
                if (fighters.get(currentFighterIdx).getActiveSpells().get(0).getUses() != fighters.get(currentFighterIdx).getActiveSpells().get(0).getMaxUses())
                    action1.setEnabled(value);
                if (fighters.get(currentFighterIdx).getActiveSpells().get(1).getUses() != fighters.get(currentFighterIdx).getActiveSpells().get(1).getMaxUses())
                    action2.setEnabled(value);
                break;
            case 3:
                if (fighters.get(currentFighterIdx).getActiveSpells().get(0).getUses() != fighters.get(currentFighterIdx).getActiveSpells().get(0).getMaxUses())
                    action1.setEnabled(value);
                if (fighters.get(currentFighterIdx).getActiveSpells().get(1).getUses() != fighters.get(currentFighterIdx).getActiveSpells().get(1).getMaxUses())
                    action2.setEnabled(value);
                if (fighters.get(currentFighterIdx).getActiveSpells().get(2).getUses() != fighters.get(currentFighterIdx).getActiveSpells().get(2).getMaxUses())
                    action3.setEnabled(value);
                break;
            case 4:
                if (fighters.get(currentFighterIdx).getActiveSpells().get(0).getUses() != fighters.get(currentFighterIdx).getActiveSpells().get(0).getMaxUses())
                    action1.setEnabled(value);
                if (fighters.get(currentFighterIdx).getActiveSpells().get(1).getUses() != fighters.get(currentFighterIdx).getActiveSpells().get(1).getMaxUses())
                    action2.setEnabled(value);
                if (fighters.get(currentFighterIdx).getActiveSpells().get(2).getUses() != fighters.get(currentFighterIdx).getActiveSpells().get(2).getMaxUses())
                    action3.setEnabled(value);
                if (fighters.get(currentFighterIdx).getActiveSpells().get(3).getUses() != fighters.get(currentFighterIdx).getActiveSpells().get(3).getMaxUses())
                    action4.setEnabled(value);
        }
    }

    private void updateSpellNames() {
        action1.setText(getSpellText(0));
        action2.setText("");
        action3.setText("");
        action4.setText("");

        if (fighters.get(currentFighterIdx).getActiveSpells().size() > 1)
            action2.setText(getSpellText(1));

        if (fighters.get(currentFighterIdx).getActiveSpells().size() > 2)
            action3.setText(getSpellText(2));

        if (fighters.get(currentFighterIdx).getActiveSpells().size() > 3)
            action4.setText(getSpellText(3));
    }

    private void useSpell(int spellIndex) {
        setButtonsEnabled(false);
        if(!soloCombat && opponents.size() == 2)
            currentOpponentIdx = 1 - currentOpponentIdx;
        else
            currentOpponentIdx = 0;
        spell = fighters.get(currentFighterIdx).getActiveSpells().get(spellIndex);

        if (spell.getIsAoE() || spell.getAnimationType() == AbstractSpell.BUFF ||  spell.getAnimationType() == AbstractSpell.HEAL || soloCombat || (opponentZeroDown ||opponentOneDown))
            useSpellOnEnnemy(null);
        else {
            waitingForTarget = true;
            instruction.setText("Sélectionne une cible pour " + spell.getName());
        }

    }

    private void useSpellOnEnnemy(Integer target) {
        waitingForTarget = false;
        instruction.setText(fighters.get(currentFighterIdx).getName() + " lance " + spell.getName() + " !");
        ArrayList<ArrayList<Creature>> fightResult = spell.use(fighters,opponents,currentFighterIdx,target);
        fighters = fightResult.get(0);
        opponents = fightResult.get(1);
        updateSpellNames();

        int offset = -1;
        if (target == null) {
            offset = 0;
            targetIdx = 0;
        } else if (target == 0) {
            offset = 1;
            targetIdx = 0;
        } else if (target == 1)
            targetIdx = 1;

        Runnable ennemyTurn = new Runnable() {
            @Override
            public void run(){
                ennemyTurn();
            }
        };
        Runnable missedDisclaimer = new Runnable() {
            @Override
            public void run(){
                instruction.setText(fighters.get(currentFighterIdx).getName() + " rate son attaque !");
            }
        };
        Handler h = new Handler();

        int animationType = spell.getAnimationType();
        switch (animationType){
            case AbstractSpell.ATTACK:
                attackFromFighterAnimation(currentFighterIdx);
                if (!spell.hasHit()) {
                    h.postDelayed(missedDisclaimer, 1400);
                    h.postDelayed(ennemyTurn, 3000);
                } else {
                    if (!soloCombat && opponentOneDown)
                        opponentsLifePoint = LosesLifeAnimation(opponentsLife, opponentsLifePoint, spell.getValue(), opponentImage, true);
                    else if (!soloCombat && opponentZeroDown)
                        opponentsLifePoint2 = LosesLifeAnimation(opponentsLife2, opponentsLifePoint2, spell.getValue(), opponentImage2, true);
                    else {
                        if ((target == null && soloCombat) || soloCombat)
                            opponentsLifePoint = LosesLifeAnimation(opponentsLife, opponentsLifePoint, spell.getValue(), opponentImage, true);
                        else if (target == null){
                            opponentsLifePoint = LosesLifeAnimation(opponentsLife, opponentsLifePoint, spell.getValue(), opponentImage, true);
                            opponentsLifePoint2 = LosesLifeAnimation(opponentsLife2, opponentsLifePoint2, spell.getValue(), opponentImage2, true);
                        } else if (target == 0)
                            opponentsLifePoint = LosesLifeAnimation(opponentsLife, opponentsLifePoint, spell.getValue(), opponentImage, true);
                        else
                            opponentsLifePoint2 = LosesLifeAnimation(opponentsLife2, opponentsLifePoint2, spell.getValue(), opponentImage2, true);
                    }
                }
                break;
            case AbstractSpell.THROW:
                throwObjectToOpponent(spell.getThrowedObject(),offset);
                if (!spell.hasHit()) {
                    h.postDelayed(missedDisclaimer, 1400);
                    h.postDelayed(ennemyTurn, 3000);
                } else {
                    if (!soloCombat && opponentOneDown)
                        opponentsLifePoint = LosesLifeAnimation(opponentsLife, opponentsLifePoint, spell.getValue(), opponentImage, true);
                    else if (!soloCombat && opponentZeroDown)
                        opponentsLifePoint2 = LosesLifeAnimation(opponentsLife2, opponentsLifePoint2, spell.getValue(), opponentImage2, true);
                    else {
                        if ((target == null && soloCombat) || soloCombat)
                            opponentsLifePoint = LosesLifeAnimation(opponentsLife, opponentsLifePoint, spell.getValue(), opponentImage, true);
                        else if (target == null) {
                            opponentsLifePoint = LosesLifeAnimation(opponentsLife, opponentsLifePoint, spell.getValue(), opponentImage, true);
                            opponentsLifePoint2 = LosesLifeAnimation(opponentsLife2, opponentsLifePoint2, spell.getValue(), opponentImage2, true);
                        } else if (target == 0)
                            opponentsLifePoint = LosesLifeAnimation(opponentsLife, opponentsLifePoint, spell.getValue(), opponentImage, true);
                        else
                            opponentsLifePoint2 = LosesLifeAnimation(opponentsLife2, opponentsLifePoint2, spell.getValue(), opponentImage2, true);
                    }
                }
                break;
            case AbstractSpell.HEAL:
                if (!soloCombat && allyOneDown) {
                    if (fighters.get(0).getHealth() > tempAnimals.get(0).getHealth()) {
                        int value = fighters.get(0).getHealth() - tempAnimals.get(0).getHealth();
                        fightersLifePoint = GainLifeAnimation(fightersLife, fightersLifePoint, spell.getValue() - value, fightersGainLifeFilter);
                        fighters.get(0).setHealth(tempAnimals.get(0).getHealth());
                    } else {
                        fightersLifePoint = GainLifeAnimation(fightersLife, fightersLifePoint, spell.getValue(), fightersGainLifeFilter);
                    }
                } else if (!soloCombat && allyZeroDown) {
                    if (fighters.get(0).getHealth() > tempAnimals.get(0).getHealth()) {
                        int value = fighters.get(0).getHealth() - tempAnimals.get(0).getHealth();
                        fightersLifePoint2 = GainLifeAnimation(fightersLife2, fightersLifePoint2, spell.getValue() - value, fightersGainLifeFilter2);
                        fighters.get(0).setHealth(tempAnimals.get(0).getHealth());
                    } else {
                        fightersLifePoint2 = GainLifeAnimation(fightersLife2, fightersLifePoint2, spell.getValue(), fightersGainLifeFilter2);
                    }
                } else {
                    if(spell.getIsAoE() && !soloCombat) {
                        if (fighters.get(0).getHealth() > tempAnimals.get(0).getHealth()) {
                            int value = fighters.get(0).getHealth() - tempAnimals.get(0).getHealth();
                            fightersLifePoint = GainLifeAnimation(fightersLife, fightersLifePoint, spell.getValue() - value, fightersGainLifeFilter);
                            fighters.get(0).setHealth(tempAnimals.get(0).getHealth());
                        } else {
                            fightersLifePoint = GainLifeAnimation(fightersLife, fightersLifePoint, spell.getValue(), fightersGainLifeFilter);
                        }
                        if (fighters.get(1).getHealth() > tempAnimals.get(1).getHealth()) {
                            int value = fighters.get(1).getHealth() - tempAnimals.get(1).getHealth();
                            fightersLifePoint2 = GainLifeAnimation(fightersLife2, fightersLifePoint2, spell.getValue() - value, fightersGainLifeFilter2);
                            fighters.get(1).setHealth(tempAnimals.get(1).getHealth());
                        } else {
                            fightersLifePoint2 = GainLifeAnimation(fightersLife2, fightersLifePoint2, spell.getValue(), fightersGainLifeFilter2);
                        }
                    } else {
                        if (fighters.get(currentFighterIdx).getHealth() > tempAnimals.get(currentFighterIdx).getHealth()) {
                            int value = fighters.get(currentFighterIdx).getHealth() - tempAnimals.get(currentFighterIdx).getHealth();
                            if (currentFighterIdx == 0)
                                fightersLifePoint = GainLifeAnimation(fightersLife, fightersLifePoint, spell.getValue() - value, fightersGainLifeFilter);
                            else
                                fightersLifePoint2 = GainLifeAnimation(fightersLife2, fightersLifePoint2, spell.getValue(), fightersGainLifeFilter2);
                            fighters.get(currentFighterIdx).setHealth(tempAnimals.get(currentFighterIdx).getHealth());
                        } else {
                            if (currentFighterIdx == 0)
                                fightersLifePoint = GainLifeAnimation(fightersLife, fightersLifePoint, spell.getValue(), fightersGainLifeFilter);
                            else
                                fightersLifePoint2 = GainLifeAnimation(fightersLife2, fightersLifePoint2, spell.getValue(), fightersGainLifeFilter2);
                        }
                    }
                }
                break;
            case AbstractSpell.BUFF:
                if (!soloCombat && allyOneDown)
                    fightersLifePoint = GainLifeAnimation(fightersLife, fightersLifePoint, spell.getValue(), fightersGainLifeFilter);
                else if (!soloCombat && allyZeroDown)
                    fightersLifePoint2 = GainLifeAnimation(fightersLife2, fightersLifePoint2, spell.getValue(), fightersGainLifeFilter2);
                else {
                    if (currentFighterIdx == 0)
                        fightersLifePoint = GainLifeAnimation(fightersLife, fightersLifePoint, spell.getValue(), fightersGainLifeFilter);
                    else
                        fightersLifePoint2 = GainLifeAnimation(fightersLife2, fightersLifePoint2, spell.getValue(), fightersGainLifeFilter2);
                }
                break;
            case AbstractSpell.DEBUFF:
                attackFromFighterAnimation(currentFighterIdx);
                if (!spell.hasHit()) {
                    h.postDelayed(missedDisclaimer, 1400);
                    h.postDelayed(ennemyTurn, 3000);
                } else {
                    if (!soloCombat && opponentOneDown)
                        opponentsLifePoint = LosesLifeAnimation(opponentsLife, opponentsLifePoint, spell.getValue(), opponentImage, true);
                    else if (!soloCombat && opponentZeroDown)
                        opponentsLifePoint2 = LosesLifeAnimation(opponentsLife2, opponentsLifePoint2, spell.getValue(), opponentImage2, true);
                    else {
                        if ((target == null && soloCombat) || soloCombat)
                            opponentsLifePoint = LosesLifeAnimation(opponentsLife, opponentsLifePoint, spell.getValue(), opponentImage, true);
                        else if (target == null) {
                            opponentsLifePoint = LosesLifeAnimation(opponentsLife, opponentsLifePoint, spell.getValue(), opponentImage, true);
                            opponentsLifePoint2 = LosesLifeAnimation(opponentsLife2, opponentsLifePoint2, spell.getValue(), opponentImage2, true);
                        } else if (target == 0)
                            opponentsLifePoint = LosesLifeAnimation(opponentsLife, opponentsLifePoint, spell.getValue(), opponentImage, true);
                        else
                            opponentsLifePoint2 = LosesLifeAnimation(opponentsLife2, opponentsLifePoint2, spell.getValue(), opponentImage2, true);
                    }
                }
                break;
            default:
        }
    }

    private void ennemyTurn() {
        opponnentsTurn = false;
        if (opponents.get(currentOpponentIdx).getHealth() > 0)
            ennemyAttack();
    }

    private void ennemyAttack() {
        if(!soloCombat && fighters.size() == 2)
            currentFighterIdx = 1 - currentFighterIdx;
        else
            currentFighterIdx = 0;
        int numberOfSpells = opponents.get(currentOpponentIdx).getActiveSpells().size();
        boolean usableSpell = false;
        Random randomGenerator = new Random();
        while(!usableSpell) {
            int randomInt = randomGenerator.nextInt(numberOfSpells);
            spell = opponents.get(currentOpponentIdx).getActiveSpells().get(randomInt);
            if (spell.getUses() != spell.getMaxUses() - 1)
                usableSpell = true;
            if (spell.getAnimationType() == AbstractSpell.HEAL && opponents.get(currentOpponentIdx).getHealth() > 50)
                usableSpell = false;
        }
        instruction.setText(opponents.get(currentOpponentIdx).getName() + " lance " + spell.getName() + " !");

        Integer target;
        if (spell.getIsAoE() || soloCombat || ((spell.getAnimationType() == AbstractSpell.DEBUFF ||
                spell.getAnimationType() == AbstractSpell.ATTACK || spell.getAnimationType() == AbstractSpell.THROW) && fighters.size() == 1)
                    || ((spell.getAnimationType() == AbstractSpell.HEAL || spell.getAnimationType() == AbstractSpell.BUFF) && opponents.size() == 1)) {
            target = null;
            targetIdx = 0;
        } else {
            Random randomGenerator2 = new Random();
            target = randomGenerator2.nextInt(2);
            targetIdx = target;
        }

        ArrayList<ArrayList<Creature>> fightResult = spell.use(opponents,fighters,currentOpponentIdx,target);

        fighters = fightResult.get(1);
        opponents = fightResult.get(0);

        Runnable fighterTurn = new Runnable() {
            @Override
            public void run(){
                setButtonsEnabled(true);
                instruction.setText(getResources().getString(R.string.combat_instruction_1) + " " + fighters.get(currentFighterIdx).getName() + " " + getResources().getString(R.string.combat_instruction_2));
            }
        };
        Runnable missedDisclaimer = new Runnable() {
            @Override
            public void run(){
                instruction.setText(opponents.get(currentOpponentIdx).getName() + " rate son attaque !");
            }
        };

        Handler h = new Handler();

        int animationType = spell.getAnimationType();
        switch (animationType){
            case AbstractSpell.ATTACK:
                attackFromOpponentAnimation(currentOpponentIdx);
                if (!spell.hasHit()) {
                    h.postDelayed(missedDisclaimer, 1400);
                    h.postDelayed(fighterTurn, 3000);
                } else {
                    if (!soloCombat && allyOneDown)
                        fightersLifePoint = LosesLifeAnimation(fightersLife, fightersLifePoint, spell.getValue(), fighterImage, false);
                    else if (!soloCombat && allyZeroDown)
                        fightersLifePoint2 = LosesLifeAnimation(fightersLife2, fightersLifePoint2, spell.getValue(), fighterImage2, false);
                    else {
                        if ((target == null && soloCombat) || soloCombat)
                            fightersLifePoint = LosesLifeAnimation(fightersLife, fightersLifePoint, spell.getValue(), fighterImage, false);
                        else if (target == null) {
                            fightersLifePoint = LosesLifeAnimation(fightersLife, fightersLifePoint, spell.getValue(), fighterImage, false);
                            fightersLifePoint2 = LosesLifeAnimation(fightersLife2, fightersLifePoint2, spell.getValue(), fighterImage2, false);
                        } else if (target == 0)
                            fightersLifePoint = LosesLifeAnimation(fightersLife, fightersLifePoint, spell.getValue(), fighterImage, false);
                        else
                            fightersLifePoint2 = LosesLifeAnimation(fightersLife2, fightersLifePoint2, spell.getValue(), fighterImage2, false);
                    }
                }
                break;
            case AbstractSpell.THROW:
                throwObjectToFighter(spell.getThrowedObject(),currentOpponentIdx);
                if (!spell.hasHit()) {
                    h.postDelayed(missedDisclaimer, 1400);
                    h.postDelayed(fighterTurn, 3000);
                } else {
                    if (!soloCombat && allyOneDown)
                        fightersLifePoint = LosesLifeAnimation(fightersLife, fightersLifePoint, spell.getValue(), fighterImage, false);
                    else if (!soloCombat && allyZeroDown)
                        fightersLifePoint2 = LosesLifeAnimation(fightersLife2, fightersLifePoint2, spell.getValue(), fighterImage2, false);
                    else {
                        if ((target == null && soloCombat) || soloCombat)
                            fightersLifePoint = LosesLifeAnimation(fightersLife, fightersLifePoint, spell.getValue(), fighterImage, false);
                        else if (target == null) {
                            fightersLifePoint = LosesLifeAnimation(fightersLife, fightersLifePoint, spell.getValue(), fighterImage, false);
                            fightersLifePoint2 = LosesLifeAnimation(fightersLife2, fightersLifePoint2, spell.getValue(), fighterImage2, false);
                        } else if (target == 0)
                            fightersLifePoint = LosesLifeAnimation(fightersLife, fightersLifePoint, spell.getValue(), fighterImage, false);
                        else
                            fightersLifePoint2 = LosesLifeAnimation(fightersLife2, opponentsLifePoint2, spell.getValue(), fighterImage2, false);
                    }
                }
                break;
            case AbstractSpell.HEAL:
                if (!soloCombat && opponentOneDown) {
                    if (opponents.get(0).getHealth() > tempOpponents.get(0).getHealth()) {
                        int value = opponents.get(0).getHealth() - tempOpponents.get(0).getHealth();
                        opponentsLifePoint = GainLifeAnimation(opponentsLife, opponentsLifePoint, spell.getValue() - value, opponentsGainLifeFilter);
                        opponents.get(0).setHealth(tempOpponents.get(0).getHealth());
                    } else {
                        opponentsLifePoint = GainLifeAnimation(opponentsLife, opponentsLifePoint, spell.getValue(), opponentsGainLifeFilter);
                    }
                } else if (!soloCombat && opponentZeroDown) {
                    if (opponents.get(0).getHealth() > tempOpponents.get(0).getHealth()) {
                        int value = opponents.get(0).getHealth() - tempOpponents.get(0).getHealth();
                        opponentsLifePoint2 = GainLifeAnimation(opponentsLife2, opponentsLifePoint2, spell.getValue() - value, opponentsGainLifeFilter2);
                        opponents.get(0).setHealth(tempOpponents.get(0).getHealth());
                    } else {
                        opponentsLifePoint2 = GainLifeAnimation(opponentsLife2, opponentsLifePoint2, spell.getValue(), opponentsGainLifeFilter2);
                    }
                } else {
                    if(spell.getIsAoE() && !soloCombat) {
                        if (opponents.get(0).getHealth() > tempOpponents.get(0).getHealth()) {
                            int value = opponents.get(0).getHealth() - tempOpponents.get(0).getHealth();
                            opponentsLifePoint = GainLifeAnimation(opponentsLife, opponentsLifePoint, spell.getValue() - value, opponentsGainLifeFilter);
                            opponents.get(0).setHealth(tempOpponents.get(0).getHealth());
                        } else {
                            opponentsLifePoint = GainLifeAnimation(opponentsLife, opponentsLifePoint, spell.getValue(), opponentsGainLifeFilter);
                        }
                        if (opponents.get(1).getHealth() > tempOpponents.get(1).getHealth()) {
                            int value = opponents.get(1).getHealth() - tempOpponents.get(1).getHealth();
                            opponentsLifePoint2 = GainLifeAnimation(opponentsLife2, opponentsLifePoint2, spell.getValue() - value, opponentsGainLifeFilter2);
                            opponents.get(1).setHealth(tempOpponents.get(1).getHealth());
                        } else {
                            opponentsLifePoint2 = GainLifeAnimation(opponentsLife2, opponentsLifePoint2, spell.getValue(), opponentsGainLifeFilter2);
                        }
                    } else {
                        if (opponents.get(currentOpponentIdx).getHealth() > tempOpponents.get(currentOpponentIdx).getHealth()) {
                            int value = opponents.get(currentOpponentIdx).getHealth() - tempOpponents.get(currentOpponentIdx).getHealth();
                            if (currentOpponentIdx == 0)
                                opponentsLifePoint = GainLifeAnimation(opponentsLife, opponentsLifePoint, spell.getValue() - value, opponentsGainLifeFilter);
                            else
                                opponentsLifePoint2 = GainLifeAnimation(opponentsLife2, opponentsLifePoint2, spell.getValue(), opponentsGainLifeFilter2);
                            opponents.get(currentOpponentIdx).setHealth(tempOpponents.get(currentOpponentIdx).getHealth());
                        } else {
                            System.out.println("Ennemy Heal");
                            if (currentOpponentIdx == 0)
                                opponentsLifePoint = GainLifeAnimation(opponentsLife, opponentsLifePoint, spell.getValue(), opponentsGainLifeFilter);
                            else
                                opponentsLifePoint2 = GainLifeAnimation(opponentsLife2, opponentsLifePoint2, spell.getValue(), opponentsGainLifeFilter2);
                        }
                    }
                }
            case AbstractSpell.BUFF:
                if (!soloCombat && opponentOneDown)
                    opponentsLifePoint = GainLifeAnimation(opponentsLife, opponentsLifePoint, spell.getValue(), opponentsGainLifeFilter);
                else if (!soloCombat && opponentZeroDown)
                    opponentsLifePoint2 = GainLifeAnimation(opponentsLife2, opponentsLifePoint2, spell.getValue(), opponentsGainLifeFilter2);
                else {
                    if (currentOpponentIdx == 0)
                        opponentsLifePoint = GainLifeAnimation(opponentsLife, opponentsLifePoint, spell.getValue(), opponentsGainLifeFilter);
                    else
                        opponentsLifePoint2 = GainLifeAnimation(opponentsLife2, opponentsLifePoint2, spell.getValue(), opponentsGainLifeFilter2);
                }
                break;
            case AbstractSpell.DEBUFF:
                attackFromOpponentAnimation(currentOpponentIdx);
                if (!spell.hasHit()) {
                    h.postDelayed(missedDisclaimer, 1400);
                    h.postDelayed(fighterTurn, 3000);
                } else {
                    if (!soloCombat && allyOneDown)
                        fightersLifePoint = LosesLifeAnimation(fightersLife, fightersLifePoint, spell.getValue(), fighterImage, false);
                    else if (!soloCombat && allyZeroDown)
                        fightersLifePoint2 = LosesLifeAnimation(fightersLife2, fightersLifePoint2, spell.getValue(), fighterImage2, false);
                    else {
                        if ((target == null && soloCombat) || soloCombat)
                            fightersLifePoint = LosesLifeAnimation(fightersLife, fightersLifePoint, spell.getValue(), fighterImage, false);
                        else if (target == null) {
                            fightersLifePoint = LosesLifeAnimation(fightersLife, fightersLifePoint, spell.getValue(), fighterImage, false);
                            fightersLifePoint2 = LosesLifeAnimation(fightersLife2, fightersLifePoint2, spell.getValue(), fighterImage2, false);
                        } else if (target == 0)
                            fightersLifePoint = LosesLifeAnimation(fightersLife, fightersLifePoint, spell.getValue(), fighterImage, false);
                        else
                            fightersLifePoint2 = LosesLifeAnimation(fightersLife2, fightersLifePoint2, spell.getValue(), fighterImage2, false);
                    }
                }
                break;
            default:
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private void win(){
        resetFight();
        Controller.success(Controller.getCurrentObjective().getSpawnId());

        if (Controller.getCurrentObjective().getSpellReward() >= 0) {
            GetSpellTask spellTask = new GetSpellTask();
            spellTask.execute();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String message = "Tu as gagné les récompenses suivantes : \n";
            if (Controller.getCurrentObjective().getHealthReward() > 0)
                message += " + " + Controller.getCurrentObjective().getHealthReward() + " pv max \n";
            if (Controller.getCurrentObjective().getStrengthReward() > 0) {
                message += " + " + Controller.getCurrentObjective().getStrengthReward() + " force \n";
                Controller.getAnimal(1).gainStrength(Controller.gainReward());
            }

            builder.setMessage(message)
                    .setTitle("Victoire !")
                    .setPositiveButton("Youpi !", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(getApplicationContext(), MapComponent.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
            SaveTask task = new SaveTask();
            task.execute();
        }
    }

    private void lose(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String message = "";
        if(soloCombat)
            message = "Ton animal s'évanoui dans un mignon petit gazouilli.";
        else
            message = "Tes animaux s'évanouissent dans un mignon petit gazouilli.";

        builder.setMessage(message)
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

        int nbFighters = 1;
        if (!soloCombat)
            nbFighters = 2;

        for (int k = 0; k < nbFighters; k++) {
            for (int i = 0; i < tempAnimals.get(k).getActiveSpells().size(); i++) {
                tempAnimals.get(k).getActiveSpells().get(i).resetUses();
            }
        }

        Controller.setAnimal1(tempAnimals.get(0));
        if (!soloCombat)
            Controller.setAnimal2(tempAnimals.get(1));
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
                    if (opponnentsTurn) {
                        if (!soloCombat){
                        opponents.remove(currentOpponentIdx);
                        if (currentOpponentIdx == 0) {
                            opponentZeroDown = true;
                            opponentImage.setOnTouchListener(null);
                        } else {
                            opponentOneDown = true;
                            opponentImage2.setOnTouchListener(null);
                        }

                        currentFighterIdx = 1 - currentFighterIdx;
                        currentOpponentIdx = 0;
                        if (opponents.size() == 0)
                            win();
                        else
                            ennemyTurn();

                        } else
                            win();
                    } else {
                        if (!soloCombat) {
                            fighters.remove(currentFighterIdx);
                            if (currentFighterIdx == 0)
                                allyZeroDown = true;
                            else
                                allyOneDown = true;

                            currentOpponentIdx = 1 - currentOpponentIdx;
                            currentFighterIdx = 0;
                            if (fighters.size() == 0)
                                lose();
                        } else
                            lose();
                    }

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

        Animation lifeAnimation;
        if (spell.getAnimationType() == AbstractSpell.BUFF) {
            lifeFilter.setImageResource(R.drawable.buff);
            lifeAnimation = AnimationUtils.loadAnimation(this, R.anim.buff_animation);
        } else {
            lifeFilter.setImageResource(R.drawable.life4);
            lifeAnimation = AnimationUtils.loadAnimation(this, R.anim.gain_life_animation);
        }
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
                    if (spell.getAnimationType() == AbstractSpell.BUFF) {
                        if (opponnentsTurn) {
                            if (!spell.getIsAoE() || soloCombat) {
                                instruction.setText("Les caractéristiques de " + fighters.get(targetIdx).getName() + " augmentent !");
                            } else {
                                instruction.setText("Les caractéristiques de tes compagnons augmentent !");
                            }
                        } else {
                            if (!spell.getIsAoE() || soloCombat) {
                                instruction.setText("Les caractéristiques de " + opponents.get(targetIdx).getName() + " augmentent !");
                            } else {
                                instruction.setText("Les caractéristiques des ennemis augmentent !");
                            }
                        }
                    } else if (spell.getAnimationType() == AbstractSpell.DEBUFF) {
                        if (opponnentsTurn) {
                            if (!spell.getIsAoE() || soloCombat) {
                                instruction.setText("Les caractéristiques de " + opponents.get(targetIdx).getName() + " diminuent !");
                            } else {
                                instruction.setText("Les caractéristiques des ennemis diminuent !");
                            }
                        } else {
                            if (!spell.getIsAoE() || soloCombat) {
                                instruction.setText("Les caractéristiques de " + fighters.get(targetIdx).getName() + " diminuent !");
                            } else {
                                instruction.setText("Les caractéristiques de tes compagnons diminuent !");
                            }
                        }
                    } else if (spell.getAnimationType() != AbstractSpell.HEAL) {
                        if (opponnentsTurn) {
                            if (!spell.getIsAoE() || soloCombat) {
                                instruction.setText(opponents.get(targetIdx).getName() + " perd " + value + "pv");
                            } else {
                                instruction.setText("Les ennemis perdent " + value + "pv");
                            }
                        } else {
                            if (!spell.getIsAoE() || soloCombat) {
                                instruction.setText(fighters.get(targetIdx).getName() + " perd " + value + "pv");
                            } else {
                                instruction.setText("Tes compagnons perdent " + value + "pv");
                            }
                        }
                    } else {
                        if (!opponnentsTurn) {
                            if (!spell.getIsAoE() || soloCombat) {
                                instruction.setText(opponents.get(targetIdx).getName() + " gagne " + value + "pv");
                            } else {
                                instruction.setText("Les ennemis gagnent " + value + "pv");
                            }
                        } else {
                            if (!spell.getIsAoE() || soloCombat) {
                                instruction.setText(fighters.get(targetIdx).getName() + " gagne " + value + "pv");
                            } else {
                                instruction.setText("Tes compagnons gagnent " + value + "pv");
                            }
                        }
                    }
                }
                currentAnim++;
            }

            @Override
            public void onAnimationEnd(Animator animation) {


                    Runnable endTurn = new Runnable() {
                        @Override
                        public void run(){
                            if (currentAnim == nbOfAnims) {
                                currentAnim = 0;
                                nbOfAnims = 0;
                                if (opponnentsTurn) {
                                    if (opponents.size() > 0 && opponents.get(currentOpponentIdx).getHealth() > 0)
                                        ennemyTurn();
                                } else {
                                    setButtonsEnabled(true);
                                    instruction.setText(getResources().getString(R.string.combat_instruction_1) + " " + fighters.get(currentFighterIdx).getName() + " " + getResources().getString(R.string.combat_instruction_2));
                                }
                            }
                        }
                    };

                    Handler h = new Handler();
                    h.postDelayed(endTurn, 1700);
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

    @Override
    public void onBackPressed() {
        return;
    }


    private class SaveTask extends AsyncTask<Void, Void, Boolean> {

        public SaveTask() {

        }

        protected void onPreExecute(){

        }
        protected Boolean doInBackground(Void... params){
            Boolean result = Controller.saveGame();
            return result;
        }
        protected  void onPostExecute(Boolean login) {

            if (!login){
                Toast.makeText(SoloCombat.this, "Echec de la sauvegarde", Toast.LENGTH_LONG).show();

            }

        }
    }

    private class GetSpellTask extends AsyncTask<Void, Void, Boolean> {

        AlertDialog.Builder builder;

        public GetSpellTask() {

        }

        protected void onPreExecute(){

        }

        protected Boolean doInBackground(Void... params){
            builder = new AlertDialog.Builder(SoloCombat.this);
            String message = "Tu as gagné les récompenses suivantes : \n";
            if (Controller.getCurrentObjective().getHealthReward() > 0) {
                message += " + " + Controller.getCurrentObjective().getHealthReward() + " pv max \n";
                Controller.getAnimal(1).gainHealth(Controller.getCurrentObjective().getHealthReward());
            }
            if (Controller.getCurrentObjective().getStrengthReward() > 0) {
                message += " + " + Controller.getCurrentObjective().getStrengthReward() + " force \n";
                Controller.getAnimal(1).gainStrength(Controller.gainReward());
            }

            ArrayList<Integer> spellRewardQuery = new ArrayList<>();
            spellRewardQuery.add(Controller.getCurrentObjective().getSpellReward());
            AbstractSpell spellReward = Controller.getSpells(spellRewardQuery, Controller.getAnimal(1).getType()).get(0);
            Controller.getAnimal(1).addSpell(spellReward,false);
            message += "Le sort \"" + spellReward.getName() +"\" !";
            builder.setMessage(message)
                    .setTitle("Victoire !")
                    .setPositiveButton("Youpi !", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(getApplicationContext(), MapComponent.class);
                            startActivity(intent);
                            finish();
                        }
                    });

            return true;
        }

        protected  void onPostExecute(Boolean login) {
            AlertDialog dialog = builder.create();
            dialog.show();
            SaveTask task = new SaveTask();
            task.execute();
        }
    }

}