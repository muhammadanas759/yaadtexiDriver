package com.taxidriver.app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.taxidriver.app.ApiResponse.ForgotPassword.ForgotResponse;
import com.taxidriver.app.ApiResponse.login.User;
import com.taxidriver.app.Connection.Services;
import com.taxidriver.app.Connection.Utils;
import com.taxidriver.app.Password;
import com.taxidriver.app.R;

import java.util.regex.Pattern;

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

    mApi.ForgetPassword(number).enqueue(new Callback<ForgotResponse>() {
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
