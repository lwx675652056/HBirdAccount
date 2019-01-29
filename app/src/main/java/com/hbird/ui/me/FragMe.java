package com.hbird.ui.me;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.hbird.base.R;
import com.hbird.base.app.constant.UrlConstants;
import com.hbird.base.databinding.FragMeBinding;
import com.hbird.base.mvc.activity.AccountAlertActivity;
import com.hbird.base.mvc.activity.ActChooseAccountType;
import com.hbird.base.mvc.activity.AskFriendsActivity;
import com.hbird.base.mvc.activity.NotificationMessageActivity;
import com.hbird.base.mvc.activity.SettingsActivity;
import com.hbird.base.mvc.activity.ShaiChengJiuActivity;
import com.hbird.base.mvc.activity.SuggestFanKuiActivity;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.ReturnBean.CheckVersionReturn;
import com.hbird.base.mvc.bean.ReturnBean.FengMessageReturn;
import com.hbird.base.mvc.bean.ReturnBean.GeRenInfoReturn;
import com.hbird.base.mvc.bean.ReturnBean.HeaderInfoReturn;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvc.view.dialog.updateDialog;
import com.hbird.base.mvc.widget.DownLoadDialog;
import com.hbird.base.mvp.model.entity.table.WaterOrderCollect;
import com.hbird.base.util.DateUtils;
import com.hbird.base.util.SPUtil;
import com.hbird.base.util.SobotUtils;
import com.hbird.base.util.Util;
import com.hbird.common.Constants;
import com.hbird.ui.address.ActEditAddress;
import com.hbird.ui.info.ActEditInfo;
import com.hbird.util.Utils;
import com.ljy.devring.DevRing;
import com.ljy.devring.util.FileUtil;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.File;
import java.util.Date;

import sing.common.base.BaseFragment;
import sing.common.base.BaseViewModel;
import sing.common.util.DownLoadUtil;
import sing.common.util.LogUtil;
import sing.common.util.StatusBarUtil;
import sing.util.SharedPreferencesUtil;
import sing.util.ToastUtil;

import static java.lang.Integer.parseInt;

public class FragMe extends BaseFragment<FragMeBinding, BaseViewModel> {

