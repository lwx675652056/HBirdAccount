package com.hbird.base.mvc.bean.ReturnBean;

import com.hbird.base.mvc.bean.BaseReturn;

/**
 * Created by Liul on 2018/9/7.
 */

public class HappynessReturn extends BaseReturn {
    public Integer spendHappinessCount;
    public Integer happy;
    public Integer normal;
    public Integer sad;

    public Integer getSpendHappinessCount() {
        return spendHappinessCount;
    }

    public void setSpendHappinessCount(Integer spendHappinessCount) {
        this.spendHappinessCount = spendHappinessCount;
    }

    public Integer getHappy() {
        return happy;
    }

    public void setHappy(Integer happy) {
        this.happy = happy;
    }

    public Integer getNormal() {
        return normal;
    }

    public void setNormal(Integer normal) {
        this.normal = normal;
    }

    public Integer getSad() {
        return sad;
    }

    public void setSad(Integer sad) {
        this.sad = sad;
    }
}
