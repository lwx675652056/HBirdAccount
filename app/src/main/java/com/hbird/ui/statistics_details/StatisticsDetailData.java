package com.hbird.ui.statistics_details;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

/**
 * @author: LiangYX
 * @ClassName: StatisticsDetailData
 * @date: 2019/1/21 10:21
 * @Description: 数据 - 统计 - 详情
 */
public class StatisticsDetailData extends BaseObservable {

    private String date;// 时间
    private String type;// XX总计消费
    private String money;// 金额、笔数
    private boolean noData;// 没有数据

    public StatisticsDetailData() {
        setDate("--年--月--日");
        setMoney("￥00.00");
        setType("总计消费");
        setNoData(true);
    }

    @Bindable
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        notifyPropertyChanged(BR.date);
    }

    @Bindable
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        notifyPropertyChanged(BR.type);
    }

    @Bindable
    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
        notifyPropertyChanged(BR.money);
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
