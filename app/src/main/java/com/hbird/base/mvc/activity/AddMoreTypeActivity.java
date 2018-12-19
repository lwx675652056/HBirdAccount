package com.hbird.base.mvc.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.hbird.base.R;
import com.hbird.base.mvc.adapter.MyExpandableListView2Adapter;
import com.hbird.base.mvc.adapter.MyExpandableListViewAdapter;
import com.hbird.base.mvc.base.baseActivity.BaseActivityPresenter;
import com.hbird.base.mvc.bean.AddMoreTypeBean;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.ReturnBean.BiaoQianReturn;
import com.hbird.base.mvc.bean.ReturnBean.ShouRuAddMoreTypeBean;
import com.hbird.base.mvc.bean.ReturnBean.ShouRuTagReturn;
import com.hbird.base.mvc.bean.ReturnBean.ZhiChuTagReturn;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvp.model.entity.table.HbirdUserCommUseIncome;
import com.hbird.base.mvp.model.entity.table.HbirdUserCommUseSpend;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.JSONUtil;
import com.hbird.base.util.SPUtil;
import com.ljy.devring.DevRing;
import com.ljy.devring.image.support.GlideApp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Liul on 2018/7/4.
 * 添加 支出、收入 类别
 */

public class AddMoreTypeActivity extends BaseActivity<BaseActivityPresenter> implements View.OnClickListener{
    @BindView(R.id.iv_back)
    ImageView mBack;
    @BindView(R.id.center_title)
    TextView mCenterTitle;
    @BindView(R.id.right_title2)
    TextView mRightTitle;
    @BindView(R.id.expandableList)
    ExpandableListView expandableListView;
    @BindView(R.id.ll_choose_items)
    LinearLayout mChooseItem;
    @BindView(R.id.iv_img)
    ImageView mImg;
    @BindView(R.id.rl_img_bg)
    RelativeLayout mImgBg;
    @BindView(R.id.tv_choose_type)
    TextView mChooseType;


    private List<List<AddMoreTypeBean.ResultBean.AllListBean.SpendTypeSonsBean>> itemList;
    private String title;
    private List<List<ShouRuAddMoreTypeBean.ResultBean.AllListBean.IncomeTypeSonsBean>> item2List;
    private AddMoreTypeBean.ResultBean.AllListBean.SpendTypeSonsBean callBacka;
    private ShouRuAddMoreTypeBean.ResultBean.AllListBean.IncomeTypeSonsBean callBackb;
    private ArrayList<String> list;
    private Integer accountBookId;
    private String bookIdType;
    private Integer types;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_add_more_type;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        title = getIntent().getStringExtra("TITLE");
        if(TextUtils.equals(title,"收入")){
            mCenterTitle.setText("添加收入类别");
            mImgBg.setBackgroundResource(R.drawable.shape_cycle_yellow);
        }else {
            mCenterTitle.setText("添加支出类别");
            mImgBg.setBackgroundResource(R.drawable.shape_cycle_blue);
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        //返回的bean对象 AddMoreTypeBean已创建好 后面调用接口直接用
        String id = SPUtil.getPrefString(this, com.hbird.base.app.constant.CommonTag.CURRENT_ACCOUNT_ID, "0");
        accountBookId = Integer.valueOf(id);
        bookIdType = SPUtil.getPrefString(this, com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_TYPE, "0");
        types = Integer.valueOf(bookIdType);
        getDate();
        list = getIntent().getStringArrayListExtra("object");
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
        switch (view.getId()){
            case R.id.iv_back:
                playVoice(R.raw.changgui02);
                finish();
                break;
            case R.id.right_title2:
                //showMessage("完成");
                //调用添加或支出收入类别接口
                playVoice(R.raw.changgui02);
                getAccountTypes();
                break;
        }
    }

