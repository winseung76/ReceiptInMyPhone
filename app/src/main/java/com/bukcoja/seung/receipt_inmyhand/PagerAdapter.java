package com.bukcoja.seung.receipt_inmyhand;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bukcoja.seung.receipt_inmyhand.View.BreakfastFragment;
import com.bukcoja.seung.receipt_inmyhand.View.DinnerFragment;
import com.bukcoja.seung.receipt_inmyhand.View.LunchFragment;

public class PagerAdapter extends FragmentStatePagerAdapter{

    int mNumOfTabs;
    private BreakfastFragment tab1;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment;
        switch (position) {
            case 0:
                fragment=new BreakfastFragment();
                return fragment;
            case 1:
                fragment=new LunchFragment();
                return fragment;
            case 2:
                fragment=new DinnerFragment();
                return fragment;
            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
