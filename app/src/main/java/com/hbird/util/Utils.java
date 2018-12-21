package com.hbird.util;

import android.content.Context;

import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.util.SPUtil;
import com.hbird.base.util.VoiceUtils;

public class Utils {

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
}
