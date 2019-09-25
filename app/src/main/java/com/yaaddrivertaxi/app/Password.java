package com.yaaddrivertaxi.app;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.yaaddrivertaxi.app.Activities.ForgotPass;

import com.yaaddrivertaxi.app.ApiResponse.login.User;
import com.yaaddrivertaxi.app.Connection.Services;
import com.yaaddrivertaxi.app.Connection.Utils;
import com.yaaddrivertaxi.app.Map.Intermediate;
import com.yaaddrivertaxi.app.Utils.LocalPersistence;
import com.yaaddrivertaxi.app.Utils.NetworkUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Password extends AppCompatActivity {
    private TextView mSignupbtn;
    private TextView mForgotPasssword;
    private ImageView mBackbtn;
    private ImageButton mNextbtn;

    private Services mApi;
public static String pass;
    private EditText mPassword;
    private void init() {

        mSignupbtn = findViewById(R.id.signupbtn);
        mForgotPasssword = findViewById(R.id.forget_password);
        mBackbtn = findViewById(R.id.backarrowbtnpass);
        mPassword = findViewById(R.id.password);
        mNextbtn = findViewById(R.id.nextbtn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        init();
        mApi= Utils.getApiService();
        mSignupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View signin) {
                Password.this.startActivity(new Intent(Password.this, SignUp.class));
                Password.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                Password.this.finish();

            }
        });
        mForgotPasssword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Password.this, ForgotPass.class));
            }
        });
        mNextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View next) {
                Password.this.signIn();
            }
        });
    }

    private void signIn() {
        String email =getIntent().getExtras().getString("email");
        pass=mPassword.getText().toString();
        final ProgressDialog dialog=new ProgressDialog(Password.this,R.style.AppCompatAlertDialogStyle);
        dialog.setMessage("Signing In");
        dialog.show();

        if (!(pass.isEmpty() && pass.length()<6)){

            if(NetworkUtil.isConnectedToWifi(Password.this)||NetworkUtil.isConnectedToMobileNetwork(Password.this)){
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(Password.this, "token not found", Toast.LENGTH_SHORT).show();
                                    Log.e("token", "getInstanceId failed", task.getException());
                                    return;
                                }
                                String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                                        Settings.Secure.ANDROID_ID);
                                String token = task.getResult().getToken();
                                callApi(android_id,token,email,pass,dialog);
                            }
                        });

            }else{
                NetworkUtil.showNoInternetAvailableErrorDialog(Password.this);
            }

        }else{
            Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
        }

mBackbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        finish();
    }
});

    }

    private void callApi(String android_id, String token, String email, String password,ProgressDialog dialog) {

        mApi.Login(android_id,"android",token,email,password).enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.e("code",response.code()+"");
                if(response.isSuccessful()) {
                    dialog.dismiss();
                    if (response.body().getAccessToken() != null) {
                        com.yaaddrivertaxi.app.Model.User currentUser = new com.yaaddrivertaxi.app.Model.User(response.body().getEmail(),response.body().getFirstName() ,response.body().getLastName(),password, response.body().getMobile(), response.body().getDevice().getId().toString(), response.body().getDevice().getToken()," ",response.body().getAccessToken());
                        currentUser.setmUserAvatart(response.body().getAvatar());
                        LocalPersistence.witeObjectToFile(getApplicationContext(),currentUser);
                        Intent intent= new Intent(getApplicationContext(), Intermediate.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("responce",response.body());
                        intent.putExtras(bundle);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);



                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        Toast.makeText(Password.this, "Successfully login", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        dialog.dismiss();
                        Toast.makeText(Password. this, "The email address or password you entered is incorrect.", Toast.LENGTH_SHORT).show();

                    }

                }else{
                    if(response.code()==401){
                        dialog.dismiss();
                        Toast.makeText(Password. this, "The email address or password you entered is incorrect.", Toast.LENGTH_SHORT).show();

                    }
                    dialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("failure",t.getLocalizedMessage());
                dialog.dismiss();
                Toast.makeText(Password.this, "Your Request Can not be Completed", Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onBackPressed();
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
