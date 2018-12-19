package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseReturn;

import java.util.List;

/**
 * Created by Liul on 2018/7/16.
 */

public class chartToBarReturn extends BaseReturn {

    public String code;
    public String msg;
    public ResultBean result;

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
        public double maxMoney;
        public List<ArraysBean> arrays;

        public double getMaxMoney() {
            return maxMoney;
        }

        public void setMaxMoney(double maxMoney) {
            this.maxMoney = maxMoney;
        }

        public List<ArraysBean> getArrays() {
            return arrays;
        }

        public void setArrays(List<ArraysBean> arrays) {
            this.arrays = arrays;
        }

        public static class ArraysBean {
            public double money;
            public long time;
            public String week;
            public String month;
            public String dayTime;

            public String getDayTime() {
                return dayTime;
            }

            public void setDayTime(String dayTime) {
                this.dayTime = dayTime;
            }

            public String getMonth() {
                return month;
            }

            public void setMonth(String month) {
                this.month = month;
            }

            public double getMoney() {
                return money;
            }

            public void setMoney(double money) {
                this.money = money;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public String getWeek() {
                return week;
            }

            public void setWeek(String week) {
                this.week = week;
            }
        }
    }
}
