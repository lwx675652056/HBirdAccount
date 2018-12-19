package com.hbird.base.util.alarmClock;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.hbird.base.R;
import com.hbird.base.mvc.activity.AccountAlertActivity;
import com.hbird.base.mvc.activity.homeActivity;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.util.SPUtil;

/**
 * Created by liul on 2018/7/14.
 * 设置记账提醒  通知
 * 参阅 https://github.com/loonggg/Android-AlarmManagerClock
 */
public class LoongggAlarmReceiver extends BroadcastReceiver {
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        String msg = intent.getStringExtra("msg");
        boolean isopen = intent.getBooleanExtra("isopen",false);
        long intervalMillis = intent.getLongExtra("intervalMillis", 0);
        if (intervalMillis != 0) {
            AlarmManagerUtil.setAlarmTime(context, System.currentTimeMillis() + intervalMillis,
                    intent);
        }
        int flag = intent.getIntExtra("soundOrVibrator", 0);
        //跳转单独页面 震动提示 弹dialog
       /* Intent clockIntent = new Intent(context, ClockAlarmActivity.class);
        clockIntent.putExtra("msg", msg);
        clockIntent.putExtra("flag", flag);
        clockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(clockIntent);*/
       //发送指定通知 根据记账提醒是否被打开
       if(!isopen){
           //如果没被打开则不提示
           return;
       }
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //例如这个id就是你传过来的
        String id = intent.getStringExtra("id");
        id= "0";
        //homeActivity是你点击通知时想要跳转的Activity
        Intent playIntent = new Intent(context, homeActivity.class);
        playIntent.putExtra("id", id);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("蜂鸟记账")
                .setContentText(msg)
                .setSmallIcon(R.mipmap.account_logo)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
//                .setSubText("主人最迷人");
        manager.notify(1, builder.build());


    }


}
