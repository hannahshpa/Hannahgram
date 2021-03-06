package com.hannahpark.hannahgram.model;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

@ParseClassName("Post")
public class Post extends ParseObject {
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_USER = "user";
    private static final String KEY_LIKES = "likes";
    private static final String KEY_COMMENTS = "comments";
    public boolean favorited;

    public Post() {
        favorited = false;
//        setLikes(0);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public Number getLikes() { return getNumber(KEY_LIKES); }

    public void setLikes(Number number) {
        put(KEY_LIKES, number);
        Log.d("likes change to ", String.valueOf(number));
    }

    public ArrayList getComments() {return (ArrayList) getList(KEY_COMMENTS);}
    public void setComments(ArrayList<String> comments) {
        put(KEY_COMMENTS, comments);
    }

    public static class Query extends ParseQuery<Post>{

        public Query() {
            super(Post.class);
        }

        public Query inOrder() {
            orderByAscending("createdAt");
            return this;
        }

        public Query getTop() {
            setLimit(20);
            return this;
        }
        //include the user in a post
        public Query withUser() {
            include("user");
            return this;
        }
    }
}
