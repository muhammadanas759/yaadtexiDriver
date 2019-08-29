package com.taxidriver.app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.MapView;
import com.taxidriver.app.Activities.TripDetails;
import com.taxidriver.app.Activities.UpcomingTripDetail;
import com.taxidriver.app.ApiResponse.CancelTrip.CancelTripResponse;
import com.taxidriver.app.ApiResponse.UpcomingTripResponse.UpcomingResponse;
import com.taxidriver.app.Connection.Utils;
import com.taxidriver.app.Fragments.PastTripFragment;
import com.taxidriver.app.Model.User;
import com.taxidriver.app.R;
import com.taxidriver.app.Utils.LocalPersistence;


import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingAdapter  extends RecyclerView.Adapter<UpcomingMapLocationHolder> {
    protected HashSet<MapView> mMapViews = new HashSet<>();
    protected List<UpcomingResponse> mMapLocations;
    protected Context context;
    public void setMapLocations(List<UpcomingResponse> mapLocations, Context context) {
        mMapLocations = mapLocations;
        this.context=context;
    }

    @Override
    public UpcomingMapLocationHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.upcoming_list_item, viewGroup, false);
        UpcomingMapLocationHolder viewHolder = new UpcomingMapLocationHolder(viewGroup.getContext(), view);

        mMapViews.add(viewHolder.mapView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UpcomingMapLocationHolder viewHolder, int position) {
        UpcomingResponse mapLocation = mMapLocations.get(position);




        viewHolder.id.setText(mapLocation.getBookingId());
        viewHolder.date.setText(mapLocation.getAssignedAt());
        viewHolder.service_detail.setText(mapLocation.getServiceType().getName());
        viewHolder.cardView.setOnClickListener(v -> {


            Intent intent =new Intent(context, UpcomingTripDetail.class);
            intent.putExtra("id",mapLocation.getId());

            context.startActivity(intent);

        });



        viewHolder.cancel.setOnClickListener(v->{
            callCancelApi(position,mapLocation.getId());
        });
        Location maploaction = new Location("");
        maploaction.setLatitude(mapLocation.getSLatitude());
        maploaction.setLongitude(mapLocation.getSLongitude());
        Location tol = new Location("");
        tol.setLatitude(mapLocation.getDLatitude());
        tol.setLongitude(mapLocation.getDLongitude());
        viewHolder.setMapLocation(maploaction, tol);
    }

    private void callCancelApi(int position,Integer ids) {
        String Token=((User) LocalPersistence.readObjectFromFile(context)).getAccessToken();

//        PastTripFragment object=context.getSu.findFragmentById(R.id.id_frag);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("YaadTaxi User App")
                .setMessage("Are you sure want to cancel ride")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {

                    dialog.dismiss();
                    Utils.getApiService().CancelTrip("Bearer "+Token,
                            String.valueOf(ids)).enqueue(new Callback<CancelTripResponse>() {
                        @Override
                        public void onResponse(Call<CancelTripResponse> call, Response<CancelTripResponse> response) {
                            if (response.isSuccessful()){
                                if (response.body()!=null){
                                    mMapLocations.remove(position);
                                    notifyItemRemoved(position);
                                    if (notifyMe!=null){
                                        notifyMe.deletItem();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CancelTripResponse> call, Throwable t) {

                        }
                    });


                })
                .setNegativeButton("No",(dialog,id)->{
                    dialog.dismiss();
                });

        final AlertDialog alert = builder.create();
        //2. now setup to change color of the button

        alert.show();


    }

    @Override
    public int getItemCount() {
            return mMapLocations == null ? 0 : mMapLocations.size();

//        return mMapLocations == null ? 0 : mMapLocations.size();
    }

    public interface delete{
         void deletItem();
    }
    private  delete notifyMe;

    public  void bindListener (delete notifyMeListener){
        notifyMe = notifyMeListener;
    }


}
