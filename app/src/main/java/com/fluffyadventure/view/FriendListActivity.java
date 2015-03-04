package com.fluffyadventure.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fluffyadventure.model.Friend;

import java.util.ArrayList;
import java.util.Arrays;

public class FriendListActivity extends Activity {

    FriendAdapter friendAdapter;
    Friend friend = new Friend("Un ami","squirrel1");
    ArrayList<Friend> friends = new ArrayList<>(Arrays.asList(friend, friend, friend));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        friendAdapter = new FriendAdapter(this,friends);
        ListView friendsView = (ListView) findViewById(R.id.FriendList);
        friendsView.setAdapter(friendAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friend_list, menu);
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
