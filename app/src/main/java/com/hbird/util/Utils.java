package com.hbird.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;

import com.hbird.base.R;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.mvc.bean.indexBaseListBean;
import com.hbird.base.mvp.model.entity.table.WaterOrderCollect;
import com.hbird.base.util.SPUtil;
import com.hbird.base.util.VoiceUtils;
import com.ljy.devring.util.ColorBar;

import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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


    public static void decoderBase64File(String base64Code, String savePath) throws Exception {
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
    public static String getHiddenPhone(String userName) {
        if (!TextUtils.isEmpty(userName) && userName.length() > 6) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < userName.length(); i++) {
                char c = userName.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }

            return sb.toString();
        } else {
            return userName;
        }
    }


    // 取渠道名
    public static String getChannelName(Context context) {
        String channelName = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelName = String.valueOf(applicationInfo.metaData.get("UMENG_CHANNEL"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channelName;
    }

    public static double to2Digit(double f) {
        BigDecimal bg = new BigDecimal(f);
        double a = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return a;
    }

    public static double to4Digit(double f) {
        BigDecimal bg = new BigDecimal(f);
        double a = bg.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
        return a;
    }

    public static String to2DigitString(double f) {
        BigDecimal bg = new BigDecimal(f);
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "";
    }

    //获取一年中总共有几周
    public static int getYearToWeek() {
        Date date = new Date();
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setMinimalDaysInFirstWeek(7);
        c.setTime(date);

        return c.get(Calendar.WEEK_OF_YEAR);
    }

    public static WaterOrderCollect toWaterOrderCollect(indexBaseListBean temp) {
        WaterOrderCollect bean = new WaterOrderCollect();
        bean.id = temp.getId();
        bean.money = temp.getMoney();
        bean.accountBookId = temp.getAccountBookId();
        bean.orderType = temp.getOrderType();
        bean.isStaged = temp.getIsStaged();
        bean.spendHappiness = temp.getSpendHappiness();
        bean.typePid = temp.getTypePid();
        bean.typePname = temp.getTypePname();
        bean.typeId = temp.getTypeId();
        bean.typeName = temp.getTypeName();
        bean.updateDate = new Date(temp.getUpdateDate());
        bean.createDate = new Date(temp.getCreateDate());
        bean.chargeDate = new Date(temp.getChargeDate());
        bean.createBy = temp.getCreateBy();
        bean.createName = temp.getCreateName();
        bean.updateBy = temp.getUpdateBy();
        bean.updateName = temp.getUpdateName();
        bean.remark = temp.getRemark();
        bean.icon = temp.getIcon();
        bean.userPrivateLabelId = temp.getUserPrivateLabelId();
        bean.reporterAvatar = temp.getReporterAvatar();
        bean.reporterNickName = temp.getReporterNickName();
        bean.abName = temp.getAbName();
        bean.assetsId = temp.getAssetsId();
        bean.assetsName = temp.getAssetsName();

//        indexBeen = new ArrayList<>();
//        if (temp.getIndexBeen() != null) {
//            for (int i = 0; i < temp.getIndexBeen().size(); i++) {
//                AccountDetailedBean.indexBean temp1 = new AccountDetailedBean.indexBean();
//                temp1.setDayIncome(temp.getIndexBeen().get(i).getDayIncome());
//                temp1.setDaySpend(temp.getIndexBeen().get(i).getDaySpend());
//                temp1.setDayTime(temp.getIndexBeen().get(i).getDayTime());
//                indexBeen.add(temp1);
//            }
//        }
        return bean;
    }

    protected static boolean useThemestatusBarColor = false;//是否使用特殊的标题栏背景颜色，android5.0以上可以设置状态栏背景色，如果不使用则使用透明色值
    protected static boolean useStatusBarColor = false;//是否使用状态栏文字和图标为暗色，如果状态栏采用了白色系，则需要使状态栏和图标为暗色，android6.0以上可以设置
    public static void setStatusBar(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = context.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            //根据上面设置是否对状态栏单独设置颜色
            if (useThemestatusBarColor) {
                context.getWindow().setStatusBarColor(context.getResources().getColor(R.color.black));
            } else {
                context.getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = context.getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !useStatusBarColor) {//android6.0以后可以对状态栏文字颜色和图标进行修改
            context.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}
