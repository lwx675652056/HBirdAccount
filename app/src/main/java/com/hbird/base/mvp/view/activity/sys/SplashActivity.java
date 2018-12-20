package com.hbird.base.mvp.view.activity.sys;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.hbird.base.R;
import com.hbird.base.app.GestureUtil;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.mvc.activity.homeActivity;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.ReturnBean.AccountZbBean;
import com.hbird.base.mvc.bean.ReturnBean.CheckVersionReturn;
import com.hbird.base.mvc.bean.ReturnBean.SystemBiaoqReturn;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvc.view.dialog.updateDialog;
import com.hbird.base.mvc.widget.DownLoadDialog;
import com.hbird.base.mvp.presenter.sys.SplashPresenter;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.mvp.view.activity.login.GestureLoginActivity;
import com.hbird.base.mvp.view.activity.login.loginActivity;
import com.hbird.base.mvp.view.iview.sys.ISplashView;
import com.hbird.base.util.DBUtil;
import com.hbird.base.util.PermissionUtils;
import com.hbird.base.util.SPUtil;
import com.hbird.base.util.Utils;
import com.hbird.base.util.alarmClock.AlarmManagerUtil;
import com.ljy.devring.DevRing;
import com.ljy.devring.base.activity.IBaseActivity;
import com.ljy.devring.util.FileUtil;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import sing.common.util.DownLoadUtil;
import sing.util.LogUtil;
import sing.util.SharedPreferencesUtil;
import sing.util.ToastUtil;

public class SplashActivity extends BaseActivity<SplashPresenter> implements ISplashView, IBaseActivity {
    @BindView(R.id.iv_gif_img)
    ImageView mImg;
    @BindView(R.id.img_bottom)
    ImageView mBImg;

