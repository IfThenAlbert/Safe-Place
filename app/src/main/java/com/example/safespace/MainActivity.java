package com.example.safespace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    public void forgotPassButton(View v) {
        Intent goForgot = new Intent(getApplicationContext(),forgotPassScreen.class);
        startActivity(goForgot);
        return;
    }

    public void logMeIn(View v) {
        // this method triggers when user wishes to login using username and password
        EditText usernameField = (EditText) findViewById(R.id.inp_user_name);
        EditText userpassField = (EditText) findViewById(R.id.inp_user_password);

        String username = usernameField.getText().toString();
        String userpass = userpassField.getText().toString();

        if(username.isEmpty() || userpass.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Enter your username and password to login !" , Toast.LENGTH_SHORT).show();
        }else{
            ParseUser.logInInBackground(username, userpass, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if(user != null) { // user login!
                        Intent goUser = new Intent(getApplicationContext(), homeScreen.class);
                        startActivity(goUser);
                        return;
                    }else{ // there is a problem
                        int errorC = e.getCode();
                        if(errorC == 101) {
                            Toast.makeText(getApplicationContext(),"Incorrect Username or Password",Toast.LENGTH_LONG).show();
                        };
                    };
                };
            });
        };
    };

    public void putMeToRegister(View v) {
        // this method triggers when user wishes to register
        Intent goRegister = new Intent(getApplicationContext(),registerScreen.class);
        startActivity(goRegister);
    }

    @Override
    public void onBackPressed() {
        // prevent user backking
    }
}