package com.yaaddrivertaxi.app.Map;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.yaaddrivertaxi.app.Activities.Summary;
import com.yaaddrivertaxi.app.Activities.Target;
import com.yaaddrivertaxi.app.Activities.Trips;
import com.yaaddrivertaxi.app.Activities.UpdateProfile;
import com.yaaddrivertaxi.app.Activities.help;
import com.yaaddrivertaxi.app.Adapter.TaxiSlection;
import com.yaaddrivertaxi.app.ApiResponse.AcceptRideRequest.AcceptRideResponse;
import com.yaaddrivertaxi.app.ApiResponse.CancelTrip.CancelTripResponse;
import com.yaaddrivertaxi.app.ApiResponse.Logout.LogoutResponse;
import com.yaaddrivertaxi.app.ApiResponse.Status.StatusResponse;
import com.yaaddrivertaxi.app.Connection.Services;
import com.yaaddrivertaxi.app.Connection.Utils;
import com.yaaddrivertaxi.app.Helper.DrawingHelper;
import com.yaaddrivertaxi.app.Model.DriverModel;
import com.yaaddrivertaxi.app.Model.Place;
import com.yaaddrivertaxi.app.Model.Riderequest;
import com.yaaddrivertaxi.app.Model.Taxi;
import com.yaaddrivertaxi.app.Model.User;
import com.yaaddrivertaxi.app.R;
import com.yaaddrivertaxi.app.Signin;
import com.yaaddrivertaxi.app.Utils.LocalPersistence;
import com.yaaddrivertaxi.app.Utils.NetworkUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.location.LocationManager.GPS_PROVIDER;

