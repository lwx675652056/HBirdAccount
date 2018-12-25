package com.hbird.base.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    // 两次点击按钮之间的点击间隔不能少于30毫秒
    private static final int MIN_CLICK_DELAY_TIME = 100;
    private static final int MIN_CLICK_TIME = 100;
    private static long lastClickTime;

    public static int dp2px(Context context, int dpValue) {
        return (int) context.getResources().getDisplayMetrics().density * dpValue;
    }

    public static int dpTopx(Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int px2dp(Context context, int px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }
    /**
     * 将手机号中间4到7位用星号代替
     * @param userName
     * @return
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


        //防止点击速度过快
    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }
    //防止点击速度过快
    public static boolean isFast2Click() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }
    /* 加减法计算器 */
    public static double sum(String str){
        String[] positive = str.split("\\+");
        double posSum = 0;
        double negSum = 0;
        for (String pos : positive) {
            if(pos.contains("-")){
                String[] negative = pos.split("\\-");
                Double temp = 0d;
                for (int i = 1; i < negative.length; i++) {
                    temp -= Double.parseDouble(negative[i]);
                }
                negSum += Double.parseDouble(negative[0]) + temp;
            }else{
                posSum += Double.parseDouble(pos);
            }
        }
        return posSum + negSum;
    }

    /**
     * 判断相机是否可用
     * 返回true 表示可以使用 返回false表示不可以使用
     */
    public static boolean cameraIsCanUse() {
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters(); //针对魅族手机
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        }
        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
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
    //获取一年中总共有几周
    public static int getYearToWeek(){
        Date date = new Date();
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setMinimalDaysInFirstWeek(7);
        c.setTime(date);

        return c.get(Calendar.WEEK_OF_YEAR);
    }
    public static int getWeeks(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);//设置周一为一周的第一天
        cal.setTime(date);
        int num = cal.get(Calendar.WEEK_OF_YEAR);
        return num;
    }

    /**
     * 根据周数获取日期 （传第几周过来 算出是哪几天 区间）
     */
    public static String getDateByWeeks(int weeks){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.WEEK_OF_YEAR, weeks); // 设置周数
        cal.set(Calendar.DAY_OF_WEEK, 2); // 1表示周日，2表示周一，7表示周六
        Date begin = cal.getTime();
        //Map<String,String> map = new HashMap<>();
        DateFormat fmt =new SimpleDateFormat("MM/dd");
        String begins = fmt.format(begin);
        //map.put("beginTime",begins);
        cal.add(Calendar.DATE,6);
        Date end = cal.getTime();
        String ends = fmt.format(end);
        //map.put("endTime",ends);
        return begins+"-"+ends;
    }
    /**
     * 根据具体年份周数获取日期范围
     * @param year
     * @param week
     * @param targetNum
     * @return
     */
    public static String getWeekDays(int year, int week, int targetNum) {


        // 计算目标周数
      /*  if (week + targetNum > 52) {
            year++;
            week += targetNum - 52;
        } else if (week + targetNum <= 0) {
            year--;
            week += targetNum + 52;
        } else {
            week += targetNum;
        }*/

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal=Calendar.getInstance();

        // 设置每周的开始日期
        cal.setFirstDayOfWeek(Calendar.SUNDAY);

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.WEEK_OF_YEAR, week);

        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        String beginDate = sdf.format(cal.getTime());

        cal.add(Calendar.DAY_OF_WEEK, 6);
        String endDate = sdf.format(cal.getTime());

        return beginDate + "-" + endDate;
    }


    //处理表情符号时的编解码方式（editorText）
    /**
     * 将输入的内容编码
     * @param content
     * @return
     */
    public static String encode(String content) {
        StringBuilder sb = new StringBuilder(content.length() * 3);
        for (char c : content.toCharArray()) {
            if (c < 256) {
                sb.append(c);
            } else {
                sb.append("\\u");
                sb.append(Character.forDigit((c >>> 12) & 0xf, 16));
                sb.append(Character.forDigit((c >>> 8) & 0xf, 16));
                sb.append(Character.forDigit((c >>> 4) & 0xf, 16));
                sb.append(Character.forDigit((c) & 0xf, 16));
            }
        }
        return sb.toString();
    }
    /**
     * 将取出内容解码
     * @param content
     * @return
     */
    public static String decode(String content) {
        final Pattern reUnicode = Pattern.compile("\\\\u([0-9a-zA-Z]{4})");
        Matcher sMatcher = reUnicode.matcher(content);
        StringBuffer sb = new StringBuffer(content.length());
        while (sMatcher.find()) {
            sMatcher.appendReplacement(sb,
                    Character.toString((char) Integer.parseInt(sMatcher.group(1), 16)));
        }
        sMatcher.appendTail(sb);
        return sb.toString();
    }


}
