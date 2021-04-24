package com.example.safespace;

import android.app.Application;

import com.parse.Parse;


public class SafeSpaceApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("your app id")
                .clientKey("client ")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
