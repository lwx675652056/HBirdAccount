package com.hbird.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;

import com.android.databinding.library.baseAdapters.BR;

public class TitleBean extends BaseObservable {

    private String title;
    private int rightResId;
    private boolean goneBack = false;//  是否隐藏返回键
    private String rightTxt;// 右侧文字
    private int rightColor = -1;// 右侧文字颜色
    private int bg_color = -1;// 背景颜色
    private Drawable backIcon;// 返回图标

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

    @Bindable
    public int getBg_color() {
        return bg_color;
    }

    public void setBg_color(int bg_color) {
        this.bg_color = bg_color;
        notifyPropertyChanged(BR.bg_color);
    }

    @Bindable
    public Drawable getBackIcon() {
        return backIcon;
    }

    public void setBackIcon(Drawable backIcon) {
        this.backIcon = backIcon;
        notifyPropertyChanged(BR.backIcon);
    }
}
