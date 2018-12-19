package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseReturn;

/**
 * Created by Liul on 2018/7/11.
 */

public class indexDatasReturn extends BaseReturn {

    private ResultBean result;
    private String code;
    private String msg;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class ResultBean {
        private CountBean count;
        private BudgetBean budget;

        public CountBean getCount() {
            return count;
        }

        public void setCount(CountBean count) {
            this.count = count;
        }

        public BudgetBean getBudget() {
            return budget;
        }

        public void setBudget(BudgetBean budget) {
            this.budget = budget;
        }

        public static class CountBean {
            private int chargeDays;
            private int monthDays;
            private String time;

            public int getChargeDays() {
                return chargeDays;
            }

            public void setChargeDays(int chargeDays) {
                this.chargeDays = chargeDays;
            }

            public int getMonthDays() {
                return monthDays;
            }

            public void setMonthDays(int monthDays) {
                this.monthDays = monthDays;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }

        public static class BudgetBean {
            private String time;
            private Double budgetMoney;
            private Double fixedLargeExpenditure;
            private Double fixedLifeExpenditure;
            private Long beginTime;
            private Long endTime;
            private String accountBookId;

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

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

            public Long getBeginTime() {
                return beginTime;
            }

            public void setBeginTime(Long beginTime) {
                this.beginTime = beginTime;
            }

            public Long getEndTime() {
                return endTime;
            }

            public void setEndTime(Long endTime) {
                this.endTime = endTime;
            }

            public String getAccountBookId() {
                return accountBookId;
            }

            public void setAccountBookId(String accountBookId) {
                this.accountBookId = accountBookId;
            }
        }
    }
}