    private final int SPLASH_DISPLAY_LENGHT = 1000; // 两秒后进入系统
    private final int SPLASH_FIRST_TIME = 700; // 两秒后进入系统
    private int once = 0;
    private int firsts = 0;
    private int REQUEST_CODE_UNKNOWN_APP = 999;
    private String appVersion;
    private String token;
    private CheckVersionReturn.ResultBean result;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView(Bundle bundle) {
        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        showTranslate(bounceInterpolator);
        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                BounceInterpolator bounceInterpolator = new BounceInterpolator();
                showTranslate2(bounceInterpolator);
            }
        }, SPLASH_FIRST_TIME);
    }

    private void showTranslate(Interpolator interpolator) {
        //创建平移动画对象
        TranslateAnimation translateAnimation = new TranslateAnimation(
                //参数：Animation.RELATIVE_TO_SELF（先对自身）
                //Animation.RELATIVE_TO_PARENT（相对父容器）
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_PARENT, 0.326f,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_PARENT, 0
        );
        //设置持续的时间
        translateAnimation.setDuration(1000);
        //设置循环的次数
        translateAnimation.setRepeatCount(0);
        //设置重复的模式，Animation.REVERSE：会进行一次倒序播放
        translateAnimation.setFillAfter(true);
        //设置插值器
        translateAnimation.setInterpolator(interpolator);
        //设置动画
        mImg.startAnimation(translateAnimation);
    }

    private void showTranslate2(Interpolator interpolator) {
        //创建平移动画对象
        TranslateAnimation translateAnimation = new TranslateAnimation(
                //参数：Animation.RELATIVE_TO_SELF（先对自身）
                //Animation.RELATIVE_TO_PARENT（相对父容器）
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_PARENT, -0.15f
        );
        //设置持续的时间
        translateAnimation.setDuration(700);
        //设置循环的次数
        translateAnimation.setRepeatCount(0);
        //设置重复的模式，Animation.REVERSE：会进行一次倒序播放
        translateAnimation.setFillAfter(true);
        //设置插值器
        translateAnimation.setInterpolator(interpolator);
        //设置动画
        mBImg.startAnimation(translateAnimation);
    }

    private void setIsContinue() {
        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                Intent mainIntent = null;
                String strToken = DevRing.cacheManager().spCache(CommonTag.SPCACH).getString(CommonTag.GLOABLE_TOKEN, "");
                if (!strToken.equals("")) {//如果存在token则直接显示主界面
                    boolean isOpen = DevRing.cacheManager().spCache(CommonTag.SPCACH).getBoolean(CommonTag.SHOUSHI_PASSWORD_OPENED, false);
                    if (isOpen && !GestureUtil.beOverMaxErrNum()) {//如果当前是开启状态，并且没有超过最大错误次数，打开手势验证
                        mainIntent = new Intent(SplashActivity.this, GestureLoginActivity.class);
                        mainIntent.putExtra("comfrom", "splash");
                    } else if (isOpen && GestureUtil.beOverMaxErrNum()) {//如果当前是开启状态，并且没有超过最大错误次数，登录验证
                        mainIntent = new Intent(SplashActivity.this, loginActivity.class);
                        //这中情况下，如果登录成功，需要清空手势密码
                        mainIntent.putExtra("comfrom", "splash_overmaxnum");
                    } else {
//                        mainIntent = new Intent(SplashActivity.this, homeActivity.class);
                        getMyAccount();// 检查账本，不然覆盖安装会存在之前的账号
                        return;
                    }
                } else {//否则显示登录界面
                    mainIntent = new Intent(SplashActivity.this, loginActivity.class);
                }
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }

        }, SPLASH_DISPLAY_LENGHT);
    }

    @Override
    protected void initData(Bundle bundle) {
        token = SPUtil.getPrefString(this, CommonTag.GLOABLE_TOKEN, "");
        //启动页类目更新检查/获取同步间隔
        boolean p = SPUtil.getPrefBoolean(this, CommonTag.FIRST_TO_ACCESS, true);

        setCheckChargeType();

        checkPermissionAndFileCheck();
        //判断闹铃的状态 如果打开则重新开启闹铃提醒
        boolean opens = SPUtil.getPrefBoolean(SplashActivity.this, CommonTag.ACCOUNT_ALERT, false);
        if (opens) {
            String alertTime = SPUtil.getPrefString(this, com.hbird.base.app.constant.CommonTag.APP_ALERT_ACCOUNT, "");
            if (alertTime != null && alertTime.length() > 0) {
                String[] times = alertTime.split(":");
                AlarmManagerUtil.setAlarm(SplashActivity.this, 0, Integer.parseInt(times[0]), Integer.parseInt
                        (times[1]), 0, 0, "今天花了多少钱呢？记一笔账吧。", 0, opens);
            }
        }
    }

    private void setCheckChargeType() {
        String lableVersion = SPUtil.getPrefString(this, CommonTag.LABEL_VERSION, "");
        if (TextUtils.isEmpty(token)) {
            //token为空则不调用此接口
            return;
        }
        NetWorkManager.getInstance().setContext(this)
                .postCheckChargeType(lableVersion, token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        SystemBiaoqReturn b1 = (SystemBiaoqReturn) b;
                        //获取到所有常用收入支出类目 （并插入到本地数据库）
                        if (null != b1 && b1.getResult() != null) {
                            setTypesToLocalDB(b1);
                            SPUtil.setPrefBoolean(SplashActivity.this, CommonTag.FIRST_TO_ACCESS, false);
                        }
                    }

                    @Override
                    public void onError(String s) {

                    }
                });
    }

    private void checkPermissionAndFileCheck() {
        PermissionUtils.initPermissions(this);
        if (!PermissionUtils.checkPermissions(this)) {
            //L.liul("Liu---不需要获取权限---");
            setUpDates();
            //setIsContinue();
        } else {
            //L.liul("Liu---需要获取权限---");
            PermissionUtils.shouldRequest(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setUpDates() {
        //判断是否需要更新
        appVersion = Utils.getVersion(this);
        if (!TextUtils.isEmpty(appVersion)) {
            updates(appVersion, token);
            return;
        }
    }

    private String url;

    private void updates(final String version, String token) {
        NetWorkManager.getInstance().setContext(this).
                getCheckVersion(token, version, new NetWorkManager.CallBack() {

                    @Override
                    public void onSuccess(BaseReturn b) {
                        CheckVersionReturn b1 = (CheckVersionReturn) b;
                        if (null != b1.getResult()) {
                            result = b1.getResult();
                            String ver = result.getVersion();
                            url = result.getUrl();
                            if (ver.compareTo(version) > 0) {
                                downLoadApp(result);
                            } else {// 当前已经是最新版本了
                                setIsContinue();
                            }
                        } else { // 当前已经是最新版本了
                            setIsContinue();
                        }
                    }

                    @Override
                    public void onError(String s) {
                        //当前的情况是没有网 先登录再说
                        //showMessage(s);
                        setIsContinue();
                    }
                });
    }

    private void downLoadApp(final CheckVersionReturn.ResultBean result) {
        //执行下载操作
        new updateDialog(this).builder()
                .setMsg(result.getUpdateLog())
                .setUpdateButton(view -> {
                    SPUtil.setPrefString(this, com.hbird.base.mvc.global.CommonTag.UPDATE_URL, url);
                    downLoad();
                })
                .setCancleButton(view -> {
                    int status = result.getInstallStatus();
                    if (status == 1) { //强制更新  否则退出程序
                        DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).put(com.hbird.base.mvc.global.CommonTag.GLOABLE_TOKEN, "");
                        DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).put(com.hbird.base.mvc.global.CommonTag.GLOABLE_TOKEN_EXPIRE, "");
                        DevRing.activityListManager().killAll();
                        finish();
                        return;
                    }
                }).show();
    }

    DownLoadDialog dialog;
    private void downLoad() {
        dialog = new DownLoadDialog(this );

        File mFileSave = FileUtil.getFile(FileUtil.getExternalCacheDir(this), "fengniao.apk");
        String url = SPUtil.getPrefString(this, com.hbird.base.mvc.global.CommonTag.UPDATE_URL, "");

        DownLoadUtil util = new DownLoadUtil(this,url,mFileSave.getPath());
        util.downLoadFile(new DownLoadUtil.Callback() {
            @Override
            public void success(String url, String localPath) {
                dialog.dismiss();
                sing.common.util.Utils.installApk(SplashActivity.this,localPath);
                DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).put(com.hbird.base.mvc.global.CommonTag.GLOABLE_TOKEN, "");
                DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).put(com.hbird.base.mvc.global.CommonTag.GLOABLE_TOKEN_EXPIRE, "");
                DevRing.activityListManager().killAll();
                finish();
            }

            @Override
            public void failure(String url, String errorMsg) {
                ToastUtil.showShort("下载失败:" + errorMsg);
                dialog.dismiss();
                DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).put(com.hbird.base.mvc.global.CommonTag.GLOABLE_TOKEN, "");
                DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).put(com.hbird.base.mvc.global.CommonTag.GLOABLE_TOKEN_EXPIRE, "");
                DevRing.activityListManager().killAll();
                finish();
            }

            @Override
            public void process(long current, long total) {
                dialog.start(current,total);
                sing.common.util.LogUtil.e("当前已下载：" + current);
            }
        });
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Build.VERSION.SDK_INT >= 23) {
            switch (requestCode) {
                case 1:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        //允许
                        PermissionUtils.getMyPermissionsList().get(0).setHas(true);
                        checkPermissionAndFileCheck();
                    } else {
                        //不允许也要开启
                        firsts = firsts + 1;
                        if (firsts == 1) {
                            PermissionUtils.getMyPermissionsList().get(0).setHas(true);
                            checkPermissionAndFileCheck();
                        } else {
                            //坚决不开启 放行
                            setUpDates();
                        }
                    }
                    break;
                case 2:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        PermissionUtils.getMyPermissionsList().get(1).setHas(true);
                        checkPermissionAndFileCheck();
                    } else {
                        once = once + 1;
                        //如果第一次点击了取消或禁止  继续检查 第二次判断则直接跳过检查 （权限不打开 某些功能不能正常使用）
                        if (once == 1) {
                            checkPermissionAndFileCheck();
                        } else {
                            setUpDates();
                        }
                    }
                    break;
            }
        }
    }

    private void setTypesToLocalDB(SystemBiaoqReturn b1) {
        SystemBiaoqReturn.ResultBean result = b1.getResult();
        String labelVersion = result.getLabelVersion();
        SPUtil.setPrefString(this, CommonTag.LABEL_VERSION, labelVersion);
        //系统标签类目
        if (null != result.getLabel()) {
            //个人userInfo
            Integer userInfoId = result.getLabel().get(0).getUserInfoId();
            SPUtil.setPrefString(this, CommonTag.USER_INFO_PERSION, userInfoId + "");
            for (int i = 0; i < result.getLabel().size(); i++) {
                SystemBiaoqReturn.ResultBean.LabelBean bean = result.getLabel().get(i);
                // 收入
                if (bean.getIncome() != null) {
                    SharedPreferencesUtil.put("userId_" + bean.getUserInfoId() + "abTypeId_" + bean.getAbTypeId() + "type_income", bean.getIncome().toString());
                }

                // 支出
                if (bean.getSpend() != null) {
                    SharedPreferencesUtil.put("userId_" + bean.getUserInfoId() + "abTypeId_" + bean.getAbTypeId() + "type_spend", bean.getSpend().toString());
                }

                LogUtil.e("label = " + result.getLabel().toString());

                List<SystemBiaoqReturn.ResultBean.LabelBean.SpendBean> spendList = result.getLabel().get(i).getSpend();
                Integer abTypeId = result.getLabel().get(i).getAbTypeId();
                DBUtil.insertAllUserCommUseSpendToLocalDB(spendList, abTypeId, userInfoId);
                List<SystemBiaoqReturn.ResultBean.LabelBean.IncomeBean> incomeList = result.getLabel().get(i).getIncome();
                DBUtil.insertAllUserCommUseIncomeToLocalDB(incomeList, abTypeId, userInfoId);
            }
        }
        //离线时间同步间隔 ( 单位 ms 1天 )--存储到本地SP
        String synInterval = result.getSynInterval() + "";
        SPUtil.setPrefString(this, CommonTag.SYNINTERVAL, synInterval);
        //个人账户账本id
        List<SystemBiaoqReturn.ResultBean.AbsBean> abs = result.getAbs();
        Set<String> set = new LinkedHashSet<String>();
        set.clear();
        if (null != abs) {
            for (int i = 0; i < abs.size(); i++) {
                String accountBookId = abs.get(i).getId() + "";
                if (i == 0) {
                    //默认账本id
                    String s = SPUtil.getPrefString(this, com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, "");
                    boolean firstGuide = SPUtil.getPrefBoolean(this, com.hbird.base.app.constant.CommonTag.APP_FIRST_ZHIYIN, true);
                    if (firstGuide) {
                        SPUtil.setPrefString(this, com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, accountBookId);
                        SPUtil.setPrefString(this, com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_ID, accountBookId);
                        SPUtil.setPrefInt(this, com.hbird.base.app.constant.CommonTag.ACCOUNT_AB_TYPEID, abs.get(i).getAbTypeId());
                    } else {
                        if (TextUtils.isEmpty(s)) {
                            SPUtil.setPrefString(this, com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, accountBookId);
                            SPUtil.setPrefString(this, com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_ID, accountBookId);
                            SPUtil.setPrefInt(this, com.hbird.base.app.constant.CommonTag.ACCOUNT_AB_TYPEID, abs.get(i).getAbTypeId());
                        }
                    }
                }

                set.add(accountBookId);
            }
        }
        if (null != set && set.size() > 0) {
            SPUtil.setStringSet(this, CommonTag.ACCOUNT_BOOK_ID_ALL, set);
        }
    }

    // 获取个人账本
    private void getMyAccount() {
        NetWorkManager.getInstance().setContext(this)
                .getMyAccounts(token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        AccountZbBean b1 = (AccountZbBean) b;
                        if (null != b1.getResult()) {
                            Set<String> set = new LinkedHashSet<>();
                            set.clear();
                            for (int i = 0; i < b1.getResult().size(); i++) {
//                                ZhangBenMsgBean bean = new ZhangBenMsgBean();
//                                bean.setZbImg(b1.getResult().get(i).getIcon());
//                                bean.setZbName(b1.getResult().get(i).getAbName());
//                                bean.setZbType(b1.getResult().get(i).getAbTypeName());
//                                bean.setZbUTime(b1.getResult().get(i).getUpdateDate() + "");
//                                bean.setId(b1.getResult().get(i).getId() + "");
                                set.add(b1.getResult().get(i).getId() + "");
                            }
                            if (null != set && set.size() > 0) {
                                SPUtil.setStringSet(SplashActivity.this, com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID_ALL, set);
                            }

                            String accountId = SPUtil.getPrefString(SplashActivity.this, com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_ID, "");
                            String abtypeId = SPUtil.getPrefString(SplashActivity.this, com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_TYPE,  "");

                            if (b1.getResult() != null && b1.getResult().size() > 0) {
                                if (TextUtils.isEmpty(accountId) || !set.contains(accountId) || TextUtils.isEmpty(abtypeId)) {
                                    AccountZbBean.ResultBean bean = b1.getResult().get(b1.getResult().size() - 1);// 默认显示最后一个，默认账本
                                    SPUtil.setPrefInt(SplashActivity.this, com.hbird.base.app.constant.CommonTag.ACCOUNT_AB_TYPEID, bean.getAbTypeId());
                                    SPUtil.setPrefString(SplashActivity.this, com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT, bean.getAbName());
                                    SPUtil.setPrefString(SplashActivity.this, com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_ID, bean.getId() + "");
                                    SPUtil.setPrefString(SplashActivity.this, com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, bean.getId() + "");
                                    SPUtil.setPrefString(SplashActivity.this, com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_TYPE, bean.getAbTypeId() + "");
                                    SPUtil.setPrefString(SplashActivity.this, com.hbird.base.app.constant.CommonTag.INDEX_TYPE_BUDGET, bean.getTypeBudget() + "");
                                    SPUtil.setPrefString(SplashActivity.this, com.hbird.base.app.constant.CommonTag.CURRENT_ACCOUNT_ID, bean.getAbTypeId() + "");
                                }
                            }
                        }

                        Intent intent = new Intent(SplashActivity.this, homeActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(String s) {
                        Intent intent = new Intent(SplashActivity.this, homeActivity.class);
                        startActivity(intent);
                    }
                });
    }
}
