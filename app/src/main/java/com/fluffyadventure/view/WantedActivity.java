package com.fluffyadventure.view;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.fluffyadventure.model.MailWanted;
import com.fluffyadventure.view.R;

public class WantedActivity extends Activity {

    TextView textUsername;
    TextView textAnimal1;
    TextView textAnimal2;
    ImageView imgAnimal1;
    ImageView imgAnimal2;
    MailWanted mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wanted);
//        mail = getIntent().getParcelableExtra("mail");
//        if (mail!=null){
//            textUsername.setText(mail.getSender());
//            textAnimal1.setText(mail.getAnimal1Name());
//            textAnimal2.setText(mail.getAnimal2Name());
//            imgAnimal1.setImageResource(getResources().getIdentifier(
//                    mail.getAnimal1Pic(), "drawable", getPackageName()));
//            imgAnimal2.setImageResource(getResources().getIdentifier(
//                    mail.getAnimal2Pic(), "drawable", getPackageName()));
//        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wanted, menu);
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
