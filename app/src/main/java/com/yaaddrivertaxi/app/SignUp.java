package com.yaaddrivertaxi.app;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.yaaddrivertaxi.app.ApiResponse.SignupResponse.SignUpResponse;
import com.yaaddrivertaxi.app.Connection.Services;
import com.yaaddrivertaxi.app.Connection.Utils;
import com.yaaddrivertaxi.app.Map.Intermediate;
import com.yaaddrivertaxi.app.Model.User;
import com.yaaddrivertaxi.app.Utils.LocalPersistence;
import com.yaaddrivertaxi.app.Utils.NetworkUtil;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;

public class SignUp extends AppCompatActivity {
    private static final String TAG = "SignUp";
    public static int APP_REQUEST_CODE = 99;
    private ImageButton nextbtn;
    private String android_id, memail, ml_name, mf_name, mpassword, user_number;
    private EditText email, l_name, f_name, password;
    private ImageView backbtn;
    private User currentUser;
    private Services mApi;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();

        if (ActivityCompat.checkSelfPermission(this, READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, READ_PHONE_NUMBERS) ==
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            String mPhoneNumber = tMgr.getLine1Number();
            Log.e("PhoneNumber ", "onRequestPermissionsResult: " + mPhoneNumber);

            return;
        } else {
            requestPermission();
        }


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
                                             currentUser = new User(memail, mf_name, ml_name, mpassword, " ", android_id, token," ","");
                                            phoneLogin();
                                            //                                            Intent intent = new Intent(SignUp.this, PhoneNumber.class);
//                                            intent.putExtra("currentUser", (Serializable) currentUser);
//                                            startActivity(intent);
//                                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

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

    public void phoneLogin() {
        final Intent intent = new Intent(SignUp.this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.TOKEN
        // ... perform additional configuration ...
        configurationBuilder.setDefaultCountryCode("JM");
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage;
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();
                } else {
                    toastMessage = String.format(
                            "Success:%s...",
                            loginResult.getAuthorizationCode().substring(0, 10));
                }

                // If you have an authorization code, retrieve it from
                // loginResult.getAuthorizationCode()
                // and pass it to your server and exchange it for an access token.
                // Success! Start your next activity...


                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(Account account) {
                        PhoneNumber phoneNumber = account.getPhoneNumber();
                        user_number = phoneNumber.toString();
                        Log.d(TAG, "onSuccess: Phone Number: " + user_number);
                        signup(account.getPhoneNumber().getPhoneNumber());
                    }

                    @Override
                    public void onError(AccountKitError accountKitError) {

                    }
                });

                Intent intent = new Intent(SignUp.this, Signin.class);
                startActivity(intent);
                finish();
            }

            // Surface the result to your user in an appropriate way.
            Toast.makeText(
                    this,
                    toastMessage,
                    Toast.LENGTH_LONG)
                    .show();
        }
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void init() {
        bundle = new Bundle();
        nextbtn = findViewById(R.id.signupnextbtn);
        email = findViewById(R.id.email);
        f_name = findViewById(R.id.f_name);
        l_name = findViewById(R.id.l_name);
        password = findViewById(R.id.password);
//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            currentUser = (User) getIntent().getSerializableExtra("currentUser"); //Obtaining data
//        }
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        backbtn = findViewById(R.id.signupbackbtn);
        mApi = Utils.getApiService();
    }

