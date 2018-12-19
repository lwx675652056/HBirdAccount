package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseReturn;

import java.util.List;

/**
 * Created by Liul on 2018/9/5.
 */

public class ChargeTypeReturn extends BaseReturn {

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
        private String synInterval;
        private List<AllSysIncomeTypeBean> allSysIncomeType;
        private List<AllUserCommUseSpendTypeBean> allUserCommUseSpendType;
        private List<AllUserCommUseIncomeTypeBean> allUserCommUseIncomeType;
        private List<AllSysSpendTypeBean> allSysSpendType;
        private List<AllUserCommUseTypePriorityBean> allUserCommUseTypePriority;

        public String getSynInterval() {
            return synInterval;
        }

        public void setSynInterval(String synInterval) {
            this.synInterval = synInterval;
        }

        public List<AllSysIncomeTypeBean> getAllSysIncomeType() {
            return allSysIncomeType;
        }

        public void setAllSysIncomeType(List<AllSysIncomeTypeBean> allSysIncomeType) {
            this.allSysIncomeType = allSysIncomeType;
        }

        public List<AllUserCommUseSpendTypeBean> getAllUserCommUseSpendType() {
            return allUserCommUseSpendType;
        }

        public void setAllUserCommUseSpendType(List<AllUserCommUseSpendTypeBean> allUserCommUseSpendType) {
            this.allUserCommUseSpendType = allUserCommUseSpendType;
        }

        public List<AllUserCommUseIncomeTypeBean> getAllUserCommUseIncomeType() {
            return allUserCommUseIncomeType;
        }

        public void setAllUserCommUseIncomeType(List<AllUserCommUseIncomeTypeBean> allUserCommUseIncomeType) {
            this.allUserCommUseIncomeType = allUserCommUseIncomeType;
        }

        public List<AllSysSpendTypeBean> getAllSysSpendType() {
            return allSysSpendType;
        }

        public void setAllSysSpendType(List<AllSysSpendTypeBean> allSysSpendType) {
            this.allSysSpendType = allSysSpendType;
        }

        public List<AllUserCommUseTypePriorityBean> getAllUserCommUseTypePriority() {
            return allUserCommUseTypePriority;
        }

        public void setAllUserCommUseTypePriority(List<AllUserCommUseTypePriorityBean> allUserCommUseTypePriority) {
            this.allUserCommUseTypePriority = allUserCommUseTypePriority;
        }

        public static class AllSysIncomeTypeBean {
            private Object incomeTypeSons;
            private Integer delflag;
            private long delDate;
            private long updateDate;
            private long createDate;
            private String parentId;
            private String incomeName;
            private Integer mark;
            private String icon;
            private String status;
            private int priority;
            private String id;

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

        public static class AllUserCommUseSpendTypeBean {
            private int userInfoId;
            private String spendTypePid;
            private String spendTypeId;
            private String spendTypePname;
            private String spendTypeName;
            private String icon;
            private int priority;
            private int id;

            public int getUserInfoId() {
                return userInfoId;
            }

            public void setUserInfoId(int userInfoId) {
                this.userInfoId = userInfoId;
            }

            public String getSpendTypePid() {
                return spendTypePid;
            }

            public void setSpendTypePid(String spendTypePid) {
                this.spendTypePid = spendTypePid;
            }

            public String getSpendTypeId() {
                return spendTypeId;
            }

            public void setSpendTypeId(String spendTypeId) {
                this.spendTypeId = spendTypeId;
            }

            public String getSpendTypePname() {
                return spendTypePname;
            }

            public void setSpendTypePname(String spendTypePname) {
                this.spendTypePname = spendTypePname;
            }

            public String getSpendTypeName() {
                return spendTypeName;
            }

            public void setSpendTypeName(String spendTypeName) {
                this.spendTypeName = spendTypeName;
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

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }

        public static class AllUserCommUseIncomeTypeBean {
            private int userInfoId;
            private String incomeTypePid;
            private String incomeTypeId;
            private String incomeTypePname;
            private String incomeTypeName;
            private String icon;
            private int priority;
            private int id;

            public int getUserInfoId() {
                return userInfoId;
            }

            public void setUserInfoId(int userInfoId) {
                this.userInfoId = userInfoId;
            }

            public String getIncomeTypePid() {
                return incomeTypePid;
            }

            public void setIncomeTypePid(String incomeTypePid) {
                this.incomeTypePid = incomeTypePid;
            }

            public String getIncomeTypeId() {
                return incomeTypeId;
            }

            public void setIncomeTypeId(String incomeTypeId) {
                this.incomeTypeId = incomeTypeId;
            }

            public String getIncomeTypePname() {
                return incomeTypePname;
            }

            public void setIncomeTypePname(String incomeTypePname) {
                this.incomeTypePname = incomeTypePname;
            }

            public String getIncomeTypeName() {
                return incomeTypeName;
            }

            public void setIncomeTypeName(String incomeTypeName) {
                this.incomeTypeName = incomeTypeName;
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

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }

        public static class AllSysSpendTypeBean {
            private Object spendTypeSons;
            private Integer delflag;
            private long delDate;
            private long updateDate;
            private long createDate;
            private String parentId;
            private String spendName;
            private Integer mark;
            private String icon;
            private String status;
            private int priority;
            private String id;

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

            public String  getParentId() {
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

        public static class AllUserCommUseTypePriorityBean {
            private String relation;
            private long updateDate;
            private long createDate;
            private int userInfoId;
            private int id;
            private int type;

            public String getRelation() {
                return relation;
            }

            public void setRelation(String relation) {
                this.relation = relation;
            }

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

            public int getUserInfoId() {
                return userInfoId;
            }

            public void setUserInfoId(int userInfoId) {
                this.userInfoId = userInfoId;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }
}
