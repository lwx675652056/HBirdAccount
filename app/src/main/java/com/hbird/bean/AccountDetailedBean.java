package com.hbird.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import com.android.databinding.library.baseAdapters.BR;
import com.hbird.base.mvc.bean.indexBaseListBean;
import com.hbird.base.util.DateUtil;
import com.hbird.base.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class AccountDetailedBean extends BaseObservable implements Comparable<AccountDetailedBean> {

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
    public String reporterAvatar; // 记录者头像
    public String reporterNickName; // 记录者昵称
    public Integer createBy;
    public String createName;
    public Integer updateBy;
    public String updateName;
    public Integer userPrivateLabelId;
    public String abName;
    public Integer assetsId;
    public String assetsName;

    @Override
    public int compareTo(@NonNull AccountDetailedBean o) {
        if (indexBeen != null && o.indexBeen != null) {
            if (createDate > o.createDate) {
                return -1;
            }
        }
        return 0;
    }

    public static class indexBean extends BaseObservable {
        private double daySpend;
        private double dayIncome;
        private long dayTime;

        private String weekDay;

        @Bindable
        public String getWeekDay() {
            return DateUtil.dateToWeek(DateUtils.getDay(dayTime));
        }

        @Bindable
        public double getDaySpend() {
            return daySpend;
        }

        public void setDaySpend(double daySpend) {
            this.daySpend = daySpend;
            notifyPropertyChanged(BR.daySpend);
        }

        @Bindable
        public double getDayIncome() {
            return dayIncome;
        }

        public void setDayIncome(double dayIncome) {
            this.dayIncome = dayIncome;
            notifyPropertyChanged(BR.dayIncome);
        }

        @Bindable
        public String getDayTime() {
            return DateUtils.getMonthDay(dayTime);
        }

        public void setDayTime(long dayTime) {
            this.dayTime = dayTime;
            notifyPropertyChanged(BR.dayTime);
        }
    }

    @Bindable
    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
        notifyPropertyChanged(BR.tag);
    }

    @Bindable
    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
        notifyPropertyChanged(BR.orderType);
    }

    @Bindable
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
        notifyPropertyChanged(BR.icon);
    }

    @Bindable
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
        notifyPropertyChanged(BR.typeName);
    }

    @Bindable
    public int getIsStaged() {
        return isStaged;
    }

    public void setIsStaged(int isStaged) {
        this.isStaged = isStaged;
        notifyPropertyChanged(BR.isStaged);
    }

    @Bindable
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
        notifyPropertyChanged(BR.remark);
    }

    @Bindable
    public Integer getSpendHappiness() {
        return spendHappiness;
    }

    public void setSpendHappiness(Integer spendHappiness) {
        this.spendHappiness = spendHappiness;
        notifyPropertyChanged(BR.spendHappiness);
    }

    @Bindable
    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
        notifyPropertyChanged(BR.money);
    }

    @Bindable
    public String getTypePid() {
        return typePid;
    }

    public void setTypePid(String typePid) {
        this.typePid = typePid;
        notifyPropertyChanged(BR.typePid);
    }

    @Bindable
    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
        notifyPropertyChanged(BR.typeId);
    }

    @Bindable
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public int getAccountBookId() {
        return accountBookId;
    }

    public void setAccountBookId(int accountBookId) {
        this.accountBookId = accountBookId;
        notifyPropertyChanged(BR.accountBookId);
    }

    @Bindable
    public long getChargeDate() {
        return chargeDate;
    }

    public void setChargeDate(long chargeDate) {
        this.chargeDate = chargeDate;
        notifyPropertyChanged(BR.chargeDate);
    }

    @Bindable
    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
        notifyPropertyChanged(BR.createDate);
    }

    @Bindable
    public String getTypePname() {
        return typePname;
    }

    public void setTypePname(String typePname) {
        this.typePname = typePname;
        notifyPropertyChanged(BR.typePname);
    }

    @Bindable
    public List<indexBean> getIndexBeen() {
        return indexBeen;
    }

    public void setIndexBeen(List<indexBean> indexBeen) {
        this.indexBeen = indexBeen;
        notifyPropertyChanged(BR.indexBeen);
    }

    @Bindable
    public String getReporterAvatar() {
        return reporterAvatar;
    }

    public void setReporterAvatar(String reporterAvatar) {
        this.reporterAvatar = reporterAvatar;
        notifyPropertyChanged(BR.reporterAvatar);
    }

    @Bindable
    public String getReporterNickName() {
        return reporterNickName;
    }

    public void setReporterNickName(String reporterNickName) {
        this.reporterNickName = reporterNickName;
        notifyPropertyChanged(BR.reporterNickName);
    }

    @Bindable
    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
        notifyPropertyChanged(BR.createBy);
    }

    @Bindable
    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
        notifyPropertyChanged(BR.createName);
    }

    @Bindable
    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
        notifyPropertyChanged(BR.updateBy);
    }

    @Bindable
    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
        notifyPropertyChanged(BR.updateName);
    }

    @Bindable
    public Integer getUserPrivateLabelId() {
        return userPrivateLabelId;
    }

    public void setUserPrivateLabelId(Integer userPrivateLabelId) {
        this.userPrivateLabelId = userPrivateLabelId;
        notifyPropertyChanged(BR.userPrivateLabelId);
    }

    @Bindable
    public String getAbName() {
        return abName;
    }

    public void setAbName(String abName) {
        this.abName = abName;
        notifyPropertyChanged(BR.abName);
    }

    @Bindable
    public Integer getAssetsId() {
        return assetsId;
    }

    public void setAssetsId(Integer assetsId) {
        this.assetsId = assetsId;
        notifyPropertyChanged(BR.assetsId);
    }

    @Bindable
    public String getAssetsName() {
        return assetsName;
    }

    public void setAssetsName(String assetsName) {
        this.assetsName = assetsName;
        notifyPropertyChanged(BR.assetsName);
    }

    public void setBean(indexBaseListBean temp) {
        tag = temp.getTag();
        orderType = temp.getOrderType();
        icon = temp.getIcon();
        typeName = temp.getTypeName();
        isStaged = temp.getIsStaged();
        remark = temp.getRemark();
        spendHappiness = temp.getSpendHappiness();
        money = temp.getMoney();
        typePid = temp.getTypePid();
        typeId = temp.getTypeId();
        id = temp.getId();
        accountBookId = temp.getAccountBookId();
        chargeDate = temp.getChargeDate();
        createDate = temp.getCreateDate();
        typePname = temp.getTypePname();
        createBy = temp.getCreateBy();
        createName = temp.createName;
        updateBy = temp.updateBy;
        updateName = temp.updateName;
        userPrivateLabelId = temp.userPrivateLabelId;
        abName = temp.abName;
        assetsId = temp.assetsId;
        assetsName = temp.assetsName;
        indexBeen = new ArrayList<>();
        if (temp.getIndexBeen() != null) {
            for (int i = 0; i < temp.getIndexBeen().size(); i++) {
                indexBean temp1 = new indexBean();
                temp1.setDayIncome(temp.getIndexBeen().get(i).getDayIncome());
                temp1.setDaySpend(temp.getIndexBeen().get(i).getDaySpend());
                temp1.setDayTime(temp.getIndexBeen().get(i).getDayTime());
                indexBeen.add(temp1);
            }
        }

        reporterAvatar = temp.getReporterAvatar();//记录者头像
        reporterNickName = temp.getReporterNickName();    //记录者昵称
    }
}
