package com.fluffyadventure.view;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fluffyadventure.controller.Controller;
import com.fluffyadventure.model.Creature;


public class BunnyChoice extends Fragment {

    TextView choice_explaination;
    TextView bunny_presentation;
    Button OkBtn;
    RadioGroup radioGroup;
    RadioButton selectedButton;
    ViewGroup rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_bunny_choice, container, false);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "GrandHotel-Regular.otf");
        choice_explaination = (TextView) rootView.findViewById(R.id.choice_explaination2);
        choice_explaination.setTypeface(font);

        bunny_presentation = (TextView) rootView.findViewById(R.id.bunny_presentation);
        bunny_presentation.setTypeface(font);

        OkBtn = (Button) rootView.findViewById(R.id.OkBtn);
        OkBtn.setTypeface(font);


        OkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioGroup = (RadioGroup) rootView.findViewById(R.id.radio_group);
                selectedButton = (RadioButton) rootView.findViewById(radioGroup.getCheckedRadioButtonId());
                int idx = radioGroup.indexOfChild(selectedButton);
                Controller.createAnimal1("", "bunny" + idx, Creature.RABBIT);
                Intent intent = new Intent(getActivity(), AnimalName.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return rootView;
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
