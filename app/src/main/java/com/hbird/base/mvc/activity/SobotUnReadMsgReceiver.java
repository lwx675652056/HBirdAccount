package com.hbird.base.mvc.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sobot.chat.utils.LogUtils;
import com.sobot.chat.utils.ZhiChiConstant;

/**
 * 获取未读消息的广播接收者  联系客服
 */
public class SobotUnReadMsgReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ZhiChiConstant.sobot_unreadCountBrocast.equals(intent.getAction())){
            int noReadNum = intent.getIntExtra("noReadCount", 0);
            String content = intent.getStringExtra("content");
            LogUtils.e("未读消息数是：" + noReadNum + "\t" + "最新一条消息内容是：" + content);

          /*  NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            //例如这个id就是你传过来的
            String id = intent.getStringExtra("id");
            id= "kefu";
            //homeActivity是你点击通知时想要跳转的Activity
            Intent playIntent = new Intent(context, homeActivity.class);
            playIntent.putExtra("id", id);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentTitle("蜂鸟客服消息"+"("+noReadNum+")")
                    .setContentText(content)
                    .setSmallIcon(R.mipmap.account_logo)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
//                    .setSubText(content);
            manager.notify(1, builder.build());*/
        }

    }
}