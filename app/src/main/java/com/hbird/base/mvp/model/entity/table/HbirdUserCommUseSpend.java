package com.hbird.base.mvp.model.entity.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by Liul on 2018/8/31.
 *  用户常用支出类目表
 */
@Entity
public class HbirdUserCommUseSpend {
    /**id*/
    @Unique
    public String id;
    public Integer mark;
    public String parentId;
    public String parentName;
    /**图标*/
    public String icon;
    public String spendName;
    /**优先级*/
    public Integer priority;
    public Integer abTypeId;
    public Integer userInfoId;



    @Generated(hash = 1203665917)
    public HbirdUserCommUseSpend(String id, Integer mark, String parentId,
            String parentName, String icon, String spendName, Integer priority,
            Integer abTypeId, Integer userInfoId) {
        this.id = id;
        this.mark = mark;
        this.parentId = parentId;
        this.parentName = parentName;
        this.icon = icon;
        this.spendName = spendName;
        this.priority = priority;
        this.abTypeId = abTypeId;
        this.userInfoId = userInfoId;
    }
    @Generated(hash = 1350683220)
    public HbirdUserCommUseSpend() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Integer getMark() {
        return this.mark;
    }
    public void setMark(Integer mark) {
        this.mark = mark;
    }
    public String getParentId() {
        return this.parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public String getParentName() {
        return this.parentName;
    }
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
    public String getIcon() {
        return this.icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getSpendName() {
        return this.spendName;
    }
    public void setSpendName(String spendName) {
        this.spendName = spendName;
    }
    public Integer getPriority() {
        return this.priority;
    }
    public void setPriority(Integer priority) {
        this.priority = priority;
    }
    public Integer getAbTypeId() {
        return this.abTypeId;
    }
    public void setAbTypeId(Integer abTypeId) {
        this.abTypeId = abTypeId;
    }
    public Integer getUserInfoId() {
        return this.userInfoId;
    }
    public void setUserInfoId(Integer userInfoId) {
        this.userInfoId = userInfoId;
    }





}
