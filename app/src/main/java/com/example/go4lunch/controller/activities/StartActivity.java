package com.example.go4lunch.controller.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.go4lunch.R;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Button mConnectBtn = findViewById(R.id.activity_start_connect_btn);

        Intent myIntent = new Intent(StartActivity.this, LoginActivity.class);

        mConnectBtn.setOnClickListener(view -> {
            this.startActivity(myIntent);
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
