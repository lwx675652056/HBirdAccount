package com.hbird.base.mvp.model.entity.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by Liul on 2018/8/31.
 *  用户常用收入类目表
 */
@Entity
public class HbirdUserCommUseIncome {
    /**id*/
    @Unique
    public String id;
    /**图标*/
    public String icon;
    /**优先级*/
    public Integer priority;
    public String incomeName;
    public Integer mark;
    public String parentId;
    public String parentName;
    public Integer abTypeId;
    public Integer userInfoId;
    @Generated(hash = 1252122962)
    public HbirdUserCommUseIncome(String id, String icon, Integer priority,
            String incomeName, Integer mark, String parentId, String parentName,
            Integer abTypeId, Integer userInfoId) {
        this.id = id;
        this.icon = icon;
        this.priority = priority;
        this.incomeName = incomeName;
        this.mark = mark;
        this.parentId = parentId;
        this.parentName = parentName;
        this.abTypeId = abTypeId;
        this.userInfoId = userInfoId;
    }
    @Generated(hash = 1270853847)
    public HbirdUserCommUseIncome() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getIcon() {
        return this.icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public Integer getPriority() {
        return this.priority;
    }
    public void setPriority(Integer priority) {
        this.priority = priority;
    }
    public String getIncomeName() {
        return this.incomeName;
    }
    public void setIncomeName(String incomeName) {
        this.incomeName = incomeName;
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
