package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseReturn;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Liul on 2018/7/16.
 */

public class chartToRanking2Return extends BaseReturn {

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
        private double falseTotalMoney;
        private Double trueTotalMoney;
        private List<StatisticsIncomeTopArraysBean> statisticsIncomeTopArrays;

        public double getFalseTotalMoney() {
            return falseTotalMoney;
        }

        public void setFalseTotalMoney(double falseTotalMoney) {
            this.falseTotalMoney = falseTotalMoney;
        }

        public Double getTrueTotalMoney() {
            return trueTotalMoney;
        }

        public void setTrueTotalMoney(Double trueTotalMoney) {
            this.trueTotalMoney = trueTotalMoney;
        }

        public List<StatisticsIncomeTopArraysBean> getStatisticsIncomeTopArrays() {
            return statisticsIncomeTopArrays;
        }

        public void setStatisticsIncomeTopArrays(List<StatisticsIncomeTopArraysBean> statisticsIncomeTopArrays) {
            this.statisticsIncomeTopArrays = statisticsIncomeTopArrays;
        }

        public static class StatisticsIncomeTopArraysBean {
            private String icon;
            private double money;
            private String typeName;
            private boolean isFrist = true;

            public boolean isFrist() {
                return isFrist;
            }

            public void setFrist(boolean frist) {
                isFrist = frist;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public double getMoney() {
                return money;
            }

            public void setMoney(double money) {
                this.money = money;
            }

            public String getTypeName() {
                return typeName;
            }

            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }
        }
    }
}
