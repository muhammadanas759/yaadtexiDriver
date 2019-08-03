package com.taxidriver.app.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



import com.taxidriver.app.ApiResponse.PasswordResponse.PasswordResponse;
import com.taxidriver.app.Connection.Services;
import com.taxidriver.app.Connection.Utils;
import com.taxidriver.app.Model.User;
import com.taxidriver.app.R;
import com.taxidriver.app.Utils.LocalPersistence;

import retrofit2.Call;
import retrofit2.Callback;


public class ChangePassword extends AppCompatActivity {
    User data;
    private EditText newPass, againPass, oldPass;
    private Button updbtn;
    private Services mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        newPass = findViewById(R.id.updnewpassword);
        againPass = findViewById(R.id.updagainpassword);
        oldPass = findViewById(R.id.updoldpassword);
        updbtn = findViewById(R.id.updsavepassword);
        mApi = Utils.getApiService2();
        data = (User) LocalPersistence.readObjectFromFile(ChangePassword.this);

        updbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    mApi.UpdatePassword("Bearer " + data.getAccessToken(),
                            newPass.getText().toString(),
                            againPass.getText().toString(),
                            oldPass.getText().toString()).enqueue(new Callback<PasswordResponse>() {
                        @Override
                        public void onResponse(Call<PasswordResponse> call, retrofit2.Response<PasswordResponse> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(ChangePassword.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                           newPass.setText("");
                           againPass.setText("");
                           oldPass.setText("");
                            }
                        }

                        @Override
                        public void onFailure(Call<PasswordResponse> call, Throwable t) {

                        }
                    });
                }
                else{
                    Toast.makeText(ChangePassword.this, "something wrong", Toast.LENGTH_SHORT).show();
                }
            }


        });


    }

    public boolean isValid() {
        if (!(oldPass.getText().toString().isEmpty() && againPass.getText().toString().isEmpty() && newPass.getText().toString().isEmpty())) {
            if (newPass.getText().toString().equals(againPass.getText().toString())) {
               return true;

            } else {
                Toast.makeText(this, "password not matched", Toast.LENGTH_SHORT).show();

            }

        } else {
            Toast.makeText(this, "Enter all fields", Toast.LENGTH_SHORT).show();
        }

        return false;
    }
}
