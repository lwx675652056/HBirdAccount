package com.hbird.base.mvc.bean;

import java.util.List;

/**
 * Created by Liul on 2018/6/29.
 */

public class dayListBean extends BaseReturn {


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
        private double monthIncome;
        private double monthSpend;
        private List<ArraysBean> arrays;

        public double getMonthIncome() {
            return monthIncome;
        }

        public void setMonthIncome(double monthIncome) {
            this.monthIncome = monthIncome;
        }

        public double getMonthSpend() {
            return monthSpend;
        }

        public void setMonthSpend(double monthSpend) {
            this.monthSpend = monthSpend;
        }

        public List<ArraysBean> getArrays() {
            return arrays;
        }

        public void setArrays(List<ArraysBean> arrays) {
            this.arrays = arrays;
        }

        public static class ArraysBean {
            private double daySpend;
            private double dayIncome;
            private long dayTime;
            private List<DayArraysBean> dayArrays;

            public double getDaySpend() {
                return daySpend;
            }

            public void setDaySpend(double daySpend) {
                this.daySpend = daySpend;
            }

            public double getDayIncome() {
                return dayIncome;
            }

            public void setDayIncome(int dayIncome) {
                this.dayIncome = dayIncome;
            }

            public long getDayTime() {
                return dayTime;
            }

            public void setDayTime(long dayTime) {
                this.dayTime = dayTime;
            }

            public List<DayArraysBean> getDayArrays() {
                return dayArrays;
            }

            public void setDayArrays(List<DayArraysBean> dayArrays) {
                this.dayArrays = dayArrays;
            }

            public static class DayArraysBean {
                private int orderType;
                private String icon;
                private String typeName;
                private int isStaged;
                private String remark;
                private Integer spendHappiness;
                private double money;
                private String typePid;
                private String typeId;
                private String id;
                private int accountBookId;
                private long chargeDate;
                private long createDate;
                private long updateDate;
                private String typePname;
                //记录者头像
                public String reporterAvatar;
                //记录者昵称
                public String reporterNickName;

                public String getReporterAvatar() {
                    return reporterAvatar;
                }

                public void setReporterAvatar(String reporterAvatar) {
                    this.reporterAvatar = reporterAvatar;
                }

                public String getReporterNickName() {
                    return reporterNickName;
                }

                public void setReporterNickName(String reporterNickName) {
                    this.reporterNickName = reporterNickName;
                }

                public long getUpdateDate() {
                    return updateDate;
                }

                public void setUpdateDate(long updateDate) {
                    this.updateDate = updateDate;
                }

                public int getOrderType() {
                    return orderType;
                }

                public void setOrderType(int orderType) {
                    this.orderType = orderType;
                }

                public String getIcon() {
                    return icon;
                }

                public void setIcon(String icon) {
                    this.icon = icon;
                }

                public String getTypeName() {
                    return typeName;
                }

                public void setTypeName(String typeName) {
                    this.typeName = typeName;
                }

                public int getIsStaged() {
                    return isStaged;
                }

                public void setIsStaged(int isStaged) {
                    this.isStaged = isStaged;
                }

                public String getRemark() {
                    return remark;
                }

                public void setRemark(String remark) {
                    this.remark = remark;
                }

                public Integer getSpendHappiness() {
                    return spendHappiness;
                }

                public void setSpendHappiness(Integer spendHappiness) {
                    this.spendHappiness = spendHappiness;
                }

                public double getMoney() {
                    return money;
                }

                public void setMoney(double money) {
                    this.money = money;
                }

                public String getTypePid() {
                    return typePid;
                }

                public void setTypePid(String typePid) {
                    this.typePid = typePid;
                }

                public String getTypeId() {
                    return typeId;
                }

                public void setTypeId(String typeId) {
                    this.typeId = typeId;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public int getAccountBookId() {
                    return accountBookId;
                }

                public void setAccountBookId(int accountBookId) {
                    this.accountBookId = accountBookId;
                }

                public long getChargeDate() {
                    return chargeDate;
                }

                public void setChargeDate(long chargeDate) {
                    this.chargeDate = chargeDate;
                }

                public long getCreateDate() {
                    return createDate;
                }

                public void setCreateDate(long createDate) {
                    this.createDate = createDate;
                }

                public String getTypePname() {
                    return typePname;
                }

                public void setTypePname(String typePname) {
                    this.typePname = typePname;
                }
            }
        }
    }
}
