package com.hannahpark.hannahgram;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hannahpark.hannahgram.model.Post;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostDetailsActivity extends AppCompatActivity {

    Post post;
    ArrayList<String> mComments;
    CommentAdapter commentAdapter;
    RecyclerView rvComments;

    @BindView(R.id.tvUsername) TextView tvUsername;
    @BindView(R.id.ivPhoto) ImageView ivPhoto;
    @BindView(R.id.tvCaption) TextView tvCaption;
    @BindView(R.id.tvTime) TextView tvTime;
    @BindView(R.id.ivHeart) ImageButton ivHeart;
    @BindView(R.id.tvLikes) TextView tvLikes;
    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;
    @BindView(R.id.ivComment) ImageButton ivComment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        ButterKnife.bind(this);

        bottomNavigationView.setSelectedItemId(R.id.search_button);


        //unwrap post
        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra("post"));
        mComments = post.getComments();

        if(post.favorited == true)
            ivHeart.setSelected(true);

        mComments = new ArrayList<>();
        commentAdapter = new CommentAdapter(mComments);
        rvComments = (RecyclerView) findViewById(R.id.rvComments);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        //RecyclerView setup (layout manager, use adapter)
        rvComments.setLayoutManager(linearLayoutManager);
        //set the adapter
        rvComments.setAdapter(commentAdapter);

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

        String numLikes = "0";
        if(post.getLikes() != null) {
            numLikes = post.getLikes().toString();
        }
        if(numLikes != "1")
            tvLikes.setText(numLikes + " likes");
        else
            tvLikes.setText(numLikes + " like");

        String url = post.getImage().getUrl();

        Glide.with(getApplicationContext())
                .load(url)
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

        ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View messageView = LayoutInflater.from(PostDetailsActivity.this).inflate(R.layout.comment_dialog, null);
                // Create alert dialog builder
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PostDetailsActivity.this);
                // set message_item.xml to AlertDialog builder
                alertDialogBuilder.setView(messageView);

                // Create alert dialog
                final AlertDialog alertDialog = alertDialogBuilder.create();

                // Configure dialog button (OK)
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Send",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Extract content from alert dialog
                                String comment = ((EditText) alertDialog.findViewById(R.id.etComment)).
                                        getText().toString();

                                mComments.add(comment);
                                commentAdapter.notifyDataSetChanged();
                                post.setComments(mComments);
                                post.saveInBackground();
                            }
                        });

                // Configure dialog button (Cancel)
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) { dialog.cancel(); }
                        });
                // Display the dialog
                alertDialog.show();
            }
        });

    }

    public void like(View view) {

        view.setSelected(!view.isSelected());

        Number number;

        if(view.isSelected()) {
            post.favorited = true;
            number = post.getLikes().intValue() + 1;
        }
        else {
            post.favorited = false;
            number = post.getLikes().intValue() - 1;
        }

        post.setLikes(number);

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
