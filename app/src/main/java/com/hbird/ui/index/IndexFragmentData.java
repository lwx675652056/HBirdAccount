package com.hbird.ui.index;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

public class IndexFragmentData extends BaseObservable {

    private int mm = 12;// 当前月
    private String spendingMoney = "0.00";// 支出的钱
    private String inComeMoney = "0.00";// 收入的钱
    private String budget = "0.00";// 剩余预算
    private boolean showMoney = true;// 是否显示金钱
    private boolean show = false;// 显示邀请按钮
    private boolean showArrow = false;// 显示下拉三角箭头
    private boolean showMember = false;// 显示账本内成员
    private boolean showMemberSeting = false;// 显示成员设置


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
    public String getBudget() {
        return budget;
    }

    public void setBudgety(String budget) {
        this.budget = budget;
        notifyPropertyChanged(BR.budget);
    }

    @Bindable
    public boolean isShowMoney() {
        return showMoney;
    }

    public void setShowMoney(boolean showMoney) {
        this.showMoney = showMoney;
        notifyPropertyChanged(BR.showMoney);
    }

    @Bindable
    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
        notifyPropertyChanged(BR.show);
    }
    @Bindable
    public boolean isShowArrow() {
        return showArrow;
    }

    public void setShowArrow(boolean showArrow) {
        this.showArrow = showArrow;
        notifyPropertyChanged(BR.showArrow);
    }
    @Bindable
    public boolean isShowMember() {
        return showMember;
    }

    public void setShowMember(boolean showMember) {
        this.showMember = showMember;
        notifyPropertyChanged(BR.showMember);
    }
    @Bindable
    public boolean isShowMemberSeting() {
        return showMemberSeting;
    }

    public void setShowMemberSeting(boolean showMemberSeting) {
        this.showMemberSeting = showMemberSeting;
        notifyPropertyChanged(BR.showMemberSeting);
    }
}
