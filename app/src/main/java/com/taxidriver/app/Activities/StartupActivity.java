package com.taxidriver.app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import com.taxidriver.app.Map.Intermediate;
import com.taxidriver.app.Model.User;
import com.taxidriver.app.R;
import com.taxidriver.app.Signin;
import com.taxidriver.app.Utils.LocalPersistence;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StartupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               checkSignIn();
            }
        },3000);



    }

    private void checkSignIn() {
        Object data= LocalPersistence.readObjectFromFile(StartupActivity.this);
        if(data==null){
            startActivity(new Intent(StartupActivity.this, Signin.class));
            finish();

        }else{
            Intent intent=new Intent(StartupActivity.this, Intermediate.class);
            intent.putExtra("responce",(User)data);
            startActivity(intent);
            finish();
        }
    }
//    public static void printHashKey(Context pContext) {
//        try {
//            PackageInfo info =pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String hashKey = new String(Base64.encode(md.digest(), 0));
//                Log.e("Hash", "printHashKey() Hash Key: " + hashKey);
//            }
//        } catch (NoSuchAlgorithmException e) {
//            Log.e("Hash", "printHashKey()", e);
//        } catch (Exception e) {
//            Log.e("Hash", "printHashKey()", e);
//        }
//    }
}
