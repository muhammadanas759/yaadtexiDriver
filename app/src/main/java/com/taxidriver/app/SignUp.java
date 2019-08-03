package com.taxidriver.app;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.taxidriver.app.Activities.PhoneNumber;
import com.taxidriver.app.Model.User;

import java.io.Serializable;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {
    private ImageButton nextbtn;
    private String android_id, memail, ml_name, mf_name, mpassword;
    private EditText email, l_name, f_name, password;
    private ImageView backbtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);
        init();

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memail=email.getText().toString();
                ml_name=l_name.getText().toString();
                mf_name=f_name.getText().toString();
                mpassword=password.getText().toString();
                if (!(memail.isEmpty() && mpassword.isEmpty() && ml_name.isEmpty() && mf_name.isEmpty())) {
                    if (Patterns.EMAIL_ADDRESS.matcher(memail).matches() && mpassword.length() >= 6) {

                        try {
                            FirebaseInstanceId.getInstance().getInstanceId()
                                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(SignUp.this, "token not found", Toast.LENGTH_SHORT).show();
                                                Log.e("token", "getInstanceId failed", task.getException());
                                                return;
                                            }
                                            String token = task.getResult().getToken();
                                            User currentUser = new User(memail, mf_name, ml_name, mpassword, " ", android_id, token," ","");
                                            Intent intent = new Intent(SignUp.this, PhoneNumber.class);
                                            intent.putExtra("currentUser", (Serializable) currentUser);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                                        }
                                    });
                            String token = FirebaseInstanceId.getInstance().getToken();
                        } catch (Exception e) {
                            Log.e("e", e.getLocalizedMessage().toString());
                        }


                    } else {
                        Toast.makeText(getApplicationContext(), "Incorrect password or Email", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill all the  fields ", Toast.LENGTH_SHORT).show();
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


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }



    private void init() {
        nextbtn = findViewById(R.id.signupnextbtn);
        email = findViewById(R.id.email);
        f_name = findViewById(R.id.f_name);
        l_name = findViewById(R.id.l_name);
        password = findViewById(R.id.password);
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        backbtn=findViewById(R.id.signupbackbtn);


    }


}