package com.hbird.ui.statistics;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

/**
 * @author: LiangYX
 * @ClassName: FragMeData
 * @date: 2019/1/17 14:13
 * @Description: 数据 - 统计
 */
public class StatisticsData extends BaseObservable {

    private boolean isInCome;// 是否为收入
    private int dateType;// 1 2 3 , 日 周 年
    private boolean isAll;// 是否为全部数据
    private boolean noDataRanking;// 收入支出排行榜是否有数据
    private boolean isOpen;// 是否展开
    private boolean isShowOpen;// 是否显示展开
    private String cop1;// 花的多 比例
    private String cop2;// 花的少 比例
    private String cop3;// 差不多 比例

    private int select;// 消费心情选择了哪个 花的多 花的少 差不多 ， 1 2 3
    private String selestStr = "";// 饼图选择的类别
    private String count = "";// 选择的饼图 几笔
    private int icon;// 选择的饼图 心情
    private boolean show;// 是否选择了饼图

    public StatisticsData() {
        this.isInCome = false;
        this.dateType = 1;
        this.isAll = false;
        this.noDataRanking = true;
        this.isOpen = false;
        this.isShowOpen = false;
        select = -1;
        this.cop1 = "--";// 花的多 比例
        this.cop2 = "--";// 花的少 比例
        this.cop3 = "--";// 差不多 比例
    }

    @Bindable
    public boolean isInCome() {
        return isInCome;
    }

    public void setInCome(boolean inCome) {
        isInCome = inCome;
        notifyPropertyChanged(BR.inCome);
    }

    @Bindable
    public int getDateType() {
        return dateType;
    }

    public void setDateType(int dateType) {
        this.dateType = dateType;
        notifyPropertyChanged(BR.dateType);
    }

    @Bindable
    public boolean isAll() {
        return isAll;
    }

    public void setAll(boolean all) {
        isAll = all;
        notifyPropertyChanged(BR.all);
    }

    @Bindable
    public boolean isNoDataRanking() {
        return noDataRanking;
    }

    public void setNoDataRanking(boolean noDataRanking) {
        this.noDataRanking = noDataRanking;
        notifyPropertyChanged(BR.noDataRanking);
    }

    @Bindable
    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
        notifyPropertyChanged(BR.open);
    }

    @Bindable
    public boolean isShowOpen() {
        return isShowOpen;
    }

    public void setShowOpen(boolean showOpen) {
        isShowOpen = showOpen;
        notifyPropertyChanged(BR.showOpen);
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
    public String getSelestStr() {
        return selestStr;
    }

    public void setSelestStr(String selestStr) {
        this.selestStr = selestStr;
        notifyPropertyChanged(BR.selestStr);
    }

    @Bindable
    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
        notifyPropertyChanged(BR.count);
    }

    @Bindable
    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
        notifyPropertyChanged(BR.icon);
    }

    @Bindable
    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
        notifyPropertyChanged(BR.show);
    }
}
