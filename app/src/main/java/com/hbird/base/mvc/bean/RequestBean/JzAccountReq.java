package com.hbird.base.mvc.bean.RequestBean;

import com.hbird.base.mvc.bean.BaseBean;

/**
 * Created by Liul on 2018/7/10.
 */

public class JzAccountReq extends BaseBean {
    private String typePid;
    private String typePname;
    private String typeId;
    private String typeName;
    private String money;
    private String chargeDate;
    private String remark;
    private int orderType;
    private int isStaged;
    private Integer spendHappiness;
    private Long updateDate;
    private Long updateBy;

    public Long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
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

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getChargeDate() {
        return chargeDate;
    }

    public void setChargeDate(String chargeDate) {
        this.chargeDate = chargeDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getIsStaged() {
        return isStaged;
    }

    public void setIsStaged(int isStaged) {
        this.isStaged = isStaged;
    }

    public Integer getSpendHappiness() {
        return spendHappiness;
    }

    public void setSpendHappiness(Integer spendHappiness) {
        this.spendHappiness = spendHappiness;
    }
}