public class DashBoard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    private String TAG = "DashBoard";

    private SupportMapFragment mMap;
    private GoogleMap map;
    private Location FromLocation;


    private ImageView mCurrentLocation;
    private ImageView mCenter;

    private TextView mGoOffline;
    private TextView mGoOnline;

    private Location mCurrentLocationLongitudeLatitutde, mToLocation, mFromLocation;

    private boolean state;
    private boolean screenMain;
    private boolean visibility;
    boolean gps;

    private boolean bottomOn = false;

    private LinearLayout mPickLocation;
    private EditText mToLocations, mFromLocations;

    private Location From;
    private Place To;
    private View include;
    private View include1;
    Bundle savedInstanceState;
    private Services mApi;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private View offlineView;
    DrawerLayout drawer;
    NavigationView navigationView;
    public static CircleImageView profileImage;
    public static TextView nameView;
    User user;
    private CountDownTimer timer;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private boolean check;



    private View arrived , endlayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_dash_board);
        mSharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        mEditor = mSharedPreferences.edit();
        statusCheck();
        mApi = Utils.getApiService();
        mMap = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        View v = navigationView.getHeaderView(0);
        profileImage = v.findViewById(R.id.profileimageView);
        nameView = v.findViewById(R.id.name);
        arrived =findViewById(R.id.track);
        endlayout=findViewById(R.id.end);

        user = ((User) LocalPersistence.readObjectFromFile(DashBoard.this));
        nameView.setText(user.getmFirstName() + " " + user.getmLastName());

        Picasso.get()
                .load("http://www.yaadtaxi.com/providerprofilepics/" + user.getmUserAvatart())
                .placeholder(R.drawable.ic_dummy_user)
                .into(profileImage);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashBoard.this, UpdateProfile.class));

            }
        });


        offlineView = findViewById(R.id.offline);
        mGoOffline = findViewById(R.id.go_offline);
        mGoOnline = findViewById(R.id.go_online);


        mGoOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.putBoolean("offline", true);
                mEditor.commit();
                status("offline");

            }
        });


        mGoOnline.setOnClickListener(v1 -> {
            mEditor.putBoolean("offline", false);
            mEditor.commit();
            status("active");
        });



        if (mSharedPreferences.getBoolean("offline", true)) {

            status("offline");

        } else {
            status("active");


            timer = new CountDownTimer(9 * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                    changeMarker();
                }

                @Override
                public void onFinish() {
                    UpdateLocation();
                }
            };
            Riderequest map = (Riderequest) LocalPersistence.readObjectFromFile(this, "map");

            //to get the state of the screen wheter a pin selection screen or main screen
            if (getIntent().getExtras() != null) {
                if (map != null) {
                    drawAcceptAndReject();
                } else {
                    if (getIntent().getExtras().getBoolean("pick")) {
                        screenMain = false;
                        mMap.onCreate(savedInstanceState);
                        visibility(screenMain);
                        setMainForPickLocation();
                        Log.e("error", "does not occurr");
                        From = getIntent().getExtras().getParcelable("location");
                        To = (Place) getIntent().getExtras().getSerializable("place");
                    } else {
                        Log.e("error", "occurr");

                        screenMain = true;
                        mMap.onCreate(savedInstanceState);
                        visibility(screenMain);
                        setMainMapWithDrawer();
                        getcurrentLocation();
                        if (mCurrentLocationLongitudeLatitutde != null)
                            timer.start();
                    }
                }
            } else {
                screenMain = true;
                mMap.onCreate(savedInstanceState);
                visibility(screenMain);
                setMainMapWithDrawer();
                getcurrentLocation();
                if (mCurrentLocationLongitudeLatitutde != null)
                    timer.start();

            }
        }


    }

    private View mAcceptAndReject;

    private void drawAcceptAndReject() {
        Riderequest map= (Riderequest) LocalPersistence.readObjectFromFile(this,"map");
        if (!map.isStatus()) {

            mAcceptAndReject = findViewById(R.id.accepts);


            findViewById(R.id.main_relative_layout).setVisibility(View.GONE);
            mAcceptAndReject.setVisibility(View.VISIBLE);

            TranslateAnimation animate = new TranslateAnimation(
                    0,                 // fromXDelta
                    0,                 // toXDelta
                    mAcceptAndReject.getHeight() + 250,  // fromYDelta
                    0);                // toYDelta
            animate.setDuration(500);
            animate.setFillAfter(true);
            mAcceptAndReject.startAnimation(animate);


            TextView name = mAcceptAndReject.findViewById(R.id.name);
            TextView to = mAcceptAndReject.findViewById(R.id.pickuplocation);

            TextView from = mAcceptAndReject.findViewById(R.id.dropoflocation);


            name.setText(map.getName());
            to.setText(map.getLocationName());
            from.setText(map.getDestinationName());


            TextView mAccept = mAcceptAndReject.findViewById(R.id.accept);
            TextView mCancel = mAcceptAndReject.findViewById(R.id.decline);


            String id = "";
            mAccept.setOnClickListener(v -> {
                acceptRequest(map.getRequestId());

            });


            mCancel.setOnClickListener(v -> {
                CancelRequest(map.getRequestId());

            });

        }else{


            mGoOffline.setVisibility(View.GONE);

           changeTheMap();



        }


    }

    private void changeTheMap() {

        Riderequest map= (Riderequest) LocalPersistence.readObjectFromFile(this,"map");
        if (map.isArrived()){
            arrived.setVisibility(View.GONE);
            endlayout.setVisibility(View.VISIBLE);
            TextView end=endlayout.findViewById(R.id.endride);
            end.setOnClickListener(v->{
                    updateFirebase(2);
                    CancelRequest(map.getRequestId());
                    LocalPersistence.deletefile(getApplicationContext(),"map");
                    endlayout.setVisibility(View.GONE);
                    this.map.clear();
                    getcurrentLocation();
                mGoOffline.setVisibility(View.VISIBLE);
            });
        }else{
            arrived.setVisibility(View.VISIBLE);
            TextView arrive=arrived.findViewById(R.id.arrived);
            arrive.setOnClickListener(v->{
                updateFirebase(1);
                DrawingHelper helper = new DrawingHelper(this.map, getApplicationContext());
                String url = helper.getDirectionsUrl(new LatLng(Double.valueOf(map.getUserlocationlat()),
                                Double.valueOf(map.getUserlocationlong())
                                ),
                        new LatLng(Double.valueOf(map.getDestlat()), Double.valueOf(map.getDestlng())));
                helper.run(url);
                drawAcceptAndReject();


            });
        }
    }

    private void updateFirebase(int i) {
        FirebaseDatabase mDatabase= FirebaseDatabase.getInstance();
        DatabaseReference mRef = mDatabase.getReference();

        Riderequest map= (Riderequest) LocalPersistence.readObjectFromFile(this,"map");

        HashMap<String, Object> result = new HashMap<>();
        result.put("status", ""+i);


        mRef.child("Users").child(map.getRequestId()).updateChildren(result);

        map.setArrived(true);
        LocalPersistence.witeObjectToFile(getApplicationContext(),map,"map");
      }

    private void CancelRequest(String id) {
        String Token = ((User) LocalPersistence.readObjectFromFile(DashBoard.this)).getAccessToken();


        mApi.CancelTrip("Bearer " + Token,
                id).enqueue(new Callback<CancelTripResponse>() {
            @Override
            public void onResponse(Call<CancelTripResponse> call, Response<CancelTripResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_bottom);


                        mAcceptAndReject.setVisibility(View.GONE);
                        mAcceptAndReject.startAnimation(slideDown);
                        findViewById(R.id.main_relative_layout).setVisibility(View.VISIBLE);
                        LocalPersistence.deletefile(getApplicationContext(),"map");


                        map.clear();
                        getcurrentLocation();

                    }
                }
            }

            @Override
            public void onFailure(Call<CancelTripResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: ",t );
            }
        });


    }

    AcceptRideResponse ride;

    private void acceptRequest(String id) {

        String Token = ((User) LocalPersistence.readObjectFromFile(DashBoard.this)).getAccessToken();

        Riderequest maps= (Riderequest) LocalPersistence.readObjectFromFile(this,"map");

        FirebaseDatabase mDatabase= FirebaseDatabase.getInstance();
        DatabaseReference mRef = mDatabase.getReference();

        mApi.AcceptRideRequest("Bearer " + Token,
                id).enqueue(new Callback<AcceptRideResponse>() {
            @Override
            public void onResponse(Call<AcceptRideResponse> call, Response<AcceptRideResponse>response) {

                if (response.isSuccessful()) {

                     AcceptRideResponse ride=  response.body();


                            Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_bottom);


                            mAcceptAndReject.setVisibility(View.GONE);
                            mAcceptAndReject.startAnimation(slideDown);
                            DrawingHelper helper = new DrawingHelper(map, getApplicationContext());
                    String url = helper.getDirectionsUrl(new LatLng(mCurrentLocationLongitudeLatitutde.getLatitude(),
                                            mCurrentLocationLongitudeLatitutde.getLongitude()),
                                    new LatLng(ride.getSLatitude(), ride.getSLongitude()));
                            helper.run(url);


                            DriverModel model =new DriverModel(maps.getRequestId(),((AcceptRideResponse)response.body()).getUser().getDeviceToken(),((AcceptRideResponse)response.body()).getUser().getFirstName(),"0");

                            mRef.child("Users").child(maps.getRequestId()).setValue(model);
                            maps.setStatus(true);
                            LocalPersistence.witeObjectToFile(getApplicationContext(),maps,"map");
                    drawAcceptAndReject();

                    }
                }


            @Override
            public void onFailure(Call<AcceptRideResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });

        map.setOnMapClickListener(null);
    }

    private void UpdateLocation() {
        Log.e("where", "in  UpdateLocation");
        changeMarker();
        callUpdateLocation();
        timer.start();
    }

    private void callUpdateLocation() {
        Log.e("where", "in  callUpdateLocation");

        String Token = ((User) LocalPersistence.readObjectFromFile(DashBoard.this)).getAccessToken();

        mApi.UpdateLocation("Bearer " + Token, mCurrentLocationLongitudeLatitutde.getLatitude(), mCurrentLocationLongitudeLatitutde.getLongitude())
                .enqueue(new Callback<LogoutResponse>() {
                    @Override
                    public void onResponse(Call<LogoutResponse> call, retrofit2.Response<LogoutResponse> response) {
                        if (response.isSuccessful()) {
                            Log.e("response", response.body().getMessage());
                        }

                    }

                    @Override
                    public void onFailure(Call<LogoutResponse> call, Throwable t) {
                        Log.e(TAG, "onFailure: ", t);
                    }
                });
    }


    private void changeMarker() {
        Log.e("where", "in  changeMarker");


//
//        LocationManager locationManager = (LocationManager)
//                getSystemService(Context.LOCATION_SERVICE);
////
////        Criteria criteria = new Criteria();
////        map.clear();
////        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////            // TODO: Consider calling
////            //    Activity#requestPermissions
////            // here to request the missing permissions, and then overriding
////            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
////            //                                          int[] grantResults)
////            // to handle the case where the user grants the permission. See the documentation
////            // for Activity#requestPermissions for more details.
////            return null;
////        }
////        Log.d("GPS Enabled", "GPS Enabled");
////        if (locationManager != null)
////        {
////           Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
////            if (location != null)
////            {
////               double latitude = location.getLatitude();
////                double longitude = location.getLongitude();
////                LatLng loc = new LatLng(latitude, longitude);
////                CameraPosition.Builder builder = new CameraPosition.Builder();
////                builder.zoom(16);
////                builder.target(loc);
////                map.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
////                // Creating a marker
////                MarkerOptions markerOptions = new MarkerOptions();
////
////                // Setting the position for the marker
////                markerOptions.position(loc);
////                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.current_location));
////                map.addMarker(markerOptions);
////                return location;
////            }
////        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location currentlocation) {
                        // Got last known location. In some rare situations this can be null.
                        if (currentlocation != null) {
                            double latitude = currentlocation.getLatitude();
                            double longitude = currentlocation.getLongitude();
                            LatLng loc = new LatLng(latitude, longitude);

                            mCurrentLocationLongitudeLatitutde = currentlocation;
//         Creating a marker
                            if (mCurrentLocationLongitudeLatitutde.getLongitude() == currentlocation.getLongitude() &&
                                    mCurrentLocationLongitudeLatitutde.getLatitude() == currentlocation.getLatitude()) {
                                Log.e("location", "is  same");

                            } else {
                                if (map != null)
                                    map.clear();
                                mCurrentLocationLongitudeLatitutde = currentlocation;
                                MarkerOptions markerOptions = new MarkerOptions();

                                // Setting the position for the marker
                                markerOptions.position(loc);
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.current_location));

                                map.addMarker(markerOptions);
                            }
                        }
                    }


                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("error", e.getLocalizedMessage());
            }
        });

    }

    private void status(String offline) {

        String Token = ((User) LocalPersistence.readObjectFromFile(DashBoard.this)).getAccessToken();

        Log.e(TAG, "onResponse: ");
        mApi.StatusUpdate("Bearer " + Token, offline)
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        if (response.isSuccessful()) {
                            Log.e(TAG, "onResponse: " + response.body().getId() + "\n" + response.code());
                            Log.e(TAG, "onResponse: " + offline);

                            if (offline.equals("offline")) {

                                offlineView.setVisibility(View.VISIBLE);
                                mGoOffline.setVisibility(View.GONE);
                                if (timer!=null)
                                timer.cancel();
                            } else {
                                offlineView.setVisibility(View.GONE);
                                mGoOffline.setVisibility(View.VISIBLE);
                                if (timer!=null)
                                timer.start();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        Log.e(TAG, "onFailure: ", t);
                        Log.e(TAG + "1", "onFailure: " + t.getMessage());
                    }
                });


    }


    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
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

    private FusedLocationProviderClient fusedLocationClient;

    private void buildAlertMessageNoGps() {
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        // getting GPS status
        boolean isGPSEnabled = locationManager.isProviderEnabled(GPS_PROVIDER);
        Log.e("", "" + isGPSEnabled);
        // getting network status
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled) {
            gps = true;
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        public void onClick(final DialogInterface dialog, final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

//                            mCurrentLocationLongitudeLatitutde = getcurrentLocation();
                            dialog.cancel();
                            onResume();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                            Toast.makeText(DashBoard.this, "Application Can not operate without location service", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }
    }


    public void setMainForPickLocation() {
        mCenter = findViewById(R.id.center);

        mMap.getMapAsync(this);
        mCurrentLocation = findViewById(R.id.current_location);

        mCurrentLocation.setOnClickListener(v -> {

//            mCurrentLocationLongitudeLatitutde = getcurrentLocation();
            getcurrentLocation();
            mCurrentLocation.setVisibility(View.GONE);

        });

        TextView mDone = findViewById(R.id.done);
        mDone.setOnClickListener(v ->
        {


            getAddress();

            visibility();
            map.setOnMapClickListener(latLng -> {
                onmapClicked();
                map.setOnMapClickListener(null);
            });


        });
    }

    /**
     * this will change the appearence of dashboard to picklocation
     *
     * @param screen
     */
    public void visibility(boolean screen) {

        changeAppearence(screen);

    }

    public void changeAppearence(boolean screen) {
        if (!screen) {
            findViewById(R.id.main_relative_layout).setVisibility(View.GONE);
            findViewById(R.id.seconrelative).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.main_relative_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.seconrelative).setVisibility(View.GONE);
        }
    }

    /**
     * to change the visibilty of the code while a user presses the done on the pick location screen done button
     */

    private void visibility() {
        mPickLocation.setVisibility(View.VISIBLE);
//            overridePendingTransition(R.anim.slide_out_top, R.anim.slide_out_bottom);
        //animation that will be making the thing look like floating
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_in_top);
        inflatebottomView();


