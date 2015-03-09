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
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.fluffyadventure.model.Mail;
import com.fluffyadventure.model.Spell;
import com.fluffyadventure.view.R;

import java.util.ArrayList;
import java.util.Arrays;


public class MailBox extends Activity {

    ImageButton btnCompose;
    MailAdapter mailAdapter;
    // Liste bidon de mails pour tester
    Mail mail = new Mail(1, "Expediteur", "Ceci est un mail");
    ArrayList<Mail> mails = new ArrayList<>(Arrays.asList(mail, mail, mail, mail, mail, mail, mail, mail, mail, mail, mail, mail));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_box);

        mailAdapter = new MailAdapter(this, mails);
        ListView mailsView = (ListView) findViewById(R.id.MailBox);
        mailsView.setAdapter(mailAdapter);

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
            mailHeader.setText(values.get(position).getHeader());
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
