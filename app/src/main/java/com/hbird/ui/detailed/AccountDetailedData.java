package com.hbird.ui.detailed;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

public class AccountDetailedData extends BaseObservable {

    private int yyyy = 2018;// 当前年
    private int mm = 8;// 当前月
    private String spendingMoney = "0.00";// 支出的钱
    private String inComeMoney = "0.00";// 收入的钱
    private boolean noData = true;// 是否有数据

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
    public String getSpendingMoney() {
        return spendingMoney;
    }

    public void setSpendingMoney(String spendingMoney) {
        this.spendingMoney = spendingMoney;
        notifyPropertyChanged(BR.spendingMoney);
    }

    @Bindable
    public String getInComeMoney() {
        return inComeMoney;
    }

    public void setInComeMoney(String inComeMoney) {
        this.inComeMoney = inComeMoney;
        notifyPropertyChanged(BR.inComeMoney);
    }

    @Bindable
    public boolean isNoData() {
        return noData;
    }

    public void setNoData(boolean noData) {
        this.noData = noData;
        notifyPropertyChanged(BR.noData);
    }
}
