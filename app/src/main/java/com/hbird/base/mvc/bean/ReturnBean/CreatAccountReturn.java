package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseReturn;

import java.util.List;

/**
 * Created by Liul(245904552@qq.com) on 2018/11/26.
 */

public class CreatAccountReturn extends BaseReturn {

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
        private List<IncomeBean> income;
        private List<SpendBean> spend;

        public List<IncomeBean> getIncome() {
            return income;
        }

        public void setIncome(List<IncomeBean> income) {
            this.income = income;
        }

        public List<SpendBean> getSpend() {
            return spend;
        }

        public void setSpend(List<SpendBean> spend) {
            this.spend = spend;
        }

        public static class IncomeBean {
            private int id;
            private int priority;
            private String icon;
            private String incomeName;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getPriority() {
                return priority;
            }

            public void setPriority(int priority) {
                this.priority = priority;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getIncomeName() {
                return incomeName;
            }

            public void setIncomeName(String incomeName) {
                this.incomeName = incomeName;
            }
        }

        public static class SpendBean {
            private int id;
            private int priority;
            private String icon;
            private String spendName;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getPriority() {
                return priority;
            }

            public void setPriority(int priority) {
                this.priority = priority;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getSpendName() {
                return spendName;
            }

            public void setSpendName(String spendName) {
                this.spendName = spendName;
            }
        }
    }
}
