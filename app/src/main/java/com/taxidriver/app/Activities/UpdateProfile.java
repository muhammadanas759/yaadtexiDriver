package com.taxidriver.app.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.squareup.picasso.Picasso;


import com.taxidriver.app.ApiResponse.UpdateProfile.UpdateProfileResponse;
import com.taxidriver.app.Connection.Services;
import com.taxidriver.app.Connection.Utils;
import com.taxidriver.app.Map.DashBoard;
import com.taxidriver.app.Model.User;
import com.taxidriver.app.R;
import com.taxidriver.app.Utils.LocalPersistence;


import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfile extends AppCompatActivity {

    int REQUEST_CODE = 1, STORAGE_PERMISSION_REQUEST_CODE = 2;
    File file;
    String android_id, token;
    private CircleImageView userImage;
    private EditText f_name, l_name, phone,service;
    private TextView email, changePass;
    private Button save;
    private Services mApi;

    public static Bitmap getBitmapFromUri(Uri uri, Context context) {
        ParcelFileDescriptor parcelFD = null;
        Bitmap bitmap = null;
        try {
            parcelFD = context.getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor imageSource = parcelFD.getFileDescriptor();

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(imageSource, null, o);

            // the new size we want to scale to
            final int REQUIRED_SIZE = 400;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            bitmap = BitmapFactory.decodeFileDescriptor(imageSource, null, o2);
            return bitmap;
            /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
            assert bitmap != null;
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
            uploadImage(byteArray);*/


        } catch (FileNotFoundException e) {
            // handle errors
        } finally {
            if (parcelFD != null)
                try {
                    parcelFD.close();
                } catch (IOException e) {
                    // ignored
                }
        }
        return bitmap;
    }

    public static File getFileFromBitmap(Bitmap bitmap, Context context) {
        File filesDir = context.getApplicationContext().getFilesDir();
        File fileImage = new File(filesDir, "sample" + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(fileImage);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {

        }
        return fileImage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        userImage = findViewById(R.id.updateImage);
        f_name = findViewById(R.id.updatefname);
        l_name = findViewById(R.id.updatesname);
        phone = findViewById(R.id.updatenumber);
        email = findViewById(R.id.updateemail);
        service = findViewById(R.id.service);
        changePass = findViewById(R.id.updatechangepass);
        save = findViewById(R.id.savebtn);
        mApi = Utils.getApiService2();
        String Token = ((User) LocalPersistence.readObjectFromFile(UpdateProfile.this)).getAccessToken();




        mApi.getProfile("Bearer "+Token).enqueue(new Callback<UpdateProfileResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {

                if(response.isSuccessful()){

                UpdateProfileResponse data=response.body();
                f_name.setText(data.getFirstName());
                l_name.setText(data.getLastName());
                phone.setText(data.getMobile());
                email.setText(data.getEmail());
                service.setText(data.getService().getServiceType().getName());
                }
            }

            @Override
            public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {

            }
        });
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.e("token", "getInstanceId failed", task.getException());
                            return;
                        }
                        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                                Settings.Secure.ANDROID_ID);

                        String token = task.getResult().getToken();

