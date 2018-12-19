package com.ljy.devring.other.permission;

/**
 * author:  admin
 * date:    2018/06/29
 * description: 权限请求的结果回调
 */

public interface PermissionListener {
    void onGranted(String permissionName);//成功授权

    void onDenied(String permissionName);//被拒绝

    void onDeniedWithNeverAsk(String permissionName);//被拒绝，且勾选了“不再提示”
}
