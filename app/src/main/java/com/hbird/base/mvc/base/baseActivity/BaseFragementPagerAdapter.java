package com.hbird.base.mvc.base.baseActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hbird.base.mvc.base.BaseFragement;
import com.hbird.base.util.L;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/10/11.
 */

public class BaseFragementPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<BaseFragement> mFragments;

    public BaseFragementPagerAdapter(FragmentManager fm, ArrayList<BaseFragement> fragments) {
        super(fm);
        this.mFragments = new ArrayList<>();
        this.mFragments.addAll(fragments);
        L.e(mFragments.size()+"");
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
