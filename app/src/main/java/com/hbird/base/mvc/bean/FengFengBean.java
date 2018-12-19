package com.hbird.base.mvc.bean;

/**
 * Created by Liul(245904552@qq.com) on 2018/11/9.
 */

public class FengFengBean extends BaseBean {
    private String content;
    private String times;
    private int type;
    private String name;
    private String itemType;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
}
