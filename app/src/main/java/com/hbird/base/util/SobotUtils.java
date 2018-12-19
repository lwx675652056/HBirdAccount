package com.hbird.base.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.hbird.base.R;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.mvc.activity.KeFuActivity;
import com.hbird.base.mvc.activity.SobotNotificationClickReceiver;
import com.hbird.base.mvc.activity.SobotUnReadMsgReceiver;
import com.sobot.chat.SobotApi;
import com.sobot.chat.SobotUIConfig;
import com.sobot.chat.api.enumtype.SobotAutoSendMsgMode;
import com.sobot.chat.api.enumtype.SobotChatTitleDisplayMode;
import com.sobot.chat.api.model.ConsultingContent;
import com.sobot.chat.api.model.Information;
import com.sobot.chat.utils.ToastUtil;
import com.sobot.chat.utils.ZhiChiConstant;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by Liul on 2018/10/12.
 * 聊天客服相关  智齿客服
 */

public class SobotUtils {
    private static int enumType = 1;//0 默认,  1  自定义,  2  公司name;
    private static long sobot_show_history_ruler = 0;//显示多少分钟内的历史记录  10分钟 -24小时

//    private static SobotNotificationClickReceiver nClickReceiver;//点击通知以后发出的广播接收者
//    private static SobotUnReadMsgReceiver unReadMsgReceiver;//获取未读消息数的广播接收者

    public static void startSobot(Context context) {
        if (context == null) {
            return;
        }

        Information info = new Information();
        info.setAppkey("615a726d25e941309939e5ce3a6d3d89");



        //用户编号
        //注意：uid为用户唯一标识，不能传入一样的值
        String deviceId = Utils.getDeviceInfo(context);
        info.setUid(deviceId);
        //用户昵称，选填
        String name = SPUtil.getPrefString(context, CommonTag.KEFUNICKNAME, "");
        info.setUname(name);
        //用户姓名，选填
        info.setRealname(name);
        //用户电话，选填
        String phone = SPUtil.getPrefString(context, CommonTag.KEFUPHONE, "");
        info.setTel(phone);
        //用户邮箱，选填
        info.setEmail("");
        //自定义头像，选填
        String img = SPUtil.getPrefString(context, CommonTag.KEFUIMG, "");
        info.setFace(img);
        //用户QQ，选填
        info.setQq("");
        //用户备注，选填
        info.setRemark("");
        //访问着陆页标题，选填
        info.setVisitTitle("");
        //访问着陆页链接地址，选填
        info.setVisitUrl("");
        Map<String,String> customInfo = new HashMap<String, String>();
        customInfo.put("your key", "your value");
        //自定义用户资料
        info.setCustomInfo(customInfo);

//        自定义聊天页面标题样式--------------------------------------------------------------------
        //设置标题栏的背景图片，选填
        info.setTitleImgId(R.drawable.sobot_delete_hismsg_normal);
        //设置标题栏的背景颜色，如果背景颜色和背景图片都设置，则以背景图片为准，选填
        //info.setColor("");
        /**
         * 设置聊天界面标题显示模式
         * @param context 上下文对象
         * @param mode titile的显示模式
         *              SobotChatTitleDisplayMode.Default:显示客服昵称(默认)
         *              SobotChatTitleDisplayMode.ShowFixedText:显示固定文本
         *              SobotChatTitleDisplayMode.ShowCompanyName:显示console设置的企业名称
         * @param content 如果需要显示固定文本，需要传入此参数，其他模式可以不传
         */
        SobotApi.setChatTitleDisplayMode(context, SobotChatTitleDisplayMode.Default,"");

        //默认false：显示转人工按钮。true：智能转人工
        info.setArtificialIntelligence(false);
        //当未知问题或者向导问题显示超过(X)次时，显示转人工按钮。
        //注意：只有ArtificialIntelligence参数为true时起作用
        info.setArtificialIntelligenceNum(3);
        //是否使用语音功能 true使用 false不使用   默认为true
        info.setUseVoice(true);
        //是否使用机器人语音功能 true使用 false不使用 默认为false,需要付费才可以使用
        info.setUseRobotVoice(false);
        //客服模式控制 -1不控制 按照服务器后台设置的模式运行
        //1仅机器人 2仅人工 3机器人优先 4人工优先
        info.setInitModeType(-1);
        //设置机器人模式下输入关键字转人工
        HashSet<String> tmpSet = new HashSet<>();
        tmpSet.add("转人工");
        tmpSet.add("人工");
        info.setTransferKeyWord(tmpSet);

//        设置昵称字段是否显示：--------------------------------------------------------------------
//        info.setShowNikeNameTv(flag);//true 表示显示 false 表示不显示
//        昵称设置为显示时，可设置昵称字段是否为必填字段：
//        info.setShowNikeName(boolean);//true 表示必填 false 表示选填
        //设置 聊天界面右边气泡背景颜色
        SobotUIConfig.sobot_chat_right_bgColor = R.color.colorPrimary;

        //返回时是否弹出满意度评价
        info.setShowSatisfaction(false);
        //设置机器人编号(特定用户群体)
        //info.setRobotCode("your robot code");
        //转接类型(0-可转入其他客服，1-必须转入指定客服)
        info.setTranReceptionistFlag(1);
        //指定客服id
        //info.setReceptionistId("your Customer service id");
        //设置主题颜色  sobot_status_bar_color名字必须是这个 修改为自己的颜色
        info.setTitleImgId(R.color.sobot_status_bar_color);
        //sdk可以设置转接成功后自动发消息
        //设置发送模式
        //SobotAutoSendMsgMode.Default  默认 不发送
        //SobotAutoSendMsgMode.SendToRobot  只给机器人发送
        //SobotAutoSendMsgMode.SendToOperator   只给人工客服发送
        //SobotAutoSendMsgMode.SendToAll   全部发送
        //info.setAutoSendMsgMode(SobotAutoSendMsgMode.SendToAll.setContent("蜂鸟记账欢迎您"));

        /**
         * action:ZhiChiConstants.sobot_unreadCountBrocast 注册广播  外部已经注册 不需要再注册
         */
       /* IntentFilter filter = new IntentFilter();
        if (unReadMsgReceiver == null){
            unReadMsgReceiver = new SobotUnReadMsgReceiver();
        }
        filter.addAction(ZhiChiConstant.sobot_unreadCountBrocast);
        context.registerReceiver(unReadMsgReceiver, filter);*/

        /**
         * 控制显示历史聊天记录的时间范围
         * @param time  查询时间(例:100-表示从现在起前100分钟的会话)
         */
//        SobotApi.hideHistoryMsg(context,time);

        /**
         * 配置用户提交人工满意度评价后释放会话
         * @param context 上下文对象
         * @param flag true 表示释放会话  false  表示不释放会话
         */
        SobotApi.setEvaluationCompletedExit(context,false);

        //设置标题显示模式
        SobotApi.setChatTitleDisplayMode(context,
                SobotChatTitleDisplayMode.values()[enumType], "在线客服");

        SobotApi.startSobotChat(context, info);
        //设置是否开启(广播)消息提醒
        SobotApi.setNotificationFlag(context, true
                , R.mipmap.account_logo, R.mipmap.account_logo);
//        SobotApi.hideHistoryMsg(context, sobot_show_history_ruler);
//        SobotApi.setEvaluationCompletedExit(context, sobot_evaluationCompletedExit);
//        SobotApi.startSobotChat(context, info);

    }
}