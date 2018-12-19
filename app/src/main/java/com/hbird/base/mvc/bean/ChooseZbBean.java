package com.hbird.base.mvc.bean;

/**
 * Created by Liul(245904552@qq.com) on 2018/11/8.
 */

public class ChooseZbBean extends BaseBean {
    private String id;
    private String zbImg;
    private int typeBudget;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZbImg() {
        return zbImg;
    }

    public void setZbImg(String zbImg) {
        this.zbImg = zbImg;
    }

    public int getTypeBudget() {
        return typeBudget;
    }

    public void setTypeBudget(int typeBudget) {
        this.typeBudget = typeBudget;
    }
}
