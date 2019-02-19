package com.hbird.ui.calendar;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

/**
 * @author: LiangYX
 * @ClassName: CalendarData
 * @date: 2018/12/26 15:38
 * @Description: 日历
 */
public class CalendarData extends BaseObservable {

    private int yyyy = 2018;// 年
    private int mm = 8;// 月
    private boolean showToday = true;

    public CalendarData() {
        this.showToday = false;
    }

    @Bindable
    public int getYyyy() {
        return yyyy;
    }

    public void setYyyy(int yyyy) {
        this.yyyy = yyyy;
        notifyPropertyChanged(BR.yyyy);
    }

    @Bindable
    public int getMm() {
        return mm;
    }

    public void setMm(int mm) {
        this.mm = mm;
        notifyPropertyChanged(BR.mm);
    }

    @Bindable
    public boolean isShowToday() {
        return showToday;
    }

    public void setShowToday(boolean showToday) {
        this.showToday = showToday;
        notifyPropertyChanged(BR.showToday);
    }
}