//        include.animate().translationY(include.getHeight()-360);
        TextView mDonebottom = include.findViewById(R.id.done);
        mDonebottom.setOnClickListener(view -> {
            bottomOn = true;
            Animation slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom);
            include.setVisibility(View.GONE);
            include.startAnimation(slideDown);
            inflateEsimateedView();

            TextView mRideButton = include1.findViewById(R.id.ride);
            mRideButton.setOnClickListener(ride -> {


                View alertLayout = LayoutInflater.from(DashBoard.this).inflate(R.layout.no_of_passenger_withholdings_dialog, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(DashBoard.this);
                builder.setView(alertLayout);
                TextView done = alertLayout.findViewById(R.id.submit);

                AlertDialog dialog = builder.create();
                dialog.show();
                done.setOnClickListener(submit -> {
                    dialog.dismiss();
                    inflateDriveriew();

                });

            });
//
//            mPickLocation.animate()
//                    .translationY(-mPickLocation.getHeight())
//                    .alpha(0.0f)
//                    .setDuration(500)
//                    .setListener(new AnimatorListenerAdapter() {
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            super.onAnimationEnd(animation);
//                            mPickLocation.setVisibility(View.GONE);
//                        }
//                    });

            DrawingHelper helper = new DrawingHelper(map, this);
            String url = helper.getDirectionsUrl(new LatLng(From.getLatitude(), From.getLongitude()),
                    new LatLng(To.getLatitude(), To.getLongitude()));
            helper.run(url);
            map.setOnMapClickListener(latLng -> {
                onmapClicked();
                map.setOnMapClickListener(null);
            });

        });
    }


    /**
     * this method will generate the necessary bottom recyclerview
     */
    private void inflatebottomView() {
        include = findViewById(R.id.downselection);

        bottomOn = false;
        include.setVisibility(View.VISIBLE);
//        include.startAnimation(slideUp);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                include.getHeight() + 250,  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        include.startAnimation(animate);
        RecyclerView recyclerView = findViewById(R.id.bottom_car_layout);

        ArrayList<Taxi> seats = new ArrayList<>();
        ArrayList<Integer> cars = new ArrayList<>();
        cars.add(R.drawable.car);
        cars.add(R.drawable.car);
        cars.add(R.drawable.car);
        cars.add(R.drawable.car);
        cars.add(R.drawable.car);
        cars.add(R.drawable.car);

        seats.add(new Taxi("Seating 1-3"));
        seats.add(new Taxi("Seating 4-5"));
        seats.add(new Taxi("Seating 6"));
        seats.add(new Taxi("Seating 7-8"));
        seats.add(new Taxi("Seating 9-10"));
        seats.add(new Taxi("Seating 11-15"));
        TaxiSlection adapter = new TaxiSlection(seats, cars, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

    }


    private void inflateEsimateedView() {
        include1 = findViewById(R.id.second);

        include1.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                include1.getHeight() + 250,  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        include1.startAnimation(animate);
    }

    public void inflateDriveriew() {
        include1 = findViewById(R.id.third);
        mPickLocation.animate()
                .translationY(-mPickLocation.getHeight())
                .alpha(0.0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mPickLocation.setVisibility(View.GONE);
                    }
                });
        include1.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                include1.getHeight() + 250,  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        include1.startAnimation(animate);
    }


    public void onmapClicked() {
        bottomOn = false;
        include.setVisibility(View.GONE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                -include.getHeight() + 250,  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        Animation slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom);

        include.startAnimation(slideDown);
        if (include1 != null) {
            include1.setVisibility(View.GONE);
            include1.startAnimation(slideDown);
        }
        mPickLocation.animate()
                .translationY(-mPickLocation.getHeight())
                .alpha(0.0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mPickLocation.setVisibility(View.GONE);
                    }
                });
        visibility(true);
        setMainMapWithDrawer();

    }


    private void getAddress(Place place) {
        Geocoder geocoder;
        List<android.location.Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(mCurrentLocationLongitudeLatitutde.getLatitude(),
                    mCurrentLocationLongitudeLatitutde.getLongitude(),
                    1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        } catch (IOException e) {
            e.printStackTrace();
        }
        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();
        findViewById(R.id.card).setVisibility(View.GONE);
        mCenter.setVisibility(View.GONE);

        com.yaaddrivertaxi.app.Model.Address addresss = new com.yaaddrivertaxi.app.Model.Address(address, city, state, country, postalCode, knownName);
        mToLocations = findViewById(R.id.to);
        mFromLocations = findViewById(R.id.from);
        mPickLocation = findViewById(R.id.to_from);

        mFromLocations.setText(getIntent().getExtras().getSerializable("address").toString());
        FromLocation = getIntent().getExtras().getParcelable("location");
        findViewById(R.id.back).setOnClickListener(view -> onBackPressed());
        mToLocations.setText(addresss.toString());
    }


    private void getAddress() {
        Geocoder geocoder;
        List<android.location.Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(mCurrentLocationLongitudeLatitutde.getLatitude(), mCurrentLocationLongitudeLatitutde.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();
        findViewById(R.id.card).setVisibility(View.GONE);
        mCenter.setVisibility(View.GONE);

        To = new Place();
        To.setLongitude(mCurrentLocationLongitudeLatitutde.getLongitude());
        To.setLatitude(mCurrentLocationLongitudeLatitutde.getLatitude());

        com.yaaddrivertaxi.app.Model.Address addresss = new com.yaaddrivertaxi.app.Model.Address(address, city, state, country, postalCode, knownName);
        mToLocations = findViewById(R.id.to);
        mFromLocations = findViewById(R.id.from);
        mPickLocation = findViewById(R.id.to_from);

        mFromLocations.setText(getIntent().getExtras().getSerializable("address").toString());
        FromLocation = getIntent().getExtras().getParcelable("location");
        findViewById(R.id.back).setOnClickListener(view -> onBackPressed());
        mToLocations.setText(addresss.toString());
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
                endingDialog();
                return;
            }
        }
        googleMap.clear();
        map = googleMap;
        //to change the map appearence according to the desired screen
        if (screenMain) {
            getcurrentLocation();
            onMapReadyMain(
                    googleMap
            );
        } else {
            onMapReadyPickLocation(
                    googleMap
            );
        }

    }

    /**
     * this will initiate the method required for the map on the main screen
     */

    public void setMainMapWithDrawer() {


        mMap.getMapAsync(this);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        mCurrentLocation = findViewById(R.id.current_location);

        getcurrentLocation();
        mCurrentLocation.setOnClickListener(v -> {


//            mCurrentLocationLongitudeLatitutde = getcurrentLocation();
            getcurrentLocation();
            mCurrentLocation.setVisibility(View.GONE);
        });


        findViewById(R.id.drawer).setOnClickListener(v ->
        {

            drawer.openDrawer(Gravity.LEFT);


        });

        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * this will set the map accordingly to the PickLocation Screen requirements
     *
     * @param googleMap
     */
    public void onMapReadyPickLocation(GoogleMap googleMap) {
        map = googleMap;
        try {

            boolean success = map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyles));

        } catch (Resources.NotFoundException e) {
            Log.e("error", e.getLocalizedMessage());
        }
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            Place place = (Place) bundle.getSerializable("place");
            if (place != null) {

                double latitude = place.getLatitude();
                double longitude = place.getLongitude();
                LatLng loc = new LatLng(latitude, longitude);
                CameraPosition.Builder builder = new CameraPosition.Builder();
                builder.zoom(16);
                builder.target(loc);
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
                mCurrentLocationLongitudeLatitutde = new Location(GPS_PROVIDER);
                mCurrentLocationLongitudeLatitutde.setLongitude(place.getLongitude());
                mCurrentLocationLongitudeLatitutde.setLatitude(place.getLatitude());
                placeMarker();
            } else
