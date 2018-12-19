package com.hbird.base.mvc.bean.RequestBean;

import com.hbird.base.mvc.bean.BaseBean;

/**
 * Created by Liul on 2018/8/18.
 */

public class FenXiMoneys extends BaseBean{
    private Double budgetMoney;
    private Double fixedLargeExpenditure;
    private Double fixedLifeExpenditure;
    private String time;

    public Double getBudgetMoney() {
        return budgetMoney;
    }

    public void setBudgetMoney(Double budgetMoney) {
        this.budgetMoney = budgetMoney;
    }

    public Double getFixedLargeExpenditure() {
        return fixedLargeExpenditure;
    }

    public void setFixedLargeExpenditure(Double fixedLargeExpenditure) {
        this.fixedLargeExpenditure = fixedLargeExpenditure;
    }

    public Double getFixedLifeExpenditure() {
        return fixedLifeExpenditure;
    }

    public void setFixedLifeExpenditure(Double fixedLifeExpenditure) {
        this.fixedLifeExpenditure = fixedLifeExpenditure;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
