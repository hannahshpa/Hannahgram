package com.hannahpark.hannahgram;

import android.app.Application;

import com.parse.Parse;

public class ParseApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("hannahgram")
                .clientKey("@string/master_key")
                .server("http://hp-hannahgram.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);

    }
}
