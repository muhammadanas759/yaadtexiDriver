package com.taxidriver.app.Activities;

import
        androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;


import com.taxidriver.app.ApiResponse.HelpResponse.HelpResponse;

import com.taxidriver.app.Connection.Services;
import com.taxidriver.app.Connection.Utils;
import com.taxidriver.app.Model.User;
import com.taxidriver.app.R;
import com.taxidriver.app.Utils.LocalPersistence;

import retrofit2.Call;
import retrofit2.Callback;

public class help extends AppCompatActivity {


    private Services mApi;

    private ImageView mPhone, mEmail, mExplore;
    private ProgressDialog dialog;

    private String phone, email, explore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maina);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();
        init();

        apiCall();
    }

    private void apiCall() {
        mApi = Utils.getApiService();
        final String mAccessToken = ((User) LocalPersistence.readObjectFromFile(help.this)).getAccessToken();
//
        mApi.Help("Bearer " + mAccessToken).enqueue(new Callback<HelpResponse>() {
            @Override
            public void onResponse(Call<HelpResponse> call, retrofit2.Response<HelpResponse> response) {
                if (response.isSuccessful()) {
                    dialog.dismiss();
                    phone = response.body().getContactNumber();
                    email = response.body().getContactEmail();
                    mEmail.setOnClickListener(v -> {
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setType("*/*");
                        emailIntent.putExtra(Intent.ACTION_SENDTO, email);
                        if (emailIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(emailIntent);
                        }
                    });
                    mPhone.setOnClickListener(v -> {
                        Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                        phoneIntent.setData(Uri.parse("tel:" + phone));
                        if (phoneIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(phoneIntent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<HelpResponse> call, Throwable t) {
                Log.e("error", t.getLocalizedMessage());
                dialog.dismiss();
            }
        });


    }

    private void init() {

        mPhone = findViewById(R.id.phone);
        mEmail = findViewById(R.id.email);

        mExplore = findViewById(R.id.explore);


        mExplore.setOnClickListener(v -> {
            Uri webpage = Uri.parse("http://www.yaadtaxi.com/");
            Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
            if (webIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(webIntent);
            }
        });
    }
}
