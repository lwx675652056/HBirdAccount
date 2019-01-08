package com.hbird.common;

import android.os.Environment;

public class Constants {

    public static final String BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/accounthbird";
    public static final String IMAGE_PATH = BASE_PATH + "/image";


    public static final String FENGFENG_ID = "fengfeng_id";// 保存的丰丰票ID
    public static final String MY_ACCOUNT = "my_account";// 保存的我的已添加所有账户，记账用
    public static final String CHOOSE_ACCOUNT_ID = "choose_account_id";// 记账最后一次选择的账户id，不选为空
    public static final String CHOOSE_ACCOUNT_DESC = "choose_account_desc";// 记账最后一次选择的账户描述，不选为空
    public static final String CHOOSE_ACCOUNT_ICON = "choose_account_icon";// 记账最后一次选择的账户图标，不选为空
    public static final String CHOOSE_ASSETS = "choose_assets";// 保存的资产

    public static final String START_INTENT_A = "start_intent_a";

    public static final String ASSETS_TIME = "assets_time";// 资产重置时间

    public static final String  USER_ID = "user_id";// 用户id
    public static final String  REGISTER_DATE = "register_date";// 注册时间
    public static final String  _FILLED_IN = "_filled_in";// 是否填写过邀请码

    public static final String USER_HEAD = "account_user_header";// 个人信息  头像
    public static final String USER_NICKNAME = "account_user_nick_name";// 个人信息  昵称

}
