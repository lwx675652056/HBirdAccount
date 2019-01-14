package com.hbird.base.mvc.fragement;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.hbird.base.R;
import com.hbird.base.mvc.activity.AskFriendsActivity;
import com.hbird.base.mvc.activity.BudgetActivity;
import com.hbird.base.mvc.activity.BudgetTimeSettingActivity;
import com.hbird.base.mvc.activity.ChooseAccountTypeActivity;
import com.hbird.base.mvc.activity.H5WebViewActivity;
import com.hbird.base.mvc.activity.InviteJiZhangActivity;
import com.hbird.base.mvc.activity.MemberManagerActivity;
import com.hbird.base.mvc.activity.MingXiInfoActivity;
import com.hbird.base.mvc.activity.MyZhangBenActivity;
import com.hbird.base.mvc.activity.NotificationMessageActivity;
import com.hbird.base.mvc.activity.homeActivity;
import com.hbird.base.mvc.adapter.IndexFragementAdapterOld;
import com.hbird.base.mvc.adapter.IndexGridViewAdapterOld;
import com.hbird.base.mvc.base.BaseFragement;
import com.hbird.base.mvc.bean.BaseReturn;
import com.hbird.base.mvc.bean.IndexFirends;
import com.hbird.base.mvc.bean.RequestBean.OffLine2Req;
import com.hbird.base.mvc.bean.RequestBean.OffLineReq;
import com.hbird.base.mvc.bean.ReturnBean.AccountMembersBean;
import com.hbird.base.mvc.bean.ReturnBean.GloableReturn;
import com.hbird.base.mvc.bean.ReturnBean.PullSyncDateReturn;
import com.hbird.base.mvc.bean.ReturnBean.WindowPopReturn;
import com.hbird.base.mvc.bean.ReturnBean.indexDatasReturn;
import com.hbird.base.mvc.bean.dayListBean;
import com.hbird.base.mvc.bean.indexBaseListBean;
import com.hbird.base.mvc.global.CommonTag;
import com.hbird.base.mvc.net.NetWorkManager;
import com.hbird.base.mvc.view.dialog.DialogUtils;
import com.hbird.base.mvc.view.dialog.IndexCommonDialogOld;
import com.hbird.base.mvc.view.dialog.InvitationFirendDialog;
import com.hbird.base.mvc.widget.HeaderViewPager;
import com.hbird.base.mvc.widget.IndicatorProgressBar;
import com.hbird.base.mvc.widget.MyTimerPop;
import com.hbird.base.mvc.widget.NewGuidePop;
import com.hbird.base.mvc.widget.TabRadioButton;
import com.hbird.base.mvc.widget.XListView;
import com.hbird.base.mvc.widget.waterwave_progress.WaterWaveProgress;
import com.hbird.base.mvp.model.entity.table.WaterOrderCollect;
import com.hbird.base.util.DBUtil;
import com.hbird.base.util.DateUtil;
import com.hbird.base.util.DateUtils;
import com.hbird.base.util.SPUtil;
import com.hbird.ui.detailed.ActAccountDetailed;
import com.hbird.util.Utils;
import com.ljy.devring.DevRing;
import com.ljy.devring.util.NetworkUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;

import butterknife.BindView;
import sing.util.AppUtil;
import sing.util.LogUtil;

import static com.hbird.base.R.id.waterwave_day;
import static com.hbird.base.app.constant.CommonTag.FIRST_COME_1_2_0;
import static com.hbird.base.app.constant.CommonTag.OFFLINEPULL_FIRST_LOGIN;
import static com.umeng.socialize.utils.ContextUtil.getPackageName;
import static java.lang.Integer.parseInt;

/**
 * Created by Liul(245904552@qq.com) on 2018/6/28.
 * 明细
 */

public class IndexFragementOld extends BaseFragement implements View.OnClickListener, XListView.IXListViewListener {
    @BindView(R.id.data_year)
    TextView dataYear;
    @BindView(R.id.data_month)
    TextView dataMonth;
    @BindView(R.id.layout_data)
    LinearLayout layoutData;
    @BindView(R.id.cash_surplus)
    TextView cashSurplus;
    @BindView(R.id.cash_compared)
    TextView cashCompared;
    /*@BindView(R.id.rl_top_btn)
    RelativeLayout mTopBtn;*/
    @BindView(R.id.xList)
    XListView lv;
    @BindView(R.id.iv_no_data)
    ImageView mNoData;
    @BindView(R.id.ll_nets)
    LinearLayout mNets;
    @BindView(R.id.waterWaveProgress1)
    WaterWaveProgress waveProgress;
    @BindView(R.id.ll_choose_budget)
    LinearLayout mBudget;
    @BindView(R.id.budget_money)
    TextView mBudgetMoney;
    @BindView(R.id.progress)
    IndicatorProgressBar mProgress;
    @BindView(R.id.budget_precent_money)
    TextView mPrecentMoney;
    @BindView(R.id.tv_budget_mod)
    TextView mBudgetMod;
    @BindView(R.id.waterwave_month)
    TextView mWaterMonth;
    @BindView(waterwave_day)
    TextView mWaterDay;
    @BindView(R.id.scrollableLayout)
    HeaderViewPager scrollableLayout;
    @BindView(R.id.ll_content)
    ScrollView mContents;
    @BindView(R.id.ll_about_top)
    LinearLayout views;
    @BindView(R.id.iv_editors)
    ImageView mEditors;
    @BindView(R.id.zhichu_jia)
    TextView mJia;
    @BindView(R.id.zhichu_jian)
    TextView mJian;
    @BindView(R.id.ll_zhangben)
    LinearLayout lZhangBen;
    @BindView(R.id.ab_moren)
    TextView mMoRen;
    @BindView(R.id.fl_times_visiable)
    FrameLayout mTimeVisiable;
    @BindView(R.id.fl_qiu_visiable)
    FrameLayout mQiuVisiable;
    @BindView(R.id.tv_time_tops)
    TextView mTimeTop;
    @BindView(R.id.tv_time_bottoms)
    TextView mTimeBottom;

    private ArrayList<String> dataY;
    private ArrayList<String> dataM;
    private int yyyy = 2018;
    private int mm = 8;
    private IndexFragementAdapterOld adapter;
    private dayListBean bean;
    private ArrayList<indexBaseListBean> dates = new ArrayList<>();
    //代表第一次进入页面
    private int ii = 0;
    private String token;
    private String mMonth;
    private int currentYY;
    private int currentMM;
    private indexDatasReturn.ResultBean indexResult;
    private MyTask task;
    private String accountId = "";
    private boolean isFirst;
    private String jzDays;
    private boolean comeInForLogin;
    private boolean timeB = false;
    private final int FIRST_LENGHT = 4000;
    private TabRadioButton jizhangs;
    private int popOnces = 0;//首页展示弹窗次数
    private int tanci = 0;
    private List<WindowPopReturn.ResultBean.ActivityBean> windowPop;
    private WindowPopReturn.ResultBean.InviteCountBean windowPopInvite;
    private boolean netTrue = false;
    private View vHead;
    private RelativeLayout mYaoQing;
    private LinearLayout mSettings;
    private LinearLayout mZhanKai;
    private LinearLayout mIndexYaoQing;
    private com.hbird.base.mvc.widget.MyGridView mGride;// 成员头像
    private ImageView mShouqi;
    private ImageView mIndexSet;// 設置按鈕
    private String typeBudget = "1";
    private String zhangbenId = "";
    private String abTypeId;

    @Override
    public int setContentId() {
        return R.layout.fragement_index_old;
    }

