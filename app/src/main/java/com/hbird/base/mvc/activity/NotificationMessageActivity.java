package com.hbird.base.mvc.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.app.constant.CommonTag;
import com.hbird.base.mvc.adapter.NotificationMessageAdapter;
import com.hbird.base.mvc.base.baseActivity.BaseActivityPresenter;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.ReturnBean.FengMessageReturn;
import com.hbird.base.mvc.bean.ReturnBean.GloableReturn;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.SPUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Liul(245904552@qq.com) on 2018/11/9.
 * 丰丰通知
 */

public class NotificationMessageActivity extends BaseActivity<BaseActivityPresenter> {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.left_title)
    TextView leftTitle;
    @BindView(R.id.center_title)
    TextView centerTitle;
    @BindView(R.id.right_title)
    TextView rightTitle;
    @BindView(R.id.right_title2)
    TextView rightTitle2;
    @BindView(R.id.right_img)
    ImageView rightImg;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.iv_no_data)
    ImageView ivNoData;
    private String token;
    private List<FengMessageReturn.ResultBean.MessageListBean> messageList;
    private String userinfoid;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_notification_message;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        centerTitle.setText("丰丰通知");
        rightTitle2.setText("全部已读");
        rightTitle2.setVisibility(View.GONE);
        rightTitle2.setTextColor(getResources().getColor(R.color.white));
        token = SPUtil.getPrefString(NotificationMessageActivity.this, CommonTag.GLOABLE_TOKEN, "");
        userinfoid = SPUtil.getPrefString(NotificationMessageActivity.this, CommonTag.USER_INFO_PERSION, "");
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        getMessage();
    }

    private void getMessage() {
        NetWorkManager.getInstance().setContext(NotificationMessageActivity.this)
                .getFengMessage(userinfoid, "1", "500", token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        FengMessageReturn b1 = (FengMessageReturn) b;
                        messageList = b1.getResult().getMessageList();
                        int unreadMessageNumber = b1.getResult().getUnreadMessageNumber();
                        if(unreadMessageNumber>0){
                            rightTitle2.setVisibility(View.VISIBLE);
                        }else {
                            rightTitle2.setVisibility(View.GONE);
                        }
                        if (messageList == null || messageList.size()<1){
                            ivNoData.setVisibility(View.VISIBLE);
                            lv.setVisibility(View.GONE);
                        }else{
                            ivNoData.setVisibility(View.GONE);
                            lv.setVisibility(View.VISIBLE);
                        }
                        NotificationMessageAdapter adapter = new NotificationMessageAdapter(NotificationMessageActivity.this, messageList);
                        lv.setAdapter(adapter);
                    }

                    @Override
                    public void onError(String s) {

                    }
                });
    }

    @Override
    protected void initEvent() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //showMessage("点击查看了 "+i);
                int status = messageList.get(i).getStatus();
                if(status==1){
                    //表示已读  返回不调接口
                    return;
                }
                NetWorkManager.getInstance().setContext(NotificationMessageActivity.this)
                        .postUpDateStaus("ONE", userinfoid,messageList.get(i).getId()+"" , token, new NetWorkManager.CallBack() {
                            @Override
                            public void onSuccess(BaseReturn b) {
                                GloableReturn b1 = (GloableReturn) b;
                                getMessage();
                            }

                            @Override
                            public void onError(String s) {
                                showMessage(s);
                            }
                        });
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.right_title2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                playVoice(R.raw.changgui02);
                finish();
                break;
            case R.id.right_title2:
                playVoice(R.raw.changgui02);
                NetWorkManager.getInstance().setContext(NotificationMessageActivity.this)
                        .postUpDateStaus("ALL", userinfoid,"", token, new NetWorkManager.CallBack() {
                            @Override
                            public void onSuccess(BaseReturn b) {
                                GloableReturn b1 = (GloableReturn) b;
                                getMessage();
                            }

                            @Override
                            public void onError(String s) {
                                showMessage(s);
                            }
                        });
                break;
        }
    }
}
