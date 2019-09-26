package com.yaaddrivertaxi.app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yaaddrivertaxi.app.ApiResponse.ForgotPassword.ForgotResponse;
import com.yaaddrivertaxi.app.ApiResponse.ResetResponse.ResetResponse;
import com.yaaddrivertaxi.app.Connection.Services;
import com.yaaddrivertaxi.app.Connection.Utils;
import com.yaaddrivertaxi.app.Model.User;
import com.yaaddrivertaxi.app.R;
import com.yaaddrivertaxi.app.Signin;
import com.yaaddrivertaxi.app.Utils.LocalPersistence;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPass extends AppCompatActivity {
private EditText pass,newpass;
private Button reset;
Services mApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        pass=findViewById(R.id.resetpassword);
        newpass=findViewById(R.id.resetagainpassword);
        reset=findViewById(R.id.resetpasswordbtn);
        mApi= Utils.getApiService();
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        ForgotResponse user = (ForgotResponse) bundle.getSerializable("user");
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check())
                callApi(user);
            }
        });
    }

    private void callApi(ForgotResponse user) {
        String Token=((User) LocalPersistence.readObjectFromFile(getApplicationContext())).getAccessToken();

        mApi.ResetPassword(
                Token,
                user.getProvider().getMobile(),
                pass.getText().toString(),
                newpass.getText().toString(),
                user.getProvider().getOtp().toString()).enqueue(new Callback<ResetResponse>() {
            @Override
            public void onResponse(Call<ResetResponse> call, Response<ResetResponse> response) {
                if(response.isSuccessful()){
                    Toast.makeText(ResetPass.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("reset", "onResponse: response.message() "+response.message() );
                    Log.e("reset", "onResponse:body().getMessage()  "+response.body().getMessage() );
                    Log.e("reset", "onResponse: response.errorBody().toString()"+response.errorBody().toString() );
                startActivity(new Intent(ResetPass.this, Signin.class));
                finish();
                }
                else{
                      Toast.makeText(ResetPass.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResetResponse> call, Throwable t) {
                Toast.makeText(ResetPass.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    public boolean check(){
        if(!(pass.getText().toString().isEmpty() && newpass.getText().toString().isEmpty())){
            return  true;
        }else{
            Toast.makeText(this, "please fill all the fields", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
