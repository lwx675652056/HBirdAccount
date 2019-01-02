package com.hbird.bean;

import java.io.Serializable;

/**
 * @author: LiangYX
 * @ClassName: AssetsBean
 * @date: 2018/12/27 20:12
 * @Description: 我的账户、资产
 */
public class AssetsBean implements Serializable {

    public String icon;// 账户图标
    public long updateDate;
    public long createDate;
    public String assetsName;// 账户名
    public int mark;// 是否在默认账户  1为是
    public double money;
    public int assetsType;// 账户id
    public String priority;

    public boolean check = false;// 删除的时候是否选择

    public AssetsBean() {
    }

    @Override
    public String toString() {
        return "{" +
                "icon:'" + icon + '\'' +
                ", updateDate:" + updateDate +
                ", createDate:" + createDate +
                ", assetsName:'" + assetsName + '\'' +
                ", mark:" + mark +
                ", money:" + money +
                ", assetsType:'" + assetsType + '\'' +
                ", priority:'" + priority + '\'' +
                '}';
    }
}
