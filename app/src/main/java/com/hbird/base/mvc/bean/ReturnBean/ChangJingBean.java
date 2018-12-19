package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseReturn;

/**
 * Created by Liul(245904552@qq.com) on 2018/11/13.
 */

public class ChangJingBean extends BaseReturn {

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
        private String time;
        private double budgetMoney;
        private double fixedLifeExpenditure;
        private double fixedLargeExpenditure;
        private Integer id;
        private Long beginTime;
        private Long endTime;
        private Integer accountBookId;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public double getBudgetMoney() {
            return budgetMoney;
        }

        public void setBudgetMoney(double budgetMoney) {
            this.budgetMoney = budgetMoney;
        }

        public double getFixedLifeExpenditure() {
            return fixedLifeExpenditure;
        }

        public void setFixedLifeExpenditure(double fixedLifeExpenditure) {
            this.fixedLifeExpenditure = fixedLifeExpenditure;
        }

        public double getFixedLargeExpenditure() {
            return fixedLargeExpenditure;
        }

        public void setFixedLargeExpenditure(double fixedLargeExpenditure) {
            this.fixedLargeExpenditure = fixedLargeExpenditure;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
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

        public Integer getAccountBookId() {
            return accountBookId;
        }

        public void setAccountBookId(Integer accountBookId) {
            this.accountBookId = accountBookId;
        }
    }
}
