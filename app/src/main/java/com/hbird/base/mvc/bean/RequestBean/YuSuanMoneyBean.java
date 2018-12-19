package com.hbird.base.mvc.bean.RequestBean;

import com.hbird.base.mvc.bean.BaseBean;

import java.math.BigDecimal;

/**
 * Created by Liul(245904552@qq.com) on 2018/11/14.
 */

public class YuSuanMoneyBean extends BaseBean {
    private BigDecimal budgetMoney;     //预算金额
    private BigDecimal fixedLargeExpenditure;   //固定大额支出
    private BigDecimal fixedLifeExpenditure;    //固定生活支出
    private String time;    //设置预算/固定支出日期 例2018-07 不传默认当年当月
    private String beginTime;   //场景账本 预算开始时间 时间戳
    private String endTime;     //场景账本 预算结束时间 时间戳
    private String accountBookId;   //账本id 必传

    public BigDecimal getBudgetMoney() {
        return budgetMoney;
    }

    public void setBudgetMoney(BigDecimal budgetMoney) {
        this.budgetMoney = budgetMoney;
    }

    public BigDecimal getFixedLargeExpenditure() {
        return fixedLargeExpenditure;
    }

    public void setFixedLargeExpenditure(BigDecimal fixedLargeExpenditure) {
        this.fixedLargeExpenditure = fixedLargeExpenditure;
    }

    public BigDecimal getFixedLifeExpenditure() {
        return fixedLifeExpenditure;
    }

    public void setFixedLifeExpenditure(BigDecimal fixedLifeExpenditure) {
        this.fixedLifeExpenditure = fixedLifeExpenditure;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAccountBookId() {
        return accountBookId;
    }

    public void setAccountBookId(String accountBookId) {
        this.accountBookId = accountBookId;
    }
}
