package com.yaaddrivertaxi.app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yaaddrivertaxi.app.ApiResponse.Summary.SummaryResponse;
import com.yaaddrivertaxi.app.Connection.Services;
import com.yaaddrivertaxi.app.Connection.Utils;
import com.yaaddrivertaxi.app.Model.User;
import com.yaaddrivertaxi.app.R;
import com.yaaddrivertaxi.app.Utils.LocalPersistence;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Summary extends AppCompatActivity implements View.OnClickListener{
private RelativeLayout rides,revenue,schduled,cancelled;
private TextView ridecount,revenuecount,schdulecount,cancelcount;
Services mApi;
User data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        getSupportActionBar().setTitle("My Summary");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        mApi= Utils.getApiService2();
        data = (User) LocalPersistence.readObjectFromFile(Summary.this);

        mApi.Summary("Bearer " + data.getAccessToken()).enqueue(new Callback<SummaryResponse>() {
            @Override
            public void onResponse(Call<SummaryResponse> call, Response<SummaryResponse> response) {
                if(response.isSuccessful()){
                    Toast.makeText(Summary.this, "success", Toast.LENGTH_SHORT).show();
ridecount.setText(response.body().getCompletedRides().toString());
revenuecount.setText(response.body().getRevenue().toString());
schdulecount.setText(response.body().getScheduledRides().toString());
cancelcount.setText(response.body().getCancelRides().toString());

                }
                else {
                    Toast.makeText(Summary.this, " not", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SummaryResponse> call, Throwable t) {

            }
        });


    }

    private void init() {
    rides=findViewById(R.id.summ_totalrides);
    revenue=findViewById(R.id.summ_revenue);
    schduled=findViewById(R.id.summ_schdulerides);
    cancelled=findViewById(R.id.summ_cancel);
    ridecount=findViewById(R.id.totalrides);
    revenuecount=findViewById(R.id.totalrevenus);
    schdulecount=findViewById(R.id.totalschdulerides);
    cancelcount=findViewById(R.id.totalcancelledrides);
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
    @Override
    public void onClick(View v) {
switch (v.getId()){
    case R.id.summ_totalrides:
        Toast.makeText(this, "total rides", Toast.LENGTH_SHORT).show();
        break;
    case R.id.summ_revenue:
        Toast.makeText(this, "total revenue", Toast.LENGTH_SHORT).show();
        break;
    case R.id.summ_schdulerides:
        Toast.makeText(this, "upcoming rides", Toast.LENGTH_SHORT).show();
        break;
    case R.id.summ_cancel:
        Toast.makeText(this, "cancelled rides", Toast.LENGTH_SHORT).show();
        break;

}
    }
}
