package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseReturn;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Liul on 2018/7/11.
 */

public class SaveMoneyReturn extends BaseReturn {

    private String msg;
    private String code;
    private List<ResultBean> result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        private Double monthSpend;
        private Double monthIncome;
        private String time;
        private Double fixedLargeExpenditure;
        private Double fixedLifeExpenditure;
        private Double budgetMoney;

        private String tags;//自己添加的 用来区分空集合时 自己手动添加的数据

        public Double getMonthSpend() {
            return monthSpend;
        }

        public void setMonthSpend(Double monthSpend) {
            this.monthSpend = monthSpend;
        }

        public Double getMonthIncome() {
            return monthIncome;
        }

        public void setMonthIncome(Double monthIncome) {
            this.monthIncome = monthIncome;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
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

        public Double getBudgetMoney() {
            return budgetMoney;
        }

        public void setBudgetMoney(Double budgetMoney) {
            this.budgetMoney = budgetMoney;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }
    }
}
