package com.taxidriver.app.Adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.taxidriver.app.Fragments.PastTripFragment;
import com.taxidriver.app.Fragments.UpcomingTripFragment;


public class TripAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public TripAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                PastTripFragment pastTripFragment= new PastTripFragment();
                return pastTripFragment;
            case 1:
                UpcomingTripFragment upcomingTripFragment= new UpcomingTripFragment();
                return upcomingTripFragment;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}