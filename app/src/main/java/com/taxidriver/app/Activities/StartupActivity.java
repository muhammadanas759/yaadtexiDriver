package com.taxidriver.app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.taxidriver.app.Map.Intermediate;
import com.taxidriver.app.Model.User;
import com.taxidriver.app.R;
import com.taxidriver.app.Signin;
import com.taxidriver.app.Utils.LocalPersistence;

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
}
