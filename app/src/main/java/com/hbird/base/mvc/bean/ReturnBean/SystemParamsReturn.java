package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseReturn;

import java.util.List;

/**
 * Created by Liul on 2018/9/5.
 */

public class SystemParamsReturn extends BaseReturn {

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
        private AllUserCommUseSpendTypeBean allUserCommUseSpendType;
        private AllSysIncomeTypeBean allSysIncomeType;
        private AllUserCommUseIncomeTypeBean allUserCommUseIncomeType;
        private String synInterval;
        private AllSysSpendTypeBean allSysSpendType;
        private String accountBookId;
        private String userInfoId;

        public AllUserCommUseSpendTypeBean getAllUserCommUseSpendType() {
            return allUserCommUseSpendType;
        }

        public void setAllUserCommUseSpendType(AllUserCommUseSpendTypeBean allUserCommUseSpendType) {
            this.allUserCommUseSpendType = allUserCommUseSpendType;
        }

        public AllSysIncomeTypeBean getAllSysIncomeType() {
            return allSysIncomeType;
        }

        public void setAllSysIncomeType(AllSysIncomeTypeBean allSysIncomeType) {
            this.allSysIncomeType = allSysIncomeType;
        }

        public AllUserCommUseIncomeTypeBean getAllUserCommUseIncomeType() {
            return allUserCommUseIncomeType;
        }

        public void setAllUserCommUseIncomeType(AllUserCommUseIncomeTypeBean allUserCommUseIncomeType) {
            this.allUserCommUseIncomeType = allUserCommUseIncomeType;
        }

        public String getSynInterval() {
            return synInterval;
        }

        public void setSynInterval(String synInterval) {
            this.synInterval = synInterval;
        }

        public AllSysSpendTypeBean getAllSysSpendType() {
            return allSysSpendType;
        }

        public void setAllSysSpendType(AllSysSpendTypeBean allSysSpendType) {
            this.allSysSpendType = allSysSpendType;
        }

        public String getAccountBookId() {
            return accountBookId;
        }

        public void setAccountBookId(String accountBookId) {
            this.accountBookId = accountBookId;
        }

        public String getUserInfoId() {
            return userInfoId;
        }

        public void setUserInfoId(String userInfoId) {
            this.userInfoId = userInfoId;
        }

        public static class AllUserCommUseSpendTypeBean {
            private String version;
            private List<AllUserCommUseSpendTypeArraysBean> allUserCommUseSpendTypeArrays;

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            public List<AllUserCommUseSpendTypeArraysBean> getAllUserCommUseSpendTypeArrays() {
                return allUserCommUseSpendTypeArrays;
            }

            public void setAllUserCommUseSpendTypeArrays(List<AllUserCommUseSpendTypeArraysBean> allUserCommUseSpendTypeArrays) {
                this.allUserCommUseSpendTypeArrays = allUserCommUseSpendTypeArrays;
            }

            public static class AllUserCommUseSpendTypeArraysBean {
                private String id;
                private String spendName;
                private String parentId;
                private String parentName;
                private String icon;
                private int priority;
                private int mark;
                private Object spendTypeSons;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getSpendName() {
                    return spendName;
                }

                public void setSpendName(String spendName) {
                    this.spendName = spendName;
                }

                public String getParentId() {
                    return parentId;
                }

                public void setParentId(String parentId) {
                    this.parentId = parentId;
                }

                public String getParentName() {
                    return parentName;
                }

                public void setParentName(String parentName) {
                    this.parentName = parentName;
                }

                public String getIcon() {
                    return icon;
                }

                public void setIcon(String icon) {
                    this.icon = icon;
                }

                public int getPriority() {
                    return priority;
                }

                public void setPriority(int priority) {
                    this.priority = priority;
                }

                public int getMark() {
                    return mark;
                }

                public void setMark(int mark) {
                    this.mark = mark;
                }

                public Object getSpendTypeSons() {
                    return spendTypeSons;
                }

                public void setSpendTypeSons(Object spendTypeSons) {
                    this.spendTypeSons = spendTypeSons;
                }
            }
        }

        public static class AllSysIncomeTypeBean {
            private String version;
            private List<AllSysIncomeTypeArraysBean> allSysIncomeTypeArrays;

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            public List<AllSysIncomeTypeArraysBean> getAllSysIncomeTypeArrays() {
                return allSysIncomeTypeArrays;
            }

            public void setAllSysIncomeTypeArrays(List<AllSysIncomeTypeArraysBean> allSysIncomeTypeArrays) {
                this.allSysIncomeTypeArrays = allSysIncomeTypeArrays;
            }

            public static class AllSysIncomeTypeArraysBean {
                private long updateDate;
                private long createDate;
                private String parentId;
                private String incomeName;
                private Object incomeTypeSons;
                private Integer delflag;
                private long delDate;
                private Integer mark;
                private String icon;
                private String status;
                private int priority;
                private String id;

                public long getUpdateDate() {
                    return updateDate;
                }

                public void setUpdateDate(long updateDate) {
                    this.updateDate = updateDate;
                }

                public long getCreateDate() {
                    return createDate;
                }

                public void setCreateDate(long createDate) {
                    this.createDate = createDate;
                }

