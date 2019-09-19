package com.yaaddrivertaxi.app.Map;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.yaaddrivertaxi.app.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class DropMapLocation extends AppCompatActivity implements OnMapReadyCallback {

    private SupportMapFragment mMap;
    private GoogleMap map;
    private ImageView mCurrentLocation;
    private ImageView mCenter;
    private EditText mToLocation, mFromLocation;
    private LinearLayout mPickLocation;
    private Location FromLocation;
    private Location mCurrentLocationLongitudeLatitutde;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_map_location);

        mMap = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mMap.onCreate(savedInstanceState);

        mCenter = findViewById(R.id.center);

        mMap.getMapAsync(this);
        mCurrentLocation = findViewById(R.id.current_location);


        mCurrentLocation.setOnClickListener(v -> {


            mCurrentLocationLongitudeLatitutde = getcurrentLocation();
            mCurrentLocation.setVisibility(View.GONE);
        });

        TextView mDone = findViewById(R.id.done);
        mDone.setOnClickListener(v ->
        {

            getAddress();

            visibility();


        });
    }

    private void visibility() {
        mPickLocation.setVisibility(View.VISIBLE);
//            overridePendingTransition(R.anim.slide_out_top, R.anim.slide_out_bottom);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_in_top);
        View include = findViewById(R.id.downselection);
        include.setVisibility(View.VISIBLE);
        include.startAnimation(slideUp);
        TextView mDonebottom = include.findViewById(R.id.done);
        mDonebottom.setOnClickListener(view -> {
            Animation slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom);
            include.setVisibility(View.GONE);
            include.startAnimation(slideDown);
            View include1 = findViewById(R.id.second);
            include1.setVisibility(View.VISIBLE);
            include1.startAnimation(slideUp);
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

            map.setOnMapClickListener(latLng -> {
                include.setVisibility(View.VISIBLE);
                include.startAnimation(slideUp);
                include1.setVisibility(View.GONE);
                include1.startAnimation(slideDown);
                mPickLocation.animate()
                        .translationY(0)
                        .alpha(1.0f)
                        .setDuration(500)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mPickLocation.setVisibility(View.VISIBLE);
                            }
                        });
                map.setOnMapClickListener(null);
            });

        });
    }

    private void getAddress() {
        Geocoder geocoder;
        List<Address> addresses = null;
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

        com.yaaddrivertaxi.app.Model.Address addresss = new com.yaaddrivertaxi.app.Model.Address(address, city, state, country, postalCode, knownName);
        mToLocation = findViewById(R.id.to);
        mFromLocation = findViewById(R.id.from);
        mPickLocation = findViewById(R.id.to_from);

        mFromLocation.setText(getIntent().getExtras().getSerializable("address").toString());
        FromLocation = getIntent().getExtras().getParcelable("location");
        findViewById(R.id.back).setOnClickListener(view -> onBackPressed());
        mToLocation.setText(addresss.toString());
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(this, DashBoard.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {

        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(false);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
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

                Location centerLocation = new Location(LocationManager.GPS_PROVIDER);
                centerLocation.setLatitude(map.getCameraPosition().target.latitude);
                centerLocation.setLongitude(map.getCameraPosition().target.longitude);
                mCurrentLocationLongitudeLatitutde = centerLocation;

            }
        });


        try {
            boolean success = map.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyles));
            mCurrentLocationLongitudeLatitutde = getcurrentLocation();
            if (!success) {
                Toast.makeText(this, "no styles", Toast.LENGTH_SHORT).show();
            }
        } catch (Resources.NotFoundException e) {
            Log.e("error", e.getLocalizedMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private Location getcurrentLocation() {
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return null;
        }
        Location location = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(criteria, false));
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng loc = new LatLng(latitude, longitude);
        CameraPosition.Builder builder = new CameraPosition.Builder();
        builder.zoom(16);
        builder.target(loc);
        map.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));

        return location;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMap.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMap.onLowMemory();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMap.onPause();
        overridePendingTransition(0, 0);
    }

}