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
    private String typePname;
    private List<indexBean> indexBeen;
    //记录者头像
    public String reporterAvatar;
    //记录者昵称
    public String reporterNickName;


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

    public static class indexBean{
        private double daySpend;
        private double dayIncome;
        private long dayTime;

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

    public int getIsStaged() {
        return isStaged;
    }

    public void setIsStaged(int isStaged) {
        this.isStaged = isStaged;
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

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        return typePname;
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
                    , double money, String typePid, String typeId, String id, int accountBookId, long chargeDate, long createDate
                    , String typePname){
         this.tag = tag;
         this. orderType = orderType;
         this. icon = icon;
         this. typeName = typeName;
         this. isStaged = isStaged;
         this. remark = remark;
         this. spendHappiness = spendHappiness;
        this. money = money;
         this. typePid = typePid;
         this. typeId = typeId;
         this. id = id;
         this. accountBookId =accountBookId;
        this. chargeDate = chargeDate;
        this. createDate = createDate;
         this. typePname = typePname;
    }
}
