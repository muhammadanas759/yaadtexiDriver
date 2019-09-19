package com.yaaddrivertaxi.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.yaaddrivertaxi.app.ApiResponse.Target.Ride;
import com.yaaddrivertaxi.app.R;


import java.util.List;

public class TargetAdapter extends RecyclerView.Adapter<TargetAdapter.MyViewHolder> {
        public List<Ride> mDataset;
        public Context context;

        // Provide a suitable constructor (depends on the kind of dataset)
        public TargetAdapter(Context context,List<Ride> myDataset) {
            mDataset = myDataset;
            this.context = context;
        }


        // Create new views (invoked by the layout manager)
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                    int viewType) {
            // create a new view
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.target_item, parent, false);
            MyViewHolder vh = new MyViewHolder(view);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            if (mDataset != null) {
               holder.time.setText(mDataset.get(position).getCreatedAt());
               holder.distance.setText(String.valueOf(mDataset.get(position).getDistance()).concat(" km"));
               holder.amount.setText(mDataset.get(position).getServiceType().getPrice().toString());

            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            if (mDataset != null)
                return mDataset.size();
            else
                return 0;
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView time,amount,distance;


            public MyViewHolder(View v) {
                super(v);
                time = v.findViewById(R.id.targettime);
                amount = v.findViewById(R.id.targetamount);
                distance = v.findViewById(R.id.targetdistance);

            }
        }
    }

