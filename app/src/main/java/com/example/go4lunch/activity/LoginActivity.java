package com.example.go4lunch.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.go4lunch.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this); // allow butterKnife on activity
    }

    @OnClick(R.id.textView2) // butterKnife use
    public void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }
}