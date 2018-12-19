package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseReturn;

import java.util.List;

/**
 * Created by Liul on 2018/7/11.
 */

public class XiaoFeiBiLiReturn extends BaseReturn {

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
        private Double foodSpend;
        private String time;
        private Double monthSpend;

        public Double getFoodSpend() {
            return foodSpend;
        }

        public void setFoodSpend(Double foodSpend) {
            this.foodSpend = foodSpend;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public Double getMonthSpend() {
            return monthSpend;
        }

        public void setMonthSpend(Double monthSpend) {
            this.monthSpend = monthSpend;
        }
    }
}
