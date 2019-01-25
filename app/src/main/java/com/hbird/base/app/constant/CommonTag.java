package com.hbird.base.app.constant;

/**
 * Created by Liul on 2018/7/5.
 * 存放公用的key
 */
public class CommonTag {

    //密码保护 - 是否开启手势密码 -开关按钮的key，默认为关闭手势密码
    public static final String SHOUSHI_PASSWORD_OPENED = "100";
    //记账提醒 开关按钮的key
    public static final String ACCOUNT_ALERT = "101";
    //声音开关按钮的key
    public static final String VOICE_KEY = "voice_key";
    //手势密码保存key
    public static final String GESTURE_PASSWORD = "GesturePassword";
    //保存登陆以后的token
    public static final String GLOABLE_TOKEN = "102";
    //保存登陆以后的token过期时间
    public static final String GLOABLE_TOKEN_EXPIRE= "103";
    //当前账号的登录方式（微信 手机号）
    public static final String CURRENT_LOGIN_METHOD = "current_login_method";

    //SharedPreference
    public static final String SPCACH = "LOCAL";
    //String strToken = DevRing.cacheManager().spCache("LOCAL").getString("X-AUTH-TOKEN", "");

    public static final String GESTURE_ERROR_NUM = "GESTURE_ERROR_NUM";
    public static final int MAX_GESTURE_ERROR_NUM = 5;

    //第二个参数是指你应用在微信开放平台上的AppID ,APP_SECRET
    public static final String WEIXIN_APP_ID = "wx3d767517f3b17ba5";
    public static final String APP_SECRET = "429196df43da68730d4d3d907fcc2c85";

    //存放闹铃提醒时间
    public static final String APP_ALERT_ACCOUNT = "app_alert_account";
    //第一次进入 indexFragement新手指引
    public static final String APP_FIRST_ZHIYIN = "app_first_zhiyin";
    //第一次进入 新手指引 选择类别
    public static final String APP_FIRST_ZHIYIN_ACCOUNT = "app_first_zhiyin_account";

    //第一次进入 新手指引 支出 收入
    public static final String APP_FIRST_ZHIYIN_ZZ = "app_first_zhiyin_zz";
    //如果是第一次则获取一次
    public static final String FIRST_TO_ACCESS = "first_to_access";

    //数据库第一次进入是否重新初始化
    public static final String DB_FIRST = "db_first";
    //离线同步间隔时间
    public static final String SYNINTERVAL = "synInterval";
    //个人userInfo
    public static final String USER_INFO_PERSION = "user_info_persion";
    //个人账户账本id
    public static final String ACCOUNT_BOOK_ID = "account_book_id";
    //个人账户头像 名字
    public static final String ACCOUNT_USER_HEADER = "account_user_header";
    public static final String ACCOUNT_USER_NICK_NAME = "account_user_nick_name";
    //个人账户 对应的 分账本id标识  区分标签类别
    public static final String ACCOUNT_AB_TYPEID = "account_ab_typeid";
    //个人账户下对应的所有账本
    public static final String ACCOUNT_BOOK_ID_ALL = "account_book_id_all";
    //当前标签版本号
    public static final String LABEL_VERSION = "label_version";
   /* //系统收入类目版本 allSysIncomeType
    public static final String ALL_SYS_INCOME_TYPE = "all_sys_income_type";
    //系统支出类目版本 allSysSpendType
    public static final String ALL_SYS_SPEND_TYPE = "all_sys_spend_type";
    //用户常用支出类目allUserCommUseSpendType
    public static final String ALL_USER_COMM_USE_SPEND_TYPE = "all_user_comm_use_spend_type";
    //用户常用收入类目allUserCommUseIncomeType
    public static final String ALL_USER_COMM_USE_INCOME_TYPE = "all_user_comm_use_income_type";
    //用户常用类目优先级 allUserCommUseTypePriority
    public static final String ALL_USER_COMM_USE_TYPE_PRIORITY = "all_user_comm_use_type_priority";*/
    //上次同步时间戳(下拉服务器数据到本地数据库时返的时间戳) 第一次调用以当前时间作为同步时间
    public static final String SYNDATE = "synDate";
    //下拉数据时 判断用户是否首次调用，卸载后安装按首次调用处理 true:首次 反之 null
    public static final String OFFLINEPULL_FIRST = "offlinepull_first";
    // 强制更新数据库
    public static final String MUST_UPDATE = "must_update";
    //下拉数据时 判断用户是否首次调用 是从重新登录 微信登录 注册界面跳转过来的判断
    public static final String OFFLINEPULL_FIRST_LOGIN = "offlinepull_first_login";
    // 第一次进入1.2.0
    public static final String FIRST_COME_1_2_0 = "first_come_1_2_0";
    //资产重置时间
    public static final String ZICHANCHONGZHI = "zichanchongzhi";
    //蜂鸟ID
    public static final String FENG_NIAO_ID  = "feng_niao_id";

    //资产相关
    //记账提醒 开关按钮的key
    public static final String XIANJIANTAG = "xianjiantag";
    public static final String ZHIFUBAOTAG = "zhifubaotag";
    public static final String WEIXINTAG = "weixintag";
    public static final String LICAITAG = "licaitag";
    public static final String SHEBAOTAG = "shebaotag";
    public static final String JIEJITAG = "jiejitag";
    public static final String GONGJIAOTAG = "gongjiaotag";
    public static final String CHUJIETAG = "chujietag";
    public static final String FUZHAITAG = "fuzhaitag";
    public static final String QITATAG = "qitatag";

    //客服昵称
    public static final String KEFUNICKNAME = "kefunickname";
    public static final String KEFUPHONE = "kefuphone";
    public static final String KEFUIMG = "kefuimg";
    //H5跳转邀请好友界面所需参数
    public static final String H5PRIMKEYIDS = "h5primkeyids";
    public static final String H5PRIMKEYNAME = "h5primkeyname";
    public static final String H5PRIMKEYZILIAO = "h5primkeyziliao";
    public static final String H5PRIMKEYPHONE = "h5primkeyphone";
    public static final String H5PRIMKEYWEIXIN = "h5primkeyweixin";
    public static final String H5PRIMKEYMONEY = "h5primkeymoney";
    //记录首页listView的Header是否被移除过
    public static final  String INDEX_LISTVIEW_HEADER = "index_listview_header";
    //当前账本 name
    public static final  String INDEX_CURRENT_ACCOUNT = "index_current_account";
    //当前账本ID
    public static final  String INDEX_CURRENT_ACCOUNT_ID= "index_current_account_id";
    // 我的--设置账本类型--账本类型ID
    public static final  String CURRENT_ACCOUNT_ID= "current_account_id";
    //当前账本类型
    public static final  String INDEX_CURRENT_ACCOUNT_TYPE= "index_current_account_type";
    //当前版本是 1普通账本还是 2场景账本
    public static final  String INDEX_TYPE_BUDGET= "index_type_budget";


}
