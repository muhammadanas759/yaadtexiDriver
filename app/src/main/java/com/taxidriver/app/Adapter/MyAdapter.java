package com.taxidriver.app.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.taxidriver.app.Model.Place;
import com.taxidriver.app.R;
import com.taxidriver.app.Ride.PickLocation;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    public List<Place> mDataset;
    public Context context;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<Place> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }

    public void update(List<Place> names) {
        this.mDataset = names;
        notifyDataSetChanged();

    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city_list_item, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if (mDataset != null) {
            holder.mTextView.setText(mDataset.get(position).getPlaceText());
            holder.city.setText(mDataset.get(position).getCity());
            Log.e("place", mDataset.get(position).toString());
            holder.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((PickLocation) context).click(mDataset.get(position));


                }
            });
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

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public TextView city;

        public MyViewHolder(View v) {
            super(v);
            mTextView = v.findViewById(R.id.list_item_textView);
            city = v.findViewById(R.id.list_state_textView);

        }
    }
}