    private void getDate() {
        String token = SPUtil.getPrefString(this, CommonTag.GLOABLE_TOKEN, "");
        showProgress("");
        if(TextUtils.equals(title,"收入")){

            NetWorkManager.getInstance().setContext(this)
                    .getIncomeLabels(accountBookId, token, new NetWorkManager.CallBack() {
                        @Override
                        public void onSuccess(BaseReturn b) {
                            hideProgress();
                            ShouRuTagReturn b1 = (ShouRuTagReturn) b;
                            String json = new Gson().toJson(b1);
                            set2Date(json);
                        }

                        @Override
                        public void onError(String s) {
                            hideProgress();
                            showMessage(s);
                        }
                    });
           /* NetWorkManager.getInstance().setContext(this)
                    .getShouRuTag(token, new NetWorkManager.CallBack() {
                        @Override
                        public void onSuccess(BaseReturn b) {
                            hideProgress();
                            ShouRuTagReturn b2 = (ShouRuTagReturn) b;
                            String json = new Gson().toJson(b2);
                            set2Date(json);
                        }

                        @Override
                        public void onError(String s) {
                            hideProgress();
                            showMessage(s);
                        }
                    });*/
        }else {

            NetWorkManager.getInstance().setContext(this)
                    .getSpendLabels(types, token, new NetWorkManager.CallBack() {
                        @Override
                        public void onSuccess(BaseReturn b) {
                            hideProgress();
                            ZhiChuTagReturn b2 = (ZhiChuTagReturn) b;
                            String json = new Gson().toJson(b2);
                            setDate(json);
                        }

                        @Override
                        public void onError(String s) {
                            hideProgress();
                            showMessage(s);
                        }
                    });
           /* NetWorkManager.getInstance().setContext(this)
                    .getZhiChuTag(token, new NetWorkManager.CallBack() {
                        @Override
                        public void onSuccess(BaseReturn b) {
                            hideProgress();
                            ZhiChuTagReturn b1 = (ZhiChuTagReturn) b;
                            String json = new Gson().toJson(b1);
                            setDate(json);
                        }

                        @Override
                        public void onError(String s) {
                            hideProgress();
                            showMessage(s);
                        }
                    });*/
        }

    }
    private void setDate(String date) {
        AddMoreTypeBean bean = new JSONUtil<String, AddMoreTypeBean>().JsonStrToObject(date, AddMoreTypeBean.class);
        final List<AddMoreTypeBean.ResultBean.AllListBean> groupList = bean.getResult().getAllList();//组 级项目
        //子级项目集合
        itemList = new ArrayList<>();
        for (int i=0;i<groupList.size();i++){
            List<AddMoreTypeBean.ResultBean.AllListBean.SpendTypeSonsBean> spendTypeSons = groupList.get(i).getSpendTypeSons();
            itemList.add(spendTypeSons);
        }
        final MyExpandableListViewAdapter adapter = new MyExpandableListViewAdapter(this, groupList, itemList,list);
        expandableListView.setAdapter(adapter);
        mChooseItem.setVisibility(View.VISIBLE);
        if(groupList.get(0) != null){
            callBacka = groupList.get(0).getSpendTypeSons().get(0);
        }
        if(callBacka!=null){
            mChooseType.setText(callBacka.getSpendName());
            GlideApp.with(AddMoreTypeActivity.this)
                    .load(callBacka.getIcon())
                    .skipMemoryCache(false)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .override(50,50)
                    .into(mImg);
            //点选后的数据回调
            adapter.setMyOnclickListener(new MyExpandableListViewAdapter.callBackInstance() {
                @Override
                public void setOnCallBack(int groupPosition, int position) {
                    playVoice(R.raw.typevoice);
                    callBacka = groupList.get(groupPosition).getSpendTypeSons().get(position);
                    mChooseItem.setVisibility(View.VISIBLE);
                    mChooseType.setText(callBacka.getSpendName());
                    GlideApp.with(AddMoreTypeActivity.this)
                            .load(callBacka.getIcon())
                            .skipMemoryCache(false)
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .override(50,50)
                            .into(mImg);
                }

            });
        }

        int groupCount = expandableListView.getCount();
        for (int i=0; i<groupCount; i++) {
            expandableListView.expandGroup(i);
        };
        // 隐藏分组指示器
        expandableListView.setGroupIndicator(null);
        // 默认展开第一组
        // expandableListView.expandGroup(0);
    }
    private void set2Date(String date) {
        ShouRuAddMoreTypeBean bean = new JSONUtil<String, ShouRuAddMoreTypeBean>().JsonStrToObject(date, ShouRuAddMoreTypeBean.class);
        final List<ShouRuAddMoreTypeBean.ResultBean.AllListBean> groupList = bean.getResult().getAllList();//组 级项目
        //子级项目集合
        item2List = new ArrayList<>();
        for (int i=0;i<groupList.size();i++){
            List<ShouRuAddMoreTypeBean.ResultBean.AllListBean.IncomeTypeSonsBean> incomeTypeSons = groupList.get(i).getIncomeTypeSons();
            item2List.add(incomeTypeSons);
        }
        final MyExpandableListView2Adapter adapter = new MyExpandableListView2Adapter(this, groupList, item2List,list);
        expandableListView.setAdapter(adapter);
        if(null != groupList.get(0)){
            callBackb = groupList.get(0).getIncomeTypeSons().get(0);
        }
        if(null != callBackb){
            mChooseItem.setVisibility(View.VISIBLE);
            mChooseType.setText(callBackb.getIncomeName());
            GlideApp.with(AddMoreTypeActivity.this)
                    .load(callBackb.getIcon())
                    .skipMemoryCache(false)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .override(50,50)
                    .into(mImg);
            //点选后的数据回调
            adapter.setMyOnclickListener(new MyExpandableListView2Adapter.callBackInstance() {

                @Override
                public void setOnCallBack(int groupPosition, int position) {
                    playVoice(R.raw.typevoice);
                    callBackb = groupList.get(groupPosition).getIncomeTypeSons().get(position);
                    mChooseItem.setVisibility(View.VISIBLE);
                    mChooseType.setText(callBackb.getIncomeName());
                    GlideApp.with(AddMoreTypeActivity.this)
                            .load(callBackb.getIcon())
                            .skipMemoryCache(false)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .override(50,50)
                            .into(mImg);
                }

            });
        }

        int groupCount = expandableListView.getCount();
        for (int i=0; i<groupCount; i++) {
            expandableListView.expandGroup(i);
        }
        ;
        // 隐藏分组指示器
        expandableListView.setGroupIndicator(null);
        // 默认展开第一组
        // expandableListView.expandGroup(0);
    }


