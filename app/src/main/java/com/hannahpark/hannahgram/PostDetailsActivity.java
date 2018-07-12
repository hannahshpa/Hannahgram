package com.hannahpark.hannahgram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hannahpark.hannahgram.model.Post;

import org.parceler.Parcels;

public class PostDetailsActivity extends AppCompatActivity {

    Post post;

    public TextView tvUsername;
    public ImageView ivPhoto;
    public TextView tvCaption;
    public TextView tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);


        tvUsername = (TextView) findViewById(R.id.tvUsername);
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        tvCaption = (TextView) findViewById(R.id.tvCaption);
        tvTime = (TextView) findViewById(R.id.tvTime);

        //unwrap post
        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra("post"));

        tvTime.setText(post.getCreationTime().toString());
        tvUsername.setText(post.getUser().getUsername());
        tvCaption.setText(post.getDescription());

        String url = post.getImage().getUrl();

        Glide.with(getApplicationContext())
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .into(ivPhoto);
    }
}
