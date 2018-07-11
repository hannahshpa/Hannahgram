package com.hannahpark.hannahgram;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    public static final String imagePath = "/Users/hannahpark/Downloads/IMG_3731.JPG";
    private EditText descriptionInput;
    private Button refreshButton;
    RecyclerView rvPosts;
    ArrayList<Post> mPosts;
    PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
                        finish();
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
        if (resultCode == 2 && requestCode == 1) {
            loadTopPosts();
        }
    }

    private void loadTopPosts() {
        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser();

        //grab posts
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e == null) {
                    for(int i = 0; i < objects.size(); ++i) {
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
}
