package com.hbird.base.mvc.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hbird.base.R;
import com.hbird.base.mvc.adapter.MyExpandableListView2NewAdapter;
import com.hbird.base.mvc.adapter.MyExpandableListViewAdapterNew;
import com.hbird.base.mvc.base.baseActivity.BaseActivityPresenter;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.ReturnBean.BiaoQianReturn;
import com.hbird.base.mvc.bean.ReturnBean.ShouRuTagReturnNew;
import com.hbird.base.mvc.bean.ReturnBean.SystemBiaoqReturn;
import com.hbird.base.mvc.bean.ReturnBean.ZhiChuTagReturnNew;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.SPUtil;
import com.hbird.util.Utils;
import com.ljy.devring.image.support.GlideApp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import sing.common.util.StatusBarUtil;
import sing.util.SharedPreferencesUtil;
import sing.util.ToastUtil;

/**
 * @author: LiangYX
 * @ClassName: AddMoreTypeActivityNew
 * @date: 2018/12/17 14:21
 * @Description: 我的里面 - 添加类别
 */
public class AddMoreTypeActivityNew extends BaseActivity<BaseActivityPresenter> implements View.OnClickListener {

    @BindView(R.id.iv_backs)
    ImageView mBack;
    @BindView(R.id.tv_center_title)
    TextView mCenterTitle;
    @BindView(R.id.tv_right_title)
    TextView mRightTitle;
    @BindView(R.id.expandableList)
    ExpandableListView expandableListView;
    @BindView(R.id.ll_choose_items)
    LinearLayout mChooseItem;
    @BindView(R.id.iv_img)
    ImageView mImg;
    @BindView(R.id.tv_choose_type)
    TextView mChooseType;

    private List<List<ZhiChuTagReturnNew.ResultBean.AllListBean.SpendTypeSonsBean>> itemList;
    private String title;
    private List<List<ShouRuTagReturnNew.ResultBean.AllListBean.IncomeTypeSonsBean>> item2List;
    private ZhiChuTagReturnNew.ResultBean.AllListBean.SpendTypeSonsBean callBacka;
    private ShouRuTagReturnNew.ResultBean.AllListBean.IncomeTypeSonsBean callBackb;
    private ArrayList<String> list;
    private String bookIdType;
    private Integer types;

    @Override
    protected int getContentLayout() {
        return R.layout.act_add_more_type;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Utils.initColor(this, Color.rgb(255, 255, 255));
        StatusBarUtil.setStatusBarLightMode(getWindow()); // 导航栏黑色字体

        title = getIntent().getStringExtra("TITLE");
        mRightTitle.setTextColor(Color.parseColor("#D80200"));// 完成变红色
        mRightTitle.setText("完成");

        if (TextUtils.equals(title, "收入")) {
            mCenterTitle.setText("添加收入类别");
        } else {
            mCenterTitle.setText("添加支出类别");
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        list = getIntent().getStringArrayListExtra("object");// 已有的
        bookIdType = SPUtil.getPrefString(this, com.hbird.base.app.constant.CommonTag.CURRENT_ACCOUNT_ID, "0");
        types = Integer.valueOf(bookIdType);
        getDate();
    }

    @Override
    protected void initEvent() {
        mBack.setOnClickListener(this);
        mRightTitle.setOnClickListener(this);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                //屏蔽ExpandableListView 组 的点击事件
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_backs:
                playVoice(R.raw.changgui02);
                finish();
                break;
            case R.id.tv_right_title: // 完成
                playVoice(R.raw.changgui02);
                getAccountTypes();
                break;
        }
    }

    private void getDate() {
        String token = SPUtil.getPrefString(this, CommonTag.GLOABLE_TOKEN, "");
        showProgress("");
        if (TextUtils.equals(title, "收入")) {
            NetWorkManager.getInstance().setContext(this).getIncomeLabelsNew(types, token, new NetWorkManager.CallBack() {
                @Override
                public void onSuccess(BaseReturn b) {
                    hideProgress();
                    ShouRuTagReturnNew b1 = (ShouRuTagReturnNew) b;
                    set2Date(b1);
                }

                @Override
                public void onError(String s) {
                    hideProgress();
                    showMessage(s);
                }
            });
        } else {
            NetWorkManager.getInstance().setContext(this).getSpendLabelsNew(types, token, new NetWorkManager.CallBack() {
                @Override
                public void onSuccess(BaseReturn b) {
                    hideProgress();
                    ZhiChuTagReturnNew b2 = (ZhiChuTagReturnNew) b;
                    setDate(b2);
                }

                @Override
                public void onError(String s) {
                    hideProgress();
                    showMessage(s);
                }
            });
        }
    }

