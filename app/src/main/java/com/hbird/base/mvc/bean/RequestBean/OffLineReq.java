package com.hbird.base.mvc.bean.RequestBean;

import com.hbird.base.mvc.bean.BaseBean;

import java.util.Date;
import java.util.List;

/**
 * Created by Liul on 2018/9/5.
 */

public class OffLineReq extends BaseBean {

    public String mobileDevice;
    public String synDate;
    public List<SynDataBean> synData;

    public String getMobileDevice() {
        return mobileDevice;
    }

    public void setMobileDevice(String mobileDevice) {
        this.mobileDevice = mobileDevice;
    }

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
        public String typeName;
        public String id;
        public Integer isStaged;
        public Integer useDegree;
        public String pictureUrl;
        public Object stagedInfo;
        public String parentId;
        public String remark;
        public int accountBookId;
        public Date chargeDate;
        public Integer orderType;
        public Double money;
        public Date createDate;
        public String createName;
        public String typeId;
        public Integer spendHappiness;
        public String typePid;
        public String typePname;
        public Date updateDate;
        public Integer delflag;
        public Integer createBy;
        public Date delDate;
        public Integer updateBy;
        public String updateName;
        public String icon;
        //本条记录用户自有标签id （新增返回字段）
        public Integer userPrivateLabelId;
        //记录者头像
        public String reporterAvatar;
        //记录者昵称
        public String reporterNickName;
        //所属账本名称
        public String abName;
        public Integer assetsId;// 账户id
        public String assetsName;// 账户名


        public Integer getAssetsId() {
            return assetsId;
        }

        public void setAssetsId(Integer assetsId) {
            this.assetsId = assetsId;
        }

        public String getAssetsName() {
            return assetsName;
        }

        public void setAssetsName(String assetsName) {
            this.assetsName = assetsName;
        }

        public void setIsStaged(Integer isStaged) {
            this.isStaged = isStaged;
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

        public Integer getUserPrivateLabelId() {
            return userPrivateLabelId;
        }

        public void setUserPrivateLabelId(Integer userPrivateLabelId) {
            this.userPrivateLabelId = userPrivateLabelId;
        }

        public String getUpdateName() {
            return updateName;
        }

        public void setUpdateName(String updateName) {
            this.updateName = updateName;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public Integer getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(Integer updateBy) {
            this.updateBy = updateBy;
        }

        public String getCreateName() {
            return createName;
        }

        public void setCreateName(String createName) {
            this.createName = createName;
        }

        public Date getDelDate() {
            return delDate;
        }

        public void setDelDate(Date delDate) {
            this.delDate = delDate;
        }

        public Double getMoney() {
            return money;
        }

        public Integer getCreateBy() {
            return createBy;
        }

        public void setCreateBy(Integer createBy) {
            this.createBy = createBy;
        }

        public void setMoney(Double money) {
            this.money = money;
        }

        public Integer getDelflag() {
            return delflag;
        }

        public void setDelflag(Integer delflag) {
            this.delflag = delflag;
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

        public int getIsStaged() {
            return isStaged;
        }

        public void setIsStaged(int isStaged) {
            this.isStaged = isStaged;
        }

        public Integer getUseDegree() {
            return useDegree;
        }

        public void setUseDegree(Integer useDegree) {
            this.useDegree = useDegree;
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

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getAccountBookId() {
            return accountBookId;
        }

        public void setAccountBookId(int accountBookId) {
            this.accountBookId = accountBookId;
        }

        public Date getChargeDate() {
            return chargeDate;
        }

        public void setChargeDate(Date chargeDate) {
            this.chargeDate = chargeDate;
        }

        public Integer getOrderType() {
            return orderType;
        }

        public void setOrderType(Integer orderType) {
            this.orderType = orderType;
        }

        public Date getCreateDate() {
            return createDate;
        }

        public void setCreateDate(Date createDate) {
            this.createDate = createDate;
        }

        public String getTypeId() {
            return typeId;
        }

        public void setTypeId(String typeId) {
            this.typeId = typeId;
        }

        public Integer getSpendHappiness() {
            return spendHappiness;
        }

        public void setSpendHappiness(Integer spendHappiness) {
            this.spendHappiness = spendHappiness;
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

        public Date getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(Date updateDate) {
            this.updateDate = updateDate;
        }
    }
}
