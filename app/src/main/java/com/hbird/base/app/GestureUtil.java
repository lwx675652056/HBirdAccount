package com.hbird.base.app;

import com.hbird.base.R;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.mvc.global.ACache;
import com.ljy.devring.DevRing;

/**
 * 手势密码错误次数相关处理类
 */
public class GestureUtil {

    /**
     * 判断手势密码是否超过最大错误次数
     * @return true =超过； false =未超过
     */
    public static boolean beOverMaxErrNum(){
        boolean bResult = false;
        int nErrorNum = DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).
                getInt(com.hbird.base.app.constant.CommonTag.GESTURE_ERROR_NUM, 0);
        if (nErrorNum >= CommonTag.MAX_GESTURE_ERROR_NUM){
            bResult = true;
        }
        return bResult;
    }

    public static void clearMaxErrNum(){
        //清零错误次数
        DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).
                put(com.hbird.base.app.constant.CommonTag.GESTURE_ERROR_NUM, 0);
    }

    /**
     * 清空手势密码（清空手势图形，清空错误次数）
     */
    public static void clearGesturePassword(){
        ACache aCache = ACache.get(RingApplication.getContextObject());
        aCache.clear();
        //清零错误次数
        DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).
                put(com.hbird.base.app.constant.CommonTag.GESTURE_ERROR_NUM, 0);
//        //得到当前用户的手势密码
//        byte[] gesturePassword = aCache.getAsBinary(com.hbird.base.mvc.global.CommonTag.GESTURE_PASSWORD);
//        //如果当前手势密码为空，则打开设置手势密码界面
//        if (gesturePassword==null || gesturePassword.length<=0){
//
//        }
    }

    /**
     * 得到当前的错误次数
     * @return
     */
    public static  int getCurErrNum(){
        int nErrorNum = DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).
                getInt(com.hbird.base.app.constant.CommonTag.GESTURE_ERROR_NUM, 0);
        return nErrorNum;
    }

    /**
     * 得到当前的错误次数
     * @return
     */
    public static  int getErrNum(){
        return com.hbird.base.app.constant.CommonTag.MAX_GESTURE_ERROR_NUM;
    }

    /**
     * 检查最大错误次数，检查的同时，是否更新错误次数
     * @return false 没超过最大允许错误次数
     *          true    超过最大允许错误次数
     */
    public static  boolean beOverMaxErrNumByAdd(){

        int nErrorNum = DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).
                getInt(com.hbird.base.app.constant.CommonTag.GESTURE_ERROR_NUM, 0);
        nErrorNum = nErrorNum + 1;
        DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).
                put(com.hbird.base.app.constant.CommonTag.GESTURE_ERROR_NUM, nErrorNum);

        if (nErrorNum >= com.hbird.base.app.constant.CommonTag.MAX_GESTURE_ERROR_NUM){
            return true;
        }else {
//            int lastChance = com.hbird.base.app.constant.CommonTag.MAX_GESTURE_ERROR_NUM - nErrorNum;
//            String strMsg = "您还有:" + lastChance + "次机会！";
            return false;
        }
    }
}
