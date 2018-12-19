package com.hbird.base.mvc.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hbird.base.R;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.app.constant.UrlConstants;
import com.hbird.base.mvc.adapter.AskFriendsAdapter;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.RequestBean.AcceptFirendReq;
import com.hbird.base.mvc.bean.ReturnBean.AskFirendBean;
import com.hbird.base.mvc.bean.ReturnBean.GeRenInfoReturn;
import com.hbird.base.mvc.bean.ReturnBean.UserFirendsReturn;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvc.widget.XListView;
import com.hbird.base.mvp.presenter.base.BasePresenter;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.DateUtil;
import com.hbird.base.util.SPUtil;
import com.hbird.base.util.Util;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.id;

/**
 * Created by Liul on 2018/10/17.
 * 邀请好友(推荐好友)
 */

public class AskFriendsActivity extends BaseActivity<BasePresenter> implements View.OnClickListener, XListView.IXListViewListener {
    @BindView(R.id.iv_back)
    ImageView mBack;
    @BindView(R.id.center_title)
    TextView mCenterTit;
    @BindView(R.id.right_title2)
    TextView mRightTit;
    @BindView(R.id.xList)
    XListView lv;
    @BindView(R.id.rl_emptys)
    RelativeLayout mEmptys;
    @BindView(R.id.rl_bottom_btn)
    RelativeLayout mBtn;

