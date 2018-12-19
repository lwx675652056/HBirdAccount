package com.hbird.base.mvc.bean.RequestBean;

import com.hbird.base.mvc.bean.BaseBean;

/**
 * Created by Liul on 2018/7/9.
 */

public class MyAccount2TypeReq extends BaseBean {
    private String incomeTypeId;
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

    public String getIncomeTypeId() {
        return incomeTypeId;
    }

    public void setIncomeTypeId(String incomeTypeId) {
        this.incomeTypeId = incomeTypeId;
    }
}
