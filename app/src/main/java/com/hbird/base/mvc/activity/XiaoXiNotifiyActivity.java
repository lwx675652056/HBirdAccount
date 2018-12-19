package com.hbird.base.mvc.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hbird.base.R;
import com.hbird.base.mvc.adapter.XiaoXiAdapter;
import com.hbird.base.mvc.base.baseActivity.BaseActivityPresenter;
import com.hbird.base.mvc.bean.RequestBean.XiaoXiBean;
import com.hbird.base.mvp.view.activity.base.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Liul on 2018/10/10.
 * 消息通知
 */

public class XiaoXiNotifiyActivity extends BaseActivity<BaseActivityPresenter> implements View.OnClickListener {
    @BindView(R.id.iv_back)
    ImageView mBack;
    @BindView(R.id.center_title)
    TextView mCenterTitle;
    @BindView(R.id.right_title2)
    TextView mRightTit;
    @BindView(R.id.lv)
    ListView lv;


    @Override
    protected int getContentLayout() {
        return R.layout.activity_xiaoxi;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mRightTit.setVisibility(View.GONE);
        mCenterTitle.setText("消息通知");
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        ArrayList<XiaoXiBean> list = new ArrayList<>();
        XiaoXiBean xBean = new XiaoXiBean();
        xBean.setNums(2);
        xBean.setTimes("2018/08/24 18:00");
        xBean.setxContent("十五天做个全新的自己，关注蜂鸟公众号，开启自己的记账之旅吧！哈哈哈哈哈哈哈哈哈哈");
        xBean.setxImgUrl("");
        xBean.setxTitle("蜂鸟活动");
        list.add(xBean);
        XiaoXiAdapter adapter = new XiaoXiAdapter(XiaoXiNotifiyActivity.this,list);


    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                playVoice(R.raw.changgui02);
                finish();
                break;
        }
    }
}
