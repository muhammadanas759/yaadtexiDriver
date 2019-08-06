package com.taxidriver.app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.taxidriver.app.ApiResponse.PastTripDetailResponse.PastTripDetailResponse;
import com.taxidriver.app.Connection.Services;
import com.taxidriver.app.Connection.Utils;
import com.taxidriver.app.Helper.DrawingHelper;
import com.taxidriver.app.Map.DashBoard;
import com.taxidriver.app.Model.User;
import com.taxidriver.app.R;
import com.taxidriver.app.Utils.LocalPersistence;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TripDetails extends AppCompatActivity implements OnMapReadyCallback {


    public MapView mapView;

    protected TextView fname,
            lname,
            date,
            time,
            bookingId,
            cash,
            comments;


    protected RatingBar ratings;

    private MapView map;

    private CircleImageView userImage;


    private  GoogleMap googleMap;


    private Services mApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Past Trips");


        mApi = Utils.getApiService();

        initializations();

        callApi();

    }

    private void callApi() {
        String id = "";
        if (getIntent() != null) {
            id = getIntent().getStringExtra("id");
        }
        String Token = ((User) LocalPersistence.readObjectFromFile(TripDetails.this)).getAccessToken();
        mApi.TripDetail("Bearer " + Token,
                id).enqueue(new Callback<List<PastTripDetailResponse>>() {
            @Override
            public void onResponse(Call<List<PastTripDetailResponse>> call, Response<List<PastTripDetailResponse>> response) {
                if (response.isSuccessful()) {
                    DateFormat readFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                    DateFormat writeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");


                    PastTripDetailResponse pastTripDetailResponse = response.body().get(0);


                    String[] dateAndTime = pastTripDetailResponse.getAssignedAt().split(" ");

                    fname.setText(pastTripDetailResponse.getUser().getFirstName());
                    lname.setText(pastTripDetailResponse.getUser().getLastName());

                    Date dates = null;
                    Date times = null;
                    try {
                        dates = inputFormat.parse(dateAndTime[0]);
                        times = readFormat.parse(dateAndTime[1]);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (date != null && time != null) {
                        date.setText(outputFormat.format(dates));
                        time.setText(outputFormat.format(times));
                    }


                    if (pastTripDetailResponse.getPayment() != null)
                        cash.setText("$" + pastTripDetailResponse.getPayment().getTotal());
                    else {
                        cash.setText("$0.00");
                    }
                    bookingId.setText(pastTripDetailResponse.getBookingId());

                    Picasso.get()
                            .load("http://yaadtaxi.com/userprofilepics/" + pastTripDetailResponse.getUser().getPicture())
                            .placeholder(R.drawable.ic_dummy_user)
                            .into(userImage);



                    userImage.setOnClickListener(v -> {
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("user",pastTripDetailResponse.getUser());
                        Intent intent =new Intent(getApplicationContext(),DisplayProfile.class);
                        intent.putExtra("data",bundle);
                        startActivity(intent);
                    });

                    if (pastTripDetailResponse.getUser().getRating() != null)
                        ratings.setRating(Float.valueOf(pastTripDetailResponse.getUser().getRating()));
                    else {
                        ratings.setRating(0f);

                    }
                    lname.setText(pastTripDetailResponse.getUser().getLastName());
                    lname.setText(pastTripDetailResponse.getUser().getLastName());
                    if (googleMap != null) {
                        updateMapContents(pastTripDetailResponse);
                    }

                }
            }

            @Override
            public void onFailure(Call<List<PastTripDetailResponse>> call, Throwable t) {

            }
        });

    }

    protected void updateMapContents(PastTripDetailResponse response) {
        // Since the mapView is re-used, need to remove pre-existing mapView features.
        DrawingHelper helper = new DrawingHelper(googleMap, this);
        LatLng from = new LatLng(Double.valueOf(response.getSLatitude()), Double.valueOf(response.getSLatitude()));
        LatLng latLngto = new LatLng(Double.valueOf(response.getDLatitude()), Double.valueOf(response.getDLatitude()));

        String url = helper.getDirectionsUrl(from, latLngto);
//        helper.run(url, 2);
        helper.run(url);

//         Creating a marker

//        // Setting the position for the marker
//        markerOptions.position(from);
//        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.user_marker));
//        markerOptions = new MarkerOptions();
//
//        // Setting the position for the marker
//        markerOptions.position(from);
//        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.provider_marker));
//        mGoogleMap.addMarker(markerOptions);
    }

    private void initializations() {

        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        cash = findViewById(R.id.cash);
        comments = findViewById(R.id.comments);
        bookingId = findViewById(R.id.bookingid);
        userImage = findViewById(R.id.user);
        ratings = findViewById(R.id.rating);
        mapView = (MapView) findViewById(R.id.map);

        mapView.onCreate(null);
        mapView.setClickable(false);
        mapView.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap mgoogleMap) {
        googleMap = mgoogleMap;
        Log.e("where", "onMapReady");

        MapsInitializer.initialize(this);
        googleMap.getUiSettings().setMapToolbarEnabled(false);


        // If we have map data, update the map content.

    }
}




