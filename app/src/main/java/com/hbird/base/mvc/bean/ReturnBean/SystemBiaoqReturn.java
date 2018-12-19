package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseReturn;

import java.util.List;

/**
 * Created by Liul(245904552@qq.com) on 2018/11/21.
 */

public class SystemBiaoqReturn extends BaseReturn {

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
        private String labelVersion;
        private Long synInterval;
        private List<AbsBean> abs;
        private List<LabelBean> label;

        public Long getSynInterval() {
            return synInterval;
        }

        public void setSynInterval(Long synInterval) {
            this.synInterval = synInterval;
        }

        public String getLabelVersion() {
            return labelVersion;
        }

        public void setLabelVersion(String labelVersion) {
            this.labelVersion = labelVersion;
        }

        public List<AbsBean> getAbs() {
            return abs;
        }

        public void setAbs(List<AbsBean> abs) {
            this.abs = abs;
        }

        public List<LabelBean> getLabel() {
            return label;
        }

        public void setLabel(List<LabelBean> label) {
            this.label = label;
        }

        public static class AbsBean {
            private Integer id;
            private Object icon;
            private Object member;
            private Object updateDate;
            private Object abTypeName;
            private Object abName;
            private Integer defaultFlag;
            private Integer typeBudget;
            private Integer userType;
            private Integer abTypeId;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public Object getIcon() {
                return icon;
            }

            public void setIcon(Object icon) {
                this.icon = icon;
            }

            public Object getMember() {
                return member;
            }

            public void setMember(Object member) {
                this.member = member;
            }

            public Object getUpdateDate() {
                return updateDate;
            }

            public void setUpdateDate(Object updateDate) {
                this.updateDate = updateDate;
            }

            public Object getAbTypeName() {
                return abTypeName;
            }

            public void setAbTypeName(Object abTypeName) {
                this.abTypeName = abTypeName;
            }

            public Object getAbName() {
                return abName;
            }

            public void setAbName(Object abName) {
                this.abName = abName;
            }

            public Integer getDefaultFlag() {
                return defaultFlag;
            }

            public void setDefaultFlag(Integer defaultFlag) {
                this.defaultFlag = defaultFlag;
            }

            public Integer getTypeBudget() {
                return typeBudget;
            }

            public void setTypeBudget(Integer typeBudget) {
                this.typeBudget = typeBudget;
            }

            public Integer getUserType() {
                return userType;
            }

            public void setUserType(Integer userType) {
                this.userType = userType;
            }

            public Integer getAbTypeId() {
                return abTypeId;
            }

            public void setAbTypeId(Integer abTypeId) {
                this.abTypeId = abTypeId;
            }
        }

        public static class LabelBean {
            private Integer userInfoId;
            private Integer abTypeId;
            private List<IncomeBean> income;
            private List<SpendBean> spend;

            public Integer getUserInfoId() {
                return userInfoId;
            }

            public void setUserInfoId(Integer userInfoId) {
                this.userInfoId = userInfoId;
            }

            public Integer getAbTypeId() {
                return abTypeId;
            }

            public void setAbTypeId(Integer abTypeId) {
                this.abTypeId = abTypeId;
            }

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
                private String incomeName;
                private String icon;
                private Integer id;
                private Integer priority;

                public String getIncomeName() {
                    return incomeName;
                }

                public void setIncomeName(String incomeName) {
                    this.incomeName = incomeName;
                }

                public String getIcon() {
                    return icon;
                }

                public void setIcon(String icon) {
                    this.icon = icon;
                }

                public Integer getId() {
                    return id;
                }

                public void setId(Integer id) {
                    this.id = id;
                }

                public Integer getPriority() {
                    return priority;
                }

                public void setPriority(Integer priority) {
                    this.priority = priority;
                }

                @Override
                public String toString() {
                    return "{" +
                            "incomeName:'" + incomeName + '\'' +
                            ", icon:'" + icon + '\'' +
                            ", id:" + id +
                            ", priority:" + priority +
                            '}';
                }
            }

            public static class SpendBean {
                private String icon;
                private Integer id;
                private String spendName;
                private Integer priority;

                public String getIcon() {
                    return icon;
                }

                public void setIcon(String icon) {
                    this.icon = icon;
                }

                public Integer getId() {
                    return id;
                }

                public void setId(Integer id) {
                    this.id = id;
                }

                public String getSpendName() {
                    return spendName;
                }

                public void setSpendName(String spendName) {
                    this.spendName = spendName;
                }

                public Integer getPriority() {
                    return priority;
                }

                public void setPriority(Integer priority) {
                    this.priority = priority;
                }

                @Override
                public String toString() {
                    return "{" +
                            "icon:'" + icon + '\'' +
                            ", id:" + id +
                            ", spendName:'" + spendName + '\'' +
                            ", priority:" + priority +
                            '}';
                }
            }

            @Override
            public String toString() {
                return "{" +
                        "userInfoId:" + userInfoId +
                        ", abTypeId:" + abTypeId +
                        ", income:" + income +
                        ", spend:" + spend +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "{" +
                    "labelVersion:'" + labelVersion + '\'' +
                    ", synInterval:" + synInterval +
                    ", abs:" + abs +
                    ", label:" + label +
                    '}';
        }
    }
}