    private int height;// 頂部红色View高度
    private FragMeData data;
    private String token;
    private String accountId;// 账本id
    private GeRenInfoReturn b1;// 个人信息
    private HeaderInfoReturn b1s;// 头部信息

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.frag_me;
    }

    @Override
    public int initVariableId() {
        return 0;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
//            StatusBarUtil.clearStatusBarDarkMode(getActivity().getWindow());
            StatusBarUtil.setStatusBarDarkTheme(getActivity(),false);// 导航栏白色字体

            data.setShowLine(false);
            data.setFengniaoId(-1);// 空值
            binding.scrollView.fullScroll(ScrollView.FOCUS_UP);//滑到顶部

            binding.flParent1.setVisibility(View.GONE);
            binding.flParent2.setVisibility(View.VISIBLE);

            // 调三个接口，获取个人信息、获取头部信息、获取丰丰通知数
            getUserInfo();
            getTopInfo();
            getMessage();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initData() {
        token = SPUtil.getPrefString(getActivity(), CommonTag.GLOABLE_TOKEN, "");
        accountId = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, "");

        data = new FragMeData();
        data.setShowLine(false);
        binding.setData(data);
        binding.setListener(new OnClick());

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.flParent1.getLayoutParams();
        params.height += StatusBarUtil.getStateBarHeight(getActivity());
        binding.flParent1.setLayoutParams(params);
        binding.flParent1.setPadding(0, StatusBarUtil.getStateBarHeight(getActivity()), 0, 0);
        binding.flParent2.setLayoutParams(params);
        binding.flParent2.setPadding(0, StatusBarUtil.getStateBarHeight(getActivity()), 0, 0);


        height = getResources().getDimensionPixelSize(R.dimen.dp_50_x);

        binding.scrollView.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> onScroll(scrollY));
    }

    public class OnClick {
        // 设置
        public void setting(View view) {
            Utils.playVoice(getActivity(), R.raw.changgui02);
            Intent intent5 = new Intent(getActivity(), SettingsActivity.class);
            if (null != b1) {
                intent5.putExtra("PHONE", b1.getResult().getMobile());
                intent5.putExtra("WEIXIN", b1.getResult().getWechatAuth());
            }
            startActivity(intent5);
        }

        // 个人信息
        public void userInfo(View view) {
            Utils.playVoice(getActivity(), R.raw.changgui02);
//            Intent intent1 = new Intent(getActivity(), PersionnalInfoActivity.class);
            Intent intent1 = new Intent(getActivity(), ActEditInfo.class);
            if (null != b1) {
                String json = new Gson().toJson(b1);
                intent1.putExtra("JSON", json);
                startActivityForResult(intent1, 1001);
            }
        }

        // 邀请的好友数
        public void friendsNum(View view) {
            Utils.playVoice(getActivity(), R.raw.changgui02);
            Intent intents = new Intent();
            intents.setClass(getActivity(), AskFriendsActivity.class);
            intents.putExtra("IDS", data.getPhone());
            intents.putExtra("NAME", data.getNickName());
            startActivity(intents);
        }

        // 本月记账天数
        public void monthNum(View view) {
            Utils.playVoice(getActivity(), R.raw.changgui02);
            Intent intent4 = new Intent(getActivity(), ShaiChengJiuActivity.class);
            String jsons = "";
            if (null != b1s) {
                jsons = new Gson().toJson(b1s);
                intent4.putExtra("JSON", jsons);
            }
            intent4.putExtra("HEADYRL", data.getHeadUrl());
            intent4.putExtra("NAME", data.getName());
            intent4.putExtra("JZDAYS", data.getMonthNum().substring(0, data.getMonthNum().indexOf("/")));
            intent4.putExtra("ZONGJZ", data.getTotalNum() + "");
            intent4.putExtra("FENXIANG", data.getInviteNum() + "");
            startActivity(intent4);
        }

        // 总记账笔数
        public void totalNum(View view) {
            Utils.playVoice(getActivity(), R.raw.changgui02);
            Intent intent4 = new Intent(getActivity(), ShaiChengJiuActivity.class);
            if (null != b1s) {
                String jsons = new Gson().toJson(b1s);
                intent4.putExtra("JSON", jsons);
            }
            if (null != b1) {
                intent4.putExtra("HEADYRL", data.getHeadUrl());
                intent4.putExtra("NAME", data.getName());
            }
            intent4.putExtra("JZDAYS", data.getMonthNum().substring(0, data.getMonthNum().indexOf("/")));
            intent4.putExtra("ZONGJZ", data.getTotalNum() + "");
            intent4.putExtra("FENXIANG", data.getInviteNum() + "");
            startActivity(intent4);
        }

        // 账户类别设置
        public void chooseAccountType(View view) {
            Utils.playVoice(getActivity(), R.raw.changgui02);
            startActivity(new Intent(getActivity(), ActChooseAccountType.class));
        }

        // 记账提醒
        public void accountAlert(View view) {
            Utils.playVoice(getActivity(), R.raw.changgui02);
            startActivity(new Intent(getActivity(), AccountAlertActivity.class));
        }

        // 丰丰通知
        public void notification(View view) {
            Utils.playVoice(getActivity(), R.raw.changgui02);
            startActivityForResult(new Intent(getActivity(), NotificationMessageActivity.class), 1000);
        }

        // 联系客服
        public void artificial(View view) {
            Utils.playVoice(getActivity(), R.raw.changgui02);
            String fengfengId = (String) SharedPreferencesUtil.get(Constants.FENGFENG_ID, "");
            if (TextUtils.isEmpty(fengfengId)) {
                ToastUtil.showShort("丰丰ID为空");
            } else {
                SobotUtils.startSobot(getActivity());
            }
        }

        // 收货地址
        public void address(View view) {
            Utils.playVoice(getActivity(), R.raw.changgui02);
            startActivity(new Intent(getActivity(), ActEditAddress.class));
        }

        // 邀请好友
        public void invite(View view) {
            Utils.playVoice(getActivity(), R.raw.changgui02);
            IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), com.hbird.base.app.constant.CommonTag.WEIXIN_APP_ID);
            WXMiniProgramObject miniProgram = new WXMiniProgramObject();
            miniProgram.webpageUrl = "https://fengniaojizhang.com/";
            miniProgram.miniprogramType = UrlConstants.IS_RELEASE ? WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE : WXMiniProgramObject.MINIPROGRAM_TYPE_TEST;
            miniProgram.userName = "gh_84f06fbaa705";

            if (data.getFengniaoId() == -1) {
                return;
            }
            miniProgram.path = "pages/details/index/main?scene=" + data.getFengniaoId();
            WXMediaMessage mediaMessage = new WXMediaMessage(miniProgram);
            mediaMessage.title = data.getName() + "向你推荐简单好用的记账软件!";
            mediaMessage.description = "不乱花，更自在。Save small.Live smart.";
            Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.mipmap.wxmini);
            Bitmap sendBitmap = Bitmap.createScaledBitmap(bitmap, 500, 400, true);
            bitmap.recycle();
            mediaMessage.thumbData = Util.bmpToByteArray(sendBitmap, true);
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = "";
            req.scene = SendMessageToWX.Req.WXSceneSession;
            req.message = mediaMessage;
            api.sendReq(req);
        }

        // 意见反馈
        public void callback(View view) {
            Utils.playVoice(getActivity(), R.raw.changgui02);
            startActivity(new Intent(getActivity(), SuggestFanKuiActivity.class));
        }

        // 检查更新
        public void checkUpDate(View view) {
            Utils.playVoice(getActivity(), R.raw.changgui02);
            String appVersion = com.hbird.base.util.Utils.getVersion(getActivity());
            if (!TextUtils.isEmpty(appVersion)) {
                //执行更新操作 获取不到token代表当前还没登录暂时不更新  token不校验 做登录之前更新操作
                updates(appVersion, token);
            }
        }

        // 关注公众号
        public void follow(View view) {
            Utils.playVoice(getActivity(), R.raw.changgui02);
            ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData mClipData = ClipData.newPlainText("Label", "fengniaojizhang01");
            cm.setPrimaryClip(mClipData);
            ToastUtil.showShort("已将公众号复制到粘贴板，请打开微信搜索关注");
        }

        // 关注微信号
        public void followWX(View view) {
            Utils.playVoice(getActivity(), R.raw.changgui02);
            ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData mClipData = ClipData.newPlainText("Label", "qiao45820");
            cm.setPrimaryClip(mClipData);
            ToastUtil.showShort("已将微信号复制到粘贴板，请打开微信搜索添加");
        }
    }

    // scrollView 滑动监听
    private void onScroll(int scrollY) {
        if (scrollY < 50) {
//            StatusBarUtil.clearStatusBarDarkMode(getActivity().getWindow());
            StatusBarUtil.setStatusBarDarkTheme(getActivity(),false);// 导航栏白色字体

            binding.flParent1.setVisibility(View.GONE);
            binding.flParent2.setVisibility(View.VISIBLE);
            binding.flParent1.setAlpha(0);

            data.setShowLine(false);
        } else if (scrollY >= height) {
            StatusBarUtil.setStatusBarDarkTheme(getActivity(),true);// 导航栏黑色字体
//            StatusBarUtil.setStatusBarLightMode(getActivity().getWindow());

            binding.flParent1.setVisibility(View.VISIBLE);
            binding.flParent2.setVisibility(View.GONE);
            binding.flParent1.setAlpha(1);

            data.setShowLine(true);
        } else {
            StatusBarUtil.setStatusBarDarkTheme(getActivity(),true);// 导航栏黑色字体
//            StatusBarUtil.setStatusBarLightMode(getActivity().getWindow());// 导航栏黑色字体

            binding.flParent1.setVisibility(View.VISIBLE);
            binding.flParent2.setVisibility(View.GONE);

            float alpha = (float) scrollY / height;
            binding.flParent1.setAlpha(alpha > 1 ? 1 : alpha);

            data.setShowLine(false);
        }
    }

    //获取个人信息
    private void getUserInfo() {
        NetWorkManager.getInstance().setContext(getActivity()).getPersionalInfos(token, new NetWorkManager.CallBack() {
            @Override
            public void onSuccess(BaseReturn b) {
                b1 = (GeRenInfoReturn) b;
                SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.H5PRIMKEYPHONE, b1.getResult().getMobile());
                SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.H5PRIMKEYWEIXIN, b1.getResult().getWechatAuth());
                SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.H5PRIMKEYZILIAO, new Gson().toJson(b1));
                SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.H5PRIMKEYIDS, b1.getResult().getId() + "");
                SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.H5PRIMKEYNAME, b1.getResult().getNickName());

