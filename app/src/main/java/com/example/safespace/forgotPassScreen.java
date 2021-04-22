package com.example.safespace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class forgotPassScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_screen);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    };

    public void submitButtonForgot(View v) {
        EditText emailVV = (EditText) findViewById(R.id.inp_forgot_email);

        try{
            String forgotEmail = emailVV.getText().toString();

            ParseUser.requestPasswordResetInBackground(forgotEmail, new RequestPasswordResetCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null) {
                        Toast.makeText(getApplicationContext(),"we have sent a link to your email",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"please make sure you enter a valid email and\nemail must be associated with your account.",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }catch (Exception e) {

        }
    }
}