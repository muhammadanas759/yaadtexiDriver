package com.yaaddrivertaxi.app;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class Signin extends AppCompatActivity {
    private LinearLayout mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        mEmail = findViewById(R.id.linear);
        mEmail.setOnClickListener(v -> {
                    openActivity2(v);
                }

        );


    }

    public void openActivity2(View view) {
        Intent intent = new Intent(this, Email.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}