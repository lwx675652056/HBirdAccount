package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseReturn;

/**
 * Created by Liul on 2018/7/9.
 */

public class SingleReturn extends BaseReturn{

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
        private String typePname;
        private Object pictureUrl;
        private Object stagedInfo;
        private String typeId;
        private long createDate;
        private Object parentId;
        private int accountBookId;
        private long chargeDate;
        private double money;
        private int orderType;
        private String typePid;
        private Object useDegree;
        private String remark;
        private Integer spendHappiness;
        private int isStaged;
        private String icon;
        private String typeName;
        private String id;

        public String getTypePname() {
            return typePname;
        }

        public void setTypePname(String typePname) {
            this.typePname = typePname;
        }

        public Object getPictureUrl() {
            return pictureUrl;
        }

        public void setPictureUrl(Object pictureUrl) {
            this.pictureUrl = pictureUrl;
        }

        public Object getStagedInfo() {
            return stagedInfo;
        }

        public void setStagedInfo(Object stagedInfo) {
            this.stagedInfo = stagedInfo;
        }

        public String getTypeId() {
            return typeId;
        }

        public void setTypeId(String typeId) {
            this.typeId = typeId;
        }

        public long getCreateDate() {
            return createDate;
        }

        public void setCreateDate(long createDate) {
            this.createDate = createDate;
        }

        public Object getParentId() {
            return parentId;
        }

        public void setParentId(Object parentId) {
            this.parentId = parentId;
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

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public int getOrderType() {
            return orderType;
        }

        public void setOrderType(int orderType) {
            this.orderType = orderType;
        }

        public String getTypePid() {
            return typePid;
        }

        public void setTypePid(String typePid) {
            this.typePid = typePid;
        }

        public Object getUseDegree() {
            return useDegree;
        }

        public void setUseDegree(Object useDegree) {
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

        public int getIsStaged() {
            return isStaged;
        }

        public void setIsStaged(int isStaged) {
            this.isStaged = isStaged;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
