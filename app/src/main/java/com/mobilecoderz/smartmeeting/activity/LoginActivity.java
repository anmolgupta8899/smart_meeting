package com.mobilecoderz.smartmeeting.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mobilecoderz.smartmeeting.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
