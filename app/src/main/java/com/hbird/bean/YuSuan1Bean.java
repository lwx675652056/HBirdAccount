package com.hbird.bean;

import android.databinding.BaseObservable;

/**
 * @author: LiangYX
 * @ClassName: YuSuan1Bean
 * @date: 2019/2/19 17:07
 * @Description: 预算完成率 日常账本
 */
public class YuSuan1Bean extends BaseObservable {

    public double budgetMoney;
    public String time;
    public double monthSpend;
    public String tag;//自己区分 空数据 和数据为0的

    @Override
    public String toString() {
        return "{" +
                "budgetMoney:" + budgetMoney +
                ", time:'" + time + '\'' +
                ", monthSpend:" + monthSpend +
                '}';
    }
}
