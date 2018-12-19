package com.hbird.base.mvc.bean.RequestBean;

import com.hbird.base.mvc.bean.BaseBean;

/**
 * Created by Liul on 2018/7/9.
 */

public class MyAccountTypeReq extends BaseBean {
    private String spendTypeId;
    private String abTypeId;
    private String labelId;

    public String getAbTypeId() {
        return abTypeId;
    }

    public void setAbTypeId(String abTypeId) {
        this.abTypeId = abTypeId;
    }

    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public String getSpendTypeId() {
        return spendTypeId;
    }

    public void setSpendTypeId(String spendTypeId) {
        this.spendTypeId = spendTypeId;
    }
}
