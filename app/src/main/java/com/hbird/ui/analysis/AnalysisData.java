package com.hbird.ui.analysis;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;
import com.hbird.base.util.DateUtils;

import java.util.Date;

/**
 * @author: LiangYX
 * @ClassName: CalendarData
 * @date: 2018/12/26 15:38
 * @Description: 日历
 */
public class AnalysisData extends BaseObservable {

    private int yyyy = 2018;// 年
    private int mm = 8;// 月
    private boolean showEdit;// 显示编辑按钮，此时不显示蓝色的设置按钮
    private boolean isTotalAccount;// 是否为总账本，预算完成率使用
    private boolean isRatioAll;// 消费结构比是全部数据还是个人的
    private boolean isSaveEfficientsAll;// 存钱效率是全部数据还是个人的

    private boolean showRatio;// 显示消费结构比，false显示无数据的“记一笔”
    private boolean showSetBudget;// 显示“设置预算”

    public AnalysisData() {
        setYyyy(Integer.parseInt(DateUtils.getCurYear("yyyy")));

        String m = DateUtils.date2Str(new Date(), "MM");
        String currentMonth = m.substring(0, 2);
        setMm(Integer.parseInt(currentMonth));
        setShowEdit(false);// 存钱效率默认显示空白
        setTotalAccount(true);// 默认不显示预算完成率
        setRatioAll(false);// 默认个人数据
        setSaveEfficientsAll(false);// 默认是个人数据
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
    public int getMm() {
        return mm;
    }

    public void setMm(int mm) {
        this.mm = mm;
        notifyPropertyChanged(BR.mm);
    }

    @Bindable
    public boolean isShowEdit() {
        return showEdit;
    }

    public void setShowEdit(boolean showEdit) {
        this.showEdit = showEdit;
        notifyPropertyChanged(BR.showEdit);
    }

    @Bindable
    public boolean isTotalAccount() {
        return isTotalAccount;
    }

    public void setTotalAccount(boolean totalAccount) {
        isTotalAccount = totalAccount;
        notifyPropertyChanged(BR.totalAccount);
    }

    @Bindable
    public boolean isRatioAll() {
        return isRatioAll;
    }

    public void setRatioAll(boolean ratioAll) {
        isRatioAll = ratioAll;
        notifyPropertyChanged(BR.ratioAll);
    }

    @Bindable
    public boolean isSaveEfficientsAll() {
        return isSaveEfficientsAll;
    }

    public void setSaveEfficientsAll(boolean saveEfficientsAll) {
        isSaveEfficientsAll = saveEfficientsAll;
        notifyPropertyChanged(BR.saveEfficientsAll);
    }

    @Bindable
    public boolean isShowRatio() {
        return showRatio;
    }

    public void setShowRatio(boolean showRatio) {
        this.showRatio = showRatio;
        notifyPropertyChanged(BR.showRatio);
    }

    @Bindable
    public boolean isShowSetBudget() {
        return showSetBudget;
    }

    public void setShowSetBudget(boolean showSetBudget) {
        this.showSetBudget = showSetBudget;
        notifyPropertyChanged(BR.showSetBudget);
    }
}
