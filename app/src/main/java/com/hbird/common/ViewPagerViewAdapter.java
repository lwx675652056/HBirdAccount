package com.hbird.common;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ViewPagerViewAdapter extends PagerAdapter {

    private List<View> viewList;
    private int currentPos;

    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
        notifyDataSetChanged();
    }

    public ViewPagerViewAdapter(List<View> viewList, int currentPos) {
        this.viewList = viewList;
        this.currentPos = currentPos;
    }

    @Override
    public int getCount() {
        return currentPos + 1;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));
    }
}
