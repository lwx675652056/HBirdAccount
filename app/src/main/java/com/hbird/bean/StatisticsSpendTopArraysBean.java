package com.hbird.bean;

/**
 * @author: LiangYX
 * @ClassName: StatisticsSpendTopArraysBean
 * @date: 2019/1/10 13:35
 * @Description: 首頁饼图分类
 */
public class StatisticsSpendTopArraysBean extends BaseBean {
    public double money;// 分类金钱
    public String icon;// 分类图标
    public String typeName;// 分类名称
    public String typeId;// 分类id
    public String userPrivateLabelId;// 分类id
    public boolean isFrist = true;

    public boolean isFrist() {
        return isFrist;
    }

    public void setFrist(boolean frist) {
        isFrist = frist;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getUserPrivateLabelId() {
        return userPrivateLabelId;
    }

    public void setUserPrivateLabelId(String userPrivateLabelId) {
        this.userPrivateLabelId = userPrivateLabelId;
    }
}
