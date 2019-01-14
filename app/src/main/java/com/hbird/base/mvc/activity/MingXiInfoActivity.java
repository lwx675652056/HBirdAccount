package com.hbird.base.mvc.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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
import com.hbird.base.mvc.bean.ReturnBean.PullSyncDateReturn;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvp.model.entity.table.WaterOrderCollect;
import com.hbird.base.mvp.presenter.base.BasePresenter;
import com.hbird.base.mvp.view.activity.base.BaseActivity;
import com.hbird.base.util.DBUtil;
import com.hbird.base.util.DateUtil;
import com.hbird.base.util.DateUtils;
import com.hbird.base.util.SPUtil;
import com.hbird.common.Constants;
import com.hbird.ui.charge.ActEditCharge;
import com.hbird.util.Utils;
import com.ljy.devring.DevRing;
import com.ljy.devring.util.NetworkUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import sing.util.ToastUtil;

/**
 * Created by Liul on 2018/7/9.
 */

public class MingXiInfoActivity extends BaseActivity<BasePresenter> implements View.OnClickListener {
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
    @BindView(R.id.tv_account)
    TextView tvAccount;

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

    @Override
    protected void initData(Bundle savedInstanceState) {
        mCenterTitle.setText("明细详情");
        mRightTitle.setVisibility(View.GONE);
        id = getIntent().getStringExtra("ID");
        setDates(id);
    }

    private void setDates(String id) {
        String sql = "SELECT * FROM WATER_ORDER_COLLECT where ID = '" + id + "'";

        Cursor cursor = DevRing.tableManager(WaterOrderCollect.class).rawQuery(sql, null);
        List<WaterOrderCollect> dbList = new ArrayList<>();
        dbList.clear();
        if (null != cursor) {
            List<WaterOrderCollect> waterOrderCollects = DBUtil.changeToList(cursor, dbList, WaterOrderCollect.class);
            waterOrderCollect = waterOrderCollects.get(0);
        }

        String typeName = waterOrderCollect.getTypeName();
        double money = waterOrderCollect.getMoney();
        long chargeDate = waterOrderCollect.getChargeDate().getTime();
        String yearMonthDay = DateUtils.getMonthDay(chargeDate);
        String day = DateUtils.getDay(chargeDate);
        String weekDay = DateUtil.dateToWeek(day);
        String remark = waterOrderCollect.getRemark();
        String iconUrl = waterOrderCollect.getIcon();
        int orderType = waterOrderCollect.getOrderType();
        String leixing = "";
        if (orderType == 1) {
            leixing = "支出";
            mBackground.setBackground(getResources().getDrawable(R.drawable.shape_cycle_blue));
            mPayNum.setTextColor(Color.parseColor("#A1C7F3"));
            mFeileiBg.setBackgroundResource(R.drawable.shape_editor3_infos);
            //mTextBg.setBackgroundResource(R.color.bg_A1C7F3);
        } else {
            leixing = "收入";
            mBackground.setBackground(getResources().getDrawable(R.drawable.shape_cycle_yellow));
            mPayNum.setTextColor(Color.parseColor("#FF7A45"));
            mFeileiBg.setBackgroundResource(R.drawable.shape_editor4_infos);
            //mTextBg.setBackgroundResource(R.color.bg_yellow);
            mXinQing.setVisibility(View.GONE);
            mViewLine.setVisibility(View.GONE);
        }
        Integer spendHappiness = waterOrderCollect.getSpendHappiness();
        if (null == spendHappiness) {
            mMood.setVisibility(View.GONE);
        } else {
            if (spendHappiness == 0) {
                mMood.setImageResource(R.mipmap.icon_mood_happy_blue);
                mMood.setVisibility(View.VISIBLE);
            } else if (spendHappiness == 1) {
                mMood.setImageResource(R.mipmap.icon_mood_normal_blue);
                mMood.setVisibility(View.VISIBLE);
            } else if (spendHappiness == 2) {
                mMood.setImageResource(R.mipmap.icon_mood_blue);
                mMood.setVisibility(View.VISIBLE);
            } else {
                mMood.setVisibility(View.GONE);
            }
        }
        mFenLei.setText("# 所属账本：" + waterOrderCollect.getAbName() + "账本 #");
        mTvJlr.setText(waterOrderCollect.getReporterNickName());
        mTypes.setText(typeName);
        mPayNum.setText(money + "");
        mRiQi.setText(yearMonthDay + " " + weekDay);
        mLeiXing.setText(leixing);
        tvAccount.setText(waterOrderCollect.getAssetsName());
        if (null == remark) {
            mBeiZhu.setText("");
        } else {
            mBeiZhu.setText(remark);
        }
        Glide.with(this).load(waterOrderCollect.getReporterAvatar()).into(mImgHeader);
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
        switch (view.getId()) {
            case R.id.iv_back:
                playVoice(R.raw.changgui02);
                finish();
                break;
            case R.id.tv_delete:
                //调用接口的删除
                playVoice(R.raw.changgui02);
                a = DBUtil.updateOneDate(waterOrderCollect);
                if (a) {
                    //刪除完 同步
                    pullToSyncDate();
                } else {
                    setResult(102);
                    finish();
                }
                break;
            case R.id.tv_editor:// 编辑
                playVoice(R.raw.changgui02);
//                Intent intent = new Intent();
//                intent.setClass(this, MingXiEditorActivity.class);
//                intent.putExtra("JSONBEAN", new Gson().toJson(waterOrderCollect));
//                startActivityForResult(intent, 103);
                Intent intent = new Intent(this, ActEditCharge.class);
                intent.putExtra(Constants.START_INTENT_A, waterOrderCollect);
                startActivityForResult(intent,103);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 103 && resultCode == 104) {
            //刷新界面数据
            setDates(id);
        }
    }

