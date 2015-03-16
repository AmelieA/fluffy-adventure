package com.fluffyadventure.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fluffyadventure.controller.Controller;
import com.fluffyadventure.model.Friend;
import com.fluffyadventure.view.R;

public class WriteMailActivity extends Activity {

    TextView textRecipient;
    EditText mailBody;
    EditText mailObject;
    Button btnCancelMail;
    Button btnSendMail;
    String recipientName;
    int recipientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_mail);
        textRecipient = (TextView)findViewById(R.id.TextMailRecipient);
        mailObject = (EditText) findViewById(R.id.TextMailObject);
        btnSendMail = (Button)findViewById(R.id.BtnSendMail);
        btnCancelMail = (Button)findViewById(R.id.BtnCancelMail);
            //Getting recipient and subject from previous activity
        recipientId = getIntent().getIntExtra("recipientId",-1);
        recipientName = getIntent().getStringExtra("recipientName");
        String subject = getIntent().getStringExtra("subject");
        if (recipientId == -1)
        {
            Toast.makeText(WriteMailActivity.this, "Internal Error: recipient not found", Toast.LENGTH_LONG).show();
            btnSendMail.setEnabled(false);
        } else {
            if (recipientName != null) {
                textRecipient.setText(recipientName);
            } else {
                Toast.makeText(WriteMailActivity.this, "Internal Error: recipient name not found", Toast.LENGTH_LONG).show();
            }
            if (subject!=null){
                mailObject.setText("Re: "+subject);
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
                String object = mailObject.getText().toString();
                String body = mailBody.getText().toString();
                //Send the message
                SendMailTask task = new SendMailTask(recipientName,object,body,WriteMailActivity.this);
                task.execute();
                Intent intent = new Intent(WriteMailActivity.this, Status.class);
                startActivity(intent);
            }
        });
        mailBody.requestFocus();
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

    private class SendMailTask extends AsyncTask<Void, Void, Boolean> {
        private String recipient;
        private String object;
        private String content;

        public SendMailTask(String recipient, String object, String content, Context ctx) {
            this.recipient = recipient;
            this.object =  object;
            this.content = content;
            //this.dialog.setCancelable(true);

        }

        protected Boolean doInBackground(Void... params){



            Boolean result = Controller.sendMail(recipient,object,content);
            //Controller.setUpObjectivesFromServer();

            //Boolean result = true;


            return result;
        }
        protected  void onPostExecute(Boolean connected) {
            Log.i("Server", "Attempted connection");
            if (!connected){
                Toast.makeText(WriteMailActivity.this, "Echec de l'envoi du message", Toast.LENGTH_LONG).show();

            }
            else
            {
                Toast.makeText(WriteMailActivity.this, "Message envoyé", Toast.LENGTH_LONG).show();
            }




        }



    }



}
