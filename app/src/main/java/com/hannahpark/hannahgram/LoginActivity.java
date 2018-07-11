package com.hannahpark.hannahgram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginBtn;
    private Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // show login screen
            final Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.username_et);
        passwordInput = findViewById(R.id.password_et);
        loginBtn = findViewById(R.id.login_btn);
        signUpBtn = findViewById(R.id.signUpBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();

                login(username, password);
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void login (String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(user != null) {
                    Log.d("LoginActivity", "Login successful!");
                    final Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("LoginActivity", "Login failure");
                    e.printStackTrace();
                }
            }
        });
    }
}
