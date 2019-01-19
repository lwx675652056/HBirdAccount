package com.hbird.ui.index;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

public class IndexFragmentData extends BaseObservable {

    private int yyyy = 12;// 当前年
    private int mm = 12;// 当前月
    private String spendingMoney = "0.00";// 支出的钱
    private String inComeMoney = "0.00";// 收入的钱
    private String budget = "0.00";// 剩余预算
    private boolean showMoney = true;// 是否显示金钱
    private boolean show = false;// 显示邀请按钮
    private boolean showArrow = false;// 显示下拉三角箭头
    private boolean showMember = false;// 显示账本内成员
    private boolean showMemberSeting = false;// 显示成员设置
    private String comparison = "";// 相比上月支出+4.5%

    private int size = 0;// 饼图数据条数
    private String selestStr = "";// 饼图选择的类别
    private int select = -1;// 选中的第几个饼图
    private String str1 = "--", str2 = "--", str3 = "--", str4 = "--", str5 = "--";// 饼图的5个类别
    private String cop1, cop2, cop3, cop4, cop5;// 饼图的5个比例
    private boolean noData;// 是否有数据
    private boolean showMore;// 有多少数据,超过3条才显示“查看全部”

    public IndexFragmentData() {
        setNoData(true);
        setShowMore(false);
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
    public int getYyyy() {
        return yyyy;
    }

    public void setYyyy(int yyyy) {
        this.yyyy = yyyy;
        notifyPropertyChanged(BR.yyyy);
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

    @Bindable
    public String getComparison() {
        return comparison;
    }

    public void setComparison(String comparison) {
        this.comparison = comparison;
        notifyPropertyChanged(BR.comparison);
    }

    @Bindable
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
        notifyPropertyChanged(BR.size);
    }

    @Bindable
    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
        notifyPropertyChanged(BR.select);
    }

    @Bindable
    public String getStr1() {
        return str1;
    }

    public void setStr1(String str1) {
        this.str1 = str1;
        notifyPropertyChanged(BR.str1);
    }

    @Bindable
    public String getStr2() {
        return str2;
    }

    public void setStr2(String str2) {
        this.str2 = str2;
        notifyPropertyChanged(BR.str2);
    }

    @Bindable
    public String getStr3() {
        return str3;
    }

    public void setStr3(String str3) {
        this.str3 = str3;
        notifyPropertyChanged(BR.str3);
    }

    @Bindable
    public String getStr4() {
        return str4;
    }

    public void setStr4(String str4) {
        this.str4 = str4;
        notifyPropertyChanged(BR.str4);
    }

    @Bindable
    public String getStr5() {
        return str5;
    }

    public void setStr5(String str5) {
        this.str5 = str5;
        notifyPropertyChanged(BR.str5);
    }

    @Bindable
    public String getCop1() {
        return cop1;
    }

    public void setCop1(String cop1) {
        this.cop1 = cop1;
        notifyPropertyChanged(BR.cop1);
    }

    @Bindable
    public String getCop2() {
        return cop2;
    }

    public void setCop2(String cop2) {
        this.cop2 = cop2;
        notifyPropertyChanged(BR.cop2);
    }

    @Bindable
    public String getCop3() {
        return cop3;
    }

    public void setCop3(String cop3) {
        this.cop3 = cop3;
        notifyPropertyChanged(BR.cop3);
    }

    @Bindable
    public String getCop4() {
        return cop4;
    }

    public void setCop4(String cop4) {
        this.cop4 = cop4;
        notifyPropertyChanged(BR.cop4);
    }

    @Bindable
    public String getCop5() {
        return cop5;
    }

    public void setCop5(String cop5) {
        this.cop5 = cop5;
        notifyPropertyChanged(BR.cop5);
    }

    @Bindable
    public String getSelestStr() {
        return selestStr;
    }

    public void setSelestStr(String selestStr) {
        this.selestStr = selestStr;
        notifyPropertyChanged(BR.selestStr);
    }

    @Bindable
    public boolean isNoData() {
        return noData;
    }

    public void setNoData(boolean noData) {
        this.noData = noData;
        notifyPropertyChanged(BR.noData);
    }

    @Bindable
    public boolean isShowMore() {
        return showMore;
    }

    public void setShowMore(boolean showMore) {
        this.showMore = showMore;
        notifyPropertyChanged(BR.showMore);
    }
}
