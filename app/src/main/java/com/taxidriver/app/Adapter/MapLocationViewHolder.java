/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taxidriver.app.Adapter;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.taxidriver.app.Helper.DrawingHelper;
import com.taxidriver.app.R;


public class MapLocationViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
    public TextView date;
    public TextView id;
    public CardView cardView;
    public TextView price;
    public MapView mapView;
    protected GoogleMap mGoogleMap;
    protected Location mMapLocation;
    protected Location to;
    private Context mContext;

    public MapLocationViewHolder(Context context, View view) {
        super(view);

        mContext = context;
        cardView=view.findViewById(R.id.card_view);
        date = (TextView) view.findViewById(R.id.date);
        price = (TextView) view.findViewById(R.id.price);

        id = (TextView) view.findViewById(R.id.bookingid);
        mapView = (MapView) view.findViewById(R.id.map);

        mapView.onCreate(null);
        mapView.setClickable(false);
        mapView.getMapAsync(this);
    }

    public void setMapLocation(Location mapLocation, Location tol) {
        mMapLocation = mapLocation;
        to = tol;
        Log.e("where", "setMapLocation");
        // If the map is ready, update its content.
        if (mGoogleMap != null) {

            mGoogleMap.setOnMapClickListener(null);
            updateMapContents();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        Log.e("where", "onMapReady");

        MapsInitializer.initialize(mContext);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        if (mGoogleMap != null) {
            updateMapContents();
        }
        // If we have map data, update the map content.

    }

    protected void updateMapContents() {
        // Since the mapView is re-used, need to remove pre-existing mapView features.
        DrawingHelper helper = new DrawingHelper(mGoogleMap, mContext);
        LatLng from = new LatLng(mMapLocation.getLatitude(), mMapLocation.getLongitude());
        LatLng latLngto = new LatLng(to.getLatitude(), to.getLongitude());

        String url = helper.getDirectionsUrl(from, latLngto);
//        helper.run(url, 2);
        helper.run(url);
        CameraPosition.Builder builder = new CameraPosition.Builder();
        builder.zoom(16);
        builder.target(from);
//         Creating a marker
        MarkerOptions markerOptions = new MarkerOptions();

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
}