//                String nickName = b1.getResult().getNickName();
//                if (TextUtils.isEmpty(nickName)) {
//                    String mobile = b1.getResult().getMobile();
//                    nickName = com.hbird.base.util.Utils.getHiddenPhone(mobile);
//                    SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.KEFUNICKNAME, "");
//                    SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.KEFUPHONE, nickName);
//                } else {
//                    SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.KEFUNICKNAME, nickName);
//                    String mobile = b1.getResult().getMobile();
//                    SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.KEFUPHONE, mobile);
//                }

                data.setName(b1.getResult().getNickName(), b1.getResult().getMobile());
                // 资料完善度
                Double precent = b1.getResult().getIntegrity();
                if (precent == 1) {
                    data.setPrecent(true);
                    data.setPrecent("已完善");// 无所谓 反正不显示
                } else {
                    data.setPrecent(false);
                    data.setPrecent("完善度" + precent * 100 + "%");
                }

                data.setFengniaoId(b1.getResult().getId());

                SharedPreferencesUtil.put(Constants.FENGFENG_ID, String.valueOf(b1.getResult().getId()));// 保存峰峰id
                //保存id 极光推送需要用到
                //JPushInterface.setAlias(getActivity(),1,b1.getResult().getId()+"");
                SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.FENG_NIAO_ID, b1.getResult().getId() + "");

                SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.KEFUIMG, b1.getResult().getAvatarUrl());

                data.setHeadUrl(b1.getResult().getAvatarUrl());
            }

            @Override
            public void onError(String s) {
                ToastUtil.showShort(s);
            }
        });
    }

    // 获取顶部信息
    private void getTopInfo() {
        // 没网的时候取本地
        int yyyy = parseInt(DateUtils.getCurYear("yyyy"));
        String mMonth = DateUtils.date2Str(new Date(), "MM");
        String s = mMonth.substring(0, 2);
        String t = yyyy + "-" + s;
        String sql = "select count(day) from (select count(charge_date2) day  from (select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT where 1=1) where account_book_id= '" + accountId + "' AND delflag = 0 AND strftime('%Y-%m', charge_date2) = '" + t + "' group by strftime('%Y-%m-%d', charge_date2)  );";
        Cursor cursor = DevRing.tableManager(WaterOrderCollect.class).rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
            data.setMonthNum(cursor.getString(0) + "/" + DateUtils.calculateDaysInMonth(yyyy, Integer.parseInt(mMonth)));
        }
        String sqls = "select count(id) As countNum from WATER_ORDER_COLLECT where account_book_id = '" + accountId + "' AND delflag = 0";
        Cursor cursor1 = DevRing.tableManager(WaterOrderCollect.class).rawQuery(sqls, null);
        if (cursor1 != null) {
            cursor1.moveToFirst();
            data.setTotalNum(Integer.parseInt(cursor1.getString(0)));
        }

        NetWorkManager.getInstance().setContext(getActivity()).getHeaderInfo(token, new NetWorkManager.CallBack() {
            @Override
            public void onSuccess(BaseReturn b) {
                b1s = (HeaderInfoReturn) b;
                if (null != b1s.getResult()) {
                    try {
                        data.setInviteNum(b1s.getResult().getInviteUsers());
                        data.setMonthNum(b1s.getResult().getDaysCount());
                        data.setTotalNum(b1s.getResult().getChargeTotal());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(String s) {
                ToastUtil.showShort(s);
            }
        });
    }

    // 获取丰丰通知
    private void getMessage() {
        String userinfoid = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.USER_INFO_PERSION, "");
        NetWorkManager.getInstance().setContext(getActivity()).getFengMessage(userinfoid, "0", "0", token, new NetWorkManager.CallBack() {
            @Override
            public void onSuccess(BaseReturn b) {
                FengMessageReturn b1 = (FengMessageReturn) b;
                data.setMsgNum(b1.getResult().getUnreadMessageNumber());
            }

            @Override
            public void onError(String s) {
            }
        });
    }

    // 检查更新
    private void updates(final String version, String token) {
        NetWorkManager.getInstance().setContext(getActivity()).getCheckVersion(token, version, new NetWorkManager.CallBack() {

            String url;

            @Override
            public void onSuccess(BaseReturn b) {
                CheckVersionReturn b1 = (CheckVersionReturn) b;
                if (null != b1.getResult()) {
                    final CheckVersionReturn.ResultBean result = b1.getResult();
                    String ver = result.getVersion();
                    url = result.getUrl();
                    if (ver.compareTo(version) > 0) {
                        //执行下载操作
                        new updateDialog(getActivity()).builder()
                                .setMsg(result.getUpdateLog())
                                .setUpdateButton(view -> {
                                    SPUtil.setPrefString(getActivity(), CommonTag.UPDATE_URL, url);
                                    downLoad(result.getInstallStatus() == 1);
                                })
                                .setCancleButton(view -> {
                                    int status = result.getInstallStatus();
                                    if (status == 1) { //强制更新  否则退出程序
                                        DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).put(CommonTag.GLOABLE_TOKEN, "");
                                        DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).put(CommonTag.GLOABLE_TOKEN_EXPIRE, "");
                                        DevRing.activityListManager().killAll();
                                        getActivity().finish();
                                        return;
                                    }
                                }).show();
                    } else {
                        ToastUtil.showShort("当前已经是最新版本了");
                    }
                } else {
                    ToastUtil.showShort("当前已经是最新版本了");
                }
            }

            @Override
            public void onError(String s) {
                ToastUtil.showShort(s);
            }
        });
    }

    // 下载
    private void downLoad(boolean force) {
        DownLoadDialog dialog = new DownLoadDialog(getActivity());

        File mFileSave = FileUtil.getFile(FileUtil.getExternalCacheDir(getActivity()), "fengniao.apk");
        String url = SPUtil.getPrefString(getActivity(), CommonTag.UPDATE_URL, "");

        DownLoadUtil util = new DownLoadUtil(getActivity(), url, mFileSave.getPath());
        util.downLoadFile(new DownLoadUtil.Callback() {
            @Override
            public void success(String url, String localPath) {
                dialog.dismiss();
                sing.common.util.Utils.installApk(getActivity(), localPath);
                DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).put(com.hbird.base.mvc.global.CommonTag.GLOABLE_TOKEN, "");
                DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).put(com.hbird.base.mvc.global.CommonTag.GLOABLE_TOKEN_EXPIRE, "");
                DevRing.activityListManager().killAll();
                getActivity().finish();
            }

            @Override
            public void failure(String url, String errorMsg) {
                ToastUtil.showShort("下载失败:" + errorMsg);
                dialog.dismiss();
                DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).put(com.hbird.base.mvc.global.CommonTag.GLOABLE_TOKEN, "");
                DevRing.cacheManager().spCache(com.hbird.base.app.constant.CommonTag.SPCACH).put(com.hbird.base.mvc.global.CommonTag.GLOABLE_TOKEN_EXPIRE, "");
                DevRing.activityListManager().killAll();
                getActivity().finish();
            }

            @Override
            public void process(long current, long total) {
                dialog.start(current, total);
                LogUtil.e("当前已下载：" + current);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            getMessage();
        } else if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            getUserInfo();
        }
    }
}
