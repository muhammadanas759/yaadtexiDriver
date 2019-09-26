package com.yaaddrivertaxi.app.Map;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.yaaddrivertaxi.app.R;


public class Intermediate extends AppCompatActivity {

    boolean permission = false;
    private ProgressDialog dialog;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermediate);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading..");
        dialog.show();

        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
//        if (permission){
//
//
//
//        }else{
//            endingDialog();
//        }
    }

    private void endingDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission Denied");
        builder.setMessage("This Application Can Not Work Without Location Permission")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(final DialogInterface dialog, final int id) {

//                            mCurrentLocationLongitudeLatitutde = getcurrentLocation();
                        dialog.cancel();
                        finish();
                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        dialog.dismiss();
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            endingDialog();
        }
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {

            Intent intent= new Intent(getApplicationContext(),DashBoard.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("responce",getIntent().getExtras().getSerializable("responce"));
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
            return;
        }


    }

}