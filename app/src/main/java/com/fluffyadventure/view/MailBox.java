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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fluffyadventure.controller.Controller;
import com.fluffyadventure.model.Mail;
import com.fluffyadventure.model.MailWanted;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;


public class MailBox extends Activity {

    ImageButton btnCompose;
    ImageButton btnReload;
    MailAdapter mailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_box);

        Log.d("Mails from controller", Controller.getMails().toString());


        mailAdapter = new MailAdapter(this, Controller.getMails());
        mailAdapter.sort(Mail.mailComparator);
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
                //TODO: uncomment when save Mails method on server is implemented
//                SyncMailsTask task = new SyncMailsTask(MailBox.this);
//                task.execute();
                Intent intent = new Intent(MailBox.this, FriendListActivity.class);
                startActivity(intent);
            }
        });

        btnReload = (ImageButton)findViewById(R.id.BtnReload);
        btnReload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //TODO: uncomment when save Mails method on server is implemented
//                SyncMailsTask task = new SyncMailsTask(MailBox.this);
//                task.execute();
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
            ImageView imgUnread = (ImageView)rowView.findViewById(R.id.imgUnread);
            imgUnread.setImageResource(getResources().getIdentifier(
                    (values.get(position).getRead() ? "mailopened" : "newmail"), "drawable", getPackageName()));
            return rowView;
        }

        public void updateData(ArrayList<Mail> values){
            this.clear();
            this.addAll(values);
        }
    }

    private class SyncMailsTask extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog dialog;
        Context ctx;

        public SyncMailsTask(Context ctx) {
            this.ctx = ctx;
            this.dialog = new ProgressDialog(this.ctx);
        }

        protected void onPreExecute(){
            this.dialog.setTitle("Synchronisation des mails...");
            this.dialog.show();
        }

        protected Boolean doInBackground(Void... params){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Boolean result = Controller.saveMails();
            return result;
        }

        protected  void onPostExecute(Boolean login) {
            System.out.println("done");
            dialog.dismiss();
            if (!login){
                Toast.makeText(ctx, "Impossible de récupérer les mails", Toast.LENGTH_LONG).show();
            }
            mailAdapter.updateData(Controller.getMails());
            mailAdapter.sort(Mail.mailComparator);
            mailAdapter.notifyDataSetChanged();
        }
    }

    public void onBackPressed() {
        //TODO: uncomment when save Mails method on server is implemented
//      SyncMailsTask task = new SyncMailsTask(MailBox.this);
//      task.execute();
        Intent intent = new Intent(MailBox.this, Status.class);
        startActivity(intent);
        finish();

    }

}
