package com.hbird.base.mvc.bean;

import com.umeng.commonsdk.debug.D;

import java.io.Serializable;

/**
 * Created by Liul on 2018/7/17.
 */
public class ChartDateBean implements Serializable {

    private String time;//日期 时间
    private Double money;//金钱

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }
}