    // 设置支出
    private void setDate(ZhiChuTagReturnNew date) {
        final List<ZhiChuTagReturnNew.ResultBean.AllListBean> groupList = date.getResult().getAllList();//组 级项目
        if (groupList == null || groupList.size() < 1) {
            ToastUtil.showShort("没有数据");
            return;
        }

        //子级项目集合
        itemList = new ArrayList<>();
        for (int i = 0; i < groupList.size(); i++) {
            List<ZhiChuTagReturnNew.ResultBean.AllListBean.SpendTypeSonsBean> spendTypeSons = groupList.get(i).getSpendTypeSons();
            itemList.add(spendTypeSons);
        }
        final MyExpandableListViewAdapterNew adapter = new MyExpandableListViewAdapterNew(this, groupList, itemList, list);
        expandableListView.setAdapter(adapter);
        mChooseItem.setVisibility(View.VISIBLE);
        if (groupList.get(0) != null) {
            callBacka = groupList.get(0).getSpendTypeSons().get(0);
        }
        if (callBacka != null) {
            mChooseType.setText(callBacka.getSpendName());
            GlideApp.with(AddMoreTypeActivityNew.this)
                    .load(callBacka.getIcon())
                    .skipMemoryCache(false)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(mImg);
            //点选后的数据回调
            adapter.setMyOnclickListener((groupPosition, position) -> {
                playVoice(R.raw.typevoice);
                callBacka = groupList.get(groupPosition).getSpendTypeSons().get(position);
                mChooseItem.setVisibility(View.VISIBLE);
                mChooseType.setText(callBacka.getSpendName());
                GlideApp.with(AddMoreTypeActivityNew.this)
                        .load(callBacka.getIcon())
                        .skipMemoryCache(false)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into(mImg);
            });
        }

        int groupCount = expandableListView.getCount();
        for (int i = 0; i < groupCount; i++) {
            expandableListView.expandGroup(i);
        }

        // 隐藏分组指示器
        expandableListView.setGroupIndicator(null);
    }

    // 设置收入
    private void set2Date(ShouRuTagReturnNew date) {
        final List<ShouRuTagReturnNew.ResultBean.AllListBean> groupList = date.getResult().getAllList();//组 级项目
        if (groupList == null || groupList.size() < 1) {
            ToastUtil.showShort("没有数据");
            return;
        }

        //子级项目集合
        item2List = new ArrayList<>();
        for (int i = 0; i < groupList.size(); i++) {
            List<ShouRuTagReturnNew.ResultBean.AllListBean.IncomeTypeSonsBean> incomeTypeSons = groupList.get(i).getIncomeTypeSons();
            item2List.add(incomeTypeSons);
        }
        final MyExpandableListView2NewAdapter adapter = new MyExpandableListView2NewAdapter(this, groupList, item2List, list);
        expandableListView.setAdapter(adapter);
        if (null != groupList.get(0)) {
            callBackb = groupList.get(0).getIncomeTypeSons().get(0);
        }
        if (null != callBackb) {
            mChooseItem.setVisibility(View.VISIBLE);
            mChooseType.setText(callBackb.getIncomeName());
            GlideApp.with(AddMoreTypeActivityNew.this)
                    .load(callBackb.getIcon())
                    .skipMemoryCache(false)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(mImg);
            //点选后的数据回调
            adapter.setMyOnclickListener((groupPosition, position) -> {
                playVoice(R.raw.typevoice);
                callBackb = groupList.get(groupPosition).getIncomeTypeSons().get(position);
                mChooseItem.setVisibility(View.VISIBLE);
                mChooseType.setText(callBackb.getIncomeName());
                GlideApp.with(AddMoreTypeActivityNew.this)
                        .load(callBackb.getIcon())
                        .skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into(mImg);
            });
        }

        int groupCount = expandableListView.getCount();
        for (int i = 0; i < groupCount; i++) {
            expandableListView.expandGroup(i);
        }

        // 隐藏分组指示器
        expandableListView.setGroupIndicator(null);
    }

