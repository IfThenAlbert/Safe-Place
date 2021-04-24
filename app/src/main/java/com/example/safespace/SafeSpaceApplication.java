package com.example.safespace;

import android.app.Application;

import com.parse.Parse;


public class SafeSpaceApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("zM9YEL4wfUrIYoD0DLbPSvxykR7SQVe7eWCkNdP6")
                .clientKey("NQGOUYFJubyfU4JftZwI5ydY2miMj8NSf69L8tER")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