//    private void signup(String number) {
//        Log.d(TAG, "signup: Phone Number: " + number);
//        currentUser.setmPhoneNumber(number);
////        ProgressDialog dialog = new ProgressDialog(PhoneNumber.this, R.style.AppCompatAlertDialogStyle);
////        dialog.setMessage("Signing up");
////        dialog.show();
//        if (NetworkUtil.isConnectedToWifi(SignUp.this) || NetworkUtil.isConnectedToMobileNetwork(SignUp.this)) {
//            mApi.signup("android", currentUser.getmDeviceToken(), currentUser.getmDeviceId(),
//                    "manual",
//                    currentUser.getmFirstName(),
//                    currentUser.getmLastName(),
//                    currentUser.getmEmail(),
//                    currentUser.getmPhoneNumber(),
//                    currentUser.getmPassword())
//
//                    .enqueue(new Callback<SignUpResponse>() {
//                        @Override
//                        public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
//                            Log.d(TAG, "onResponse: " + response.body());
//                            if (response.isSuccessful()) {
//                                if (response.body().getSuccess()) {
//                                    Constants.curr_password = currentUser.getmPassword();
//                                    Toast.makeText(SignUp.this, "Successfully signup", Toast.LENGTH_SHORT).show();
//                                    Log.e("SignUp", "onResponse: " + response.body().getResponse().getUser().getEmail());
//                                    LoginResponse loginResponse = new LoginResponse();
//                                    com.traveltaxi.yaadtaxi.ApiResponse.Login.Response response1 = new com.traveltaxi.yaadtaxi.ApiResponse.Login.Response();
//                                    com.traveltaxi.yaadtaxi.ApiResponse.Login.User user = new com.traveltaxi.yaadtaxi.ApiResponse.Login.User();
//                                    com.traveltaxi.yaadtaxi.ApiResponse.SignupResponse.User user1 = response.body().getResponse().getUser();
//                                    user.setFirstName(user1.getFirstName());
//                                    user.setDeviceId(user1.getDeviceId());
//                                    user.setDeviceToken(user.getDeviceToken());
//                                    user.setEmail(user1.getEmail());
//                                    user.setLastName(user1.getLastName());
//                                    user.setId(user1.getId());
//                                    user.setMobile(user1.getMobile());
//                                    user.setDeviceType(user1.getDeviceType());
//                                    user.setPicture("https://firebasestorage.googleapis.com/v0/b/favour-ab5af.appspot.com/o/Anas%20bhai%2FHTB1qSxEJpXXXXX2XXXXq6xXFXXXx.jpg?alt=media&token=a98fc4ef-052d-43fc-b7ee-f2ab620a299d");
//                                    response1.setUser(user);
//                                    response1.setAccessToken(response.body().getResponse().getAccessToken());
//                                    response1.setTokenType(response.body().getResponse().getTokenType());
//                                    LocalPersistence.witeObjectToFile(SignUp.this, response1);
//                                    SharedPreferenceHelper.setSharedPreferenceString(SignUp.this, "accesstoken", response.body().getResponse().getAccessToken());
//                                } else {
//                                    Toast.makeText(SignUp.this, "Response if False", Toast.LENGTH_LONG).show();
//                                }
//                            } else {
////    dialog.dismiss();
//                                Toast.makeText(SignUp.this, "Failed", Toast.LENGTH_SHORT).show();
//
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<SignUpResponse> call, Throwable t) {
////            dialog.dismiss();
//                            Toast.makeText(SignUp.this, "Failure", Toast.LENGTH_SHORT).show();
//
//                        }
//
//                    });
//        } else {
////        dialog.dismiss();
//            NetworkUtil.showNoInternetAvailableErrorDialog(SignUp.this);
//        }
//
//
//    }
private void signup(String number){
            Log.e(TAG, "signup: Phone Number: " + number);

    currentUser.setmPhoneNumber(number);
//    ProgressDialog dialog=new ProgressDialog(PhoneNumber.this,R.style.AppCompatAlertDialogStyle);
//    dialog.setMessage("Signing up");
//    dialog.show();
    if(NetworkUtil.isConnectedToWifi(SignUp.this)||NetworkUtil.isConnectedToMobileNetwork(SignUp.this)){

        mApi.signup(
                currentUser.getmDeviceId(),
                "android",
                currentUser.getmDeviceToken(),
                currentUser.getmFirstName(),
                currentUser.getmLastName(),
                currentUser.getmEmail(),
                currentUser.getmPhoneNumber(),
                currentUser.getmPassword(),
                currentUser.getmPassword()).enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                if(response.isSuccessful()){
                    Toast.makeText(SignUp.this, "Successfull", Toast.LENGTH_SHORT).show();
                    Log.e("SignUp", "getAccessToken: "+response.body().getAccessToken());
                    Log.e("SignUp", "getEmail: "+response.body().getEmail());
                    User user=new User(response.body().getEmail(),response.body().getFirstName(),response.body().getLastName(), Password.pass,response.body().getMobile(),response.body().getDevice().getId().toString(),response.body().getDevice().getToken()," ",response.body().getAccessToken());
                    LocalPersistence.witeObjectToFile(SignUp.this,user);
                    Intent intent= new Intent(getApplicationContext(), Intermediate.class);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
//                        SharedPreferenceHelper.setSharedPreferenceString(PhoneNumber.this,"accesstoken",response.body().getResponse().getAccessToken());
                }
                else{
                    Toast.makeText(SignUp.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
//            dialog.dismiss();
                Toast.makeText(SignUp.this, "Failure", Toast.LENGTH_SHORT).show();

            }

        });
    }
    else{
//        dialog.dismiss();
        NetworkUtil.showNoInternetAvailableErrorDialog(SignUp.this);
    }



}

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{READ_SMS, READ_PHONE_NUMBERS, READ_PHONE_STATE}, 100);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100:
                TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                String mPhoneNumber = tMgr.getLine1Number();
                Log.e("PhoneNumber ", "onRequestPermissionsResult: " + mPhoneNumber);
//                phoneNumber.setText(mPhoneNumber);
                break;
        }
    }

}
