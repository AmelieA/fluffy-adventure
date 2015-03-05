package com.fluffyadventure.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fluffyadventure.view.R;

public class WriteMailActivity extends Activity {

    TextView textRecipient;
    EditText mailBody;
    Button btnCancelMail;
    Button btnSendMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_mail);

        textRecipient = (TextView)findViewById(R.id.TextMailRecipient);

        mailBody = (EditText)findViewById(R.id.TextMailBody);

        Typeface font = Typeface.createFromAsset(getAssets(), "GrandHotel-Regular.otf");
        btnCancelMail = (Button)findViewById(R.id.BtnCancelMail);
        btnCancelMail.setTypeface(font);
        btnCancelMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WriteMailActivity.this, FriendListActivity.class);
                startActivity(intent);
            }
        });

        btnSendMail = (Button)findViewById(R.id.BtnSendMail);
        btnSendMail.setTypeface(font);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_write_mail, menu);
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