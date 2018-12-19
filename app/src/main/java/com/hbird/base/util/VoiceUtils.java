package com.hbird.base.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.hbird.base.R;

/**
 * Created by Liul on 2018/10/24.
 */

public class VoiceUtils {
    private static VoiceUtils instance = null;
    private static Context context;
    private static SoundPool soundPool;
    private static int changgui01;
    private static int changgui02;
    private static int jizhang;
    private static int jizhangfinish;
    private static int typevoice;

    private VoiceUtils() {
    }

    public static VoiceUtils newInstance(Context c) {
        context = c;
        if (null == instance) {
            instance = new VoiceUtils();
            soundPool = new SoundPool(3, AudioManager.STREAM_SYSTEM, 0);
            changgui01 = soundPool.load(context, R.raw.changgui01, 1);
            changgui02 = soundPool.load(context, R.raw.changgui02, 1);
            jizhang = soundPool.load(context, R.raw.jizhang, 1);
            jizhangfinish = soundPool.load(context, R.raw.jizhangfinish, 1);
            typevoice = soundPool.load(context, R.raw.typevoice, 1);
        }
        return instance;
    }

    public void playVoice(int resid) {
        int soundID = soundPool.load(context, R.raw.jizhang, 1);// 这个加载需要时间，用提前加载好的
        switch (resid) {
            case R.raw.changgui01:
                soundID = changgui01;
                break;
            case R.raw.changgui02:
                soundID = changgui02;
                break;
            case R.raw.jizhang:
                soundID = jizhang;
                break;
            case R.raw.jizhangfinish:
                soundID = jizhangfinish;
                break;
            case R.raw.typevoice:
                soundID = typevoice;
                break;
            default:
                soundID = changgui01;
                break;
        }

        soundPool.play(
                soundID,
                0.9f,   //左耳道音量【0~1】
                0.9f,   //右耳道音量【0~1】
                0,     //播放优先级【0表示最低优先级】
                0,     //循环模式【0表示循环一次，-1表示一直循环，其他表示数字+1表示当前数字对应的循环次数】
                1);     //播放速度【1是正常，范围从0~2】
    }
}