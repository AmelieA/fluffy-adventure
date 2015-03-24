package com.fluffyadventure.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
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
    ImageButton btnMapWanted;
    MailWanted mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wanted);
        textUsername = (TextView)findViewById(R.id.textWantedUsername);
        textAnimal1 = (TextView)findViewById(R.id.textWantedChar1);
        textAnimal2 = (TextView)findViewById(R.id.textWantedChar2);
        imgAnimal1 = (ImageView)findViewById(R.id.imgWantedChar1);
        imgAnimal2 = (ImageView)findViewById(R.id.imgWantedChar2);
        btnMapWanted = (ImageButton)findViewById(R.id.BtnMapWanted);
        btnMapWanted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WantedActivity.this,MapComponent.class);
                intent.putExtra("Longitude",mail.getLongitude());
                intent.putExtra("Latitude",mail.getLatitude());
                startActivity(intent);
                finish();
            }
        }

        );
        mail = getIntent().getParcelableExtra("mail");
        if (mail!=null){
            textUsername.setText(mail.getContent());
            textAnimal1.setText(mail.getAnimal1Name());
            textAnimal2.setText(mail.getAnimal2Name());
            try{
                imgAnimal1.setImageResource(getResources().getIdentifier(
                        mail.getAnimal1Pic(), "drawable", getPackageName()));
                imgAnimal2.setImageResource(getResources().getIdentifier(
                        mail.getAnimal2Pic(), "drawable", getPackageName()));
            } catch (Exception e){
                Log.e("Wanted Activity","Drawable not found");
            }
        }
    }
}
