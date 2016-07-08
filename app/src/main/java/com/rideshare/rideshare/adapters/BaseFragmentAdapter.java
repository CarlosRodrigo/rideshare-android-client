package com.rideshare.rideshare.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.rideshare.rideshare.fragments.ItineraryFragment;
import com.rideshare.rideshare.fragments.RidesFragment;

public class BaseFragmentAdapter extends FragmentStatePagerAdapter {

    private String[] mTabTitles;

    public BaseFragmentAdapter(FragmentManager fm, String[] tabTitles) {
        super(fm);
        mTabTitles = tabTitles;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ItineraryFragment();
            case 1:
                return new RidesFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mTabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitles[position];
    }
}
