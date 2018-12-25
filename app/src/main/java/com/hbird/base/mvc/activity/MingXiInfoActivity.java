package com.hbird.base.mvc.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hbird.base.R;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.RequestBean.OffLine2Req;
import com.hbird.base.mvc.bean.RequestBean.OffLineReq;
import com.hbird.base.mvc.bean.ReturnBean.GloableReturn;
import com.hbird.base.mvc.bean.ReturnBean.PullSyncDateReturn;
import com.hbird.base.mvc.bean.ReturnBean.SingleReturn;
import com.hbird.base.mvc.bean.indexBaseListBean;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvp.model.entity.table.WaterOrderCollect;
import com.hbird.base.mvp.presenter.base.BasePresenter;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.DBUtil;
import com.hbird.base.util.DateUtil;
import com.hbird.base.util.DateUtils;
import com.hbird.base.util.SPUtil;
import com.hbird.util.Utils;
import com.ljy.devring.DevRing;
import com.ljy.devring.util.NetworkUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Liul on 2018/7/9.
 */

public class MingXiInfoActivity extends BaseActivity<BasePresenter> implements View.OnClickListener{
    @BindView(R.id.iv_back)
    ImageView mBack;
    @BindView(R.id.center_title)
    TextView mCenterTitle;
    @BindView(R.id.right_title2)
    TextView mRightTitle;
    @BindView(R.id.iv_icon)
    ImageView mIcon;
    @BindView(R.id.tv_types)
    TextView mTypes;
    @BindView(R.id.tv_pay_num)
    TextView mPayNum;
    @BindView(R.id.tv_riqi)
    TextView mRiQi;
    @BindView(R.id.tv_leixing)
    TextView mLeiXing;
    @BindView(R.id.iv_mood)
    ImageView mMood;
    @BindView(R.id.tv_beizhu)
    TextView mBeiZhu;
    @BindView(R.id.tv_editor)
    RelativeLayout mEditor;
    @BindView(R.id.tv_delete)
    RelativeLayout mDelete;
    private String id;
    @BindView(R.id.rl_background)
    RelativeLayout mBackground;
    @BindView(R.id.text_bg)
    LinearLayout mTextBg;
    @BindView(R.id.ll_xinqings)
    LinearLayout mXinQing;
    @BindView(R.id.view_line)
    View mViewLine;
    @BindView(R.id.tv_jlr)
    TextView mTvJlr;
    @BindView(R.id.img_head)
    com.hbird.base.mvc.widget.cycleView mImgHeader;
    @BindView(R.id.tv_fenlei)
    TextView mFenLei;
    @BindView(R.id.ll_feilei_bg)
    LinearLayout mFeileiBg;