//                mCurrentLocationLongitudeLatitutde = getcurrentLocation();
                getcurrentLocation();
        } else {


        }

        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();

        map.getUiSettings().setMyLocationButtonEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                return;
            }
        }

        map.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                if (i == REASON_GESTURE) {

                    mCurrentLocation.setVisibility(View.VISIBLE);
                }
            }
        });
        map.setMyLocationEnabled(false);

        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
//                mCenter.setVisibility(View.GONE);
//                MarkerOptions markerOptions = new MarkerOptions().position(map.getCameraPosition().target)
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
//                map.addMarker(markerOptions);
                if (getIntent().getExtras() != null) {
                    Bundle bundle = getIntent().getExtras();
                    Place place = (Place) bundle.getSerializable("place");
                    if (place == null) {
                        Location centerLocation = new Location("");
                        centerLocation.setLatitude(map.getCameraPosition().target.latitude);
                        centerLocation.setLongitude(map.getCameraPosition().target.longitude);
                        mCurrentLocationLongitudeLatitutde = centerLocation;
                    }
                }
            }
        });


    }

    /**
     * this will set the map accordingly to the Main Screen requirements
     *
     * @param googleMap
     */
    public void onMapReadyMain(GoogleMap googleMap) {

        map = googleMap;
        map.clear();
        getcurrentLocation();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                return;
            }
        }

        map.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                if (i == REASON_GESTURE) {

                    mCurrentLocation.setVisibility(View.VISIBLE);
                }
            }
        });
        map.setMyLocationEnabled(false);


        try {
            boolean success = map.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyles));