    private void getAccountTypes() {
        if(TextUtils.equals(title,"收入")){
            if(null==callBackb){
                showMessage("请选择要添加的类别");
                return;
            }
        }else {
            if(null==callBacka){
                showMessage("请选择要添加的类别");
                return;
            }
        }
        String token = SPUtil.getPrefString(this, CommonTag.GLOABLE_TOKEN, "");
        String a = SPUtil.getPrefString(this, com.hbird.base.app.constant.CommonTag.CURRENT_ACCOUNT_ID, "0");
        showProgress("");
        if(TextUtils.equals(title,"收入")){
            //添加用户常用收入类目
            NetWorkManager.getInstance().setContext(this)
                    .postShouRuType(callBackb.getId(), Integer.parseInt(a), token, new NetWorkManager.CallBack() {
                        @Override
                        public void onSuccess(BaseReturn b) {
                            hideProgress();
                            BiaoQianReturn b1 = (BiaoQianReturn) b;
                            String version = b1.getResult().getVersion();
                            SPUtil.setPrefString(AddMoreTypeActivity.this, com.hbird.base.app.constant.CommonTag.LABEL_VERSION,version);
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
        }else {
            //添加用户常用支出类目
            NetWorkManager.getInstance().setContext(this)
                    .postZhiChuType(callBacka.getId(), types, token, new NetWorkManager.CallBack() {
                        @Override
                        public void onSuccess(BaseReturn b) {
                            hideProgress();
                            BiaoQianReturn b1 = (BiaoQianReturn) b;
                            String version = b1.getResult().getVersion();
                            SPUtil.setPrefString(AddMoreTypeActivity.this, com.hbird.base.app.constant.CommonTag.LABEL_VERSION,version);
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
        if(TextUtils.equals(title,"收入")){
            HbirdUserCommUseIncome h = new HbirdUserCommUseIncome();
            h.setIcon(callBackb.getIcon());
            h.setMark(callBackb.getMark());
            h.setId(callBackb.getId());
            h.setIncomeName(callBackb.getIncomeName());
            h.setParentId(callBackb.getParentId());
            h.setParentName(callBackb.getParentName());
            h.setPriority(callBackb.getPriority());
            h.setAbTypeId(callBackb.getAbTypeId());
            h.setUserInfoId(userInfoId);
            boolean b = DevRing.tableManager(HbirdUserCommUseIncome.class).insertOne(h);
        }else {
            HbirdUserCommUseSpend h = new HbirdUserCommUseSpend();
            h.setIcon(callBacka.getIcon());
            h.setMark(callBacka.getMark());
            h.setId(callBacka.getId());
            h.setSpendName(callBacka.getSpendName());
            h.setParentId(callBacka.getParentId());
            h.setParentName(callBacka.getParentName());
            h.setPriority(callBacka.getPriority());
            h.setAbTypeId(callBackb.getAbTypeId());
            h.setUserInfoId(userInfoId);

            boolean b = DevRing.tableManager(HbirdUserCommUseSpend.class).insertOne(h);
        }
    }

}
