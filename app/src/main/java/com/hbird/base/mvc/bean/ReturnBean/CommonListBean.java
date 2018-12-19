package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseReturn;

/**
 * Created by Liul on 2018/7/10.
 * 收入bean
 */

public class CommonListBean extends BaseReturn{

    private String icon;
    private String id;
    private String incomeName;
    private int mark;
    private String parentId;
    private String parentName;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIncomeName() {
        return incomeName;
    }

    public void setIncomeName(String incomeName) {
        this.incomeName = incomeName;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
