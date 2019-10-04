    package com.yaaddrivertaxi.app.Fragments;


    import android.os.Bundle;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Toast;

    import androidx.fragment.app.Fragment;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.yaaddrivertaxi.app.Adapter.MapLocationAdapter;

    import com.yaaddrivertaxi.app.ApiResponse.TripHistory.TripHistoryResponse;
    import com.yaaddrivertaxi.app.Connection.Services;
    import com.yaaddrivertaxi.app.Connection.Utils;
    import com.yaaddrivertaxi.app.Model.User;
    import com.yaaddrivertaxi.app.R;
    import com.yaaddrivertaxi.app.Utils.LocalPersistence;

    import java.util.List;

    import retrofit2.Call;
    import retrofit2.Callback;

    /**
     * A simple {@link Fragment} subclass.
     */
    public class PastTripFragment extends Fragment  {

        protected MapLocationAdapter mListAdapter;
        protected RecyclerView mRecyclerView;
        View view;
        private Services mApi;

        public PastTripFragment() {
            // Required empty public constructor
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            view= inflater.inflate(R.layout.fragment_past_trip, container, false);
            mApi = Utils.getApiService();

            String Token=((User)LocalPersistence.readObjectFromFile(getActivity())).getAccessToken();
//            String Token="eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjFkMTE2OThlNDc3YTBkNTZhMTc1N2Y0MjY5ZWE3YmIzNTVkNzAxZDQ3NzdjMzNlMjZhOTA0MzFkMWE4MTBlOGI1ODljYTQ0Njg1ZGRkMDIyIn0.eyJhdWQiOiIxIiwianRpIjoiMWQxMTY5OGU0NzdhMGQ1NmExNzU3ZjQyNjllYTdiYjM1NWQ3MDFkNDc3N2MzM2UyNmE5MDQzMWQxYTgxMGU4YjU4OWNhNDQ2ODVkZGQwMjIiLCJpYXQiOjE1NjQwOTA4NzIsIm5iZiI6MTU2NDA5MDg3MiwiZXhwIjoxODc5NzEwMDcyLCJzdWIiOiIyNzgiLCJzY29wZXMiOltdfQ.HzVI5e9N2aQHFMOZhVn95xAGHsIKQL78X-sOUX5rbzmH2Kb3qEGSO0w1uyNKMaYNd4WWePkqHDAUKPTR3rxIXKdBoCpvcMk4waagax06aEyMWfUT0l_r-QjqiNCn1wGr1tkinRK0oZR3jTD-utn_H8ZGMTxRiC1o7Plxz45VgcedHNF-Vd7QXJP0-ME7Dd3CdGypbFh3pMKohY1ixmkVcOn69p5qwt3rxFTWYl1B8LnjcxbvS9TzlB2kdgO6RBw4sGbe6dVFyuPBwDTFn7fxhAyKLygT7umZAt2n9YXSGmcxc3c7V7Zww87rl_xtdy1hWYTmUwDGiVehdiomXb-9xfkZLzuEzuVcP1jx7kml4k0WxfX7j_CpSNzyzbqKMGId-0wP2iCofsGmkGVc4m_F2T_yxvDfOkUZ5i4Tlx6XdKEvej5gwwIlMHCGgwNp5MLTgauykmXUUZPJU8t6mkOhnr49G-TYB_f0Pg5tl7SHy7aCinK-CsZ2rx5fgm9DUk5AtD01xmKPlAbcCCo5ox1nTiRGRW-On8l7nF5UvsXGXnmuGPZIf3qeZ1NgMvbwWkJCgkXFiCZ1EoLOv6ul3yrCXRMuY6udBEHr8Kcp0dZSGzL_Fl3M4lbgMRwbnTj0TY7jpybtPJvxrwgDQuQhJ9VawMTH8sfKq9pFJQE3HTPzyTM";
            mApi.Trip("Bearer " + Token).enqueue(new Callback<List<TripHistoryResponse>>() {
                @Override
                public void onResponse(Call<List<TripHistoryResponse>> call, retrofit2.Response<List<TripHistoryResponse>> response) {
                    if (response.isSuccessful()) {
                        Log.e("past", "onResponse: " );
                        mRecyclerView = (RecyclerView) view.findViewById(R.id.card_list);

                        // Determine the number of columns to display, based on screen width.

                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        mListAdapter = new MapLocationAdapter();
                        mListAdapter.setMapLocations(response.body(),getContext());

                        if (response.body().size() == 0) {
                            view.findViewById(R.id.main_relative_layout).setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                        }
                        mRecyclerView.setAdapter(mListAdapter);
                    }
                    else{
                        Log.e("Trip", "onResponse: not success ");
                    }
                }

                @Override
                public void onFailure(Call<List<TripHistoryResponse>> call, Throwable t) {
                    Log.e("response", t.getLocalizedMessage());
                }
            });

            return view;
        }


    }
