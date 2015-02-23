package com.fluffyadventure.view;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class SoloCombat extends ActionBarActivity {

    TextView opponentsName;
    TextView fightersName;
    TextView instruction;
    Button action1;
    Button action2;
    Button action3;
    Button action4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solo_combat);

        opponentsName = (TextView)findViewById(R.id.Instruction);
        fightersName = (TextView)findViewById(R.id.FightersName);
        instruction = (TextView)findViewById(R.id.Instruction);
        action1 = (Button)findViewById(R.id.Action1);
        action2 = (Button)findViewById(R.id.Action2);
        action3 = (Button)findViewById(R.id.Action3);
        action4 = (Button)findViewById(R.id.Action4);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_solo_combat, menu);
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
}
