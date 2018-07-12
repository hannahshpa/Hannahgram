package com.hannahpark.hannahgram;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hannahpark.hannahgram.model.Post;

import org.parceler.Parcels;

import java.util.Date;

public class PostDetailsActivity extends AppCompatActivity {

    Post post;

    public TextView tvUsername;
    public ImageView ivPhoto;
    public TextView tvCaption;
    public TextView tvTime;
    private BottomNavigationView bottomNavigationView;
    public ImageButton ivHeart;
    public TextView tvLikes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.search_button);

        tvUsername = (TextView) findViewById(R.id.tvUsername);
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        tvCaption = (TextView) findViewById(R.id.tvCaption);
        tvTime = (TextView) findViewById(R.id.tvTime);
        ivHeart = (ImageButton) findViewById(R.id.ivHeart);
        tvLikes = (TextView) findViewById(R.id.tvLikes);

        //unwrap post
        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra("post"));

        if(post.getLikes().intValue() != 0)
            ivHeart.setSelected(true);

        //relative time
        //get relative time
        Date currentDate = new Date();
        long currentDateLong = currentDate.getTime();
        long oldDate = post.getCreatedAt().getTime();

        CharSequence relativeTime = DateUtils
                .getRelativeTimeSpanString(oldDate, currentDateLong, 0);
        tvTime.setText(relativeTime);

        tvUsername.setText(post.getUser().getUsername());
        tvCaption.setText(post.getDescription());

        String numLikes = post.getLikes().toString();
        if(numLikes != "1")
            tvLikes.setText(numLikes + " likes");
        else
            tvLikes.setText(numLikes + " like");

        String url = post.getImage().getUrl();

        Glide.with(getApplicationContext())
                .load(url)
//                .apply(RequestOptions.circleCropTransform())
                .into(ivPhoto);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_button:
                        final Intent intent = new Intent(PostDetailsActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                    case R.id.post_button:
                        final Intent i = new Intent(PostDetailsActivity.this, CameraActivity.class);
                        startActivity(i);
                        return true;
                    case R.id.user_button:
                        final Intent intent2 = new Intent(PostDetailsActivity.this, LogoutActivity.class);
                        startActivity(intent2);
                        finish();
                        return true;
                }
                return false;
            }
        });

    }

    public void like(View view) {

        view.setSelected(!view.isSelected());
        Number number = post.getLikes().intValue() + 1;
        Log.d("likes", String.valueOf(number));
        post.setLikes(number);
        Log.d("getlikes", String.valueOf(post.getLikes()));
        post.saveInBackground();


        String numLikes = post.getLikes().toString();
        if(numLikes != "1")
            tvLikes.setText(numLikes + " likes");
        else
            tvLikes.setText(numLikes + " like");
    }

    public void save(View view) {
        view.setSelected(!view.isSelected());
    }
}
