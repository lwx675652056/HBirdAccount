package com.hbird.base.mvp.model.entity.table;

import org.greenrobot.greendao.annotation.Entity;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by Liul on 2018/8/31.
 * 用户常用类目排序表
 */
@Entity
public class HbirdUserCommTypePriority {
    /**id*/
    @Unique
    public Integer id;
    /**用户详情id*/
    public Integer userInfoId;
    /**用户所属类目类型  1:支出 2:收入*/
    public Integer type;
    /**创建时间*/
    public Date createDate;
    /**类目优先级关系*/
    public String relation;
    /**更新时间*/
    public Date updateDate;
    @Generated(hash = 1433603685)
    public HbirdUserCommTypePriority(Integer id, Integer userInfoId, Integer type,
            Date createDate, String relation, Date updateDate) {
        this.id = id;
        this.userInfoId = userInfoId;
        this.type = type;
        this.createDate = createDate;
        this.relation = relation;
        this.updateDate = updateDate;
    }
    @Generated(hash = 653891905)
    public HbirdUserCommTypePriority() {
    }
    public Integer getId() {
        return this.id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getUserInfoId() {
        return this.userInfoId;
    }
    public void setUserInfoId(Integer userInfoId) {
        this.userInfoId = userInfoId;
    }
    public Integer getType() {
        return this.type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public Date getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getRelation() {
        return this.relation;
    }
    public void setRelation(String relation) {
        this.relation = relation;
    }
    public Date getUpdateDate() {
        return this.updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }


}
