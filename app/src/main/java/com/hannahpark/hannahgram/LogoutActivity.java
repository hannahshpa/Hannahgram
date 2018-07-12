package com.hannahpark.hannahgram;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hannahpark.hannahgram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class LogoutActivity extends AppCompatActivity {
    private Button logoutBtn;
    private BottomNavigationView bottomNavigationView;
    private TextView tvUsername;
    RecyclerView rvPics;
    ArrayList<Post> mPosts;
    GridAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        logoutBtn = findViewById(R.id.logoutButton);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.user_button);
        tvUsername = findViewById(R.id.tvUsername);

        tvUsername.setText(ParseUser.getCurrentUser().getUsername());

        logoutBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ParseUser.logOut(); //logout the current user
                final Intent intent = new Intent(LogoutActivity.this, LoginActivity.class); //return to login screen
                startActivity(intent);
                finish();
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_button:
                        final Intent intent = new Intent(LogoutActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                    case R.id.post_button:
                        final Intent i = new Intent(LogoutActivity.this, CameraActivity.class);
                        startActivity(i);
//                        finish();
                }
                return false;
            }
        });


        rvPics = (RecyclerView) findViewById(R.id.rvPics);
        mPosts = new ArrayList<>();
        gridAdapter = new GridAdapter(mPosts);

        //RecyclerView setup (layout manager, use adapter)
        rvPics.setLayoutManager(new GridLayoutManager(this, 3));
        //set the adapter
        rvPics.setAdapter(gridAdapter);

        loadMyPosts();
    }

    private void loadMyPosts() {
        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser().inOrder();
        //grab posts
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e == null) {
                    for(int i = objects.size()-1; i >= 0; i--) {
                        Log.d("HomeActivity", "Post[" + i + "] = "
                                + objects.get(i).getDescription()
                                + "\nusername = " + objects.get(i).getUser().getUsername()
                        );  //end of log.d
                        mPosts.add(objects.get(i));
                        gridAdapter.notifyItemInserted(mPosts.size()-1);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
