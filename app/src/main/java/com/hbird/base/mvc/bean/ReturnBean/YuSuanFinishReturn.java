package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseReturn;

import java.util.List;

/**
 * Created by Liul on 2018/7/11.
 */

public class YuSuanFinishReturn extends BaseReturn {

    private String code;
    private String msg;
    private List<ResultBean> result;

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

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        private int budgetMoney;
        private String time;
        private double monthSpend;
        private String tag;//自己区分 空数据 和数据为0的

        public int getBudgetMoney() {
            return budgetMoney;
        }

        public void setBudgetMoney(int budgetMoney) {
            this.budgetMoney = budgetMoney;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public double getMonthSpend() {
            return monthSpend;
        }

        public void setMonthSpend(double monthSpend) {
            this.monthSpend = monthSpend;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }
}
