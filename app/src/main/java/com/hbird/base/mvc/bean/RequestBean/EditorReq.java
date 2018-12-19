package com.hbird.base.mvc.bean.RequestBean;

import com.hbird.base.mvc.bean.BaseBean;

/**
 * Created by Liul on 2018/7/9.
 */

public class EditorReq extends BaseBean {
    private String id;
    private String money;
    private String typePid;
    private String typeId;
    private String typePname;
    private String typeName;
    private String isStaged;
    private String orderType;
    private String remark;
    private Long chargeDate;
    private Integer spendHappiness;

    public Integer getSpendHappiness() {
        return spendHappiness;
    }

    public void setSpendHappiness(Integer spendHappiness) {
        this.spendHappiness = spendHappiness;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
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

    public String getIsStaged() {
        return isStaged;
    }

    public void setIsStaged(String isStaged) {
        this.isStaged = isStaged;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getChargeDate() {
        return chargeDate;
    }

    public void setChargeDate(Long chargeDate) {
        this.chargeDate = chargeDate;
    }
}
