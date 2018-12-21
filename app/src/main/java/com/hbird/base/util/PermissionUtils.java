package com.hbird.base.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.hbird.base.mvc.bean.MyPermissionBean;

import java.util.ArrayList;
import java.util.List;

import sing.common.util.LogUtil;

/**
 * Created by chenggs on 2017/6/21.
 */
public class PermissionUtils {

    public static List<MyPermissionBean> myPermissionsList = new ArrayList<MyPermissionBean>();

    public static List<MyPermissionBean> getMyPermissionsList() {
        return myPermissionsList;
    }

    public void setMyPermissionsList(List<MyPermissionBean> myPermissionsList) {
        this.myPermissionsList = myPermissionsList;
    }

    public static void initPermissions(Context mContext){
        MyPermissionBean myPermissionBean_storage = new MyPermissionBean();
        myPermissionBean_storage.setPermissionName(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        myPermissionBean_storage.setPermissionRequestInfo("存储");
        myPermissionsList.add(myPermissionBean_storage);

        MyPermissionBean myPermissionBean_camera = new MyPermissionBean();
        myPermissionBean_camera.setPermissionName(Manifest.permission.CAMERA);
        myPermissionBean_camera.setPermissionRequestInfo("相机");
        myPermissionsList.add(myPermissionBean_camera);

       /* MyPermissionBean myPermissionBean_setting = new MyPermissionBean();
        myPermissionBean_setting.setPermissionName(Manifest.permission.WRITE_SETTINGS);
        myPermissionBean_setting.setPermissionRequestInfo("系统设置");
        myPermissionsList.add(myPermissionBean_setting);*/


        checkPermissions(mContext);
    }

    /**
     * 检查权限
     * @param mContext
     * @return
     */
    public static boolean checkPermission(Context mContext){
        //是否需要动态请求权限
        if (Build.VERSION.SDK_INT >= 23) {
            if(!Settings.canDrawOverlays(mContext)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                mContext.startActivity(intent);
                return false;
            }
        }
        return  true;
    }

    public static boolean checkPermissions(Context mContext){
        //是否需要动态请求权限
        boolean isNeedPermission = false;
        if(Build.VERSION.SDK_INT >= 23){
            for(int i =0; i<myPermissionsList.size();i++){
                int permission = ContextCompat.checkSelfPermission(mContext, myPermissionsList.get(i).getPermissionName());
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    myPermissionsList.get(i).setHas(false);
                    isNeedPermission = true;
                }else{
                    myPermissionsList.get(i).setHas(true);
                }
            }
        }
        return isNeedPermission;
    }

    public static void shouldRequest(Activity mContext){
        try {
            for(int i =0; i<myPermissionsList.size();i++){
                if(myPermissionsList!=null && !myPermissionsList.get(i).isHas()){
                    LogUtil.e("liul---需要获取权限--->"+myPermissionsList.get(i).getPermissionName());
                    if (ActivityCompat.shouldShowRequestPermissionRationale(mContext, myPermissionsList.get(i).getPermissionName())) {
                        explainDialog(mContext,myPermissionsList,i);
                        return;
                    }else{
                        //请求权限
                        ActivityCompat.requestPermissions(mContext, new String[]{myPermissionsList.get(i).getPermissionName()}, i+1);
                        return;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void explainDialog(final Activity mContext, final List<MyPermissionBean> permissionsList, final int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("应用需要获取您的"+permissionsList.get(i).getPermissionRequestInfo()+"权限,禁止将导致应用不能正常运行，是否授权？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //请求权限
                        ActivityCompat.requestPermissions(mContext, new String[]{permissionsList.get(i).getPermissionName()}, i+1);
                    }
                }).setNegativeButton("取消", null)
                .create().show();
    }

}
