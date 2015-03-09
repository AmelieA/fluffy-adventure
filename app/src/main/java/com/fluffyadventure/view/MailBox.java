package com.fluffyadventure.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fluffyadventure.model.Mail;

import java.util.ArrayList;
import java.util.Arrays;


public class MailBox extends Activity {

    MailAdapter mailAdapter;
    Mail mail = new Mail(1, "Title", "Ceci est un mail");
    ArrayList<Mail> mails = new ArrayList<>(Arrays.asList(mail, mail, mail, mail, mail, mail, mail, mail, mail, mail, mail, mail));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_box);

        mailAdapter = new MailAdapter(this, mails);
        ListView mailsView = (ListView) findViewById(R.id.MailBox);
        mailsView.setAdapter(mailAdapter);
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

            TextView mailHeader = (TextView) rowView.findViewById(R.id.MailHeader);
            mailHeader.setText(values.get(position).getHeader());
            TextView message = (TextView) rowView.findViewById(R.id.Message);
            message.setText(values.get(position).getMessage());

            return rowView;
        }
    }

}