    private SingleReturn bean;
    private WaterOrderCollect waterOrderCollect;
    private boolean comeInForLogin;
    private String accountId;
    private String token;
    private Boolean a;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_mingxi_info;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        accountId = SPUtil.getPrefString(MingXiInfoActivity.this, com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, "");
    }
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(indexBaseListBean event) {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mCenterTitle.setText("明细详情");
        mRightTitle.setVisibility(View.GONE);
        id = getIntent().getStringExtra("ID");
//        String data = getIntent().getStringExtra("DATA");
//        indexBaseListBean = new Gson().fromJson(data, indexBaseListBean.class);
        setDates(id);
    }

    private void setDates(String id) {
       /* String token = SPUtil.getPrefString(this, CommonTag.GLOABLE_TOKEN, "");
        showProgress("");
        NetWorkManager.getInstance().setContext(this)
                .getAccountInfo(id, token, new NetWorkManager.CallBack() {

                    @Override
                    public void onSuccess(BaseReturn b) {
                        hideProgress();
                        bean = (SingleReturn) b;
                        setDate(bean);
                    }

                    @Override
                    public void onError(String s) {
                        hideProgress();
                        showMessage(s);
                    }
                });*/
       //通过id查询本地数据库
        //String sql = "SELECT * FROM WATER_ORDER_COLLECT where ID = '"+id+"'";
//        String sql = "SELECT wo.id,wo.money,wo.account_book_id,wo.order_type,wo.is_staged,wo.spend_happiness,wo.use_degree,wo.type_pid,wo.type_pname,wo.type_id,wo.type_name,wo.picture_url,wo.create_date,wo.charge_date,wo.remark, ( CASE wo.order_type WHEN 1 THEN st.icon WHEN 2 THEN it.icon ELSE NULL END ) AS icon FROM WATER_ORDER_COLLECT wo LEFT JOIN HBIRD_SPEND_TYPE st ON wo.type_id = st.id LEFT JOIN HBIRD_INCOME_TYPE it ON wo.type_id = it.id where wo.ID = '"+id+"'";
        String sql = "SELECT  id, money, account_book_id, order_type, is_staged, spend_happiness, use_degree" +
                ", type_pid, type_pname, type_id, type_name, picture_url, create_date, charge_date" +
                ", remark, USER_PRIVATE_LABEL_ID, REPORTER_AVATAR, REPORTER_NICK_NAME,AB_NAME,icon FROM WATER_ORDER_COLLECT " +
                " where ID = '"+id+"'";

        Cursor cursor = DevRing.tableManager(WaterOrderCollect.class).rawQuery(sql, null);
        List<WaterOrderCollect> dbList = new ArrayList<>();
        dbList.clear();
        if (null != cursor) {
            List<WaterOrderCollect> waterOrderCollects = DBUtil.changeToList(cursor, dbList, WaterOrderCollect.class);
            waterOrderCollect = waterOrderCollects.get(0);
        }

        String typeName = waterOrderCollect.getTypeName();
        double money = waterOrderCollect.getMoney();
        long chargeDate =waterOrderCollect.getChargeDate().getTime();
        String yearMonthDay = DateUtils.getMonthDay(chargeDate);
        String day = DateUtils.getDay(chargeDate);
        String weekDay = DateUtil.dateToWeek(day);
        String remark =waterOrderCollect.getRemark();
        String iconUrl =waterOrderCollect.getIcon();
        int orderType =waterOrderCollect.getOrderType();
        String leixing="";
        if(orderType==1){
            leixing = "支出";
            mBackground.setBackground(getResources().getDrawable(R.drawable.shape_cycle_blue));
            mTypes.setTextColor(getResources().getColor(R.color.bg_A1C7F3));
            mPayNum.setTextColor(getResources().getColor(R.color.bg_A1C7F3));
            mFeileiBg.setBackgroundResource(R.drawable.shape_editor3_infos);
            //mTextBg.setBackgroundResource(R.color.bg_A1C7F3);
        }else {
            leixing = "收入";
            mBackground.setBackground(getResources().getDrawable(R.drawable.shape_cycle_yellow));
            mTypes.setTextColor(getResources().getColor(R.color.bg_yellow));
            mPayNum.setTextColor(getResources().getColor(R.color.bg_yellow));
            mFeileiBg.setBackgroundResource(R.drawable.shape_editor4_infos);
            //mTextBg.setBackgroundResource(R.color.bg_yellow);
            mXinQing.setVisibility(View.GONE);
            mViewLine.setVisibility(View.GONE);
        }
        Integer spendHappiness =waterOrderCollect.getSpendHappiness();
        if(null==spendHappiness){
            mMood.setVisibility(View.GONE);
        }else {
            if(spendHappiness==0){
                mMood.setImageResource(R.mipmap.icon_mood_happy_blue);
                mMood.setVisibility(View.VISIBLE);
            }else if(spendHappiness==1){
                mMood.setImageResource(R.mipmap.icon_mood_normal_blue);
                mMood.setVisibility(View.VISIBLE);
            }else if(spendHappiness==2){
                mMood.setImageResource(R.mipmap.icon_mood_blue);
                mMood.setVisibility(View.VISIBLE);
            }else {
                mMood.setVisibility(View.GONE);
            }
        }
        mFenLei.setText("# 所属账本："+waterOrderCollect.getAbName()+"账本 #");
        mTvJlr.setText(waterOrderCollect.getReporterNickName());
        mTypes.setText(typeName);
        mPayNum.setText(money+"");
        mRiQi.setText(yearMonthDay +" "+ weekDay);
        mLeiXing.setText(leixing);
        if(null==remark){
            mBeiZhu.setText("无");
        }else {
            mBeiZhu.setText(remark);
        }
        Glide.with(this).load(waterOrderCollect.getReporterAvatar()).into(mImgHeader);
        Glide.with(this).load(iconUrl).into(mIcon);

    }

    private void setDate(SingleReturn event) {
        String typeName = event.getResult().getTypeName();
        double money = event.getResult().getMoney();
        long chargeDate = event.getResult().getChargeDate();
        String yearMonthDay = DateUtils.getMonthDay(chargeDate);
        String day = DateUtils.getDay(chargeDate);
        String weekDay = DateUtil.dateToWeek(day);
        String remark = event.getResult().getRemark();
        String iconUrl = event.getResult().getIcon();
        int orderType = event.getResult().getOrderType();
        String leixing="";
        if(orderType==1){
            leixing = "支出";
            mBackground.setBackground(getResources().getDrawable(R.drawable.shape_cycle_blue));
            mTypes.setTextColor(getResources().getColor(R.color.bg_A1C7F3));
            mPayNum.setTextColor(getResources().getColor(R.color.bg_A1C7F3));
            //mTextBg.setBackgroundResource(R.color.bg_A1C7F3);
        }else {
            leixing = "收入";
            mBackground.setBackground(getResources().getDrawable(R.drawable.shape_cycle_yellow));
            mTypes.setTextColor(getResources().getColor(R.color.bg_yellow));
            mPayNum.setTextColor(getResources().getColor(R.color.bg_yellow));
            //mTextBg.setBackgroundResource(R.color.bg_yellow);
            mXinQing.setVisibility(View.GONE);
            mViewLine.setVisibility(View.GONE);
        }
        Integer spendHappiness = event.getResult().getSpendHappiness();
        if(null==spendHappiness){
            mMood.setVisibility(View.GONE);
        }else {
            if(spendHappiness==0){
                mMood.setImageResource(R.mipmap.icon_mood_happy_blue);
                mMood.setVisibility(View.VISIBLE);
            }else if(spendHappiness==1){
                mMood.setImageResource(R.mipmap.icon_mood_normal_blue);
                mMood.setVisibility(View.VISIBLE);
            }else if(spendHappiness==2){
                mMood.setImageResource(R.mipmap.icon_mood_blue);
                mMood.setVisibility(View.VISIBLE);
            }else {
                mMood.setVisibility(View.GONE);
            }
        }

        mTypes.setText(typeName);
        mPayNum.setText(money+"");
        mRiQi.setText(yearMonthDay +" "+ weekDay);
        mLeiXing.setText(leixing);
        if(null==remark){
            mBeiZhu.setText("备注：无");
        }else {
            mBeiZhu.setText("备注："+remark);
        }

        Glide.with(this).load(iconUrl).into(mIcon);
    }

    @Override
    protected void initEvent() {
        mBack.setOnClickListener(this);
        mEditor.setOnClickListener(this);
        mDelete.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                playVoice(R.raw.changgui02);
                finish();
                break;
            case R.id.tv_delete:
                //调用接口的删除
                /*showProgress("");
                NetWorkManager.getInstance().setContext(this)
                        .clearDingDanInfo(id, SPUtil.getPrefString(this, CommonTag.GLOABLE_TOKEN, ""), new NetWorkManager.CallBack() {
                            @Override
                            public void onSuccess(BaseReturn b) {
                                hideProgress();
                                GloableReturn gloableBean = (GloableReturn) b;
                                String code = gloableBean.getCode();
                                if(TextUtils.equals(code,"200")){
                                    setResult(102);
                                    finish();
                                }
                            }

                            @Override
                            public void onError(String s) {
                                hideProgress();
                                showMessage(s);
                            }
                        });*/
                playVoice(R.raw.changgui02);
                String accoutBookId = SPUtil.getPrefString(MingXiInfoActivity.this, com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, "");
                a = DBUtil.deleteOnesDate(id,accoutBookId);
                //刪除完 同步
                pullToSyncDate();

                break;
            case R.id.tv_editor:
                //showMessage("编辑");
                playVoice(R.raw.changgui02);
                Intent intent = new Intent();
                intent.setClass(this,MingXiEditorActivity.class);
                intent.putExtra("JSONBEAN",new Gson().toJson(waterOrderCollect));
                startActivityForResult(intent,103);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==103 && resultCode==104){
            //刷新界面数据
            setDates(id);
        }
    }
    private void pullToSyncDate() {
        //判断当前网络状态
        boolean netWorkAvailable = NetworkUtil.isNetWorkAvailable(MingXiInfoActivity.this);
        if(!netWorkAvailable){
            if(a){
                setResult(102);
                finish();
            }
            return;
        }
        token = SPUtil.getPrefString(this, CommonTag.GLOABLE_TOKEN, "");
        String mobileDevice = Utils.getDeviceInfo(MingXiInfoActivity.this);
        comeInForLogin = SPUtil.getPrefBoolean(MingXiInfoActivity.this, com.hbird.base.app.constant.CommonTag.OFFLINEPULL_FIRST_LOGIN, false);
        NetWorkManager.getInstance().setContext(MingXiInfoActivity.this)
                .postPullToSyncDate(mobileDevice,comeInForLogin, token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        PullSyncDateReturn b1 = (PullSyncDateReturn) b;
                        String synDate = b1.getResult().getSynDate();
                        long L=0;
                        if(null!=synDate){
                            try {
                                L = Long.parseLong(synDate);
                            }catch (Exception e){

                            }
                        }
                        SPUtil.setPrefLong(MingXiInfoActivity.this, com.hbird.base.app.constant.CommonTag.SYNDATE,L);
                        List<PullSyncDateReturn.ResultBean.SynDataBean> synData = b1.getResult().getSynData();
                        //插入本地数据库
                        DBUtil.insertLocalDB(synData);
                        SPUtil.setPrefBoolean(MingXiInfoActivity.this, com.hbird.base.app.constant.CommonTag.OFFLINEPULL_FIRST, false);
//                        List<WaterOrderCollect> list = DevRing.tableManager(WaterOrderCollect.class).loadAll();
//                        showMessage(list.size() + "");
                        pushOffLine();
                    }

                    @Override
                    public void onError(String s) {
                        //showMessage(s);
                        if(a){
                            setResult(102);
                            finish();
                        }
                    }
                });
    }
    private void pushOffLine() {
        OffLineReq req = new OffLineReq();
        String deviceId = Utils.getDeviceInfo(MingXiInfoActivity.this);
        req.setMobileDevice(deviceId);
        Long time = SPUtil.getPrefLong(MingXiInfoActivity.this, com.hbird.base.app.constant.CommonTag.SYNDATE, new Date().getTime());
        String times = String.valueOf(time);
        req.setSynDate(times);

        //本地数据库查找未上传数据 上传至服务器
        String sql = "SELECT * FROM WATER_ORDER_COLLECT wo where wo.ACCOUNT_BOOK_ID = " + accountId + " AND wo.UPDATE_DATE >= " + time;
        Cursor cursor = DevRing.tableManager(WaterOrderCollect.class).rawQuery(sql, null);
        List<OffLineReq.SynDataBean> myList = new ArrayList<>();
        myList.clear();
        if (null != cursor) {
            myList = DBUtil.changeToListPull(cursor, myList, OffLineReq.SynDataBean.class);
        }
        List<OffLine2Req.SynDataBean> myList2 = new ArrayList<>();
        myList2.clear();
        OffLine2Req req2 = new OffLine2Req();
        req2.setMobileDevice(req.getMobileDevice());
        req2.setSynDate(req.getSynDate());
        if(myList!=null){
            for (int i = 0; i < myList.size(); i++) {
                OffLineReq.SynDataBean s1 = myList.get(i);
                OffLine2Req.SynDataBean synDataBean = new OffLine2Req.SynDataBean();
                synDataBean.setId(s1.getId());
                synDataBean.setAccountBookId(s1.getAccountBookId());
                if(s1.getChargeDate()!=null){
                    synDataBean.setChargeDate(s1.getChargeDate().getTime());
                }
                synDataBean.setCreateBy(s1.getCreateBy());
                if(s1.getCreateDate()!=null){
                    synDataBean.setCreateDate(s1.getCreateDate().getTime());
                }
                synDataBean.setCreateName(s1.getCreateName());
                if(s1.getDelDate()!=null){
                    synDataBean.setDelDate(s1.getDelDate().getTime());
                }

                synDataBean.setDelflag(s1.getDelflag());
                synDataBean.setIcon(s1.getIcon());
                synDataBean.setIsStaged(s1.getIsStaged());
                synDataBean.setMoney(s1.getMoney());
                synDataBean.setOrderType(s1.getOrderType());
                synDataBean.setParentId(s1.getParentId());
                synDataBean.setPictureUrl(s1.getPictureUrl());
                synDataBean.setRemark(s1.getRemark());
                synDataBean.setSpendHappiness(s1.getSpendHappiness());
                synDataBean.setTypeId(s1.getTypeId());
                synDataBean.setTypeName(s1.getTypeName());
                if(s1.getUpdateDate()!=null){
                    synDataBean.setUpdateDate(s1.getUpdateDate().getTime());
                }
                synDataBean.setTypePname(s1.getTypePname());
                synDataBean.setUpdateBy(s1.getUpdateBy());
                synDataBean.setUseDegree(s1.getUseDegree());
                synDataBean.setUpdateName(s1.getUpdateName());
                synDataBean.setUserPrivateLabelId(s1.getUserPrivateLabelId());
                myList2.add(synDataBean);
            }
            req2.setSynData(myList2);
        }


        if(myList2!=null && myList2.size()>0){
            req2.setSynData(myList2);
        }
        String jsonInfo = new Gson().toJson(req2);
        NetWorkManager.getInstance().setContext(MingXiInfoActivity.this)
                .pushOffLineToFwq(jsonInfo, token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        GloableReturn b1 = (GloableReturn) b;
                        if(a){
                            setResult(102);
                            finish();
                        }
                    }

                    @Override
                    public void onError(String s) {
                        if(a){
                            setResult(102);
                            finish();
                        }
                    }
                });
    }



}
