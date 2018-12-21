package com.hbird.base.mvc.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hbird.base.util.SobotUtils;
import com.sobot.chat.utils.ZhiChiConstant;

/**
 * 点击通知以后发出的广播接收者（联系客服）
 */
public class SobotNotificationClickReceiver  extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ZhiChiConstant.SOBOT_NOTIFICATION_CLICK.equals(intent.getAction())){
            //L.e("点击了通知发出的广播........................");
            //跳转到联系客服界面
            SobotUtils.startSobot(context);
        }
    }
}