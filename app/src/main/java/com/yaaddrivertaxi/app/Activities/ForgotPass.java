package com.yaaddrivertaxi.app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.yaaddrivertaxi.app.ApiResponse.ForgotPassword.ForgotResponse;
import com.yaaddrivertaxi.app.Connection.Services;
import com.yaaddrivertaxi.app.Connection.Utils;
import com.yaaddrivertaxi.app.Model.User;
import com.yaaddrivertaxi.app.R;
import com.yaaddrivertaxi.app.Utils.LocalPersistence;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPass extends AppCompatActivity {
private EditText forgetpass;
private ImageButton nextbtn;
private ImageView backbtn;
Services mApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        forgetpass=findViewById(R.id.forget_number);
        nextbtn=findViewById(R.id.nextbtn);
        backbtn=findViewById(R.id.backarrowbtnpass);
        mApi= Utils.getApiService();


        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String number=forgetpass.getText().toString();
                if(!number.isEmpty()){
                callApi(number);
                }
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void callApi(String number) {
        String Token=((User) LocalPersistence.readObjectFromFile(getApplicationContext())).getAccessToken();

    mApi.ForgetPassword(Token,number).enqueue(new Callback<ForgotResponse>() {
        @Override
        public void onResponse(Call<ForgotResponse> call, Response<ForgotResponse> response) {
if(response.isSuccessful()){
    ForgotResponse forgotResponse=response.body();
    Intent intent=new Intent(ForgotPass.this, ResetPass.class);
    Bundle b = new Bundle();
    b.putSerializable("user", forgotResponse);
    intent.putExtras(b); //pass bundle to your intent
    startActivity(intent);
finish();
}else {
    Toast.makeText(ForgotPass.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
}
        }

        @Override
        public void onFailure(Call<ForgotResponse> call, Throwable t) {
            Toast.makeText(ForgotPass.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
    }
}
