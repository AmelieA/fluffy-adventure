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

import com.fluffyadventure.model.Friend;
import com.fluffyadventure.view.R;

public class WriteMailActivity extends Activity {

    TextView textRecipient;
    EditText mailBody;
    Button btnCancelMail;
    Button btnSendMail;
    String recipientName;
    int recipientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_mail);
        textRecipient = (TextView)findViewById(R.id.TextMailRecipient);
        btnSendMail = (Button)findViewById(R.id.BtnSendMail);
        btnCancelMail = (Button)findViewById(R.id.BtnCancelMail);
            //Getting recipient from previous activity
        recipientId = getIntent().getIntExtra("recipientId",-1);
        recipientName = getIntent().getStringExtra("recipientName");
        if (recipientId == -1)
        {
            textRecipient.setText("Internal Error: recipient not found");
            btnSendMail.setEnabled(false);
        } else {
            if (recipientName != null) {
                textRecipient.setText(recipientName);
            } else {
                textRecipient.setText("Internal Error: recipient name not found");
            }
        }
        mailBody = (EditText)findViewById(R.id.TextMailBody);

        btnCancelMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WriteMailActivity.this, FriendListActivity.class);
                startActivity(intent);
            }
        });

        btnSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String body = mailBody.toString();
                //Send the message
            }
        });
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

    public void onBackPressed() {
        Intent intent = new Intent(WriteMailActivity.this, FriendListActivity.class);
        startActivity(intent);
        finish();

    }
}
