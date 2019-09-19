package com.yaaddrivertaxi.app.Ride;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.yaaddrivertaxi.app.Adapter.CustomAutoCompleteAdapter;
import com.yaaddrivertaxi.app.Map.DashBoard;
import com.yaaddrivertaxi.app.Model.Place;
import com.yaaddrivertaxi.app.R;

public class PickLocation extends AppCompatActivity {

    private ImageView mCancelToLocation, mCancelFromLocation;
    private AutoCompleteTextView mToLocation, mFromLocation;
    private LinearLayout mPickLocation;

    private Location to, from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_location);

        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addApi(Places.GEO_DATA_API).build();
        mGoogleApiClient.connect();
        mToLocation = findViewById(R.id.to);
        mFromLocation = findViewById(R.id.from);
        CustomAutoCompleteAdapter adapter = new CustomAutoCompleteAdapter(this, mGoogleApiClient);
        CustomAutoCompleteAdapter adapters = new CustomAutoCompleteAdapter(this, mGoogleApiClient);
        mToLocation.setAdapter(adapter);
        mFromLocation.setAdapter(adapters);
        mCancelToLocation = findViewById(R.id.close_to);
        mCancelFromLocation = findViewById(R.id.close_from);
        mPickLocation = findViewById(R.id.pick_location);
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle.getShort("type") == 0) {
                mFromLocation.setText(bundle.getSerializable("address").toString());
                mCancelFromLocation.setVisibility(View.VISIBLE);
                from = bundle.getParcelable("location");
            }

        }

        mToLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if (s.toString().trim().length() != 0) {
                    mCancelToLocation.setVisibility(View.VISIBLE);
                } else {
                    mCancelToLocation.setVisibility(View.GONE);
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mFromLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().trim().length() != 0) {
                    mCancelFromLocation.setVisibility(View.VISIBLE);
                } else {
                    mCancelFromLocation.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mCancelFromLocation.setOnClickListener(v -> {
            mFromLocation.setText("");
            mCancelFromLocation.setVisibility(View.GONE);
        });
        mCancelToLocation.setOnClickListener(v -> {
            mToLocation.setText("");
            mCancelToLocation.setVisibility(View.GONE);
        });


        mPickLocation.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), DashBoard.class);
            Bundle bundle = getIntent().getExtras();
            bundle.putSerializable("address", bundle.getSerializable("address"));

            bundle.putParcelable("location", from);
            bundle.putBoolean("pick", true);
            bundle.putBoolean("jaga", false);
            bundle.putSerializable("responce",getIntent().getExtras().getSerializable("responce"));
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.back).setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), DashBoard.class))
        );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 200) {
                to = (Location) data.getExtras().getSerializable("location");
                mToLocation.setText(data.getExtras().getSerializable("address").toString());
                mCancelToLocation.setVisibility(View.VISIBLE);

            }
        }
    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, DashBoard.class);
        startActivity(intent);
        finish();
    }

    private void backPressed() {
        Bundle bundle = new Bundle();
        bundle.putShort("type", (short) 0);

        bundle.putParcelable("to", to);
        bundle.putParcelable("from", to);
        bundle.putSerializable("responce",getIntent().getExtras().getSerializable("responce"));
        Intent intent = new Intent(this, DashBoard.class);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void click(Place place) {

        Intent intent = new Intent(this, DashBoard.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("place", place);
        bundle.putSerializable("address", getIntent().getExtras().getSerializable("address"));
        bundle.putBoolean("pick", true);
        bundle.putParcelable("location", from);
        bundle.putSerializable("responce",getIntent().getExtras().getSerializable("responce"));
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}
