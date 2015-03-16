package com.fluffyadventure.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fluffyadventure.controller.Controller;
import com.fluffyadventure.model.Friend;

import java.util.ArrayList;
import java.util.Arrays;

public class FriendListActivity extends Activity {

    Button btnInBox;

    Button btnAddFriend;

    FriendAdapter friendAdapter;
    Friend friend = new Friend("Un ami","squirrel1",13);
    ArrayList<Friend> friends = new ArrayList<>(Arrays.asList(friend, friend, friend));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        friends = Controller.getFriends();

        btnInBox = (Button)findViewById(R.id.BtnInBox);
        btnInBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendListActivity.this, MailBox.class);
                startActivity(intent);
                finish();
            }
        });

        btnAddFriend = (Button)findViewById(R.id.BtnNew);
        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendListActivity.this, AddFriendActivity.class);
                startActivity(intent);
                finish();
            }
        });

        friendAdapter = new FriendAdapter(this,friends);
        ListView friendsView = (ListView) findViewById(R.id.FriendList);
        friendsView.setAdapter(friendAdapter);
        friendsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Friend entry = (Friend) parent.getItemAtPosition(position);
                Intent intent = new Intent(FriendListActivity.this, WriteMailActivity.class);
                intent.putExtra("recipientId",entry.getId());
                intent.putExtra("recipientName",entry.getName());
                startActivity(intent);
            }
        });
    }

    public void onBackPressed() {
        Intent intent = new Intent(FriendListActivity.this, MailBox.class);
        startActivity(intent);
        finish();

    }

    public class FriendAdapter extends ArrayAdapter<Friend> {
        private final Context context;
        private final ArrayList<Friend> values;

        public FriendAdapter(Context context, ArrayList<Friend> values) {
            super(context,R.layout.friendlist_row_layout,values);
            this.context=context;
            this.values=values;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            View rowView = convertView;
            // reuse views
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.friendlist_row_layout, parent, false);
            ImageView friendPic = (ImageView) rowView.findViewById(R.id.friendAvatar);
            String imagePath = values.get(position).getImage();
            friendPic.setImageResource(getResources().getIdentifier(
                    imagePath, "drawable", getPackageName()));
            TextView friendName = (TextView) rowView.findViewById(R.id.friendName);
            friendName.setText(values.get(position).getName());

            return rowView;
        }

    }

}
