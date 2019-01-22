package com.hbird.bean;

import android.databinding.BaseObservable;

/**
 * @author: LiangYX
 * @ClassName: AccountBean
 * @date: 2019/1/22 14:22
 * @Description: 账本
 */
public class AccountBean extends BaseObservable {

    public String icon;// 账本图片
    public String abTypeName;// 账本类型
    public String member;// 账本人数
    public String updateDate;// 更新时间 1548065479000
    public String id;
    public String userType;
    public String typeBudget;
    public int abTypeId;
    public String abName; // 账本名

    @Override
    public String toString() {
        return "{" +
                "icon:'" + icon + '\'' +
                ", abTypeName:'" + abTypeName + '\'' +
                ", member:" + member +
                ", updateDate:" + updateDate +
                ", id:" + id +
                ", userType:" + userType +
                ", typeBudget:" + typeBudget +
                ", abTypeId:" + abTypeId +
                ", abName:'" + abName + '\'' +
                '}';
    }
}
