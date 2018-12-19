package com.hbird.base.mvc.bean.RequestBean;

import com.hbird.base.mvc.bean.BaseBean;

/**
 * Created by Liul on 2018/9/5.
 */

public class SystemParamReq extends BaseBean {
    private String sysSpendTypeVersion;
    private String sysIncomeTypeVersion;
    private String userCommUseSpendTypeVersion;
    private String userCommUseIncomeTypeVersion;
    private String userCommTypePriorityVersion;
    private String labelVersion;

    public String getSysSpendTypeVersion() {
        return sysSpendTypeVersion;
    }

    public void setSysSpendTypeVersion(String sysSpendTypeVersion) {
        this.sysSpendTypeVersion = sysSpendTypeVersion;
    }

    public String getSysIncomeTypeVersion() {
        return sysIncomeTypeVersion;
    }

    public void setSysIncomeTypeVersion(String sysIncomeTypeVersion) {
        this.sysIncomeTypeVersion = sysIncomeTypeVersion;
    }

    public String getUserCommUseSpendTypeVersion() {
        return userCommUseSpendTypeVersion;
    }

    public void setUserCommUseSpendTypeVersion(String userCommUseSpendTypeVersion) {
        this.userCommUseSpendTypeVersion = userCommUseSpendTypeVersion;
    }

    public String getUserCommUseIncomeTypeVersion() {
        return userCommUseIncomeTypeVersion;
    }

    public void setUserCommUseIncomeTypeVersion(String userCommUseIncomeTypeVersion) {
        this.userCommUseIncomeTypeVersion = userCommUseIncomeTypeVersion;
    }

    public String getUserCommTypePriorityVersion() {
        return userCommTypePriorityVersion;
    }

    public void setUserCommTypePriorityVersion(String userCommTypePriorityVersion) {
        this.userCommTypePriorityVersion = userCommTypePriorityVersion;
    }

    public String getLabelVersion() {
        return labelVersion;
    }

    public void setLabelVersion(String labelVersion) {
        this.labelVersion = labelVersion;
    }
}
