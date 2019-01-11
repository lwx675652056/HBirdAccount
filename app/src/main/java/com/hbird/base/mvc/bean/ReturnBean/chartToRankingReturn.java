package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.bean.StatisticsSpendTopArraysBean;

import java.util.List;

/**
 * Created by Liul on 2018/7/16.
 */

public class chartToRankingReturn extends BaseReturn {

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
        public double falseTotalMoney;
        public int totalCount;
        public double trueTotalMoney;
        public List<StatisticsSpendHappinessArraysBean> statisticsSpendHappinessArrays;
        public List<StatisticsSpendTopArraysBean> statisticsSpendTopArrays;

        public double getFalseTotalMoney() {
            return falseTotalMoney;
        }

        public void setFalseTotalMoney(double falseTotalMoney) {
            this.falseTotalMoney = falseTotalMoney;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public double getTrueTotalMoney() {
            return trueTotalMoney;
        }

        public void setTrueTotalMoney(double trueTotalMoney) {
            this.trueTotalMoney = trueTotalMoney;
        }

        public List<StatisticsSpendHappinessArraysBean> getStatisticsSpendHappinessArrays() {
            return statisticsSpendHappinessArrays;
        }

        public void setStatisticsSpendHappinessArrays(List<StatisticsSpendHappinessArraysBean> statisticsSpendHappinessArrays) {
            this.statisticsSpendHappinessArrays = statisticsSpendHappinessArrays;
        }

        public List<StatisticsSpendTopArraysBean> getStatisticsSpendTopArrays() {
            return statisticsSpendTopArrays;
        }

        public void setStatisticsSpendTopArrays(List<StatisticsSpendTopArraysBean> statisticsSpendTopArrays) {
            this.statisticsSpendTopArrays = statisticsSpendTopArrays;
        }

        public static class StatisticsSpendHappinessArraysBean {
            public int count;
            public Integer spendHappiness;
            public boolean isFrist = true;

            public boolean isFrist() {
                return isFrist;
            }

            public void setFrist(boolean frist) {
                isFrist = frist;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public Integer getSpendHappiness() {
                return spendHappiness;
            }

            public void setSpendHappiness(Integer spendHappiness) {
                this.spendHappiness = spendHappiness;
            }
        }
    }
}
