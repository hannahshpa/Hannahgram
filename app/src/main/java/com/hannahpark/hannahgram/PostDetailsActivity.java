package com.hannahpark.hannahgram;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hannahpark.hannahgram.model.Post;

import org.parceler.Parcels;

public class PostDetailsActivity extends AppCompatActivity {

    Post post;

    public TextView tvUsername;
    public ImageView ivPhoto;
    public TextView tvCaption;
    public TextView tvTime;
    private BottomNavigationView bottomNavigationView;

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

        //unwrap post
        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra("post"));
        tvTime.setText(post.getCreatedAt().toString());
        tvUsername.setText(post.getUser().getUsername());
        tvCaption.setText(post.getDescription());

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
}
