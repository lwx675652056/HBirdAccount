package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseReturn;

import java.util.List;

/**
 * Created by Liul(245904552@qq.com) on 2018/11/27.
 */

public class YuSuanZbFinishReturn extends BaseReturn {

    private String msg;
    private String code;
//    private List<ResultBean> result;
    private ResultsBean result;

    public ResultsBean getResults() {
        return result;
    }

    public void setResults(ResultsBean results) {
        this.result = results;
    }

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

//    public List<ResultBean> getResult() {
//        return result;
//    }

//    public void setResult(List<ResultBean> result) {
//        this.result = result;
//    }

    public static class ResultBean {
        private int budgetMoney;
        private String time;
        private int monthSpend;

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

        public int getMonthSpend() {
            return monthSpend;
        }

        public void setMonthSpend(int monthSpend) {
            this.monthSpend = monthSpend;
        }
    }
    public static class ResultsBean {
        private SceneBudgetBean sceneBudget;
        private List<ArraysBean> arrays;

        public SceneBudgetBean getSceneBudget() {
            return sceneBudget;
        }

        public void setSceneBudget(SceneBudgetBean sceneBudget) {
            this.sceneBudget = sceneBudget;
        }

        public List<ArraysBean> getArrays() {
            return arrays;
        }

        public void setArrays(List<ArraysBean> arrays) {
            this.arrays = arrays;
        }

        public static class SceneBudgetBean {
            private int id;
            private long endTime;
            private int accountBookId;
            private long beginTime;
            private int sceneType;
            private int budgetMoney;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public long getEndTime() {
                return endTime;
            }

            public void setEndTime(long endTime) {
                this.endTime = endTime;
            }

            public int getAccountBookId() {
                return accountBookId;
            }

            public void setAccountBookId(int accountBookId) {
                this.accountBookId = accountBookId;
            }

            public long getBeginTime() {
                return beginTime;
            }

            public void setBeginTime(long beginTime) {
                this.beginTime = beginTime;
            }

            public int getSceneType() {
                return sceneType;
            }

            public void setSceneType(int sceneType) {
                this.sceneType = sceneType;
            }

            public int getBudgetMoney() {
                return budgetMoney;
            }

            public void setBudgetMoney(int budgetMoney) {
                this.budgetMoney = budgetMoney;
            }
        }

        public static class ArraysBean {
            private String time;
            private int money;
            private Object weekTime;

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public int getMoney() {
                return money;
            }

            public void setMoney(int money) {
                this.money = money;
            }

            public Object getWeekTime() {
                return weekTime;
            }

            public void setWeekTime(Object weekTime) {
                this.weekTime = weekTime;
            }
        }
    }

}
