package com.hbird.base.mvc.bean.RequestBean;

import com.hbird.base.mvc.bean.BaseBean;

import java.util.List;

/**
 * Created by Liul on 2018/7/9.
 */

public class delZcTypeReq extends BaseBean {
    private String spendTypeIds;
    private int abTypeId;
    private List<Integer> labelIds;

    public int getAbTypeId() {
        return abTypeId;
    }

    public void setAbTypeId(int abTypeId) {
        this.abTypeId = abTypeId;
    }

    public String getSpendTypeIds() {
        return spendTypeIds;
    }

    public void setSpendTypeIds(String spendTypeIds) {
        this.spendTypeIds = spendTypeIds;
    }

    public List<Integer> getLabelIds() {
        return labelIds;
    }

    public void setLabelIds(List<Integer> labelIds) {
        this.labelIds = labelIds;
    }
}
