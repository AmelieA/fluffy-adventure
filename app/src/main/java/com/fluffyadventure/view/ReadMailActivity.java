package com.fluffyadventure.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fluffyadventure.model.Mail;

public class ReadMailActivity extends Activity {

    TextView textMailSender;
    TextView textMailBody;
    Button btnMailBox;
    Button btnAnswer;
    Mail mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_mail);
        textMailBody=(TextView) findViewById(R.id.TextMailBody);
        textMailSender=(TextView) findViewById(R.id.TextMailSender);

        mail = getIntent().getParcelableExtra("mail");
        if (mail != null){
            textMailSender.setText(mail.getSender());
            textMailBody.setText(mail.getContent());
        } else {
            textMailSender.clearComposingText();
            textMailBody.clearComposingText();
        }

        btnMailBox=(Button) findViewById(R.id.BtnInBox);
        btnMailBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReadMailActivity.this, MailBox.class);
                startActivity(intent);
                finish();
            }
        });
        btnAnswer=(Button)findViewById(R.id.BtnAnswer);
        btnAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReadMailActivity.this, WriteMailActivity.class);
                intent.putExtra("recipientId",mail.getSenderId());
                intent.putExtra("recipientName",mail.getSender());
                intent.putExtra("subject",mail.getObject());
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

    public void onBackPressed() {
        Intent intent = new Intent(ReadMailActivity.this, MailBox.class);
        startActivity(intent);
        finish();

    }
}
