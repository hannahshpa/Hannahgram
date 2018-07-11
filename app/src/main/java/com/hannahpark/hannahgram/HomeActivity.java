package com.hannahpark.hannahgram;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.hannahpark.hannahgram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private SwipeRefreshLayout swipeContainer;
    private EditText descriptionInput;
    private Button refreshButton;
    RecyclerView rvPosts;
    ArrayList<Post> mPosts;
    PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //check who user is
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            // show login screen
            final Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        rvPosts = (RecyclerView) findViewById(R.id.rvPosts);
        mPosts = new ArrayList<>();
        postAdapter = new PostAdapter(mPosts);

        //RecyclerView setup (layout manager, use adapter)
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        //set the adapter
        rvPosts.setAdapter(postAdapter);

//        refreshButton = (Button) findViewById(R.id.refresh_button);
//        descriptionInput = (EditText) findViewById(R.id.etDescription);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

//        refreshButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                loadTopPosts();
//            }
//        });

        loadTopPosts();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.post_button:
                        final Intent intent = new Intent(HomeActivity.this, CameraActivity.class);
                        startActivityForResult(intent, 1);
//                        finish();
                        return true;
                    case R.id.user_button:
                        final Intent intent2 = new Intent(HomeActivity.this, LogoutActivity.class);
                        startActivity(intent2);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        Log.d("HomeActivity", "on activity result!");
        if (resultCode == 2 && requestCode == 1) {
//            String postID = data.getStringExtra(" Post");
//            System.out.println(postID + " postId");
          //  final Post.Query postQuery = new Post.Query();
           // postQuery.inOrder();
//            Post post = null;
                mPosts.clear();
                postAdapter.notifyDataSetChanged();
                loadTopPosts();
//            postQuery.findInBackground(new FindCallback<Post>() {
//                @Override
//                public void done(List<Post> objects, ParseException e) {
//                    if(e == null) {
//                            mPosts.add(objects.get(objects.size()-1));
//                            postAdapter.notifyItemInserted(mPosts.size()-1);
//                            rvPosts.scrollToPosition(0);
////                        mPosts.clear();
////                        mPosts.addAll(objects);
////                        postAdapter.notifyDataSetChanged();
////                        rvPosts.scrollToPosition(0);
//                        }
//                     else {
//                        e.printStackTrace();
//                    }
//                }
//            });
        }
    }

    private void loadTopPosts() {
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
                        postAdapter.notifyItemInserted(mPosts.size()-1);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void fetchTimelineAsync(int page) {
        postAdapter.clear();
        mPosts.clear();
        loadTopPosts();
        postAdapter.addAll(mPosts);
    }

}
