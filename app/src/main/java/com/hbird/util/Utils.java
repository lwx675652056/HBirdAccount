package com.hbird.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;

import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.util.SPUtil;
import com.hbird.base.util.VoiceUtils;
import com.ljy.devring.util.ColorBar;

import java.io.FileOutputStream;
import java.lang.reflect.Method;

public class Utils {

    // 播放声音
    public static void playVoice(Context context, int resid) {
        boolean opens = SPUtil.getPrefBoolean(context, CommonTag.VOICE_KEY, true);
        if (opens) {
            try {
                VoiceUtils voiceUtils = VoiceUtils.newInstance(context);
                voiceUtils.playVoice(resid);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    //获取手机设备的唯一标识
    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }

            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            }
            return device_id;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取设备的信息
     */
    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    //获取版本号
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public static void decoderBase64File(String base64Code,String savePath) throws Exception {
        base64Code = base64Code.split(",")[1];// 去掉前面的 data:image/png;base64,xxxxxxxxxxxxxxx
        byte[] buffer = Base64.decode(base64Code, Base64.DEFAULT);
        FileOutputStream out = new FileOutputStream(savePath);
        out.write(buffer);
        out.close();
    }

    public static String getNumToNumber(Double d) {
        java.text.NumberFormat NF = java.text.NumberFormat.getInstance();
        NF.setGroupingUsed(false);//去掉科学计数法显示
        return NF.format(d);
    }


    public static void initColor(Activity activity, int color) {
        ColorBar.newColorBuilder()
                .applyNav(true)
                .navColor(color)
                .navDepth(0)
                .statusColor(color)
                .statusDepth(0)
                .build(activity)
                .apply();
    }

    /**
     * 将手机号中间4到7位用星号代替
     */
    public static String getHiddenPhone(String userName){
        if(!TextUtils.isEmpty(userName) && userName.length() > 6 ){
            StringBuilder sb  =new StringBuilder();
            for (int i = 0; i < userName.length(); i++) {
                char c = userName.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }

            return sb.toString();
        }else{
            return userName;
        }
    }
}
