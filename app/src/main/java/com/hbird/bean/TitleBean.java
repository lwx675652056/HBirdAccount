package com.hbird.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

public class TitleBean extends BaseObservable {

    private String title;
    private int rightResId;
    private boolean goneBack = false;//  是否隐藏返回键
    private String rightTxt;// 右侧文字
    private int rightColor;// 右侧文字

    public TitleBean() {
    }

    public TitleBean(String title) {
        this.title = title;
    }

    public TitleBean(String title, int rightResId) {
        this.title = title;
        this.rightResId = rightResId;
    }

    public TitleBean(boolean goneBack, String title, int rightResId) {
        this.title = title;
        this.rightResId = rightResId;
        this.goneBack = goneBack;
    }

    public TitleBean(String title, String rightTxt, int rightColor) {
        this.title = title;
        this.rightTxt = rightTxt;
        this.rightColor = rightColor;
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public int getRightResId() {
        return rightResId;
    }

    public void setRightResId(int rightResId) {
        this.rightResId = rightResId;
        notifyPropertyChanged(BR.rightResId);
    }

    @Bindable
    public boolean isGoneBack() {
        return goneBack;
    }

    public void setGoneBack(boolean goneBack) {
        this.goneBack = goneBack;
        notifyPropertyChanged(BR.goneBack);
    }

    @Bindable
    public String getRightTxt() {
        return rightTxt;
    }

    public void setRightTxt(String rightTxt) {
        this.rightTxt = rightTxt;
        notifyPropertyChanged(BR.rightTxt);
    }
    @Bindable
    public int getRightColor() {
        return rightColor;
    }

    public void setRightColor(int rightColor) {
        this.rightColor = rightColor;
        notifyPropertyChanged(BR.rightColor);
    }
}
