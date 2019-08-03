package com.taxidriver.app.Fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.taxidriver.app.Adapter.UpcomingAdapter;


import com.taxidriver.app.Connection.Services;
import com.taxidriver.app.Connection.Utils;
import com.taxidriver.app.Model.User;
import com.taxidriver.app.R;
import com.taxidriver.app.Utils.LocalPersistence;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpcomingTripFragment extends Fragment {
    protected UpcomingAdapter mListAdapter;
    protected RecyclerView mRecyclerView;
    View view;

    public UpcomingTripFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_upcoming_trip, container, false);
        Services mApi = Utils.getApiService();
        Log.e("schedule", "onCreateView: ");

//        String Token = ((Response) LocalPersistence.readObjectFromFile(getActivity())).getAccessToken();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("schedule", "onResume: ");

        String Token=((User) LocalPersistence.readObjectFromFile(getActivity())).getAccessToken();
//        String Token="eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjFkMTE2OThlNDc3YTBkNTZhMTc1N2Y0MjY5ZWE3YmIzNTVkNzAxZDQ3NzdjMzNlMjZhOTA0MzFkMWE4MTBlOGI1ODljYTQ0Njg1ZGRkMDIyIn0.eyJhdWQiOiIxIiwianRpIjoiMWQxMTY5OGU0NzdhMGQ1NmExNzU3ZjQyNjllYTdiYjM1NWQ3MDFkNDc3N2MzM2UyNmE5MDQzMWQxYTgxMGU4YjU4OWNhNDQ2ODVkZGQwMjIiLCJpYXQiOjE1NjQwOTA4NzIsIm5iZiI6MTU2NDA5MDg3MiwiZXhwIjoxODc5NzEwMDcyLCJzdWIiOiIyNzgiLCJzY29wZXMiOltdfQ.HzVI5e9N2aQHFMOZhVn95xAGHsIKQL78X-sOUX5rbzmH2Kb3qEGSO0w1uyNKMaYNd4WWePkqHDAUKPTR3rxIXKdBoCpvcMk4waagax06aEyMWfUT0l_r-QjqiNCn1wGr1tkinRK0oZR3jTD-utn_H8ZGMTxRiC1o7Plxz45VgcedHNF-Vd7QXJP0-ME7Dd3CdGypbFh3pMKohY1ixmkVcOn69p5qwt3rxFTWYl1B8LnjcxbvS9TzlB2kdgO6RBw4sGbe6dVFyuPBwDTFn7fxhAyKLygT7umZAt2n9YXSGmcxc3c7V7Zww87rl_xtdy1hWYTmUwDGiVehdiomXb-9xfkZLzuEzuVcP1jx7kml4k0WxfX7j_CpSNzyzbqKMGId-0wP2iCofsGmkGVc4m_F2T_yxvDfOkUZ5i4Tlx6XdKEvej5gwwIlMHCGgwNp5MLTgauykmXUUZPJU8t6mkOhnr49G-TYB_f0Pg5tl7SHy7aCinK-CsZ2rx5fgm9DUk5AtD01xmKPlAbcCCo5ox1nTiRGRW-On8l7nF5UvsXGXnmuGPZIf3qeZ1NgMvbwWkJCgkXFiCZ1EoLOv6ul3yrCXRMuY6udBEHr8Kcp0dZSGzL_Fl3M4lbgMRwbnTj0TY7jpybtPJvxrwgDQuQhJ9VawMTH8sfKq9pFJQE3HTPzyTM";
//        Utils.getApiService().Upcoming("Bearer " + Token).enqueue(new Callback<List<UpcomingResponse>>() {
//            @Override
//            public void onResponse(Call<List<UpcomingResponse>> call, retrofit2.Response<List<UpcomingResponse>> response) {
//                if (response.isSuccessful()) {
//                    Log.e("schedule", "onResponse: " );
//
//                    mRecyclerView = (RecyclerView) view.findViewById(R.id.card_list2);
//
//                    // Determine the number of columns to display, based on screen width.
//
//                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//                    mListAdapter = new UpcomingAdapter();
//                    mListAdapter.setMapLocations(response.body());
//                    mRecyclerView.setAdapter(mListAdapter);
//                    if (response.body().size() == 0) {
//                        view.findViewById(R.id.main_relative_layout).setVisibility(View.VISIBLE);
//                        mRecyclerView.setVisibility(View.GONE);
//                    }
//                }else
//                {
//                    Toast.makeText(getContext(), response.code()+"", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<UpcomingResponse>> call, Throwable t) {
//                Log.e("response", t.getLocalizedMessage());
//            }
//        });

    }
}
