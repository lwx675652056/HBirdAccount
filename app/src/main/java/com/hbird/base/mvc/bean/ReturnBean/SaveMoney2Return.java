package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseReturn;

import java.util.List;

/**
 * Created by Liul(245904552@qq.com) on 2018/11/26.
 */

public class SaveMoney2Return extends BaseReturn {

    private String code;
    private String msg;
    private ResultBean result;

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

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        private FixedSpendBean fixedSpend;
        private List<ArraysBean> arrays;

        public FixedSpendBean getFixedSpend() {
            return fixedSpend;
        }

        public void setFixedSpend(FixedSpendBean fixedSpend) {
            this.fixedSpend = fixedSpend;
        }

        public List<ArraysBean> getArrays() {
            return arrays;
        }

        public void setArrays(List<ArraysBean> arrays) {
            this.arrays = arrays;
        }

        public static class FixedSpendBean {
            private double fixedLifeExpenditure;
            private double fixedLargeExpenditure;

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
        }

        public static class ArraysBean {
            private double monthIncome;
            private String time;
            private double monthSpend;

            public double getMonthIncome() {
                return monthIncome;
            }

            public void setMonthIncome(double monthIncome) {
                this.monthIncome = monthIncome;
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
        }
    }
}
