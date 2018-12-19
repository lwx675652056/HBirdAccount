package com.hbird.base.mvc.bean;

/**
 * Created by Liul(245904552@qq.com) on 2018/11/7.
 */

public class MembersBean extends  BaseBean {
    private String memberImgUrl;
    private String memberName;
    private String memberTime;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberImgUrl() {
        return memberImgUrl;
    }

    public void setMemberImgUrl(String memberImgUrl) {
        this.memberImgUrl = memberImgUrl;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberTime() {
        return memberTime;
    }

    public void setMemberTime(String memberTime) {
        this.memberTime = memberTime;
    }
}
