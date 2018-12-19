package com.hbird.base.mvc.bean;

import java.io.Serializable;

/**
 * Created by Liul on 2017/07/06.
 * 动态申请权限实体类
 */
public class MyPermissionBean implements Serializable {
    private boolean isHas = false;
    private String permissionName = "";
    private String permissionRequestInfo = "";

    public boolean isHas() {
        return isHas;
    }

    public void setHas(boolean has) {
        isHas = has;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionRequestInfo() {
        return permissionRequestInfo;
    }

    public void setPermissionRequestInfo(String permissionRequestInfo) {
        this.permissionRequestInfo = permissionRequestInfo;
    }
}
