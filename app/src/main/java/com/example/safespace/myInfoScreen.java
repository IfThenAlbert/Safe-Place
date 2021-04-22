package com.example.safespace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TextView;

import com.parse.ParseUser;

public class myInfoScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info_screen);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ParseUser current = ParseUser.getCurrentUser();

//        if(current == null) {
//            Intent goHome = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(goHome);
//            return;
//        };

        TextView tvUserId = (TextView) findViewById(R.id.txtId);
        TextView tvUserName = (TextView) findViewById(R.id.txtName);
        TextView tvUserEmail = (TextView) findViewById(R.id.txtEmail);
        TextView tvUserPhone = (TextView) findViewById(R.id.txtPhone);

        tvUserId.setText("ID: " +  current.getObjectId());
        tvUserName.setText("Name: " + current.get("firstname").toString() + " " + current.get("lastname").toString());
        tvUserEmail.setText("Email: " + current.getEmail());
        tvUserPhone.setText("Phone #: +63 " + current.get("phonenumber"));

    };
}