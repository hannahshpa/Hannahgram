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
import android.widget.ProgressBar;

import com.hannahpark.hannahgram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private SwipeRefreshLayout swipeContainer;
    // Store a member variable for the listener
    private EndlessRecyclerViewScrollListener scrollListener;

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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        //RecyclerView setup (layout manager, use adapter)
        rvPosts.setLayoutManager(linearLayoutManager);
        //set the adapter
        rvPosts.setAdapter(postAdapter);

        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextData(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvPosts.addOnScrollListener(scrollListener);

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

    // Append the next page of data into the adapter
    public void loadNextData(int offset) {
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()
        final Post.Query postQuery = new Post.Query();
        postQuery.withUser().inOrder();
        final int pos = offset*20;

        //grab posts
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e == null) {
                    // on some click or some loading we need to wait for...
                    ProgressBar pb = (ProgressBar) findViewById(R.id.pbLoading);
                    pb.setVisibility(ProgressBar.VISIBLE);

                    if(objects.size()-pos - 1 > 0) {
                        for(int i = objects.size() - pos - 1; i >= Math.max(0,objects.size() - pos - 20); i--) {
                            Log.d("HomeActivity", "Post[" + i + "] = "
                                    + objects.get(i).getDescription()
                                    + "\nusername = " + objects.get(i).getUser().getUsername()
                            );  //end of log.d
                            mPosts.add(objects.get(i));
                            postAdapter.notifyItemInserted(mPosts.size()-1);
                        }
                    }
                    // run a background job and once complete
                    pb.setVisibility(ProgressBar.INVISIBLE);
                } else {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        Log.d("HomeActivity", "on activity result!");
        if (resultCode == 2 && requestCode == 1) {
                mPosts.clear();
                postAdapter.notifyDataSetChanged();
                loadTopPosts();
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
                    // on some click or some loading we need to wait for...
                    ProgressBar pb = (ProgressBar) findViewById(R.id.pbLoading);
                    pb.setVisibility(ProgressBar.VISIBLE);

                    for(int i = objects.size()-1; i >= 0; i--) {
                        Log.d("HomeActivity", "Post[" + i + "] = "
                                + objects.get(i).getDescription()
                                + "\nusername = " + objects.get(i).getUser().getUsername()
                        );  //end of log.d
                        mPosts.add(objects.get(i));
                        postAdapter.notifyItemInserted(mPosts.size()-1);
                    }

                    // run a background job and once complete
                    pb.setVisibility(ProgressBar.INVISIBLE);
                } else {
                    e.printStackTrace();
                    System.out.println("Posts not loading bcuz "+ e.getMessage());
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