//                        Update update = new Update(android_id, token, Token);
//                        update.execute();
                    }
                });
        User data=((User) LocalPersistence.readObjectFromFile(UpdateProfile.this));


        userImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openPictureChooser();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (file != null) {



                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);
                    RequestBody fname= RequestBody.create(MultipartBody.FORM,f_name.getText().toString());
                    RequestBody lname= RequestBody.create(MultipartBody.FORM,l_name.getText().toString());
                    RequestBody emails= RequestBody.create(MultipartBody.FORM,email.getText().toString());
                    RequestBody phones= RequestBody.create(MultipartBody.FORM,phone.getText().toString());



                    mApi.UpdateProfile("Bearer " + data.getAccessToken(),
                            fname,
                            lname,
                            emails,
                            phones,
                            body).enqueue(new Callback<UpdateProfileResponse>() {
                        @Override
                        public void onResponse(Call<UpdateProfileResponse> call, retrofit2.Response<UpdateProfileResponse> response) {
                            if (response.isSuccessful()) {

                                data.setmFirstName(response.body().getFirstName());
                                data.setmLastName(response.body().getLastName());
                                data.setmPhoneNumber(response.body().getMobile());
                                data.setmEmail(response.body().getEmail());
                                data.setmUserAvatart(response.body().getAvatar());

                                LocalPersistence.deletefile(UpdateProfile.this);
                                LocalPersistence.witeObjectToFile(UpdateProfile.this, data);
                                Toast.makeText(UpdateProfile.this, "successfully updated", Toast.LENGTH_SHORT).show();
                                Picasso.get()
                                        .load("http://yaadtaxi.com/userprofilepics/"+response.body().getAvatar())
                                        .placeholder(R.drawable.ic_dummy_user)
                                        .into(DashBoard.profileImage);
//                                DashBoard.nameView.setText(response.body().getFirstName()+" "+response.body().getLastName());
                            } else {

                                Log.e("UPdate", "onResponse: "+response.code() );
                                Log.e("Response", "onResponse: "+response.body() );

                            }

                        }

                        @Override
                        public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                            Log.e("Response", "onFailure: " + t.getMessage() );
                            Toast.makeText(UpdateProfile.this, "Onfailure", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {

                    Toast.makeText(UpdateProfile.this, "please select image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateProfile.this, ChangePassword.class));
            }
        });

    }

    private void addImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE);
    }

    private void askPermissions() {

        int permissionCheckStorage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        // we already asked for permisson & Permission granted, call camera intent
        if (permissionCheckStorage == PackageManager.PERMISSION_GRANTED) {

            //do what you want
            addImage();

        } else {

            // if storage request is denied
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("You need to give permission to access storage in order to work this feature.");
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("GIVE PERMISSION", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                        // Show permission request popup
                        ActivityCompat.requestPermissions(UpdateProfile.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                STORAGE_PERMISSION_REQUEST_CODE);
                    }
                });
                builder.show();

            } //asking permission for first time
            else {
                // Show permission request popup for the first time
                ActivityCompat.requestPermissions(UpdateProfile.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        STORAGE_PERMISSION_REQUEST_CODE);

            }

        }
    }

    public void openPictureChooser() {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfile.this);
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("Camera")) {
                    if (ContextCompat.checkSelfPermission(UpdateProfile.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(UpdateProfile.this, new String[]{Manifest.permission.CAMERA}, 200);
                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 200);
                    }
                } else if (items[which].equals("Gallery")) {
                    if (ContextCompat.checkSelfPermission(UpdateProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(UpdateProfile.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent.createChooser(intent, "Select file"), 100);
                    }

                } else if (items[which].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }
    private Uri uri;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getData() != null) {
                    uri = data.getData();
                    Bitmap bitmap = getBitmapFromUri(uri, UpdateProfile.this);
                    if (bitmap != null) {
                        userImage.setImageBitmap(bitmap);
                        file = getFileFromBitmap(bitmap, UpdateProfile.this);
                    }
                    //file=FileHelper.getFileFromUri(uri , UpdateProfile.this);
                }
            }
        } else if (requestCode == 200) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getExtras();
                final Bitmap bmp = (Bitmap) bundle.get("data");

                userImage.setImageBitmap(bmp);
                File filesDir = getApplicationContext().getFilesDir();
                File fileImage = new File(filesDir, "sample" + ".jpg");

                OutputStream os;
                try {
                    os = new FileOutputStream(fileImage);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.flush();
                    os.close();
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
                }
                file = fileImage;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 200);
            }
        } else if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent.createChooser(intent, "Select file"), 100);
            }
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

        super.onBackPressed();
    }
}

/*

class Update extends AsyncTask<String, String, String> {


    String id, token;
    Services mApi;
    String access;
*/

//    public Update(String id, String token, String access) {
//
//        this.id = id;
//        this.token = token;
//        mApi = Utils.getApiService();
//        this.access = access;
//
//    }
//
//    @Override
//    protected String doInBackground(String... params) {
//
//        mApi.GetUserDetails("Bearer " + access, "android", token, id).enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, retrofit2.Response<User> response) {
//                if (response.isSuccessful())
//                    Log.e("response", response.body().toString());
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Log.e("response", t.getLocalizedMessage());
//            }
//        });
//        return "";
//    }


//}