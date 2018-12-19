package com.hbird.base.mvc.bean;

import com.alibaba.fastjson.annotation.JSONField;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.util.Date;

/**
 * Created by Liul on 2018/8/29.
 *
 */
@Entity
public class WaterOrderBean {

    /**流水记录号*/
    public String id;
    /**单笔金额*/
    public Double money;
    /**所属账本*/
    public Integer accountBookId;
    /**订单类型 1:支出  2:收入*/
    public Integer orderType;
    /**付款方式 1:即时 2:分期*/
    public Integer isStaged;
    /**愉悦度,0:高兴 1:一般 2:差*/
    public Integer spendHappiness;
    /**使用度(保留字段)*/
    public Integer useDegree;
    /**二级类目id*/
    public String typePid;
    /**二级类目_name*/
    public String typePname;
    /**三级类目id*/
    public String typeId;
    /**三级类目_name*/
    public String typeName;
    /**分期parent_id,有此项为分期父级,无此项为即时*/
    public Integer parentId;
    /**图片记录*/
    public String pictureUrl;
    /**更新时间*/
    public Date updateDate;
    /**创建时间*/
    public Date createDate;
    /**记账时间*/
    public String chargeDate;
    /**删除状态,0:有效 1:删除*/
    @JSONField(serialize=false)
    public Integer delflag;
    /**删除时间*/
    public Date delDate;
    /**创建者id*/
    public Integer createBy;
    /**创建者名称*/
    public String createName;
    /**修改者id*/
    public Integer updateBy;
    /**修改者名称*/
    public String updateName;
    /**备注*/
    public String remark;
    /**连表查询时的多余字段 -- 添加*/
    public String icon;

    @Generated(hash = 2087952972)
    public WaterOrderBean(String id, Double money, Integer accountBookId,
            Integer orderType, Integer isStaged, Integer spendHappiness,
            Integer useDegree, String typePid, String typePname, String typeId,
            String typeName, Integer parentId, String pictureUrl, Date updateDate,
            Date createDate, String chargeDate, Integer delflag, Date delDate,
            Integer createBy, String createName, Integer updateBy,
            String updateName, String remark, String icon) {
        this.id = id;
        this.money = money;
        this.accountBookId = accountBookId;
        this.orderType = orderType;
        this.isStaged = isStaged;
        this.spendHappiness = spendHappiness;
        this.useDegree = useDegree;
        this.typePid = typePid;
        this.typePname = typePname;
        this.typeId = typeId;
        this.typeName = typeName;
        this.parentId = parentId;
        this.pictureUrl = pictureUrl;
        this.updateDate = updateDate;
        this.createDate = createDate;
        this.chargeDate = chargeDate;
        this.delflag = delflag;
        this.delDate = delDate;
        this.createBy = createBy;
        this.createName = createName;
        this.updateBy = updateBy;
        this.updateName = updateName;
        this.remark = remark;
        this.icon = icon;
    }

    @Generated(hash = 124233626)
    public WaterOrderBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Integer getAccountBookId() {
        return accountBookId;
    }

    public void setAccountBookId(Integer accountBookId) {
        this.accountBookId = accountBookId;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getIsStaged() {
        return isStaged;
    }

    public void setIsStaged(Integer isStaged) {
        this.isStaged = isStaged;
    }

    public Integer getSpendHappiness() {
        return spendHappiness;
    }

    public void setSpendHappiness(Integer spendHappiness) {
        this.spendHappiness = spendHappiness;
    }

    public Integer getUseDegree() {
        return useDegree;
    }

    public void setUseDegree(Integer useDegree) {
        this.useDegree = useDegree;
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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getChargeDate() {
        return chargeDate;
    }

    public void setChargeDate(String chargeDate) {
        this.chargeDate = chargeDate;
    }

    public Integer getDelflag() {
        return delflag;
    }

    public void setDelflag(Integer delflag) {
        this.delflag = delflag;
    }

    public Date getDelDate() {
        return delDate;
    }

    public void setDelDate(Date delDate) {
        this.delDate = delDate;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
