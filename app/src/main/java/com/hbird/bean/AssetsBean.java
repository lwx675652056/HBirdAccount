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
    public int mark;
    public double money;
    public int assetsType;// 账户id
    public String priority;

    public boolean exist;// 本地排序
    public boolean isSetting = false;// 是否已设置过值
    public boolean check = false;// 删除的时候是否选择

    public AssetsBean() {
    }

//    public AssetsBean(int order, int assetsType, String desc, int icon, boolean exist) {
//        this.order = order;
//        this.assetsType = assetsType;
//        this.desc = desc;
//        this.icon = icon;
//        this.exist = exist;
//    }


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
