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
}
