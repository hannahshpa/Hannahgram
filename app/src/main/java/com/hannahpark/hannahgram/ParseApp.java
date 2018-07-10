package com.hannahpark.hannahgram;

import android.app.Application;

import com.hannahpark.hannahgram.model.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("hannahgram")
                .clientKey("@string/master_key")
                .server("http://hp-hannahgram.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);

    }
}
