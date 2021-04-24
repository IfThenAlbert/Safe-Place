package com.example.safespace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class registerScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    };

    public void goRegNow(View v) {
        EditText fnameV = (EditText) findViewById(R.id.inp_new_firstname);
        EditText lnameV = (EditText) findViewById(R.id.inp_new_lastname);
        EditText emailV = (EditText) findViewById(R.id.inp_new_email);
        EditText unameV = (EditText) findViewById(R.id.inp_new_username);
        EditText passV = (EditText) findViewById(R.id.inp_new_pass);
        EditText phoneV = (EditText) findViewById(R.id.inp_new_phone);

        try{
            String nfname = fnameV.getText().toString();
            String nlname = lnameV.getText().toString();
            String nemail = emailV.getText().toString();
            String nuname = unameV.getText().toString();
            String npass = passV.getText().toString();
            String nphone=  phoneV.getText().toString();

            if(nphone.length() == 10) {
                ParseUser newOne = new ParseUser();
                newOne.setEmail(nemail);
                newOne.setUsername(nuname);
                newOne.setPassword(npass);
                newOne.put("firstname",nfname);
                newOne.put("lastname",nlname);
                newOne.put("phonenumber",nphone);

                newOne.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e== null) {
                            Intent goUser = new Intent(getApplicationContext(), homeScreen.class);
                            startActivity(goUser);
                            return;
                        }else{
                            Toast.makeText(getApplicationContext(),"Unable to sign you up",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }else{
                Toast.makeText(getApplicationContext(),"Please  make sure phone number is correct",Toast.LENGTH_LONG).show();
            }


        }catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Something went wrong ...",Toast.LENGTH_LONG).show();
        }

    }
}