    private void pullToSyncDate() {
        //判断当前网络状态
        boolean netWorkAvailable = NetworkUtil.isNetWorkAvailable(MingXiInfoActivity.this);
        if (!netWorkAvailable) {
            setResult(102);
            finish();
            return;
        }
        token = SPUtil.getPrefString(this, CommonTag.GLOABLE_TOKEN, "");
        String deviceId = Utils.getDeviceInfo(MingXiInfoActivity.this);
        comeInForLogin = SPUtil.getPrefBoolean(MingXiInfoActivity.this, com.hbird.base.app.constant.CommonTag.OFFLINEPULL_FIRST_LOGIN, false);
        NetWorkManager.getInstance().setContext(MingXiInfoActivity.this).postPullToSyncDate(deviceId, comeInForLogin, token, new NetWorkManager.CallBack() {
            @Override
            public void onSuccess(BaseReturn b) {
                PullSyncDateReturn b1 = (PullSyncDateReturn) b;
                String synDate = b1.getResult().getSynDate();
                long L = 0;
                if (null != synDate) {
                    try {
                        L = Long.parseLong(synDate);
                    } catch (Exception e) {

                    }
                }
                SPUtil.setPrefLong(MingXiInfoActivity.this, com.hbird.base.app.constant.CommonTag.SYNDATE, L);
                List<PullSyncDateReturn.ResultBean.SynDataBean> synData = b1.getResult().getSynData();
                //插入本地数据库
                DBUtil.insertLocalDB(synData);
                SPUtil.setPrefBoolean(MingXiInfoActivity.this, com.hbird.base.app.constant.CommonTag.OFFLINEPULL_FIRST, false);

                pushOffLine();
            }

            @Override
            public void onError(String s) {
                ToastUtil.showShort(s);
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
        String sql = "SELECT * FROM WATER_ORDER_COLLECT where UPDATE_DATE >= " + time;
        Cursor cursor = DevRing.tableManager(WaterOrderCollect.class).rawQuery(sql, null);

        List<WaterOrderCollect> l = new ArrayList<>();
        if (null != cursor) {
            l = DBUtil.changeToList(cursor, l, WaterOrderCollect.class);
        }

        List<OffLineReq.SynDataBean> myList = new ArrayList<>();
        myList.clear();

        if (l != null && l.size() > 0) {
            for (int i = 0; i < l.size(); i++) {
                OffLineReq.SynDataBean t = new OffLineReq.SynDataBean();
                WaterOrderCollect w = l.get(i);
                t.setTypeName(w.typeName);
                t.setId(w.id);
                t.setIsStaged(w.isStaged);
                t.setUseDegree(w.useDegree);
                t.setPictureUrl(w.getPictureUrl());
                t.setParentId(String.valueOf(w.getParentId()));
                t.setRemark(w.getRemark());
                t.setAccountBookId(w.getAccountBookId());
                t.setOrderType(w.getOrderType());
                t.setMoney(w.getMoney());
                t.setCreateDate(w.getCreateDate());
                t.setCreateName(w.getCreateName());
                t.setTypeId(w.getTypeId());
                t.setTypeName(w.getTypeName());
                t.setSpendHappiness(w.getSpendHappiness());
                t.setChargeDate(w.getChargeDate());
                t.setTypePid(w.getTypePid());
                t.setTypePname(w.getTypePname());
                t.setUpdateDate(w.getUpdateDate());
                t.setDelflag(w.getDelflag());
                t.setCreateBy(w.getCreateBy());
                t.setDelDate(w.getDelDate());
                t.setUpdateBy(w.getUpdateBy());
                t.setUpdateName(w.getUpdateName());
                t.setIcon(w.getIcon());
                t.setUserPrivateLabelId(w.getUserPrivateLabelId());
                t.setReporterAvatar(w.getReporterAvatar());
                t.setReporterNickName(w.getReporterNickName());
                t.setAbName(w.getAbName());
                t.setAssetsId(w.getAssetsId());
                t.setAssetsName(w.getAssetsName());
                myList.add(t);
            }
        }

        //本地数据库查找未上传数据 上传至服务器
        List<OffLine2Req.SynDataBean> myList2 = new ArrayList<>();
        myList2.clear();
        OffLine2Req req2 = new OffLine2Req();
        req2.setMobileDevice(req.getMobileDevice());
        req2.setSynDate(req.getSynDate());
        if (myList != null) {
            for (int i = 0; i < myList.size(); i++) {
                OffLineReq.SynDataBean s1 = myList.get(i);
                OffLine2Req.SynDataBean synDataBean = new OffLine2Req.SynDataBean();
                synDataBean.setId(s1.getId());
                synDataBean.setAccountBookId(s1.getAccountBookId());
                if (s1.getChargeDate() != null) {
                    synDataBean.setChargeDate(s1.getChargeDate().getTime());
                }
                synDataBean.setCreateBy(s1.getCreateBy());
                if (s1.getCreateDate() != null) {
                    synDataBean.setCreateDate(s1.getCreateDate().getTime());
                }
                synDataBean.setCreateName(s1.getCreateName());
                if (s1.getDelDate() != null) {
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
                if (s1.getUpdateDate() != null) {
                    synDataBean.setUpdateDate(s1.getUpdateDate().getTime());
                }
                synDataBean.setTypePname(s1.getTypePname());
                synDataBean.setUpdateBy(s1.getUpdateBy());
                synDataBean.setUseDegree(s1.getUseDegree());
                synDataBean.setUpdateName(s1.getUpdateName());
                synDataBean.setUserPrivateLabelId(s1.getUserPrivateLabelId());
                synDataBean.setAssetsId(s1.getAssetsId());
                synDataBean.setAssetsName(s1.getAssetsName());
                myList2.add(synDataBean);
            }
            req2.setSynData(myList2);
        }


        if (myList2.size() > 0) {
            req2.setSynData(myList2);
        }
        String jsonInfo = new Gson().toJson(req2);
        NetWorkManager.getInstance().setContext(MingXiInfoActivity.this).pushOffLineToFwq(jsonInfo, token, new NetWorkManager.CallBack() {
            @Override
            public void onSuccess(BaseReturn b) {
                ToastUtil.showShort("删除成功");
                finish();
            }

            @Override
            public void onError(String s) {
            }
        });
    }
}