package com.yaaddrivertaxi.app.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yaaddrivertaxi.app.Adapter.TargetAdapter;
import com.yaaddrivertaxi.app.ApiResponse.Target.TargetResponse;
import com.yaaddrivertaxi.app.Connection.Services;
import com.yaaddrivertaxi.app.Connection.Utils;
import com.yaaddrivertaxi.app.Model.User;
import com.yaaddrivertaxi.app.R;
import com.yaaddrivertaxi.app.Utils.LocalPersistence;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Target extends AppCompatActivity {
private TextView mEarning,mTargetCompleted,noRides;
ProgressBar mProgressBar;
private RecyclerView recyclerView;
Services api;
User data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);
        getSupportActionBar().setTitle("My Earning");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        data = (User) LocalPersistence.readObjectFromFile(Target.this);
        String token=data.getAccessToken();
        Log.e("bearer token", "onCreate:bearer token "+data.getAccessToken());
//        String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjIwMywiaXNzIjoiaHR0cDovL3lhYWR0YXhpLmNvbS9hcGkvcHJvdmlkZXIveWFhZC9sb2dpbiIsImlhdCI6MTU2NDg2MjQwOSwiZXhwIjoxNTY1MjIyNDA5LCJuYmYiOjE1NjQ4NjI0MDksImp0aSI6IlJlZEJlTlZyVm10NUVvS1MifQ.JPg-6BOYk8bg1A0lNCTDh87Lpib1fgdpFsyApHL34tI";
        api.Target("Bearer " + token).enqueue(new Callback<TargetResponse>() {
            @Override
            public void onResponse(Call<TargetResponse> call, Response<TargetResponse> response) {
                if(response.isSuccessful()){
                    mTargetCompleted.setText(String.valueOf(response.body().getRidesCount()).concat("/10"));
                   mProgressBar.setProgress(response.body().getRidesCount()*10);
                    recyclerView.setHasFixedSize(true);
                    LinearLayoutManager linearLayoutManager =new LinearLayoutManager(Target.this, LinearLayoutManager.VERTICAL,false);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    TargetAdapter adapter=new TargetAdapter(Target.this,response.body().getRides());
                    recyclerView.setAdapter(adapter);
                    if(response.body().getRides().isEmpty()){
noRides.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    Toast.makeText(Target.this, "not", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TargetResponse> call, Throwable t) {

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void init() {
    mEarning=findViewById(R.id.targetearning);
    mTargetCompleted=findViewById(R.id.targetcompleted);
    noRides=findViewById(R.id.norides);
    mProgressBar=findViewById(R.id.ProgressBar);
    api= Utils.getApiService();
    data = (User) LocalPersistence.readObjectFromFile(Target.this);
    recyclerView=findViewById(R.id.targetrecyclerview);
    }
}
