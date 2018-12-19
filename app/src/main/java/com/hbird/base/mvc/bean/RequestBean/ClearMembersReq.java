package com.hbird.base.mvc.bean.RequestBean;

import com.hbird.base.mvc.bean.BaseBean;

import java.util.List;

/**
 * Created by Liul on 2018/11/14.
 */

public class ClearMembersReq extends BaseBean {

    private String abId;
    private List<String> memberIds;

    public String getAbId() {
        return abId;
    }

    public void setAbId(String abId) {
        this.abId = abId;
    }

    public List<String> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<String> memberIds) {
        this.memberIds = memberIds;
    }
}
