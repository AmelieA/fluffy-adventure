package com.fluffyadventure.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ReadMailActivity extends Activity {

    TextView textMailSender;
    TextView textMailBody;
    Button btnMailBox;
    Button btnAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_mail);
        Typeface font = Typeface.createFromAsset(getAssets(), "GrandHotel-Regular.otf");
        textMailBody=(TextView) findViewById(R.id.TextMailBody);

        textMailSender=(TextView) findViewById(R.id.TextMailSender);

        btnMailBox=(Button) findViewById(R.id.BtnInBox);
        btnMailBox.setTypeface(font);
        btnMailBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReadMailActivity.this, MailBox.class);
                startActivity(intent);
                finish();
            }
        });
        btnAnswer=(Button)findViewById(R.id.BtnAnswer);
        btnAnswer.setTypeface(font);
        btnAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReadMailActivity.this, WriteMailActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_read_mail, menu);
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
