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
public class AssetsDetailData extends BaseObservable {

    private int yyyy = 2018;// 当前年
    private int mm = 8;// 当前月
    private String spendingMoney = "0.00";// 支出的钱
    private String inComeMoney = "0.00";// 收入的钱

    private String icon;// 账户图标
    private String assetsName;// 账户名
    private double money;
    private int assetsType;// 账户id

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
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
        notifyPropertyChanged(BR.icon);
    }

    @Bindable
    public String getAssetsName() {
        return assetsName;
    }

    public void setAssetsName(String assetsName) {
        this.assetsName = assetsName;
        notifyPropertyChanged(BR.assetsName);
    }

    @Bindable
    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
        notifyPropertyChanged(BR.money);
    }

    @Bindable
    public int getAssetsType() {
        return assetsType;
    }

    public void setAssetsType(int assetsType) {
        this.assetsType = assetsType;
        notifyPropertyChanged(BR.assetsType);
    }
}