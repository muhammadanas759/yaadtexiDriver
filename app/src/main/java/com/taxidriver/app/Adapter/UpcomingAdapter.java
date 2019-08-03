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

public class UpcomingAdapter  extends RecyclerView.Adapter<MapLocationViewHolder> {
    protected HashSet<MapView> mMapViews = new HashSet<>();
//    protected List<UpcomingResponse> mMapLocations;

//    public void setMapLocations(List<UpcomingResponse> mapLocations) {
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
//        UpcomingResponse mapLocation = mMapLocations.get(position);



//
//        viewHolder.id.setText(mapLocation.getBookingId());
//        viewHolder.date.setText(mapLocation.getAssignedAt());
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
        return 2;
//        return mMapLocations == null ? 0 : mMapLocations.size();
    }


}
