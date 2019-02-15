package com.hbird.base.mvc.bean;

/**
 * Created by Liul(245904552@qq.com) on 2018/11/8.
 */

public class ZhangBenMsgBean extends BaseBean {
    private String zbType;
    private String zbImg;
    private String zbName;
    private String zbUTime;
    private String id;
    private int defaultFlag;
    private int abTypeId;
    private int typeBudget;

    public int getTypeBudget() {
        return typeBudget;
    }

    public void setTypeBudget(int typeBudget) {
        this.typeBudget = typeBudget;
    }

    public int getAbTypeId() {
        return abTypeId;
    }

    public void setAbTypeId(int abTypeId) {
        this.abTypeId = abTypeId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZbType() {
        return zbType;
    }

    public void setZbType(String zbType) {
        this.zbType = zbType;
    }

    public String getZbImg() {
        return zbImg;
    }

    public void setZbImg(String zbImg) {
        this.zbImg = zbImg;
    }

    public String getZbName() {
        return zbName;
    }

    public void setZbName(String zbName) {
        this.zbName = zbName;
    }

    public String getZbUTime() {
        return zbUTime;
    }

    public void setZbUTime(String zbUTime) {
        this.zbUTime = zbUTime;
    }

    public int getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(int defaultFlag) {
        this.defaultFlag = defaultFlag;
    }
}
