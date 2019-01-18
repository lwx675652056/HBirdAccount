package com.hbird.ui.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

public class DataData extends BaseObservable {

    private int select = 0;// 当前选择的第几个

    public DataData() {
        this.select = 0;
    }

    @Bindable
    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
        notifyPropertyChanged(BR.select);
    }
}