    private void getAccountTypes() {
        if (TextUtils.equals(title, "收入")) {
            if (null == callBackb) {
                showMessage("请选择要添加的类别");
                return;
            }
        } else {
            if (null == callBacka) {
                showMessage("请选择要添加的类别");
                return;
            }
        }
        String token = SPUtil.getPrefString(this, CommonTag.GLOABLE_TOKEN, "");
        String a = SPUtil.getPrefString(this, com.hbird.base.app.constant.CommonTag.CURRENT_ACCOUNT_ID, "0");
        showProgress("");
        if (TextUtils.equals(title, "收入")) {
            //添加用户常用收入类目
            NetWorkManager.getInstance().setContext(this).postShouRuType(callBackb.getId(), Integer.parseInt(a), token, new NetWorkManager.CallBack() {
                @Override
                public void onSuccess(BaseReturn b) {
                    hideProgress();
                    BiaoQianReturn b1 = (BiaoQianReturn) b;
                    String version = b1.getResult().getVersion();
                    SPUtil.setPrefString(AddMoreTypeActivityNew.this, com.hbird.base.app.constant.CommonTag.LABEL_VERSION, version);
                    insertDates();
                    setResult(123);
                    finish();
                }

                @Override
                public void onError(String s) {
                    hideProgress();
                    showMessage(s);
                }
            });
        } else {
            //添加用户常用支出类目
            NetWorkManager.getInstance().setContext(this).postZhiChuType(callBacka.getId(), types, token, new NetWorkManager.CallBack() {
                @Override
                public void onSuccess(BaseReturn b) {
                    hideProgress();
                    BiaoQianReturn b1 = (BiaoQianReturn) b;
                    String version = b1.getResult().getVersion();
                    SPUtil.setPrefString(AddMoreTypeActivityNew.this, com.hbird.base.app.constant.CommonTag.LABEL_VERSION, version);
                    insertDates();
                    showMessage("成功");
                    setResult(121);
                    finish();
                }

                @Override
                public void onError(String s) {
                    hideProgress();
                    showMessage(s);
                }
            });
        }
    }

    private void insertDates() {
        //插入本地数据库
        String userInfo = SPUtil.getPrefString(this, com.hbird.base.app.constant.CommonTag.USER_INFO_PERSION, "");
        Integer userInfoId = Integer.valueOf(userInfo);
        String abTypeId = SPUtil.getPrefString(this, com.hbird.base.app.constant.CommonTag.CURRENT_ACCOUNT_ID, "");

        if (TextUtils.equals(title, "收入")) {
            String temp = (String) SharedPreferencesUtil.get("userId_" + userInfoId + "abTypeId_" + abTypeId + "type_income", "");
            List<SystemBiaoqReturn.ResultBean.LabelBean.IncomeBean> list = JSON.parseArray(temp, SystemBiaoqReturn.ResultBean.LabelBean.IncomeBean.class);
            if (list == null) {
                list = new ArrayList<>();
            }
            SystemBiaoqReturn.ResultBean.LabelBean.IncomeBean bean = new SystemBiaoqReturn.ResultBean.LabelBean.IncomeBean();
            bean.setId(Integer.parseInt(callBackb.getId()));
            bean.setIcon(callBackb.getIcon());
            bean.setIncomeName(callBackb.getIncomeName());
            bean.setPriority(callBackb.getPriority());
            list.add(bean);

            SharedPreferencesUtil.put("userId_" + userInfoId + "abTypeId_" + abTypeId + "type_income", list.toString());
//            HbirdUserCommUseIncome h = new HbirdUserCommUseIncome();
//            h.setIcon(callBackb.getIcon());
//            h.setMark(callBackb.getMark());
//            h.setId(callBackb.getId());
//            h.setIncomeName(callBackb.getIncomeName());
//            h.setParentId(callBackb.getParentId());
//            h.setParentName(callBackb.getParentName());
//            h.setPriority(callBackb.getPriority());
//            h.setAbTypeId(callBackb.getAbTypeId());
//            h.setUserInfoId(userInfoId);
//            boolean b = DevRing.tableManager(HbirdUserCommUseIncome.class).insertOne(h);
        } else {
//            HbirdUserCommUseSpend h = new HbirdUserCommUseSpend();
//            h.setIcon(callBacka.getIcon());
//            h.setMark(callBacka.getMark());
//            h.setId(callBacka.getId());
//            h.setSpendName(callBacka.getSpendName());
//            h.setParentId(callBacka.getParentId());
//            h.setParentName(callBacka.getParentName());
//            h.setPriority(callBacka.getPriority());
//            h.setAbTypeId(callBackb.getAbTypeId());
//            h.setUserInfoId(userInfoId);
//
//            boolean b = DevRing.tableManager(HbirdUserCommUseSpend.class).insertOne(h);

            String temp = (String) SharedPreferencesUtil.get("userId_" + userInfoId + "abTypeId_" + abTypeId + "type_spend", "");
            List<SystemBiaoqReturn.ResultBean.LabelBean.SpendBean> list = JSON.parseArray(temp, SystemBiaoqReturn.ResultBean.LabelBean.SpendBean.class);
            if (list == null) {
                list = new ArrayList<>();
            }
            SystemBiaoqReturn.ResultBean.LabelBean.SpendBean bean = new SystemBiaoqReturn.ResultBean.LabelBean.SpendBean();
            bean.setId(Integer.parseInt(callBacka.getId()));
            bean.setIcon(callBacka.getIcon());
            bean.setSpendName(callBacka.getSpendName());
            bean.setPriority(callBacka.getPriority());
            list.add(bean);

            SharedPreferencesUtil.put("userId_" + userInfoId + "abTypeId_" + abTypeId + "type_spend", list.toString());
        }
    }
}