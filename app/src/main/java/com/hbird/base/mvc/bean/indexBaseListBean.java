package com.hbird.base.mvc.bean;

import java.util.List;

/**
 * Created by Liul on 2018/6/29.
 * 自己抽取出来的 首页明细 总数据
 */

public class indexBaseListBean extends BaseBean {
    private int tag;//自定义标签 区分二三级条目 （0标题条目和1数据条目） 根据这个首页显示不同的样式布局
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
    private List<indexBean> indexBeen;
    //记录者头像
    public String reporterAvatar;
    //记录者昵称
    public String reporterNickName;
    public Integer createBy;
    public String createName;
    public Integer updateBy;
    public String updateName;
    public Integer userPrivateLabelId;
    public String abName;
    public Integer assetsId;
    public String assetsName;

    public Integer useDegree;
    public Integer parentId;
    public String pictureUrl;
    public Integer delflag;
    public long delDate;


    @Override
    public String toString() {
        return "{" +
                "tag:" + tag +
                ", orderType:" + orderType +
                ", icon:'" + icon + '\'' +
                ", typeName:'" + typeName + '\'' +
                ", delflag:'" + delflag + '\'' +
                ", delDate:'" + delDate + '\'' +
                ", parentId:'" + parentId + '\'' +
                ", pictureUrl:'" + pictureUrl + '\'' +
                ", isStaged:" + isStaged +
                ", useDegree:" + useDegree +
                ", remark:'" + remark + '\'' +
                ", spendHappiness:" + spendHappiness +
                ", money:" + money +
                ", typePid:'" + typePid + '\'' +
                ", typeId:'" + typeId + '\'' +
                ", id:'" + id + '\'' +
                ", accountBookId:" + accountBookId +
                ", chargeDate:" + chargeDate +
                ", createDate:" + createDate +
                ", updateDate:" + updateDate +
                ", typePname:'" + typePname + '\'' +
                ", indexBeen:" + indexBeen +
                ", reporterAvatar:'" + reporterAvatar + '\'' +
                ", reporterNickName:'" + reporterNickName + '\'' +
                ", createBy:" + createBy +
                ", createName:'" + createName + '\'' +
                ", updateBy:" + updateBy +
                ", updateName:'" + updateName + '\'' +
                ", userPrivateLabelId:" + userPrivateLabelId +
                ", abName:'" + abName + '\'' +
                ", assetsId:" + assetsId +
                ", assetsName:'" + assetsName + '\'' +
                '}';
    }

    public String getReporterAvatar() {
        return (reporterAvatar == null || reporterAvatar.equals("null")) ? "" : reporterAvatar;
    }

    public void setReporterAvatar(String reporterAvatar) {
        this.reporterAvatar = reporterAvatar;
    }

    public String getReporterNickName() {
        return (reporterNickName == null || reporterNickName.equals("null")) ? "" : reporterNickName;
    }

    public void setReporterNickName(String reporterNickName) {
        this.reporterNickName = reporterNickName;
    }

    public Integer getCreateBy() {
        return (createBy == null || createBy.equals("null")) ? 1 : createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public String getCreateName() {
        return (createName == null || createName.equals("null")) ? "" : createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Integer getUpdateBy() {
        return (updateBy == null || updateBy.equals("null")) ? 1 : updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateName() {
        return (updateName == null || updateName.equals("null")) ? "" : updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public Integer getUserPrivateLabelId() {
        return (userPrivateLabelId == null || userPrivateLabelId.equals("null")) ? 1 : userPrivateLabelId;
    }

    public void setUserPrivateLabelId(Integer userPrivateLabelId) {
        this.userPrivateLabelId = userPrivateLabelId;
    }

    public Integer getUseDegree() {
        return (useDegree == null || useDegree.equals("null")) ? 1 : useDegree;
    }

    public void setUseDegree(Integer useDegree) {
        this.useDegree = useDegree;
    }

    public Integer getParentId() {
        return (parentId == null || parentId.equals("null")) ? 1 : parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getPictureUrl() {
        return (pictureUrl == null || pictureUrl.equals("null")) ? "" : pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Integer getDelflag() {
        return (delflag == null || delflag.equals("null")) ? 0 : delflag;
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

    public String getAbName() {
        return (abName == null || abName.equals("null")) ? "" : abName;
    }

    public void setAbName(String abName) {
        this.abName = abName;
    }

    public Integer getAssetsId() {
        return (assetsId == null || assetsId.equals("null")) ? 1 : assetsId;
    }

    public void setAssetsId(Integer assetsId) {
        this.assetsId = assetsId;
    }

    public String getAssetsName() {
        return (assetsName == null || assetsName.equals("null")) ? "" : assetsName;
    }

    public void setAssetsName(String assetsName) {
        this.assetsName = assetsName;
    }

    public static class indexBean {
        private double daySpend;
        private double dayIncome;
        private long dayTime;

        @Override
        public String toString() {
            return "{" +
                    "daySpend:" + daySpend +
                    ", dayIncome:" + dayIncome +
                    ", dayTime:" + dayTime +
                    '}';
        }

        public double getDaySpend() {
            return daySpend;
        }

        public void setDaySpend(double daySpend) {
            this.daySpend = daySpend;
        }

        public double getDayIncome() {
            return dayIncome;
        }

        public void setDayIncome(double dayIncome) {
            this.dayIncome = dayIncome;
        }

        public long getDayTime() {
            return dayTime;
        }

        public void setDayTime(long dayTime) {
            this.dayTime = dayTime;
        }
    }


    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getIcon() {
        return (icon == null || icon.equals("null")) ? "" : icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTypeName() {
        return (typeName == null || typeName.equals("null")) ? "" : typeName;
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
        return (remark == null || remark.equals("null")) ? "" : remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getSpendHappiness() {
        return (spendHappiness == null || spendHappiness.equals("null")) ? 1 : spendHappiness;
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
        return (typePid == null || typePid.equals("null")) ? "" : typePid;
    }

    public void setTypePid(String typePid) {
        this.typePid = typePid;
    }

    public String getTypeId() {
        return (typeId == null || typeId.equals("null")) ? "" : typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getId() {
        return (id == null || id.equals("null")) ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
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
        return (typePname == null || typePname.equals("null")) ? "" : typePname;
    }

    public void setTypePname(String typePname) {
        this.typePname = typePname;
    }

    public List<indexBean> getIndexBeen() {
        return indexBeen;
    }

    public void setIndexBeen(List<indexBean> indexBeen) {
        this.indexBeen = indexBeen;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public void setDates(int tag, int orderType, String icon, String typeName, int isStaged, String remark, Integer spendHappiness
            , double money, String typePid, String typeId, String id, int accountBookId, long chargeDate, long createDate, long updateDate
            , String typePname) {
        this.tag = tag;
        this.orderType = orderType;
        this.icon = icon;
        this.typeName = typeName;
        this.isStaged = isStaged;
        this.remark = remark;
        this.spendHappiness = spendHappiness;
        this.money = money;
        this.typePid = typePid;
        this.typeId = typeId;
        this.id = id;
        this.accountBookId = accountBookId;
        this.chargeDate = chargeDate;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.typePname = typePname;
        this.reporterAvatar = "";
        this.reporterNickName = "";
        this.createBy = 0;
        this.createName = "";
        this.updateBy = 0;
        this.updateName = "";
        this.userPrivateLabelId = 0;
        this.abName = "";
        this.assetsId = -1;
        this.assetsName = "";
        this.pictureUrl = "";
        this.useDegree = 0;
        this.parentId = 0;
        this.delflag = 0;
        this.delDate = 0;
    }
}
