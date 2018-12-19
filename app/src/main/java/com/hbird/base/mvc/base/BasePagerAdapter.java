package com.hbird.base.mvc.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/2/24.
 */

public class BasePagerAdapter extends FragmentPagerAdapter {

    private ArrayList<BaseFragement> mFragments;

    public BasePagerAdapter(FragmentManager fm, ArrayList<BaseFragement> fragments) {
        super(fm);
        this.mFragments = new ArrayList<>();
        this.mFragments.addAll(fragments);
    }

    @Override
    public Fragment getItem(int position) {

        return mFragments.get(position);
    }

    @Override
    public int getCount() {

        return mFragments.size();
    }


}
