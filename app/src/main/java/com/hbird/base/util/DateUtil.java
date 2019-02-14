package com.hbird.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Liul on 2018/6/29.
 */


public class DateUtil {
    public DateUtil() {
    }

    public static String formatDate(String pattern, Date date) {
        SimpleDateFormat fmt = new SimpleDateFormat(pattern);
        if (date == null) {
            date = new Date();
        }

        return fmt.format(date);
    }


    public static boolean compareTime(long cTime, long section) {
        long nNowTime = getNowTime();
        return nNowTime - cTime > section;
    }

    public static long getNowTime() {
        Date nNowTimeDate = new Date();
        return nNowTimeDate.getTime();
    }

    public static String getRefrashTime() {
        String month = formatDate("MM月", (Date) null);
        String day = formatDate("dd日", (Date) null);
        String time = formatDate("HH:mm", (Date) null);
        if (month.startsWith("0")) {
            month = (String) month.subSequence(1, month.length());
        }

        if (day.startsWith("0")) {
            day = (String) day.subSequence(1, day.length());
        }

        String date = month + day + " " + time;
        return date;
    }

    public static String getWeekday(String date) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdw = new SimpleDateFormat("EEEE");
        Date d = null;

        try {
            d = sd.parse(date);
        } catch (ParseException var5) {
            var5.printStackTrace();
        }

        return sdw.format(d);
    }

    /**
     * 日期转星期
     *
     * @param datetime
     * @return
     */
    public static String dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 将2018/07/11转换为long毫秒值
     */
    public static Long dateToLong(String date) {
        long l = 0L;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date d = sdf.parse(date);
            l = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return l;
    }

    /**
     * 将2018年07月11日 转换为long毫秒值
     */
    public static Long dateToLongs(String date) {
        long l = 0L;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        try {
            Date d = sdf.parse(date);
            l = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return l;
    }

    //获取指定年，月的第一天
    public static String getMonthday2First(int year, int month) {
        //获取指定年月的第一天
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        //获取某月最小天数
        int firstDay = cal.getMinimum(Calendar.DATE);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String first = sdf.format(cal.getTime());
        //获取指定年月的最后一天;
        Date date = null;
        try {
            date = sdf.parse(first);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime();
        String s = String.valueOf(ts);
        return s;
    }

    //获取指定年，月的最后一天
    public static String getMonthday2Last(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DATE);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //默认为当天24点
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        /*//格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String last = sdf.format(cal.getTime());
        Date date = null;
        try {
            date = sdf.parse(last);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime();
        String s = String.valueOf(ts);*/

        return cal.getTimeInMillis() + "";
    }

    /*
     * 将时间转换为时间戳
     */
    public static Date dateToStamp(String s) throws ParseException {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
        //Date date = simpleDateFormat.parse(s);
        long millisecond = Long.parseLong(s);
        Date date = new Date(millisecond);
        return date;
    }

    public static String dateToStamp1(String s) throws ParseException{
        if (s == null){
            return "";
        }
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /**
     * Tue Jul 12 00:00:00 GMT+08:00 2016转为2016-07-12
     */
    public static Date dateTode(String s) throws ParseException {
        SimpleDateFormat sf1 = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy", Locale.ENGLISH);
        Date date = sf1.parse(s);
        return date;
    }

    public static Date getDay(Date chargeDate) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
        //Date date = simpleDateFormat.parse(s);
        Calendar cal = Calendar.getInstance();
        cal.setTime(chargeDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(long s,String pattern) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 计算两个时间戳之间相差的时间
     */
    public static long getDateDiff(long l1,long l2) {
        try {
            Date d1 = new Date(l1);
            Date d2 = new Date(l2);
            long diff = d1.getTime() - d2.getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            return days;
        } catch (Exception e) {
            return 0;
        }
    }
}