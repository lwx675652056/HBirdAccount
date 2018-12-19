package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseReturn;

import java.util.List;

/**
 * Created by Liul on 2018/9/3.
 */

public class PullSyncDateReturn extends BaseReturn {

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
        private String synDate;
        private List<SynDataBean> synData;

        public String getSynDate() {
            return synDate;
        }

        public void setSynDate(String synDate) {
            this.synDate = synDate;
        }

        public List<SynDataBean> getSynData() {
            return synData;
        }

        public void setSynData(List<SynDataBean> synData) {
            this.synData = synData;
        }

        public static class SynDataBean {
            private String updateName;
            private int updateBy;
            private long chargeDate;
            private double money;
            private int delflag;
            private long delDate;
            private String typeId;
            private String icon;
            private long updateDate;
            private long createDate;
            private Integer parentId;
            private String createName;
            private int createBy;
            private int accountBookId;
            private int isStaged;
            private int orderType;
            private Integer useDegree;
            private String remark;
            private Integer spendHappiness;
            private String pictureUrl;
            private Object stagedInfo;
            private String typePid;
            private String typePname;
            private String typeName;
            private String id;
            private Integer userPrivateLabelId;
            private String reporterAvatar;
            private String reporterNickName;
            private String abName;

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getUpdateName() {
                return updateName;
            }

            public void setUpdateName(String updateName) {
                this.updateName = updateName;
            }

            public int getUpdateBy() {
                return updateBy;
            }

            public void setUpdateBy(int updateBy) {
                this.updateBy = updateBy;
            }

            public long getChargeDate() {
                return chargeDate;
            }

            public void setChargeDate(long chargeDate) {
                this.chargeDate = chargeDate;
            }

            public double getMoney() {
                return money;
            }

            public void setMoney(double money) {
                this.money = money;
            }

            public int getDelflag() {
                return delflag;
            }

            public void setDelflag(int delflag) {
                this.delflag = delflag;
            }

            public long getDelDate() {
                return delDate;
            }

            public void setDelDate(long delDate) {
                this.delDate = delDate;
            }

            public String getTypeId() {
                return typeId;
            }

            public void setTypeId(String typeId) {
                this.typeId = typeId;
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

            public Integer getParentId() {
                return parentId;
            }

            public void setParentId(Integer parentId) {
                this.parentId = parentId;
            }

            public String getCreateName() {
                return createName;
            }

            public void setCreateName(String createName) {
                this.createName = createName;
            }

            public int getCreateBy() {
                return createBy;
            }

            public void setCreateBy(int createBy) {
                this.createBy = createBy;
            }

            public int getAccountBookId() {
                return accountBookId;
            }

            public void setAccountBookId(int accountBookId) {
                this.accountBookId = accountBookId;
            }

            public int getIsStaged() {
                return isStaged;
            }

            public void setIsStaged(int isStaged) {
                this.isStaged = isStaged;
            }

            public int getOrderType() {
                return orderType;
            }

            public void setOrderType(int orderType) {
                this.orderType = orderType;
            }

            public Integer getUseDegree() {
                return useDegree;
            }

            public void setUseDegree(Integer useDegree) {
                this.useDegree = useDegree;
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

            public String getPictureUrl() {
                return pictureUrl;
            }

            public void setPictureUrl(String pictureUrl) {
                this.pictureUrl = pictureUrl;
            }

            public Object getStagedInfo() {
                return stagedInfo;
            }

            public void setStagedInfo(Object stagedInfo) {
                this.stagedInfo = stagedInfo;
            }

            public String getTypePid() {
                return typePid;
            }

            public void setTypePid(String typePid) {
                this.typePid = typePid;
            }

            public String getTypePname() {
                return typePname;
            }

            public void setTypePname(String typePname) {
                this.typePname = typePname;
            }

            public String getTypeName() {
                return typeName;
            }

            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public Integer getUserPrivateLabelId() {
                return userPrivateLabelId;
            }

            public void setUserPrivateLabelId(Integer userPrivateLabelId) {
                this.userPrivateLabelId = userPrivateLabelId;
            }

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

            public String getAbName() {
                return abName;
            }

            public void setAbName(String abName) {
                this.abName = abName;
            }
        }
    }
}
