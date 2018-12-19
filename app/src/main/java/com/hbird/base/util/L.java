package com.hbird.base.util;

import android.text.TextUtils;
import android.util.Log;

import com.hbird.base.app.constant.UrlConstants;


/**
 * 打印日志
 * 通过isDebug控制是否要打印
 * Created by Liul on 2018/06/28.
 */

public class L {

    private static String className;//类名
    private static String methodName;//方法名
    private static int lineNumber;//行数
    private static boolean isDebug = true;//是否是debug模式

    private L() {

    }

    public static boolean isDebuggable() {
        if(UrlConstants.IS_RELEASE){
            //正式环境 关闭log日志
            isDebug = false;
        }else {
            //测试环境 打开日志
            isDebug = true;
        }
        return isDebug;
    }

    private static String createLog(String log) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(methodName);
        buffer.append("(").append(className).append(":").append(lineNumber).append(")");
        buffer.append(log);
        return buffer.toString();
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }


    public static void e(String message) {
        if (!isDebuggable())
            return;


        getMethodNames(new Throwable().getStackTrace());
        Log.e(className, createLog(message));
    }


    public static void i(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.i(className, createLog(message));
    }

    public static void d(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.d(className, createLog(message));
    }

    public static void v(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.v(className, createLog(message));
    }

    public static void liu(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.w(className, createLog(message));
    }

    public static void liul(String msg){
        if (!isDebuggable())
            return;
        if (msg == null|| TextUtils.isEmpty(msg))
            msg = "空";
        Log.e("Liul", msg);
    }
}
