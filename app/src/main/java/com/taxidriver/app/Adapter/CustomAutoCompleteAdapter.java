package com.taxidriver.app.Adapter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.taxidriver.app.Model.Place;
import com.taxidriver.app.R;
import com.taxidriver.app.Ride.PickLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomAutoCompleteAdapter extends ArrayAdapter {
    public static final String TAG = "CustomAutoCompAdapter";
    private List<Place> dataList;
    private Context mContext;
    private GeoDataClient geoDataClient;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> mAreas;
    private CustomAutoCompleteAdapter.CustomAutoCompleteFilter listFilter =
            new CustomAutoCompleteAdapter.CustomAutoCompleteFilter();
    private List<Place> names = new ArrayList<>();
    private GoogleApiClient googleApiClient;

    //    private TextView country;
    private Spinner placeType;
    private int[] placeTypeValues = {1, 2, 3, 4, 6, 9};

    public CustomAutoCompleteAdapter(Context context, GoogleApiClient googleApiClient) {
        super(context, android.R.layout.simple_dropdown_item_1line,
                new ArrayList<Place>());
        mContext = context;
        this.googleApiClient = googleApiClient;
        //get GeoDataClient
        geoDataClient = Places.getGeoDataClient(mContext, null);
        mAreas = new ArrayList<>();
        mRecyclerView = ((PickLocation) context).findViewById(R.id.results);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView

        // names.add(new Place("taha"));  names.add(new Place("taha"));  names.add(new Place("taha"));  names.add(new Place("taha"));  names.add(new Place("taha"));  names.add(new Place("taha"));  names.add(new Place("taha"));  names.add(new Place("taha"));  names.add(new Place("taha"));  names.add(new Place("taha"));  names.add(new Place("taha"));  // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(dataList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        //get country textview, placetype spinner to get
        // current values to perform research
//        country = ((Activity) context).findViewById(R.id.country);
//        placeType = ((Activity) context).findViewById(R.id.place_type);

        //spinner value map from array resources
//        placeTypeValues = ((Activity) context).getResources().
//                getIntArray(R.string.placeTypesValue);

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Place getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.activity_list_item,
                            parent, false);
        }

        TextView textOne = view.findViewById(android.R.id.text1);
        textOne.setText(dataList.get(position).getPlaceText());

        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }

    public class CustomAutoCompleteFilter extends Filter {
        private Object lock = new Object();
        private Object lockTwo = new Object();
        private boolean placeResults = false;


        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            placeResults = false;
            final List<Place> placesList = new ArrayList<>();

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = new ArrayList<Place>();
                    results.count = 0;
                }
            } else {
                final String searchStrLowerCase = prefix.toString().toLowerCase();

                Task<AutocompletePredictionBufferResponse> task
                        = getAutoCompletePlaces(searchStrLowerCase);

                task.addOnCompleteListener(new OnCompleteListener<AutocompletePredictionBufferResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<AutocompletePredictionBufferResponse> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Auto complete prediction successful");
                            AutocompletePredictionBufferResponse predictions = task.getResult();
                            Place autoPlace;
                            for (AutocompletePrediction prediction : predictions) {
                                autoPlace = new Place();
                                autoPlace.setPlaceId(prediction.getPlaceId());
                                autoPlace.setPlaceText(prediction.getPrimaryText(null).toString());
                                Place finalAutoPlace = autoPlace;
                                Places.GeoDataApi.getPlaceById(googleApiClient, prediction.getPlaceId())
                                        .setResultCallback(new ResultCallback<PlaceBuffer>() {
                                            @Override
                                            public void onResult(PlaceBuffer places) {
                                                if (places.getStatus().isSuccess()) {
                                                    finalAutoPlace.setPlaceText(places.get(0).getName().toString());

                                                    Geocoder geocoder = new Geocoder(mContext, places.get(0).getLocale());
                                                    List<Address> addresses = null;
                                                    try {
                                                        addresses = geocoder.getFromLocation(places.get(0).getLatLng().latitude, places.get(0).getLatLng().longitude, 1);
                                                        if (addresses.get(0).getLocality() != null) {
                                                            finalAutoPlace.setCity(addresses.get(0).getLocality());
                                                        } else if (addresses.get(0).getSubLocality() != null) {
                                                            finalAutoPlace.setCity(addresses.get(0).getSubLocality());
                                                        } else if (addresses.get(0).getSubAdminArea() != null) {
                                                            finalAutoPlace.setCity(addresses.get(0).getSubAdminArea());
                                                        }
//                                                    finalAutoPlace.setCity(prediction.getSecondaryText(null).toString().split(",")[0]);
                                                        finalAutoPlace.setAddress(addresses.get(0).getAddressLine(0));
                                                        finalAutoPlace.setLatitude(places.get(0).getLatLng().latitude);
                                                        finalAutoPlace.setLongitude(places.get(0).getLatLng().longitude);
                                                        Log.e("Place", prediction.getSecondaryText(null).toString());
                                                        Log.e("Place", prediction.getFullText(null).toString());

                                                        //                                                    Log.e("tag",addresses.get(0).getAddressLine(2));
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                            }
                                        });
                                placesList.add(autoPlace);

                            }
                            // predictions.release();
                            Log.d(TAG, "Auto complete predictions size " + placesList.size());
                        } else {
                            Log.d(TAG, "Auto complete prediction unsuccessful");
                        }
                        //inform waiting thread about api call completion
                        placeResults = true;
                        synchronized (lockTwo) {
                            lockTwo.notifyAll();
                        }
                    }
                });

                //wait for the results from asynchronous API call
                while (!placeResults) {
                    synchronized (lockTwo) {
                        try {
                            lockTwo.wait();
                        } catch (InterruptedException e) {

                        }
                    }
                }
                results.values = placesList;
                results.count = placesList.size();
                Log.d(TAG, "Autocomplete predictions size after wait" + results.count);
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                dataList = (List<Place>) results.values;
                mAdapter = new MyAdapter(dataList, getContext());
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            } else {
                dataList = null;
            }
//            if (results.count > 0) {
//
//               // notifyDataSetChanged();
//            } else {
//               // notifyDataSetInvalidated();
//            }
        }

        private Task<AutocompletePredictionBufferResponse> getAutoCompletePlaces(String query) {
//            create autocomplete filter using data from filter Views
            AutocompleteFilter.Builder filterBuilder = new AutocompleteFilter.Builder();
//            filterBuilder.setCountry(country.getText().toString());
//            filterBuilder.setTypeFilter(placeTypeValues[placeType.getSelectedItemPosition()]);

            Task<AutocompletePredictionBufferResponse> results =
                    geoDataClient.getAutocompletePredictions(query, null,
                            filterBuilder.build());
            return results;
        }
    }


//    public void click(String data){
//        ((Location_getter)mContext).click(data);
//    }
}
