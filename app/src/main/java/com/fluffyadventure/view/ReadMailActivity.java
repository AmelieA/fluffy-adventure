package com.fluffyadventure.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fluffyadventure.controller.Controller;
import com.fluffyadventure.model.Mail;

public class ReadMailActivity extends Activity {

    TextView textMailSender;
    TextView textMailBody;
    TextView textMailSubject;
    Button btnMailBox;
    Button btnAnswer;
    Mail mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_mail);
        textMailBody=(TextView) findViewById(R.id.TextMailBody);
        textMailSender=(TextView) findViewById(R.id.TextMailSender);
        textMailSubject=(TextView) findViewById(R.id.TextMailObject);

        mail = getIntent().getParcelableExtra("mail");
        if (mail != null){
            textMailSender.setText(mail.getSender());
            textMailBody.setText(mail.getContent());
            textMailSubject.setText(mail.getObject());
            Controller.setMailAsRead(true,mail);
        } else {
            textMailSender.clearComposingText();
            textMailBody.clearComposingText();
            textMailSubject.clearComposingText();
            Toast.makeText(ReadMailActivity.this, "Internal Error: mail not found", Toast.LENGTH_LONG).show();
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

    public void onBackPressed() {
        Intent intent = new Intent(ReadMailActivity.this, MailBox.class);
        startActivity(intent);
        finish();

    }
}