    private String ids;
    private String nickName;
    private ArrayList<UserFirendsReturn.ResultBean.ContentBean> myList;
    private AskFriendsAdapter adapter;
    private int PageNum = 1;
    private String token;
    private String nickName1;
    private String avatarUrl;
    private int id;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_ask_firend;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mCenterTit.setText("邀请好友");
        mRightTit.setVisibility(View.GONE);
        ids = getIntent().getStringExtra("IDS");
        nickName = getIntent().getStringExtra("NAME");
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        token = SPUtil.getPrefString(AskFriendsActivity.this, CommonTag.GLOABLE_TOKEN, "");
        getResouseInfo();
      /*  ArrayList<AskFirendBean> list = new ArrayList<>();
        for (int i=0;i<5;i++){
            AskFirendBean bean = new AskFirendBean();
            bean.setTimes("2018-09-01加入");
            bean.setImgUrl("");
            bean.setNames("知呼先生"+i);
            list.add(bean);
        }*/
        //getUserInfos();
    }

    private void getUserInfos() {
        NetWorkManager.getInstance().setContext(AskFriendsActivity.this)
                .getPersionalInfos(token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        GeRenInfoReturn b1 = (GeRenInfoReturn) b;
                        nickName1 = b1.getResult().getNickName();
                        avatarUrl = b1.getResult().getAvatarUrl();
                        id = b1.getResult().getId();

                    }

                    @Override
                    public void onError(String s) {

                    }
                });
    }

    private void getResouseInfo() {

        showProgress("");
        NetWorkManager.getInstance().setContext(AskFriendsActivity.this).getAllHistoryUser(1, 10, token, new NetWorkManager.CallBack() {
            @Override
            public void onSuccess(BaseReturn b) {
                hideProgress();
                UserFirendsReturn b1 = (UserFirendsReturn) b;
                if (b1 != null) {
                    List<UserFirendsReturn.ResultBean.ContentBean> list = b1.getResult().getContent();
                    myList = new ArrayList<>();
                    myList.clear();
                    if (list.size() > 0 && list.size() == 10) {
                        for (int i = 0; i < list.size(); i++) {
                            myList.add(list.get(i));
                        }
                        adapter = new AskFriendsAdapter(AskFriendsActivity.this, list);
                        lv.setAdapter(adapter);
                        lv.setPullLoadEnable(true);
                    } else if (list.size() == 0) {
                        lv.setVisibility(View.GONE);
                        mEmptys.setVisibility(View.VISIBLE);
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            myList.add(list.get(i));
                        }
                        adapter = new AskFriendsAdapter(AskFriendsActivity.this, list);
                        lv.setAdapter(adapter);
                        lv.setPullLoadEnable(false);
                    }

                } else {
                    lv.setVisibility(View.GONE);
                    mEmptys.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onError(String s) {
                hideProgress();
                showMessage(s);
            }
        });
    }

    @Override
    protected void initEvent() {
        mBack.setOnClickListener(this);
        mBtn.setOnClickListener(this);
        lv.setXListViewListener(this);
        lv.setPullLoadEnable(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                playVoice(R.raw.changgui02);
                finish();
                break;
            case R.id.rl_bottom_btn:
                //showMessage("微信邀请记账");
                playVoice(R.raw.changgui02);
                InviteYou();
                break;
        }
    }

    @Override
    public void onRefresh() {
        PageNum = 1;
        getResouseInfo();
        onLoad();
    }


    @Override
    public void onLoadMore() {
        loadMoreDatas();
    }

    private void loadMoreDatas() {
        PageNum++;
        showProgress("");
        NetWorkManager.getInstance().setContext(AskFriendsActivity.this).getAllHistoryUser(PageNum, 10, token, new NetWorkManager.CallBack() {
            @Override
            public void onSuccess(BaseReturn b) {
                hideProgress();
                UserFirendsReturn b1 = (UserFirendsReturn) b;
                if (b1 != null) {
                    List<UserFirendsReturn.ResultBean.ContentBean> list = b1.getResult().getContent();
                    if (list.size() > 0 && list.size() == 10) {
                        myList.addAll(list);
                        loadAskMoreView(list);
                        lv.setPullLoadEnable(true);

                    } else if (list.size() == 0) {
                        lv.setPullLoadEnable(false);
                        showMessage("暂无数据");
                    } else {
                        myList.addAll(list);
                        loadAskMoreView(list);
                        lv.setPullLoadEnable(false);
                    }

                }

            }

            @Override
            public void onError(String s) {
                hideProgress();
                showMessage(s);
            }
        });
    }

    private void loadAskMoreView(List<UserFirendsReturn.ResultBean.ContentBean> moreList) {
        adapter.loadMore(moreList);
        lv.stopLoadMore();
    }

    private void onLoad() {
        String time = DateUtil.formatDate("HH:mm", new Date());
        lv.setRefreshTime("今天:" + time);
        lv.stopRefresh();
    }

    private void InviteYou() {
        IWXAPI api = WXAPIFactory.createWXAPI(AskFriendsActivity.this, com.hbird.base.app.constant.CommonTag.WEIXIN_APP_ID);
        WXMiniProgramObject miniProgram = new WXMiniProgramObject();
        miniProgram.webpageUrl = "https://fengniaojizhang.com/";//兼容低版本网页链接
        // 正式版:0，测试版:1，体验版:2
        miniProgram.miniprogramType = UrlConstants.IS_RELEASE ? WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE : WXMiniProgramObject.MINIPROGRAM_TYPE_TEST;
        miniProgram.userName = "gh_84f06fbaa705";//小程序端提供参数 小程序ID
        if (TextUtils.isEmpty(ids)) {
            return;
        }
        miniProgram.path = "pages/details/index/main?scene=" + ids;//小程序端提供参数
        WXMediaMessage mediaMessage = new WXMediaMessage(miniProgram);
        mediaMessage.title = nickName + "向你推荐简单好用的记账软件!";//自定义
        mediaMessage.description = "不乱花，更自在。Save small.Live smart.";//自定义
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.wxmini);
        Bitmap sendBitmap = Bitmap.createScaledBitmap(bitmap, 500, 400, true);
        bitmap.recycle();
        mediaMessage.thumbData = Util.bmpToByteArray(sendBitmap, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "";
        req.scene = SendMessageToWX.Req.WXSceneSession;
        req.message = mediaMessage;
        api.sendReq(req);

    }
}
