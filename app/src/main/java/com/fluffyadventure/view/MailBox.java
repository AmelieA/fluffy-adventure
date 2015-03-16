package com.fluffyadventure.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fluffyadventure.controller.Controller;
import com.fluffyadventure.model.Mail;
import com.fluffyadventure.model.MailWanted;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class MailBox extends Activity {

    ImageButton btnCompose;
    MailAdapter mailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_box);

        Log.d("Mails from controller", Controller.getMails().toString());


        mailAdapter = new MailAdapter(this, Controller.getMails());
        //test Wanted
            mailAdapter.add(new MailWanted(0,"Mission","Attaquez tartampion !","tartampion",0,"Alice","Bob","squirrel1","squirrel2"));
        ListView mailsView = (ListView) findViewById(R.id.MailBox);
        mailsView.setAdapter(mailAdapter);
        mailsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).getClass().equals(MailWanted.class)){
                    MailWanted entry = (MailWanted) parent.getItemAtPosition(position);
                    Intent intent = new Intent(MailBox.this, WantedActivity.class);
                    intent.putExtra("mail", entry);
                    startActivity(intent);
                } else {
                    Mail entry = (Mail) parent.getItemAtPosition(position);
                    Intent intent = new Intent(MailBox.this, ReadMailActivity.class);
                    intent.putExtra("mail", entry);
                    startActivity(intent);
                }
            }
        });

        btnCompose = (ImageButton)findViewById(R.id.BtnCompose);
        btnCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MailBox.this, FriendListActivity.class);
                startActivity(intent);
            }
        });
    }

    public class MailAdapter extends ArrayAdapter<Mail> {
        private final Context context;
        private final ArrayList<Mail> values;

        public MailAdapter(Context context, ArrayList<Mail> values) {
            super(context, R.layout.mail_box_row_layout, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            // reuse views
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.mail_box_row_layout, parent, false);

            TextView mailSender = (TextView) rowView.findViewById(R.id.MailSender);
            mailSender.setText(values.get(position).getSender());
            TextView subject = (TextView) rowView.findViewById(R.id.MailSubject);
            subject.setText(values.get(position).getObject());
            if (!values.get(position).getRead()){
                //do sth
            }

            return rowView;
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(MailBox.this, Status.class);
        startActivity(intent);
        finish();

    }




}
