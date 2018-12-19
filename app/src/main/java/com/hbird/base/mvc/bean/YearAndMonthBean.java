package com.hbird.base.mvc.bean;

/**
 * Created by Liul on 2018/7/17.
 */

public class YearAndMonthBean extends BaseBean {
    private String mDate;
    private double money;

    private String emptyTag;//区分是否为空的  （空和0 是两种情况）

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getEmptyTag() {
        return emptyTag;
    }

    public void setEmptyTag(String emptyTag) {
        this.emptyTag = emptyTag;
    }
}
