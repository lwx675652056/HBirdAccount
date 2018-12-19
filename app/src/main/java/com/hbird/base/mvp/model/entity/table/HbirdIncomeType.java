package com.hbird.base.mvp.model.entity.table;

import org.greenrobot.greendao.annotation.Entity;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by Liul on 2018/8/31.
 * 系统收入类目表
 */
@Entity
public class HbirdIncomeType {
    @Unique
    public String id;
    /**收入类目名称*/
    public String incomeName;
    /**收入父级类目*/
    public String parentId;
    /**图标*/
    public String icon;
    /**状态(0:下线,1:上线)*/
    public String status;
    /**优先级*/
    public Integer priority;
    /**常用标记,0:不常用,1:常用*/
    public Integer mark;
    /**更新时间*/
    public Date updateDate;
    /**创建时间*/
    public Date createDate;
    /**删除标记*/
    public Integer delflag;
    /**删除时间*/
    public Date delDate;
    @Generated(hash = 1149116708)
    public HbirdIncomeType(String id, String incomeName, String parentId,
            String icon, String status, Integer priority, Integer mark,
            Date updateDate, Date createDate, Integer delflag, Date delDate) {
        this.id = id;
        this.incomeName = incomeName;
        this.parentId = parentId;
        this.icon = icon;
        this.status = status;
        this.priority = priority;
        this.mark = mark;
        this.updateDate = updateDate;
        this.createDate = createDate;
        this.delflag = delflag;
        this.delDate = delDate;
    }
    @Generated(hash = 71130263)
    public HbirdIncomeType() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getIncomeName() {
        return this.incomeName;
    }
    public void setIncomeName(String incomeName) {
        this.incomeName = incomeName;
    }
    public String getParentId() {
        return this.parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public String getIcon() {
        return this.icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Integer getPriority() {
        return this.priority;
    }
    public void setPriority(Integer priority) {
        this.priority = priority;
    }
    public Integer getMark() {
        return this.mark;
    }
    public void setMark(Integer mark) {
        this.mark = mark;
    }
    public Date getUpdateDate() {
        return this.updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public Date getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public Integer getDelflag() {
        return this.delflag;
    }
    public void setDelflag(Integer delflag) {
        this.delflag = delflag;
    }
    public Date getDelDate() {
        return this.delDate;
    }
    public void setDelDate(Date delDate) {
        this.delDate = delDate;
    }
}
