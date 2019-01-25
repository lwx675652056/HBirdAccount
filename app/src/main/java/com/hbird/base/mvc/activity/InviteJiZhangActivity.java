package com.hbird.base.mvc.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hbird.base.R;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.app.constant.UrlConstants;
import com.hbird.base.mvc.adapter.InviteGridViewAdapter;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.IndexFirends;
import com.hbird.base.mvc.bean.RequestBean.AcceptFirendReq;
import com.hbird.base.mvc.bean.ReturnBean.AccountMembersBean;
import com.hbird.base.mvc.bean.ReturnBean.GeRenInfoReturn;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvc.widget.MyGridView;
import com.hbird.base.mvp.presenter.base.BasePresenter;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.SPUtil;
import com.hbird.base.util.Util;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import sing.util.LogUtil;


/**
 * Created by Liul(245904552@qq.com) on 2018/11/8.
 * 邀请记账
 */

public class InviteJiZhangActivity extends BaseActivity<BasePresenter> {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.center_title)
    TextView centerTitle;
    @BindView(R.id.right_title)
    TextView rightTitle;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.grde_view)
    MyGridView grdeView;
    private String id;
    private String token;
    private String nickName1;
    private String avatarUrl;
    private int ids;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_invite_jizhang;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        centerTitle.setText("邀请记账");
        rightTitle.setVisibility(View.GONE);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        token = SPUtil.getPrefString(InviteJiZhangActivity.this, CommonTag.GLOABLE_TOKEN, "");
        id = getIntent().getStringExtra("ID");
        getAccountMembers();
        getUserInfos();
    }

    private void getAccountMembers() {
        NetWorkManager.getInstance().setContext(InviteJiZhangActivity.this).getAccountMember(token, id, new NetWorkManager.CallBack() {
            @Override
            public void onSuccess(BaseReturn b) {
                AccountMembersBean b1 = (AccountMembersBean) b;

                String yourSelf = b1.getResult().getYourSelf();
                String owner = b1.getResult().getOwner();
                List<String> members = b1.getResult().getMembers();
                if (null != owner) {
                    members.add(owner);
                } else {
                    //代表我是管理员
                }
                ArrayList<String> list = new ArrayList<>();
                list.clear();
                if (yourSelf != null) {
                    list.add(yourSelf);
                } else {
                    list.add("");
                }
                if (members != null) {
                    list.addAll(members);
                }

                //如果list>1 表示有成员 需要展示头像 隐藏邀请记账 收起
                if (list != null) {
                    if (list.size() > 0) {
                        //初始化 邀请来的好友
                        ArrayList<IndexFirends> indexFirends = new ArrayList<>();
                        indexFirends.clear();
                        for (int i = 0; i < list.size(); i++) {
                            IndexFirends indexBean = new IndexFirends();
                            indexBean.setImgUrl(list.get(i));
                            indexFirends.add(indexBean);
                        }
                        InviteGridViewAdapter grideView = new InviteGridViewAdapter(InviteJiZhangActivity.this, indexFirends);
                        grdeView.setAdapter(grideView);
                        tvDesc.setText("每个账本最多5人记录，还可邀请" + (5 - indexFirends.size()) + "人");
                    }
                }
            }

            @Override
            public void onError(String s) {
            }
        });
    }

    @Override
    protected void initEvent() {
    }

    @OnClick({R.id.iv_back, R.id.rl_wx_invite})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                playVoice(R.raw.changgui02);
                finish();
                break;
            case R.id.rl_wx_invite:// 邀请好友记账
                playVoice(R.raw.changgui02);
                InviteYou();
                break;
        }
    }

    private void getUserInfos() {
        NetWorkManager.getInstance().setContext(InviteJiZhangActivity.this)
                .getPersionalInfos(token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        GeRenInfoReturn b1 = (GeRenInfoReturn) b;
                        nickName1 = b1.getResult().getNickName();
                        if (TextUtils.isEmpty(nickName1)) {
                            StringBuffer sb = new StringBuffer();
                            String temp = b1.getResult().getMobile();
                            if (temp != null && !temp.equals("")) {// 手机号不为空
                                sb.append(temp.substring(0, 3))
                                        .append("****")
                                        .append(temp.substring(7, temp.length()));
                                nickName1 = sb.toString();
                            } else {
                                nickName1 = b1.getResult().getId() + "";
                            }

                            LogUtil.e(nickName1);
                        }
                        avatarUrl = b1.getResult().getAvatarUrl();
                        ids = b1.getResult().getId();

                    }

                    @Override
                    public void onError(String s) {
                    }
                });
    }

    private void InviteYou() {
        IWXAPI api = WXAPIFactory.createWXAPI(InviteJiZhangActivity.this, com.hbird.base.app.constant.CommonTag.WEIXIN_APP_ID);
        WXMiniProgramObject miniProgram = new WXMiniProgramObject();
        miniProgram.webpageUrl = "https://fengniaojizhang.com/";//兼容低版本网页链接
        // 正式版:0，测试版:1，体验版:2
        miniProgram.miniprogramType = UrlConstants.IS_RELEASE ? WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE : WXMiniProgramObject.MINIPROGRAM_TYPE_TEST;
        miniProgram.userName = "gh_84f06fbaa705";//小程序端提供参数 小程序ID
        if (TextUtils.isEmpty(ids + "")) {
            return;
        }
        String bookId = SPUtil.getPrefString(InviteJiZhangActivity.this, CommonTag.ACCOUNT_BOOK_ID, "");//账本ID
        String bookName = SPUtil.getPrefString(InviteJiZhangActivity.this, CommonTag.INDEX_CURRENT_ACCOUNT, "");//账本名称
        String typeBudget = SPUtil.getPrefString(InviteJiZhangActivity.this, CommonTag.INDEX_TYPE_BUDGET, "");//账本预算类型
        String abTypeId = SPUtil.getPrefString(InviteJiZhangActivity.this, CommonTag.INDEX_CURRENT_ACCOUNT_TYPE, "");//账本的abTypeId

        AcceptFirendReq req = new AcceptFirendReq();
        req.setBookId(bookId);
        req.setBookName(bookName);
        req.setTypeBudget(typeBudget);
        req.setAbTypeId(abTypeId);
        req.setUsername(nickName1);
        req.setUserAvatar(avatarUrl);
        req.setCreateId(ids + "");
        String ss = new Gson().toJson(req);
        miniProgram.path = "pages/details/index/main?isJoin=" + ss;//小程序端提供参数
        LogUtil.e("小程序端提供参数:" + ss);
        WXMediaMessage mediaMessage = new WXMediaMessage(miniProgram);
        mediaMessage.title = nickName1 + "邀请你加入『" + bookName + "』账本";//自定义
        mediaMessage.description = "不乱花，更自在。Save small.Live smart.";
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.wxmini);
        Bitmap sendBitmap = Bitmap.createScaledBitmap(bitmap, 500, 400, true);
        bitmap.recycle();
        mediaMessage.thumbData = Util.bmpToByteArray(sendBitmap, true);
        SendMessageToWX.Req reqs = new SendMessageToWX.Req();
        reqs.transaction = "";
        reqs.scene = SendMessageToWX.Req.WXSceneSession;
        reqs.message = mediaMessage;
        api.sendReq(reqs);

    }

}
