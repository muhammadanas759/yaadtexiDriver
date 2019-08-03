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

import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.MapView;
import com.taxidriver.app.R;

import java.util.HashSet;
import java.util.List;

public class MapLocationAdapter extends RecyclerView.Adapter<MapLocationViewHolder> {
    protected HashSet<MapView> mMapViews = new HashSet<>();
//    protected List<TripResponse> mMapLocations;

//    public void setMapLocations(List<TripResponse> mapLocations) {
//        mMapLocations = mapLocations;
//    }

    @Override
    public MapLocationViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        MapLocationViewHolder viewHolder = new MapLocationViewHolder(viewGroup.getContext(), view);

        mMapViews.add(viewHolder.mapView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MapLocationViewHolder viewHolder, int position) {
//        TripResponse mapLocation = mMapLocations.get(position);
//
//
//        viewHolder.id.setText(mapLocation.getBookingId());
//        viewHolder.date.setText(mapLocation.getAssignedAt());
//        viewHolder.price.setText(String.valueOf(mapLocation.getPayment().getTotal()));
//
//        Location maploaction = new Location("");
//        maploaction.setLatitude(mapLocation.getSLatitude());
//        maploaction.setLongitude(mapLocation.getSLongitude());
//        Location tol = new Location("");
//        tol.setLatitude(mapLocation.getDLatitude());
//        tol.setLongitude(mapLocation.getDLongitude());
//        viewHolder.setMapLocation(maploaction, tol);
    }

    @Override
    public int getItemCount() {
        return 0;
//        return mMapLocations == null ? 0 : mMapLocations.size();
    }


}