//            mCurrentLocationLongitudeLatitutde = getcurrentLocation();
            getcurrentLocation();
            state = !state;

            if (!success) {
                Toast.makeText(this, "no styles", Toast.LENGTH_SHORT).show();
            }
        } catch (Resources.NotFoundException e) {
            Log.e("error", e.getLocalizedMessage());
        }
    }

    private void placeMarker() {

        fromLocation();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 200) {
                Bundle bundle = data.getExtras();
                mFromLocation = (Location) bundle.getSerializable("from");
                state = false;
                mToLocation = (Location) bundle.getSerializable("to");
                if (mToLocation != null) {
                    setToCamera(mToLocation);
                } else {
                    Toast.makeText(this, "No Destination Selected", Toast.LENGTH_SHORT).show();
//                    mCurrentLocationLongitudeLatitutde = getcurrentLocation();
                    getcurrentLocation();
                }

            }
        }
    }

    private Location getcurrentLocation() {
//        buildAlertMessageNoGps();

        if (map != null)
            map.clear();
        check = true;
//
//        LocationManager locationManager = (LocationManager)
//                getSystemService(Context.LOCATION_SERVICE);
////
////        Criteria criteria = new Criteria();
////        map.clear();
////        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////            // TODO: Consider calling
////            //    Activity#requestPermissions
////            // here to request the missing permissions, and then overriding
////            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
////            //                                          int[] grantResults)
////            // to handle the case where the user grants the permission. See the documentation
////            // for Activity#requestPermissions for more details.
////            return null;
////        }
////        Log.d("GPS Enabled", "GPS Enabled");
////        if (locationManager != null)
////        {
////           Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
////            if (location != null)
////            {
////               double latitude = location.getLatitude();
////                double longitude = location.getLongitude();
////                LatLng loc = new LatLng(latitude, longitude);
////                CameraPosition.Builder builder = new CameraPosition.Builder();
////                builder.zoom(16);
////                builder.target(loc);
////                map.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
////                // Creating a marker
////                MarkerOptions markerOptions = new MarkerOptions();
////
////                // Setting the position for the marker
////                markerOptions.position(loc);
////                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.current_location));
////                map.addMarker(markerOptions);
////                return location;
////            }
////        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return null;
            }
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location currentlocation) {
                        // Got last known location. In some rare situations this can be null.
                        if (currentlocation != null) {
                            double latitude = currentlocation.getLatitude();
                            double longitude = currentlocation.getLongitude();
                            LatLng loc = new LatLng(latitude, longitude);
                            CameraPosition.Builder builder = new CameraPosition.Builder();
                            builder.zoom(16);
                            builder.target(loc);
                            mCurrentLocationLongitudeLatitutde = currentlocation;
                            map.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
//         Creating a marker
                            if (timer!=null)
                                timer.start();
                            MarkerOptions markerOptions = new MarkerOptions();

                            // Setting the position for the marker
                            markerOptions.position(loc);
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.current_location));
                            map.addMarker(markerOptions);
                        } else {
                            locationRequest = LocationRequest.create();
                            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                            locationRequest.setInterval(20 * 1000);
                            locationCallback = new LocationCallback() {
                                @Override
                                public void onLocationResult(LocationResult locationResult) {
                                    if (locationResult == null) {
                                        return;
                                    }
                                    for (Location currentlocation : locationResult.getLocations()) {
                                        if (currentlocation != null) {
                                            if (check) {
                                                check = false;
                                                double latitude = currentlocation.getLatitude();
                                                double longitude = currentlocation.getLongitude();
                                                LatLng loc = new LatLng(latitude, longitude);
                                                CameraPosition.Builder builder = new CameraPosition.Builder();
                                                builder.zoom(16);
                                                builder.target(loc);
                                                mCurrentLocationLongitudeLatitutde = currentlocation;
                                                map.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
//         Creating a marker
                                                if (timer!=null)
                                                    timer.start();
                                                MarkerOptions markerOptions = new MarkerOptions();

                                                // Setting the position for the marker
                                                markerOptions.position(loc);
                                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.current_location));
                                                map.addMarker(markerOptions);
                                                break;
                                            }
                                        }
                                    }
                                }

                                ;
                            };
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    Activity#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for Activity#requestPermissions for more details.
                                    return;
                                }
                            }
                            fusedLocationClient.requestLocationUpdates(locationRequest,
                                    locationCallback,
                                    null);
                        }
                    }


                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("error", e.getLocalizedMessage());
            }
        });
        return null;
    }

    private void fromLocation() {
        map.clear();
//
//        LocationManager locationManager = (LocationManager)
//                getSystemService(Context.LOCATION_SERVICE);
////
////        Criteria criteria = new Criteria();
////        map.clear();
////        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////            // TODO: Consider calling
////            //    Activity#requestPermissions
////            // here to request the missing permissions, and then overriding
////            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
////            //                                          int[] grantResults)
////            // to handle the case where the user grants the permission. See the documentation
////            // for Activity#requestPermissions for more details.
////            return null;
////        }
////        Log.d("GPS Enabled", "GPS Enabled");
////        if (locationManager != null)
////        {
////           Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
////            if (location != null)
////            {
////               double latitude = location.getLatitude();
////                double longitude = location.getLongitude();
////                LatLng loc = new LatLng(latitude, longitude);
////                CameraPosition.Builder builder = new CameraPosition.Builder();
////                builder.zoom(16);
////                builder.target(loc);
////                map.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
////                // Creating a marker
////                MarkerOptions markerOptions = new MarkerOptions();
////
////                // Setting the position for the marker
////                markerOptions.position(loc);
////                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.current_location));
////                map.addMarker(markerOptions);
////                return location;
////            }
////        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location currentlocation) {
                        // Got last known location. In some rare situations this can be null.
                        if (currentlocation != null) {
                            double latitude = currentlocation.getLatitude();
                            double longitude = currentlocation.getLongitude();
                            LatLng loc = new LatLng(latitude, longitude);

//         Creating a marker
                            MarkerOptions markerOptions = new MarkerOptions();

                            // Setting the position for the marker
                            markerOptions.position(loc);
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.current_location));
                            map.addMarker(markerOptions);
                        }
                    }


                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("error", e.getLocalizedMessage());
            }
        });


    }

    private void setToCamera(@org.jetbrains.annotations.NotNull Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng loc = new LatLng(latitude, longitude);
        CameraPosition.Builder builder = new CameraPosition.Builder();
        builder.zoom(16);
        builder.target(loc);
        map.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));


    }


    @Override
    protected void onResume() {
        Log.e("mess", "resume");
        user = ((User) LocalPersistence.readObjectFromFile(DashBoard.this));
        nameView.setText(user.getmFirstName() + " " + user.getmLastName());
        super.onResume();
        if (mMap != null) {
            if (getIntent().getExtras() != null) {
                if (getIntent().getExtras().getBoolean("pick")) {
                    screenMain = false;
                    mMap.onCreate(savedInstanceState);
                    visibility(screenMain);
                    setMainForPickLocation();
                    Log.e("error", "does not occurr");
                    From = getIntent().getExtras().getParcelable("location");
                    To = (Place) getIntent().getExtras().getSerializable("place");
                    Log.e("mess", "resume3");

                } else {
                    Log.e("error", "occurr");

                    screenMain = true;
                    mMap.onCreate(savedInstanceState);
                    visibility(screenMain);
                    setMainMapWithDrawer();
                    Log.e("mess", "resume2");
                    getcurrentLocation();

                    if (mCurrentLocationLongitudeLatitutde != null)
                        timer.start();

                }
            } else {
                screenMain = true;
                mMap.onCreate(savedInstanceState);
                visibility(screenMain);
                setMainMapWithDrawer();
                Log.e("mess", "resume3");
                getcurrentLocation();

                if (mCurrentLocationLongitudeLatitutde != null)
                    timer.start();
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.e("mess", "start");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMap != null)
            mMap.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMap.onLowMemory();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (visibility == false) {
                if (bottomOn) {
                    onmapClicked();

                } else {
                    visibility = true;

                    visibility(visibility);
                    screenMain = true;
                    setMainMapWithDrawer();
                }
            } else {

                super.onBackPressed();


            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.mytrips) {
            startActivity(new Intent(DashBoard.this, Trips.class));
        } else if (id == R.id.myearning) {
            startActivity(new Intent(DashBoard.this, Target.class));
        } else if (id == R.id.summary) {
            startActivity(new Intent(DashBoard.this, Summary.class));

        } else if (id == R.id.help) {
            startActivity(new Intent(DashBoard.this, help.class));

        } else if (id == R.id.share) {

        } else if (id == R.id.logout) {
            logoutDialog();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void Logout() {
        String Token = ((User) LocalPersistence.readObjectFromFile(DashBoard.this)).getAccessToken();
        LocalPersistence.deletefile(DashBoard.this);
        if (NetworkUtil.isConnectedToInternet(getApplicationContext())) {
            ProgressDialog dialog = new ProgressDialog(DashBoard.this, R.style.AppCompatAlertDialogStyle);
            dialog.setMessage("Logging Out");
            dialog.show();
            mApi.Logout("Bearer " + Token).enqueue(new Callback<LogoutResponse>() {
                @Override
                public void onResponse(Call<LogoutResponse> call, retrofit2.Response<LogoutResponse> response) {
                    if (response.isSuccessful()) {
                        dialog.dismiss();
                        Log.e("logout", "onResponse: " + response.body().getMessage());
                        startActivity(new Intent(getApplicationContext(), Signin.class));
                        finish();
                    } else {

                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "An error Occurred", Toast.LENGTH_SHORT).show();


                        dialog.dismiss();

                    }
                }

                @Override
                public void onFailure(Call<LogoutResponse> call, Throwable t) {
                    dialog.dismiss();
                }
            });
        } else
            NetworkUtil.showNoInternetAvailableErrorDialog(getApplicationContext());


    }

    private void logoutDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("YaadTaxi User App")
                .setIcon(ContextCompat.getDrawable(this, R.drawable.yaadtaxi))
                .setMessage("Are you sure want to logout")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {

                    dialog.dismiss();
                    Logout();
                    timer.cancel();

                })
                .setNegativeButton("No", (dialog, id) -> {
                    dialog.dismiss();
                });

        final AlertDialog alert = builder.create();
        //2. now setup to change color of the button
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(DashBoard.this, R.color.back));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(DashBoard.this, R.color.back));
            }
        });

        alert.show();
    }


}