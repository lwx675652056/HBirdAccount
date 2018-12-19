package com.hbird.base.mvc.bean;

import java.io.Serializable;
/**
 * Created by Liul on 2018/6/30.
 */
public class CeshiDateBean implements Serializable {

    private String typeName;//分类 名称
    private int total_binding;
    private int totals;//总共消费统计
    private boolean isFrist = true;

    public boolean isFrist() {
        return isFrist;
    }

    public void setFrist(boolean frist) {
        isFrist = frist;
    }


    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getTotal_binding() {
        return total_binding;
    }

    public void setTotal_binding(int total_binding) {
        this.total_binding = total_binding;
    }

    public int getTotals() {
        return totals;
    }

    public void setTotals(int totals) {
        this.totals = totals;
    }

}