                public String getParentId() {
                    return parentId;
                }

                public void setParentId(String parentId) {
                    this.parentId = parentId;
                }

                public String getIncomeName() {
                    return incomeName;
                }

                public void setIncomeName(String incomeName) {
                    this.incomeName = incomeName;
                }

                public Object getIncomeTypeSons() {
                    return incomeTypeSons;
                }

                public void setIncomeTypeSons(Object incomeTypeSons) {
                    this.incomeTypeSons = incomeTypeSons;
                }

                public Integer getDelflag() {
                    return delflag;
                }

                public void setDelflag(Integer delflag) {
                    this.delflag = delflag;
                }

                public long getDelDate() {
                    return delDate;
                }

                public void setDelDate(long delDate) {
                    this.delDate = delDate;
                }

                public Integer getMark() {
                    return mark;
                }

                public void setMark(Integer mark) {
                    this.mark = mark;
                }

                public String getIcon() {
                    return icon;
                }

                public void setIcon(String icon) {
                    this.icon = icon;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public int getPriority() {
                    return priority;
                }

                public void setPriority(int priority) {
                    this.priority = priority;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }
            }
        }

        public static class AllUserCommUseIncomeTypeBean {
            private String version;
            private List<AllUserCommUseIncomeTypeArraysBean> allUserCommUseIncomeTypeArrays;

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            public List<AllUserCommUseIncomeTypeArraysBean> getAllUserCommUseIncomeTypeArrays() {
                return allUserCommUseIncomeTypeArrays;
            }

            public void setAllUserCommUseIncomeTypeArrays(List<AllUserCommUseIncomeTypeArraysBean> allUserCommUseIncomeTypeArrays) {
                this.allUserCommUseIncomeTypeArrays = allUserCommUseIncomeTypeArrays;
            }

            public static class AllUserCommUseIncomeTypeArraysBean {
                private String id;
                private String incomeName;
                private String parentId;
                private String parentName;
                private String icon;
                private int priority;
                private int mark;
                private Object incomeTypeSons;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getIncomeName() {
                    return incomeName;
                }

                public void setIncomeName(String incomeName) {
                    this.incomeName = incomeName;
                }

                public String getParentId() {
                    return parentId;
                }

                public void setParentId(String parentId) {
                    this.parentId = parentId;
                }

                public String getParentName() {
                    return parentName;
                }

                public void setParentName(String parentName) {
                    this.parentName = parentName;
                }

                public String getIcon() {
                    return icon;
                }

                public void setIcon(String icon) {
                    this.icon = icon;
                }

                public int getPriority() {
                    return priority;
                }

                public void setPriority(int priority) {
                    this.priority = priority;
                }

                public int getMark() {
                    return mark;
                }

                public void setMark(int mark) {
                    this.mark = mark;
                }

                public Object getIncomeTypeSons() {
                    return incomeTypeSons;
                }

                public void setIncomeTypeSons(Object incomeTypeSons) {
                    this.incomeTypeSons = incomeTypeSons;
                }
            }
        }

        public static class AllSysSpendTypeBean {
            private String version;
            private List<AllSysSpendTypeArraysBean> allSysSpendTypeArrays;

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            public List<AllSysSpendTypeArraysBean> getAllSysSpendTypeArrays() {
                return allSysSpendTypeArrays;
            }

            public void setAllSysSpendTypeArrays(List<AllSysSpendTypeArraysBean> allSysSpendTypeArrays) {
                this.allSysSpendTypeArrays = allSysSpendTypeArrays;
            }

            public static class AllSysSpendTypeArraysBean {
                private long updateDate;
                private long createDate;
                private String parentId;
                private String spendName;
                private Object spendTypeSons;
                private Integer delflag;
                private long delDate;
                private Integer mark;
                private String  icon;
                private String status;
                private int priority;
                private String id;

                public long getUpdateDate() {
                    return updateDate;
                }

                public void setUpdateDate(long updateDate) {
                    this.updateDate = updateDate;
                }

                public long getCreateDate() {
                    return createDate;
                }

                public void setCreateDate(long createDate) {
                    this.createDate = createDate;
                }

                public String getParentId() {
                    return parentId;
                }

                public void setParentId(String parentId) {
                    this.parentId = parentId;
                }

                public String getSpendName() {
                    return spendName;
                }

                public void setSpendName(String spendName) {
                    this.spendName = spendName;
                }

                public Object getSpendTypeSons() {
                    return spendTypeSons;
                }

                public void setSpendTypeSons(Object spendTypeSons) {
                    this.spendTypeSons = spendTypeSons;
                }

                public Integer getDelflag() {
                    return delflag;
                }

                public void setDelflag(Integer delflag) {
                    this.delflag = delflag;
                }

                public long getDelDate() {
                    return delDate;
                }

                public void setDelDate(long delDate) {
                    this.delDate = delDate;
                }

                public Integer getMark() {
                    return mark;
                }

                public void setMark(Integer mark) {
                    this.mark = mark;
                }

                public String getIcon() {
                    return icon;
                }

                public void setIcon(String icon) {
                    this.icon = icon;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public int getPriority() {
                    return priority;
                }

                public void setPriority(int priority) {
                    this.priority = priority;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }
            }
        }
    }
}
