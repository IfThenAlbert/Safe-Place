package com.example.safespace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class friendsScreen extends AppCompatActivity {

    private ArrayList<String> friendsName;
    private ArrayList<String> friendsNumber;
    private ArrayList<String> friendsId;
    private String cu;
    private  ArrayAdapter arrd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_screen);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ParseUser current = ParseUser.getCurrentUser();

        if(current == null){
            Intent goHome = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(goHome);
            return;
        };

        cu = current.getObjectId();
        friendsName = new ArrayList<String>();
        friendsNumber = new ArrayList<String>();
        friendsId = new ArrayList<String>();
        ListView lvFriends = (ListView) findViewById(R.id.lst_friends);

        ParseQuery<ParseObject> queryFriends = ParseQuery.getQuery("safeplacefriends");
        queryFriends.whereEqualTo("friendsof",current.getObjectId().toString()).findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null) {

                    for(ParseObject r: objects){
                        friendsName.add(r.getString("name").toString());
                        friendsNumber.add(r.getString("phone").toString());
                        friendsId.add(r.getObjectId());
                    }


                    arrd = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,friendsName.toArray());
                    lvFriends.setAdapter(arrd);
                }
            }
        });

        lvFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParseQuery<ParseObject> qq = ParseQuery.getQuery("safeplacefriends");
                qq.getInBackground(friendsId.get(position), new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        object.deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e==null){
                                    Toast.makeText(getApplicationContext(),"DELETED",Toast.LENGTH_SHORT).show();
                                    friendsId.remove(position);
                                    friendsName.remove(position);
                                    friendsNumber.remove(position);
                                    Intent goRefresh = new Intent(getApplicationContext(),friendsScreen.class);
                                    startActivity(goRefresh);
                                };
                            }
                        });
                    };
                });
            }
        });

        lvFriends.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),""+friendsName.get(position) + " => +63 " + friendsNumber.get(position),Toast.LENGTH_LONG).show();
                return false;
            }
        });

    };

    public void addButton(View v) {
        EditText inpFriendName = (EditText) findViewById(R.id.inp_friend_name);
        EditText inpFriendPhone = (EditText) findViewById(R.id.inp_friend_phone);

        try {
            String name = inpFriendName.getText().toString();
            String phone = inpFriendPhone.getText().toString();

            if(phone.length() == 10) {
                ParseObject friendly = new ParseObject("safeplacefriends");
                friendly.put("friendsof",cu);
                friendly.put("name",""+name);
                friendly.put("phone",""+phone);

                friendly.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e== null){
                            Toast.makeText(getApplicationContext(),"ADDED ! !!", Toast.LENGTH_SHORT).show();
                            Intent goRefresh = new Intent(getApplicationContext(),friendsScreen.class);
                            startActivity(goRefresh);
                        }else{
                            Toast.makeText(getApplicationContext(),"SOMETHING WENT WRONG!!", Toast.LENGTH_SHORT).show();
                        };
                    }
                });
            }else{
                Toast.makeText(getApplicationContext(),"invalid phone number", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Please fill out correctly", Toast.LENGTH_SHORT).show();
        }
    };

    public void backButton(View v){
        Intent goBack = new Intent(getApplicationContext(),homeScreen.class);
        startActivity(goBack);
    }

    @Override
    public void onBackPressed() {
        // prevent user from backing
    }
}