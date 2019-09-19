package com.yaaddrivertaxi.app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.yaaddrivertaxi.app.Map.DashBoard;
import com.yaaddrivertaxi.app.R;

public class Coupon extends AppCompatActivity {
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_coupon);

        back=findViewById(R.id.backcopn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Coupon.this, DashBoard.class));

            }
        });
    }
}