    @Override
    public void initView() {
        //数据库相关操作
        jizhangs = getActivity().findViewById(R.id.btn_jizhang);
        popOnces = 0;//重新执行则将其置为0
        vHead = View.inflate(getActivity(), R.layout.item_head_list, null);
        mYaoQing = vHead.findViewById(R.id.rl_yaoqing);
        mSettings = vHead.findViewById(R.id.ll_toux);
        mZhanKai = vHead.findViewById(R.id.ll_zhankai);
        mIndexYaoQing = vHead.findViewById(R.id.ll_index_yaoqing);
        mShouqi = vHead.findViewById(R.id.iv_shouqi);
        mIndexSet = vHead.findViewById(R.id.iv_index_setting);
        mGride = vHead.findViewById(R.id.mgv_banzi);

        adapter = new IndexFragementAdapterOld(getActivity(), IndexFragementOld.this, dates);
        lv.setAdapter(adapter);


        // 临时
        getActivity().findViewById(R.id.more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ActAccountDetailed.class);
                intent.putExtra("accountId",accountId);
                startActivity(intent);
            }
        });

        ApplicationInfo appInfo = null;
        try {
            appInfo = getActivity().getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String msg = appInfo.metaData.getString("UMENG_CHANNEL");
        ((Button)getActivity().findViewById(R.id.more)).setText(msg);
    }

    @Override
    public void initData() {
        zhangbenId = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_ID, "");
        LogUtil.e("zhangbenId:" + zhangbenId);
        if (TextUtils.isEmpty(zhangbenId)) {
            //账本id
            accountId = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, "");
            zhangbenId = accountId;
        }
        //账本id
        accountId = zhangbenId;
        LogUtil.e("accountId:" + accountId);

        waveProgress.setShowNumerical(false);
        yyyy = parseInt(DateUtils.getCurYear("yyyy"));
        currentYY = yyyy;
        dataYear.setText(yyyy + " 年");
        mMonth = DateUtils.date2Str(new Date(), "MM");
        String s = mMonth.substring(0, 2);
        mm = parseInt(s);
        currentMM = mm;
        dataMonth.setText(mMonth);

        typeBudget = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.INDEX_TYPE_BUDGET, "1");
        token = SPUtil.getPrefString(getActivity(), CommonTag.GLOABLE_TOKEN, "");
        LogUtil.e("token:" + token);
        //间隔上传时间
        String jiange = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.SYNINTERVAL, "");
        long prefLong = SPUtil.getPrefLong(getActivity(), com.hbird.base.app.constant.CommonTag.SYNDATE, 0);
        long time = new Date().getTime();
        if (prefLong != 0) {
            long l = time - prefLong;
            try {
                if (!TextUtils.isEmpty(jiange)) {
                    double v = Double.parseDouble(jiange);
                    if (l > v) {
                        //超过了最大间隔时间则自动上传
                        timeB = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        setInitData();
        //新手指引
        boolean firstGuide = SPUtil.getPrefBoolean(getActivity(), com.hbird.base.app.constant.CommonTag.APP_FIRST_ZHIYIN, true);
        if (firstGuide) {
            //第一次进入
            setNewGuide();
        }

        //设置listView 滑动监听相关 与头部相结合实现相互关联
        scrollableLayout.setCurrentScrollableContainer(() -> lv);

        //实现视觉差效果
        scrollableLayout.setOnScrollListener((currentY, maxY) -> mContents.setTranslationY(currentY / 2));

        //首页弹框接口
        if (getUserVisibleHint()) {
            setHomePage();
            netTrue = true;
        }
    }

    @Override
    public void initListener() {
        lv.setXListViewListener(this);
        lv.addHeaderView(vHead);
        layoutData.setOnClickListener(this);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                playVoice(R.raw.changgui02);
                boolean prefBoolean = SPUtil.getPrefBoolean(getActivity(), com.hbird.base.app.constant.CommonTag.INDEX_LISTVIEW_HEADER, false);
                int p = position;
                if (prefBoolean) {
                    p = p + 1;
                }
                if (p == 1) {
                    return;
                }
                if (dates.size() - 1 < (p - 2) || dates.get(p - 2).getTag() == 0) {
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(getActivity(), MingXiInfoActivity.class);
                intent.putExtra("ID", dates.get(p - 2).getId());
                startActivityForResult(intent, 101);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                playVoice(R.raw.changgui02);
                boolean prefBoolean = SPUtil.getPrefBoolean(getActivity(), com.hbird.base.app.constant.CommonTag.INDEX_LISTVIEW_HEADER, false);
                int p = position;
                if (prefBoolean) {
                    p = p + 1;
                }
                alertDialog(p);
                return true;
            }
        });
        mBudget.setOnClickListener(this);
        mTimeVisiable.setOnClickListener(this);
        lZhangBen.setOnClickListener(this);

        // 展开
        mZhanKai.setOnClickListener(view -> {
            mZhanKai.setVisibility(View.GONE);
            mIndexYaoQing.setVisibility(View.VISIBLE);
        });

        // 设置
        mIndexSet.setOnClickListener(view -> {
            playVoice(R.raw.changgui02);
            Intent intent = new Intent();
            intent.setClass(getActivity(), MemberManagerActivity.class);
            intent.putExtra("ID", zhangbenId);
            startActivity(intent);
        });

        // 邀请记账
        mYaoQing.setOnClickListener(view -> {
            playVoice(R.raw.changgui02);
            Intent intent = new Intent();
            intent.setClass(getActivity(), InviteJiZhangActivity.class);
            intent.putExtra("ID", zhangbenId);
            startActivity(intent);
        });

        // 收起
        mShouqi.setOnClickListener(view -> {
            playVoice(R.raw.changgui02);
            mZhanKai.setVisibility(View.VISIBLE);
            mIndexYaoQing.setVisibility(View.GONE);
        });
    }

    @Override
    public void loadData() {

    }

    @Override
    public void loadDate() {
        //com.hbird.base.util.L.liul("77777777777777777777777");
        getIndexInfo();
        if (popOnces >= 0) {
            //继续弹窗
            tanDialog(windowPop);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ii = ii + 1;
        if (ii > 1) {
            //com.hbird.base.util.L.liul("88888888888888888888888888888");
            //getHeadNetInfo();
            getIndexInfo();
            if (popOnces >= 0) {
                //继续弹窗
                tanDialog(windowPop);
            }
        }

    }

    @Override
    public boolean getUserVisibleHint() {
        return super.getUserVisibleHint();
    }

    @Override
    public void loadDataForNet() {
        super.loadDataForNet();
        //获取首页明细数据
        if (ii > 0) {
            return;
        }
        //下拉将数据同步到本地数据库 (如果是从登陆界面过来的 注册 或微信则需要重新拉数据 而非直接展示
//        com.hbird.base.util.L.liul("99999999999999999999999");
        comeInForLogin = SPUtil.getPrefBoolean(getActivity(), OFFLINEPULL_FIRST_LOGIN, false);
        isFirst = SPUtil.getPrefBoolean(getActivity(), com.hbird.base.app.constant.CommonTag.OFFLINEPULL_FIRST, true);

        // 因为前辈的数据库不能升级，现阶段数据库新增了字段，故这里废弃之前的数据库，强制同步最新数据到新数据库
        boolean mustUpload = SPUtil.getPrefBoolean(getActivity(), com.hbird.base.app.constant.CommonTag.MUST_UPDATE, false);

        if (NetworkUtil.isNetWorkAvailable(getActivity())) {
            if (isFirst || !mustUpload) {
                SPUtil.setPrefBoolean(getActivity(), com.hbird.base.app.constant.CommonTag.MUST_UPDATE, true);
                pullToSyncDate();
                SPUtil.setPrefBoolean(getActivity(), OFFLINEPULL_FIRST_LOGIN, false);
            } else {
                if (comeInForLogin) {
                    pullToSyncDate();
                } else {
                    //判断上次同步时间与本次登录的时间对比 大于一天则执行pull push操作
                    if (timeB) {
                        pullToSyncDate();
                    } else {
                        getIndexInfo();
                    }

                }
            }
        } else {
            getIndexInfo();
        }
        //获取头部预算 及记账天数
        //getHeadNetInfo();
    }

    private void pullToSyncDate() {
        //判断当前网络状态
        boolean netWorkAvailable = NetworkUtil.isNetWorkAvailable(getActivity());
        setCurrentNets();
        if (!netWorkAvailable) {
            return;
        }
        showGifProgress("");
        String mobileDevice = Utils.getDeviceInfo(getActivity());
        if (AppUtil.getVersionCode(getActivity()) < 10) {
            comeInForLogin = SPUtil.getPrefBoolean(getActivity(), OFFLINEPULL_FIRST_LOGIN, false);
        } else {
            boolean a = SPUtil.getPrefBoolean(getActivity(), FIRST_COME_1_2_0, false);
            if (!a) {// 主动更新一下数据库
                SPUtil.setPrefBoolean(getActivity(), FIRST_COME_1_2_0, true);
                comeInForLogin = true;
            }
        }

        NetWorkManager.getInstance().setContext(getActivity())
                .postPullToSyncDate(mobileDevice, comeInForLogin, token, new NetWorkManager.CallBack() {
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
                        SPUtil.setPrefLong(getActivity(), com.hbird.base.app.constant.CommonTag.SYNDATE, L);
                        List<PullSyncDateReturn.ResultBean.SynDataBean> synData = b1.getResult().getSynData();
                        //插入本地数据库
                        DBUtil.insertLocalDB(synData);
                        SPUtil.setPrefBoolean(getActivity(), com.hbird.base.app.constant.CommonTag.OFFLINEPULL_FIRST, false);
//                        List<WaterOrderCollect> list = DevRing.tableManager(WaterOrderCollect.class).loadAll();
//                        showMessage(list.size() + "");
                        hideGifProgress();
                        if (isFirst) {
                            getIndexInfo();
                            SPUtil.setPrefBoolean(getActivity(), OFFLINEPULL_FIRST_LOGIN, false);
                        } else {
                            comeInForLogin = SPUtil.getPrefBoolean(getActivity(), OFFLINEPULL_FIRST_LOGIN, false);
                            if (comeInForLogin) {
                                getIndexInfo();
                            } else {
                                pushOffLine();
                                getIndexInfo();
                            }
                        }
                        isFirst = false;
                        SPUtil.setPrefBoolean(getActivity(), OFFLINEPULL_FIRST_LOGIN, false);
                    }

                    @Override
                    public void onError(String s) {
                        hideProgress();
                        showMessage(s);
                    }
                });
    }

    private void getHeadNetInfo() {
        String mm2 = null;
        if (mm < 10) {
            mm2 = "0" + mm;
        } else {
            mm2 = mm + "";
        }
        final String t = yyyy + "-" + mm2;
        if (TextUtils.isEmpty(zhangbenId)) {
            setZhangBenAbout();
        } else {
            NetWorkManager.getInstance().setContext(getActivity())
                    .getIndexDatas(t, zhangbenId, token, new NetWorkManager.CallBack() {
                        @Override
                        public void onSuccess(BaseReturn b) {
                            indexDatasReturn indexData = (indexDatasReturn) b;
                            indexResult = indexData.getResult();
                            if (indexResult.getBudget() != null) {
                                String s = "";
                                if (indexResult.getBudget().getBudgetMoney() != null) {
                                    s = indexResult.getBudget().getBudgetMoney() + "";
                                }
                                SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.H5PRIMKEYMONEY, s);
                            }
                            String sql = "select count(day) from (select count(charge_date2) day  from " +
                                    "(select *,datetime(charge_date/1000, 'unixepoch', 'localtime') charge_date2 from WATER_ORDER_COLLECT where 1=1)" +
                                    " where account_book_id= '" + accountId + "' AND delflag = 0 AND strftime('%Y-%m', charge_date2) = '" + t + "' group by strftime('%Y-%m-%d', charge_date2)  );";
                            Cursor cursor = DevRing.tableManager(WaterOrderCollect.class).rawQuery(sql, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                jzDays = cursor.getString(0);

                            }
                            setZhangBenAbout();


                        }

                        @Override
                        public void onError(String s) {
                            setZhangBenAbout();
                        }
                    });
        }

    }

    private void setZhangBenAbout() {
        String moRenAcc = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT, "默认账本");
        mMoRen.setText(moRenAcc);
        //List list = DevRing.tableManager(WaterOrderCollect.class).loadAll();
        zhangbenId = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_ID, "");
        if (TextUtils.isEmpty(zhangbenId)) {
            // 表示这个地方是总账本
            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) mContents.getLayoutParams();
            linearParams.height = (int) getActivity().getResources().getDimension(R.dimen.height_4_80);
            mContents.setLayoutParams(linearParams);
            boolean prefBoolean = SPUtil.getPrefBoolean(getActivity(), com.hbird.base.app.constant.CommonTag.INDEX_LISTVIEW_HEADER, false);
            if (prefBoolean) {
                lv.removeHeaderView(vHead);
            } else {
                lv.removeHeaderView(vHead);
                //如果将此headview移除 则记录 再次点击时需要回复
                SPUtil.setPrefBoolean(getActivity(), com.hbird.base.app.constant.CommonTag.INDEX_LISTVIEW_HEADER, true);
            }
            //查询总账本的数据 并展示
        } else {
            boolean prefBoolean = SPUtil.getPrefBoolean(getActivity(), com.hbird.base.app.constant.CommonTag.INDEX_LISTVIEW_HEADER, false);
            if (prefBoolean) {
                lv.addHeaderView(vHead);
                SPUtil.setPrefBoolean(getActivity(), com.hbird.base.app.constant.CommonTag.INDEX_LISTVIEW_HEADER, false);
            } else {

            }
            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) mContents.getLayoutParams();
            linearParams.height = (int) getActivity().getResources().getDimension(R.dimen.height_19_80);
            mContents.setLayoutParams(linearParams);
            if (TextUtils.equals(typeBudget, "1")) {
                setWaveDate(indexResult);
                mTimeVisiable.setVisibility(View.GONE);
                mQiuVisiable.setVisibility(View.VISIBLE);
            } else {
                //场景账本
                setWaveDate(indexResult);
                mQiuVisiable.setVisibility(View.GONE);
                mTimeVisiable.setVisibility(View.VISIBLE);
                //设置场景账本 首页对应的时间
                if (null != indexResult && null != indexResult.getBudget()) {
                    if (indexResult.getBudget().getBeginTime() != null) {
                        String beginTime = DateUtils.getYearMonthDay(indexResult.getBudget().getBeginTime());
                        mTimeTop.setText(beginTime);
                    } else {
                        mTimeTop.setText("- - 年 - - 月 - - 日");
                    }
                    if (indexResult.getBudget().getEndTime() != null) {
                        String endTime = DateUtils.getYearMonthDay(indexResult.getBudget().getEndTime());
                        mTimeBottom.setText(endTime);
                    } else {
                        mTimeBottom.setText("- - 年 - - 月 - - 日");
                    }
                }
            }
        }
    }

    private void setWaveDate(indexDatasReturn.ResultBean result) {
        if (null == result || result.getCount() == null) {
            return;
        }

        int chargeDays = result.getCount().getChargeDays();
        int monthDays = result.getCount().getMonthDays();
        double i = 0;
        if (monthDays == 0) {
            i = 0;
        } else {
            i = Double.valueOf(chargeDays) / Double.valueOf(monthDays);
        }

        mWaterMonth.setText(mm + "月记账");
        mWaterDay.setText(jzDays + "/" + monthDays);
        //设置水波纹效果
        if (TextUtils.equals(typeBudget, "1")) {
            DecimalFormat dfs = new DecimalFormat("0");
            String isspre = dfs.format(i * 100);
            int issprecent = Integer.parseInt(isspre);
            if (issprecent > 0) {
                waveProgress.animateWave();
            }
            waveProgress.setProgress(issprecent);
            setGrade(waveProgress);
            waveProgress.setShowProgress(true);
        }


        if (result.getBudget() != null) {
            if (result.getBudget().getBudgetMoney() != null) {
                mBudgetMoney.setText(result.getBudget().getBudgetMoney() + "");
                String s = cashSurplus.getText().toString();
                Double i1 = Double.parseDouble(s);
                Double i3 = result.getBudget().getBudgetMoney();//预算
                DecimalFormat d = new DecimalFormat("######0.00");

                mBudgetMod.setText(d.format(i3 - i1) + " 元");
                double precents = 0;
                if (i3 != 0.0) {
                    precents = i1 / i3;
                } else {
                    precents = 10;//确定当0.0时 预算超额为999
                }

                DecimalFormat df = new DecimalFormat("0");
                String precentes = df.format(precents * 100);
                int precent = Integer.parseInt(precentes);
                if (precent > 999) {
                    precent = 999;
                } else if (precent < -999) {
                    precent = -999;
                }
                mPrecentMoney.setText(precent + "%");
                //计算日超额  预算/天数（月）  *最后记账天数  - 总支出   （判断是正还是负）
                Double budgetMoney = result.getBudget().getBudgetMoney();
                double pingjun = budgetMoney / monthDays;
                int sDays = 0;//最后记账天数
                if (dates == null || dates.size() == 0) {

                } else {
                    long dayTime = dates.get(0).getIndexBeen().get(0).getDayTime();
                    String Dayday = DateUtils.getMonthDayTodd(dayTime);
                    sDays = Integer.valueOf(Dayday);
                }
                if (pingjun * sDays - i1 < 0) {
                    mProgress.setProgressDrawable(getResources().getDrawable(R.drawable.index_progress_bg_yellow));
                    waveProgress.setmRingColor(getResources().getColor(R.color.text_F7D85B));

                } else {
                    mProgress.setProgressDrawable(getResources().getDrawable(R.drawable.index_progress_bg));
                    waveProgress.setmRingColor(getResources().getColor(R.color.bg_D1EEBB));
                }
                if (precent == 100) {
                    if (precents == 1) {
                        mProgress.setProgressDrawable(getResources().getDrawable(R.drawable.index_progress_bg_white));
                        waveProgress.setmRingColor(getResources().getColor(R.color.white));
                    } else {
                        mProgress.setProgressDrawable(getResources().getDrawable(R.drawable.index_progress_bg));
                        waveProgress.setmRingColor(getResources().getColor(R.color.bg_D1EEBB));
                    }

                } else if (precent > 100) {
                    mProgress.setProgressDrawable(getResources().getDrawable(R.drawable.index_progress_bg_yellow));
                    waveProgress.setmRingColor(getResources().getColor(R.color.text_F7D85B));
                }

                mProgress.setMax(100);
                setIpbValue(precent, mProgress, precent, precents);
                if (task != null) {
                    task.cancel(true);
                }
                task = new MyTask(mProgress);
                int money1 = (int) Math.round(precents);
                task.executeOnExecutor(Executors.newSingleThreadExecutor(), precent, money1);

            } else {
                mBudgetMoney.setText("- -");
                mBudgetMod.setText("- -" + " 元");
                mPrecentMoney.setText("- -" + "%");
                setIpbValue(0, mProgress, 0, 0);
                if (task != null) {
                    task.cancel(true);
                }
                task = new MyTask(mProgress);
                task.executeOnExecutor(Executors.newSingleThreadExecutor(), 0, 0);
            }
        } else {
            mBudgetMoney.setText("- -");
            mBudgetMod.setText("- -" + " 元");
            mPrecentMoney.setText("- -" + "%");
            setIpbValue(0, mProgress, 0, 0);
            if (task != null) {
                task.cancel(true);
            }
            task = new MyTask(mProgress);
            task.executeOnExecutor(Executors.newSingleThreadExecutor(), 0, 0);
        }
    }

    private void setNewGuide() {
        View cv = getActivity().getWindow().getDecorView();
        cv.post(new Runnable() {
            @Override
            public void run() {
                //改为popwindow
                final NewGuidePop pop = new NewGuidePop(getActivity(), jizhangs, "点击这里开始记账~", 2, new NewGuidePop.PopDismissListener() {
                    @Override
                    public void PopDismiss() {
                        SPUtil.setPrefBoolean(getActivity(), com.hbird.base.app.constant.CommonTag.APP_FIRST_ZHIYIN, false);
                    }
                });
                pop.showPopWindowToUp();
                new Handler().postDelayed(() -> {
                    if (pop != null) {
                        pop.hidePopWindow();
                    }
                }, FIRST_LENGHT);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_zhangben: // 选择账本
                playVoice(R.raw.changgui02);
                Intent intent2 = new Intent();
                intent2.setClass(getActivity(), MyZhangBenActivity.class);
                String id = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, "");
                intent2.putExtra("ID", id);
                startActivityForResult(intent2, 131);
                break;
            case R.id.layout_data:
                //yyyy,mm 代表选择年,月的默认当前传入值
                playVoice(R.raw.changgui02);
                new MyTimerPop(getActivity(), layoutData, dataY, dataM, yyyy - 2010, mm - 1, new MyTimerPop.OnDateListener() {
                    @Override
                    public void getYearList(String s) {
                        dataYear.setText(s + " 年");
                        yyyy = parseInt(s);
                    }

                    @Override
                    public void getMonthList(String s) {
                        String[] ss = s.split("月份");
                        dataMonth.setText(ss[0]);
                        mm = parseInt(ss[0]);
                        getIndexInfo();
                    }
                }, new MyTimerPop.PopDismissListener() {
                    @Override
                    public void PopDismiss() {
                    }
                }).showPopWindow();
                break;
            case R.id.fl_times_visiable: // 预算时间设置
                Intent intent = new Intent();
                intent.setClass(getActivity(), BudgetTimeSettingActivity.class);
                intent.putExtra("topTime", mTimeTop.getText().toString());
                intent.putExtra("endTime", mTimeBottom.getText().toString());
                intent.putExtra("accountBookId", zhangbenId);
                startActivityForResult(intent, 103);
                break;
            case R.id.rl_top_btn: //来记一笔
                playVoice(R.raw.jizhang);
                LogUtil.e("11");
                startActivity(new Intent(getActivity(), ChooseAccountTypeActivity.class));
                LogUtil.e("22");
                break;
            case R.id.ll_choose_budget:  //设置预算 预算界面
                playVoice(R.raw.changgui02);
                Intent intent3 = new Intent(getActivity(), BudgetActivity.class);
                intent3.putExtra("MONTH", mm + "");
                intent3.putExtra("YEAR", yyyy + "");
                if (indexResult.getBudget() != null) {
                    String s = "";
                    if (indexResult.getBudget().getBudgetMoney() != null) {
                        s = indexResult.getBudget().getBudgetMoney() + "";
                    }
                    intent3.putExtra("MONEY", s);
                }
                intent3.putExtra("accountBookId", zhangbenId);
                startActivity(intent3);
                break;
        }
    }


    private void getIndexInfo() {
        //查询指定月份记录
        String MonthFirstDay = DateUtil.getMonthday2First(yyyy, mm);
        String MonthLastDay = DateUtil.getMonthday2Last(yyyy, mm);
        String MonthLastDays = MonthLastDay.substring(0, MonthLastDay.length() - 3) + "000";
//        List list = DevRing.tableManager(WaterOrderCollect.class).loadAll();
        //String sql = "SELECT * FROM WATER_ORDER_COLLECT wo where wo.ACCOUNT_BOOK_ID=" + 80 + " AND wo.DELFLAG = 0 AND wo.CHARGE_DATE >=" + MonthFirstDay + " and wo.CHARGE_DATE<=" + MonthLastDay + " ORDER BY wo.CHARGE_DATE DESC,wo.CREATE_DATE DESC";
        //String sql = "SELECT wo.id,wo.money,wo.account_book_id,wo.order_type,wo.is_staged,wo.spend_happiness,wo.use_degree,wo.type_pid,wo.type_pname,wo.type_id,wo.type_name,wo.picture_url,wo.create_date,wo.charge_date,wo.remark, ( CASE wo.order_type WHEN 1 THEN st.icon WHEN 2 THEN it.icon ELSE NULL END ) AS icon FROM WATER_ORDER_COLLECT wo LEFT JOIN HBIRD_SPEND_TYPE st ON wo.type_id = st.id LEFT JOIN HBIRD_INCOME_TYPE it ON wo.type_id = it.id where wo.ACCOUNT_BOOK_ID=" + accountId + " AND wo.DELFLAG = 0 AND wo.CHARGE_DATE >=" + MonthFirstDay + " and wo.CHARGE_DATE<" + MonthLastDays + " ORDER BY wo.CHARGE_DATE DESC,wo.CREATE_DATE DESC";
        String sql = "";
        accountId = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, "");
        zhangbenId = accountId;
        if (TextUtils.isEmpty(accountId)) {
            Set<String> set = new LinkedHashSet<>();
            Set<String> prefSet = SPUtil.getPrefSet(getActivity(), com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID_ALL, set);
            if (prefSet != null) {
                String ids = "";
                Iterator<String> iterator = prefSet.iterator();
                while (iterator.hasNext()) {
                    String s = iterator.next();
                    ids += s + ",";
                }
                if (!TextUtils.isEmpty(ids)) {
                    String substring = ids.substring(0, ids.length() - 1);
                    sql = "SELECT  id, money, account_book_id, order_type, is_staged, spend_happiness, use_degree" +
                            ", type_pid, type_pname, type_id, type_name, picture_url, create_date, charge_date" +
                            ", remark, USER_PRIVATE_LABEL_ID, REPORTER_AVATAR, REPORTER_NICK_NAME,AB_NAME,icon FROM WATER_ORDER_COLLECT " +
                            " where  ACCOUNT_BOOK_ID in " + "(" + substring + ")" + " AND  DELFLAG = 0 " + "AND CHARGE_DATE >=" + MonthFirstDay + " and CHARGE_DATE<" + MonthLastDays + " ORDER BY  CHARGE_DATE DESC, CREATE_DATE DESC";
                }
            }
        } else {
            sql = "SELECT  id, money, account_book_id, order_type, is_staged, spend_happiness, use_degree" +
                    ", type_pid, type_pname, type_id, type_name, picture_url, create_date, charge_date" +
                    ", remark, USER_PRIVATE_LABEL_ID, REPORTER_AVATAR, REPORTER_NICK_NAME,AB_NAME,icon FROM WATER_ORDER_COLLECT " +
                    " where  ACCOUNT_BOOK_ID=" + accountId + " AND  DELFLAG = 0 " + "AND CHARGE_DATE >=" + MonthFirstDay + " and CHARGE_DATE<" + MonthLastDays + " ORDER BY  CHARGE_DATE DESC, CREATE_DATE DESC";

        }

        Cursor cursor = DevRing.tableManager(WaterOrderCollect.class).rawQuery(sql, null);

        List<WaterOrderCollect> dbList = new ArrayList<>();
        dbList.clear();
        if (null != cursor) {
            try {
                dbList = DBUtil.changeToList(cursor, dbList, WaterOrderCollect.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (null != dbList && dbList.size() > 0) {
            Collections.sort(dbList);
            Map<String, Object> dbDate = getDBDate(dbList);
            String json = new Gson().toJson(dbDate);
            dayListBean.ResultBean bean = new Gson().fromJson(json, dayListBean.ResultBean.class);

            if (bean != null) {
                dates = getDBDates(bean);
                if (dates != null && dates.size() > 0) {
                    //填充ListView 刷新界面
                    mNoData.setVisibility(View.GONE);
                    lv.setVisibility(View.VISIBLE);
                    double monthIncome = bean.getMonthIncome();
                    double monthSpend = bean.getMonthSpend();
                    String monthIncomes = getNumToNumber(monthIncome);
                    String monthSpends = getNumToNumber(monthSpend);
                    cashSurplus.setText(monthSpends);
                    if (monthSpend < 0) {
                        mJian.setVisibility(View.GONE);
                    } else {
                        mJian.setVisibility(View.VISIBLE);
                    }
                    if (monthIncome < 0) {
                        mJia.setVisibility(View.GONE);
                    } else {
                        mJia.setVisibility(View.VISIBLE);
                    }
                    cashCompared.setText(monthIncomes);
                    adapter = new IndexFragementAdapterOld(getActivity(), IndexFragementOld.this, dates);
                    lv.setAdapter(adapter);
                    lv.setPullLoadEnable(false);
                } else {
                    cashSurplus.setText("0.00");
                    cashCompared.setText("0.00");
                    mNoData.setVisibility(View.VISIBLE);
                    lv.setVisibility(View.GONE);
                }
            }
        } else {
            cashSurplus.setText("0.00");
            cashCompared.setText("0.00");
            if (dates != null) {
                dates.clear();
            } else {
                dates = new ArrayList<>();
            }
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
//            mNoData.setVisibility(View.VISIBLE);
//            lv.setVisibility(View.GONE);
            mNoData.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);

            lv.setPullLoadEnable(false);
        }
        getHeadNetInfo();
        //获取账本内组员情况 --->首页展示
        if (!TextUtils.isEmpty(zhangbenId)) {
            getAccountMembers();
        }
    }

    private void getAccountMembers() {
        NetWorkManager.getInstance().setContext(getActivity())
                .getAccountMember(token, zhangbenId, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        AccountMembersBean b1 = (AccountMembersBean) b;

                        String yourSelf = b1.getResult().getYourSelf();
                        String owner = b1.getResult().getOwner();
                        List<String> members = b1.getResult().getMembers();
                        LogUtil.e("空代表是管理員：" + owner);

                        ArrayList<String> list = new ArrayList<>();
                        list.clear();
                        if (yourSelf != null) {
                            list.add(yourSelf);
                        } else {
                            list.add("");// 你没有头像
                        }

                        if (members != null) {
                            list.addAll(members);
                        }

                        //初始化 邀请来的好友
                        if (list.size() > 1) {
                            ArrayList<IndexFirends> indexFirends = new ArrayList<>();

                            for (int i = 0; i < list.size(); i++) {
                                IndexFirends indexBean = new IndexFirends();
                                indexBean.setImgUrl(list.get(i));
                                indexFirends.add(indexBean);
                            }
                            IndexGridViewAdapterOld grideView = new IndexGridViewAdapterOld(getActivity(), IndexFragementOld.this, indexFirends);
                            mGride.setAdapter(grideView);
                        }

                        mZhanKai.setVisibility(View.GONE);// 展开要隐藏

                        if (null != owner) {//不是管理员  显示成员头像，不显示邀请，不显示设置
                            members.add(owner);
                            mIndexSet.setVisibility(View.GONE);
                            mIndexYaoQing.setVisibility(View.GONE);
                        } else {  //代表是管理员   显示邀请，显示设置
                            LogUtil.e(list.size() + "");

                            if (list.size() == 1) {// 只有自己
                                mIndexSet.setVisibility(View.GONE);
                                mSettings.setVisibility(View.GONE);
                                mIndexYaoQing.setVisibility(View.VISIBLE);
                            } else {
                                mIndexSet.setVisibility(View.VISIBLE);
                                mIndexYaoQing.setVisibility(View.GONE);
                                mSettings.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onError(String s) {

                    }
                });
    }

    private String getNumToNumber(Double d) {
        java.text.NumberFormat NF = java.text.NumberFormat.getInstance();
        NF.setGroupingUsed(false);//去掉科学计数法显示
        return NF.format(d);
    }

    private Map<String, Object> getDBDate(List<WaterOrderCollect> list) {
        //获取到当月所有记录
        Map<Date, Object> map = new LinkedHashMap<>();
        for (Iterator<WaterOrderCollect> it = list.iterator(); it.hasNext(); ) {
            WaterOrderCollect warter = it.next();
            //判断是否包含日期
            Date chargeDate = warter.getChargeDate();
            Date day = DateUtil.getDay(chargeDate);
            if (map.containsKey(day)) {
                ((ArrayList) map.get(day)).add(warter);
            } else {
                List<WaterOrderCollect> lists = new ArrayList<>();
                lists.add(warter);
                map.put(day, lists);
            }
        }
        Map<String, Object> ja = new HashMap();
        if (map.size() > 0) {
            //Map<Date, Object> resultMap = sortMapByKey(map);
            JSONArray array = new JSONArray();
            JSONArray array2 = new JSONArray();
            for (Map.Entry<Date, Object> entry : map.entrySet()) {
                //封装成key value格式
                JSONObject obj = new JSONObject();
                if (!TextUtils.equals(entry.getKey() + "", "dayTime")) {
                    obj.put("dayTime", entry.getKey());
                }

                if (!TextUtils.equals(entry.getKey() + "", "dayTime")) {
                    obj.put("dayArrays", entry.getValue());
                }
                array.add(JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue));
            }
            //获取日统计
            for (int i = 0; i < array.size(); i++) {
                JSONObject jsonObject = JSON.parseObject((String) array.get(i));
                if (!TextUtils.isEmpty(jsonObject.getString("dayArrays"))) {
                    BigDecimal dayIncome = new BigDecimal(0);
                    BigDecimal daySpend = new BigDecimal(0);
                    List lists = JSONArray.parseArray(jsonObject.getString("dayArrays"));
                    for (int j = 0; j < lists.size(); j++) {
                        //支出
                        WaterOrderCollect warter = JSONObject.parseObject(JSONObject.toJSONString(lists.get(j)), WaterOrderCollect.class);
                        if (warter.getOrderType() == 1) {
                            daySpend = daySpend.add(new BigDecimal(warter.getMoney()));
                        }
                        if (warter.getOrderType() == 2) {
                            dayIncome = dayIncome.add(new BigDecimal(warter.getMoney()));
                        }
                    }
                    jsonObject.put("dayIncome", dayIncome);
                    jsonObject.put("daySpend", daySpend);
                    array2.add(jsonObject);
                    //jsonObject转json
                }
            }
            //获取月份统计数据
            if (!TextUtils.isEmpty(accountId)) {
                Map<String, BigDecimal> account = getAccount(Integer.parseInt(accountId));
                ja.put("arrays", array2);
                ja.put("monthSpend", account.get("spend"));
                ja.put("monthIncome", account.get("income"));
                return ja;
            } else {
                Map<String, BigDecimal> account = getAccounts();
                ja.put("arrays", array2);
                ja.put("monthSpend", account.get("spend"));
                ja.put("monthIncome", account.get("income"));
                return ja;
            }

        }
        return ja;
    }

    public Map<String, BigDecimal> getAccount(int accountBookId) {
        Map<String, BigDecimal> listBySql = new HashMap<>();
        listBySql.clear();
        String MonthFirstDay = DateUtil.getMonthday2First(yyyy, mm);
        String MonthLastDay = DateUtil.getMonthday2Last(yyyy, mm);
        long l = System.currentTimeMillis();
        Date date = new Date(l);
        String sql = "SELECT SUM( CASE WHEN order_type = 1 THEN money ELSE 0 END) AS spend," +
                "SUM( CASE WHEN order_type = 2 THEN money ELSE 0 END) AS income FROM `WATER_ORDER_COLLECT`" +
                " WHERE charge_date >= '" + MonthFirstDay + "' and charge_date <= '" + MonthLastDay + "'" +
                " AND account_book_id = '" + accountBookId + "' AND delflag = 0;";
        Cursor cursor = DevRing.tableManager(WaterOrderCollect.class).rawQuery(sql, null);
        if (cursor != null) {
            int count = cursor.getCount();
            if (cursor.moveToFirst()) {
                String spend = cursor.getString(0);
                listBySql.put("spend", new BigDecimal(spend));
                String income = cursor.getString(1);
                listBySql.put("income", new BigDecimal(income));
            }
        }
        return listBySql;
    }

    public Map<String, BigDecimal> getAccounts() {
        Map<String, BigDecimal> listBySql = new HashMap<>();
        listBySql.clear();
        String MonthFirstDay = DateUtil.getMonthday2First(yyyy, mm);
        String MonthLastDay = DateUtil.getMonthday2Last(yyyy, mm);
        long l = System.currentTimeMillis();
        Date date = new Date(l);
        Set<String> sets = new LinkedHashSet<>();
        Set<String> prefSet = SPUtil.getPrefSet(getActivity(), com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID_ALL, sets);
        String sql = "";
        Cursor cursor = null;
        if (prefSet != null) {
            String ids = "";
            Iterator<String> iterator = prefSet.iterator();
            while (iterator.hasNext()) {
                String s = iterator.next();
                ids += s + ",";
            }
            String substring = ids.substring(0, ids.length() - 1);
            sql = "SELECT SUM( CASE WHEN order_type = 1 THEN money ELSE 0 END) AS spend," +
                    "SUM( CASE WHEN order_type = 2 THEN money ELSE 0 END) AS income FROM `WATER_ORDER_COLLECT`" +
                    " WHERE charge_date >= '" + MonthFirstDay + "' and charge_date <= '" + MonthLastDay + "'" +
                    " AND account_book_id in " + "(" + substring + ")" + " AND delflag = 0;";
            cursor = DevRing.tableManager(WaterOrderCollect.class).rawQuery(sql, null);
        }


        if (cursor != null) {
            int count = cursor.getCount();
            if (cursor.moveToFirst()) {
                String spend = cursor.getString(0);
                listBySql.put("spend", new BigDecimal(spend));
                String income = cursor.getString(1);
                listBySql.put("income", new BigDecimal(income));
            }
        }
        return listBySql;
    }

    private ArrayList<indexBaseListBean> getDates(dayListBean bean) {
        List<dayListBean.ResultBean.ArraysBean> arrays = bean.getResult().getArrays();
        if (arrays == null) return null;
        //数据处理 抽取到同一个集合indexBeans中去
        ArrayList<indexBaseListBean> been = new ArrayList<>();


        for (int i = 0; i < arrays.size(); i++) {
            List<dayListBean.ResultBean.ArraysBean.DayArraysBean> dayArrays = arrays.get(i).getDayArrays();
            indexBaseListBean indexBeans = new indexBaseListBean();
            if (dayArrays != null && dayArrays.size() > 0) {
                ArrayList<indexBaseListBean.indexBean> iBeen = new ArrayList<>();
                indexBeans.setDates(0, 0, "", "", 0, "", 0, 0, "", "", "", 0, 0, 0, "");
                indexBaseListBean.indexBean xBean = new indexBaseListBean.indexBean();
                xBean.setDayIncome(arrays.get(i).getDayIncome());
                xBean.setDaySpend(arrays.get(i).getDaySpend());
                xBean.setDayTime(arrays.get(i).getDayTime());
                iBeen.add(xBean);
                indexBeans.setIndexBeen(iBeen);
                been.add(indexBeans);
            }
            for (int j = 0; j < dayArrays.size(); j++) {
                indexBaseListBean indexBeans2 = new indexBaseListBean();
                dayListBean.ResultBean.ArraysBean.DayArraysBean dates = dayArrays.get(j);
                indexBeans2.setTag(1);//1为数据条目 （自定义标签）
                indexBeans2.setOrderType(dates.getOrderType());
                indexBeans2.setIcon(dates.getIcon());
                indexBeans2.setTypeName(dates.getTypeName());
                indexBeans2.setIsStaged(dates.getIsStaged());
                indexBeans2.setRemark(dates.getRemark());
                if (null != dates.getSpendHappiness()) {
                    indexBeans2.setSpendHappiness(dates.getSpendHappiness());
                }
                indexBeans2.setMoney(dates.getMoney());
                indexBeans2.setTypePid(dates.getTypePid());
                indexBeans2.setTypeId(dates.getTypeId());
                indexBeans2.setId(dates.getId());
                indexBeans2.setAccountBookId(dates.getAccountBookId());
                indexBeans2.setChargeDate(dates.getChargeDate());
                indexBeans2.setCreateDate(dates.getCreateDate());
                indexBeans2.setTypeName(dates.getTypeName());
                been.add(indexBeans2);
            }
        }
        return been;
    }


    private ArrayList<indexBaseListBean> getDBDates(dayListBean.ResultBean bean) {
        List<dayListBean.ResultBean.ArraysBean> arrays = bean.getArrays();
        if (arrays == null) return null;
        //数据处理 抽取到同一个集合indexBeans中去
        ArrayList<indexBaseListBean> been = new ArrayList<>();
        for (int i = 0; i < arrays.size(); i++) {
            List<dayListBean.ResultBean.ArraysBean.DayArraysBean> dayArrays = arrays.get(i).getDayArrays();
            indexBaseListBean indexBeans = new indexBaseListBean();
            if (dayArrays != null && dayArrays.size() > 0) {
                ArrayList<indexBaseListBean.indexBean> iBeen = new ArrayList<>();
                indexBeans.setDates(0, 0, "", "", 0, "", 0, 0, "", "", "", 0, 0, 0, "");
                indexBaseListBean.indexBean xBean = new indexBaseListBean.indexBean();
                xBean.setDayIncome(arrays.get(i).getDayIncome());
                xBean.setDaySpend(arrays.get(i).getDaySpend());
                xBean.setDayTime(arrays.get(i).getDayTime());
                iBeen.add(xBean);
                indexBeans.setIndexBeen(iBeen);
                been.add(indexBeans);
            }
            for (int j = 0; j < dayArrays.size(); j++) {
                indexBaseListBean indexBeans2 = new indexBaseListBean();
                dayListBean.ResultBean.ArraysBean.DayArraysBean dates = dayArrays.get(j);
                indexBeans2.setTag(1);//1为数据条目 （自定义标签）
                indexBeans2.setOrderType(dates.getOrderType());
                indexBeans2.setIcon(dates.getIcon());
                indexBeans2.setTypeName(dates.getTypeName());
                indexBeans2.setIsStaged(dates.getIsStaged());
                indexBeans2.setRemark(dates.getRemark());
                if (null != dates.getSpendHappiness()) {
                    indexBeans2.setSpendHappiness(dates.getSpendHappiness());
                }
                indexBeans2.setMoney(dates.getMoney());
                indexBeans2.setTypePid(dates.getTypePid());
                indexBeans2.setTypeId(dates.getTypeId());
                indexBeans2.setId(dates.getId());
                indexBeans2.setAccountBookId(dates.getAccountBookId());
                indexBeans2.setChargeDate(dates.getChargeDate());
                indexBeans2.setCreateDate(dates.getCreateDate());
                indexBeans2.setTypeName(dates.getTypeName());
                indexBeans2.setReporterAvatar(dates.getReporterAvatar());
                indexBeans2.setReporterNickName(dates.getReporterNickName());
                been.add(indexBeans2);
            }
        }
        return been;
    }

    //初始化 年（2010-2050），月（1-12）
    private void setInitData() {
        dataY = new ArrayList<>();
        dataY.clear();
        for (int i = 2010; i < 2051; i++) {
            dataY.add(i + "");
        }
        dataM = new ArrayList<>();
        dataM.clear();
        String temp = "月份";
        for (int i = 1; i < 13; i++) {
            dataM.add(i + temp);
        }
    }

    private void alertDialog(final int position) {
        new DialogUtils(getActivity())
                .builder()
                .setTitle("温馨提示")
                .setMsg("确认删除吗？")
                .setCancleButton("取消", view -> {
                })
                .setSureButton("删除", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        String id = dates.get(position - 2).getId();
////                        dates.get(position-1).get
//
//                        //数据库的操作 （删除显示的是 数据库的更新）
//                        Boolean b = DBUtil.updateOneDate(id, accountId);
//                        if (b) {
//                            //刷新界面数据
//                            getIndexInfo();
//                            //删除 并同步上传到服务器
//                            pullToSyncDate();
//                        }
                    }
                }).show();
    }

    @Override
    public void onRefresh() {
        getIndexInfo();
        //getHeadNetInfo();
        pullToSyncDate();
        onLoad();
    }

    private void pushOffLine() {
        OffLineReq req = new OffLineReq();
        String deviceId = Utils.getDeviceInfo(getActivity());
        req.setMobileDevice(deviceId);
        Long time = SPUtil.getPrefLong(getActivity(), com.hbird.base.app.constant.CommonTag.SYNDATE, new Date().getTime());
        String times = String.valueOf(time);
        req.setSynDate(times);

        //本地数据库查找未上传数据 上传至服务器
        String sql = "SELECT * FROM WATER_ORDER_COLLECT wo where wo.ACCOUNT_BOOK_ID = " + zhangbenId + " AND wo.UPDATE_DATE >= " + time;
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
                myList2.add(synDataBean);
            }
            req2.setSynData(myList2);
        }


        if (myList2 != null && myList2.size() > 0) {
            req2.setSynData(myList2);
        }
        String jsonInfo = new Gson().toJson(req2);
        setCurrentNets();
        NetWorkManager.getInstance().setContext(getActivity())
                .pushOffLineToFwq(jsonInfo, token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        GloableReturn b1 = (GloableReturn) b;
                        //更新同步成功 调用预算接口
                        getHeadNetInfo();
                    }

                    @Override
                    public void onError(String s) {
                    }
                });
    }

    private void setCurrentNets() {
        //判断当前网络状态
        boolean netWorkAvailable = NetworkUtil.isNetWorkAvailable(getActivity());
        if (!netWorkAvailable) {
            //网络不可用  添加一下判断的原因看下面
            if (null != mNets) {
                mNets.setVisibility(View.VISIBLE);
            }
        } else {
            //莫名其妙这个地方会报空指针异常 ？ 只能加此判断先（也只能是mNets可能为空了）
            if (null != mNets) {
                mNets.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onLoadMore() {

    }

    private void onLoad() {
        String time = DateUtil.formatDate("HH:mm", new Date());
        lv.setRefreshTime("今天:" + time);
        lv.stopRefresh();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 103 && resultCode == 104) {
            mTimeTop.setText(data.getStringExtra("topTime"));
            mTimeBottom.setText(data.getStringExtra("endTime"));
        }
        if (requestCode == 131 && resultCode == 130) {
            mMoRen.setText(data.getStringExtra("TITLE"));
            SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT, data.getStringExtra("TITLE"));
            zhangbenId = data.getStringExtra("ID");
            accountId = zhangbenId;
            SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_ID, zhangbenId);
            SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.ACCOUNT_BOOK_ID, zhangbenId);
            typeBudget = data.getStringExtra("typeBudget");
            abTypeId = data.getStringExtra("abTypeId");
            SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.INDEX_CURRENT_ACCOUNT_TYPE, data.getStringExtra("abTypeId"));
            SPUtil.setPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.INDEX_TYPE_BUDGET, data.getStringExtra("typeBudget"));
        }
    }

    private void setIpbValue(final int progress,
                             final IndicatorProgressBar ipb_progress,
                             final int total,
                             final double preTotal) {
        ipb_progress.setProgress(progress);
        ipb_progress.setProgressToal(total);
        ipb_progress.invalidate();
        ipb_progress.requestLayout();
    }

    private class MyTask extends AsyncTask<Integer, Integer, Object> {
        private IndicatorProgressBar idc;

        public MyTask(IndicatorProgressBar idc) {
            this.idc = idc;
        }

        @Override
        protected Void doInBackground(Integer... params) {
            try {
                for (int i = 0; i <= params[0]; i++) {
                    publishProgress(i, params[0], params[1]);
                    Thread.sleep(4);
                }
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progresses) {
            setIpbValue(progresses[0], idc, progresses[1], progresses[2]);
        }
    }

    /**
     * 动画加载水波纹进度
     */
    private int mysProgress;

    public void setGrade(final WaterWaveProgress watercircleview) {
        final int progress = watercircleview.getProgress();
        mysProgress = 1;
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x1223) {
                    watercircleview.setProgress(mysProgress * (1));
                } else if (msg.what == 0x1224) {
                    watercircleview.setProgress(progress);
                }
            }
        };
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                if (mysProgress < progress) {
                    msg.what = 0x1223;
                    mysProgress++;
                } else {
                    msg.what = 0x1224;
                    this.cancel();
                }
                handler.sendMessage(msg);
            }
        }, 0, 50);
    }

    private void setHasRed(String id, final int ii) {
        //首页公告已读接口
        NetWorkManager.getInstance().setContext(getActivity())
                .putHasRead(id, token, new NetWorkManager.CallBack() {
                    @Override
                    public void onSuccess(BaseReturn b) {
                        GloableReturn b1 = (GloableReturn) b;
                        dialog2TiaoZhuan(ii);
                    }

                    @Override
                    public void onError(String s) {
                        showMessage(s);
                    }
                });
    }

    private void setHomePage() {
        String version = Utils.getVersion(getActivity());
        NetWorkManager.getInstance().setContext(getActivity()).getHomeWindows(version, token, new NetWorkManager.CallBack() {
            @Override
            public void onSuccess(BaseReturn b) {
                WindowPopReturn b1 = (WindowPopReturn) b;
                windowPop = b1.getResult().getActivity();
                windowPopInvite = b1.getResult().getInviteCount();
                tanci = 0;
                //首页普通弹窗（跳转内链外链）
                if (null != windowPop) {
                    if (windowPop.size() > 0) {
                        popOnces = windowPop.size();
                        //先只展示第一条弹窗
                        final String id = windowPop.get(0).getId();
                        new IndexCommonDialogOld(getActivity(), IndexFragementOld.this)
                                .builder()
                                .setMsg(windowPop.get(0).getImage())
                                .setChaKan(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        popOnces = popOnces - 1;
                                        setHasRed(id, 0);
                                    }
                                })
                                .setCancleButton(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        popOnces = popOnces - 1;
                                        if (popOnces > 0) {
                                            //继续弹窗
                                            tanDialog(windowPop);
                                        } else {
                                            //是否要弹出好友推荐框
                                            tanHaoYouDialog();
                                        }
                                    }
                                }).show();
                    } else {
                        tanHaoYouDialog();
                    }
                }

            }

            @Override
            public void onError(String s) {

            }
        });
    }

    private void tanHaoYouDialog() {
        if (null != windowPopInvite) {
            if (windowPopInvite.getInviteCount() > 0) {
                //邀请好友的弹窗
                int inviteCount = windowPopInvite.getInviteCount();
                int inviteFriendsAware = windowPopInvite.getInviteFriendsAware();
                new InvitationFirendDialog(getActivity()).builder()
                        .setMsg(inviteCount + "", inviteFriendsAware + "")
                        .setChaKan(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                windowPopInvite.setInviteCount(0);//查看后现将其设置为0 再回来即使不请求接口 也不再调用此弹窗
                                String ids = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.H5PRIMKEYIDS, "");
                                String name = SPUtil.getPrefString(getActivity(), com.hbird.base.app.constant.CommonTag.H5PRIMKEYNAME, "");
                                playVoice(R.raw.changgui02);
                                //showMessage("邀请好友");
                                Intent intents = new Intent();
                                intents.setClass(getActivity(), AskFriendsActivity.class);
                                intents.putExtra("IDS", ids);
                                intents.putExtra("NAME", name);
                                startActivity(intents);
                            }
                        })
                        .setCancleButton(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                windowPopInvite.setInviteCount(0);//查看后现将其设置为0 再回来即使不请求接口 也不再调用此弹窗
                            }
                        }).show();
            }
        }
    }

    private void tanDialog(final List<WindowPopReturn.ResultBean.ActivityBean> windowPop) {
        tanci = tanci + 1;
        if (null == windowPop) {
            return;
        }
        int size = windowPop.size();
        if (tanci >= size) {
            //还要判断是否要弹出好友推荐框
            if (getUserVisibleHint()) {
                //显示出明细界面 并且 没有重新请求接口时调用（几率很小）
               /* if(!netTrue){
                    tanHaoYouDialog();
                }*/
                tanHaoYouDialog();
            }
            return;
        }
        final String id = windowPop.get(tanci).getId();
        new IndexCommonDialogOld(getActivity(), IndexFragementOld.this)
                .builder()
                .setMsg(windowPop.get(tanci).getImage())
                .setChaKan(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popOnces = popOnces - 1;
                        setHasRed(id, tanci);

                    }
                })
                .setCancleButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popOnces = popOnces - 1;
                        if (popOnces > 0) {
                            //继续弹窗
                            tanDialog(windowPop);
                        } else {
                            //是否要弹出好友推荐框
                            tanHaoYouDialog();
                        }
                    }
                }).show();
    }

    private void dialog2TiaoZhuan(int m) {
        String jumpType = windowPop.get(m).getJumpType();
        if (TextUtils.equals(jumpType, "0")) {
            //内链
            String connectionAddress = windowPop.get(m).getConnectionAddress();
            homeActivity activity;
            switch (connectionAddress) {
                case "mxsy":
                    playVoice(R.raw.changgui02);
                    activity = (homeActivity) getActivity();
                    activity.setTiaozhuanFragement(0, 0);
                    break;
                case "tbtj":
                    activity = (homeActivity) getActivity();
                    activity.setTiaozhuanFragement(1, 0);
                    break;
                case "tbfx":
                    activity = (homeActivity) getActivity();
                    activity.setTiaozhuanFragement(1, 1);
                    break;
                case "tbzc":
                    activity = (homeActivity) getActivity();
                    activity.setTiaozhuanFragement(1, 2);
                    break;
                case "jzsy":
                    playVoice(R.raw.changgui02);
                    startActivity(new Intent(getActivity(), ChooseAccountTypeActivity.class));
                    break;
                case "lppsy":
                    activity = (homeActivity) getActivity();
                    activity.setTiaozhuanFragement(2, 0);
                    break;
                case "fftz":
                    playVoice(R.raw.changgui02);
                    startActivity(new Intent(getActivity(), NotificationMessageActivity.class));
                    break;
            }

        } else if (TextUtils.equals(jumpType, "1")) {
            //外链
            String address = windowPop.get(m).getConnectionAddress();
            String shareTitle = windowPop.get(m).getShareTitle();
            String shareImage = windowPop.get(m).getShareImage();
            String shareContent = windowPop.get(m).getShareContent();
            Intent intent = new Intent();
            intent.setClass(getActivity(), H5WebViewActivity.class);
            intent.putExtra("URL", address);
            intent.putExtra("TITLE", shareTitle);
            intent.putExtra("SHAREIMAGE", shareImage);
            intent.putExtra("SHARECONTENT", shareContent);
            startActivity(intent);
        }
    }


}
