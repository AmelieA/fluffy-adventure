package com.fluffyadventure.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.fluffyadventure.controller.Controller;
import com.fluffyadventure.model.Mail;
import com.fluffyadventure.model.MailWanted;
import com.fluffyadventure.view.R;

import java.util.ArrayList;
import java.util.Arrays;


public class MailBox extends Activity {

    ImageButton btnCompose;
    MailAdapter mailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_box);

        mailAdapter = new MailAdapter(this, Controller.getMails());
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

            TextView mailHeader = (TextView) rowView.findViewById(R.id.MailSender);
            mailHeader.setText(values.get(position).getSender());
            TextView message = (TextView) rowView.findViewById(R.id.MailSubject);
            message.setText(values.get(position).getMessage());

            return rowView;
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(MailBox.this, Status.class);
        startActivity(intent);
        finish();

    }

}
