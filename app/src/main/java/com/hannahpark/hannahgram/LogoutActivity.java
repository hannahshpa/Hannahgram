package com.hannahpark.hannahgram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class LogoutActivity extends AppCompatActivity {
    private Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        logoutBtn = findViewById(R.id.logoutButton);

        logoutBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(LogoutActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
