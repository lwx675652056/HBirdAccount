package com.hbird.ui.assets;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

/**
 * @author: LiangYX
 * @ClassName: AssetsData
 * @date: 2018/12/29 14:57
 * @Description: 资产首页
 */
public class AssetsData extends BaseObservable {

    private String time = "2019年01月01日";// 年
    private String value = "0.00";// 资产值
    private boolean edit = false;// 是否为编辑名称状态

    @Bindable
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
        notifyPropertyChanged(BR.time);
    }

    @Bindable
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        notifyPropertyChanged(BR.value);
    }

    @Bindable
    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
        notifyPropertyChanged(BR.edit);
    }
}