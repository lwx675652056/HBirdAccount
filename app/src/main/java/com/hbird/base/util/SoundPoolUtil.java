package com.hbird.base.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

/**
 * @author: LiangYX
 * @ClassName: SoundPoolUtil
 * @date: 2018/12/10 20:18
 * @Description: 声音工具
 */
public class SoundPoolUtil {
    private static SoundPoolUtil soundPoolUtil;
    private SoundPool soundPool;

    //单例模式
    public static SoundPoolUtil getInstance(Context context,int resId) {
        if (soundPoolUtil == null)
            soundPoolUtil = new SoundPoolUtil(context,resId);
        return soundPoolUtil;
    }

    private SoundPoolUtil(Context context,int resId) {
        soundPool = new SoundPool(3, AudioManager.STREAM_SYSTEM, 0);
        //加载音频文件
        soundPool.load(context, resId, 1);
    }

    public void play(int number) {
        Log.d("tag", "number " + number);
        //播放音频
        soundPool.play(number, 1, 1, 0, 0, 1);
    }
}